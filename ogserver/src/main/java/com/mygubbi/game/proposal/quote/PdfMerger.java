package com.mygubbi.game.proposal.quote;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by shilpa on 14/7/17.
 */
public class PdfMerger{
    private Map<String,PdfNumber> inputPdfMap;
    private String mergedFileName;

    public PdfMerger(Map<String, PdfNumber> inputPdfMap, String mergedFileName) {
        this.inputPdfMap = inputPdfMap;
        this.mergedFileName = mergedFileName;
    }

    public Map<String, PdfNumber> getInputPdfMap() {
        return inputPdfMap;
    }

    public String getMergedFileName() {
        return mergedFileName;
    }

    public void setInputPdfMap(Map<String, PdfNumber> inputPdfMap) {
        this.inputPdfMap = inputPdfMap;
    }

    public void setMergedFileName(String mergedFileName) {
        this.mergedFileName = mergedFileName;
    }

    public void mergePdfFiles(){

        Document document = new Document();
        Map<PdfReader,PdfNumber> readers =  new LinkedHashMap<>();
        int totalPages = 0;

        //Create pdf Iterator object using inputPdfList.
        Iterator<String> pdfIterator = inputPdfMap.keySet().iterator();

        try {
            // Create reader list for the input pdf files.
            while (pdfIterator.hasNext()) {
                String inputFile = pdfIterator.next();
                PdfReader pdfReader = new PdfReader(new FileInputStream(inputFile));
                readers.put(pdfReader, inputPdfMap.get(inputFile));

                totalPages = totalPages + pdfReader.getNumberOfPages();
            }

            // Create writer for the outputStream
            OutputStream outputStream = new FileOutputStream(mergedFileName);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);

            //Open document.
            document.open();

            //Contain the pdf data.
            PdfContentByte pageContentByte = writer.getDirectContent();

            PdfImportedPage pdfImportedPage;
            int currentPdfReaderPage = 1;
            Iterator<PdfReader> iteratorPDFReader = readers.keySet().iterator();

            // Iterate and process the reader list.
            while (iteratorPDFReader.hasNext()) {
                PdfReader pdfReader = iteratorPDFReader.next();

                //Create page and add content.
                while (currentPdfReaderPage <= pdfReader.getNumberOfPages()) {
                    document.newPage();
                    writer.addPageDictEntry(PdfName.ROTATE, readers.get(pdfReader));
                    pdfImportedPage = writer.getImportedPage(pdfReader, currentPdfReaderPage);
                    pageContentByte.addTemplate(pdfImportedPage, 0, 0);
                    currentPdfReaderPage++;
                }
                currentPdfReaderPage = 1;
            }

            //Close document and outputStream.
            outputStream.flush();
            document.close();
            outputStream.close();

            System.out.println("Pdf files merged successfully.");
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}