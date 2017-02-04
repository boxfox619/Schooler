package com.schooler.schoolerapplication.networking;

import com.schooler.schoolerapplication.networking.datamodel.HeaderProperty;
import com.schooler.schoolerapplication.networking.datamodel.Request;
import com.schooler.schoolerapplication.networking.datamodel.Response;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;


public class HttpBox {
	//HttpBox.post().setCommand(1234).putBodyData(JSonObject).push();

	//해당 url 수정 필요
	public static final String SERVER_URL = "http://localhost";

	public static Request post(){return new Request(SERVER_URL, Request.TYPE_GET);}
	public static Request post(String url) {
		return new Request(url,Request.TYPE_POST);
	}

	public static Response push(Request request) throws HttpBoxException,IOException {
		URL serverUrl = new URL(request.getUrl());
		HttpURLConnection urlConnection = (HttpURLConnection) serverUrl.openConnection();
		urlConnection.setRequestMethod(request.getType());

		for(int i = 0 ; i <request.getHeaderPropertiesSize(); i++){
			HeaderProperty property = request.getProperty(i);
			urlConnection.setRequestProperty(property.getKey(), property.getValue());
		}
		urlConnection.setDoOutput(true);

		BufferedWriter httpRequestBodyWriter = new BufferedWriter(
				new OutputStreamWriter(urlConnection.getOutputStream()));
		httpRequestBodyWriter.write(request.getBodyData());
		httpRequestBodyWriter.close();

		Response response = new Response(urlConnection.getResponseCode(), urlConnection.getResponseMessage());
		Scanner httpResponseScanner = new Scanner(urlConnection.getInputStream());
		while (httpResponseScanner.hasNextLine()) {
			response.appendBody(httpResponseScanner.nextLine());
		}
		httpResponseScanner.close();
		return response;
	}
}