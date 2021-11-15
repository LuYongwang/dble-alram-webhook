package io.github.luyongwang.dble.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.Map;

/**
 * 基于Gson封装工具
 *
 * @author yongwang.lu
 */
public final class JsonUtil {

    private static final Gson GSON = new GsonBuilder()
            // 对value为null的属性也进行序列化
            .serializeNulls()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    private JsonUtil() {
    }


    /**
     * 获取GsonBuilder实例
     *
     * @return
     */
    public static GsonBuilder builder() {
        return new GsonBuilder();
    }

    /**
     * 将对象转为json字符串
     *
     * @param object
     * @return
     */
    public static String toJson(Object object) {
        return GSON.toJson(object);
    }

    /**
     * 将json字符串转为指定类型的实例
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> T parse(String json, Class<T> cls) {
        return GSON.fromJson(json, cls);
    }

    /**
     * 将json转为Map
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> Map<String, T> toMap(String json) {
        return GSON.fromJson(json, new TypeToken<Map<String, T>>() {
        }.getType());
    }

    /**
     * 将json转为指定类型的List
     *
     * @param json
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> List<T> toList(String json, Class<T> cls) {
        // 根据泛型返回解析指定的类型,TypeToken<List<T>>{}.getType()获取返回类型
        return GSON.fromJson(json, new TypeToken<List<T>>() {
        }.getType());
    }

    /**
     * 将json转为Map List
     *
     * @param json
     * @param <T>
     * @return
     */
    public static <T> List<Map<String, T>> toMapList(String json) {
        return GSON.fromJson(json,
                new TypeToken<List<Map<String, T>>>() {
                }.getType());
    }

}