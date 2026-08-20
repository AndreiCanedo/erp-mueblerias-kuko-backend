package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.dao.ITransaccionDao;
import com.bolsadeideas.backend.muebleria.model.TipoReferencia;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FinanzasServices {
	
	@Autowired
	private ITransaccionDao transaccionDao;
	
	@Autowired
	private FinanzasTransactionalServices xServices;
	
	
	///////////////////////////////
	///////////INGRESO/////////////
	///////////////////////////////
	
	//Se separo registroIngreso con RegistroIngresoInterno ya que la funcion que lleva transactional no soporta retrys
	public void registrarIngreso(BigDecimal monto, String descripcion, Long referenciaId, TipoReferencia referenciaTipo) {
		
		String operacionId = UUID.randomUUID().toString();
		
		ejecutarConReintentos(operacionId, () -> xServices
													.registrarIngresoInterno(monto, 
																			descripcion, 
																			referenciaId, 
																			referenciaTipo, 
																			operacionId)
				);
	}
	
	
	
	///////////////////////////////
	////////////EGRESO/////////////
	///////////////////////////////
	public void registrarEgreso(BigDecimal monto, String descripcion, Long referenciaId, TipoReferencia referenciaTipo) {

		String operacionId = UUID.randomUUID().toString();
		
		ejecutarConReintentos(operacionId, () -> xServices
													.registrarEgresoInterno(monto, 
																			descripcion, 
																			referenciaId, 
																			referenciaTipo, 
																			operacionId)
				);
	}
	
	
	
	///////////////////////////////
	////////////AJUSTE/////////////
	///////////////////////////////

	public void registrarAjuste(BigDecimal monto, String descripcion, Long referenciaId, TipoReferencia referenciaTipo) {
		
		String operacionId = UUID.randomUUID().toString();
		
		ejecutarConReintentos(operacionId, () -> xServices
													.registrarAjusteInterno(monto, 
																			descripcion, 
																			referenciaId, 
																			referenciaTipo, 
																			operacionId)
				);
		
	}
	
	//////////////////////////////////////
	////////////AJUSTE INGRESO////////////
	//////////////////////////////////////
	
	public void ajusteIngreso(BigDecimal diferencia, String descripcion, Long referenciaId, TipoReferencia referenciaTipo) {
		
		String operacionId = UUID.randomUUID().toString();
		
		ejecutarConReintentos(operacionId, () -> xServices
													.ajustarIngresoInterno(diferencia, 
																			descripcion, 
																			referenciaId, 
																			referenciaTipo, 
																			operacionId)
				);
		
	}
	
	//////////////////////////////////////
	////////////AJUSTE EGRESO/////////////
	//////////////////////////////////////
	
	public void ajusteEgreso(BigDecimal diferencia, String descripcion, Long referenciaId, TipoReferencia referenciaTipo) {
		
		String operacionId = UUID.randomUUID().toString();
		
		ejecutarConReintentos(operacionId, () -> xServices
													.ajustarEgresoInterno(diferencia, 
																			descripcion, 
																			referenciaId, 
																			referenciaTipo, 
																			operacionId)
				);
}
	
	/*************************************************************************************/
	/*****************************CENTRALIZAR LOS INTENTOS**********************************/
	/*************************************************************************************/

	@FunctionalInterface
	private interface OperacionFinanciera{
		void ejecutar();
	}
	
	private void ejecutarConReintentos(String operacionId, OperacionFinanciera operacion) {
		
		int maxIntentos = 3;
		
		for(int intento = 1; intento <= maxIntentos; intento++) {
			try {
				operacion.ejecutar();
				return;
			}catch(ObjectOptimisticLockingFailureException e) {
				if(intento == maxIntentos) {
					throw e;
				}
				
				dormirAntesDeReintentar(intento);
			}catch(DataIntegrityViolationException e) {
				//Si la operacion ya existe, significa que un intento anterior 
				//probablemente alcanzo a registrarla
				
				if(transaccionDao.existsByOperacionId(operacionId)) {
					return;
				}
				
				throw e;
			}
		}
		
	}
	
	/***************************************************************************************/
	/***************************************HELPERS*****************************************/
	/***************************************************************************************/
	
	private void dormirAntesDeReintentar(int intento) {
		try {
			
			Thread.sleep(100L * intento);
			
		} catch(InterruptedException e) {
			
			Thread.currentThread().interrupt();
			
			throw new IllegalStateException("El reinicio Financiero Fue interrumpido", e);
		}
	}
	

}
