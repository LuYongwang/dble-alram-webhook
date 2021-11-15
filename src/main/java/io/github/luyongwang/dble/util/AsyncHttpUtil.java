package io.github.luyongwang.dble.util;

import lombok.extern.slf4j.Slf4j;
import okhttp3.*;

import java.io.IOException;
import java.util.Map;

/**
 * 利用okhttp进行get和post的访问
 *
 * @author yongwang.lu
 */
@Slf4j
public class AsyncHttpUtil {


    private AsyncHttpUtil() {
    }

    /**
     * post请求
     *
     * @param url
     * @param reqData 提交的参数为key=value&key1=value1的形式
     */
    public static void asyncPost(String url, Map<String, Object> reqData) {
        OkHttpClient client = new OkHttpClient();
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = (new Request.Builder()).url(url).post(RequestBody.create(JsonUtil.toJson(reqData), mediaType)).build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                log.info("告警请求失败");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                log.info("告警请求成功 url:{} response:{}", url, result);
            }
        });
    }
}