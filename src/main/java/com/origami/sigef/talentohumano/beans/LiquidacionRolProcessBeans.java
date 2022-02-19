/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.RolRubro;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.model.LiquidacionRolDAO;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.talentohumano.services.CaucionServidoresService;
import com.origami.sigef.talentohumano.services.CuotaAnticipoService;
import com.origami.sigef.talentohumano.services.DescuentoRubroValorService;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.FondosReservaService;
import com.origami.sigef.talentohumano.services.HoraExtraSuplementariaService;
import com.origami.sigef.talentohumano.services.LiquidacionRolService;
import com.origami.sigef.talentohumano.services.OtroDescuentoService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.PrestamoIESService;
import com.origami.sigef.talentohumano.services.RetencionImpuestoRentaService;
import com.origami.sigef.talentohumano.services.RolRubroService;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
 * @author ORIGAMI2
 */
@Named(value = "liquidacionProcesoView")
@ViewScoped
public class LiquidacionRolProcessBeans extends BpmnBaseRoot implements Serializable {

    private static final Logger LOG = Logger.getLogger(LiquidacionRolProcessBeans.class.getName());

    @Inject
    private RolRubroService rolRubroService;
    @Inject
    private UserSession userSession;
    @Inject
    private LiquidacionRolService liquidacionRolService;
    @Inject
    private RolesDePagoService rolesPagoService;
    @Inject
    private ValoresRolesService valorRolesService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private MasterCatalogoService catalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private DiasLaboradoService diasService;
    @Inject
    private DistributivoEscalaService distributivoEscaService;
    @Inject
    private FondosReservaService fondosService;
    @Inject
    private OtroDescuentoService otroDescuentoService;
    @Inject
    private DescuentoRubroValorService descuentoService;
    @Inject
    private HoraExtraSuplementariaService horasService;
    @Inject
    private ValoresDistributivoService valoresService;
    @Inject
    private DistributivoAnexoService distAnexoService;
    @Inject
    private AnticipoRemuneracionService anticipoService;
    @Inject
    private CuotaAnticipoService cuotaService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private PrestamoIESService prestamoService;
    @Inject
    private RetencionImpuestoRentaService retencionService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private CaucionServidoresService caucionService;
    @Inject
    private ClienteService clienteService;

    private LazyModel<LiquidacionRol> lazy;
    private List<TipoRol> listaRol;
    private List<MasterCatalogo> periodos;
    private List<RolesDePago> listaRoles;
    private List<RolesDePago> auxLista;
    private List<ValoresRoles> listaValoresRoles;
    private List<ValoresRoles> listaValoresRolesAnexos;
    private List<LiquidacionRol> listaLiquidar;
    private List<LiquidacionRolDAO> listaLiquidarDAO;
    private TipoRol rolSeleccionado;
    private OpcionBusqueda busqueda;
    private LiquidacionRol liquidacionRol;
    private RolRubro rolRubro;
    private String cedula;
    private CatalogoItem deuda;
    private CatalogoItem aprobado;
    private CatalogoItem registrado;
    private DistributivoEscala escala;
    private LiquidacionRolDAO liquidacionDAO;
    private ValoresRoles rubroAnexoSeleccionado;
    private BigDecimal rmuMesGlobal;
    private BigDecimal valorAportePartronal;
    private BigDecimal netoRecibir;
    private BigDecimal totalNeto;
    private BigDecimal totalEgreso;
    private BigDecimal totalIngreso;
    private Boolean enviarTramite;
    private Boolean renderFondoReserva;
    //proceso
    @Inject
    private FileUploadDoc uploadDoc;

    private UploadedFile files;
    private String observaciones;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
            }
        }
        cedula = "";
        totalEgreso = BigDecimal.ZERO;
        totalIngreso = BigDecimal.ZERO;
        totalNeto = BigDecimal.ZERO;
        busqueda = new OpcionBusqueda();
        rolSeleccionado = new TipoRol();
        listaRoles = new ArrayList<>();
        listaLiquidarDAO = new ArrayList<>();
        listaValoresRolesAnexos = new ArrayList<>();
        liquidacionDAO = new LiquidacionRolDAO();
        rubroAnexoSeleccionado = new ValoresRoles();
        auxLista = rolesPagoService.findRolesXPeriodo(busqueda.getAnio());
        aprobado = catalogoItemService.getEstadoRol("aprobado-rol");
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listaRol = tipoRolService.listaRolesAdicionalAndGenerelByEstadoRol(busqueda.getAnio(), registrado);
        deuda = anticipoService.getEstadoAnticipo("EST_ANTI", (short) 3);
    }

    //<editor-fold defaultstate="collapsed" desc="Tarea">
    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            getParamts().put("usuarioTTHH", clienteService.getrolsUser(RolUsuario.titularTH));
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

    public void abriDlogo() {
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="LIQUIDAR ROL">

    public void liquidarRol() {
        if (rolSeleccionado != null) {
            cancelar();
            calcularValores();
            lazy = new LazyModel<>(LiquidacionRol.class);
            lazy.getFilterss().put("estado", true);
            lazy.getFilterss().put("tipoRol", rolSeleccionado);
            lazy.getSorteds().put("rolPago.servidor.persona.apellido", "ASC");
            lazy.setDistinct(false);
            if (rolSeleccionado.getId() != null) {
                enviarTramite = true;

            } else {
                enviarTramite = true;
            }
            //determinar el tipo del rol que pertecene
            if (rolSeleccionado.getTipoRol().getCodigo().equals("ROL_FONDO_RESERVA")) {
                renderFondoReserva = true;
            } else {
                renderFondoReserva = false;
            }
        } else {
            cancelar();
            calcularValores();
            lazy = null;
            renderFondoReserva = false;
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Reporte Roles">
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
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="MEODOS EDITAR-ELIMINAR-CALCULARTOTALES-ROLES">
    public void calcularTotales(LiquidacionRol liq) {
        netoRecibir = BigDecimal.ZERO;
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        double totaIng = 0, totalEg = 0, totalNeto = 0;
        for (RolRubro r : lista) {
            if (r.getValorAsignacion().getValorParametrizable().getClasificacion().getCodigo().equals("I")) {
                totaIng = totaIng + r.getValorRubro().doubleValue();
            } else {
                totalEg = totalEg + r.getValorRubro().doubleValue();
            }
        }
        totalNeto = totaIng - totalEg;
        liq.setTotalIngreso(new BigDecimal(totaIng));
        liq.setTotalEgreso(new BigDecimal(totalEg));
        liq.setNetoRecibir(new BigDecimal(totalNeto));
        netoRecibir = liq.getNetoRecibir();
        liquidacionRolService.edit(liq);
    }

    public void calcularValores() {
        totalEgreso = liquidacionRolService.totalEgreso(rolSeleccionado);
        totalIngreso = liquidacionRolService.totalIngreso(rolSeleccionado);
        totalNeto = liquidacionRolService.totalNeto(rolSeleccionado);
    }

    public void newValores() {
        totalEgreso = BigDecimal.ZERO;
        totalIngreso = BigDecimal.ZERO;
        totalNeto = BigDecimal.ZERO;
    }

    public void editar(LiquidacionRol liq) {
        this.cedula = liq.getRolPago().getServidor().getPersona().getIdentificacion();
        this.liquidacionRol = liq;
        llenasListaDAO(liq);
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

    public void actulizarRoles() {
        listaRol = tipoRolService.listaRolesXanio(busqueda.getAnio());
    }

    private void llenasListaDAO(LiquidacionRol liq) {
        listaValoresRolesAnexos = valorRolesService.rubrosTipoOrigen(rolSeleccionado.getAnio(), liq.getRolPago(), "DIS-ANE");
        listaLiquidarDAO = new ArrayList<>();
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        for (RolRubro r : lista) {
            if (!"SUELDON-EGRESO".equals(r.getValorAsignacion().getValorParametrizable().getTipo().getCodigo())) {
                liquidacionDAO = new LiquidacionRolDAO();
                liquidacionDAO.setLiquidacionRol(new LiquidacionRol());
                liquidacionDAO.setValorAsignacion(new ValoresRoles());
                liquidacionDAO.setLiquidacionRol(r.getLiquidacionRol());
                liquidacionDAO.setValorRubro(r.getValorRubro());
                liquidacionDAO.setValorAsignacion(r.getValorAsignacion());
                listaLiquidarDAO.add(liquidacionDAO);
            }
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="APRO-ROL">
    public void aprobarRol() {
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Rol no Seleccionando");
            return;
        }
        if (rolSeleccionado.getEstadoAprobacion().getCodigo().equals("aprobado-rol") || rolSeleccionado.getEstadoAprobacion().getCodigo().equals("pagado-rol")) {
            JsfUtil.addWarningMessage("Información", "Rol se encuentra " + rolSeleccionado.getEstadoAprobacion().getTexto());
            return;
        }
        listaLiquidar = new ArrayList<>();
        listaLiquidar = liquidacionRolService.getListaXrol(rolSeleccionado);
        int cont;
        for (LiquidacionRol lr : listaLiquidar) {
            cont = 0;
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("RET_JUD", lr.getRolPago(), rolSeleccionado))
                    && Utils.isNotEmpty(descuentoService.rubroOtroDescuento("RET_JUD", rolSeleccionado, lr.getRolPago()))) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de Retenciones Judiciales " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("OTROS_E", lr.getRolPago(), rolSeleccionado))
                    && Utils.isNotEmpty(descuentoService.rubroOtroDescuento("OTROS_E", rolSeleccionado, lr.getRolPago()))) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de OTROS EGRESOS " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("PRES_QUI", lr.getRolPago(), rolSeleccionado))
                    && Utils.isNotEmpty(prestamoService.prestamoRubro(lr.getRolPago().getServidor(), "PRES_QUI", rolSeleccionado))) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de PRESTAMO QUIROGRAFARIO " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("PRES_HIP", lr.getRolPago(), rolSeleccionado))
                    && Utils.isNotEmpty(prestamoService.prestamoRubro(lr.getRolPago().getServidor(), "PRES_HIP", rolSeleccionado))) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de PRESTAMO HIPOTECARIO " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("IMP_RENTA", lr.getRolPago(), rolSeleccionado))
                    && Utils.isNotEmpty(retencionService.retencionMensualRubro(lr.getRolPago().getServidor(), "IMP_RENTA", rolSeleccionado))) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de RETENCION IMPUESTO A LA RENTA " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("CAUCIONES", lr.getRolPago(), rolSeleccionado))
                    && caucionService.caucionRubro(rolSeleccionado, lr.getRolPago().getServidor(), "CAUCIONES") != null) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de CAUCION SERVIDOR " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (Utils.isEmpty(rolesPagoService.rubrosAsignado("ANT_RMU", lr.getRolPago(), rolSeleccionado))
                    && Utils.isNotEmpty(anticipoService.anticipoRubro(lr.getRolPago().getServidor(), "ANT_RMU", rolSeleccionado))) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de ANTICIPO DE RMU " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (cont > 0) {
                return;
            }
        }
        rolSeleccionado.setEstadoAprobacion(aprobado);
        rolSeleccionado.setNumTramite(new BigInteger(this.tramite.getNumTramite() + ""));
        rolSeleccionado.setUsuarioRegistraAprueba(userSession.getNameUser());
        rolSeleccionado.setUsuarioModifica(userSession.getNameUser());
        rolSeleccionado.setFechaModificacion(new Date());
        tipoRolService.edit(rolSeleccionado);
        JsfUtil.addSuccessMessage("Información", "Rol " + rolSeleccionado.getMes().getDescripcion() + " Aprobado Correctamente.");
        JsfUtil.addInformationMessage("Información", "Guardar Documentación.");
        //System.out.println("aprobado");
    }
//</editor-fold>

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
//            PrimeFaces.current().ajax().update(":panelRol");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void openDialogRol(LiquidacionRol liq) {
        this.cedula = liq.getRolPago().getServidor().getPersona().getIdentificacion();
        this.liquidacionRol = liq;
//        System.out.println("liqiodacion neto " + liquidacionRol.getNetoRecibir());
        llenasListaDAO(liq);
        PrimeFaces.current().executeScript("PF('dlgDetalle').show()");
        PrimeFaces.current().ajax().update("frmDetalleRol");

    }

//<editor-fold defaultstate="collapsed" desc="metodos de cancelar y newObjet's">
    private void newLiquidacionRol() {
        cedula = "";
        liquidacionRol = new LiquidacionRol();
        liquidacionRol.setRmu(new DistributivoEscala());
        liquidacionRol.setRolPago(new RolesDePago());
        liquidacionRol.setTipoRol(new TipoRol());
    }

    private void newRolRubro() {
        rolRubro = new RolRubro();
        rolRubro.setLiquidacionRol(new LiquidacionRol());
        rolRubro.setValorAsignacion(new ValoresRoles());
    }

    public void cancelar() {
        cedula = "";
        liquidacionRol = new LiquidacionRol();
        listaLiquidarDAO = new ArrayList<>();
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="Getter's and Setter's">
    public Boolean getRenderFondoReserva() {
        return renderFondoReserva;
    }

    public void setRenderFondoReserva(Boolean renderFondoReserva) {
        this.renderFondoReserva = renderFondoReserva;
    }

    public List<LiquidacionRolDAO> getListaLiquidarDAO() {
        return listaLiquidarDAO;
    }

    public void setListaLiquidarDAO(List<LiquidacionRolDAO> listaLiquidarDAO) {
        this.listaLiquidarDAO = listaLiquidarDAO;
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

    public LazyModel<LiquidacionRol> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<LiquidacionRol> lazy) {
        this.lazy = lazy;
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

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public LiquidacionRol getLiquidacionRol() {
        return liquidacionRol;
    }

    public void setLiquidacionRol(LiquidacionRol liquidacionRol) {
        this.liquidacionRol = liquidacionRol;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<ValoresRoles> getListaValoresRolesAnexos() {
        return listaValoresRolesAnexos;
    }

    public void setListaValoresRolesAnexos(List<ValoresRoles> listaValoresRolesAnexos) {
        this.listaValoresRolesAnexos = listaValoresRolesAnexos;
    }

    public LiquidacionRolDAO getLiquidacionDAO() {
        return liquidacionDAO;
    }

    public void setLiquidacionDAO(LiquidacionRolDAO liquidacionDAO) {
        this.liquidacionDAO = liquidacionDAO;
    }

    public ValoresRoles getRubroAnexoSeleccionado() {
        return rubroAnexoSeleccionado;
    }

    public void setRubroAnexoSeleccionado(ValoresRoles rubroAnexoSeleccionado) {
        this.rubroAnexoSeleccionado = rubroAnexoSeleccionado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getEnviarTramite() {
        return enviarTramite;
    }

    public void setEnviarTramite(Boolean enviarTramite) {
        this.enviarTramite = enviarTramite;
    }
//</editor-fold>
}
