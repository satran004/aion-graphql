package org.satran.blockchain.graphql.it;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.springframework.stereotype.Component;

@Component
public class TestUtil {

    public static String createRestBaseUrlWithPort(int port) {
        return "http://localhost:" + port + "/rest";
    }

    public static String createGraphQLURLWithPort(int port) {
        return "http://localhost:" + port  + "/graphql";
    }

    public static JsonArray getJSONArray(String json, String api, String method) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        JsonArray jsonArray = jsonElement.getAsJsonObject().get("data").getAsJsonObject()
            .get(api).getAsJsonObject()
            .get(method).getAsJsonArray();

        return jsonArray;
    }

    public static JsonObject getJsonObject(String json, String api, String method) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        JsonObject jsonObject = jsonElement.getAsJsonObject().get("data").getAsJsonObject()
            .get(api).getAsJsonObject()
            .get(method).getAsJsonObject();

        return jsonObject;
    }

    public static String getStringValue(String json, String api, String method) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        String result = jsonElement.getAsJsonObject().get("data").getAsJsonObject()
            .get(api).getAsJsonObject()
            .get(method).getAsString();

        return result;
    }

    public static JsonObject getJsonObject(String json) {
        JsonParser jsonParser = new JsonParser();
        JsonElement jsonElement = jsonParser.parse(json);

        JsonObject jsonObject = jsonElement.getAsJsonObject();

        return jsonObject;
    }
}
