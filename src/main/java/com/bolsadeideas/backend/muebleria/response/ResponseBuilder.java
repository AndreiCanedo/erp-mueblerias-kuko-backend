package com.bolsadeideas.backend.muebleria.response;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseBuilder<T> {
	
	
	/////////////////////////////////////////////////////////////////////
	///////////////////////////RESPUESTA PARA LISTAS/////////////////////
	/////////////////////////////////////////////////////////////////////
	
	public static <T> ResponseEntity<ResponseRest<T>> buildResponse(
			HttpStatus status, String code, String message, List<T> data){
		return ResponseEntity.status(status).body(
				ResponseRest.<T>builder()
				.status(status)
				.code(code)
				.message(message)
				.data(data)
				.build()
				);
	}
	
	public static <T> ResponseEntity<ResponseRest<T>> buildSuccessResponse(List<T> data) {
        return buildResponse(HttpStatus.OK, "00", "Operación exitosa", data);
    }
    
    public static <T> ResponseEntity<ResponseRest<T>> buildErrorResponse(
            HttpStatus status, String code, String message) {
        return buildResponse(status, code, message, Collections.emptyList());
    }
    
    
    //////////////////////////////
    ///RESPUESTA CON PAGINACION///
    //////////////////////////////
    
    public static <T> ResponseEntity<ResponseRest<T>> buildPageResponse(
            List<T> data,
            int page,
            int size,
            long totalElements,
            int totalPages
    ) {
        return ResponseEntity.ok(
                ResponseRest.<T>builder()
                        .status(HttpStatus.OK)
                        .code("00")
                        .message("Operación exitosa")
                        .data(data)
                        .page(page)
                        .size(size)
                        .totalElements(totalElements)
                        .totalPages(totalPages)
                        .build()
        );
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////RESPUESTA PARA OBJETOS/////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    
    public static <T> ResponseEntity<ResponseRestObject<T>> buildResponseObject(
			HttpStatus status, String code, String message, T data){
		return ResponseEntity.status(status).body(
				ResponseRestObject.<T>builder()
				.status(status)
				.code(code)
				.message(message)
				.data(data)
				.build()
				);
	}
    
    public static <T> ResponseEntity<ResponseRestObject<T>> buildSuccessResponseObject(T data) {
        return buildResponseObject(HttpStatus.OK, "00", "Operación exitosa", 
            data);
    }
    
    public static <T> ResponseEntity<ResponseRestObject<T>> buildErrorResponseObject(
            HttpStatus status, String code, String message) {
        return buildResponseObject(status, code, message, null);
    }
    
}
