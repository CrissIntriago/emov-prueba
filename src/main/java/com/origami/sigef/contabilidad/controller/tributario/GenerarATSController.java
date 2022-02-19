/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller.tributario;

import com.google.gson.Gson;
import com.origami.sigef.Entidad.Controller.DatosGeneralesEntidadController;
import com.origami.sigef.ats.entities.AtsArchivo;
import com.origami.sigef.ats.modelAts.Compras;
import com.origami.sigef.ats.modelAts.DetalleCompras;
import com.origami.sigef.ats.modelAts.DetalleVentas;
import com.origami.sigef.ats.modelAts.FormasPagoModel;
import com.origami.sigef.ats.modelAts.IVA;
import com.origami.sigef.ats.modelAts.VentaEstablecimiento;
import com.origami.sigef.ats.modelAts.Ventas;
import com.origami.sigef.ats.service.AtsArchivoService;
import com.origami.sigef.certificacion_presupuesto_anual.service.ReservaCompromisoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.FilesUtil;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.model.TalonResumenModel;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.entities.Cajero;
import com.origami.sigef.tesoreria.comprobantelectronico.service.CajeroService;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.DocumentosUtil;
import com.origami.sigef.tesoreria.entities.Liquidacion;
import com.origami.sigef.tesoreria.entities.LiquidacionPago;
import com.origami.sigef.tesoreria.service.LiquidacionPagoService;
import com.origami.sigef.tesoreria.service.LiquidacionService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.File;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.event.FileUploadEvent;

/**
 *
 * @author ORIGAMI
 */
@Named(value = "generacionATSView")
@ViewScoped
public class GenerarATSController extends BpmnBaseRoot implements Serializable {

    @Inject
    private KatalinaService katalinaService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private LiquidacionService liquidacionService;
    @Inject
    private LiquidacionPagoService liquidacionPagoService;
    @Inject
    private ValoresService valoresService;
    @Inject
    private AtsArchivoService atsArchivoService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private ReservaCompromisoService reservaCompromisoService;
    @Inject
    private CajeroService cajeroService;

    private IVA ats;
    private List<Integer> anios;
    private Integer anioMax;
    private List<String> meses;
    private Integer anio;
    private Integer mes;
    private List<Liquidacion> liquidacionesCompras;
    private Compras compras;
    private Ventas ventas;
    private Gson gson;
    private BigDecimal totalVentas;
    private String ruta;
    private VentaEstablecimiento ventaEstablecimiento;

    private List<AtsArchivo> atsArchivos;

    private String descripcionObservacion, verificacionDebito;
    private Long numTramite;

    private Boolean tareaRegistroContable, transferenciaAprobada;

    private AtsArchivo atsDebito;

    ///PARA  EL TALON DE RESUMEN :D
    private List<TalonResumenModel> talonResumenModels;
    private BigDecimal totalCompras = BigDecimal.ZERO;

    @PostConstruct
    public void init() {
        tareaRegistroContable = Boolean.FALSE;
        if (!servletSession.estaVacio()) { //PARA CUANDO REGRESE DEL DIARIO GENERAL
            this.setTaskId(String.valueOf(servletSession.getParametros().get("taskID")));
        }
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                numTramite = tramite.getNumTramite();

                atsArchivos = atsArchivoService.findAllAtsArchivos(numTramite);
                if (atsArchivos == null) {
                    atsArchivos = new ArrayList<>();
                } else {
                    tareaRegistroContable = Boolean.TRUE;
                    switch (tarea.getName()) {
                        case "Registro contable":
                            saveAtsArchivo("Registro contable retención IVA", "NO");
                            saveAtsArchivo("Registro contable retención RENTAS", "NO");
                            break;
                        case "Verificación de débito automático":
                            saveAtsArchivo("Verificación de débito automático", "");
                            atsDebito = atsArchivos.get(atsArchivos.size() - 1);
                            break;
                    }
                }

            }
        }

        loadModel();
        gson = new Gson();
        ruta = valoresService.findByCodigo("RUTA_ATS");
    }

    public void loadModel() {
        compras = new Compras();
        compras.setDetalleCompras(new ArrayList<>());
        ventas = new Ventas();
        ventaEstablecimiento = new VentaEstablecimiento();
        ventas.setDetalleVentas(new ArrayList<>());
        ats = new IVA();
        totalVentas = BigDecimal.ZERO;
        talonResumenModels = new ArrayList<>();
        loadAniosMeses();
    }

    private void loadAniosMeses() {
        anios = new ArrayList<>();
        anioMax = (Utils.getAnio(new Date()) - 1);
        for (int i = anioMax; i <= (Utils.getAnio(new Date())); i++) {
            anios.add(i);
        }
        Collections.sort(anios, Collections.reverseOrder());
        meses = Utils.getMeses();
        anio = anios.get(0);
        mes = Utils.getMes(new Date());
    }

    public Integer mes(String mes) {
        return Utils.convertirLetraAMes(mes);
    }

    public void saveAtsArchivo(String descripcion, String ruta) {
//        String descripcion = tipo ?  : "Talón de resumen";
        Integer numeroMes = mes, numeroAnio = anio;
        Long id = null;
        if (Utils.isNotEmpty(atsArchivos)) {
            for (AtsArchivo ats : atsArchivos) {
                numeroAnio = ats.getAnio();
                numeroMes = ats.getMes();
                if (ats.getDescripcion().equals(descripcion)) {
                    id = ats.getId();
                    break;
                }
            }
        }
        if (id == null) {
            AtsArchivo atsArchivo = new AtsArchivo(numTramite, descripcion, new Date(), session.getNameUser(), ruta, numeroAnio, numeroMes);
            atsArchivo = atsArchivoService.create(atsArchivo);
            if (atsArchivos == null) {
                atsArchivos = new ArrayList<>();
            }
            atsArchivos.add(atsArchivo);
        }
    }

    public void generarXML() {
        if (mes != null && anio != null) {
            liquidacionesCompras = liquidacionService.findAtsByLiquidacion(anio, mes);
            ventas.setDetalleVentas(liquidacionService.findAllVentasByLiquidacion(anio, mes));
            generacionATS();
            createVentas();
            createVentasEstablecimiento();
            if (compras.getDetalleCompras() != null && !compras.getDetalleCompras().isEmpty()) {
                ats.setCompras(compras);
            } else {
                createATSCabecera();
            }
            ruta = ruta + "AT-" + String.format("%02d", mes) + anio + ".xml";
            if (DocumentosUtil.crearArchivo(ats, ruta)) {
                saveAtsArchivo("Archivo ATS-" + String.format("%02d", mes) + "-" + anio, ruta);
                descargarAts(ruta, anio, mes);
            } else {
                JsfUtil.addErrorMessage("", "ATS no generado");
            }
            loadModel();
        }
    }

    public void descargarAts(String ruta, Integer anio, Integer mes) {
        if (!ruta.isEmpty()) {
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            servletSession.setNombreDocumento(ruta);
            servletSession.setContentType("text/xml");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
        } else {
            this.anio = anio;
            this.mes = mes;
            generarTalonResumen();
        }
    }

    public void debitoAutomatico(AtsArchivo atsArchivo) {
        verificacionDebito = "";
        this.atsDebito = atsArchivo;
        JsfUtil.update("frmArchivo");
        JsfUtil.executeJS("PF('dlgArchivoDebito').show();");
    }

    public void closeDebitoAutomatico() {
        atsDebito.setDescripcion(atsDebito.getDescripcion() + " : " + verificacionDebito);
        atsArchivoService.edit(atsDebito);
        JsfUtil.update("formMaster");
        JsfUtil.executeJS("PF('dlgArchivoDebito').hide();");
    }

    public void handleFileUpload(FileUploadEvent event) {
        try {
            File file = FilesUtil.copyFileServer1(event, ruta);
            uploadDoc.upload(tramite, event.getFile());
            if (file != null) {
                atsDebito.setArchivo(file.getAbsolutePath());
                JsfUtil.addInformationMessage("", "Archivo subido correctamente");
            } else {
                JsfUtil.addErrorMessage("", "Ocurrio un error, intente nuevamente");
            }
        } catch (Exception ex) {
            Logger.getLogger(DatosGeneralesEntidadController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void generacionATS() {
        if (!liquidacionesCompras.isEmpty()) {
            for (Liquidacion l : liquidacionesCompras) {
                if (l.getDetalleCompras() != null && !l.getDetalleCompras().isEmpty()) {
                    l.setAts(gson.fromJson(l.getDetalleCompras(), IVA.class));
                    createCompras(l.getAts(), l);
                }
            }
        }
    }

    public void notificar() {
        String asunto = "NOTIFICACIÓN DE  DÉBITO SRI - " + (Utils.convertirMesALetra(atsArchivos.get(0).getMes()) + "/" + atsArchivos.get(0).getAnio());
        Cliente c = reservaCompromisoService.devuelveClienteNotitfacion2(clienteService.getrolsUser(RolUsuario.financiero));
        enviarCorreo(c.getEmail(), asunto);

        c = reservaCompromisoService.devuelveClienteNotitfacion2(clienteService.getrolsUser(RolUsuario.contador));
        enviarCorreo(c.getEmail(), asunto);

    }

    public void enviarCorreo(String email, String asunto) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + " EL PAGO DE IMPUESTOS AL SRI HA SIDO APROBADO CON EXITO"
                + " SEGÚN EL NÚMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con éxito");

    }

    public void createCompras(IVA atsFromJson, Liquidacion l) {
        if (atsFromJson != null) {
            createATSCabecera(atsFromJson);
            for (DetalleCompras comprasDetalle : atsFromJson.getCompras().getDetalleCompras()) {
                comprasDetalle.setAutRetencion1(l.getNumeroAutorizacion());
                comprasDetalle.setFechaEmiRet1(formatoFecha(comprasDetalle.getFechaEmision()));
                comprasDetalle.setFechaEmision(formatoFecha(comprasDetalle.getFechaEmision()));
                comprasDetalle.setFechaRegistro(formatoFecha(comprasDetalle.getFechaEmision()));
                compras.getDetalleCompras().add(comprasDetalle);
            }
        }
    }

    private String formatoFecha(String fechaJso) {
        if (!fechaJso.contains("/")) {
            SimpleDateFormat formatoDelTexto = new SimpleDateFormat("yyyy-MM-dd");
            Date fecha = null;
            try {
                fecha = formatoDelTexto.parse(fechaJso);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }
            return Utils.dateFormatPattern("dd/MM/yyyy", fecha);
        } else {
            return fechaJso;
        }
    }

    private void createATSCabecera(IVA atsFromJson) {
        ats.setTipoIDInformante(atsFromJson.getTipoIDInformante());
        ats.setIdInformante(atsFromJson.getIdInformante());
        ats.setRazonSocial(atsFromJson.getRazonSocial());
        ats.setAnio(atsFromJson.getAnio());
        ats.setMes(atsFromJson.getMes());
        ats.setNumEstabRuc(atsFromJson.getNumEstabRuc());
        ats.setCodigoOperativo(atsFromJson.getCodigoOperativo());
    }

    private void createATSCabecera() {
        ats.setTipoIDInformante("R");
        ats.setIdInformante(userSession.getUsuario().getEmpresaId().getRuc());
        ats.setRazonSocial(Utils.quitarTildes(userSession.getUsuario().getEmpresaId().getNombreEntidad()));
        ats.setAnio(anio);
        ats.setMes(String.format("%02d", mes));
        ats.setNumEstabRuc("001");
        ats.setCodigoOperativo("IVA");
        ats.setTotalVentas(ats.getTotalVentas() != null ? ats.getTotalVentas() : BigDecimal.ZERO.setScale(2));
    }

    public void createVentas() {
        double suma = 0.00;
        if (!ventas.getDetalleVentas().isEmpty()) {
            for (DetalleVentas v : ventas.getDetalleVentas()) {
                //se suma los valores que sea de facturas fisicas
                if (v.getTipoEmision().equals("F")) {
                    suma = suma + v.getBaseImpGrav().doubleValue() + v.getBaseImponible().doubleValue();
                }
                //totalVentas.add(v.getBaseImpGrav());
                v.setFormasDePago(createPagosVentas(v.getIdCliente()));
            }
            tipoIdentificacionCliente();
            ats.setVentas(ventas);
            totalVentas = new BigDecimal(suma);
//            ats.setTotalVentas(().setScale(2, RoundingMode.HALF_UP));
            ats.setTotalVentas(totalVentas.setScale(2, RoundingMode.HALF_UP));
        }
    }

    public void createVentasEstablecimiento() {
        ventaEstablecimiento.setVentaEst(liquidacionService.findAllVentasByEstablecimiento(anio, mes));
        ats.setVentasEstablecimiento(ventaEstablecimiento);
    }

    public void tipoIdentificacionCliente() {
        for (DetalleVentas v : ventas.getDetalleVentas()) {
            switch (v.getTpIdCliente()) {
                case "04": //RUC
                    v.setIdCliente(v.getIdCliente() + "001");
                    break;
                case "07": // OTROS
                    v.setIdCliente("999999999");
                    break;
            }
        }
    }

    public FormasPagoModel createPagosVentas(String identificacion) {
        List<LiquidacionPago> pagosVentas = liquidacionPagoService.findAllLiquidacionPagoByCliente_Identificacion(identificacion, mes, anio);
        FormasPagoModel pagosModel = new FormasPagoModel();
        pagosModel.setFormaPago(new ArrayList<>());
        if (!pagosVentas.isEmpty()) {
            for (LiquidacionPago p : pagosVentas) {
                pagosModel.getFormaPago().add(p.getFormaPago().getCodigo());
            }
        }
        return pagosModel;
    }

    public void generarTalonResumen() {
        if (mes != null && anio != null) {
            saveAtsArchivo("Talón de resumen " + String.format("%02d", mes) + "-" + anio, "");
            servletSession.borrarDatos();
            servletSession.borrarParametros();
            servletSession.addParametro("anio", anio);
            servletSession.addParametro("periodo", String.format("%02d", mes) + "-" + anio);
            if (isCargaEnCero()) {
                servletSession.setNombreSubCarpeta("tributacion");
                servletSession.setNombreReporte("talonResumenRetencionEnCero");
            } else {
                servletSession.addParametro("mes", mes);
                talonResumenCompras();
                servletSession.addParametro("SUBREPORT_DIR_TRIBUTACION", JsfUtil.getRealPath("reportes/tributacion/"));
                servletSession.setNombreSubCarpeta("tributacion");
                servletSession.setNombreReporte("talonResumenRetencion");
            }
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        }
    }

    private Boolean isCargaEnCero() {
        liquidacionesCompras = liquidacionService.findAtsByLiquidacion(anio, mes);
        ventas.setDetalleVentas(liquidacionService.findAllVentasByLiquidacion(anio, mes));
        if (liquidacionesCompras.isEmpty() && ventas.getDetalleVentas().isEmpty()) {
            return true;
        }
        return false;
    }

    private void talonResumenCompras() {
        talonResumenModels = liquidacionService.findAllComprasByLiquidacion(anio, mes);
        TalonResumenModel tFactura = new TalonResumenModel();
        TalonResumenModel tNotaDebito = new TalonResumenModel();
        int cont = 0;
        Integer contRegistrosFactura = 0;
        Integer contRegistrosNotas = 0;
        if (!talonResumenModels.isEmpty()) {

            List<TalonResumenModel> newTalon = new ArrayList<>();
            for (TalonResumenModel m : talonResumenModels) {
                cont++;
                if (cont == 1) {
                    switch (m.getCodCompra()) {
                        case "01":
                            initTalonResumenCompra(tFactura, m);
                            break;
                        case "05":
                            initTalonResumenCompra(tNotaDebito, m);
                            break;
                    }
                }
                switch (m.getCodCompra()) {
                    case "01":
                        tFactura.setNumRegistros(contRegistrosFactura++);
                        initTalonResumenCompraSum(tFactura, m);
                        break;
                    case "05":
                        tNotaDebito.setNumRegistros(contRegistrosNotas++);
                        initTalonResumenCompraSum(tNotaDebito, m);
                        break;
                }
            }
            if (tNotaDebito.getCodCompra() != null) {
                newTalon.add(tNotaDebito);
            }
            if (tFactura.getCodCompra() != null) {
                newTalon.add(tFactura);
            }
            //servletSession.setDataSource(new ArrayList());
            servletSession.addParametro("talonResumenModel", newTalon);
        }
    }

    private void initTalonResumenCompra(TalonResumenModel t, TalonResumenModel m) {
        t.setCodCompra(m.getCodCompra());
        t.setLiquidacionId(m.getLiquidacionId());
        t.setFacturaId(m.getFacturaId());
        t.setTransaccion(m.getTransaccion());
    }

    private void initTalonResumenCompraSum(TalonResumenModel t, TalonResumenModel m) {
        t.setBi_tarifa0(t.getBi_tarifa0().add(m.getBi_tarifa0()));
        t.setBi_tarifa_diferente0(t.getBi_tarifa_diferente0().add(m.getBi_tarifa_diferente0()));
        t.setBi_no_objetivoIva(t.getBi_no_objetivoIva().add(m.getBi_no_objetivoIva()));
        t.setValor_iva(t.getValor_iva().add(m.getValor_iva()));
    }

    public void aprobacionTransferencia(Boolean aprobada) {
        this.transferenciaAprobada = aprobada;
        observacionesIngreso();
    }

    public void observacionesIngreso() {
        if (tareaRegistroContable) {
            for (AtsArchivo ats : atsArchivos) {
                if (ats.getDescripcion().contains("retención")) {
                    if (ats.getArchivo().equals("NO")) {
                        JsfUtil.addWarningMessage("Advertencia", "Debe realizar: " + ats.getDescripcion());
                        return;
                    }
                }
            }
        }
        if (Utils.isNotEmpty(atsArchivos)) {
            if (atsArchivos.size() < 1) {
                JsfUtil.addWarningMessage("Advertencia", "Debe generar el ATS y el talón de resumen");
            } else {
                observacion = new Observaciones();
                observacion.setEstado(true);
                observacion.setFecCre(new Date());
                observacion.setTarea(tarea.getName());
                observacion.setUserCre(session.getName());

                JsfUtil.executeJS("PF('dlgObservaciones').show()");
                JsfUtil.update(":frmDlgObser");
            }
        } else {
            JsfUtil.addWarningMessage("Advertencia", "Debe generar el ATS y el talón de resumen");
        }
    }

    public void terminarTarea() {
        try {
            this.observacion.setObservacion(descripcionObservacion);
            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            getParamts().put("financiero", clienteService.getrolsUser(RolUsuario.financiero));
            getParamts().put("tesorero", clienteService.getrolsUser(RolUsuario.tesorero));

            if (tarea.getName().equals("Verificación de débito automático")) {
                getParamts().put("aprobado", transferenciaAprobada ? 0 : 1);
                if (transferenciaAprobada) {
                    atsDebito.setDescripcion(atsDebito.getDescripcion() + " ::: TRANSFERENCIA APROBADA");
                } else {
                    atsDebito.setDescripcion(atsDebito.getDescripcion() + " ::: TRANSFERENCIA RECHAZADA");
                }
                atsArchivoService.edit(atsDebito);
            }
            super.completeTask((HashMap<String, Object>) getParamts());
            dashBoard();
        } catch (Exception e) {
            System.out.println("error " + e);
        }
    }

    public void dashBoard() {
        JsfUtil.redirect(CONFIG.URL_APP + "procesos/bandeja-tareas");
    }

    public String mesValor(Integer mesNumerito) {
        return Utils.convertirMesALetra(mesNumerito);
    }

    public void diarioGeneral(AtsArchivo retencion) {
        servletSession.borrarDatos();
        servletSession.addParametro("taskID", this.session.getTaskID());
        servletSession.addParametro("tramite", numTramite);
        servletSession.addParametro("tarea", tarea.getName());
        servletSession.addParametro("tipo", retencion.getDescripcion().contains("IVA") ? "IVA" : "RENTAS");
        servletSession.addParametro("ats", retencion.getId());
        servletSession.addParametro("diario", retencion.getDiario());
        servletSession.addParametro("diario", retencion.getDiario());
        servletSession.addParametro("diario", retencion.getDiario());
        JsfUtil.redirect(CONFIG.URL_APP + "contabilidad/diarioGeneralIntegrado");

    }

//<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public IVA getAts() {
        return ats;
    }

    public void setAts(IVA ats) {
        this.ats = ats;
    }

    public List<Integer> getAnios() {
        return anios;
    }

    public void setAnios(List<Integer> anios) {
        this.anios = anios;
    }

    public List<String> getMeses() {
        return meses;
    }

    public void setMeses(List<String> meses) {
        this.meses = meses;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer mes) {
        this.mes = mes;
    }

    public List<AtsArchivo> getAtsArchivos() {
        return atsArchivos;
    }

    public void setAtsArchivos(List<AtsArchivo> atsArchivos) {
        this.atsArchivos = atsArchivos;
    }

    public Long getNumTramite() {
        return numTramite;
    }

    public void setNumTramite(Long numTramite) {
        this.numTramite = numTramite;
    }

    public String getDescripcionObservacion() {
        return descripcionObservacion;
    }

    public void setDescripcionObservacion(String descripcionObservacion) {
        this.descripcionObservacion = descripcionObservacion;
    }

    public Boolean getTareaRegistroContable() {
        return tareaRegistroContable;
    }

    public AtsArchivo getAtsDebito() {
        return atsDebito;
    }

    public void setAtsDebito(AtsArchivo atsDebito) {
        this.atsDebito = atsDebito;
    }

    public void setTareaRegistroContable(Boolean tareaRegistroContable) {
        this.tareaRegistroContable = tareaRegistroContable;
    }

    public String getVerificacionDebito() {
        return verificacionDebito;
    }

    public void setVerificacionDebito(String verificacionDebito) {
        this.verificacionDebito = verificacionDebito;
    }

//</editor-fold>
}
