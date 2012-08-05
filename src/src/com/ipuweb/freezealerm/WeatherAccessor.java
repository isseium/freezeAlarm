package com.ipuweb.freezealerm;


import java.io.ByteArrayOutputStream;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.util.Log;

public class WeatherAccessor {
	public String apikey = "4bbe6c911e072823120508";
	
	/*
	 * 翌日の天気情報JSONを取得する
	 * 翌日の定義: 午前0〜3時は、当日とする
	 */
	public JSONArray getTommorowWeather(){
        String jsonText = "";
	    HttpClient httpClient = new DefaultHttpClient();
	    JSONArray weather = null;
	    
	    StringBuilder uri = new StringBuilder("http://free.worldweatheronline.com/feed/weather.ashx?q=Morioka,Japan&format=json&num_of_days=2&key=" + this.apikey);
	    HttpGet request = new HttpGet(uri.toString());
	    HttpResponse httpResponse = null;
	
	    try {
	        httpResponse = httpClient.execute(request);
	    } catch (Exception e) {
	         Log.d("TEST JSONSampleActivity", "Error Execute");
	         return null;
	    }
	    
	    int status = httpResponse.getStatusLine().getStatusCode();
	
	    if (HttpStatus.SC_OK == status) {
	        try {
	            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	            httpResponse.getEntity().writeTo(outputStream);
	            jsonText = outputStream.toString(); // JSONデータ
	            Log.d("Test DATA", jsonText);
	        } catch (Exception e) {
	            Log.d("JSONSampleActivity", "Error");
	        }
	    } else {
	        return null;
	    }
	    
	    // JSON パース
	    try {
	        JSONObject rootObject = new JSONObject(jsonText);
	        weather = rootObject.getJSONObject("data").getJSONArray("weather");
	    } catch (Exception e) {
	    	Log.e("ERROR", e.getMessage());
	    }
	    
	    return weather;
	}
}
