package com.bolsadeideas.backend.muebleria.services;

import org.springframework.http.ResponseEntity;

import com.bolsadeideas.backend.muebleria.dao.request.ActualizarOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CancelarOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dao.request.CrearOrdenCompraRequest;
import com.bolsadeideas.backend.muebleria.dtos.OrdenCompraDTO;
import com.bolsadeideas.backend.muebleria.response.ResponseRest;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

public interface IOrdenCompraServices {

	public ResponseEntity<ResponseRest<OrdenCompraDTO>> searchOrdenCompra();
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> searchOrdenCompraById(Long id);
	//public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> saveOrdenCompra(OrdenCompraDTO ordenCompraDao);
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> saveOrdenCompra(CrearOrdenCompraRequest request);
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> updateOrdenCompra(ActualizarOrdenCompraRequest request, Long id);
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> confirmarOrdenCompra(Long id);
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> deleteOrdenCompraById(Long id);
	public ResponseEntity<ResponseRestObject<OrdenCompraDTO>> cancelarOrdenCompra(CancelarOrdenCompraRequest request, Long id);
	
}
