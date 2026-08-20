package com.bolsadeideas.backend.muebleria.model;

public enum TipoReferencia {
	//Nuevas Referencias
	PAGO_ORDEN,
	CANCELACION_PAGO_ORDEN,
	CANCELACION_EGRESO,
	DEVOLUCION_ORDEN,
	
	//Referencias actuales
	INGRESO,
    EGRESO,
    ORDEN,
    
    AJUSTE_UPDATE_EGRESO,
    AJUSTE_UPDATE_INGRESO,
    AJUSTE_DELETE_EGRESO,
    AJUSTE_DELETE_INGRESO,
    AJUSTE_MANUAL
}
