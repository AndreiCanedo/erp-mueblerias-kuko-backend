package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearPagoOrdenRequest;
import com.bolsadeideas.backend.muebleria.dtos.PagoOrdenDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IPagoOrdenServices {

	public ResponseEntity<ResponseRest<PagoOrdenDTO>> searchPagos();
	public ResponseEntity<ResponseRest<PagoOrdenDTO>> searchPagosByOrdenId(Long ordenId);
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> searchPagoById(Long id);
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> savePago(CrearPagoOrdenRequest request);
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> updateDatosPago(ActualizarPagoOrdenRequest request, Long id);
	public ResponseEntity<ResponseRestObject<PagoOrdenDTO>> cancelarPago(CancelarPagoOrdenRequest request, Long id);
	
}
