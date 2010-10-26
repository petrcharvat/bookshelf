package com.pch.bm.amazon;

import com.pch.bm.amazon.ws.*;
import org.springframework.beans.factory.annotation.Required;

import javax.xml.ws.Holder;
import java.util.List;

public class AmazonSearchServiceImpl implements AmazonSearchService {

    protected AWSECommerceServicePortType binding;

    protected String accessKey;

    public List<Item> search(final ItemSearchRequest request) {

        final ItemSearch search = new ItemSearch();
        search.getRequest().add(request);
        search.setAWSAccessKeyId(accessKey);

        final Holder<List<Items>> items = new Holder<List<Items>>();

        binding.itemSearch(search.getMarketplaceDomain(),
                           search.getAWSAccessKeyId(),
                           search.getSubscriptionId(),
                           search.getAssociateTag(),
                           search.getXMLEscaping(),
                           search.getValidate(),
                           search.getShared(),
                           search.getRequest(),
                           null,
                           items);

        return items.value.get(0).getItem();
    }

    @Override
    public List<Item> lookup(final ItemLookupRequest request) {

        final ItemLookup search = new ItemLookup();
        search.getRequest().add(request);
        search.setAWSAccessKeyId(accessKey);

        final Holder<List<Items>> items = new Holder<List<Items>>();

        binding.itemLookup(search.getMarketplaceDomain(),
                           search.getAWSAccessKeyId(),
                           search.getSubscriptionId(),
                           search.getAssociateTag(),
                           search.getXMLEscaping(),
                           search.getValidate(),
                           search.getShared(),
                           search.getRequest(),
                           (Holder<OperationRequest>) null,
                           items);

        return items.value.get(0).getItem();
    }

    @Required
    public void setBinding(final AWSECommerceServicePortType binding) {
        this.binding = binding;
    }

    @Required
    public void setAccessKey(final String accessKey) {
        this.accessKey = accessKey;
    }
}
