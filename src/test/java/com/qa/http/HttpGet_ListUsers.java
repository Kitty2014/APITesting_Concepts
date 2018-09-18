package com.qa.http;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;

import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.Test;

import com.google.gson.Gson;

public class HttpGet_ListUsers {

	@Test
	public void getListOfUsersDetails() throws ClientProtocolException, IOException, ParseException {
		CloseableHttpClient httpclient = HttpClients.createDefault(); // create http connection
		org.apache.http.client.methods.HttpGet httpGet = new org.apache.http.client.methods.HttpGet(
				"https://reqres.in/api/users"); // create httpRequest
		httpGet.addHeader("Content-Type", "application/json");

		CloseableHttpResponse httpResponse = httpclient.execute(httpGet); // executing the request

		StatusLine statusLine = httpResponse.getStatusLine(); // Status Line
		System.out.println("Status Line-->" + statusLine.toString());
		System.out.println("Status Code-->" + httpResponse.getStatusLine().getStatusCode());// status Code

		Header[] allHeaders = httpResponse.getAllHeaders();

		// Printing headers in response
		for (Header header : allHeaders) {
			System.out.println(header.getName() + "-->" + header.getValue());
		}

		String jsonString = null;

		// For Response Body
		HttpEntity responseEntity = httpResponse.getEntity();
		if (responseEntity != null) {
			jsonString = EntityUtils.toString(responseEntity, "UTF-8");
			System.out.println(jsonString);
		}

		// 1. Ignoring GSON API as it requires creation of POJO
		/*
		 * Gson g = new Gson(); Player p =g.fromJson(jsonString, Player.)
		 */

		// 2. With JSON Simple API
		JSONParser parser = new JSONParser();
		JSONObject jsonObject = (JSONObject) parser.parse(jsonString);

		// Printing single JSON object in response
		String totalPages = jsonObject.get("total_pages").toString();
		System.out.println(totalPages);

		// Printing JSON array in the respsone
		JSONArray dataArray = (JSONArray) jsonObject.get("data");

		for (int i = 0; i < dataArray.size(); i++) {
			JSONObject objects = (JSONObject) dataArray.get(i);

			Set keyValue = objects.keySet();
			Iterator keySet = keyValue.iterator();
			while (keySet.hasNext()) {
				String key = keySet.next().toString();
				System.out.println("Key : " + key + ", Value : " + objects.get(key));
			}
		}

		System.out.println("--------------------------------------------------");

		// getting user details
		JSONArray jsonArray = (JSONArray) jsonObject.get("data");

		// iterating phoneNumbers
		Iterator itr2 = jsonArray.iterator();

		while (itr2.hasNext()) {
			Iterator itr1 = ((Map) itr2.next()).entrySet().iterator();
			while (itr1.hasNext()) {
				Map.Entry pair = (Entry) itr1.next();
				System.out.println("Key : "+ pair.getKey() + " : " + pair.getValue());
			}
		}
		// 3. Ignoring Jackson API as it requires creation of POJO
		// Player ronaldo = new ObjectMapper().readValue(jsonString, Player.class);
	}
}