package com.pch.bm.amazon;

import com.pch.bm.amazon.ws.Item;
import com.pch.bm.amazon.ws.ItemLookupRequest;
import com.pch.bm.amazon.ws.ItemSearchRequest;

import java.util.List;

public interface AmazonSearchService {
    List<Item> search(ItemSearchRequest request);

    List<Item> lookup(ItemLookupRequest request);
}
