package com.bolsadeideas.backend.muebleria.response;


import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseRestObject <T>{
	
	private HttpStatus status;
	private String code;
	private String message;
	private T data;
	
}
