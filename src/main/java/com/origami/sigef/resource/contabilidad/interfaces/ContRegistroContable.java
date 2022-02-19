/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.interfaces;

import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.models.DetalleReservaCompromisoModel;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.contabilidad.models.RelacionLocalModel;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Criss Intriago
 */
@Local
public interface ContRegistroContable {

    public String validaciones(ContDiarioGeneral contDiarioGeneral, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles);

    public void saveEditFacturas(List<Factura> facturas, List<Factura> delete, ContDiarioGeneral contDiarioGeneral);

    public ContDiarioGeneral create(ContDiarioGeneral contDiarioGeneral, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<Long> idModulos, Boolean accion);

    public ContDiarioGeneral edit(ContDiarioGeneral contDiarioGeneral, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete);

    public ContDiarioGeneral ContabilidadAnular(ContDiarioGeneral contDiarioGeneral);

    public void ContabilidadModuloCreate(ContDiarioGeneral contDiarioGeneral, String class1);

    public void ContabilidadModuloRemove(ContDiarioGeneral contDiarioGeneral);

    public void ContabilidadImprimirReporte(ContDiarioGeneral contDiarioGeneral, String tipoDocumento);

    public List<PartePresupuestariaModel> ContabilidadSaldoPresupuesto(ContDiarioGeneralDetalle diarioGeneralDetalle, Short periodo, Boolean devengado, Boolean tipoRelacion);

    public LazyModel<SolicitudReservaCompromiso> ContCertificacionesPresupuestarias(ContDiarioGeneral contDiarioGeneral, Boolean tipo);

    public DetalleReservaCompromisoModel detalleCertificacionPresupuestaria(SolicitudReservaCompromiso certificacionPresupuestaria, Integer cod_modulo, String cod_tipo);

    public List<ContCuentas> ContabilidaCuentasContables(Long idprescatalogopresupuestario);

    public ContDiarioGeneral findById(Long id);

    public List<ContDiarioGeneralDetalle> findByIdDiario(ContDiarioGeneral diario);

    public SolicitudReservaCompromiso getReservaCompromiso(ContDiarioGeneral diario);

    public ContDiarioGeneral findByDiarioPeriodo(Integer idDiario, Short anio);

    public List<BeneficiarioSolicitudReserva> beneficiarioComprobante(SolicitudReservaCompromiso reserva);

    public List<ContDiarioGeneral> getListDiarioGeneral(Short anio);

    public List<PartePresupuestariaModel> getEmisionDiaria(String fechaEmision);

    public ContCuentas findByIdContCuenta(Long idCuenta);

    public PresCatalogoPresupuestario findByIdCatalogo(Long idCatalogo);

    public List<PartePresupuestariaModel> relacionPresupuesto(ContDiarioGeneralDetalle contDiarioGeneralDetalle, ContDiarioGeneral contDiarioGeneral, Boolean tipoRelacion);

    public List<String> verificarRelacion(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList, List<PartePresupuestariaModel> partePresupuestariaModelList);

    public RelacionLocalModel relacionLocal(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList, Boolean tipoRelacion, ContDiarioGeneralDetalle contDiarioGeneralDetalle, List<String> partidasList);

    public void determinarTipoRegistro(Boolean tipoRelacion, ContDiarioGeneralDetalle contDiarioGeneralDetalle);

    public String guardarRelacionesPresupuestarias(PartePresupuestariaModel partePresupuestariaModel, ContDiarioGeneralDetalle contDiarioGeneralDetalle, Boolean tipoRelacion);

    public List<Depreciacion> getDepreciacionList(Short periodo);

    public List<ContDiarioGeneralDetalle> getDetalleDepreciacion(Depreciacion depreciacion);

    public List<CatalogoMovimiento> getMotivoMovimientos();

    public List<CatalogoMovimiento> getMotivoMovimientoInventario(Boolean accion);

    public List<ContDiarioGeneralDetalle> getBienesIngreso(List<Long> idModulos);

    public List<PartePresupuestariaModel> getEmisionFondoTercero(String fechaEmision);

    public List<ContDiarioGeneralDetalle> getInventario(List<Long> idModulos, Boolean tipoInventario);

    public double saldoDisponible(String partida, Short periodo);

    public List<ContDiarioGeneralDetalle> getaddCuentas(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalle, List<Factura> facturas);

    public List<ContDiarioGeneralDetalle> getDeleteCuentas(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalle, Factura factura);

    public List<Factura> getFacturas(ContDiarioGeneral contDiarioGeneral);

    public ContDiarioGeneral findByDiarioPeriodoRetenido(Integer numRegistroContable, Short anio);

    public List<ContDiarioGeneralDetalle> getDetalleDiarioFactura(List<Factura> facturas);

    public Boolean periodoContableValidador(Date fecha, Short periodo);

    public void getUpdateFacturas(List<Factura> facturasSeleccionadas, ContDiarioGeneral contDiarioGeneral);

    public List<ContDiarioGeneralDetalle> getTesoreriaDetalle(List<Long> idDiarioList, Short periodo);

    public List<ContDiarioGeneral> getTesoreriaList(Integer cod_modulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado, Boolean accion);

    public void registroEmisiones(FinaRenLiquidacion liqui);

    public void registroRecaudaciones(FinaRenPago pago);

    public List<ThTipoRol> getRoles(Short periodo);

    public ThTipoRol getThTipoRol(Long id);

    public List<ContDiarioGeneralDetalle> getDetalleRol(ThTipoRol thTipoRolSeleccionado);

    public List<FinaRenTipoLiquidacion> getTipoLiquidacionList();

    public Boolean isEmisionContabilizada(FinaRenLiquidacion liquidacion);

    public void anularEmision(FinaRenLiquidacion liquidacion);

    public void anularEmisionGeneral(Long valor1, Long valor2, Short periodo);

    public List<Long> findRelacionModulos(List<ContDiarioGeneral> idModulos, Integer cod_modulo);

    public void editRegistroContable(ContDiarioGeneral contDiarioGeneral);

    public List<ContDiarioGeneralDetalle> getTesoreriaPredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado, Short periodo);

    public List<Long> findRelacionModulosPredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado);

    public void updateRegistroContablePredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado);

    //anulacion de prescripcion de titulo
    public void anularPorPrescripcion(FinaRenLiquidacion liquidacion);
}
