package com.bolsadeideas.backend.muebleria.services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.bolsadeideas.backend.muebleria.dao.IOrdenCompraDao;
import com.bolsadeideas.backend.muebleria.dao.IPagoOrdenDao;
import com.bolsadeideas.backend.muebleria.dao.ITransaccionDao;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardCobranzaDTO;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardDTO;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardFinanzaPuntoDTO;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardKpisDTO;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardOrdenResumenDTO;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardOrdenesDTO;
import com.bolsadeideas.backend.muebleria.dtos.dashboard.DashboardPeriodoDTO;
import com.bolsadeideas.backend.muebleria.model.EstadoEntrega;
import com.bolsadeideas.backend.muebleria.model.EstadoOrdenCompra;
import com.bolsadeideas.backend.muebleria.model.EstadoPagoOrden;
import com.bolsadeideas.backend.muebleria.model.NaturalezaFinanciera;
import com.bolsadeideas.backend.muebleria.model.OrdenCompra;
import com.bolsadeideas.backend.muebleria.model.Proceso;
import com.bolsadeideas.backend.muebleria.model.Transaccion;
import com.bolsadeideas.backend.muebleria.response.ResponseBuilder;
import com.bolsadeideas.backend.muebleria.response.ResponseRestObject;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DashBoardServicesImpl implements IDashBoardServices{
	
	@Autowired
    private ITransaccionDao transaccionDao;

    @Autowired
    private IOrdenCompraDao ordenCompraDao;

    @Autowired
    private IPagoOrdenDao pagoOrdenDao;

	@Override
	public ResponseEntity<ResponseRestObject<DashboardDTO>> obtenerDashboard(LocalDate inicio, LocalDate fin) {
		
		try {
			
			String error = validarPeriodo(inicio, fin);
			
			if(error != null) {
				return ResponseBuilder.buildErrorResponseObject(HttpStatus.BAD_REQUEST, "400", error);
			}
			
			DashboardPeriodoDTO periodo = construirPeriodo(inicio, fin);
			
			LocalDateTime inicioFecha = inicio.atStartOfDay();
			LocalDateTime finFecha = fin.atTime(LocalTime.MAX);
			LocalDateTime inicioAnterior = periodo.getInicioAnterior().atStartOfDay();
			LocalDateTime finAnterior = periodo.getFinAnterior().atTime(LocalTime.MAX);
			
			DashboardKpisDTO kpis = construirKpis(
					inicioFecha,
					finFecha,
					inicioAnterior,
					finAnterior
					);
			
			DashboardCobranzaDTO cobranza =
					construirCobranza();
			
			DashboardDTO dashboard = new DashboardDTO();
			
			dashboard.setPeriodo(periodo);
			dashboard.setKpis(kpis);
			dashboard.setCobranza(cobranza);
			
			dashboard.setFinanzas(construirFinanzas(inicioFecha, finFecha));
			
			dashboard.setOrdenes(construirOrdenes());
			
			return ResponseBuilder.buildSuccessResponseObject(dashboard);
		
		} catch(Exception e) {
			log.error("Error al construir el dashboard para el periodo {} - {}: ", inicio, fin, e);

		    return ResponseBuilder.buildErrorResponseObject(HttpStatus.INTERNAL_SERVER_ERROR, "500", "Error en el Sistema");
		}
		
		
	}
	
	/********************************************************/
    /*********************** PERIODO ************************/
    /********************************************************/

    private DashboardPeriodoDTO construirPeriodo(LocalDate inicio, LocalDate fin) {
    	
    	//ChronoUnit, se usa para que tu pidas de 1 ags al 7 ags esta diferencia es 6 pero tu ocupas
    	//los 7 dias por eso se usa esto +1
        long cantidadDias = ChronoUnit.DAYS.between(inicio, fin) + 1;

        LocalDate finAnterior = inicio.minusDays(1);
        LocalDate inicioAnterior = finAnterior.minusDays(cantidadDias - 1);

        DashboardPeriodoDTO periodo = new DashboardPeriodoDTO();

        periodo.setInicio(inicio);
        periodo.setFin(fin);
        periodo.setInicioAnterior(inicioAnterior);
        periodo.setFinAnterior(finAnterior);

        return periodo;
    }

    /********************************************************/
    /************************ KPIS ****************************/
    /********************************************************/

    private DashboardKpisDTO construirKpis(
    		LocalDateTime inicio,
            LocalDateTime fin,
            LocalDateTime inicioAnterior,
            LocalDateTime finAnterior) {

        List<Transaccion> transaccionesActuales = transaccionDao.findByFechaBetween(inicio, fin);

        List<Transaccion> transaccionesAnteriores = transaccionDao.findByFechaBetween(inicioAnterior, finAnterior);

        BigDecimal ingresos = calcularMovimientos( transaccionesActuales, NaturalezaFinanciera.INGRESO);

        BigDecimal egresos = calcularMovimientos( transaccionesActuales, NaturalezaFinanciera.EGRESO);

        BigDecimal ingresosAnteriores = calcularMovimientos( transaccionesAnteriores, NaturalezaFinanciera.INGRESO);

        BigDecimal egresosAnteriores = calcularMovimientos(transaccionesAnteriores, NaturalezaFinanciera.EGRESO);

        BigDecimal ventas = calcularVentasPeriodo(inicio, fin);

        BigDecimal ventasAnteriores = calcularVentasPeriodo( inicioAnterior, finAnterior);

        BigDecimal saldoPendiente = calcularSaldoPendienteGeneral();

        long ordenesActivas = contarOrdenesActivas();

        DashboardKpisDTO kpis = new DashboardKpisDTO();

        kpis.setIngresos(ingresos);
        kpis.setEgresos(egresos);
        kpis.setBalance(ingresos.subtract(egresos));

        kpis.setVentas(ventas);
        kpis.setSaldoPendiente(saldoPendiente);
        kpis.setOrdenesActivas(ordenesActivas);

        kpis.setTendenciaIngresos(calcularTendencia(ingresos, ingresosAnteriores));

        kpis.setTendenciaEgresos(calcularTendencia(egresos, egresosAnteriores));

        kpis.setTendenciaVentas(calcularTendencia(ventas, ventasAnteriores));

        return kpis;
    }

    private BigDecimal calcularMovimientos(List<Transaccion> transacciones, NaturalezaFinanciera naturaleza) {

        return transacciones.stream()
            .filter(transaccion -> transaccion.getNaturaleza() == naturaleza)
            .map(Transaccion::getMonto)
            .filter(monto -> monto != null)
            .map(BigDecimal::abs)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /********************************************************/
    /************************ VENTAS ************************/
    /********************************************************/

    private BigDecimal calcularVentasPeriodo(LocalDateTime inicio, LocalDateTime fin) {

        return ordenCompraDao.findAll()
        		.stream()
                .filter(this::esVenta)
                .filter(orden -> orden.getFechaConfirmacion() != null)
                .filter(orden -> !orden.getFechaConfirmacion().isBefore(inicio) && !orden.getFechaConfirmacion().isAfter(fin))
                .map(OrdenCompra::getTotal)
                .map(this::valorSeguro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private boolean esVenta(OrdenCompra orden) {

        return orden.getEstadoOrden() == EstadoOrdenCompra.CONFIRMADA
            || orden.getEstadoOrden() == EstadoOrdenCompra.CERRADA
            || orden.getEstadoOrden() == EstadoOrdenCompra.FINALIZADA;
    }

    /********************************************************/
    /******************** SALDO PENDIENTE *******************/
    /********************************************************/

    private BigDecimal calcularSaldoPendienteGeneral() {

        return ordenCompraDao.findAll()
            .stream()
            .filter(this::esVenta)
            .map(orden -> {

                BigDecimal total = valorSeguro(orden.getTotal());

                BigDecimal pagado = pagoOrdenDao.sumMontoByOrdenIdAndEstado(orden.getId(), EstadoPagoOrden.APLICADO);

                BigDecimal pendiente = total.subtract(valorSeguro(pagado));

                return pendiente.max(BigDecimal.ZERO);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /********************************************************/
    /********************** COBRANZA ************************/
    /********************************************************/

    private DashboardCobranzaDTO construirCobranza() {

        List<OrdenCompra> ordenes = ordenCompraDao.findAll().stream().filter(this::esVenta).toList();

        BigDecimal totalVendido = ordenes.stream()
        		.map(OrdenCompra::getTotal)
                .map(this::valorSeguro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalCobrado = ordenes.stream()
                .map(orden -> pagoOrdenDao.sumMontoByOrdenIdAndEstado(orden.getId(), EstadoPagoOrden.APLICADO))
                .map(this::valorSeguro)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal saldoPendiente = totalVendido
                .subtract(totalCobrado)
                .max(BigDecimal.ZERO);

        BigDecimal porcentajeCobrado = calcularPorcentaje(totalCobrado, totalVendido);

        DashboardCobranzaDTO cobranza = new DashboardCobranzaDTO();

        cobranza.setTotalVendido(totalVendido);
        cobranza.setTotalCobrado(totalCobrado);
        cobranza.setSaldoPendiente(saldoPendiente);
        cobranza.setPorcentajeCobrado(porcentajeCobrado);

        return cobranza;
    }
    
    /********************************************************/
    /****************** GRAFICA FINANCIERA *******************/
    /********************************************************/

    private List<DashboardFinanzaPuntoDTO> construirFinanzas(LocalDateTime inicio, LocalDateTime fin) {

        List<Transaccion> transacciones = transaccionDao.findByFechaBetween(inicio, fin);
        
        //Map para que busque directamente el objeto que ocupo y linkedHashMap porque respeta el orden
        Map<LocalDate, DashboardFinanzaPuntoDTO> puntos = new LinkedHashMap<>();

        LocalDate fechaActual = inicio.toLocalDate();
        LocalDate fechaFinal = fin.toLocalDate();

        while (!fechaActual.isAfter(fechaFinal)) {

            DashboardFinanzaPuntoDTO punto = new DashboardFinanzaPuntoDTO();

            punto.setFecha(fechaActual);
            punto.setIngresos(BigDecimal.ZERO);
            punto.setEgresos(BigDecimal.ZERO);
            punto.setBalance(BigDecimal.ZERO);

            puntos.put(fechaActual, punto);

            fechaActual = fechaActual.plusDays(1);
        }

        for (Transaccion transaccion : transacciones) {

            if (transaccion.getFecha() == null || transaccion.getMonto() == null) {
                continue;
            }

            LocalDate fecha = transaccion.getFecha().toLocalDate();

            DashboardFinanzaPuntoDTO punto = puntos.get(fecha);

            if (punto == null) {
                continue;
            }

            BigDecimal monto = transaccion.getMonto().abs();

            if (transaccion.getNaturaleza() == NaturalezaFinanciera.INGRESO) {

                punto.setIngresos(punto.getIngresos().add(monto));

            } else if (transaccion.getNaturaleza() == NaturalezaFinanciera.EGRESO) {

                punto.setEgresos(punto.getEgresos().add(monto));
            }
        }

        puntos.values().forEach(punto -> punto.setBalance(punto.getIngresos().subtract(punto.getEgresos())));

        return new ArrayList<>(puntos.values());
    }
    
    /********************************************************/
    /***************** RESUMEN DE ORDENES ********************/
    /********************************************************/

    private DashboardOrdenesDTO construirOrdenes() {

        List<OrdenCompra> ordenesActivas = ordenCompraDao.findAll()
            .stream()
            .filter(this::esOrdenOperativa)
            .toList();

        long pendientes = ordenesActivas.stream()
            .filter(orden ->orden.getProceso() == Proceso.SIN_INICIAR).count();

        long produccion = ordenesActivas.stream()
            .filter(orden -> orden.getProceso() == Proceso.PRODUCCION).count();

        long listasParaEntregar = ordenesActivas.stream()
            .filter(this::estaListaParaEntregar).count();

        long atrasadas = ordenesActivas.stream()
            .filter(this::estaAtrasada).count();

        List<DashboardOrdenResumenDTO> proximas =
            ordenesActivas.stream()
                .filter(orden -> orden.getFechaEntrega() != null)
                .filter(orden -> orden.getEstadoEntrega() != EstadoEntrega.ENTREGADA)
                .sorted(Comparator.comparing(OrdenCompra::getFechaEntrega))
                .limit(8)
                .map(this::convertirOrdenResumen)
                .toList();

        DashboardOrdenesDTO resumen = new DashboardOrdenesDTO();

        resumen.setPendientes(pendientes);
        resumen.setProduccion(produccion);
        resumen.setListasParaEntregar(listasParaEntregar);
        resumen.setAtrasadas(atrasadas);
        resumen.setProximas(proximas);

        return resumen;
    }
    
    /********************************************************/
    /*************** CONVERTIR ORDEN RESUMEN ****************/
    /********************************************************/
    
    private DashboardOrdenResumenDTO convertirOrdenResumen(OrdenCompra orden) {

        DashboardOrdenResumenDTO dto = new DashboardOrdenResumenDTO();

        dto.setId(orden.getId());

        if (orden.getCliente() != null) {

            dto.setClienteId(orden.getCliente().getId());

            dto.setCliente(orden.getCliente().getNombre());

        } else {

            dto.setClienteId(null);
            dto.setCliente("Cliente no disponible");
        }

        dto.setTotal(valorSeguro(orden.getTotal()));

        dto.setFechaEntrega(orden.getFechaEntrega());

        dto.setEstadoOrden(orden.getEstadoOrden() != null ? orden.getEstadoOrden().name() : null);

        dto.setProceso(orden.getProceso() != null ? orden.getProceso().name() : null);

        dto.setEstadoPago(orden.getEstadoPago() != null ? orden.getEstadoPago().name() : null);

        dto.setEstadoEntrega( orden.getEstadoEntrega() != null ? orden.getEstadoEntrega().name() : null);

        return dto;
    }

    /********************************************************/
    /******************** ORDENES ACTIVAS ********************/
    /********************************************************/

    private long contarOrdenesActivas() {

        return ordenCompraDao.findAll()
            .stream()
            .filter(this::esOrdenOperativa)
            .count();
    }

    /********************************************************/
    /*********************** HELPERS *************************/
    /********************************************************/

    private BigDecimal calcularTendencia(BigDecimal actual, BigDecimal anterior) {

        actual = valorSeguro(actual);
        anterior = valorSeguro(anterior);

        if (anterior.compareTo(BigDecimal.ZERO) == 0) {

            return actual.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : BigDecimal.valueOf(100);
        }

        return actual
            .subtract(anterior)
            //el 4 son decimas a tomar en cuenta y half up redondea la decima final
            .divide(anterior, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2,RoundingMode.HALF_UP);
    }

    private BigDecimal calcularPorcentaje(BigDecimal parte, BigDecimal total) {

        parte = valorSeguro(parte);
        total = valorSeguro(total);

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return parte.divide(total, 4, RoundingMode.HALF_UP)
            .multiply(BigDecimal.valueOf(100))
            .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal valorSeguro(BigDecimal valor) {
        return valor != null ? valor : BigDecimal.ZERO;
    }

    private String validarPeriodo(LocalDate inicio, LocalDate fin) {

        if (inicio == null || fin == null) {
            return "Las fechas de inicio y fin son obligatorias";
        }

        if (inicio.isAfter(fin)) {
            return "La fecha de inicio no puede ser posterior a la fecha final";
        }
        
        return null;
    }
    
    private boolean esOrdenOperativa( OrdenCompra orden) {

        if (orden.getEstadoOrden() == null) {
            return false;
        }

        return orden.getEstadoOrden() != EstadoOrdenCompra.COTIZACION
            && orden.getEstadoOrden() != EstadoOrdenCompra.CANCELADA
            && orden.getEstadoOrden() != EstadoOrdenCompra.FINALIZADA;
    }
    
    private boolean estaListaParaEntregar(OrdenCompra orden) {

        if (orden.getProceso() == null) {
            return false;
        }

        return orden.getProceso() == Proceso.PAQUETERIA
            || orden.getProceso() == Proceso.INSTALACION;
    }
    
    private boolean estaAtrasada(OrdenCompra orden) {

        if (orden.getFechaEntrega() == null) {
            return false;
        }

        if (orden.getEstadoEntrega() == EstadoEntrega.ENTREGADA) {
            return false;
        }

        return orden.getFechaEntrega().isBefore(LocalDate.now());
    }
    
    
    
	
}
