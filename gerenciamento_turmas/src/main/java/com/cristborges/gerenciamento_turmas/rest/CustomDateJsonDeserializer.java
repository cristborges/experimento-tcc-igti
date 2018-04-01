package com.cristborges.gerenciamento_turmas.rest;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class CustomDateJsonDeserializer extends JsonDeserializer<Date> {
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MMM dd, yyyy h:mm:ss a");

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String text = p.getText().trim();
		try {
			return simpleDateFormat.parse(text);
		} catch (ParseException e) {
			return ctxt.parseDate(text);
		}
	}
}
