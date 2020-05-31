package com.w2aAPITestingFramework.rough;

import static io.restassured.RestAssured.given;

import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.w2a.APITestingFramework.pojo.Orders;
import com.w2a.APITestingFramework.pojo.PurchaseUnits;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class TestPayPal {

	static String access_token;
	static String client_id = "AUBh-_YTW0Xp-HRw4mw5RFaxoIGrKbLHIRgN92JZZyQZfiJjUvBtoA1wWrtCfj00LlMH2fA7bhLErtJq";
	static String secret = "EMn44tbOW-1aZdtMn1xuI5CYDBsFqVkZBvtvOHFzIExHvKKVY9RSXkL6FcVqiuOZgBuwZAv28dLUdMqz";
	static String orderId;
	
	@Test(priority = 1)
	public void getAuthKey() {
		
		RestAssured.baseURI = "https://api.sandbox.paypal.com/";
		
		Response response = 
		given()
		.param("grant_type", "client_credentials")
		.auth()
		.preemptive()
		.basic(client_id, secret)
		.post("v1/oauth2/token");
		
		response.prettyPrint();
		
		//access_token
		access_token = response.jsonPath().get("access_token").toString();
		System.out.println(access_token);
		
	}
	
	@Test(priority=2, dependsOnMethods = "getAuthKey")
	public void createOrder() {
		
		ArrayList<PurchaseUnits> list = new ArrayList <PurchaseUnits>();
		list.add(new PurchaseUnits("USD", "500.00"));
		
		Orders order = new Orders("CAPTURE", list);
		
/*		String jsonBody = "{\n" + 
				"  \"intent\": \"CAPTURE\",\n" + 
				"  \"purchase_units\": [\n" + 
				"    {\n" + 
				"      \"amount\": {\n" + 
				"        \"currency_code\": \"USD\",\n" + 
				"        \"value\": \"100.00\"\n" + 
				"      }\n" + 
				"    }\n" + 
				"  ]\n" + 
				"}";
*/		
		
		RestAssured.baseURI = "https://api.sandbox.paypal.com/";
		
		Response response = given()
		.contentType(ContentType.JSON)
		.auth()
		.oauth2(access_token)
		.body(order)
		.post("v2/checkout/orders");
		
		response.prettyPrint();
		
		Assert.assertEquals(response.jsonPath().get("status").toString(), "CREATED");
		orderId = response.jsonPath().get("id").toString();
	
	}
	
	@Test(priority = 3, dependsOnMethods = {"getAuthKey", "createOrder"})
	public void getOrder() {
		
		System.out.println("---Getting the Order---");
		
		RestAssured.baseURI = "https://api.sandbox.paypal.com/";
		
		Response response = given()
		.contentType(ContentType.JSON)
		.auth()
		.oauth2(access_token)
		.get("v2/checkout/orders"+ "/" +orderId);
		
		response.prettyPrint();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
	}
	
}
