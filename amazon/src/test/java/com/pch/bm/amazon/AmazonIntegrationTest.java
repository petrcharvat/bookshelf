package com.pch.bm.amazon;

import com.pch.bm.amazon.ws.Item;
import com.pch.bm.amazon.ws.ItemLookupRequest;
import com.pch.bm.amazon.ws.ItemSearchRequest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springAmazon.xml"})
public class AmazonIntegrationTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private AmazonSearchService service;

    @Test
    public void testFindJEEBooks() {
        final ItemSearchRequest request = new ItemSearchRequest();
        request.setSearchIndex("Books");
        request.setKeywords("j2ee");

        final List<Item> items = service.search(request);
        Assert.assertNotNull(items);

        for (final Item item : items) {
            log.debug(item.getASIN() + " " + item.getItemAttributes().getTitle());
        }
    }

    @Test
    public void testFindByISBN() {
        final ItemLookupRequest request = new ItemLookupRequest();
//        request.setSearchIndex("Books");
        request.getItemId().add("1590594614");
        request.getItemId().add("0130449164");
        request.getResponseGroup().remove("Small");
        request.getResponseGroup().add("Medium");

        final List<Item> items = service.lookup(request);
        Assert.assertNotNull(items);

        for (final Item item : items) {
            log.debug(item.getASIN() + " " + item.getItemAttributes().getTitle());
        }
    }


}


