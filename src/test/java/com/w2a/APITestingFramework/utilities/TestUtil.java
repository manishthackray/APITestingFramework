package com.w2a.APITestingFramework.utilities;

import org.json.JSONObject;

import com.w2a.APITestingFramework.listeners.ExtentListeners;

public class TestUtil {

	public static boolean jsonHasKey(String json, String key) {
		
		JSONObject jsonObject = new JSONObject(json);
		ExtentListeners.testReport.get().info("validating the presence of key : " + key);
		
		return jsonObject.has(key);
	}
	
	public static String getJsonKeyValue(String json, String key) {
		
		JSONObject jsonObject = new JSONObject(json); 
		ExtentListeners.testReport.get().info("validating value of key : " + key);
		
		return jsonObject.get(key).toString();
	}
}
