/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.procesos.pagos;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.RolRubro;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.AcumulacionFondoReservaService;
import com.origami.sigef.talentohumano.services.FondosReservaService;
import com.origami.sigef.talentohumano.services.LiquidacionRolService;
import com.origami.sigef.talentohumano.services.RolRubroService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author jintr
 */
@Named(value = "liquidacionRolView")
@ViewScoped
public class LiquidacionRolController extends BpmnBaseRoot implements Serializable {

    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private MasterCatalogoService catalogoService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private LiquidacionRolService liquidacionRolService;
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private RolRubroService rolRubroService;
    @Inject
    private FondosReservaService fondosService;
    @Inject
    private AcumulacionFondoReservaService acumulacionFondoService;

    private UploadedFile files;
    private OpcionBusqueda busqueda;
    private TipoRol rolSeleccionado;
    private CatalogoItem registrado;
    private LiquidacionRol liquidacionRol;
    private CatalogoItem fondoReservaItem;

    private List<TipoRol> listaRol;
    private List<MasterCatalogo> periodos;

    private LazyModel<FondosReserva> liquidacionFondosLazy;
    private LazyModel<LiquidacionRol> liquidacionRolLazy;

    private Boolean horasExtras;
    private Boolean roles;
    private Boolean fondosReserva;
    private Boolean enviarTramite;
    private Boolean disable = Boolean.FALSE;

    private BigDecimal totalNeto;
    private BigDecimal totalEgreso;
    private BigDecimal totalIngreso;
    private BigDecimal totalAcumula;
    private BigDecimal totalNoAcumula;

    private String observaciones;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
            }
        }
        busqueda = new OpcionBusqueda();
        rolSeleccionado = new TipoRol();
        liquidacionRol = new LiquidacionRol();
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        fondoReservaItem = catalogoItemService.getEstadoRol("ACU-FONDOS-RESERVA");
        actualizarRoles();
        newValores();
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        cargarTramite();
    }

    private void cargarTramite() {
        TipoRol temp = tipoRolService.getFindRol(tramite.getNumTramite(), busqueda.getAnio());
        if (temp != null) {
            rolSeleccionado = temp;
            disable = Boolean.TRUE;
            liquidarRol();
        }
    }

    public void actualizarRoles() {
        listaRol = tipoRolService.listaRolesAdicionalAndGenerelByEstadoRol(busqueda.getAnio(), registrado);
    }

    public void liquidarRol() {
        if (rolSeleccionado != null) {
            enviarTramite = true;
            calcularValores();
            switch (rolSeleccionado.getTipoRol().getCodigo()) {
                case "ROL_FONDO_RESERVA":
                    roles = Boolean.FALSE;
                    fondosReserva = Boolean.TRUE;
                    horasExtras = Boolean.FALSE;
                    liquidacionFondosLazy = new LazyModel<>(FondosReserva.class);
                    liquidacionFondosLazy.setDistinct(false);
                    liquidacionFondosLazy.getFilterss().put("estado", true);
                    liquidacionFondosLazy.getFilterss().put("tipoRol", rolSeleccionado);
                    liquidacionFondosLazy.setDistinct(false);
                    totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.TRUE);
                    totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.FALSE);
                    break;
                case "ROL-HORAS-EXTRAS":
                    roles = Boolean.FALSE;
                    fondosReserva = Boolean.FALSE;
                    horasExtras = Boolean.TRUE;
                    break;
                default:
                    roles = Boolean.TRUE;
                    fondosReserva = Boolean.FALSE;
                    horasExtras = Boolean.FALSE;
                    liquidacionRolLazy = new LazyModel<>(LiquidacionRol.class);
                    liquidacionRolLazy.getFilterss().put("estado", true);
                    liquidacionRolLazy.getFilterss().put("tipoRol", rolSeleccionado);
                    liquidacionRolLazy.getSorteds().put("rolPago.servidor.persona.apellido", "ASC");
                    liquidacionRolLazy.setDistinct(false);
                    break;

            }
        } else {
            newValores();
            enviarTramite = false;
            liquidacionFondosLazy = null;
            liquidacionRolLazy = null;
        }
    }

    public void newValores() {
        totalEgreso = BigDecimal.ZERO;
        totalIngreso = BigDecimal.ZERO;
        totalNeto = BigDecimal.ZERO;
        totalAcumula = BigDecimal.ZERO;
        totalNoAcumula = BigDecimal.ZERO;
        horasExtras = Boolean.FALSE;
        roles = Boolean.FALSE;
        fondosReserva = Boolean.FALSE;
        enviarTramite = Boolean.FALSE;
    }

    public void calcularValores() {
        totalEgreso = liquidacionRolService.totalEgreso(rolSeleccionado);
        totalIngreso = liquidacionRolService.totalIngreso(rolSeleccionado);
        totalNeto = liquidacionRolService.totalNeto(rolSeleccionado);
    }

    public void impresionResumenRol() {
        Boolean etiquetas = Boolean.FALSE;
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (!rolSeleccionado.getEstadoAprobacion().getCodigo().equals("registrado-rol")) {
            etiquetas = Boolean.TRUE;
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        serveltSession.addParametro("id_tipo_roll", rolSeleccionado.getId());
        serveltSession.addParametro("MOSTRAR_ETIQUETA_APROBADO", etiquetas);
        serveltSession.setNombreReporte("rol_resumen");
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void impresionDetalleRol() {
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        serveltSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        serveltSession.addParametro("mes", rolSeleccionado.getMes().getCodigo().toUpperCase());
        serveltSession.setNombreReporte("rol_all");
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("SUBREPOR_DIR_VALORRUBRO", JsfUtil.getRealPath("reportes/LiquidacionRol/"));
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void impresionRoGenerall() {
        Boolean etiquetas = Boolean.FALSE;
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (!rolSeleccionado.getEstadoAprobacion().getCodigo().equals("registrado-rol")) {
            etiquetas = Boolean.TRUE;
        }
        calcularValores();
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        Distributivo distributivoRev = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        serveltSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        serveltSession.addParametro("MOSTRAR_ETIQUETA_APROBADO", etiquetas);
        serveltSession.addParametro("total_ing", totalIngreso);
        serveltSession.addParametro("total_eg", totalEgreso);
        serveltSession.addParametro("total_neto", totalNeto);
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_revisor", distributivoRev != null ? distributivoRev.getCargo().getNombreCargo() : "");
        serveltSession.setNombreReporte("rol_general");
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void impresionRoles(LiquidacionRol liq) {
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        serveltSession.addParametro("id", liq.getId());
        serveltSession.addParametro("mes", liq.getTipoRol().getMes().getCodigo().toUpperCase());
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.setNombreReporte("rol_individual");
        serveltSession.addParametro("SUBREPOR_DIR_VALORRUBRO", JsfUtil.getRealPath("reportes/LiquidacionRol/"));
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void imprimirReporte(String tipoArchivo) {
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        Distributivo distributivoRev = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        serveltSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_revisor", distributivoRev != null ? distributivoRev.getCargo().getNombreCargo() : "");
        /*Imprimir reporte seleccionado*/
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            serveltSession.setOnePagePerSheet(true);
        }
        serveltSession.addParametro("filtroTodos", 1);
        serveltSession.addParametro("acumula", true);
        serveltSession.setNombreReporte("fondosReservaGeneral");
        serveltSession.setContentType(tipoArchivo);
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void adjuntarDocumento() {
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUploadInformeTec(FileUploadEvent event) {
        try {
            files = event.getFile();
            if (files != null) {
                uploadDoc.upload(tramite, files);
            }
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void eliminar(LiquidacionRol liq) {
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        for (RolRubro r : lista) {
            r.setEstado(Boolean.FALSE);
            rolRubroService.edit(r);
        }
        liq.setEstado(Boolean.FALSE);
        liquidacionRolService.edit(liq);
        calcularValores();
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            getParamts().put("usuarioTalentoHumano", clienteService.getrolsUser(RolUsuario.titularTH));
            rolSeleccionado.setNumTramite(BigInteger.valueOf(tramite.getNumTramite()));
            tipoRolService.edit(rolSeleccionado);
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

    public void eliminar(FondosReserva fondo) {
        fondo.setEstado(Boolean.FALSE);
        fondosService.edit(fondo);
        totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.TRUE);
        totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.FALSE);
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public LiquidacionRol getLiquidacionRol() {
        return liquidacionRol;
    }

    public void setLiquidacionRol(LiquidacionRol liquidacionRol) {
        this.liquidacionRol = liquidacionRol;
    }

    public List<TipoRol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<TipoRol> listaRol) {
        this.listaRol = listaRol;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public LazyModel<FondosReserva> getLiquidacionFondosLazy() {
        return liquidacionFondosLazy;
    }

    public void setLiquidacionFondosLazy(LazyModel<FondosReserva> liquidacionFondosLazy) {
        this.liquidacionFondosLazy = liquidacionFondosLazy;
    }

    public LazyModel<LiquidacionRol> getLiquidacionRolLazy() {
        return liquidacionRolLazy;
    }

    public void setLiquidacionRolLazy(LazyModel<LiquidacionRol> liquidacionRolLazy) {
        this.liquidacionRolLazy = liquidacionRolLazy;
    }

    public Boolean getHorasExtras() {
        return horasExtras;
    }

    public void setHorasExtras(Boolean horasExtras) {
        this.horasExtras = horasExtras;
    }

    public Boolean getRoles() {
        return roles;
    }

    public void setRoles(Boolean roles) {
        this.roles = roles;
    }

    public Boolean getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(Boolean fondosReserva) {
        this.fondosReserva = fondosReserva;
    }

    public Boolean getEnviarTramite() {
        return enviarTramite;
    }

    public void setEnviarTramite(Boolean enviarTramite) {
        this.enviarTramite = enviarTramite;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public BigDecimal getTotalEgreso() {
        return totalEgreso;
    }

    public void setTotalEgreso(BigDecimal totalEgreso) {
        this.totalEgreso = totalEgreso;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(BigDecimal totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotalAcumula() {
        return totalAcumula;
    }

    public void setTotalAcumula(BigDecimal totalAcumula) {
        this.totalAcumula = totalAcumula;
    }

    public BigDecimal getTotalNoAcumula() {
        return totalNoAcumula;
    }

    public void setTotalNoAcumula(BigDecimal totalNoAcumula) {
        this.totalNoAcumula = totalNoAcumula;
    }

    public Boolean getDisable() {
        return disable;
    }

    public void setDisable(Boolean disable) {
        this.disable = disable;
    }

}
