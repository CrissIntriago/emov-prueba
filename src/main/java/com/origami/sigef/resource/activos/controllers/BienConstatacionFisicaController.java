/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.activos.controllers;

import com.origami.sigef.activos.service.BienesItemService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActivoFijoServidor;
import com.origami.sigef.common.entities.BienesItem;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Valores;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.activos.entities.BienConstatacionFisica;
import com.origami.sigef.resource.activos.entities.BienConstatacionFisicaDetalle;
import com.origami.sigef.resource.activos.services.BienConstatacionFisicaDetalleService;
import com.origami.sigef.resource.activos.services.BienConstatacionFisicaService;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.resource.talento_humano.entities.ThServidorCargo;
import com.origami.sigef.resource.talento_humano.services.ThServidorCargoService;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.SelectEvent;

/**
 * @author Sandra Arroba
 * @author Luis Pozo Gonzabay
 */
@Named(value = "bienConstatacionFisicaView")
@ViewScoped
public class BienConstatacionFisicaController implements Serializable {

    private BienConstatacionFisica constatacionBienes;
    private BienConstatacionFisicaDetalle detalleConstatacionBienes;
    private Servidor servidor;
    private Cliente cliente;
    private BienConstatacionFisica constatacionGlobal;
    private BienesItem bienesItem;
    private Valores valores;

    private LazyModel<BienConstatacionFisica> lazyConstatacionFisicaBienes;
    private List<BienesItem> listBienesItem;
    private List<UnidadAdministrativa> listUnidad;
    private List<BienConstatacionFisicaDetalle> listDetalleConstatacionBienes;
    private List<BienConstatacionFisica> listConstatacionFisicaBienes;
    private List<ActivoFijoServidor> listActivoFijoServidor;
    private List<CatalogoItem> listEstadoBien;
    private List<CatalogoItem> listEstadosConstatac;

    private Boolean bandera = Boolean.TRUE;
    private Boolean direccionBol;
    private Boolean departamentoBol;
    private Boolean unidadBol;
    private Boolean botonSave = Boolean.TRUE;
    private Boolean botonGenerar = Boolean.TRUE;
    private Boolean imprimirBol = Boolean.TRUE;
    private Boolean disEdit = Boolean.FALSE;
    private Boolean estadoObservacion = Boolean.FALSE;
    private Boolean editEstadoObservacion = Boolean.FALSE;
    private ThServidorCargo cargoSelectData;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private BienConstatacionFisicaService constatacionBienesService;
    @Inject
    private BienConstatacionFisicaDetalleService constatacionDetalleBienesService;
    @Inject
    private BienesItemService bienesItemService;
    @Inject
    private ServletSession servletSess;
    @Inject
    private ValoresService valoresService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServidorService servidorService;
    @Inject
    private ThServidorCargoService thServidorCargoService;

    @PostConstruct
    public void initView() {
        constatacionBienes = new BienConstatacionFisica();
        detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
        constatacionGlobal = new BienConstatacionFisica();
        servidor = new Servidor();
        cliente = new Cliente();
        bienesItem = new BienesItem();
        servidor.setPersona(new Cliente());
        constatacionBienes.setCustodio(new Servidor());
        lazyConstatacionFisicaBienes = new LazyModel<>(BienConstatacionFisica.class);
        lazyConstatacionFisicaBienes.getFilterss().put("estado", Boolean.TRUE);
        listEstadoBien = catalogoItemService.findCatalogoItems("estado_bien");
        listDetalleConstatacionBienes = new ArrayList<>();
        listConstatacionFisicaBienes = new ArrayList<>();
        listActivoFijoServidor = new ArrayList<>();
        listBienesItem = new ArrayList<>();
        listUnidad = new ArrayList<>();
        listEstadosConstatac = new ArrayList<>();
        listEstadosConstatac = constatacionBienesService.findAllEstados("estados_constatacion_fisica_bienes");
        valores = valoresService.findByNamedQuery1("Valores.findCodeLike", "RUTA_IMAGEN_SERVICE");
    }

    public void newConstatacion(BienConstatacionFisica consta, Boolean edit) {
        Subject subject = SecurityUtils.getSubject();
        imprimirBol = Boolean.TRUE;
        cargoSelectData = new ThServidorCargo();
        if (consta == null) {
            constatacionBienes = new BienConstatacionFisica();
            listActivoFijoServidor = new ArrayList<>();
            listDetalleConstatacionBienes = new ArrayList<>();
            servidor = new Servidor();
            cliente = new Cliente();
            servidor.setPersona(new Cliente());
            botonGenerar = Boolean.FALSE;
            estadoObservacion = Boolean.FALSE;
            disEdit = Boolean.FALSE;
            constatacionBienes.setEstadoConstatacion(catalogoItemService.getEstadoConstatacion("NEW-CF-BIEN", "estados_constatacion_fisica_bienes"));
            Long orden = constatacionBienesService.getOrderConstatacionBien();
            constatacionBienes.setFechaConstatacion(new Date());
            constatacionBienes.setFechaCreacion(new Date());
            constatacionBienes.setEstado(Boolean.TRUE);
            constatacionBienes.setOrden(orden);
            String codigo = Utils.completarCadenaConCeros(constatacionBienes.getOrden() + "", 4);
            constatacionBienes.setCodigo("CFB-" + codigo);
            constatacionBienes.setUsuarioCreacion(subject.getPrincipal().toString());
            bandera = Boolean.FALSE;
            PrimeFaces.current().executeScript("PF('dlgConstatacion').show()");
        } else if (consta.getEstadoConstatacion().getCodigo().equals("EMI-CF-BIEN") && !edit) {
            disEdit = Boolean.TRUE;
            botonGenerar = Boolean.TRUE;
            estadoObservacion = Boolean.TRUE;
            editEstadoObservacion = Boolean.FALSE;
            botonSave = Boolean.FALSE;
            this.constatacionBienes = consta;
            if (constatacionBienes.getCustodioBoolean()) {
                servidor = constatacionBienes.getCustodio();
                if (servidor != null) {
                    cargoSelectData = thServidorCargoService.findByThServidor(servidor);
                }
            }
            listDetalleConstatacionBienes = constatacionDetalleBienesService.getListConstatacionDetalleBienesByConstatacion(constatacionBienes);
            if (!listDetalleConstatacionBienes.isEmpty()) {
                for (int i = 0; i < listDetalleConstatacionBienes.size(); i++) {
                    listDetalleConstatacionBienes.get(i).setUrlImagen((valores.getValorString() + "CONSTATACIONBIEN/id/" + listDetalleConstatacionBienes.get(i).getId()) == null ? "" : (valores.getValorString() + "CONSTATACIONBIEN/id/" + listDetalleConstatacionBienes.get(i).getId()));
                }
            }
            PrimeFaces.current().executeScript("PF('dlgConstatacion').show()");
        } else if (consta.getEstadoConstatacion().getCodigo().equals("REG-CF-BIEN") && !edit) {

            disEdit = Boolean.TRUE;
            botonGenerar = Boolean.TRUE;
            botonSave = Boolean.TRUE;
            estadoObservacion = Boolean.TRUE;
            editEstadoObservacion = Boolean.TRUE;
            this.constatacionBienes = consta;
            if (constatacionBienes.getCustodioBoolean()) {
                servidor = constatacionBienes.getCustodio();
                cargoSelectData = thServidorCargoService.findByThServidor(servidor);
                if (cargoSelectData != null) {
                    constatacionBienes.setCargo(cargoSelectData.getIdCargo().getNombreCargo());
                }
            }
            listDetalleConstatacionBienes = constatacionDetalleBienesService.getListConstatacionDetalleBienesByConstatacion(constatacionBienes);
            if (!listDetalleConstatacionBienes.isEmpty()) {
                for (BienConstatacionFisicaDetalle constFisicaDetBienes : listDetalleConstatacionBienes) {
                    constFisicaDetBienes.getBienesItem().setEstadoBienConst(constFisicaDetBienes.getEstadoBien());
                    constFisicaDetBienes.getBienesItem().setObservAdicional(constFisicaDetBienes.getObservacion());
                    constFisicaDetBienes.setUrlImagen((valores.getValorString() + "CONSTATACIONBIEN/id/" + constFisicaDetBienes.getId()) == null ? "" : (valores.getValorString() + "CONSTATACIONBIEN/id/" + constFisicaDetBienes.getId()));
                }
            }
            PrimeFaces.current().executeScript("PF('dlgConstatacion').show()");
        }
    }

    public void buscarUnidad() {
        Utils.openDialog("/facelet/talentoHumano/unidadAdministrativa/dialogUnidadAdministrativa", "55%", "500");
    }

    public void mostrarImgDialog(BienConstatacionFisicaDetalle constatacion) {
        if (constatacion != null) {
            detalleConstatacionBienes = constatacion;
        } else {
            detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
        }
        PrimeFaces.current().ajax().update("formImg");
        PrimeFaces.current().executeScript("PF('imagenDialog').show()");
    }

    public void limpiarLista() {
        listActivoFijoServidor = new ArrayList<>();
        listDetalleConstatacionBienes = new ArrayList<>();
    }

    public void limpiarFuncionario() {
        servidor = new Servidor();
        constatacionBienes.setCustodio(null);
        constatacionBienes.setCargo(null);
        constatacionBienes.setUnidad(null);
        servidor.setPersona(new Cliente());
        listDetalleConstatacionBienes = new ArrayList<>();
        cargoSelectData = new ThServidorCargo();
    }

    public void buscarServidor() {
        listDetalleConstatacionBienes = new ArrayList<>();
        constatacionBienes.setCustodio(new Servidor());
        if (servidor.getPersona().getIdentificacion() != null) {
            Servidor serv = constatacionBienesService.findByIdentificacion(servidor.getPersona().getIdentificacion());
            if (serv != null) {
                this.servidor = serv;
                constatacionBienes.setCustodio(servidor);
                ThServidorCargo cargo = thServidorCargoService.findByThServidor(servidor);
                if (cargo != null) {
                    constatacionBienes.setCargo(cargo.getIdCargo().getNombreCargo());
                } else {
                    System.out.println("no encuentra cargo para este servidor publico");
                    JsfUtil.addWarningMessage("Advertencia", "El servidor no tiene un cargo, comuniquese con administrador de sistemas.");
                }
                getListBienesByServidor();
                setearUnidadAdministrativa();
                botonSave = Boolean.FALSE;
                if (listDetalleConstatacionBienes.isEmpty()) {
                    botonSave = Boolean.TRUE;
                    JsfUtil.addWarningMessage("Advertencia", "No hay bienes asignados al Funcionario");
                }
            } else {
                Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "457");
            }
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "457");
        }
    }

    public void selectDataServidor(SelectEvent evt) {
        servidor = ((Servidor) evt.getObject());
        listActivoFijoServidor = new ArrayList<>();
        constatacionBienes.setCustodio(servidor);
        cargoSelectData = thServidorCargoService.findByThServidor(servidor);
        if (cargoSelectData != null) {
            constatacionBienes.setCargo(cargoSelectData.getIdCargo().getNombreCargo());
        } else {
            System.out.println("no encuentra cargo para este servidor publico");
            JsfUtil.addWarningMessage("Advertencia", "El servidor no tiene un cargo, comuniquese con administrador de sistemas.");
        }

        getListBienesByServidor();
        botonSave = Boolean.FALSE;
        if (listDetalleConstatacionBienes.isEmpty()) {
            botonSave = Boolean.TRUE;
            JsfUtil.addWarningMessage("Advertencia", "No hay bienes asignados al Funcionario");
        }
        setearUnidadAdministrativa();
    }

    public void selectDataUnidad(SelectEvent evt) {
        constatacionBienes.setUnidad((UnidadAdministrativa) evt.getObject());
        generar();
    }

    public void getListBienesByServidor() {
        listDetalleConstatacionBienes = new ArrayList<>();
        ThServidorCargo cargo = thServidorCargoService.findByThServidor(servidor);
        if (cargo != null) {
            if (cargo.getIdCargo().getNombreCargo().equals("GUARDALMACÉN")) {
                listActivoFijoServidor = constatacionBienesService.getListActivoFijoServByGuardalmacen(servidor);
            } else {
                listActivoFijoServidor = constatacionBienesService.getListActivoFijoServByServidor(servidor);
                int n = listActivoFijoServidor.size();
            }
            for (ActivoFijoServidor activoFijoServidor : listActivoFijoServidor) {
                detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
                detalleConstatacionBienes.setBienesItem(activoFijoServidor.getBienesItem());
                detalleConstatacionBienes.setCustodioBien(activoFijoServidor.getActivoFijoCustodio().getServidor());
                listDetalleConstatacionBienes.add(detalleConstatacionBienes);

            }
        }
    }

    public void setearUnidadAdministrativa() {
        ThServidorCargo cargo = thServidorCargoService.findByThServidor(servidor);
        if (cargo != null) {
            constatacionBienes.setUnidad(cargo.getIdCargo().getIdUnidad());
        } else {
            System.out.println("no encuentra cargo para este servidor publico");
            JsfUtil.addWarningMessage("Advertencia", "El servidor no tiene un cargo, comuniquese con administrador de sistemas.");
        }
    }

    public void generar() {
        constatacionBienes.setCustodio(null);
        listActivoFijoServidor = getBienes(constatacionBienes.getUnidad());
        if (listActivoFijoServidor.isEmpty()) {
            JsfUtil.addWarningMessage("ERROR", "No hay bienes asignados a esta Unidad Administrativa");
            botonSave = Boolean.TRUE;
        } else {
            for (ActivoFijoServidor activoFijoServidor : listActivoFijoServidor) {
                detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
                detalleConstatacionBienes.setBienesItem(activoFijoServidor.getBienesItem());
                detalleConstatacionBienes.setCustodioBien(activoFijoServidor.getActivoFijoCustodio().getServidor());
                listDetalleConstatacionBienes.add(detalleConstatacionBienes);

            }
            botonSave = Boolean.FALSE;
        }

    }

    public List<ActivoFijoServidor> getBienes(UnidadAdministrativa unidad) {
        List<ActivoFijoServidor> bienes = new ArrayList<>();
        if (Utils.isNotEmpty(unidad.getUnidadAdministrativaList())) {
            for (UnidadAdministrativa ua : unidad.getUnidadAdministrativaList()) {
                List<ActivoFijoServidor> bienesTemp = getBienes(ua);
                if (bienesTemp != null) {
                    bienes.addAll(bienesTemp);
                }
            }
            List<ActivoFijoServidor> bienesUnidadIngresada = constatacionBienesService.getListActivoFijoServByUnidadAdministrativa(unidad);
            if (bienesUnidadIngresada != null) {
                bienes.addAll(bienesUnidadIngresada);
            }
        } else {
            List<ActivoFijoServidor> bienesTemp = constatacionBienesService.getListActivoFijoServByUnidadAdministrativa(unidad);
            if (bienesTemp != null) {
                bienes.addAll(bienesTemp);
            }
        }
        return bienes;
    }

    public void limpiar() {
        if (constatacionBienes.getCustodioBoolean() != null) {
            if (!constatacionBienes.getCustodioBoolean()) {
                servidor = new Servidor();
                servidor.setPersona(new Cliente());
                constatacionBienes.setCustodio(new Servidor());
                constatacionBienes.setUnidad(null);
            } else {
                servidor = new Servidor();
                servidor.setPersona(new Cliente());
                constatacionBienes.setUnidad(null);
                constatacionBienes.setCustodio(new Servidor());
            }
        }
        listDetalleConstatacionBienes = new ArrayList<>();
        botonSave = Boolean.TRUE;
    }

    public void onCellEdit(CellEditEvent event) {
        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();
        int nn = event.getRowIndex();
//        int diff = listDetalle.get(nn).getCantMas() - listDetalle.get(nn).getCantidadExistente();
//        listDetalle.get(nn).setCantMen(diff);
        JsfUtil.addSuccessMessage("Información", "Valor Modificado");
    }

    public void saveConstatacion() {
        if (constatacionBienes.getObservacion() == null) {
            JsfUtil.addErrorMessage("ERROR", "Ingrese una Observación de la Constatación Fisica");
            return;
        }
        if (listDetalleConstatacionBienes.isEmpty()) {
            JsfUtil.addErrorMessage("ERROR", "Registre Bienes para constatar");
            return;
        }
        if (constatacionBienes.getEstadoConstatacion().getCodigo().equals("NEW-CF-BIEN")) {
            constatacionBienes.setEstadoConstatacion(catalogoItemService.getEstadoConstatacion("EMI-CF-BIEN", "estados_constatacion_fisica_bienes"));
            constatacionGlobal = constatacionBienesService.create(constatacionBienes);
            if (!listDetalleConstatacionBienes.isEmpty()) {
                for (BienConstatacionFisicaDetalle constFisicaDetalleB : listDetalleConstatacionBienes) {
                    detalleConstatacionBienes = constFisicaDetalleB;
                    detalleConstatacionBienes.setConstatacionFisica(constatacionGlobal);
                    constatacionDetalleBienesService.create(constFisicaDetalleB);
                    detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
                }
            }
            JsfUtil.addInformationMessage("Guardado", "Constatación Emitida Correctamente");
        } else if (constatacionBienes.getEstadoConstatacion().getCodigo().equals("EMI-CF-BIEN")) {
            for (BienConstatacionFisicaDetalle constFisicaDetalleB : listDetalleConstatacionBienes) {
                if (constFisicaDetalleB.getBienesItem().getEstadoBienConst() == null) {
                    JsfUtil.addErrorMessage("ERROR", "Por favor Ingrese estado del Bien");
                    return;
                }
                if (constFisicaDetalleB.getBienesItem().getObservAdicional() == null) {
                    JsfUtil.addErrorMessage("ERROR", "Por favor Ingrese una Observación del Bien");
                    return;
                }
            }
            constatacionBienes.setEstadoConstatacion(catalogoItemService.getEstadoConstatacion("REG-CF-BIEN", "estados_constatacion_fisica_bienes"));
            constatacionBienesService.edit(constatacionBienes);
            constatacionGlobal = Utils.clone(constatacionBienes);
            if (!listDetalleConstatacionBienes.isEmpty()) {
                for (BienConstatacionFisicaDetalle constFisicaDetalleB : listDetalleConstatacionBienes) {
                    detalleConstatacionBienes = constFisicaDetalleB;
                    detalleConstatacionBienes.setEstadoBien(detalleConstatacionBienes.getBienesItem().getEstadoBienConst());
                    detalleConstatacionBienes.setObservacion(detalleConstatacionBienes.getBienesItem().getObservAdicional());
                    constatacionDetalleBienesService.edit(detalleConstatacionBienes);
                    bienesItem = detalleConstatacionBienes.getBienesItem();
                    bienesItem.setEstadoBien(detalleConstatacionBienes.getEstadoBien());
                    bienesItemService.edit(bienesItem);
                    detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
                    bienesItem = new BienesItem();
                }
            }
            JsfUtil.addInformationMessage("Guardado", "Constatación Registrada Correctamente");
        }
        imprimirBol = Boolean.FALSE;
        PrimeFaces.current().executeScript("PF('dlgConstatacion').hide()");
    }

    public void eliminar(BienConstatacionFisica constatac) {
        constatacionBienes = constatac;
        if (constatacionBienes.getEstadoConstatacion().getCodigo().equals("EMI-CF-BIEN")) {
            constatacionBienes.setEstado(Boolean.FALSE);
            constatacionBienesService.edit(constatacionBienes);
            JsfUtil.addSuccessMessage("Info", "Constatación eliminada correctamente");
            PrimeFaces.current().ajax().update("frmMain");
        } else if (constatacionBienes.getEstadoConstatacion().getCodigo().equals("REG-CF-BIEN")) {
            JsfUtil.addErrorMessage("Error", "La Constatación está Registrada, no puede ser eliminada");
        }
    }

    public void cancelar() {
        constatacionBienes = new BienConstatacionFisica();
        detalleConstatacionBienes = new BienConstatacionFisicaDetalle();
        constatacionGlobal = new BienConstatacionFisica();
        listActivoFijoServidor = new ArrayList<>();
        listDetalleConstatacionBienes = new ArrayList<>();
        cargoSelectData = new ThServidorCargo();
        servidor = new Servidor();
        PrimeFaces.current().executeScript("PF('dlgConstatacion').hide()");
    }

    public void imprimirConstatacion() {
        if (constatacionGlobal.getCustodioBoolean()) {
            servletSess.addParametro("id_cf", constatacionGlobal.getId());
            servletSess.setNombreReporte("bienesConstatacionFisicaFuncionario");
            servletSess.setNombreSubCarpeta("reportesBienes");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } else {
            servletSess.addParametro("id_cf", constatacionGlobal.getId());
            servletSess.setNombreReporte("bienesConstatacionFisicaUnidad");
            servletSess.setNombreSubCarpeta("reportesBienes");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    public void reImprimirConstatacion(BienConstatacionFisica constat) {
        if (constat == null) {
            constat = constatacionGlobal;
        }
        if (constat.getCustodioBoolean() == null) {
            JsfUtil.addErrorMessage("Advertencia", "Debe seleccionar si la constatación es por Funcionario o por Unidad Administrativa.");
            return;
        }
        Servidor custodio;
        Servidor serv = clienteService.getUsuarioServidor(clienteService.getrolsUser(RolUsuario.guardaAlmacen)).getFuncionario();
        if (constat.getCustodioBoolean()) {
            custodio = constat.getCustodio();
            servletSess.setNombreReporte("bienesConstatacionFisicaFuncionario");
        } else {
            custodio = servidorService.findServidorByUnidad(constat.getUnidad());
            servletSess.setNombreReporte("bienesConstatacionFisicaUnidad");
        }
        ThServidorCargo cargoServ = thServidorCargoService.findByThServidor(serv);
        servletSess.addParametro("id_cf", constat.getId());
        servletSess.addParametro("nombre_guardalmacen", serv == null ? "" : serv.getPersona().getNombreCompleto());
        servletSess.addParametro("cedula_guardalmacen", serv == null ? "" : serv.getPersona().getIdentificacion());
        servletSess.addParametro("cargo_guardalmacen", cargoServ == null ? "" : cargoServ.getIdCargo().getNombreCargo());
        if (custodio != null) {
            ThServidorCargo cargoCustodio = thServidorCargoService.findByThServidor(custodio);
            servletSess.addParametro("nombre_custodio", custodio.getPersona().getNombreCompleltoSql());
            servletSess.addParametro("cedula_custodio", custodio.getPersona().getIdentificacion());
            servletSess.addParametro("cargo_custodio", cargoCustodio == null ? "" : cargoCustodio.getIdCargo().getNombreCargo());
        }
        servletSess.setNombreSubCarpeta("reportesBienes");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }
//<editor-fold defaultstate="collapsed" desc="Getters and Setters">

    public BienConstatacionFisica getConstatacionBienes() {
        return constatacionBienes;
    }

    public void setConstatacionBienes(BienConstatacionFisica constatacionBienes) {
        this.constatacionBienes = constatacionBienes;
    }

    public BienConstatacionFisicaDetalle getDetalleConstatacionBienes() {
        return detalleConstatacionBienes;
    }

    public void setDetalleConstatacionBienes(BienConstatacionFisicaDetalle detalleConstatacionBienes) {
        this.detalleConstatacionBienes = detalleConstatacionBienes;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public BienesItem getBienesItem() {
        return bienesItem;
    }

    public void setBienesItem(BienesItem bienesItem) {
        this.bienesItem = bienesItem;
    }

    public LazyModel<BienConstatacionFisica> getLazyConstatacionFisicaBienes() {
        return lazyConstatacionFisicaBienes;
    }

    public void setLazyConstatacionFisicaBienes(LazyModel<BienConstatacionFisica> lazyConstatacionFisicaBienes) {
        this.lazyConstatacionFisicaBienes = lazyConstatacionFisicaBienes;
    }

    public List<BienesItem> getListBienesItem() {
        return listBienesItem;
    }

    public void setListBienesItem(List<BienesItem> listBienesItem) {
        this.listBienesItem = listBienesItem;
    }

    public List<UnidadAdministrativa> getListUnidad() {
        return listUnidad;
    }

    public void setListUnidad(List<UnidadAdministrativa> listUnidad) {
        this.listUnidad = listUnidad;
    }

    public List<BienConstatacionFisicaDetalle> getListDetalleConstatacionBienes() {
        return listDetalleConstatacionBienes;
    }

    public void setListDetalleConstatacionBienes(List<BienConstatacionFisicaDetalle> listDetalleConstatacionBienes) {
        this.listDetalleConstatacionBienes = listDetalleConstatacionBienes;
    }

    public List<ActivoFijoServidor> getListActivoFijoServidor() {
        return listActivoFijoServidor;
    }

    public void setListActivoFijoServidor(List<ActivoFijoServidor> listActivoFijoServidor) {
        this.listActivoFijoServidor = listActivoFijoServidor;
    }

    public List<BienConstatacionFisica> getListConstatacionFisicaBienes() {
        return listConstatacionFisicaBienes;
    }

    public void setListConstatacionFisicaBienes(List<BienConstatacionFisica> listConstatacionFisicaBienes) {
        this.listConstatacionFisicaBienes = listConstatacionFisicaBienes;
    }

    public List<CatalogoItem> getListEstadoBien() {
        return listEstadoBien;
    }

    public void setListEstadoBien(List<CatalogoItem> listEstadoBien) {
        this.listEstadoBien = listEstadoBien;
    }

    public List<CatalogoItem> getListEstadosConstatac() {
        return listEstadosConstatac;
    }

    public void setListEstadosConstatac(List<CatalogoItem> listEstadosConstatac) {
        this.listEstadosConstatac = listEstadosConstatac;
    }

    public Boolean getBandera() {
        return bandera;
    }

    public void setBandera(Boolean bandera) {
        this.bandera = bandera;
    }

    public Boolean getDireccionBol() {
        return direccionBol;
    }

    public void setDireccionBol(Boolean direccionBol) {
        this.direccionBol = direccionBol;
    }

    public Boolean getDepartamentoBol() {
        return departamentoBol;
    }

    public void setDepartamentoBol(Boolean departamentoBol) {
        this.departamentoBol = departamentoBol;
    }

    public Boolean getUnidadBol() {
        return unidadBol;
    }

    public void setUnidadBol(Boolean unidadBol) {
        this.unidadBol = unidadBol;
    }

    public Boolean getBotonSave() {
        return botonSave;
    }

    public void setBotonSave(Boolean botonSave) {
        this.botonSave = botonSave;
    }

    public Boolean getBotonGenerar() {
        return botonGenerar;
    }

    public void setBotonGenerar(Boolean botonGenerar) {
        this.botonGenerar = botonGenerar;
    }

    public Boolean getImprimirBol() {
        return imprimirBol;
    }

    public void setImprimirBol(Boolean imprimirBol) {
        this.imprimirBol = imprimirBol;
    }

    public Boolean getDisEdit() {
        return disEdit;
    }

    public void setDisEdit(Boolean disEdit) {
        this.disEdit = disEdit;
    }

    public Boolean getEstadoObservacion() {
        return estadoObservacion;
    }

    public void setEstadoObservacion(Boolean estadoObservacion) {
        this.estadoObservacion = estadoObservacion;
    }

    public Boolean getEditEstadoObservacion() {
        return editEstadoObservacion;
    }

    public void setEditEstadoObservacion(Boolean editEstadoObservacion) {
        this.editEstadoObservacion = editEstadoObservacion;
    }

    public BienConstatacionFisica getConstatacionGlobal() {
        return constatacionGlobal;
    }

    public void setConstatacionGlobal(BienConstatacionFisica constatacionGlobal) {
        this.constatacionGlobal = constatacionGlobal;
    }

    public ThServidorCargo getCargoSelectData() {
        return cargoSelectData;
    }

    public void setCargoSelectData(ThServidorCargo cargoSelectData) {
        this.cargoSelectData = cargoSelectData;
    }
//</editor-fold>

}
