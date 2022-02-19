/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.implInterfaces;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Services.FinaRenTipoLiquidacionService;
import com.origami.sigef.activos.entities.Depreciacion;
import com.origami.sigef.activos.service.BienesMovimientoService;
import com.origami.sigef.activos.service.CatalogoMovimientoService;
import com.origami.sigef.activos.service.DepreciacionDetalleService;
import com.origami.sigef.activos.service.DepreciacionService;
import com.origami.sigef.activos.service.ProcesoIngresoService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ContratoReservaService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Adquisiciones;
import com.origami.sigef.common.entities.BeneficiarioSolicitudReserva;
import com.origami.sigef.common.entities.BienesMovimiento;
import com.origami.sigef.common.entities.CatalogoMovimiento;
import com.origami.sigef.common.entities.Factura;
import com.origami.sigef.common.entities.Inventario;
import com.origami.sigef.common.entities.SolicitudReservaCompromiso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.BeneficiarioSolicitudReservaService;
import com.origami.sigef.contabilidad.service.ControlCuentaContableService;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContContabilidadModulo;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.models.DetalleReservaCompromisoModel;
import com.origami.sigef.resource.contabilidad.models.PartePresupuestariaModel;
import com.origami.sigef.resource.contabilidad.models.RelacionLocalModel;
import com.origami.sigef.resource.contabilidad.models.RolDetalleModel;
import com.origami.sigef.resource.contabilidad.services.ContContabilidadModuloService;
import com.origami.sigef.resource.contabilidad.services.ContCuentaPartidaService;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralDetalleService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralService;
import com.origami.sigef.resource.contabilidad.services.ContFacturaDetalleService;
import com.origami.sigef.resource.contabilidad.services.FacturaService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.presupuesto.entities.PresFuenteFinanciamiento;
import com.origami.sigef.resource.presupuesto.entities.PresPlanProgramatico;
import com.origami.sigef.resource.presupuesto.procesos.services.ReservaCompromisoServices;
import com.origami.sigef.resource.presupuesto.services.PresCatalogoPresupuestarioService;
import com.origami.sigef.resource.presupuesto.services.PresFuenteFinanciamientoService;
import com.origami.sigef.resource.presupuesto.services.PresPlanProgramaticoService;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.interfaces.ThInterfaces;
import com.origami.sigef.resource.talento_humano.services.ThLiquidacionRolDetalleService;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Criss Intriago
 */
@Singleton
@ApplicationScoped
public class ImplContRegistroContable implements ContRegistroContable {

    private static final Logger LOG = Logger.getLogger(ImplContRegistroContable.class.getName());

    @Inject
    private ContDiarioGeneralService contDiarioGeneralService;
    @Inject
    private ContDiarioGeneralDetalleService contDiarioGeneralDetalleService;
    @Inject
    private ContContabilidadModuloService contContabilidadModuloService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ContCuentaPartidaService cuentaPartidaService;
    @Inject
    private ContratoReservaService contratoReservaService;
    @Inject
    private ReservaCompromisoServices reservaCompromisoServices;
    @Inject
    private UserSession userSession;
    @Inject
    private BeneficiarioSolicitudReservaService beneficiarioSolicitudReservaService;
    @Inject
    private ControlCuentaContableService controlCuentaContableService;
    @Inject
    private ContCuentasService contCuentasService;
    @Inject
    private PresCatalogoPresupuestarioService catalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CatalogoService catalogoServiceCtg;
    @Inject
    private DepreciacionService depreciacionService;
    @Inject
    private DepreciacionDetalleService depreciacionDetalleService;
    @Inject
    private CatalogoMovimientoService catalogoMovimientoService;
    @Inject
    private BienesMovimientoService bienesMovimientoService;
    @Inject
    private ProcesoIngresoService inventarioService;
    @Inject
    private ContFacturaDetalleService contFacturaDetalleService;
    @Inject
    private FacturaService facturaService;
    @Inject
    private ThInterfaces thInterfaces;
    @Inject
    private ThLiquidacionRolDetalleService thLiquidacionRolDetalleService;
    @Inject
    private PresPlanProgramaticoService presPlanProgramaticoService;
    @Inject
    private PresCatalogoPresupuestarioService presCatalogoPresupuestarioService;
    @Inject
    private PresFuenteFinanciamientoService presFuenteFinanciamientoService;
    @Inject
    private FinaRenTipoLiquidacionService finaRenTipoLiquidacionService;

    @Override
    public String validaciones(ContDiarioGeneral contDiarioGeneral, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles) {
        if (contDiarioGeneral.getPeriodo() == null) {
            return "Debe seleccionar un periodo";
        }
        if (Utils.getAnio(contDiarioGeneral.getFechaRegistro()) != contDiarioGeneral.getPeriodo().intValue()) {
            return "El periodo seleccionado no es igual al periodo de la fecha del registro";
        }
        if (controlCuentaContableService.validarPeriodo(contDiarioGeneral.getFechaRegistro(), contDiarioGeneral.getPeriodo())) {
            return "El periodo selecionado se encuentra cerrado";
        }
        if (contDiarioGeneral.getClase() == null) {
            return "Debe seleccionar una clase";
        }
        if (contDiarioGeneral.getTipo() == null) {
            return "Debe seleccionar un tipo";
        }
        if (contDiarioGeneral.getDescripcion() == null || contDiarioGeneral.getDescripcion().equals("")) {
            return "Debe ingresar una descripción";
        }
        return "";
    }

    @Override
    public void saveEditFacturas(List<Factura> facturas, List<Factura> delete, ContDiarioGeneral contDiarioGeneral) {
        //elimnar relacion de facturas
        if (delete != null) {
            for (Factura f : delete) {
                f.setIdConDiario(null);
                facturaService.edit(f);
            }
        }
        //relacionar facturas
        if (facturas != null) {
            for (Factura f : facturas) {
                f.setIdConDiario(contDiarioGeneral);
                facturaService.edit(f);
            }
        }
    }

    @Override
    public ContDiarioGeneral create(ContDiarioGeneral contDiarioGeneral, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<Long> idModulos, Boolean accion) {
        contDiarioGeneral.setNumRegistro(contDiarioGeneralService.nextRegistro(contDiarioGeneral.getPeriodo(), contDiarioGeneral.getRevisado()));
        contDiarioGeneral.setRetencion(determinarRegistroContableRetencion(contDiarioGeneralDetalles, contDiarioGeneral.getPeriodo()));
        contDiarioGeneral = contDiarioGeneralService.create(contDiarioGeneral);
        for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalles) {
            item.setIdContDiarioGeneral(contDiarioGeneral);
            contDiarioGeneralDetalleService.create(item);
        }
        crearContabilizarModulo(idModulos, accion, contDiarioGeneral);
        userSession.setIdDiario(null);
        return contDiarioGeneral;
    }

    @Override
    public ContDiarioGeneral edit(ContDiarioGeneral contDiarioGeneral, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete) {
        contDiarioGeneral.setRetencion(determinarRegistroContableRetencion(contDiarioGeneralDetalles, contDiarioGeneral.getPeriodo()));
        contDiarioGeneralService.edit(contDiarioGeneral);
        //edita
        if (contDiarioGeneralDetalles != null) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalles) {
                if (item.getId() != null) {
                    contDiarioGeneralDetalleService.edit(item);
                } else {
                    item.setIdContDiarioGeneral(contDiarioGeneral);
                    contDiarioGeneralDetalleService.create(item);
                }
            }
        }
        //elimina
        if (contDiarioGeneralDetallesDelete != null) {
            for (ContDiarioGeneralDetalle item : contDiarioGeneralDetallesDelete) {
                System.out.println("ContDiarioGeneralDetalle: " + item.getId());
                contDiarioGeneralDetalleService.remove(item);
            }
        }
        userSession.setIdDiario(null);
        return contDiarioGeneral;
    }

    public Boolean determinarRegistroContableRetencion(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, Short periodo) {
        Boolean result = Boolean.FALSE;
        List<ContCuentas> idCuentasContables = new ArrayList<>();
        if (contDiarioGeneralDetalles != null) {
            if (!contDiarioGeneralDetalles.isEmpty()) {
                for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalles) {
                    idCuentasContables.add(item.getIdContCuentas());
                }
            }
        }
        if (!idCuentasContables.isEmpty()) {
            result = contDiarioGeneralDetalleService.determinarRetencion(idCuentasContables, periodo);
        }
        return result;
    }

    @Override
    public ContDiarioGeneral ContabilidadAnular(ContDiarioGeneral contDiarioGeneral) {
        ContDiarioGeneral anulacion = Utils.clone(contDiarioGeneral);
        anulacion.setId(null);
        anulacion.setDescripcion("ANULACION: " + anulacion.getDescripcion());
        anulacion.setIdDiarioGeneral(contDiarioGeneral);
        List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles = contDiarioGeneralDetalleService.findByNamedQuery("ContDiarioGeneralDetalle.findByIdRegistroContable", contDiarioGeneral);
        List<ContDiarioGeneralDetalle> detalleList = new ArrayList<>();
        for (ContDiarioGeneralDetalle item : contDiarioGeneralDetalles) {
            ContDiarioGeneralDetalle detalle = Utils.clone(item);
            detalle.setIdContDiarioGeneral(null);
            detalle.setId(null);
            detalle.setDebe(valorNegativo(detalle.getDebe()));
            detalle.setHaber(valorNegativo(detalle.getHaber()));
            detalle.setDevengado(valorNegativo(detalle.getDevengado()));
            detalle.setComprometido(valorNegativo(detalle.getComprometido()));
            detalle.setEjecutado(valorNegativo(detalle.getEjecutado()));
            detalleList.add(detalle);
        }
        List<Long> idModulos = new ArrayList<>();
        List<BigInteger> temp = contContabilidadModuloService.findContabilidadModulos(contDiarioGeneral);
        for (BigInteger item : temp) {
            idModulos.add(item.longValue());
        }
        anulacion = create(anulacion, detalleList, idModulos, false);
        contDiarioGeneral.setIdDiarioGeneral(anulacion);
        contDiarioGeneral.setAnulado(Boolean.TRUE);
        activarModulo(contDiarioGeneral);
        contDiarioGeneralService.edit(contDiarioGeneral);
        List<Factura> _temp = getFacturas(contDiarioGeneral);
        if (_temp != null && !_temp.isEmpty()) {
            for (Factura fac : _temp) {
                fac.setIdConDiario(null);
                facturaService.edit(fac);
            }
        }
        ContabilidadImprimirReporte(anulacion, "pdf");
        return anulacion;
    }

    public BigDecimal valorNegativo(BigDecimal valor) {
        if (valor.doubleValue() == 0) {
            return valor;
        } else {
            return BigDecimal.valueOf(-1 * valor.doubleValue());
        }
    }

    public void activarModulo(ContDiarioGeneral diario) {
        SolicitudReservaCompromiso solicitud = getReservaCompromiso(diario);
        if (solicitud != null) {
            solicitud.setContabilizado(Boolean.FALSE);
            reservaCompromisoServices.edit(solicitud);
        }
    }

    public void crearContabilizarModulo(List<Long> idModulos, Boolean contabilizado, ContDiarioGeneral contDiarioGeneral) {
        if (idModulos == null) {
            return;
        }
        if (!idModulos.isEmpty()) {
            for (Long item : idModulos) {
                if (null != contDiarioGeneral.getCodModulo()) {
                    ContContabilidadModulo contModulo = new ContContabilidadModulo();
                    contModulo.setIdContDiarioGeneral(contDiarioGeneral);
                    contModulo.setCodModulo(contDiarioGeneral.getCodModulo());
                    contModulo.setIdModulo(new BigInteger(item + ""));
                    contContabilidadModuloService.create(contModulo);
                    switch (contDiarioGeneral.getCodModulo()) {
                        //modulo de certificacion presupuestarias
                        case 0:
                            if (contabilizado) {
                                if ((contDiarioGeneralDetalleService.detalleReservaCompromiso(item, contDiarioGeneral.getCodModulo(), CONFIG.COD_DEVENGADO).isEmpty())) {
                                    reservaCompromisoServices.updateContabilizado(item, contabilizado);
                                }
                            } else {
                                reservaCompromisoServices.updateContabilizado(item, contabilizado);
                            }
                            break;
                        //modulo emision recaudacion
                        case 2:
                            break;
                        //modulo talento humano
                        case 3:
                            ThTipoRol tr = thInterfaces.getThTipoRol(item);
                            tr.setContabilizado(contabilizado);
                            thInterfaces.editThTipoRol(tr);
                            break;
                        //modulo bienes - depreciacion
                        case 4:
                            Depreciacion dep = depreciacionService.find(item);
                            dep.setContabilizado(contabilizado);
                            depreciacionService.edit(dep);
                            break;
                        //modulo bienes - ingreso
                        case 5:
                            BienesMovimiento bien = bienesMovimientoService.find(item);
                            bien.setContabilizado(contabilizado);
                            bienesMovimientoService.edit(bien);
                            break;
                        //modulo inventario - ingresos - egresos
                        case 6:
                        case 7:
                            Inventario inventario = inventarioService.find(item);
                            inventario.setContabilizado(contabilizado);
                            inventarioService.edit(inventario);
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }

    @Override
    public void ContabilidadModuloCreate(ContDiarioGeneral contDiarioGeneral, String class1) {
        ContContabilidadModulo contContabilidadModulo = new ContContabilidadModulo(contDiarioGeneral, class1);
        contContabilidadModuloService.create(contContabilidadModulo);
    }

    @Override
    public void ContabilidadModuloRemove(ContDiarioGeneral contDiarioGeneral) {
        ContContabilidadModulo contContabilidadModulo = contContabilidadModuloService.findByNamedQuery1("ContContabilidadModulo.findByIdContDiarioGeneral", contDiarioGeneral.getId());
        contContabilidadModulo.setEstado(Boolean.FALSE);
        contContabilidadModuloService.edit(contContabilidadModulo);
    }

    @Override
    public void ContabilidadImprimirReporte(ContDiarioGeneral contDiarioGeneral, String tipoDocumento) {
        servletSession.addParametro("id_diario", contDiarioGeneral.getId());
        servletSession.setNombreReporte("registro_contable");
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreSubCarpeta("_contabilidad");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    @Override
    public List<PartePresupuestariaModel> ContabilidadSaldoPresupuesto(ContDiarioGeneralDetalle diarioGeneralDetalle, Short periodo, Boolean devengado, Boolean tipoRelacion) {
        List<PartePresupuestariaModel> result = new ArrayList<>();
        if (diarioGeneralDetalle.getIdContCuentas().getPagadoDevengado()) {
            System.out.println("DEVENGADO/EJECUTADO");
            result = cuentaPartidaService.findByCuentaPartidasPagadoDevengado(diarioGeneralDetalle.getIdContCuentas(), periodo, devengado);
        } else {
            if (tipoRelacion) {
                System.out.println("findByCuentaPartidasPagadoDevengado");
                result = cuentaPartidaService.findByCuentaPartidasPagadoDevengado(diarioGeneralDetalle.getIdContCuentas(), periodo, devengado);
            } else {
                System.out.println("findByCuentaPartidas");
                result = cuentaPartidaService.findByCuentaPartidas(diarioGeneralDetalle.getIdContCuentas(), periodo, devengado);
            }
        }
        return result;
    }

    @Override
    public LazyModel<SolicitudReservaCompromiso> ContCertificacionesPresupuestarias(ContDiarioGeneral contDiarioGeneral, Boolean tipo) {
        LazyModel<SolicitudReservaCompromiso> result = new LazyModel<>(SolicitudReservaCompromiso.class);
        result.getSorteds().put("secuencial", "ASC");
        result.getFilterss().put("periodo", contDiarioGeneral.getPeriodo());
        result.getFilterss().put("contabilizado", false);
        result.getFilterss().put("comprometido", true);
        result.getFilterss().put("antiguo", tipo);
        result.getFilterss().put("estado.codigo", "APRO");
        return result;
    }

    @Override
    public DetalleReservaCompromisoModel detalleCertificacionPresupuestaria(SolicitudReservaCompromiso certificacionPresupuestaria, Integer cod_modulo, String cod_tipo) {
        List<PartePresupuestariaModel> detalleList = contDiarioGeneralDetalleService.detalleReservaCompromiso(certificacionPresupuestaria.getId(), cod_modulo, cod_tipo);
        List<Adquisiciones> adquicionnesList = contratoReservaService.findContratos(certificacionPresupuestaria);
        return new DetalleReservaCompromisoModel(detalleList, adquicionnesList);
    }

    @Override
    public List<ContCuentas> ContabilidaCuentasContables(Long idprescatalogopresupuestario) {
        return cuentaPartidaService.getCuentaList(idprescatalogopresupuestario);
    }

    @Override
    public ContDiarioGeneral findById(Long id) {
        return contDiarioGeneralService.find(id);
    }

    @Override
    public List<ContDiarioGeneralDetalle> findByIdDiario(ContDiarioGeneral diario) {
        return contDiarioGeneralDetalleService.findByNamedQuery("ContDiarioGeneralDetalle.findByIdRegistroContable", diario);
    }

    public SolicitudReservaCompromiso getReservaCompromiso(ContDiarioGeneral diario) {
        Long id = contContabilidadModuloService.getReserva(diario);
        if (id != null) {
            return reservaCompromisoServices.find(id);
        } else {
            return null;
        }

    }

    @Override
    public ContDiarioGeneral findByDiarioPeriodo(Integer idDiario, Short anio) {
        return contDiarioGeneralService.findByDiarioPeriodo(idDiario, anio);
    }

    @Override
    public List<BeneficiarioSolicitudReserva> beneficiarioComprobante(SolicitudReservaCompromiso reserva) {
        return beneficiarioSolicitudReservaService.getListaBeneficiarioSolicitudReservas(reserva);
    }

    @Override
    public List<ContDiarioGeneral> getListDiarioGeneral(Short anio) {
        return contDiarioGeneralService.findByNamedQuery("ContDiarioGeneral.findByComprometido", anio);
    }

    @Override
    public List<PartePresupuestariaModel> getEmisionDiaria(String fechaEmision) {
        return contDiarioGeneralDetalleService.getParteEmisionDiaria(fechaEmision);
    }

    @Override
    public ContCuentas findByIdContCuenta(Long idCuenta) {
        return (ContCuentas) contCuentasService.find(idCuenta);
    }

    @Override
    public PresCatalogoPresupuestario findByIdCatalogo(Long idCatalogo) {
        return (PresCatalogoPresupuestario) catalogoService.find(idCatalogo);
    }

    @Override
    public List<PartePresupuestariaModel> relacionPresupuesto(ContDiarioGeneralDetalle contDiarioGeneralDetalle, ContDiarioGeneral contDiarioGeneral, Boolean tipoRelacion) {
        List<PartePresupuestariaModel> result = new ArrayList<>();
        if (tipoRelacion) {
            if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("213") && contDiarioGeneralDetalle.getIdContCuentas().getPagadoDevengado()) {//ejecutado
                result = ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral.getPeriodo(), false, tipoRelacion);
            } else {//devengado
                result = ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral.getPeriodo(), true, tipoRelacion);
            }
        } else {
            if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("113")) {//ejecutado
                result = ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral.getPeriodo(), false, tipoRelacion);
            } else {//devengado
                result = ContabilidadSaldoPresupuesto(contDiarioGeneralDetalle, contDiarioGeneral.getPeriodo(), true, tipoRelacion);
            }
        }
        return result;
    }

    @Override
    public List<String> verificarRelacion(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList, List<PartePresupuestariaModel> partePresupuestariaModelList) {
        List<String> partidasList = new ArrayList<>();
        for (ContDiarioGeneralDetalle a : contDiarioGeneralDetallesList) {
            if (a.getPartidaPresupuestaria() != null) {
                for (PartePresupuestariaModel b : partePresupuestariaModelList) {
                    if (a.getPartidaPresupuestaria().equals(b.getPartidapresupuestaria())) {
                        partidasList.add(a.getPartidaPresupuestaria());
                    }
                }
            }
        }
        return partidasList;
    }

    @Override
    public RelacionLocalModel relacionLocal(List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesList, Boolean tipoRelacion, ContDiarioGeneralDetalle contDiarioGeneralDetalle, List<String> partidasList) {
        RelacionLocalModel _result = new RelacionLocalModel();
        List<PartePresupuestariaModel> result = new ArrayList<>();
        for (String partida : partidasList) {
            for (ContDiarioGeneralDetalle temp : contDiarioGeneralDetallesList) {
                double devengado = 0;
                double ejecutado = 0;
                double sumDevengado = 0;
                double sumEjecutado = 0;
                if (tipoRelacion) {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("2") && contDiarioGeneralDetalle.getIdContCuentas().getPagadoDevengado()) {
                        sumEjecutado = contDiarioGeneralDetalle.getDebe().doubleValue();
                    } else {
                        sumDevengado = contDiarioGeneralDetalle.getDebe().doubleValue();
                    }
                } else {
                    if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("1")) {
                        sumEjecutado = contDiarioGeneralDetalle.getHaber().doubleValue();
                    } else {
                        sumDevengado = contDiarioGeneralDetalle.getHaber().doubleValue();
                    }
                }
                if (temp.getPartidaPresupuestaria() != null) {
                    if (partida.equals(temp.getPartidaPresupuestaria())) {
                        if (temp.getTipoRegistro().getCodigo().equals("diario_general_devengado")) {
                            sumDevengado = sumDevengado + temp.getDevengado().doubleValue();
                            devengado = devengado + temp.getDevengado().doubleValue();
                            for (ContDiarioGeneralDetalle temp_1 : contDiarioGeneralDetallesList) {
                                if (temp_1.getPartidaPresupuestaria() != null) {
                                    if (temp_1.getTipoRegistro().getCodigo().equals("diario_general_ejecucion")) {
                                        if (temp.getPartidaPresupuestaria().equals(temp_1.getPartidaPresupuestaria())) {
                                            sumEjecutado = sumEjecutado + temp_1.getEjecutado().doubleValue();
                                            ejecutado = ejecutado + temp_1.getEjecutado().doubleValue();
                                        }
                                    }
                                }
                            }
                            if (sumDevengado >= sumEjecutado) {
                                contDiarioGeneralDetalle.setPartidaPresupuestaria(temp.getPartidaPresupuestaria());
                                contDiarioGeneralDetalle.setIdPresCatalogoPresupuestario(temp.getIdPresCatalogoPresupuestario());
                                contDiarioGeneralDetalle.setIdPresPlanProgramatico(temp.getIdPresPlanProgramatico());
                                contDiarioGeneralDetalle.setIdPresFuenteFinanciamiento(temp.getIdPresFuenteFinanciamiento());
                                determinarTipoRegistro(tipoRelacion, contDiarioGeneralDetalle);
                                _result.setContDiarioGeneralDetalle(contDiarioGeneralDetalle);
                                _result.setResultList(null);
                                return _result;
                            } else {
                                PartePresupuestariaModel model = new PartePresupuestariaModel();
                                model.setIdtemp(Long.valueOf(result.size() + 1));
                                model.setPartidapresupuestaria(temp.getPartidaPresupuestaria());
                                model.setDescripcion(temp.getIdPresCatalogoPresupuestario().getDescripcion());
                                model.setIdprescatalogopresupuestario(temp.getIdPresCatalogoPresupuestario().getId());
                                model.setIdpresfuentefinanciamiento(temp.getIdPresFuenteFinanciamiento().getId());
                                model.setIdpresplanprogramatico(temp.getIdPresPlanProgramatico().getId());
                                if (devengado >= ejecutado) {
                                    model.setSaldodisponible(new BigDecimal(sumDevengado));
                                    result.add(model);
                                }
                            }
                        }
                    }
                }
            }
        }
        _result.setContDiarioGeneralDetalle(null);
        _result.setResultList(result);
        return _result;
    }

    @Override
    public void determinarTipoRegistro(Boolean tipoRelacion, ContDiarioGeneralDetalle contDiarioGeneralDetalle) {
        if (tipoRelacion) {
            if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("213") && contDiarioGeneralDetalle.getIdContCuentas().getPagadoDevengado()) {
                contDiarioGeneralDetalle.setTipoRegistro(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "diario_general_ejecucion"));
                contDiarioGeneralDetalle.setEjecutado(contDiarioGeneralDetalle.getDebe());
            } else {
                contDiarioGeneralDetalle.setTipoRegistro(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "diario_general_devengado"));
                contDiarioGeneralDetalle.setDevengado(contDiarioGeneralDetalle.getDebe());
            }
        } else {
            if (contDiarioGeneralDetalle.getIdContCuentas().getCodigo().startsWith("113")) {
                contDiarioGeneralDetalle.setTipoRegistro(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "diario_general_ejecucion"));
                contDiarioGeneralDetalle.setEjecutado(contDiarioGeneralDetalle.getHaber());
            } else {
                contDiarioGeneralDetalle.setTipoRegistro(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "diario_general_devengado"));
                contDiarioGeneralDetalle.setDevengado(contDiarioGeneralDetalle.getHaber());
            }
        }
    }

    @Override
    public String guardarRelacionesPresupuestarias(PartePresupuestariaModel partePresupuestariaModel, ContDiarioGeneralDetalle contDiarioGeneralDetalle, Boolean tipoRelacion) {
        if (partePresupuestariaModel == null) {
            return "Debe seleccionar una relacion presupuestaria";
        }
        if (!partePresupuestariaModel.getTipoPresupuesto()) {
            if (partePresupuestariaModel.getSaldodisponible().doubleValue() <= 0) {
                JsfUtil.addWarningMessage("AVISO!!!", "La partida presupustaria " + partePresupuestariaModel.getPartidapresupuestaria() + ", no dispone de saldo para devengar");
                return "La partida presupustaria " + partePresupuestariaModel.getPartidapresupuestaria() + ", no dispone de saldo para devengar";
            }
            if (tipoRelacion) {
                if (contDiarioGeneralDetalle.getDebe().doubleValue() > partePresupuestariaModel.getSaldodisponible().doubleValue()) {
                    contDiarioGeneralDetalle.setDebe(partePresupuestariaModel.getSaldodisponible());
                }
            } else {
                if (contDiarioGeneralDetalle.getHaber().doubleValue() > partePresupuestariaModel.getSaldodisponible().doubleValue()) {
                    contDiarioGeneralDetalle.setHaber(partePresupuestariaModel.getSaldodisponible());
                }
            }
        }
        if (partePresupuestariaModel.getPartidapresupuestaria() != null) {
            contDiarioGeneralDetalle.setPartidaPresupuestaria(partePresupuestariaModel.getPartidapresupuestaria());
        }
        if (partePresupuestariaModel.getIdprescatalogopresupuestario() != null) {
            contDiarioGeneralDetalle.setIdPresCatalogoPresupuestario(new PresCatalogoPresupuestario(partePresupuestariaModel.getIdprescatalogopresupuestario()));
        }
        if (partePresupuestariaModel.getIdpresplanprogramatico() != null) {
            contDiarioGeneralDetalle.setIdPresPlanProgramatico(new PresPlanProgramatico(partePresupuestariaModel.getIdpresplanprogramatico()));
        }
        if (partePresupuestariaModel.getIdpresfuentefinanciamiento() != null) {
            contDiarioGeneralDetalle.setIdPresFuenteFinanciamiento(new PresFuenteFinanciamiento(partePresupuestariaModel.getIdpresfuentefinanciamiento()));
        }
        determinarTipoRegistro(tipoRelacion, contDiarioGeneralDetalle);
        return "";
    }

    @Override
    public List<Depreciacion> getDepreciacionList(Short periodo) {
        return depreciacionService.findByNamedQuery("Depreciacion.findPeriodo", periodo);
    }

    @Override
    public List<ContDiarioGeneralDetalle> getDetalleDepreciacion(Depreciacion depreciacion) {
        List<PartePresupuestariaModel> aux = depreciacionDetalleService.getRegistroContable(depreciacion);
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        if (aux != null) {
            if (!aux.isEmpty()) {
                for (PartePresupuestariaModel item : aux) {
                    ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle(contCuentasService.find(item.getIdtemp()), item.getSaldodisponible());
                    detalle.setNumeracion(result.size() + 1);
                    result.add(detalle);
                }
            }
        }
        return result;
    }

    @Override
    public List<PartePresupuestariaModel> getEmisionFondoTercero(String fechaEmision) {
        return contDiarioGeneralDetalleService.getemisionFondoTerceros(fechaEmision);
    }

    @Override
    public List<CatalogoMovimiento> getMotivoMovimientos() {
        return catalogoMovimientoService.findByNamedQuery("CatalogoMovimiento.findByList", "BIEN-ING");
    }

    @Override
    public List<CatalogoMovimiento> getMotivoMovimientoInventario(Boolean accion) {
        if (accion) {
            return catalogoMovimientoService.findByNamedQuery("CatalogoMovimiento.findByListEntradas", "INGINV");
        } else {
            return catalogoMovimientoService.findByNamedQuery("CatalogoMovimiento.findByListSalidas", "SALINV");
        }
    }

    @Override
    public List<ContDiarioGeneralDetalle> getBienesIngreso(List<Long> idModulos) {
        List<PartePresupuestariaModel> aux = contDiarioGeneralDetalleService.getContabilidad(idModulos);
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        for (PartePresupuestariaModel item : aux) {
            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle(contCuentasService.find(item.getIdtemp()), item.getSaldodisponible(), item.getMontoDisponible());
            detalle.setNumeracion(result.size() + 1);
            result.add(detalle);
        }
        return result;
    }

    @Override
    public List<ContDiarioGeneralDetalle> getInventario(List<Long> idModulos, Boolean tipoInventario) {
        List<PartePresupuestariaModel> aux = new ArrayList<>();
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        if (tipoInventario) {
            aux = contDiarioGeneralDetalleService.getContabilidadInvIngreso(idModulos);
        } else {
            aux = contDiarioGeneralDetalleService.getContabilidadInvSalidas(idModulos);
        }
        for (PartePresupuestariaModel item : aux) {
            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle(contCuentasService.find(item.getIdtemp()), item.getSaldodisponible(), item.getMontoDisponible());
            detalle.setNumeracion(result.size() + 1);
            result.add(detalle);
        }
        return result;
    }

    @Override
    public double saldoDisponible(String partida, Short periodo) {
        return contDiarioGeneralService.getSaldo(partida, periodo);
    }

    @Override
    public List<ContDiarioGeneralDetalle> getaddCuentas(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalle, List<Factura> facturas) {
        List<Long> idList = new ArrayList<>();
        for (Factura fac : facturas) {
            idList.add(fac.getId());
        }
        List<PartePresupuestariaModel> aux = contFacturaDetalleService.facturaDetalleContable(idList);
        for (PartePresupuestariaModel item : aux) {
            ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
            ContCuentas cc = contCuentasService.find(item.getIdtemp());
            detalle.setIdContCuentas(cc);
            detalle.setHaber(item.getSaldodisponible());
            detalle.setSaldoRetencion(item.getMontoDisponible());
            detalle.setNumeracion(contDiarioGeneralDetalle.size() + 1);
            detalle.setDatoCargado(Boolean.TRUE);
            detalle.setFactura(Boolean.TRUE);
            contDiarioGeneralDetalle.add(detalle);
        }
        return contDiarioGeneralDetalle;
    }

    @Override
    public List<ContDiarioGeneralDetalle> getDeleteCuentas(List<ContDiarioGeneralDetalle> contDiarioGeneralDetalle, Factura factura) {
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        List<ContDiarioGeneralDetalle> aux = Utils.clone(contDiarioGeneralDetalle);
        if (!contDiarioGeneralDetalle.isEmpty()) {
            for (ContDiarioGeneralDetalle detalle : aux) {
                if (detalle.getIdDetalleFactura() != null) {
                    if (detalle.getIdDetalleFactura().getIdFactura().getId().equals(factura.getId())) {
                        if (detalle.getId() != null) {
                            result.add(detalle);
                            contDiarioGeneralDetalle.remove(detalle);
                        } else {
                            contDiarioGeneralDetalle.remove(detalle);
                        }
                    }
                }
            }
        }
        return result;
    }

    @Override
    public List<Factura> getFacturas(ContDiarioGeneral contDiarioGeneral) {
        return facturaService.findByNamedQuery("Factura.findByDiarioGeneral", contDiarioGeneral);
    }

    @Override
    public ContDiarioGeneral findByDiarioPeriodoRetenido(Integer numRegistroContable, Short anio) {
        return contDiarioGeneralService.findByNamedQuery1("ContDiarioGeneral.findByRetencion", numRegistroContable, anio);
    }

    @Override
    public List<ContDiarioGeneralDetalle> getDetalleDiarioFactura(List<Factura> facturas) {
        return contDiarioGeneralDetalleService.getDetalleFactura(facturas);
    }

    @Override
    public Boolean periodoContableValidador(Date fecha, Short periodo) {
        return controlCuentaContableService.validarPeriodo(fecha, periodo);
    }

    @Override
    public void getUpdateFacturas(List<Factura> facturasSeleccionadas, ContDiarioGeneral contDiarioGeneral) {
        for (Factura fac : facturasSeleccionadas) {
            fac.setRetenida(Boolean.TRUE);
            facturaService.edit(fac);
        }
        contDiarioGeneralService.getUpdateDiario(contDiarioGeneral.getId(), facturasSeleccionadas.size());
    }

    @Override
    public void registroEmisiones(FinaRenLiquidacion liqui) {
        Date fecha = liqui.getFechaIngreso();
        Short periodo = Utils.getAnio(fecha).shortValue();
        int value = contDiarioGeneralService.registro_contable_emisiones(liqui, MOD_CONTABILIDAD.MOD_TES_EMISION, fecha, periodo, CONFIG.COD_DEVENGADO);
    }

    @Override
    public void registroRecaudaciones(FinaRenPago pago) {
        try {
            ContDiarioGeneral diarioG;
            ContDiarioGeneralDetalle debe;
            ContDiarioGeneralDetalle haber;
            List<ContDiarioGeneralDetalle> detalleDiarioG;
            List<Long> liquidaciones = new ArrayList<>();
            if (pago != null) {
                liquidaciones.add(pago.getLiquidacion().getId());
                diarioG = new ContDiarioGeneral();
                diarioG.setCodModulo(MOD_CONTABILIDAD.MOD_TES_EMISION);
                diarioG.setCuadrado(Boolean.TRUE);
                diarioG.setBeneficiario(pago.getLiquidacion().getNombreComprador());
                diarioG.setIdentificacion(pago.getLiquidacion().getIdentificacion());
                diarioG.setFechaRegistro(new Date());
                diarioG.setPeriodo(Utils.getAnio(new Date()).shortValue());
                diarioG.setRevisado(Boolean.FALSE);
                diarioG.setDescripcion(pago.getLiquidacion().getTipoLiquidacion().getNombreTitulo() + "-" + pago.getLiquidacion().getAnio());
                diarioG.setDebe(BigDecimal.ZERO);
                diarioG.setHaber(BigDecimal.ZERO);
                detalleDiarioG = new ArrayList<>();
                diarioG.setTipoLiquidacion(pago.getLiquidacion().getTipoLiquidacion());
                if (Utils.isNotEmpty(pago.getLiquidacion().getRenDetLiquidacionCollection())) {
                    for (FinaRenDetLiquidacion dt : pago.getLiquidacion().getRenDetLiquidacionCollection()) {
                        debe = new ContDiarioGeneralDetalle();
                        haber = new ContDiarioGeneralDetalle();
                        if (dt.getRubro().getRubroDelMunicipio()) {
                            diarioG.setDebe(diarioG.getDebe().add(dt.getValor()));
                            //cuentas del debe                        
                            debe.setAnio(diarioG.getPeriodo().intValue());
                            debe.setDebe(dt.getValor());
                            debe.setIdContCuentas(dt.getRubro().getContCc());

                            haber.setAnio(diarioG.getPeriodo().intValue());
                            haber.setDebe(dt.getValor());
                            haber.setIdContCuentas(dt.getRubro().getContCp());

                            haber.setPartidaPresupuestaria(dt.getRubro().getPartida().getCodigo().concat(dt.getRubro().getPartida().getFueteNew().getCodFuente()));
                            haber.setIdPresFuenteFinanciamiento(dt.getRubro().getPartida().getFueteNew());
                            haber.setIdPresCatalogoPresupuestario(dt.getRubro().getPartida());
                            haber.setTipoRegistro(catalogoServiceCtg.getTipoItem(CONFIG.COD_DEVENGADO));
                            detalleDiarioG.add(haber);
                            detalleDiarioG.add(debe);
                        } else {
                            diarioG.setDebe(diarioG.getDebe().add(dt.getValor()));
                            //cuentas del debe                        
                            debe.setAnio(diarioG.getPeriodo().intValue());
                            debe.setDebe(dt.getValor());
                            debe.setIdContCuentas(dt.getRubro().getContCc());

                            haber.setAnio(diarioG.getPeriodo().intValue());
                            haber.setDebe(dt.getValor());
                            haber.setIdContCuentas(dt.getRubro().getContCp());

                            haber.setPartidaPresupuestaria(dt.getRubro().getPartida().getCodigo().concat(dt.getRubro().getPartida().getFueteNew().getCodFuente()));
                            haber.setIdPresFuenteFinanciamiento(dt.getRubro().getPartida().getFueteNew());
                            haber.setIdPresCatalogoPresupuestario(dt.getRubro().getPartida());
                            haber.setTipoRegistro(catalogoServiceCtg.getTipoItem(CONFIG.COD_DEVENGADO));
                            detalleDiarioG.add(haber);
                            detalleDiarioG.add(debe);
                        }
                    }
                }
                this.create(diarioG, detalleDiarioG, liquidaciones, Boolean.FALSE);
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    @Override
    public List<ContDiarioGeneralDetalle> getTesoreriaDetalle(List<Long> idDiarioList, Short periodo) {
        return contDiarioGeneralService.getTesoreriaDetalle(idDiarioList, periodo);
    }

    @Override
    public List<ContDiarioGeneral> getTesoreriaList(Integer cod_modulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado, Boolean accion) {
        if (accion) {
            return contDiarioGeneralService.findByNamedQuery("ContDiarioGeneral.findByTesoreriaModulo", cod_modulo, fechaDesde, fechaHasta, tipoSeleccionado);
        } else {
            return contDiarioGeneralService.findByNamedQuery("ContDiarioGeneral.findByTesoreria", cod_modulo, fechaDesde, fechaHasta);
        }
    }

    @Override
    public List<ThTipoRol> getRoles(Short periodo) {
        return thInterfaces.getRolesAprobados(periodo);
    }

    @Override
    public List<ContDiarioGeneralDetalle> getDetalleRol(ThTipoRol thTipoRolSeleccionado) {
        List<RolDetalleModel> temp = thLiquidacionRolDetalleService.getDetalleRol(thTipoRolSeleccionado);
        System.out.println("temp: " + temp.size());
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        int cont = 1;
        if (!temp.isEmpty()) {
            for (RolDetalleModel item : temp) {
                ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
                if (item.getIdcuenta() != null) {
                    detalle.setIdContCuentas(contCuentasService.find(item.getIdcuenta()));
                }
                if (item.getIdestructura() != null) {
                    detalle.setIdPresPlanProgramatico(presPlanProgramaticoService.find(item.getIdestructura()));
                }
                if (item.getIdpresupuesto() != null) {
                    detalle.setIdPresCatalogoPresupuestario(presCatalogoPresupuestarioService.find(item.getIdpresupuesto()));
                }
                if (item.getIdfuente() != null) {
                    detalle.setIdPresFuenteFinanciamiento(presFuenteFinanciamientoService.find(item.getIdfuente()));
                }
                detalle.setDebe(item.getDebe());
                detalle.setHaber(item.getHaber());
                if (item.getPartida() != null) {
                    detalle.setPartidaPresupuestaria(item.getPartida());
                    detalle.setComprometido(item.getDebe());
                    detalle.setDevengado(item.getDebe());
                    detalle.setTipoRegistro(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "diario_general_devengado"));
                }
                detalle.setFactura(Boolean.FALSE);
                detalle.setSaldoRetencion(BigDecimal.ZERO);
                detalle.setSaldoDisponible(BigDecimal.ZERO);
                detalle.setDatoCargado(Boolean.TRUE);
                detalle.setNumeracion(cont);
                result.add(detalle);
                cont += 1;
            }
        }
        return result;
    }

    @Override
    public ThTipoRol getThTipoRol(Long id) {
        return thInterfaces.getThTipoRol(id);
    }

    @Override
    public List<FinaRenTipoLiquidacion> getTipoLiquidacionList() {
        return finaRenTipoLiquidacionService.findAllTipoLiquidacion();
    }

    @Override
    public Boolean isEmisionContabilizada(FinaRenLiquidacion liquidacion) {
        return contContabilidadModuloService.isContabilizada(MOD_CONTABILIDAD.MOD_TES_EMISION, liquidacion.getId());
    }

    @Override
    public void anularEmision(FinaRenLiquidacion liquidacion) {
        contContabilidadModuloService.isAnularEmision(MOD_CONTABILIDAD.MOD_TES_EMISION, liquidacion.getId());
    }

    @Override
    public void anularEmisionGeneral(Long valor1, Long valor2, Short periodo) {
        contContabilidadModuloService.isAnularEmisionGlobal(valor1, valor2, MOD_CONTABILIDAD.MOD_TES_EMISION, periodo);
    }

    @Override
    public List<Long> findRelacionModulos(List<ContDiarioGeneral> idModulos, Integer cod_modulo) {
        return contContabilidadModuloService.getListIdModulos(idModulos, cod_modulo);
    }

    @Override
    public void editRegistroContable(ContDiarioGeneral contDiarioGeneral) {
        contDiarioGeneralService.edit(contDiarioGeneral);
    }

    @Override
    public List<ContDiarioGeneralDetalle> getTesoreriaPredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado, Short periodo) {
        return contDiarioGeneralService.getTesoreriaPredios(codModulo, fechaDesde, fechaHasta, tipoSeleccionado, periodo);
    }

    @Override
    public List<Long> findRelacionModulosPredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado) {
        return contContabilidadModuloService.getListIdModulosPredios(codModulo, fechaDesde, fechaHasta, tipoSeleccionado);
    }

    @Override
    public void updateRegistroContablePredios(Integer codModulo, Date fechaDesde, Date fechaHasta, FinaRenTipoLiquidacion tipoSeleccionado) {
        contDiarioGeneralService.updateRegistroContablePredios(codModulo, fechaDesde, fechaHasta, tipoSeleccionado);
    }

    @Override
    public void anularPorPrescripcion(FinaRenLiquidacion liquidacion) {
        contDiarioGeneralService.anularPorPrescripcion(liquidacion, MOD_CONTABILIDAD.ANULAR_PRESCRIPCION, CONFIG.COD_DEVENGADO);
    }
}
