package com.igodiva.coolweather.util;

import android.text.TextUtils;

import com.igodiva.coolweather.model.City;
import com.igodiva.coolweather.model.CoolWeatherDB;
import com.igodiva.coolweather.model.County;
import com.igodiva.coolweather.model.Province;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

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
                                city.setCityCode(xmlPullParser.getAttributeValue(null, "url").substring(3, 9));
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
                                county.setCountyCode(xmlPullParser.getAttributeValue(null, "url").substring(3, 9));
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
}
