package com.skava.testSfoLogin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import org.springframework.web.client.HttpClientErrorException;

import com.skava.commerce.orchestration.client.api.CustomersApi;
import com.skava.commerce.orchestration.client.invoker.ApiClient;
import com.skava.commerce.orchestration.client.model.CustomerLoginRequest;
import com.skava.commerce.orchestration.client.model.CustomerLoginResponse;

public class TestSFOLogin {
	
/** 
 * logs in the user and gets the sessions ID for same 
 * 
 * @param identity
 * @param password
 * @param businessId
 * @param storeId
 * @return
 * @throws Exception 
 */
	public static String getSessionForUser(String identity, String password, String storeId, String apiKey) throws Exception
	{
		ApiClient apiClient = new ApiClient();
		apiClient.setBasePath("https://stratus.skavacommerce.com/orchestrationservices/storefront/");
		apiClient.addDefaultHeader("x-api-key",apiKey);
		apiClient.setDebugging(true);
        String xVersion = null;
        String locale = null;
        String xSkSessionId = null;
        CustomersApi customersApi = new CustomersApi(apiClient);
        CustomerLoginRequest body = new CustomerLoginRequest();
        body.setIdentity(identity);
        String hashedPw = HashPW.createJWHash(password);
        body.setPassword(hashedPw);
        body.setIdentityType(null);
        CustomerLoginResponse loginResponse =null;
        try 
          {
          loginResponse = customersApi.login(storeId, body, locale, xSkSessionId, xVersion);
          }
        catch (HttpClientErrorException e)
        {
        	 System.out.println("Error "+e.getRawStatusCode()+" when calling login");
             System.out.println(e.getResponseBodyAsString());
             return null;
        }
        String sessionForUser = loginResponse.getSessionId();
        System.out.println("Valid login, session id:"+sessionForUser);
        return sessionForUser;
	}


	public static void main(String[] args) throws Exception {
		System.out.println("Starting customer driver");
	   	String storeId = "132";
	   	Scanner scan = new Scanner(System.in);
	   	System.out.println("Enter api key:");
		String apiKey = scan.nextLine();
		System.out.println("Enter user identity email:");
		String identity = scan.nextLine();
		System.out.println("Enter password:");
		String password = scan.nextLine();
		
		String sessionId = getSessionForUser(identity,password,storeId, apiKey);
	}
	
	
}
