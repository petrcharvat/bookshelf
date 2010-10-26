package com.pch.bm.pdfextract;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.CharUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

public class IsbnExtractor {


    private final Logger log = LoggerFactory.getLogger(getClass());

    public String findIsbn(final File file) {

        PDDocument pdDocument = null;
        try {
            PDFTextStripper stripper = new PDFTextStripper();
            stripper.setStartPage(0);
            stripper.setEndPage(10);

//        final String x = "d:\\books\\Richard Monson-Haefel - J2EE Web Services.pdf";
//        final String x = "d:\\books\\Maven_definitive_guide.pdf";
//            final String x = "d:\\books\\Adapting Configuration Management for Agile Teams.pdf";

            log.debug(BooleanUtils.toStringYesNo(file.exists()));

            pdDocument = PDDocument.load(file);

            final StringWriter w = new StringWriter();
            stripper.writeText(pdDocument, w);
            w.flush();
            final String text = w.toString();

            log.debug(text);

            return findAndNormalizeIsbn(text);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            if (pdDocument != null) {
                try {
                    pdDocument.close();
                } catch (IOException e) {
                    log.error(e.getMessage(), e);
                }
            }
        }

        return null;
    }

    private static final String[] x = {
            "ISBN-10",
            "ISBN-13",
            "ISBN",
            "LSBN",
            "THISTITLEISALSOAVAILABLEINPRINTAS",
    };

    protected String findAndNormalizeIsbn(final String text) {
        final String u = StringUtils.remove(text.toUpperCase(), ' ');

        String str = null;

        for (int i = 0; i < x.length; i++) {
            String s = x[i];
            if (StringUtils.indexOf(u, s) >= 0) {
                str = StringUtils.substringAfter(u, s);
                break;
            }
        }

        if (str == null) {
            return null;
        }


        final StringBuilder result = new StringBuilder(13);
        for (int i = 0; result.length() < 13 && i < str.length(); i++) {
            final char c = str.charAt(i);
            if (CharUtils.isAsciiNumeric(c) || c == 'x' || c == 'X') {
                result.append(c);
            } else if (c!='-' && result.length() > 0) {
                break;
            }
        }

        return result.toString();
    }

}
