/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.tesoreria.liquidaciones;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.ItemTarifario;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.talentohumano.services.BancoService;
import com.origami.sigef.tesoreria.entities.CorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.DetalleCorteOrdenCobro;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionDetalle;
import com.origami.sigef.tesoreria.entities.LiquidacionTipo;
import com.origami.sigef.tesoreria.modelTarifario.CorteOrdenCobroModelTS;
import com.origami.sigef.tesoreria.modelTarifario.EntidadFinancieraModelTS;
import com.origami.sigef.tesoreria.modelTarifario.ItemTarifarioModelTService;
import com.origami.sigef.tesoreria.service.CorteOrdenCobroService;
import com.origami.sigef.tesoreria.service.DetalleCorteOrdenCobroService;
import com.origami.sigef.tesoreria.service.ItemTarifarioService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Origami
 */
@Named(value = "ordenCobroView")
@ViewScoped
public class OrdenCobroOC implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private ItemTarifarioModelTService itemTarifarioService;
    @Inject
    private ItemTarifarioService itemService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CorteOrdenCobroService ordenCobroSerrvice;
    @Inject
    private DetalleCorteOrdenCobroService detalleCorteService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private CuentaContableService cuentaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private BancoService bancoService;

    private List<CorteOrdenCobroModelTS> listOrdenCobro;
    private List<CorteOrdenCobroModelTS> listAux;
    private List<CorteOrdenCobroModelTS> aux;
    private List<ItemTarifario> listaItems;
    private List<MasterCatalogo> periodos;
    private List<CorteOrdenCobro> listaCorte;
    private List<CorteOrdenCobro> listaCorteConsulta;
    private List<EntidadFinancieraModelTS> listaEntidadBanco;
    private List<EntidadFinancieraModelTS> auxEntidadBanco;
    private List<CuentaContable> listaCuenta;
    private List<LiquidacionDetalle> listDetalleLiqui;
    private List<Liquidacion> listLiquidacion;
    private List<Banco> entidadBancariaList;

    private LazyModel<DetalleCorteOrdenCobro> lazy;

    private Date fechaCorte;
    private OpcionBusqueda busqueda;
    private CorteOrdenCobro corte;
    private CorteOrdenCobro corteSeleccionado;
    private DetalleCorteOrdenCobro detalleCorte;
    private String codigo;
    private String estado;
    private EntidadFinancieraModelTS banco;
    private CuentaContable cuentaSeleccionada;
    private boolean guardarOC;
    private String item;
    private String fechaCorteReg;
    private String tipoEmision;
    private LiquidacionTipo liquidacionTipo;
    private Banco entidadBancaria;

    @PostConstruct
    protected void init() {
//        fechaCorte = new Date();
        guardarOC = false;
        item = "";
        tipoEmision = "";
        liquidacionTipo = new LiquidacionTipo();
        fechaCorteReg = "";
        busqueda = new OpcionBusqueda();
        entidadBancaria = new Banco();
        listOrdenCobro = new ArrayList<>();
        listaEntidadBanco = new ArrayList<>();
        entidadBancariaList = new ArrayList<>();
        corte = new CorteOrdenCobro();
        corteSeleccionado = new CorteOrdenCobro();
        banco = new EntidadFinancieraModelTS();
        banco.setCuenta(new CuentaContable());
        cuentaSeleccionada = new CuentaContable();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listaCorte = ordenCobroSerrvice.getListaOrdenCobro();
        listaCorteConsulta = ordenCobroSerrvice.listaOrdenCobroEmitidas();
        listaCuenta = cuentaService.getHijosCtaBanco("11101", busqueda.getAnio());
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }

    public void tipoBusqueda() throws ParseException {
        if (tipoEmision.equals("") || tipoEmision == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un Tipo");
            return;
        }
        if (fechaCorte == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione una fecha de corte");
            return;
        }
        if (busqueda.getAnio().intValue() != Utils.getAnio(fechaCorte).intValue()) {
            JsfUtil.addWarningMessage("AVISO!!!", "El periodo selecionado es diferente a de la fecha de corte selecionada");
            return;
        }
        if (ordenCobroSerrvice.getOrdenFechaMax(tipoEmision, busqueda.getAnio()) != null) {
            if (fechaCorte.compareTo(ordenCobroSerrvice.getOrdenFechaMax(tipoEmision, busqueda.getAnio()).getFechaCorte()) == 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada no puede ser menor a las ya registradas.");
                resetValue();
                return;
            }
            if (fechaCorte.compareTo(ordenCobroSerrvice.getOrdenFechaMax(tipoEmision, busqueda.getAnio()).getFechaCorte()) < 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada no puede ser menor a las ya registradas.");
                resetValue();
                return;
            }
        }
        switch (tipoEmision) {
            case "TT":
            case "TC":
                liquidacionTipo = ordenCobroSerrvice.getTipoLiquidacion(tipoEmision);
                buscarCobrosElectronico();
                break;
            default:
                buscar();
                break;
        }
    }

    public void buscar() throws ParseException {
        if (fechaCorte == null) {
            JsfUtil.addErrorMessage("Fecha Corte", "Debe seleccionar la fecha de Corte.");
            return;
        }
        listaCorte = ordenCobroSerrvice.getListaOrdenCobro(tipoEmision, busqueda.getAnio());
        item = "";
        String fechaCt = Utils.dateFormatPattern("yyyy/MM/dd HH:mm", fechaCorte);
        if (!listaCorte.isEmpty()) {
            CorteOrdenCobro corteOrd = ordenCobroSerrvice.getOrdenFechaMax(tipoEmision, busqueda.getAnio());
            String fechaReg = Utils.dateFormatPattern("yyyy/MM/dd HH:mm", corteOrd.getFechaCorte());
            listAux = itemTarifarioService.corteOrdenCobroList(fechaCt, fechaReg);
            auxEntidadBanco = itemTarifarioService.getEntidadFinancieraList(fechaCt, fechaReg);
        } else {
            listAux = itemTarifarioService.corteOrdenCobroList(fechaCt, "");
            auxEntidadBanco = itemTarifarioService.getEntidadFinancieraList(fechaCt, "");
        }
        listaItems = itemService.getListaItem(Short.parseShort(Utils.getAnio(fechaCorte) + ""));
        aux = new ArrayList<>();
        listOrdenCobro.clear();
        if (Utils.isEmpty(listaItems)) {
            JsfUtil.addWarningMessage("Catalogo Item Tarifario para el Périodo de la fecha de corte no Existe.", "");
            return;
        }
        if (!listAux.isEmpty()) {
            for (ItemTarifario it : listaItems) {
                listAux.stream().filter((ct) -> (it.getCodigoItem().equals(ct.getCodigotarifa()) && ct.getSeteado() == false)).map((ct) -> {
                    ct.setItem(it);
                    return ct;
                }).map((ct) -> {
                    ct.setSeteado(true);
                    return ct;
                }).forEachOrdered((ct) -> {
                    listOrdenCobro.add(ct);
                });
            }
            listOrdenCobro.sort(Comparator.comparing(CorteOrdenCobroModelTS::getOrdencobro));
            listaEntidadBanco = listaBanco(auxEntidadBanco, listOrdenCobro);
        }
        if (!listAux.isEmpty()) {
            for (CorteOrdenCobroModelTS ct : listAux) {
                int cont = 0;
                for (ItemTarifario it : listaItems) {
                    cont++;
                    if (it.getCodigoItem().equals(ct.getCodigotarifa())) {
                        cont--;
                    }
                }
                if (cont == listaItems.size() && ct.getCodigotarifa() != null) {
                    item = item + ct.getCodigotarifa() + "-";
                }
            }
            if (!"".equals(item)) {
                guardarOC = true;
                JsfUtil.addWarningMessage("Información", "Verifique que " + item + " Se encuentre registrado en Mantenimiento Item Tarifario");
            }
        }
        codigo = "EMI" + "-" + Utils.completarCadenaConCeros((ordenCobroSerrvice.getCantReg() + 1) + "", 4);
        estado = "EMITIDO";

    }

    public void buscarCobrosElectronico() throws ParseException {
        listDetalleLiqui = new ArrayList<>();
        listLiquidacion = ordenCobroSerrvice.getLiquidacionDetalle(liquidacionTipo);
        Date fechaC = Utils.devolverFecha(fechaCorte, "yyyy/MM/dd");
        List<BigDecimal> valores = new ArrayList<>();
        if (listLiquidacion == null || listLiquidacion.isEmpty()) {
            listDetalleLiqui = ordenCobroSerrvice.getDetalleLiquidacion(fechaC, null, liquidacionTipo);
            valores = ordenCobroSerrvice.getValorTotal(fechaC, null, liquidacionTipo);
        } else {
            listDetalleLiqui = ordenCobroSerrvice.getDetalleLiquidacion(fechaC, listLiquidacion.get(0).getFechaEmision(), liquidacionTipo);
            valores = ordenCobroSerrvice.getValorTotal(fechaC, listLiquidacion.get(0).getFechaEmision(), liquidacionTipo);
        }
        listaEntidadBanco.clear();
        if (valores != null) {
            if (!valores.isEmpty()) {
                for (BigDecimal b : valores) {
                    EntidadFinancieraModelTS ent = new EntidadFinancieraModelTS();
                    ent.setValorTotal(b);
                    listaEntidadBanco.add(ent);
                }
            }
        }
        listOrdenCobro.clear();
        if (listDetalleLiqui != null) {
            if (!listDetalleLiqui.isEmpty()) {
                for (LiquidacionDetalle lq : listDetalleLiqui) {
                    CorteOrdenCobroModelTS model = new CorteOrdenCobroModelTS();
                    model.setCodigotarifa(lq.getItemTarifario().getCodigoItem());
                    model.setFechaemision(lq.getLiquidacion().getFechaEmision() + "");
                    model.setOrdencobro(lq.getLiquidacion().getNumTramite() + "");
                    model.setNombreitem(lq.getItemTarifario().getDescripcion());
                    model.setItem(lq.getItemTarifario());
                    model.setIdentificacion(lq.getLiquidacion().getCliente().getIdentificacionCompleta());
                    model.setNombre(lq.getLiquidacion().getCliente().getNombreCompleto());
                    model.setTotal(lq.getValor());
                    model.setNumeropapeleta(lq.getLiquidacion().getIdLiquidacionRefenrencia() + "");
                    listOrdenCobro.add(model);
                }
            }
        }
        entidadBancariaList = bancoService.getBancoList();
        listOrdenCobro.sort(Comparator.comparing(CorteOrdenCobroModelTS::getOrdencobro));
        codigo = "EMI" + "-" + Utils.completarCadenaConCeros((ordenCobroSerrvice.getCantReg() + 1) + "", 4);
        estado = "EMITIDO";
    }

    public List<EntidadFinancieraModelTS> listaBanco(List<EntidadFinancieraModelTS> bc, List<CorteOrdenCobroModelTS> oc) {
        List<EntidadFinancieraModelTS> listaBco = new ArrayList<>();
        EntidadFinancieraModelTS banco = new EntidadFinancieraModelTS();
        if (!oc.isEmpty()) {
            for (EntidadFinancieraModelTS ban : bc) {
                banco = new EntidadFinancieraModelTS();
                banco.setBanco(ban.getBanco());
                banco.setIdentidadfinanciera(ban.getIdentidadfinanciera());
                Boolean var = Boolean.FALSE;
                for (CorteOrdenCobroModelTS cort : oc) {
                    if (ban.getIdentidadfinanciera().equals(cort.getIdbanco())) {
                        var = Boolean.TRUE;
                        banco.setValorTotal(banco.getValorTotal().add(cort.getTotal()));
                    }
                }
                if (var) {
                    listaBco.add(banco);
                }
            }
        }
        return listaBco;
    }

    public String getDateCorte(Date fecha) {
        if (fecha == null) {
            return fechaCorteReg = "";
        }
        return fechaCorteReg = Utils.dateFormatPattern("dd-MM HH:mm", fecha);
    }

    public void guardar() throws ParseException {
        listaCorte = ordenCobroSerrvice.getListaOrdenCobro(tipoEmision);
        if (listOrdenCobro.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "Verifique que exitan Datos para Registrar.");
            return;
        }
        if (!listaEntidadBanco.isEmpty()) {
            String bco = "";
            for (EntidadFinancieraModelTS bc : listaEntidadBanco) {
                if (bc.getCuenta() == null) {
                    bco = bco + " - " + bc.getBanco();
                }
            }
            if (!"".equals(bco)) {
                JsfUtil.addWarningMessage("Información", "Entidad financiera " + bco + " no se asignó Cuenta Contable");
                return;
            }
        }
        if (guardarOC) {
            JsfUtil.addWarningMessage("Información", "Verifique que " + item + " Se encuentre registrado en Mantenimiento Item Tarifario");
            return;
        }
        boolean edit = corte.getId() != null;
        if (edit) {

        } else {
            corte.setCodigo(codigo);
            corte.setFechaCorte(fechaCorte);
            corte.setEstadoCorte(estado);
            corte.setPeriodo(busqueda.getAnio());
            corte.setCodigoDes(corte.getDescripcion() + "-" + corte.getCodigo() + "-" + Utils.dateFormatPattern("dd-MM HH:mm", corte.getFechaCorte()));
            corte.setCodigoEmitido(corte.getCodigo() + "-" + Utils.dateFormatPattern("dd-MM HH:mm", corte.getFechaCorte()));
            corte.setTipoCorte(tipoEmision);
            corte = ordenCobroSerrvice.create(corte);
//            setearList(corte);
            if (tipoEmision.equals("FISICAS")) {
                setearList(corte);
            } else {
                setearListElect(corte);
                actualizarLiquidacionDetalle();
            }
        }
        JsfUtil.addSuccessMessage("Registro", "Datos Registrado con Exito");
        resetValue();
        JsfUtil.redirectFaces("/tesoreria/oden-cobro/reportes");
    }

    private void actualizarLiquidacionDetalle() {
        if (!listLiquidacion.isEmpty()) {
            for (Liquidacion lq : listLiquidacion) {
                lq.setEstadoVerificado(Boolean.TRUE);
                liquidacionService.edit(lq);
            }
        }
        listLiquidacion = new ArrayList<>();
        listDetalleLiqui = new ArrayList<>();
    }

    public void setearList(CorteOrdenCobro corte) throws ParseException {
        if (!listOrdenCobro.isEmpty()) {
            for (EntidadFinancieraModelTS e : listaEntidadBanco) {
                for (CorteOrdenCobroModelTS c : listOrdenCobro) {
                    if (Objects.equals(e.getIdentidadfinanciera(), c.getIdbanco())) {
                        newDetalleCorte();
                        detalleCorte.setBanco(c.getBanco());
                        detalleCorte.setCodigoTarifa(c.getCodigotarifa());
                        detalleCorte.setComprobanteInterno(c.getComprobanteinterno());
                        detalleCorte.setConceptoTarifario(c.getnombreitem());
                        detalleCorte.setCorteOrdenCobro(corte);
                        detalleCorte.setEstadoOrden(c.getEstadoorden());
                        detalleCorte.setIdentificacion(c.getIdentificacion());
                        detalleCorte.setIdordenCobro(c.getIdordencobro());
                        detalleCorte.setItemTarifa(c.getItem());
                        detalleCorte.setNombre(c.getNombre());
                        detalleCorte.setNumeroPapeleta(c.getNumeropapeleta());
                        detalleCorte.setOrdenCobro(c.getOrdencobro());
                        detalleCorte.setTotal(c.getTotal());
                        detalleCorte.setCuentaCaja(new CuentaContable());
                        detalleCorte.setCuentaCaja(e.getCuenta());
                        detalleCorte.setFechaEmision(c.getFechaemision());
                        detalleCorte.setId_banco(c.getIdbanco());
                        detalleCorte.setPlaca(c.getPlaca());
                        detalleCorte = detalleCorteService.create(detalleCorte);
                    }
                }
            }
        }
    }

    public void setearListElect(CorteOrdenCobro corte) throws ParseException {
        if (!listOrdenCobro.isEmpty()) {
            for (EntidadFinancieraModelTS e : listaEntidadBanco) {
                for (CorteOrdenCobroModelTS c : listOrdenCobro) {
//                    if (Objects.equals(e.getIdentidadfinanciera(), c.getIdbanco())) {
                    newDetalleCorte();
                    detalleCorte.setBanco(c.getBanco());
                    detalleCorte.setCodigoTarifa(c.getCodigotarifa());
                    detalleCorte.setComprobanteInterno(c.getComprobanteinterno());
                    detalleCorte.setConceptoTarifario(c.getnombreitem());
                    detalleCorte.setCorteOrdenCobro(corte);
                    detalleCorte.setEstadoOrden(c.getEstadoorden());
                    detalleCorte.setIdentificacion(c.getIdentificacion());
                    detalleCorte.setIdordenCobro(c.getIdordencobro());
                    detalleCorte.setItemTarifa(c.getItem());
                    detalleCorte.setNombre(c.getNombre());
                    detalleCorte.setNumeroPapeleta(c.getNumeropapeleta());
                    detalleCorte.setOrdenCobro(c.getOrdencobro());
                    detalleCorte.setTotal(c.getTotal());
                    detalleCorte.setCuentaCaja(new CuentaContable());
                    detalleCorte.setCuentaCaja(e.getCuenta());
                    detalleCorte.setFechaEmision(c.getFechaemision());
                    detalleCorte.setId_banco(c.getIdbanco());
                    detalleCorte.setPlaca(c.getPlaca());
                    detalleCorte.setVerificado(Boolean.TRUE);
                    detalleCorte.setId_banco(Long.parseLong(e.getEntidadBancaria().getCuentaCorriente()));
                    detalleCorte.setBanco(e.getEntidadBancaria().getNombreBanco());
                    detalleCorte = detalleCorteService.create(detalleCorte);

                }
            }
        }
    }

//    public void rederict(boolean var) {
//        if (var) {
//            JsfUtil.redirectFaces("/tesoreria/oden-cobro/reportes");
//        } else {
//            JsfUtil.redirectFaces("/tesoreria/oden-cobro");
//        }
//    }
    public void eliminar() {
        if (corteSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de Seleccionar un Orden de Corte de Cobro");
            return;
        }
        if (corteSeleccionado.getDiarioGeneralEmision() != null && !corteSeleccionado.getDiarioGeneralEmision().getEstadoDiario().equals("ANULADO")) {
            JsfUtil.addWarningMessage("Advertencia", "La EMISIÓN seleccionada consta en los Registros del Diario general En estado: " + corteSeleccionado.getDiarioGeneralEmision().getEstadoDiario());
            return;
        }
        if (corteSeleccionado.getEstadoCorte().equals("EMITIDO")) {
            corteSeleccionado.setEstado(Boolean.FALSE);
            ordenCobroSerrvice.edit(corteSeleccionado);
            corteSeleccionado = new CorteOrdenCobro();
            buscarOrden();
            listaCorte = ordenCobroSerrvice.getListaOrdenCobro(tipoEmision);
        } else {
            JsfUtil.addWarningMessage("Información", "El Corte de Cobro esta en estado: " + corteSeleccionado.getEstadoCorte());
        }
    }

    public void cancelar() {
        corteSeleccionado = new CorteOrdenCobro();
        buscarOrden();
    }

    public void buscarOrden() {
        lazy = new LazyModel<>(DetalleCorteOrdenCobro.class
        );
        lazy.getFilterss().put("corteOrdenCobro", corteSeleccionado);
        lazy.getSorteds().put("ordenCobro", "ASC");
    }

    public void newDetalleCorte() {
        detalleCorte = new DetalleCorteOrdenCobro();
        detalleCorte.setCorteOrdenCobro(new CorteOrdenCobro());
        detalleCorte.setItemTarifa(new ItemTarifario());
    }

    public void resetValue() {
        corte = new CorteOrdenCobro();
        newDetalleCorte();
        listOrdenCobro = new ArrayList<>();
        listaEntidadBanco = new ArrayList<>();
        newBanco();
        codigo = "";
        estado = "";
        item = "";
        guardarOC = false;
    }

    public void newBanco() {
        banco = new EntidadFinancieraModelTS();
        banco.setCuenta(new CuentaContable());
    }

    public void reporteDetalle() {
        if (corteSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de Seleccionar un Orden de Corte de Cobro");
            return;
        }
        serveltSession.addParametro("id_corte", corteSeleccionado.getId());
        ParametrosTesorera();
        serveltSession.setNombreReporte("DetalleDevengado");
        serveltSession.setNombreSubCarpeta("OrdenCobro");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void reporteResumen() {
        if (corteSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de Seleccionar un Orden de Corte de Cobro");
            return;
        }
        serveltSession.addParametro("id_corte", corteSeleccionado.getId());
        ParametrosTesorera();
        serveltSession.setNombreReporte("ResumenCobro");
        serveltSession.setNombreSubCarpeta("OrdenCobro");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void reporteDetalleTransacciones() {
        if (corteSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de Seleccionar un Orden de Corte de Cobro");
            return;
        }
        serveltSession.addParametro("id_corte", corteSeleccionado.getId());
        ParametrosTesorera();
        serveltSession.setNombreReporte("DetalleTransacciones");
        serveltSession.setNombreSubCarpeta("OrdenCobro");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void reporteCaja() {
        BigDecimal bco, cta;
        if (corteSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de Seleccionar un Orden de Corte de Cobro");
            return;
        }
        cta = detalleCorteService.getvalorCuenta(corteSeleccionado);
        bco = detalleCorteService.getvalorBanco(corteSeleccionado);
        serveltSession.addParametro("SUBREPORT_DIR", JsfUtil.getRealPath("/reportes/"));
        serveltSession.addParametro("id_corte", corteSeleccionado.getId());
        serveltSession.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId().getUrlLogoReporte());
        serveltSession.addParametro("valor_cta", cta);
        serveltSession.addParametro("valor_bco", bco);
        ParametrosTesorera();
        serveltSession.setNombreReporte("CobroCaja");
        serveltSession.setNombreSubCarpeta("OrdenCobro");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    private void ParametrosTesorera() {
//        Cliente tesorero = clienteService.getClienteEspecificos(RolUsuario.tesorero);
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.tesorero));
        serveltSession.addParametro("jefeTesorero", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.tesorero).getTexto());
        serveltSession.addParametro("ci_tesorero", distributivo.getServidorPublico().getPersona().getIdentificacion());
        serveltSession.addParametro("nombre_tesorero", distributivo.getServidorPublico().getPersona().getNombreCompleto());
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public void cuentaEntidad(EntidadFinancieraModelTS bco) {
        bco.setCuenta(cuentaSeleccionada);
    }

    public void entidadBancaria(EntidadFinancieraModelTS bco) {
        bco.setEntidadBancaria(entidadBancaria);
    }

    public List<CorteOrdenCobroModelTS> getListOrdenCobro() {
        return listOrdenCobro;
    }

    public String getTipoEmision() {
        return tipoEmision;
    }

    public void setTipoEmision(String tipoEmision) {
        this.tipoEmision = tipoEmision;
    }

    public List<CorteOrdenCobro> getListaCorteConsulta() {
        return listaCorteConsulta;
    }

    public void setListaCorteConsulta(List<CorteOrdenCobro> listaCorteConsulta) {
        this.listaCorteConsulta = listaCorteConsulta;
    }

    public void setListOrdenCobro(List<CorteOrdenCobroModelTS> listOrdenCobro) {
        this.listOrdenCobro = listOrdenCobro;
    }

    public String getFechaCorteReg() {
        return fechaCorteReg;
    }

    public void setFechaCorteReg(String fechaCorteReg) {
        this.fechaCorteReg = fechaCorteReg;
    }

    public List<CorteOrdenCobroModelTS> getListAux() {
        return listAux;
    }

    public void setListAux(List<CorteOrdenCobroModelTS> listAux) {
        this.listAux = listAux;
    }

    public Date getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(Date fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    public CorteOrdenCobro getCorte() {
        return corte;
    }

    public void setCorte(CorteOrdenCobro corte) {
        this.corte = corte;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public CorteOrdenCobro getCorteSeleccionado() {
        return corteSeleccionado;
    }

    public void setCorteSeleccionado(CorteOrdenCobro corteSeleccionado) {
        this.corteSeleccionado = corteSeleccionado;
    }

    public List<CorteOrdenCobro> getListaCorte() {
        return listaCorte;
    }

    public void setListaCorte(List<CorteOrdenCobro> listaCorte) {
        this.listaCorte = listaCorte;
    }

    public LazyModel<DetalleCorteOrdenCobro> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<DetalleCorteOrdenCobro> lazy) {
        this.lazy = lazy;
    }

    public List<EntidadFinancieraModelTS> getListaEntidadBanco() {
        return listaEntidadBanco;
    }

    public void setListaEntidadBanco(List<EntidadFinancieraModelTS> listaEntidadBanco) {
        this.listaEntidadBanco = listaEntidadBanco;
    }

    public EntidadFinancieraModelTS getBanco() {
        return banco;
    }

    public void setBanco(EntidadFinancieraModelTS banco) {
        this.banco = banco;
    }

    public CuentaContable getCuentaSeleccionada() {
        return cuentaSeleccionada;
    }

    public void setCuentaSeleccionada(CuentaContable cuentaSeleccionada) {
        this.cuentaSeleccionada = cuentaSeleccionada;
    }

    public List<CuentaContable> getListaCuenta() {
        return listaCuenta;
    }

    public void setListaCuenta(List<CuentaContable> listaCuenta) {
        this.listaCuenta = listaCuenta;
    }

    public List<Banco> getEntidadBancariaList() {
        return entidadBancariaList;
    }

    public void setEntidadBancariaList(List<Banco> entidadBancariaList) {
        this.entidadBancariaList = entidadBancariaList;
    }

    public Banco getEntidadBancaria() {
        return entidadBancaria;
    }

    public void setEntidadBancaria(Banco entidadBancaria) {
        this.entidadBancaria = entidadBancaria;
    }

//</editor-fold>    
}
