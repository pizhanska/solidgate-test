package com.solidgate.framework.helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.openqa.selenium.remote.http.HttpResponse;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.UUID;
import java.util.function.Supplier;

public class DataPrepHelper {
    private static JsonOperationsHelper jsonOperationsHelper = new JsonOperationsHelper();


    public static String getPaymentPageDataWithUniqueOrderId(String file) {
        JsonNode jsonNode = jsonOperationsHelper.readJsonNodeFromFile(file);
        JsonNode locatedNode = jsonNode.path("order");
        locatedNode = ((ObjectNode) locatedNode).put("order_id", UUID.randomUUID().toString());
        ((ObjectNode) jsonNode).set("oder", locatedNode);
        return jsonOperationsHelper.convertJsonNodeToString(jsonNode);
    }

    public static Supplier<InputStream> createContentSupplier(String content) {
        return () -> new ByteArrayInputStream(content.getBytes());
    }

    public static JsonNode getOrderNode(String data) {
        JsonNode jsonNode = jsonOperationsHelper.convertStringToJsonNode(data);
        return jsonNode.path("order");
    }

    public static String getOrderIdJsonString(String data) {
        return "{\"order_id\": " + getOrderNode(data).get("order_id").toString() + "}";
    }

    public static JsonNode getOrderNodeFromOrderStatusResponse(HttpResponse response) {
        return getOrderNode(response.toString().split(": ")[1]);
    }

}
