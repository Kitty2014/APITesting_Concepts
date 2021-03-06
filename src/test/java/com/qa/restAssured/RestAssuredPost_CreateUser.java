package com.qa.restAssured;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map.Entry;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.qa.parameters.CreateUser;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class RestAssuredPost_CreateUser {

	@Test
	public void createUser() throws IOException {

		// 1. Base URI
		RestAssured.baseURI = "https://reqres.in";

		// 2. CretingRest Client
		RequestSpecification restClient = RestAssured.given();

		HashMap<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("content-type", "application/json");

		for (Entry<String, String> entry : headerMap.entrySet()) {
			restClient.header(entry.getKey(), entry.getValue());
		}

		// 3. Creating object of User Class
		CreateUser userRequest = new CreateUser("Deepak", "Cleaning");

		// 4. Object Mapper to serialize Java Objects into JSON String
		ObjectMapper mapper = new ObjectMapper();
		String jsonStringRequest = mapper.writeValueAsString(userRequest);
		System.out.println("JSON string Request Payload-->" + jsonStringRequest);

		// 5. Passing JSON Payload in restClient
		restClient.body(jsonStringRequest);

		// 6. Rest Response
		Response restResponse = restClient.request(Method.POST, "/api/users");

		// 7. Status Code in response
		System.out.println("Status code-->" + restResponse.getStatusCode());

		// 8. Getting jsonStringResponse
		String jsonStringResponse = restResponse.getBody().asString();
		System.out.println("JSON String Response Payload-->" + jsonStringResponse);

		// 9. Deserialization fron JsonString to POJO
		CreateUser userResponse = mapper.readValue(jsonStringResponse, CreateUser.class);
		System.out.println("User id-->" + userResponse.getId());

		// 10. Comparing Name passed in Request and Name received in Response
		Assert.assertEquals(userRequest.getName(), userResponse.getName(), "Name does not match");
	}
}
