package com.bolsadeideas.backend.muebleria.dao.request;

import lombok.Data;

@Data
public class CancelarPagoOrdenRequest {
	
	private String motivo;
	
}

/*
	JSON EJEMPLO
	{
  		"motivo": "La transferencia fue registrada dos veces"
	}
*/
