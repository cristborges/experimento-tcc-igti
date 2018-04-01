package com.cristborges.experimento_camel_tcc.gerenciamentoturma.smartclass;

public class ProcessarMatriculasAlunoResponse {

	private int responseCode;
	private String response;

	public ProcessarMatriculasAlunoResponse() {
	}

	public ProcessarMatriculasAlunoResponse(int responseCode, String response) {
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

	@Override
	public String toString() {
		return new StringBuilder("FulfillmentResponse { responseCode: ").append(responseCode)
				.append(", response=").append(response).append("}")
				.toString();
	}
}
