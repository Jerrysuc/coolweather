package com.igodiva.coolweather.util;

/**
 * Created by su11 on 2016/1/14.
 */
public interface HttpCallbackListener {
    void onFinish(String response);

    void onError(Exception e);
}
