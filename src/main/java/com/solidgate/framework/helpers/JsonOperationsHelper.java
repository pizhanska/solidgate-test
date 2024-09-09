package com.solidgate.framework.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.NullNode;
import com.solidgate.framework.api.model.response.PaymentPageResponseModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;


public class JsonOperationsHelper {

    private final ObjectMapper objectMapper;
    private Logger logger = LoggerFactory.getLogger(JsonOperationsHelper.class);


    public JsonOperationsHelper() {
        this.objectMapper = new ObjectMapper();
    }

    public String convertJsonNodeToString(JsonNode jsonNode) {
        try {
            return objectMapper.writeValueAsString(jsonNode);
        } catch (IOException e) {
            logger.error("Failed to convert JsonNode to String", e);
            return "";
        }
    }


    public JsonNode convertStringToJsonNode(String json) {
        try {
            return objectMapper.readValue(json, JsonNode.class);
        } catch (IOException e) {
            logger.error("Failed to convert JsonNode to String", e);

        }
        return NullNode.getInstance();
    }


    public PaymentPageResponseModel paymentPageResponseJsonToObject(String jsonStringResp) {
        String response = jsonStringResp.split(": ")[1];
        PaymentPageResponseModel paymentPageResponseModel = new PaymentPageResponseModel();
        try {
            paymentPageResponseModel = objectMapper.readValue(response, PaymentPageResponseModel.class);
        } catch (IOException e) {
            logger.error("Can't map JSON string to Payment Page Response");
        }
        return paymentPageResponseModel;
    }

    private InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName + ".json");
    }

    public JsonNode readJsonNodeFromFile(String fileName) {
        try (InputStream in = getResourceAsStream(fileName)) {
            if (in == null) {
                String errorMsg = "Resource not found: " + fileName + ".json";
                logger.error(errorMsg);
                throw new IOException(errorMsg);
            }
            return objectMapper.readValue(in, JsonNode.class);
        } catch (IOException e) {
            logger.error("Failed to read JSON from file: " + fileName + ".json", e);
            return NullNode.getInstance();
        }
    }
}
