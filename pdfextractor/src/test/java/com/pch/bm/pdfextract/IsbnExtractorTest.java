package com.pch.bm.pdfextract;

import org.junit.Assert;
import org.junit.Test;

public class IsbnExtractorTest {

    IsbnExtractor service = new IsbnExtractor();

    @Test
    public void testFindIsbn() throws Exception {
       Assert.assertEquals("345234235X", service.findAndNormalizeIsbn("dflkjsd isbn : 3-4-5--234-235-x dasdf"));
       Assert.assertEquals("0596529554", service.findAndNormalizeIsbn("dflk ls ISBN-10: 0-596-52955-4 sd" ));
       Assert.assertEquals("0596004087", service.findAndNormalizeIsbn("234 ISB N : 0-596-00408-7" ));
       Assert.assertEquals("0471385646", service.findAndNormalizeIsbn("This title is also available in print as 0-471-38564-6" ));
       Assert.assertEquals("9780470140925", service.findAndNormalizeIsbn("ISBN: 978-0-470-14092-5" ));
       Assert.assertEquals("0471462004", service.findAndNormalizeIsbn("lSBN 0-47 1-46200-4 " ));
       Assert.assertEquals("9780470057476", service.findAndNormalizeIsbn("and index.ISBN-13: 978-0-470-05747-6 (cloth : alk. paper)" ));
    }
}
