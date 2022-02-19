/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.implInterfaces;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.DetalleBanco;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ControlCuentaContableService;
import com.origami.sigef.resource.conf.models.MOD_CONTABILIDAD;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroContable;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroPago;
import com.origami.sigef.resource.contabilidad.services.ContBeneficiarioComprobantePagoService;
import com.origami.sigef.resource.contabilidad.services.ContComprobantePagoService;
import com.origami.sigef.resource.contabilidad.services.ContContabilidadModuloService;
import com.origami.sigef.resource.contabilidad.services.ContCuentaPartidaService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralDetalleService;
import com.origami.sigef.resource.talento_humano.entities.ThLiquidacionRol;
import com.origami.sigef.resource.talento_humano.entities.ThTipoRol;
import com.origami.sigef.resource.talento_humano.services.ThLiquidacionRolService;
import com.origami.sigef.resource.talento_humano.services.ThTipoRolService;
import com.origami.sigef.talentohumano.services.detalleBancoServices;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

/**
 *
 * @author Criss Intriago
 */
@Singleton
@ApplicationScoped
public class ImplContRegistroPago implements ContRegistroPago {

    @Inject
    private ContComprobantePagoService comprobantePagoService;
    @Inject
    private ContBeneficiarioComprobantePagoService contBeneficiarioComprobantePagoService;
    @Inject
    private ContDiarioGeneralDetalleService contDiarioGeneralDetalleService;
    @Inject
    private ContRegistroContable contRegistroContable;
    @Inject
    private detalleBancoServices bancoServices;
    @Inject
    private ContCuentaPartidaService contCuentaPartidaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ControlCuentaContableService controlCuentaContableService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ThTipoRolService thTipoRolService;
    @Inject
    private ThLiquidacionRolService thLiquidacionRolService;
    @Inject
    private ContContabilidadModuloService contContabilidadModuloService;

    @Override
    public String validaciones(ContComprobantePago contComprobantePago, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<ContBeneficiarioComprobantePago> beneficiarioComprobantePago) {
        if (contComprobantePago.getPeriodo() == null) {
            return "Debe seleccionar un periodo";
        }
        if (Utils.getAnio(contComprobantePago.getFechaRegistro()) != contComprobantePago.getPeriodo().intValue()) {
            return "El periodo seleccionado no es igual al periodo de la fecha del registro";
        }
        if (controlCuentaContableService.validarPeriodo(contComprobantePago.getFechaRegistro(), contComprobantePago.getPeriodo())) {
            return "El periodo selecionado se encuentra cerrado";
        }
        if (contComprobantePago.getFechaRegistro() == null) {
            return "Debe seleccionar una fecha de registro";
        }
        if (contComprobantePago.getDescripcion() == null || contComprobantePago.getDescripcion().equals("")) {
            return "Debe ingresar una descripción";
        }
        if (contDiarioGeneralDetalles.isEmpty()) {
            return "No existe detalle del comprobante de pago ha registrar";
        }
        if (beneficiarioComprobantePago.isEmpty()) {
            return "No existe ningun beneficiario";
        }
        for (ContBeneficiarioComprobantePago beneficiario : beneficiarioComprobantePago) {
            if (beneficiario.getIdDetalleBanco() == null) {
                return "El beneficiario con el No. de RUC/CI " + beneficiario.getIdCliente().getIdentificacionCompleta() + ", no dispone de una cuenta bancaria";
            }
        }
        return "";
    }

    @Override
    public ContComprobantePago create(ContComprobantePago contComprobantePago, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles, List<ContBeneficiarioComprobantePago> beneficiarioComprobantePago) {
        contComprobantePago.setNumRegistro(comprobantePagoService.nextRegistro(contComprobantePago.getPeriodo()));
        contComprobantePago = comprobantePagoService.create(contComprobantePago);
        //agregar detalle
        for (ContDiarioGeneralDetalle detalle : contDiarioGeneralDetalles) {
            detalle.setIdContComprobantePago(contComprobantePago);
            contDiarioGeneralDetalleService.create(detalle);
        }
        //agregar beneficiario
        for (ContBeneficiarioComprobantePago beneficiario : beneficiarioComprobantePago) {
            beneficiario.setNumRegistro(contBeneficiarioComprobantePagoService.nextRegistro(contComprobantePago.getPeriodo()));
            beneficiario.setIdComprobantePago(contComprobantePago);
            contBeneficiarioComprobantePagoService.create(beneficiario);
        }
        diarioDevengado(contComprobantePago);
        userSession.setIdComprobante(null);
        return contComprobantePago;
    }

    @Override
    public ContComprobantePago edit(ContComprobantePago contComprobantePago, List<ContDiarioGeneralDetalle> contDiarioGeneralDetalles,
            List<ContDiarioGeneralDetalle> contDiarioGeneralDetallesDelete, List<ContBeneficiarioComprobantePago> beneficiarioComprobantePago,
            List<ContBeneficiarioComprobantePago> beneficiarioComprobantePagoDelete) {
        comprobantePagoService.edit(contComprobantePago);
        //editar o agregar detalle
        for (ContDiarioGeneralDetalle detalle : contDiarioGeneralDetalles) {
            if (detalle.getId() != null) {
                contDiarioGeneralDetalleService.edit(detalle);
            } else {
                detalle.setIdContComprobantePago(contComprobantePago);
                contDiarioGeneralDetalleService.create(detalle);
            }
        }
        //eliminar detalle
        for (ContDiarioGeneralDetalle detalle : contDiarioGeneralDetallesDelete) {
            contDiarioGeneralDetalleService.remove(detalle);
        }
        //eliminar beneficiario
        if (beneficiarioComprobantePagoDelete != null) {
            for (ContBeneficiarioComprobantePago beneficiario : beneficiarioComprobantePagoDelete) {
                contBeneficiarioComprobantePagoService.remove(beneficiario);
            }
        }
        //agregar o modificar beneficiario
        for (ContBeneficiarioComprobantePago beneficiario : beneficiarioComprobantePago) {
            if (beneficiario.getId() != null) {
                contBeneficiarioComprobantePagoService.edit(beneficiario);
            } else {
                beneficiario.setNumRegistro(contBeneficiarioComprobantePagoService.nextRegistro(contComprobantePago.getPeriodo()));
                beneficiario.setIdComprobantePago(contComprobantePago);
                contBeneficiarioComprobantePagoService.create(beneficiario);
            }
        }
        diarioDevengado(contComprobantePago);
        userSession.setIdComprobante(null);
        return contComprobantePago;
    }

    public void diarioDevengado(ContComprobantePago comprobante) {
        if (comprobante.getIdContDiarioGeneral() != null) {
            if (contDiarioGeneralDetalleService.diarioDevengadoTotal(comprobante.getIdContDiarioGeneral())) {
                comprobante.getIdContDiarioGeneral().setComprobantePago(Boolean.TRUE);
                contRegistroContable.edit(comprobante.getIdContDiarioGeneral(), null, null);
            }
        }

    }

    @Override
    public ContDiarioGeneral anular(ContComprobantePago contComprobantePago) {
        //Datos iniciales
        List<ContDiarioGeneralDetalle> detalleComprobante = findByIdComprobantePago(contComprobantePago);
        //Crear registro contable
        ContDiarioGeneral registroContable = new ContDiarioGeneral();
        List<ContDiarioGeneralDetalle> detalleList = new ArrayList<>();
        //Registro contable
        registroContable.setDescripcion("ANULACIÓN COMPROBANTE DE PAGO NO. " + contComprobantePago.getNumRegistro() + ", PERIODO " + contComprobantePago.getPeriodo() + "; " + contComprobantePago.getDescripcion());
        registroContable.setPeriodo(contComprobantePago.getPeriodo());
        registroContable.setFechaRegistro(contComprobantePago.getFechaRegistro());
        if (contComprobantePago.getIdContDiarioGeneral() != null) {
            registroContable.setClase(contComprobantePago.getIdContDiarioGeneral().getClase());
            registroContable.setTipo(contComprobantePago.getIdContDiarioGeneral().getTipo());
            registroContable.setCodModulo(contComprobantePago.getIdContDiarioGeneral().getCodModulo());
        } else {
            registroContable.setClase(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "clase_diario"));
            registroContable.setTipo(catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", "tipo_financiero"));
            registroContable.setCodModulo(MOD_CONTABILIDAD.MOD_MANUALES);
        }
        //Suma Debe - Haber
        double totalDebe = 0;
        double totalHaber = 0;
        //Registro del detalle
        for (ContDiarioGeneralDetalle detalle : detalleComprobante) {
            ContDiarioGeneralDetalle aux = Utils.clone(detalle);
            aux.setId(null);
            aux.setIdContComprobantePago(null);
            aux.setDebe(negativo(detalle.getDebe()));
            aux.setHaber(negativo(detalle.getHaber()));
            aux.setComprometido(negativo(detalle.getComprometido()));
            aux.setDevengado(negativo(detalle.getDevengado()));
            aux.setEjecutado(negativo(detalle.getEjecutado()));
            totalDebe = totalDebe + Utils.redondearDosDecimales(aux.getDebe().doubleValue());
            totalHaber = totalHaber + Utils.redondearDosDecimales(aux.getHaber().doubleValue());
            detalleList.add(aux);
        }
        registroContable.setDebe(new BigDecimal(totalDebe));
        registroContable.setHaber(new BigDecimal(totalHaber));
        ContDiarioGeneral result = contRegistroContable.create(registroContable, detalleList, null, true);
        //editar estado de comprobante pago
        contComprobantePago.setEstado(Boolean.FALSE);
        comprobantePagoService.edit(contComprobantePago);
        //editar estado del diario relacionado
        if (contComprobantePago.getIdContDiarioGeneral() != null) {
            contComprobantePago.getIdContDiarioGeneral().setComprobantePago(Boolean.FALSE);
            contRegistroContable.edit(contComprobantePago.getIdContDiarioGeneral(), null, null);
        }
        return result;
    }

    private BigDecimal negativo(BigDecimal monto) {
        if (monto.doubleValue() > 0) {
            return monto.negate();
        } else {
            return monto;
        }
    }

    @Override
    public DetalleBanco beneficiarioBanco(Cliente cliente) {
        return bancoServices.findDetalleCliente(cliente);
    }

    @Override
    public List<ContDiarioGeneralDetalle> detalleComprobantePago(ContDiarioGeneral diarioGeneral) {
        List<ContDiarioGeneralDetalle> result = new ArrayList<>();
        //Cuentas marcadas por comprobante
        List<ContDiarioGeneralDetalle> cuentasComprobante = contDiarioGeneralDetalleService.cuentasComprobanteList(diarioGeneral);
        for (ContDiarioGeneralDetalle temp : cuentasComprobante) {
            double devengado = temp.getHaber().doubleValue();
            ContDiarioGeneralDetalle aux = new ContDiarioGeneralDetalle();
            aux.setIdContCuentas(temp.getIdContCuentas());
            List<ContDiarioGeneralDetalle> cuentasPartidas = contDiarioGeneralDetalleService.cuentasComprobantePartidaList(diarioGeneral);
            for (ContDiarioGeneralDetalle temp1 : cuentasPartidas) {
                if (contCuentaPartidaService.validarRelacion(temp.getIdContCuentas(), temp1.getIdPresCatalogoPresupuestario())) {
                    aux.setIdPresCatalogoPresupuestario(temp1.getIdPresCatalogoPresupuestario());
                    aux.setIdPresPlanProgramatico(temp1.getIdPresPlanProgramatico());
                    aux.setIdPresFuenteFinanciamiento(temp1.getIdPresFuenteFinanciamiento());
                    aux.setPartidaPresupuestaria(temp1.getPartidaPresupuestaria());
                    if (devengado > 0) {
                        if (temp1.getDebe().doubleValue() >= devengado) {
                            aux.setDebe(new BigDecimal(devengado));
                            aux.setEjecutado(new BigDecimal(devengado));
                            devengado = 0;
                            temp1.setDebe(new BigDecimal(temp1.getDebe().doubleValue() - devengado));
                        } else {
                            aux.setDebe(temp1.getDebe());
                            aux.setEjecutado(temp1.getDebe());
                            devengado = devengado - temp1.getDebe().doubleValue();
                            temp1.setDebe(BigDecimal.ZERO);
                        }
                        aux.setNumeracion(result.size() + 1);
                        aux.setMaxiValor(aux.getDebe());
                        ContDiarioGeneralDetalle clone = Utils.clone(aux);
                        result.add(clone);
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ContComprobantePago findById(Long id) {
        return comprobantePagoService.find(id);
    }

    @Override
    public List<ContDiarioGeneralDetalle> findByIdComprobantePago(ContComprobantePago comprobante) {
        return contDiarioGeneralDetalleService.findByNamedQuery("ContDiarioGeneralDetalle.findByIdRegistroPago", comprobante);
    }

    @Override
    public List<ContBeneficiarioComprobantePago> findByIdBeneficiaciosPagos(ContComprobantePago contComprobantePago) {
        return contBeneficiarioComprobantePagoService.findByNamedQuery("ContBeneficiarioComprobantePago.findByComprobantePago", contComprobantePago);
    }

    @Override
    public void ComprobantPagoImprimirReporte(ContComprobantePago contComprobantePago, String tipoDocumento) {
        servletSession.addParametro("id_comprobante", contComprobantePago.getId());
        servletSession.setContentType(tipoDocumento);
        servletSession.setNombreReporte("comprobante_pago");
        servletSession.setNombreSubCarpeta("_contabilidad");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    @Override
    public ContComprobantePago findByNumRegistro(Integer idComprobante, Short periodo) {
        return comprobantePagoService.findByNamedQuery1("ContComprobantePago.findByNumRegistro", idComprobante, periodo);
    }

    @Override
    public void editTransferencia(ContComprobantePago idContComprobantePago) {
        idContComprobantePago.setTransferencia(Boolean.TRUE);
        comprobantePagoService.edit(idContComprobantePago);
    }

    @Override
    public ThTipoRol findThTipoRol(ContDiarioGeneral diario) {
        Long id = contContabilidadModuloService.getReserva(diario);
        if (id != null) {
            return thTipoRolService.find(id);
        } else {
            return null;
        }
    }

    @Override
    public List<ThLiquidacionRol> getThLiquidacionRol(ThTipoRol thTipoRol) {
        return thLiquidacionRolService.getThTipoRolList(thTipoRol);
    }
}
