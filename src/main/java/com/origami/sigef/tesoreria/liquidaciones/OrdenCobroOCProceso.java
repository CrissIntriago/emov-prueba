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
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
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
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Luis Pozo Gonzabay
 */
@Named(value = "ordenCobroProcesoView")
@ViewScoped
public class OrdenCobroOCProceso extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(OrdenCobroOCProceso.class.getName());

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
    private BancoService bancoService;
    @Inject
    private LiquidacionService liquidacionService;

    private List<CorteOrdenCobroModelTS> listOrdenCobro;
    private List<CorteOrdenCobroModelTS> listAux;
    private List<CorteOrdenCobroModelTS> aux;
    private List<ItemTarifario> listaItems;
    private List<MasterCatalogo> periodos;
    private List<CorteOrdenCobro> listaCorte;
    private List<EntidadFinancieraModelTS> listaEntidadBanco;
    private List<EntidadFinancieraModelTS> listaEntidadBancoAux;
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

    //proceso
    @Inject
    private FileUploadDoc uploadDoc;

    private List<UploadedFile> files;
    private String observaciones;

    @PostConstruct
    protected void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                fechaCorte = new Date();
                guardarOC = false;
                item = "";
                fechaCorteReg = "";
                liquidacionTipo = new LiquidacionTipo();
                entidadBancaria = new Banco();
                busqueda = new OpcionBusqueda();
                listOrdenCobro = new ArrayList<>();
                listaEntidadBanco = new ArrayList<>();
                listaEntidadBancoAux = new ArrayList<>();
                corte = new CorteOrdenCobro();
                corteSeleccionado = new CorteOrdenCobro();
                banco = new EntidadFinancieraModelTS();
                banco.setCuenta(new CuentaContable());
                cuentaSeleccionada = new CuentaContable();
                periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
                listaCorte = ordenCobroSerrvice.listaOrdenCobroEmitidasProcess(tramite.getNumTramite());
                listaCuenta = cuentaService.getHijosCtaBanco("11101", busqueda.getAnio());
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void buscar() {
        listaCorte = ordenCobroSerrvice.getListaOrdenCobro(tipoEmision);
        String fechaCt = Utils.dateFormatPattern("yyyy/MM/dd HH:mm", fechaCorte);
        if (ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio()) != null) {
//            for (CorteOrdenCobro c : listaCorte) {
//                if (fechaCorte.compareTo(c.getFechaCorte()) == 0) {
//                    JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada ya se encuentra registrada.");
//                    resetValue();
//                    return;
//                }
//                if (fechaCorte.compareTo(c.getFechaCorte()) < 0) {
//                    JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada no puede ser menor a las ya registradas.");
//                    resetValue();
//                    return;
//                }
            if (fechaCorte.compareTo(ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio()).getFechaCorte()) == 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada no puede ser menor a las ya registradas.");
                resetValue();
                return;
            }
            if (fechaCorte.compareTo(ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio()).getFechaCorte()) < 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada no puede ser menor a las ya registradas.");
                resetValue();
                return;
            }
            CorteOrdenCobro corteOrd = ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio());
            String fechaReg = Utils.dateFormatPattern("yyyy/MM/dd HH:mm", corteOrd.getFechaCorte());
            listAux = itemTarifarioService.corteOrdenCobroList(fechaCt, fechaReg);
            listaEntidadBancoAux = itemTarifarioService.getEntidadFinancieraList(fechaCt, fechaReg);
        } else {
            listAux = itemTarifarioService.corteOrdenCobroList(fechaCt, "");
            listaEntidadBancoAux = itemTarifarioService.getEntidadFinancieraList(fechaCt, "");
        }
        listaItems = itemService.getListaItem(Short.parseShort(Utils.getAnio(fechaCorte) + ""));
        aux = new ArrayList<>();
        listOrdenCobro.clear();
        if (Utils.isEmpty(listaItems)) {
            JsfUtil.addWarningMessage("Catalogo Item Tarifario para el Periodo actual no existe.", "");
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
            listaEntidadBanco = listaBanco(listaEntidadBancoAux, listOrdenCobro);
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
                    item = item + "-" + ct.getCodigotarifa();
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

    public void tipoBusqueda() throws ParseException {
        if (tipoEmision.equals("") || tipoEmision == null) {
            JsfUtil.addWarningMessage("Seleccione un Tipo", "");
            return;
        }
        if (ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio()) != null) {
            if (fechaCorte.compareTo(ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio()).getFechaCorte()) == 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de corte ingresada no puede ser menor a las ya registradas.");
                resetValue();
                return;
            }
            if (fechaCorte.compareTo(ordenCobroSerrvice.getOrdenFechaMax(tipoEmision,busqueda.getAnio()).getFechaCorte()) < 0) {
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

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            switch (getTramite().getTipoTramite().getAbreviatura()) {
                case "PPS_profesionales":
                    getParamts().put("usuarioTe", clienteService.getrolsUser(RolUsuario.tesorero));
                    break;
                default:
                    getParamts().put("revision", clienteService.getrolsUser(RolUsuario.controlPrevio));
                    break;
            }
            if (saveTramite() == null) {
                return;
            }
            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());
            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
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

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void dlgSubirArchivos() {
        PrimeFaces.current().executeScript("PF('dlgDocumentos').show()");
        PrimeFaces.current().ajax().update(":requisitoDialogForm");

    }

    public void adjuntarArchivos() {
        PrimeFaces.current().executeScript("PF('dlgAdjuntar').show()");
        PrimeFaces.current().ajax().update(":adjuntarDialogForm");

    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            if (files == null) {
                files = new ArrayList<>();
            }
            files.add(event.getFile());
            if (files != null) {
                uploadDoc.upload(tramite, files);
                files.clear();
            }
            PrimeFaces.current().executeScript("PF('dlgAdjuntar').hide()");
            PrimeFaces.current().ajax().update(":requisitoDialogForm");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select ", e);
            System.out.println("error al subir el archivo " + e);
        }
    }

    public String obtenerHoraMinutos(Date fecha) {
        int horas, minutos;
        Calendar format = Calendar.getInstance();
        format.setTime(fecha);
        horas = format.get(Calendar.HOUR_OF_DAY);
        minutos = format.get(Calendar.MINUTE);
        return horas + ":" + minutos;
    }

    public Date fechaZone(Date fecha) throws ParseException {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
// o SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z"); 
        formato.setTimeZone(TimeZone.getTimeZone("UTC"));
        String fechaFormat = formato.format(fecha);
        Date fechaCorte = new Date();
        return fechaCorte = formato.parse(fechaFormat);
    }

    public String getDateCorte(Date fecha) {
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
            corte.setNumTramite(tramite.getNumTramite());
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
        JsfUtil.redirectFaces("/procesos/tesoreria/cierre-caja-chica/reportesRecaudacion");
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

    public void setearList(CorteOrdenCobro corte) throws ParseException {
        if (!listOrdenCobro.isEmpty()) {
            detalleCorteService.guardarListaDetalle(listaEntidadBanco, listOrdenCobro, corte);
        }
    }

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
//            detalleCorteService.deleteDetalleCorte(corteSeleccionado);
            corteSeleccionado = new CorteOrdenCobro();
//            buscarOrden();
            listaCorte = ordenCobroSerrvice.getListaOrdenCobro(tipoEmision);
            JsfUtil.redirectFaces("/procesos/tesoreria/cierre-caja-chica/recaudacion-diaria");
        } else {
            JsfUtil.addWarningMessage("Información", "El Corte de Cobro esta en estado: " + corteSeleccionado.getEstadoCorte());
        }
    }

    public void cancelar() {
        corteSeleccionado = new CorteOrdenCobro();
        buscarOrden();
    }

    public void buscarOrden() {
        lazy = new LazyModel<>(DetalleCorteOrdenCobro.class);
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
        Distributivo tesorero = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.tesorero));
        serveltSession.addParametro("jefeTesorero", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.tesorero).getTexto());
        serveltSession.addParametro("ci_tesorero", tesorero.getServidorPublico().getPersona().getIdentificacion());
        serveltSession.addParametro("nombre_tesorero", tesorero.getServidorPublico().getPersona().getNombreCompleto());
    }

    public void entidadBancaria(EntidadFinancieraModelTS bco) {
        bco.setEntidadBancaria(entidadBancaria);
    }

//<editor-fold defaultstate="collapsed" desc="Getter and Setter">
    public void cuentaEntidad(EntidadFinancieraModelTS bco) {
        bco.setCuenta(cuentaSeleccionada);
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

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
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
