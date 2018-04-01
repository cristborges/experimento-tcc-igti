package com.cristborges.gerenciamento_turmas.rest;

public class GenericRestResponse {

	private int responseCode;
	private String response;

	public GenericRestResponse() {
	}

	public GenericRestResponse(int responseCode, String response) {
		super();
		this.responseCode = responseCode;
		this.response = response;
	}

	public int getResponseCode() {
		return responseCode;
	}

	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}
}
