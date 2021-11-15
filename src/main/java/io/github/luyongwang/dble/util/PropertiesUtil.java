package io.github.luyongwang.dble.util;

import com.google.gson.JsonObject;

import java.util.Properties;

/**
 * @author yongwang.lu
 */
public class PropertiesUtil {

    private PropertiesUtil() {
    }


    /**
     * Converts the properties object into a JSON string.
     *
     * @param properties The properties object to convert.
     * @return A JSON string representing the object.
     */
    public static JsonObject convertToJson(Properties properties) {
        JsonObject json = new JsonObject();
        for (Object key : properties.keySet()) {

            String baseKey = key.toString();
            String[] splittedKey = baseKey.split("\\.");

            JsonObject nestedObject = json;

            for (int i = 0; i < splittedKey.length - 1; ++i) {
                if (!nestedObject.has(splittedKey[i])) {
                    nestedObject.add(splittedKey[i], new JsonObject());
                }
                nestedObject = nestedObject.getAsJsonObject(splittedKey[i]);
            }
            nestedObject.addProperty(splittedKey[splittedKey.length - 1], properties.getProperty(baseKey));
        }
        return json;
    }

}