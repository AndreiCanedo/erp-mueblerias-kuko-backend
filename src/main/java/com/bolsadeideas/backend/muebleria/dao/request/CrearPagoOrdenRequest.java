package com.bolsadeideas.backend.muebleria.dao.request;

import java.math.BigDecimal;

import com.bolsadeideas.backend.muebleria.model.FormaPago;

import lombok.Data;

@Data
public class CrearPagoOrdenRequest {

	private Long ordenId;
	private BigDecimal monto;
	private FormaPago formaPago;
	private String referencia;
	private String observaciones;
	
	
}
/*
  JSON EJEMPLO
{
  "ordenId": 15,
  "monto": 2000,
  "formaPago": "TRANSFERENCIA",
  "referencia": "TR-983452",
  "observaciones": "Primer pago del cliente"
}
*/