package com.mygubbi;

import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.*;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

/**
 * Created by shilpa on 14/7/17.
 */
public class MergePdfTest {
    public static void main(String args[]){
        try {

            Map<String,PdfNumber> inputPdfMap = new LinkedHashMap<>();
            inputPdfMap.put("/home/shilpa/Downloads/merge_pdf/Quotation.pdf",PdfPage.PORTRAIT);
            inputPdfMap.put("/home/shilpa/Downloads/merge_pdf/sow_checklist.pdf",PdfPage.LANDSCAPE);
            String fileOutputName = "/home/shilpa/Downloads/merge_pdf/merged_file.pdf";

//            PdfMerger merger = new PdfMerger(inputPdfMap,fileOutputName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
