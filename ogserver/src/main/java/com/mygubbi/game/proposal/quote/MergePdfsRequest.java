package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by shilpa on 14/7/17.
 */
public class MergePdfsRequest {

    private List<String> inputPdfs;
    private String mergedFileName;
    private final static Logger LOG = LogManager.getLogger(MergePdfsRequest.class);
    public MergePdfsRequest(List<String> pdfs,String mergedFileName){
        this.inputPdfs = pdfs;
        this.mergedFileName = mergedFileName;
    }


    public String getMergedFileName() {
        return mergedFileName;
    }

    public void setMergedFileName(String mergedFileName) {
        this.mergedFileName = mergedFileName;
    }

    public void mergePdfFiles(){
        try {
            Document PDFCombineUsingJava = new Document();
            PdfCopy copy = new PdfCopy(PDFCombineUsingJava, new FileOutputStream(mergedFileName));
            PDFCombineUsingJava.open();
            PdfReader ReadInputPDF;
            int number_of_pages;
            for (int i = 0; i < inputPdfs.size(); i++) {
                ReadInputPDF = new PdfReader(inputPdfs.get(i));
                number_of_pages = ReadInputPDF.getNumberOfPages();
                for (int page = 0; page < number_of_pages; ) {
                    copy.addPage(copy.getImportedPage(ReadInputPDF, ++page));
                }
            }
            PDFCombineUsingJava.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println(e);
        }
    }

}
