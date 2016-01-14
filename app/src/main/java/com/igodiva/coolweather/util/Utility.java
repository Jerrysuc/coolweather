package com.igodiva.coolweather.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;

import com.igodiva.coolweather.model.City;
import com.igodiva.coolweather.model.CoolWeatherDB;
import com.igodiva.coolweather.model.County;
import com.igodiva.coolweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by su11 on 2016/1/14.
 */
public class Utility {

    /**
     * 解析和处理服务器返回的省级数据
     * @param coolWeatherDB
     * @param response
     * @return
     */
    public synchronized static boolean handleProvincesResonpse(CoolWeatherDB coolWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String noteName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            if ("province".equals(noteName)) {
                                Province province = new Province();
                                province.setProvinceName(xmlPullParser.getAttributeValue(null, "quName"));
                                province.setProvinceCode(xmlPullParser.getAttributeValue(null, "code"));
                                province.setPyName(xmlPullParser.getAttributeValue(null, "pyName"));
                                coolWeatherDB.saveProvince(province);
                                LogUtil.d("Test", province.getProvinceCode() + " ---> " + province.getProvinceName());
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     * 解析和处理城市数据
     * @param coolWeatherDB
     * @param response
     * @return
     */
    public static boolean handleCityResponse(CoolWeatherDB coolWeatherDB, String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String noteName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            if ("city".equals(noteName)) {
                                City city = new City();
                                city.setCityName(xmlPullParser.getAttributeValue(null, "cityname"));
                                city.setCityCode(xmlPullParser.getAttributeValue(null, "url"));
                                city.setPyName(xmlPullParser.getAttributeValue(null, "pyName"));
                                city.setProvinceId(provinceId);
                                coolWeatherDB.saveCity(city);
                                LogUtil.d("Test", city.getCityCode() + " ---> " + city.getCityName());
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public static boolean handleCountyResponse(CoolWeatherDB coolWeatherDB, String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(response));
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    String noteName = xmlPullParser.getName();
                    switch (eventType) {
                        case XmlPullParser.START_TAG: {
                            if ("city".equals(noteName)) {
                                County county = new County();
                                county.setCountyName(xmlPullParser.getAttributeValue(null, "cityname"));
                                county.setCountyCode(xmlPullParser.getAttributeValue(null, "url"));
                                county.setPyName(xmlPullParser.getAttributeValue(null, "pyName"));
                                county.setCityId(cityId);
                                coolWeatherDB.saveCounty(county);
                                LogUtil.d("Test", county.getCountyCode() + " ---> " + county.getCountyName());
                            }
                            break;
                        }
                        default:
                            break;
                    }
                    eventType = xmlPullParser.next();
                }
                return true;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    /**
     *  解析服务器返回的JSON 数据，并将解析出的数据存储到本地
     * @param context
     * @param response
     */
    public static void handlerWeatherResponse(Context context, String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONObject weatherInfo = jsonObject.getJSONObject("weatherinfo");
            String cityName = weatherInfo.getString("city");
            String weatherCode = weatherInfo.getString("cityid");
            String temp1 = weatherInfo.getString("temp1");
            String temp2 = weatherInfo.getString("temp2");
            String weatherDesp = weatherInfo.getString("weather");
            String publishTime = weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2, weatherDesp, publishTime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences 文件中
     * @param context
     * @param cityName
     * @param weatherCode
     * @param temp1
     * @param temp2
     * @param weatherDesp
     * @param publishTime
     */
    private static void saveWeatherInfo(Context context, String cityName, String weatherCode, String temp1, String temp2, String weatherDesp, String publishTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日", Locale.CHINA);
        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }
}
