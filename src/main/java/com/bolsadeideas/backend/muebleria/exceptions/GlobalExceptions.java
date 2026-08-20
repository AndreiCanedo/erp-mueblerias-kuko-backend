package com.bolsadeideas.backend.muebleria.exceptions;


import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;


@RestControllerAdvice
public class GlobalExceptions {
	
    /************************************************************/
    /**************** EXCEPCIONES DE LA APLICACION **************/
    /************************************************************/
	
	// Manejo genérico para AppExceptions
    @ExceptionHandler(AppExceptions.class)
    public ResponseEntity<ResponseRest<Object>> handleAppExceptions(AppExceptions ex) {
        return ResponseBuilder.buildErrorResponse(ex.getHttpStatus(), 
            String.valueOf(ex.getHttpStatus().value()), ex.getMessage());
    }
    
    /************************************************************/
    /**************** VALIDACIONES @VALID ***********************/
    /************************************************************/
    
    // Manejo de errores de validación
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseRest<Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
        
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));
        
        return ResponseBuilder.buildErrorResponse(HttpStatus.BAD_REQUEST, "400", errorMessage);
    }
    
    /************************************************************/
    /**************** JSON / ENUM INVALIDO **********************/
    /************************************************************/
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ResponseRest<Object>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
    	
    	return ResponseBuilder.buildErrorResponse(
    			HttpStatus.BAD_REQUEST, "400", "Los datos enviados no tienen un formato válido"
    			);
    }
    
    /************************************************************/
    /********************* NO ENCONTRADO ************************/
    /************************************************************/
    @ExceptionHandler(MuebleriaNotFoundException.class)
    public ResponseEntity<ResponseRest<Object>> handleMuebleriaNotFound(
    		MuebleriaNotFoundException ex) {
    	return ResponseBuilder.buildErrorResponse(
    			HttpStatus.NOT_FOUND, "404", ex.getMessage());
    }
    
    
    /************************************************************/
    /****************** ERROR NO CONTROLADO  ********************/
    /************************************************************/

    // Manejo de errores inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseRest<Object>> handleGenericException(Exception ex) {
    	ex.printStackTrace();
        return ResponseBuilder.buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,
            "Error interno del servidor", "500");
    }

   
    
    
	
	
}
