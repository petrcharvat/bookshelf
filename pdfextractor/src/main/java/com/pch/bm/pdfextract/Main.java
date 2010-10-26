package com.pch.bm.pdfextract;

import com.pch.bm.amazon.AmazonSearchService;
import com.pch.bm.amazon.ws.Item;
import com.pch.bm.amazon.ws.ItemAttributes;
import com.pch.bm.amazon.ws.ItemLookupRequest;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {

        final ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext(
                new String[]{"springAmazon.xml"});

        final AmazonSearchService amazon = (AmazonSearchService) appContext.getBean("amazonService");

        final Collection<File> collection = FileUtils.listFiles(new File("d:\\temp\\Wiley"), new String[]{"pdf", "PDF"}, true);
        for (final File file : collection) {
            final IsbnExtractor extractor = new IsbnExtractor();

            final String isbn = extractor.findIsbn(file);
            System.out.println(file.getName() + "; isbn=" + isbn);
            if (isbn != null) {
                final ItemLookupRequest request = new ItemLookupRequest();
                request.getResponseGroup().remove("Small");
                request.getResponseGroup().add("Medium");
                request.getItemId().add(isbn);
                final List<Item> items = amazon.lookup(request);
                if (!items.isEmpty()) {
                    final Item item = items.get(0);

                    final ItemAttributes itemAttributes = item.getItemAttributes();
                    final String title =  StringUtils.replaceChars(itemAttributes.getTitle(), ":/\\", "---");


                    String newFile = itemAttributes.getPublisher() + " - " + StringUtils.left(itemAttributes.getPublicationDate(), 4) + " - " + title + ".pdf";
                    System.out.println(newFile);

                    final String path = FilenameUtils.getFullPath(file.getPath());
                    final File nw = new File(FilenameUtils.concat(path, newFile));




                    System.out.println(nw);

                    file.renameTo(nw);


//                System.out.println("asin = " + item.getASIN()+" author"+ itemAttributes.getAuthor()+" publisher="+itemAttributes.getPublisher()+" title="+itemAttributes.getTitle()+" "+itemAttributes.getPublicationDate());
                } else {
                    System.out.println("Not Found.");
                }
                System.out.println("");


            }

        }


// of course, an ApplicationContext is just a BeanFactory


//        final IsbnExtractor extractor = new IsbnExtractor();
//        final File file = new File("d:\\library\\computer\\publishers\\O'Reilly\\O'Reilly - 1998 - Learning The Bash Shell.pdf");
//        final String isbn = extractor.findIsbn(file);
//        System.out.println(file.getName() + " isbn = " + isbn);


//        PDFTextStripper stripper = new PDFTextStripper();

//        final String x = "d:\\books\\Richard Monson-Haefel - J2EE Web Services.pdf";
//        final String x = "d:\\books\\Maven_definitive_guide.pdf";
//        final String x = "d:\\books\\Adapting Configuration Management for Agile Teams.pdf";
//        final File file = new File(x);


//        final IsbnExtractor extractor = new IsbnExtractor();
//        final String isbn = extractor.findIsbn(file);
//        System.out.println("isbn = " + isbn);

    }
}
