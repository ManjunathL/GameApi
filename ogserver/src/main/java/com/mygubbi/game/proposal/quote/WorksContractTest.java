package com.mygubbi.game.proposal.quote;

import com.itextpdf.awt.geom.AffineTransform;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;

import java.io.*;

/**
 * Created by Shruthi on 10/27/2017.
 */
public class WorksContractTest
{
    public static final String SRC = "E:/WorkContract.pdf";
    public static final String DEST = "E:/WorkContractMergedPdf.pdf";

    public static void main(String[] args)
            throws IOException, DocumentException {
        File file = new File(DEST);
        file.getParentFile().mkdirs();
        //new WorksContractTest().manipulatePdf(SRC, DEST);
        FileInputStream fileInputStream=new FileInputStream(SRC);
        new WorksContractTest().splitIntoHalfPages(fileInputStream,file);

    }
    public void manipulatePdf(String src, String dest)
            throws IOException, DocumentException {
        // Creating a reader
        PdfReader reader = new PdfReader(src);
        // step 1
        Rectangle pagesize = new Rectangle(
                PageSize.A4.getWidth() * 4,
                PageSize.A4.getHeight() * 2);
        Document document = new Document(pagesize);
        // step 2
        PdfWriter writer
                = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        PdfContentByte canvas = writer.getDirectContent();
        float a4_width = PageSize.A4.getWidth();
        float a4_height = PageSize.A4.getHeight();
        int n = reader.getNumberOfPages();
        int p = 1;
        PdfImportedPage page;
        while ((p - 1) / 16 <= n / 16) {
            addPage(canvas, reader, p + 3, 0);
            addPage(canvas, reader, p + 12, a4_width);
            addPage(canvas, reader, p + 15, a4_width * 2);
            addPage(canvas, reader, p, a4_width * 3);
            AffineTransform at = AffineTransform.getRotateInstance(-Math.PI);
            at.concatenate(AffineTransform.getTranslateInstance(0, -a4_height * 2));
            canvas.saveState();
            canvas.concatCTM(at);
            addPage(canvas, reader, p + 4, -a4_width);
            addPage(canvas, reader, p + 11, -a4_width * 2);
            addPage(canvas, reader, p + 8, -a4_width * 3);
            addPage(canvas, reader, p + 7, -a4_width * 4);
            canvas.restoreState();
            document.newPage();
            addPage(canvas, reader, p + 1, 0);
            addPage(canvas, reader, p + 14, a4_width);
            addPage(canvas, reader, p + 13, a4_width * 2);
            addPage(canvas, reader, p + 2, a4_width * 3);
            canvas.saveState();
            canvas.concatCTM(at);
            addPage(canvas, reader, p + 6, -a4_width);
            addPage(canvas, reader, p + 9, -a4_width * 2);
            addPage(canvas, reader, p + 10, -a4_width * 3);
            addPage(canvas, reader, p + 5, -a4_width * 4);
            canvas.restoreState();
            document.newPage();
            p += 16;
        }
        // step 5
        document.close();
        reader.close();
    }

    public void addPage(PdfContentByte canvas,
                        PdfReader reader, int p, float x) {
        if (p > reader.getNumberOfPages()) return;
        PdfImportedPage page = canvas.getPdfWriter().getImportedPage(reader, p);
        canvas.addTemplate(page, x, 0);
    }

    void splitIntoHalfPages(InputStream source, File target) throws IOException, DocumentException
    {
        final PdfReader reader = new PdfReader(source);

        try (   OutputStream targetStream = new FileOutputStream(target)    )
        {
            Document document = new Document();
            PdfCopy copy = new PdfCopy(document, targetStream);
            document.open();

            for (int page = 1; page <= reader.getNumberOfPages(); page++)
            {
                PdfDictionary pageN = reader.getPageN(page);
                Rectangle cropBox = reader.getCropBox(page);
                PdfArray leftBox = new PdfArray(new float[]{cropBox.getLeft(), cropBox.getBottom(), (cropBox.getLeft() + cropBox.getRight()) / 2.0f, cropBox.getTop()});
                PdfArray rightBox = new PdfArray(new float[]{(cropBox.getLeft() + cropBox.getRight()) / 2.0f, cropBox.getBottom(), cropBox.getRight(), cropBox.getTop()});

                PdfImportedPage importedPage = copy.getImportedPage(reader, page);
                pageN.put(PdfName.CROPBOX, leftBox);
                copy.addPage(importedPage);
                pageN.put(PdfName.CROPBOX, rightBox);
                copy.addPage(importedPage);
            }

            document.close();
        }
        finally
        {
            reader.close();
        }
    }

}
