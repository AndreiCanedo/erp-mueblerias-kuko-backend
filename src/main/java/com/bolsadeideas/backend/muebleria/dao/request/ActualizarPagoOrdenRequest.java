package com.bolsadeideas.backend.muebleria.dao.request;

import com.bolsadeideas.backend.muebleria.model.FormaPago;

import lombok.Data;

@Data
public class ActualizarPagoOrdenRequest {

	private FormaPago formaPago;
	private String referencia;
	private String observaciones;
	
}


/*

	JSON EJEMPLO
	{
  		"formaPago": "TRANSFERENCIA",
  		"referencia": "TR-983452-CORREGIDA",
  		"observaciones": "Se corrigió la referencia bancaria"
	}
*/
