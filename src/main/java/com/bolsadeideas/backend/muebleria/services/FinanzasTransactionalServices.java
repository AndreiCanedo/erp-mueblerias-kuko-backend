package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.dao.IMuebleriaDao;
import com.bolsadeideas.backend.muebleria.dao.ITransaccionDao;
import com.bolsadeideas.backend.muebleria.model.Muebleria;
import com.bolsadeideas.backend.muebleria.model.NaturalezaFinanciera;
import com.bolsadeideas.backend.muebleria.model.TipoReferencia;
import com.bolsadeideas.backend.muebleria.model.TipoTransaccion;
import com.bolsadeideas.backend.muebleria.model.Transaccion;

import jakarta.transaction.Transactional;

@Service
public class FinanzasTransactionalServices {
	
	@Autowired
	private ITransaccionDao transaccionDao;
	
	@Autowired
	private IMuebleriaDao muebleriaDao;
	
	private static final Long MUEBLERIA_ID = 1L;
	
	///////////////////////////////
	///////OBTENER MUEBLERIA///////
	///////////////////////////////
	
	private Muebleria getMuebleria() {
		return muebleriaDao.findById(MUEBLERIA_ID)
				.orElseThrow(() -> new RuntimeException("No existe Muebleria inicializada"));
	}

	///////////////////////////////
	///////////INGRESO/////////////
	///////////////////////////////
	
	@Transactional
	public void registrarIngresoInterno( BigDecimal monto, 
											String descripcion, 
											Long referenciaId,
											TipoReferencia referenciaTipo,
											String operacionId) {

		validarMonto(monto);
		
		validarOperacionId(operacionId);
		
		if(transaccionDao.existsByOperacionId(operacionId)) {
			return;	
		}
		
		Muebleria muebleria = getMuebleria();
		
		BigDecimal netoAnterior = valorSeguro(muebleria.getNeto());
		
		BigDecimal netoNuevo = netoAnterior.add(monto);
		
		//Crear Transaccion
		
		Transaccion t = crearTransaccion(
				monto,
				TipoTransaccion.INGRESO,
				netoAnterior,
				netoNuevo,
				descripcion,
				referenciaId,
				referenciaTipo,
				operacionId,
				NaturalezaFinanciera.INGRESO
				);
		
		
		//Actualizar Saldo
		//BigDecimal ingresos = Optional.ofNullable(muebleria.getTotalIngresos()).orElse(BigDecimal.ZERO);
		
		muebleria.setNeto(netoNuevo);
		muebleria.setTotalIngresos(valorSeguro(muebleria.getTotalIngresos()).add(monto));
		
		muebleriaDao.save(muebleria);
		transaccionDao.save(t);
	}
	
	///////////////////////////////
	////////////EGRESO/////////////
	///////////////////////////////
	
	@Transactional
	public void registrarEgresoInterno(BigDecimal monto, 
										String descripcion, 
										Long referenciaId,
										TipoReferencia referenciaTipo,
										String operacionId) {
		
		//validarMonto(monto);
		
		validarMonto(monto);
		
		validarOperacionId(operacionId);
		
		validarReferenciaId(referenciaId, referenciaTipo);
		
		if(transaccionDao.existsByOperacionId(operacionId)) {
			return;
		}
		
		Muebleria muebleria = getMuebleria();
		
		BigDecimal netoAnterior = valorSeguro(muebleria.getNeto());
		
		
		BigDecimal netoNuevo = netoAnterior.subtract(monto);
		
		//Crear Transaccion
		
		Transaccion t = crearTransaccion(
				monto,
				TipoTransaccion.EGRESO,
				netoAnterior,
				netoNuevo,
				descripcion,
				referenciaId,
				referenciaTipo,
				operacionId,
				NaturalezaFinanciera.EGRESO
				);
		
		
		muebleria.setNeto(netoNuevo);
		muebleria.setTotalEgresos(valorSeguro(muebleria.getTotalEgresos().add(monto)));
		
		muebleriaDao.save(muebleria);
		transaccionDao.save(t);
	}
	
	///////////////////////////////
	////////////AJUSTE/////////////
	///////////////////////////////
	
	@Transactional
	public void registrarAjusteInterno (BigDecimal monto, 
										String descripcion, 
										Long referenciaId, 
										TipoReferencia tipoReferencia,
										String operacionId) {
		
		if(monto == null || monto.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("El monto del ajuste no puede ser cero");
		}
		
		validarOperacionId(operacionId);
		
		if(transaccionDao.existsByOperacionId(operacionId)) {
			return;
		}
		
		Muebleria muebleria = getMuebleria();
		
		BigDecimal netoAnterior = valorSeguro(muebleria.getNeto());
		BigDecimal netoNuevo = netoAnterior.add(monto);
		
	
		
		NaturalezaFinanciera naturaleza =
		        monto.compareTo(BigDecimal.ZERO) > 0
		        ? NaturalezaFinanciera.INGRESO
		        : NaturalezaFinanciera.EGRESO;
		
		//Crear Transaccion
		
		Transaccion t = crearTransaccion(
				monto,
				TipoTransaccion.AJUSTE,
				netoAnterior,
				netoNuevo,
				descripcion,
				referenciaId,
				tipoReferencia,
				operacionId,
				naturaleza
				);
		
		
		muebleria.setNeto(netoNuevo);
		
		muebleriaDao.save(muebleria);
		transaccionDao.save(t);
	}
	
	//////////////////////////////////////
	////////////AJUSTE INGRESO////////////
	//////////////////////////////////////	
	
	@Transactional
	public void ajustarIngresoInterno(BigDecimal diferencia, 
										String descripcion, 
										Long referenciaId,
										TipoReferencia referenciaTipo,
										String operacionId) {
		
		if(diferencia == null || diferencia.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("La diferencia del Ingreso no puede ser cero");
		}
		
		validarOperacionId(operacionId);
		validarReferenciaId(referenciaId, referenciaTipo);

		if(transaccionDao.existsByOperacionId(operacionId)) return;
		
		Muebleria muebleria = getMuebleria();
		
		BigDecimal netoAnterior = valorSeguro(muebleria.getNeto());
		BigDecimal totalIngresoAnterior = valorSeguro(muebleria.getTotalIngresos());
		
		
	     //diferencia positiva:
	     //el gasto aumentó y el neto disminuye.
	     
	     //diferencia negativa:
	     //el gasto disminuyó y el neto aumenta.
		
		BigDecimal netoNuevo = netoAnterior.add(diferencia);
		
		BigDecimal totalIngresoNuevo = totalIngresoAnterior.add(diferencia);
		
		if(totalIngresoNuevo.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("El total de ingresos no puede ser negativo");
		}
		
		NaturalezaFinanciera naturaleza =
		        diferencia.compareTo(BigDecimal.ZERO) > 0
		        ? NaturalezaFinanciera.INGRESO
		        : NaturalezaFinanciera.EGRESO;
	     
		 Transaccion t = crearTransaccion(
				diferencia,
				TipoTransaccion.AJUSTE,
				netoAnterior,
				netoNuevo,
				descripcion,
				referenciaId,
				referenciaTipo,
				operacionId,
				naturaleza
		);		
		
		
		muebleria.setNeto(netoNuevo);
		muebleria.setTotalIngresos(totalIngresoNuevo);
		
		muebleriaDao.save(muebleria);
		transaccionDao.save(t);
		 
	}
	
	//////////////////////////////////////
	////////////AJUSTE EGRESO/////////////
	//////////////////////////////////////
	
	@Transactional
	public void ajustarEgresoInterno(BigDecimal diferencia, 
										String descripcion, 
										Long referenciaId,
										TipoReferencia referenciaTipo,
										String operacionId) {
		
		if(diferencia == null || diferencia.compareTo(BigDecimal.ZERO) == 0) {
			throw new IllegalArgumentException("La diferencia del egreso no puede ser cero");
		}
		
		validarOperacionId(operacionId);
		validarReferenciaId(referenciaId, referenciaTipo);

		if(transaccionDao.existsByOperacionId(operacionId)) return;
		
		Muebleria muebleria = getMuebleria();
		
		BigDecimal netoAnterior = valorSeguro(muebleria.getNeto());
		BigDecimal totalEgresoAnterior = valorSeguro(muebleria.getTotalEgresos());
		
		
	     //diferencia positiva:
	     //el gasto aumentó y el neto disminuye.
	     
	     //diferencia negativa:
	     //el gasto disminuyó y el neto aumenta.
		
		BigDecimal netoNuevo = netoAnterior.subtract(diferencia);
		
		BigDecimal totalEgresoNuevo = totalEgresoAnterior.add(diferencia);
		
		if(totalEgresoNuevo.compareTo(BigDecimal.ZERO) < 0) {
			throw new IllegalArgumentException("El total de egresos no puede ser negativo");
		}
		
		NaturalezaFinanciera naturaleza =
		        diferencia.compareTo(BigDecimal.ZERO) > 0
		        ? NaturalezaFinanciera.INGRESO
		        : NaturalezaFinanciera.EGRESO;
	     
		 Transaccion t = crearTransaccion(
				diferencia,
				TipoTransaccion.AJUSTE,
				netoAnterior,
				netoNuevo,
				descripcion,
				referenciaId,
				referenciaTipo,
				operacionId,
				naturaleza
		);		
		
		
		muebleria.setNeto(netoNuevo);
		muebleria.setTotalEgresos(totalEgresoNuevo);
		
		muebleriaDao.save(muebleria);
		transaccionDao.save(t);
		 
	}
	
	////////////////
	///VALIDACION///
	////////////////
	
	private void validarMonto(BigDecimal monto) {
		if(monto == null || monto.compareTo(BigDecimal.ZERO) <= 0) {
			throw new IllegalArgumentException("Monto inválido");
		}
	}
	
	private void validarOperacionId(String operacionId) {
		if(operacionId == null || operacionId.isBlank()) {
			throw new IllegalArgumentException("El identificador de operacion es obligatorio");
		}
	}
	
	private void validarReferenciaId(Long referenciaId, TipoReferencia referenciaTipo) {
		if(referenciaId == null) {
			throw new IllegalArgumentException("El identificador por referencia es obligatorio");
		}
		
		if(referenciaTipo == null) {
			throw new IllegalArgumentException("El tipo de Referencia es olbigatorio");
		}
	}
	
	private Transaccion crearTransaccion(
			BigDecimal monto,
			TipoTransaccion tipo,
			BigDecimal netoAnterior,
			BigDecimal netoNuevo,
	        String descripcion,
	        Long referenciaId,
	        TipoReferencia referenciaTipo,
	        String operacionId,
	        NaturalezaFinanciera naturaleza
	        
	) {
	    Transaccion t = new Transaccion();
	    t.setMonto(monto);
	    t.setTipo(tipo);
	    t.setNetoAnterior(netoAnterior);
	    t.setNetoNuevo(netoNuevo);
	    t.setDescripcion(descripcion);
	    t.setReferenciaId(referenciaId);
	    t.setReferenciaTipo(referenciaTipo);
	    t.setOperacionId(operacionId);
	    t.setNaturaleza(naturaleza);
	    return t;
	}
	
	private BigDecimal valorSeguro(BigDecimal valor) {
		return valor != null ? valor : BigDecimal.ZERO;
	}
	
}
