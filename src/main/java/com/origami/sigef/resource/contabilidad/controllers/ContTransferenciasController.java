/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.resource.contabilidad.controllers;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Banco;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.contabilidad.entities.ContBeneficiarioComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContComprobantePago;
import com.origami.sigef.resource.contabilidad.entities.ContCuentaEntidad;
import com.origami.sigef.resource.contabilidad.entities.ContTransferencias;
import com.origami.sigef.resource.contabilidad.entities.ContTransferenciasDetalle;
import com.origami.sigef.resource.contabilidad.interfaces.ContRegistroTransferencia;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author Criss Intriago
 */
@Named(value = "contTransferenciasView")
@ViewScoped
public class ContTransferenciasController implements Serializable {

    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ContRegistroTransferencia contRegistroTransferencia;
    @Inject
    private ClienteService clienteService;
    @Inject
    UserSession userSession;

    private ContTransferencias contTransferencias;
    private OpcionBusqueda opcionBusqueda;
    private CatalogoItem conceptoSeleccionado;
    private ContCuentaEntidad contCuentaEntidad;
    private ContTransferenciasDetalle contTransferenciaDetalle;

    private List<ContTransferenciasDetalle> contTransferenciasDetalleList;
    private List<Short> listaPeriodo;
    private List<CatalogoItem> conceptoTransferencia;
    private List<Servidor> servidoresList;
    private List<Long> idComprobantePagoList;
    private List<UploadedFile> files;

    private Long idComprobante;

    private LazyModel<ContComprobantePago> contComprobantePagoLazy;
    private LazyModel<ContTransferencias> contTransferenciasLazy;
    private LazyModel<Banco> bancoLazy;

    private Boolean camposCorresponsal, tipoResponsable, view;

    private Date fechaAcreditacion;

    @PostConstruct
    public void init() {
        limpiar();
        this.camposCorresponsal = Boolean.TRUE;
        this.view = Boolean.TRUE;
        this.tipoResponsable = Boolean.FALSE;
        listaPeriodo = catalogoItemService.getPeriodo();
        conceptoTransferencia = catalogoItemService.findByCatalogo("concepto_transferencia");
        contTransferenciaDetalle = new ContTransferenciasDetalle();
        //bancoLazy
        this.bancoLazy = new LazyModel<>(Banco.class);
        this.bancoLazy.getSorteds().put("nombreBanco", "ASC");
        this.bancoLazy.getFilterss().put("estado", true);
        loadLazyModel();
        loadDataRedirect();
    }

    public void loadDataRedirect() {
        if (userSession.getIdTransferencia() != null) {
            contTransferencias = contRegistroTransferencia.findById(userSession.getIdTransferencia());
            contTransferenciasDetalleList = contRegistroTransferencia.findByIdTransferencia(contTransferencias);
            view = Utils.clone(userSession.getViewTransferencia());
            userSession.setIdTransferencia(null);
            userSession.setViewTransferencia(Boolean.FALSE);
        }
    }

    public void loadLazyModel() {
        if (opcionBusqueda.getAnio() != null) {
            contTransferenciasLazy = new LazyModel<>(ContTransferencias.class);
            contTransferenciasLazy.getSorteds().put("numReferencia", "ASC");
            contTransferenciasLazy.getFilterss().put("periodo", opcionBusqueda.getAnio());
        } else {
            contTransferenciasLazy = null;
        }
    }

    public void limpiar() {
        idComprobante = null;
        contCuentaEntidad = null;
        opcionBusqueda = new OpcionBusqueda();
        contTransferencias = new ContTransferencias();
        contTransferencias.setPeriodo(opcionBusqueda.getAnio());
        contTransferencias.setMaximaAutoridad(contRegistroTransferencia.getCargoServidor(clienteService.getResponsableTransferencia(RolUsuario.maximaAutoridad)));
        contTransferencias.setResponsableTesoreria(contRegistroTransferencia.getCargoServidor(clienteService.getResponsableTransferencia(RolUsuario.tesorero)));
        contTransferenciasDetalleList = new ArrayList<>();
        idComprobantePagoList = new ArrayList<>();
    }

    public void buscarComprobantePago() {
        if (contTransferencias.getPeriodo() == null) {
            JsfUtil.addWarningMessage("AVISO!!", "Se debe seleccionar primero el periodo");
            return;
        }
        if (idComprobante != null) {
            ContComprobantePago comprobante = contRegistroTransferencia.findNumComprobante(idComprobante, contTransferencias.getPeriodo());
            if (comprobante != null) {
                comprobanteSeleccionado(comprobante);
            } else {
                openDlgComprobante();
            }
        } else {
            openDlgComprobante();
        }

    }

    public void openDlgComprobante() {
        if (contTransferencias.getPeriodo() != null) {
            this.contComprobantePagoLazy = new LazyModel<>(ContComprobantePago.class);
            this.contComprobantePagoLazy.getSorteds().put("id", "ASC");
            this.contComprobantePagoLazy.getFilterss().put("periodo", contTransferencias.getPeriodo());
            this.contComprobantePagoLazy.getFilterss().put("estado", true);
            this.contComprobantePagoLazy.getFilterss().put("transferencia", false);
            if (contCuentaEntidad != null) {
                if (contCuentaEntidad.getId() != null) {
                    this.contComprobantePagoLazy.getFilterss().put("transferencia", contCuentaEntidad);
                }
            }
            PrimeFaces.current().executeScript("PF('contComprobantePagoDlg').show()");
            PrimeFaces.current().ajax().update("contComprobantePagoForm");
        } else {
            this.contComprobantePagoLazy = null;
            JsfUtil.addWarningMessage("AVISO!!", "Se debe seleccionar primero el periodo");
        }
    }

    public void comprobanteSeleccionado(ContComprobantePago comprobante) {
        if (idComprobantePagoList.contains(comprobante.getId())) {
            JsfUtil.addWarningMessage("AVISO!!", "El comprobante de pago ya esta cargado");
            return;
        } else {
            idComprobantePagoList.add(comprobante.getId());
        }
        if (comprobante.getCuentaBancaria() != null) {
            contCuentaEntidad = comprobante.getCuentaBancaria();
        }
        contTransferencias = contRegistroTransferencia.initObject(comprobante, contTransferencias);
        contTransferenciasDetalleList = contRegistroTransferencia.detalleTransferencia(comprobante, contTransferenciasDetalleList);
        contTransferencias.setValor(BigDecimal.ZERO);
        double valor = 0;
        if (!contTransferenciasDetalleList.isEmpty()) {
            for (ContTransferenciasDetalle detalle : contTransferenciasDetalleList) {
                valor = valor + detalle.getValor().doubleValue();
            }
        }
        contTransferencias.setValor(new BigDecimal(valor));
        PrimeFaces.current().executeScript("PF('contComprobantePagoDlg').hide()");
        PrimeFaces.current().ajax().update("formMain");
    }

    public List<ContBeneficiarioComprobantePago> viewDetalleBeneficiarios(ContComprobantePago comprobante) {
        return contRegistroTransferencia.findByBeneficiario(comprobante);
    }

    public void actualizarTabla() {
        if (contTransferenciasDetalleList != null) {
            if (!contTransferenciasDetalleList.isEmpty()) {
                for (ContTransferenciasDetalle detalle : contTransferenciasDetalleList) {
                    if (conceptoSeleccionado != null) {
                        detalle.setConcepto(conceptoSeleccionado.getCodigo());
                    } else {
                        detalle.setConcepto(null);
                    }
                }
            }
        }
    }

    public void activarCorresponsal() {
        if (contTransferencias.getCorresponsal()) {
            this.camposCorresponsal = Boolean.FALSE;
            contTransferencias.setCtaCteBceCorresponsal("01820030");
            contTransferencias.setNombreCorresponsal("SPI-BANCO CENTRAL");
        } else {
            this.camposCorresponsal = Boolean.TRUE;
            contTransferencias.setCtaRotativa("");
            contTransferencias.setCtaCteBceCorresponsal("");
            contTransferencias.setNombreCorresponsal("");
        }
        PrimeFaces.current().ajax().update("corresponsalGrid");
    }

    public void buscarResponsables(Boolean tipoResponsable) {
        this.tipoResponsable = tipoResponsable;
        if (tipoResponsable) {
            servidoresList = clienteService.getListServidoresRol(RolUsuario.maximaAutoridad);
        } else {
            servidoresList = clienteService.getListServidoresRol(RolUsuario.tesorero);
        }
        PrimeFaces.current().executeScript("PF('servidoresDlg').show()");
    }

    public void servidorSeleccionado(Servidor servidor) {
        if (tipoResponsable) {
            contTransferencias.setMaximaAutoridad(contRegistroTransferencia.getCargoServidor(servidor));
        } else {
            contTransferencias.setResponsableTesoreria(contRegistroTransferencia.getCargoServidor(servidor));
        }
        PrimeFaces.current().executeScript("PF('servidoresDlg').hide()");
        PrimeFaces.current().ajax().update("responsablesGrid");
        PrimeFaces.current().ajax().update("responsablesGrid2");
    }

    public void save() {
        String mensaje = contRegistroTransferencia.validaciones(contTransferencias, contTransferenciasDetalleList);
        if (mensaje.equals("")) {
            PrimeFaces.current().executeScript("PF('menuArchivosDlg').show()");
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", mensaje);
        }
    }

    public void generarArchivosDescargas(int accion) throws FileNotFoundException {
        contRegistroTransferencia.generarArchivosDescargas(accion, contTransferencias);
        PrimeFaces.current().ajax().update("menuArchivosForm");
    }

    public void form(ContTransferencias contTransferencias, Boolean accion) {
        userSession.setIdTransferencia(contTransferencias.getId());
        userSession.setViewTransferencia(view);
        JsfUtil.redirect(CONFIG.URL_APP + "tesoreria/transferencias");
    }

    public void reporte(ContTransferencias contTransferencias, String tipoArchivo) {
        contRegistroTransferencia.generarReporte(contTransferencias, tipoArchivo);
    }

    public void anularGeneral(ContTransferencias contTransferencias) {
        contRegistroTransferencia.anularGeneral(contTransferencias);
        JsfUtil.addSuccessMessage("INFO", "Se anulo correctamente");
    }

    public void uploadDoc(ContTransferencias contTransferencias) {
        this.contTransferencias = contTransferencias;
        files = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('uploadDocDlg').show()");
        PrimeFaces.current().ajax().update("uploadDocForm");
    }

    public void subirDocGeneral(FileUploadEvent event) {
        files.add(event.getFile());
        contRegistroTransferencia.upload(contTransferencias, files, fechaAcreditacion);
        PrimeFaces.current().executeScript("PF('uploadDocDlg').hide()");
        PrimeFaces.current().ajax().update("transferenciaTable");
        JsfUtil.addSuccessMessage("INFO!!", "Documento agregado correctamente");
    }

    public void cambiarBanco(ContTransferenciasDetalle contTransferenciasDetalle) {
        this.contTransferenciaDetalle = contTransferenciasDetalle;
        PrimeFaces.current().executeScript("PF('bancoDlg').show()");
        PrimeFaces.current().ajax().update("bancoForm");
    }

    public void closeDlgBanco(Banco banco) {
        contTransferenciaDetalle.setInstitucionFinanciera(banco);
        PrimeFaces.current().executeScript("PF('bancoDlg').hide()");
        PrimeFaces.current().ajax().update("detalleTransferenciaTable");
        contTransferenciaDetalle = new ContTransferenciasDetalle();
        JsfUtil.addSuccessMessage("INFO!!", "Se ha modificado correctamente el detalle");
    }

    public void verDocumentos(ContTransferencias contTransferencias) {
        this.contTransferencias = contTransferencias;
        PrimeFaces.current().executeScript("PF('viewDocumentoDlg').show()");
        PrimeFaces.current().ajax().update("viewDocumentoForm");
    }

    public ContTransferencias getContTransferencias() {
        return contTransferencias;
    }

    public void setContTransferencias(ContTransferencias contTransferencias) {
        this.contTransferencias = contTransferencias;
    }

    public CatalogoItem getConceptoSeleccionado() {
        return conceptoSeleccionado;
    }

    public void setConceptoSeleccionado(CatalogoItem conceptoSeleccionado) {
        this.conceptoSeleccionado = conceptoSeleccionado;
    }

    public List<ContTransferenciasDetalle> getContTransferenciasDetalleList() {
        return contTransferenciasDetalleList;
    }

    public void setContTransferenciasDetalleList(List<ContTransferenciasDetalle> contTransferenciasDetalleList) {
        this.contTransferenciasDetalleList = contTransferenciasDetalleList;
    }

    public List<Short> getListaPeriodo() {
        return listaPeriodo;
    }

    public void setListaPeriodo(List<Short> listaPeriodo) {
        this.listaPeriodo = listaPeriodo;
    }

    public List<CatalogoItem> getConceptoTransferencia() {
        return conceptoTransferencia;
    }

    public void setConceptoTransferencia(List<CatalogoItem> conceptoTransferencia) {
        this.conceptoTransferencia = conceptoTransferencia;
    }

    public Long getIdComprobante() {
        return idComprobante;
    }

    public void setIdComprobante(Long idComprobante) {
        this.idComprobante = idComprobante;
    }

    public LazyModel<ContComprobantePago> getContComprobantePagoLazy() {
        return contComprobantePagoLazy;
    }

    public void setContComprobantePagoLazy(LazyModel<ContComprobantePago> contComprobantePagoLazy) {
        this.contComprobantePagoLazy = contComprobantePagoLazy;
    }

    public Boolean getCamposCorresponsal() {
        return camposCorresponsal;
    }

    public void setCamposCorresponsal(Boolean camposCorresponsal) {
        this.camposCorresponsal = camposCorresponsal;
    }

    public List<Servidor> getServidoresList() {
        return servidoresList;
    }

    public void setServidoresList(List<Servidor> servidoresList) {
        this.servidoresList = servidoresList;
    }

    public Boolean getTipoResponsable() {
        return tipoResponsable;
    }

    public void setTipoResponsable(Boolean tipoResponsable) {
        this.tipoResponsable = tipoResponsable;
    }

    public Boolean getView() {
        return view;
    }

    public void setView(Boolean view) {
        this.view = view;
    }

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public LazyModel<ContTransferencias> getContTransferenciasLazy() {
        return contTransferenciasLazy;
    }

    public void setContTransferenciasLazy(LazyModel<ContTransferencias> contTransferenciasLazy) {
        this.contTransferenciasLazy = contTransferenciasLazy;
    }

    public Date getFechaAcreditacion() {
        return fechaAcreditacion;
    }

    public void setFechaAcreditacion(Date fechaAcreditacion) {
        this.fechaAcreditacion = fechaAcreditacion;
    }

    public ContTransferenciasDetalle getContTransferenciaDetalle() {
        return contTransferenciaDetalle;
    }

    public void setContTransferenciaDetalle(ContTransferenciasDetalle contTransferenciaDetalle) {
        this.contTransferenciaDetalle = contTransferenciaDetalle;
    }

    public LazyModel<Banco> getBancoLazy() {
        return bancoLazy;
    }

    public void setBancoLazy(LazyModel<Banco> bancoLazy) {
        this.bancoLazy = bancoLazy;
    }

}
