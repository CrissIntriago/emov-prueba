/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Teletrbajo.Controller;

import com.origami.sigef.Teletrbajo.Entity.HerramientasUtilizadas;
import com.origami.sigef.Teletrbajo.Models.ActividadesDTO;
import com.origami.sigef.Teletrbajo.Service.HerramientasUtilizadasService;
import com.origami.sigef.Teletrbajo.Service.TeletrabajoService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.math.BigInteger;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import com.origami.sigef.Teletrbajo.Entity.Teletrabajo;
import com.origami.sigef.Teletrbajo.Models.ImprimirDTO;
import com.origami.sigef.Teletrbajo.Models.ImprimirIndividualDTO;
import com.origami.sigef.Teletrbajo.Models.TeletrabajoDTO;
import com.origami.sigef.certificacion_presupuesto_anual.model.DocumentosModel;
import com.origami.sigef.certificacion_presupuesto_anual.service.ProcedimientoRequisitoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.ProcedimientoRequisito;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.procesos.Model.ListarequisitosModel;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.apache.commons.io.IOUtils;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class ActividadesTeletrabajoMB implements Serializable {

    @Inject
    private TeletrabajoService service;
    @Inject
    private CatalogoService serviceCatalogo;
    @Inject
    private HerramientasUtilizadasService serviceHerramientas;
    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession user;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ProcedimientoRequisitoService requisitoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    private List<UnidadAdministrativa> unidades;

    private List<UploadedFile> files;
    private Teletrabajo teletrabajo;
    private List<Cliente> listaRequirientes;
    private List<CatalogoItem> listHerramientas;
    private LazyModel<Teletrabajo> lazy;
    private LazyModel<Teletrabajo> lazyControl;
    private List<HerramientasUtilizadas> listaHerramientasUtilizadas;
    private List<CatalogoItem> listaHerramientasSeleccionadas;
    private List<String> porcentajeAvance;
    private String porcentajeValor;
    private Boolean generarReprote, ver;
    private Date inicio, fin;
    private int mes;
    private Cliente clienteFiltrados;
    private List<DocumentosModel> documentosListas;
    private DocumentosModel archivo;
    private StreamedContent streamedContent;
    private UploadedFile file;
    private boolean contentAvailable = false;
    private List<ListarequisitosModel> listaRequisitosGlobal;
    private ListarequisitosModel requisitosObjeto;
    private ProcedimientoRequisito p;
    private Cliente controlAprobador;
    private Date fechaDesde, fechaHasta;
    private Cliente clienteFiltar;
    private List<Cliente> clientesTeletrabajo;
    private String totalesHoras;
    private int horas;
    private int minutos;

    @PostConstruct
    public void init() {
        this.teletrabajo = new Teletrabajo();
        listaRequirientes = new ArrayList<>();
        listHerramientas = new ArrayList<>();
        listaHerramientasUtilizadas = new ArrayList<>();
        listaHerramientasSeleccionadas = new ArrayList<>();
        porcentajeAvance = new ArrayList<>();
        clienteFiltrados = new Cliente();
        clienteFiltrados = clienteService.getUsuarioServidor(user.getNameUser()).getFuncionario().getPersona();
        lazy = new LazyModel<>(Teletrabajo.class);
        if (clienteFiltrados != null) {
            lazy.getFilterss().put("personaSolicitada", clienteFiltrados);
        } else {
            lazy.getFilterss().put("personaSolicitada.id", 0);
        }
        lazy.getSorteds().put("fechaTarea", "ASC");
        lazy.setDistinct(false);
        generarReprote = Boolean.FALSE;
        files = new ArrayList<>();
        documentosListas = new ArrayList<>();
        archivo = new DocumentosModel();
        ver = Boolean.TRUE;
        controlAprobador = new Cliente();
        controlAprobador = clienteService.getUsuarioServidor(user.getNameUser()).getFuncionario().getPersona();
        clienteFiltar = new Cliente();
        this.requisitosObjeto = new ListarequisitosModel();
        listaRequisitosGlobal = new ArrayList<>();
        p = new ProcedimientoRequisito();
        p = requisitoService.findAll().get(0);
        unidades = new ArrayList<>();
        clientesTeletrabajo = new ArrayList<>();
        clientesTeletrabajo = service.listaClienteTeletrabajo(Boolean.TRUE, user.getUsuario().getFuncionario().getPersona());
        fechaDesde = new Date();
        fechaHasta = new Date();
        totalesHoras = "";
        lazyControl = new LazyModel<>(Teletrabajo.class);
        lazyControl.getFilterss().put("responsable", controlAprobador);
        lazyControl.getFilterss().put("estado", true);
        lazyControl.setDistinct(false);
        lazyControl.getSorteds().put("fechaTarea", "DESC");
    }

    public void visualizar(Teletrabajo t) {
        ver = Boolean.FALSE;
        teletrabajo = new Teletrabajo();
        teletrabajo = t;
        listaHerramientasSeleccionadas = new ArrayList<>();
        for (HerramientasUtilizadas data : teletrabajo.getListaTeletrabajo()) {
            listaHerramientasSeleccionadas.add(data.getHerramientas());
        }
        PrimeFaces.current().executeScript("PF('viewDlogoActividades').show()");
        PrimeFaces.current().ajax().update("formDlogoView");
    }

    public void dlgoRegistrarEditar(Teletrabajo obj) {
        ver = Boolean.TRUE;
        this.teletrabajo = new Teletrabajo();
        this.listaRequirientes = new ArrayList<>();
        this.listHerramientas = new ArrayList<>();
        this.listaHerramientasUtilizadas = new ArrayList<>();
        this.listaHerramientasSeleccionadas = new ArrayList<>();
        this.porcentajeAvance = new ArrayList<>();
        this.porcentajeValor = "";
        this.listaRequirientes = service.listaRequirientes(2, true);
        this.listHerramientas = serviceCatalogo.MostarTodoCatalogo("herramientas_utilizadas");
        this.porcentajeAvance = serviceCatalogo.porcentajeAvances("porcentaje_anvance");
        this.requisitosObjeto = new ListarequisitosModel();
        this.listaRequisitosGlobal = new ArrayList<>();
        if (obj != null) {
            teletrabajo = new Teletrabajo();
            teletrabajo = obj;
            porcentajeValor = teletrabajo.getGradoEjecucion().toString();

            for (HerramientasUtilizadas data : teletrabajo.getListaTeletrabajo()) {
                listaHerramientasSeleccionadas.add(data.getHerramientas());
            }
        } else {
            ver = Boolean.FALSE;
            teletrabajo = new Teletrabajo();
            teletrabajo.setFechaTarea(new Date());
            teletrabajo.setFechaLimite(new Date());
            teletrabajo.setPersonaSolicitada(user.getUsuario().getFuncionario().getPersona());
            teletrabajo.setRequiriente(aprobadorcliente());
            porcentajeValor = "100";
        }
        generarReprote = Boolean.FALSE;
        PrimeFaces.current().executeScript("PF('dlogTeletrabajoActividades').show()");
        PrimeFaces.current().ajax().update("fmdlogoActividades");
    }

    public void save() {
        try {
            if (teletrabajo.getHoraInicio().equals(null) && teletrabajo.getHoraFin().equals(null)) {
                JsfUtil.addWarningMessage("", "revise los campos");
                return;
            }
            if (teletrabajo.getHoraInicio() == null && teletrabajo.getHoraFin() == null) {
                JsfUtil.addWarningMessage("", "revise los campos");
                return;
            }
            if (teletrabajo.getHoraFin().getTime() <= teletrabajo.getHoraInicio().getTime()) {
                JsfUtil.addWarningMessage("", "La hora fin no puede ser menora la hora de incio y no puedo ser igual");
                return;
            }
            if (teletrabajo.getHoraFin().compareTo(teletrabajo.getHoraInicio()) <= 0) {
                JsfUtil.addWarningMessage("", "La hora fin no puede ser menora la hora de incio y no puedo ser igual");
                return;
            }
            teletrabajo.setPersonaSolicitada(user.getUsuario().getFuncionario().getPersona());
            Long dato = Long.valueOf(porcentajeValor);
            teletrabajo.setGradoEjecucion(BigInteger.valueOf(dato));
            HerramientasUtilizadas dataTools = new HerramientasUtilizadas();
            List<HerramientasUtilizadas> toolsSave = new ArrayList<>();
            for (CatalogoItem item : listaHerramientasSeleccionadas) {
                dataTools = new HerramientasUtilizadas();
                dataTools.setTeletrabajo(teletrabajo);
                dataTools.setHerramientas(item);
                toolsSave.add(dataTools);
            }
            teletrabajo.setResponsable(teletrabajo.getRequiriente());
            teletrabajo.setTiempoEjecucion(calcularTiempEjecucion(teletrabajo.getHoraInicio(), teletrabajo.getHoraFin()));
            teletrabajo.setListaTeletrabajo(toolsSave);
            if (teletrabajo.getId() != null) {
                serviceHerramientas.eliminar(teletrabajo);
                service.edit(teletrabajo);
                JsfUtil.addInformationMessage("Exito", "su registro de edito correctamente");
            } else {
                teletrabajo.setUnidad(clienteService.unidadRol(user.getNameUser()));
                teletrabajo = service.create(teletrabajo);
                JsfUtil.addInformationMessage("Exito", "su registro de guardo correctamente");
            }
            //teletrabajo = new Teletrabajo();
            files = new ArrayList<>();
            for (ListarequisitosModel i : listaRequisitosGlobal) {
                files.add(i.getFile());
            }
            if (files.size() > 0) {
                uploadDoc.upload(teletrabajo, files);
            }
            generarReprote = Boolean.TRUE;
            teletrabajo = new Teletrabajo();
            PrimeFaces.current().executeScript("PF('dlogTeletrabajoActividades').hide()");
            PrimeFaces.current().ajax().update("fmdlogoActividades");
            PrimeFaces.current().ajax().update("messages");
            PrimeFaces.current().ajax().update("formMain");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "Ocurrió un Error en el sistema");
            e.printStackTrace();
        }
    }

    public String calcularTiempEjecucion(Date time1, Date time2) throws ParseException {
        int diferencia = (int) ((time2.getTime() - time1.getTime()) / 1000);
        int dias = 0;
        int horas = 0;
        int minutos = 0;
        if (diferencia > 86400) {
            dias = (int) Math.floor(diferencia / 86400);
            diferencia = diferencia - (dias * 86400);
        }
        if (diferencia > 3600) {
            horas = (int) Math.floor(diferencia / 3600);
            diferencia = diferencia - (horas * 3600);
        }
        if (diferencia > 60) {
            minutos = (int) Math.floor(diferencia / 60);
            diferencia = diferencia - (minutos * 60);
        }
        return horas + "h y " + minutos + "m";
    }

    public void eliminar(Teletrabajo data) {
        try {
            data.setEstado(Boolean.FALSE);
            service.edit(data);
            JsfUtil.addInformationMessage("Exito", "su registro de elimino correctamente");
            PrimeFaces.current().ajax().update("formMain");
        } catch (Exception e) {
            JsfUtil.addErrorMessage("Error", "Ocurrió un Error en el sistema");
            e.printStackTrace();
        }
    }

    public void actualizarFechaLimite() {
        teletrabajo.setFechaLimite(teletrabajo.getFechaTarea());
    }

    public void imprimir(int tipo, String tipoExcel, Teletrabajo t, int mess) throws ParseException {
        ss.borrarDatos();
        mes = mess;
        if (tipo == 1) {
            imprimirInidividual(tipoExcel, t);
        } else if (tipo == 2) {
            imprimirGrupalFechas(tipoExcel, inicio, fin);
        } else {
            inicio = verfifcarMesIncial(mes);
            fin = verfifcarMesfinal(mes);
            imprimirGrupalFechas(tipoExcel, inicio, fin);
        }
    }

    public void imprimirInidividual(String tipo, Teletrabajo t) throws ParseException {
        String herramienta = "";
        String nombre = t.getPersonaSolicitada().getNombreCompleto();
        String cargo = clienteService.getuusuarioLogeadoCargo(user.getNameUser()).getCargo().getNombreCargo();
        String requiriente = t.getRequiriente().getNombreCompleto();
        for (HerramientasUtilizadas data : t.getListaTeletrabajo()) {
            herramienta += data.getHerramientas().getTexto().toUpperCase() + "; ";
        }
        ImprimirIndividualDTO imprimir = new ImprimirIndividualDTO(t, herramienta, nombre, cargo, requiriente);
        ss.setDataSource(Arrays.asList(imprimir));
        ss.setContentType(tipo);
        if (tipo.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(true);
        }
        ss.setNombreReporte("informeIndividual");
        ss.setNombreSubCarpeta("Teletrabajo");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirGrupalFechas(String tipo, Date inicio, Date fin) throws ParseException {
        Usuarios c = clienteService.getUsuarioServidor(user.getNameUser());
        List<Teletrabajo> actividadesList = service.listaActividadeDiarias(c.getFuncionario().getPersona(), inicio, fin);
        ActividadesDTO data = new ActividadesDTO();
        TeletrabajoDTO dto = new TeletrabajoDTO();
        List<TeletrabajoDTO> datos = new ArrayList<>();
        this.horas = 0;
        this.minutos = 0;
        Cliente aprueba = new Cliente();
        for (Teletrabajo t : actividadesList) {
            dto = new TeletrabajoDTO();
            data.setNombreCompleto(t.getPersonaSolicitada().getNombreCompleto());
            data.setElaborado(user.getUsuario().getFuncionario().getPersona().getNombreCompleto());
            aprueba = new Cliente();
            aprueba = t.getResponsable();
            dto.setTeletrabajo(t);
            dto.setUnidad("");
            if (t.getGradoEjecucion().compareTo(new BigInteger("100")) == 0) {
                dto.setAvanceUno("X");
            } else if (t.getGradoEjecucion().compareTo(new BigInteger("50")) == 0) {
                dto.setAvanceDos("X");
            } else {
                dto.setAvanceTres("X");
            }
            dto.setTiempoEjecucion(t.getTiempoEjecucion());
            devolverTotalHorasMinutos(dto.getTiempoEjecucion());
            datos.add(dto);
        }
        data.setFechaDesde(inicio);
        data.setFechaHasta(fin);
        data.setCargo(clienteService.getuusuarioLogeadoCargo(user.getNameUser()).getCargo().getNombreCargo());
        SimpleDateFormat sdf = new SimpleDateFormat("mm");
        data.setTotalHoras(horas + ":" + minutos + "(Horas:minutos)");
        if (aprueba != null) {
            data.setAprobado(aprueba.getNombreCompleto());
        } else {
            data.setAprobado("");
        }
        ImprimirDTO datosFinal = new ImprimirDTO(data, datos);
        ss.setDataSource(Arrays.asList(datosFinal));
        ss.setContentType(tipo);
        if (tipo.equalsIgnoreCase("xlsx")) {
            ss.setOnePagePerSheet(true);
        }
        ss.setNombreReporte("actividadesDiarias");
        ss.setNombreSubCarpeta("Teletrabajo");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void devolverTotalHorasMinutos(String totalHoras) {
        totalHoras = totalHoras.replace(" ", "");
        totalHoras = totalHoras.replace("h", "");
        totalHoras = totalHoras.replace("m", "");
        String[] array = totalHoras.split("y");
        this.horas += Integer.parseInt(array[0]);
        this.minutos += Integer.parseInt(array[1]);
        if (minutos > 60) {
            this.horas += (minutos / 60);
            this.minutos = (minutos % 60);
        }
    }

    public int tiempEjecucion(Date time1, Date time2) throws ParseException {
        Calendar calInicio = Calendar.getInstance();
        calInicio.setTime(time1);//pasamos horaInicio a Calendar
        Calendar calSalida = Calendar.getInstance();//hora actual sistema
        calSalida.setTime(time2);
        //calculamos diferencia
        int difHoras = calSalida.get(Calendar.HOUR_OF_DAY) - calInicio.get(Calendar.HOUR_OF_DAY);
        int difMinutos = calSalida.get(Calendar.MINUTE) - calInicio.get(Calendar.MINUTE);
        return (difHoras * 60) + difMinutos;
    }

    public void dlogoInoforme() {
        inicio = new Date();
        fin = new Date();
        PrimeFaces.current().executeScript("PF('informe').show()");
        PrimeFaces.current().ajax().update("fmInforme");
    }

    public Date verfifcarMesIncial(int mes) {
        Calendar anio = Calendar.getInstance();
        anio.setTime(new Date());
        Calendar cal = Calendar.getInstance();
        cal.set(anio.get(Calendar.YEAR), mes - 1, 1);
        return cal.getTime();
    }

    public Date verfifcarMesfinal(int mes) {
        Calendar anio = Calendar.getInstance();
        anio.setTime(new Date());
        Calendar calFin = Calendar.getInstance();
        calFin.set(anio.get(Calendar.YEAR), mes - 1, 1);
        calFin.set(anio.get(Calendar.YEAR), mes - 1, calFin.getActualMaximum(Calendar.DAY_OF_MONTH));
        return calFin.getTime();
    }

    public void handleFileUpload(FileUploadEvent event) {
        file = null;
        file = event.getFile();
        requisitosObjeto = new ListarequisitosModel();
        requisitosObjeto.setFile(file);
        requisitosObjeto.setRequisitos(p);
        listaRequisitosGlobal.add(requisitosObjeto);
        PrimeFaces.current().ajax().update("messages");
        JsfUtil.addInformationMessage("Información", "El archvio se subió correctamente");
    }

    public void viewFile(ListarequisitosModel modelFile) {
        try {
            if (modelFile.getFile() != null) {
                ss.setContentType(modelFile.getFile().getContentType());
                ss.setNombreDocumento(modelFile.getFile().getFileName());
                ss.setTempData(modelFile.getFile().getInputstream());
            }
        } catch (Exception e) {
            e.printStackTrace();;
        }
    }

    public DefaultStreamedContent pdfDocumentGenerate(DocumentosModel m) throws IOException {
        try {
            byte[] document = IOUtils.toByteArray(m.getArchivo().getInputstream());
            return new DefaultStreamedContent(new ByteArrayInputStream(document), "application/pdf", "Actor_List.pdf");
        } finally {
        }
    }

    public void verDocumento(DocumentosModel m) throws IOException {
        streamedContent = pdfDocumentGenerate(m);
        PrimeFaces.current().executeScript("PF('dlogoMedia').show()");
        PrimeFaces.current().ajax().update("fmMedia");
    }

    public void eliminarDocumento(int index) {
        this.listaRequisitosGlobal.remove(index);
    }

    public String listaUnidadesPadres() {
        UnidadAdministrativa unidadCliente = clienteService.unidadRol(user.getNameUser());
        boolean bandera = false;
        if (unidadCliente != null) {
            try {
                unidades = unidadService.padreUnidades(unidadCliente);
                Rol rolFinal = new Rol();
                List<Rol> rroles = new ArrayList<>();
                if (!unidades.isEmpty()) {
                    for (UnidadAdministrativa u : unidades) {
                        rroles = new ArrayList<>();
                        rroles = unidadService.rolDetinatario(u);
                        rolFinal = new Rol();
                        for (Rol r : rroles) {
                            if (r != null) {
                                if (r.getIsDirector() != null) {
                                    if (r.getIsDirector()) {
                                        bandera = true;
                                        rolFinal = r;
                                    }
                                }
                            }
                            if (bandera) {
                                break;
                            }
                        }
                        if (bandera) {
                            break;
                        }
                    }
                }
                Cliente aprobador = new Cliente();
                if (bandera) {
                    aprobador = unidadService.getUsuarioDestino(rolFinal).getFuncionario().getPersona();
                }
                if (aprobador.getId() != null) {
                    return aprobador.getNombreCompleto();
                } else {
                    return "";
                }
            } catch (Exception e) {
                return "";
            }
        } else {
            return "";
        }
    }

    public Cliente aprobadorcliente() {
        UnidadAdministrativa unidadCliente = clienteService.unidadRol(user.getNameUser());
        boolean bandera = false;
        if (unidadCliente != null) {
            try {
                unidades = unidadService.padreUnidades(unidadCliente);
                Rol rolFinal = new Rol();
                List<Rol> rroles = new ArrayList<>();
                if (!unidades.isEmpty()) {
                    for (UnidadAdministrativa u : unidades) {
                        rroles = new ArrayList<>();
                        rroles = unidadService.rolDetinatario(u);
                        rolFinal = new Rol();
                        for (Rol r : rroles) {
                            if (r != null) {
                                if (r.getIsDirector() != null) {
                                    if (r.getIsDirector()) {
                                        bandera = true;
                                        rolFinal = r;
                                    }
                                }
                            }
                            if (bandera) {
                                break;
                            }
                        }
                        if (bandera) {
                            break;
                        }
                    }
                }
                Cliente aprobador = new Cliente();
                if (bandera) {
                    aprobador = unidadService.getUsuarioDestino(rolFinal).getFuncionario().getPersona();
                }
                if (aprobador.getId() != null) {
                    return aprobador;
                } else {
                    return new Cliente();
                }
            } catch (Exception e) {
                return new Cliente();
            }
        } else {
            return new Cliente();
        }
    }

    public String transformCustomFormatDate(Date date) {
        if (date != null) {
            DateFormat format = new SimpleDateFormat(datePattern());
            return format.format(date);
        }
        return "";
    }

    public String datePattern() {
        return "yyyy-MM-dd";
    }

    public void filtrarLazy() {
        lazyControl = new LazyModel<>(Teletrabajo.class);
        lazyControl.getFilterss().put("fechaTarea", fechaDesde);
        lazyControl.getFilterss().put("fechaLimite", fechaHasta);
        lazyControl.getFilterss().put("responsable", controlAprobador);

    }

    public void totalHoras(Teletrabajo t) {
        fechaDesde = t.getFechaTarea();
        teletrabajo = new Teletrabajo();
        teletrabajo = t;
        fechaHasta = t.getFechaLimite();
        totalesHoras = "";
        this.horas = 0;
        this.minutos = 0;
        List<Teletrabajo> result = new ArrayList<>();
        if (fechaDesde != null && fechaHasta != null && (t.getPersonaSolicitada() != null && t.getPersonaSolicitada().getId() != null)) {
            result = service.listaActividadeDiarias(t.getPersonaSolicitada(), fechaDesde, fechaHasta, t.getResponsable());
        } else if (fechaDesde != null && fechaHasta != null && (t.getPersonaSolicitada() == null || t.getPersonaSolicitada().getId() == null)) {
            result = service.listaActividadeDiariasFechas(fechaDesde, fechaHasta, t.getResponsable());
        } else if (fechaDesde == null && fechaHasta == null && (t.getPersonaSolicitada() != null && t.getPersonaSolicitada().getId() != null)) {
            result = service.clientesOnline(t.getPersonaSolicitada(), t.getResponsable());
        } else {
            result = service.listaActividadeDiariasFechas(t.getResponsable());
        }
        for (Teletrabajo item : result) {
            devolverTotalHorasMinutos(item.getTiempoEjecucion());
        }
        totalesHoras = horas + ":" + minutos + " (Horas:minutos)";
        PrimeFaces.current().executeScript("PF('totalHorasDilog').show()");
        PrimeFaces.current().ajax().update("fmTotalHoras");
    }

    public void totalHorasFilter() {
        Teletrabajo t = new Teletrabajo();
        t = teletrabajo;
        totalesHoras = "";
        this.horas = 0;
        this.minutos = 0;
        List<Teletrabajo> result = new ArrayList<>();
        if (fechaDesde != null && fechaHasta != null && (t.getPersonaSolicitada() != null && t.getPersonaSolicitada().getId() != null)) {
            result = service.listaActividadeDiarias(t.getPersonaSolicitada(), fechaDesde, fechaHasta, t.getResponsable());
        } else if (fechaDesde != null && fechaHasta != null && (t.getPersonaSolicitada() == null || t.getPersonaSolicitada().getId() == null)) {
            result = service.listaActividadeDiariasFechas(fechaDesde, fechaHasta, t.getResponsable());
        } else if (fechaDesde == null && fechaHasta == null && (t.getPersonaSolicitada() != null && t.getPersonaSolicitada().getId() != null)) {
            result = service.clientesOnline(t.getPersonaSolicitada(), t.getResponsable());
        } else {
            result = service.listaActividadeDiariasFechas(t.getResponsable());
        }
        for (Teletrabajo item : result) {
            devolverTotalHorasMinutos(item.getTiempoEjecucion());
        }
        totalesHoras = horas + ":" + minutos + " (Horas:minutos)";
    }

//<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public Teletrabajo getTeletrabajo() {
        return teletrabajo;
    }

    public void setTeletrabajo(Teletrabajo teletrabajo) {
        this.teletrabajo = teletrabajo;
    }

    public List<Cliente> getListaRequirientes() {
        return listaRequirientes;
    }

    public void setListaRequirientes(List<Cliente> listaRequirientes) {
        this.listaRequirientes = listaRequirientes;
    }

    public List<CatalogoItem> getListHerramientas() {
        return listHerramientas;
    }

    public void setListHerramientas(List<CatalogoItem> listHerramientas) {
        this.listHerramientas = listHerramientas;
    }

    public LazyModel<Teletrabajo> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<Teletrabajo> lazy) {
        this.lazy = lazy;
    }

    public List<CatalogoItem> getListaHerramientasSeleccionadas() {
        return listaHerramientasSeleccionadas;
    }

    public void setListaHerramientasSeleccionadas(List<CatalogoItem> listaHerramientasSeleccionadas) {
        this.listaHerramientasSeleccionadas = listaHerramientasSeleccionadas;
    }

    public List<HerramientasUtilizadas> getListaHerramientasUtilizadas() {
        return listaHerramientasUtilizadas;
    }

    public void setListaHerramientasUtilizadas(List<HerramientasUtilizadas> listaHerramientasUtilizadas) {
        this.listaHerramientasUtilizadas = listaHerramientasUtilizadas;
    }

    public List<String> getPorcentajeAvance() {
        return porcentajeAvance;
    }

    public void setPorcentajeAvance(List<String> porcentajeAvance) {
        this.porcentajeAvance = porcentajeAvance;
    }

    public String getPorcentajeValor() {
        return porcentajeValor;
    }

    public void setPorcentajeValor(String porcentajeValor) {
        this.porcentajeValor = porcentajeValor;
    }

    public Boolean getGenerarReprote() {
        return generarReprote;
    }

    public void setGenerarReprote(Boolean generarReprote) {
        this.generarReprote = generarReprote;
    }

    public Date getInicio() {
        return inicio;
    }

    public void setInicio(Date inicio) {
        this.inicio = inicio;
    }

    public Date getFin() {
        return fin;
    }

    public void setFin(Date fin) {
        this.fin = fin;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public Cliente getClienteFiltrados() {
        return clienteFiltrados;
    }

    public void setClienteFiltrados(Cliente clienteFiltrados) {
        this.clienteFiltrados = clienteFiltrados;
    }

    public List<DocumentosModel> getDocumentosListas() {
        return documentosListas;
    }

    public void setDocumentosListas(List<DocumentosModel> documentosListas) {
        this.documentosListas = documentosListas;
    }

    public DocumentosModel getArchivo() {
        return archivo;
    }

    public void setArchivo(DocumentosModel archivo) {
        this.archivo = archivo;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public StreamedContent getStreamedContent() {

        return streamedContent;
    }

    public void setStreamedContent(StreamedContent streamedContent) {
        this.streamedContent = streamedContent;
    }

    public LazyModel<Teletrabajo> getLazyControl() {
        return lazyControl;
    }

    public void setLazyControl(LazyModel<Teletrabajo> lazyControl) {
        this.lazyControl = lazyControl;
    }

    public List<UploadedFile> getFiles() {
        return files;
    }

    public void setFiles(List<UploadedFile> files) {
        this.files = files;
    }

    public UploadedFile getFile() {
        return file;
    }

    public void setFile(UploadedFile file) {
        this.file = file;
    }

    public List<ListarequisitosModel> getListaRequisitosGlobal() {
        return listaRequisitosGlobal;
    }

    public void setListaRequisitosGlobal(List<ListarequisitosModel> listaRequisitosGlobal) {
        this.listaRequisitosGlobal = listaRequisitosGlobal;
    }

    public ListarequisitosModel getRequisitosObjeto() {
        return requisitosObjeto;
    }

    public void setRequisitosObjeto(ListarequisitosModel requisitosObjeto) {
        this.requisitosObjeto = requisitosObjeto;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public Cliente getClienteFiltar() {
        return clienteFiltar;
    }

    public void setClienteFiltar(Cliente clienteFiltar) {
        this.clienteFiltar = clienteFiltar;
    }

    public List<Cliente> getClientesTeletrabajo() {
        return clientesTeletrabajo;
    }

    public void setClientesTeletrabajo(List<Cliente> clientesTeletrabajo) {
        this.clientesTeletrabajo = clientesTeletrabajo;
    }

    public String getTotalesHoras() {
        return totalesHoras;
    }

    public void setTotalesHoras(String totalesHoras) {
        this.totalesHoras = totalesHoras;
    }

//</editor-fold>
}
