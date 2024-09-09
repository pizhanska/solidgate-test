package com.solidgate.framework.api.model.response;

import java.util.UUID;

public class PaymentPageResponseModel {
    private String url;
    private UUID guid;

    public String getURL() {
        return url;
    }

    public void setURL(String value) {
        this.url = value;
    }

    public UUID getGUID() {
        return guid;
    }

    public void setGUID(UUID value) {
        this.guid = value;
    }
}
