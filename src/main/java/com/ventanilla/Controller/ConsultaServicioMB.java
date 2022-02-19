/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ventanilla.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.ventanilla.Entity.RegistroSolicitudRequisitos;
import com.ventanilla.Entity.SolicitudDocumento;
import com.ventanilla.Entity.SolicitudServicios;
import com.ventanilla.Services.VentanillaService;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ricardo
 */
@Named
@ViewScoped
public class ConsultaServicioMB implements Serializable {

    @Inject
    private VentanillaService ventanillaService;
    @Inject
    protected UserSession session;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ServletSession servletSession;
    private LazyModel<SolicitudServicios> lazySolicitud;
    private SolicitudServicios solicitudServicios;
    private List<UnidadAdministrativa> departamentos;
    private Boolean verDetalle;
    private List<RegistroSolicitudRequisitos> registroRequisitos;
    private List<RegistroSolicitudRequisitos> requisitosErrores;
    private RegistroSolicitudRequisitos registroSolicitudRequisito;
    private Observaciones ultimaObservacion;
    //Para la subida de archivos
    private UploadedFile file;
    private File FILE;

    @PostConstruct
    public void init() {
        requisitosErrores = new ArrayList<>();
        lazySolicitud = new LazyModel<>(SolicitudServicios.class);
        lazySolicitud.addSorted("fechaCreacion", "DESC");
        departamentos = ventanillaService.findAllQuery("SELECT DISTINCT ua FROM Servicio s JOIN s.departamento ua WHERE ua.estado = true AND s.activo = true ORDER BY ua.nombre", null);
        verDetalle = Boolean.FALSE;
    }

    public void visualizarDetalle(Boolean view, SolicitudServicios s) {
        verDetalle = view;
        solicitudServicios = s;
        registroRequisitos = new ArrayList<>();
        requisitosErrores = new ArrayList<>();
        if (solicitudServicios != null) {
            if (!Utils.isEmpty(solicitudServicios.getTramite().getObservaciones())) {
                ultimaObservacion = solicitudServicios.getTramite().getObservaciones().get(solicitudServicios.getTramite().getObservaciones().size() - 1);
            }
            registroRequisitos = ventanillaService.findAllRegistroRequisitosBySolicitud(solicitudServicios);
            if (!Utils.isEmpty(registroRequisitos)) {
                registroRequisitos.stream().filter(rs -> (rs.getCorreccion())).forEachOrdered(rs -> {
                    requisitosErrores.add(rs);
                });
            }
        }
    }

    public void abrirDialogDocumento(RegistroSolicitudRequisitos rs) {
        registroSolicitudRequisito = rs;
        JsfUtil.update("formDocumento");
        JsfUtil.executeJS("PF('DlgoDocumento').show()");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            file = event.getFile();
            FILE = Utils.copyFileServer(file, SisVars.rutaRepositorioArchivo);
//            uploadDoc.upload(solicitudServicios.getTramite(), file);
            crearSolicitudDocumento();
            JsfUtil.executeJS("PF('DlgoDocumento').hide()");
            JsfUtil.update("panelDetalle");
            JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
        } catch (IOException e) {
            JsfUtil.addErrorMessage(null, "Ocurrió un error al subir el archivo");
        }
    }

    private void crearSolicitudDocumento() {
        SolicitudDocumento sd = new SolicitudDocumento();
        sd.setEstado("A");
        sd.setUsuario(session.getNameUser());
        sd.setFechaCreacion(new Date());
        sd.setSolicitudServicio(solicitudServicios);
        if (file == null && file == null) {
            JsfUtil.addErrorMessage("", "Error al crear el archivo intente nuevamente");
            return;
        }
        sd.setNombreArchivo(FILE.getName());
        sd.setTipoArchivo(file.getContentType());
        sd.setRutaArchivo(FILE.getAbsolutePath());
        sd = (SolicitudDocumento) ventanillaService.save(sd);
        registroSolicitudRequisito.setSolicitudDocumento(sd);
        registroSolicitudRequisito.setCorregido(Boolean.TRUE);
        ventanillaService.save(registroSolicitudRequisito);
    }

    public void borrarDocumento(RegistroSolicitudRequisitos rs) {
        rs.getSolicitudDocumento().setEstado("I");
        ventanillaService.save(rs.getSolicitudDocumento());
        rs.setSolicitudDocumento(null);
        if (rs.getCorreccion()) {
            rs.setCorregido(Boolean.FALSE);
        }
        ventanillaService.save(rs);
        JsfUtil.addSuccessMessage("", "Documento eliminado con éxito");
        JsfUtil.update("panelDetalle");
    }

    public void imprimirTicket(SolicitudServicios s) {
        servletSession.borrarDatos();
        servletSession.borrarParametros();
        List<SolicitudServicios> ss = new ArrayList<>();
        ss.add(s);
        servletSession.setDataSource(ss);
        servletSession.setNombreSubCarpeta("ventanilla");
        servletSession.setNombreReporte("ticketTramite");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void notificarCorreccion() {
        if (!Utils.isEmpty(requisitosErrores)) {
            Boolean notificar = Boolean.TRUE;
            for (RegistroSolicitudRequisitos rs : requisitosErrores) {
                if (rs.getCorreccion() && !rs.getCorregido()) {
                    notificar = Boolean.FALSE;
                }
            }
            if (!notificar) {
                JsfUtil.addErrorMessage("", "¡Debe corregir todos los requisitos con errores!");
                return;
            }
            solicitudServicios.setEnObservacion(Boolean.FALSE);
            ventanillaService.save(solicitudServicios);
            JsfUtil.addSuccessMessage("", "Solicitud notificada");
            JsfUtil.update("formMain");
        }
    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public RegistroSolicitudRequisitos getRegistroSolicitudRequisito() {
        return registroSolicitudRequisito;
    }

    public void setRegistroSolicitudRequisito(RegistroSolicitudRequisitos registroSolicitudRequisito) {
        this.registroSolicitudRequisito = registroSolicitudRequisito;
    }

    public List<RegistroSolicitudRequisitos> getRequisitosErrores() {
        return requisitosErrores;
    }

    public void setRequisitosErrores(List<RegistroSolicitudRequisitos> requisitosErrores) {
        this.requisitosErrores = requisitosErrores;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public File getFILE() {
        return FILE;
    }

    public void setFILE(File FILE) {
        this.FILE = FILE;
    }

    public List<RegistroSolicitudRequisitos> getRegistroRequisitos() {
        return registroRequisitos;
    }

    public void setRegistroRequisitos(List<RegistroSolicitudRequisitos> registroRequisitos) {
        this.registroRequisitos = registroRequisitos;
    }

    public Observaciones getUltimaObservacion() {
        return ultimaObservacion;
    }

    public void setUltimaObservacion(Observaciones ultimaObservacion) {
        this.ultimaObservacion = ultimaObservacion;
    }

    public SolicitudServicios getSolicitudServicios() {
        return solicitudServicios;
    }

    public void setSolicitudServicios(SolicitudServicios solicitudServicios) {
        this.solicitudServicios = solicitudServicios;
    }

    public Boolean getVerDetalle() {
        return verDetalle;
    }

    public void setVerDetalle(Boolean verDetalle) {
        this.verDetalle = verDetalle;
    }

    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public LazyModel<SolicitudServicios> getLazySolicitud() {
        return lazySolicitud;
    }

    public void setLazySolicitud(LazyModel<SolicitudServicios> lazySolicitud) {
        this.lazySolicitud = lazySolicitud;
    }
//</editor-fold>
}
