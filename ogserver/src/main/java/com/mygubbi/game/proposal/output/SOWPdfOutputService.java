package com.mygubbi.game.proposal.output;

import com.mygubbi.common.LocalCache;
import com.mygubbi.common.VertxInstance;
import com.mygubbi.config.ConfigHolder;
import com.mygubbi.game.proposal.quote.MergePdfsRequest;
import com.mygubbi.game.proposal.quote.SowPdfRequest;

import com.mygubbi.si.gdrive.DriveFile;
import com.mygubbi.si.gdrive.DriveServiceProvider;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.json.JsonObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Created by shilpa on 13/7/17.
 */
public class SOWPdfOutputService extends AbstractVerticle {
    private final static Logger LOG = LogManager.getLogger(ProposalOutputService.class);

    public static final String CREATE_SOW_PDF_OUTPUT = "create.sowpdf.output";
    public static final String CREATE_MERGED_PDF_OUTPUT = "create.merged.output";
    public DriveServiceProvider serviceProvider;


    @Override
    public void start(Future<Void> startFuture) throws Exception
    {
        LOG.debug("Inside service: SOWPdfOutputService");
        this.setupSowPdfOutput();
        startFuture.complete();
    }

    @Override
    public void stop() throws Exception
    {
        super.stop();
    }

    private void setupSowPdfOutput(){
        LOG.debug("Inside service: setupSowPdfOutput");
        this.serviceProvider = new DriveServiceProvider();
        EventBus eb = VertxInstance.get().eventBus();
        eb.localConsumer(CREATE_SOW_PDF_OUTPUT, (Message<Integer> message) -> {
            SowPdfRequest sowPdfReq = (SowPdfRequest) LocalCache.getInstance().remove(message.body());
            /* read xls file name and location
                upload using google api and download pdf from there
                convert pdf to json object
             */
            String filePath = sowPdfReq.getXlsFileName();
            String fileDownloaded = sowPdfReq.getFileNameToUpload();
            String fileNameInDrive = sowPdfReq.getXlsFileNameInDrive();
            String userId = sowPdfReq.getUserId();
            LOG.info("file for Upload :: "+filePath+fileDownloaded);
            LOG.info("Default file :: "+fileNameInDrive);
            DriveFile file = this.serviceProvider.uploadFileForUser(filePath+fileDownloaded,userId,fileNameInDrive);
            System.out.println(file);
            String pdfToDownload = ConfigHolder.getInstance().getStringValue("sow_downloaded_pdf_fomat","sow.pdf");
            this.serviceProvider.downloadFile(file.getId(),filePath+pdfToDownload, DriveServiceProvider.TYPE_PDF);
            sendResponse(message, new JsonObject().put("sowPdfFile",filePath+pdfToDownload ));

        }).completionHandler(res -> {
            LOG.info("setupSowPdfOutput started." + res.succeeded());
        });

        eb.localConsumer(CREATE_MERGED_PDF_OUTPUT, (Message<Integer> message) -> {
            LOG.debug("Inside service: CREATE_MERGED_PDF_OUTPUT");
            MergePdfsRequest mergePdfReq = (MergePdfsRequest) LocalCache.getInstance().remove(message.body());
            mergePdfReq.mergePdfFiles();
            sendResponse(message, new JsonObject().put("quoteFile",mergePdfReq.getMergedFileName() ));
        }).completionHandler(res -> {
            LOG.info("setup PDf Merger Output started." + res.succeeded());
        });
    }
    private void sendResponse(Message message, JsonObject response)
    {
        message.reply(LocalCache.getInstance().store(response));
    }
}
