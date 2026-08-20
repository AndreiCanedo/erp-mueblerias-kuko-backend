package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dtos.OrdenDetalleDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IOrdenDetalleServices {
	
	public ResponseEntity<ResponseRest<OrdenDetalleDTO>> searchOrdenDetalle();
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> searchOrdenDetalleById(Long id);
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> saveOrdenDetalle(OrdenDetalleDTO ordenDetalleDao);
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> updateOrdenDetalle(OrdenDetalleDTO OrdenDetalleDao, Long id);
	public ResponseEntity<ResponseRestObject<OrdenDetalleDTO>> deleteOrdenDetalleById(Long id);

}
