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
import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.RolRubro;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.model.LiquidacionRolDAO;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.talentohumano.services.CaucionServidoresService;
import com.origami.sigef.talentohumano.services.DescuentoRubroValorService;
import com.origami.sigef.talentohumano.services.LiquidacionRolService;
import com.origami.sigef.talentohumano.services.PrestamoIESService;
import com.origami.sigef.talentohumano.services.RetencionImpuestoRentaService;
import com.origami.sigef.talentohumano.services.RolRubroService;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
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
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jintr
 */
@Named(value = "aprobacionLiquidacionRolView")
@ViewScoped
public class AprobacionLiquidacionRolBeans extends BpmnBaseRoot implements Serializable {

    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private RolRubroService rolRubroService;
    @Inject
    private LiquidacionRolService liquidacionRolService;
    @Inject
    private UserSession userSession;
    @Inject
    private RolesDePagoService rolesPagoService;
    @Inject
    private DescuentoRubroValorService descuentoService;
    @Inject
    private PrestamoIESService prestamoService;
    @Inject
    private RetencionImpuestoRentaService retencionService;
    @Inject
    private CaucionServidoresService caucionService;
    @Inject
    private AnticipoRemuneracionService anticipoService;
    @Inject
    private ValoresRolesService valorRolesService;

    private OpcionBusqueda opcionBusqueda;
    private TipoRol rolSeleccionado;
    private CatalogoItem aprobado;
    private LiquidacionRol liquidacionRol;
    private LiquidacionRolDAO liquidacionDAO;

    private BigDecimal totalNeto;
    private BigDecimal totalEgreso;
    private BigDecimal totalIngreso;

    private int codAprobado;

    private String observaciones;
    private String cedula;

    private Boolean completarTareaBtn;

    private LazyModel<LiquidacionRol> lazy;

    private List<LiquidacionRol> listaLiquidar;
    private List<ValoresRoles> listaValoresRolesAnexos;
    private List<LiquidacionRolDAO> listaLiquidarDAO;

    @PostConstruct
    public void initialize() {
        opcionBusqueda = new OpcionBusqueda();
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
            }
        }
        rolSeleccionado = tipoRolService.getFindRol(getTramite().getNumTramite(), opcionBusqueda.getAnio());
        aprobado = catalogoItemService.getEstadoRol("aprobado-rol");
        if (rolSeleccionado.getEstadoAprobacion().getId().equals(aprobado.getId())) {
            completarTareaBtn = true;
        }
        lazy = new LazyModel<>(LiquidacionRol.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getSorteds().put("rolPago.servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
        calcularValores();
    }

    public void editar(LiquidacionRol liquidacion) {
        this.cedula = liquidacion.getRolPago().getServidor().getPersona().getIdentificacion();
        this.liquidacionRol = liquidacion;
        llenasListaDAO(liquidacion);
        PrimeFaces.current().executeScript("PF('detalleDLG').show()");
        PrimeFaces.current().ajax().update("detalleForm");
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

    public void eliminar(LiquidacionRol liquidacion) {
        List<RolRubro> lista = rolRubroService.getListaValores(liquidacion, rolSeleccionado);
        for (RolRubro r : lista) {
            r.setEstado(Boolean.FALSE);
            rolRubroService.edit(r);
        }
        liquidacion.setEstado(Boolean.FALSE);
        liquidacionRolService.edit(liquidacion);
        calcularValores();
    }

    public void impresionRoles(LiquidacionRol liquidacion) {
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        serveltSession.addParametro("id", liquidacion.getId());
        serveltSession.addParametro("mes", liquidacion.getTipoRol().getMes().getCodigo().toUpperCase());
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", catalogoItemService.getEstadoRol(RolUsuario.titularTH) != null ? catalogoItemService.getEstadoRol(RolUsuario.titularTH).getTexto() : "");
        serveltSession.setNombreReporte("rol_individual");
        serveltSession.addParametro("SUBREPOR_DIR_VALORRUBRO", JsfUtil.getRealPath("reportes/LiquidacionRol/"));
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void calcularValores() {
        totalEgreso = liquidacionRolService.totalEgreso(rolSeleccionado).negate();
        totalIngreso = liquidacionRolService.totalIngreso(rolSeleccionado);
        totalNeto = liquidacionRolService.totalNeto(rolSeleccionado);
    }

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
            if (rolesPagoService.rubrosAsignado("RET_JUD", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !descuentoService.rubroOtroDescuento("RET_JUD", rolSeleccionado, lr.getRolPago()).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de Retenciones Judiciales " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("OTROS_E", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !descuentoService.rubroOtroDescuento("OTROS_E", rolSeleccionado, lr.getRolPago()).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de OTROS EGRESOS " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("PRES_QUI", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !prestamoService.prestamoRubro(lr.getRolPago().getServidor(), "PRES_QUI", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de PRESTAMO QUIROGRAFARIO " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("PRES_HIP", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !prestamoService.prestamoRubro(lr.getRolPago().getServidor(), "PRES_HIP", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de PRESTAMO HIPOTECARIO " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("IMP_RENTA", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !retencionService.retencionMensualRubro(lr.getRolPago().getServidor(), "IMP_RENTA", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de RETENCION IMPUESTO A LA RENTA " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("CAUCIONES", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && caucionService.caucionRubro(rolSeleccionado, lr.getRolPago().getServidor(), "CAUCIONES") != null) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de CAUCION SERVIDOR " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("ANT_RMU", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !anticipoService.anticipoRubro(lr.getRolPago().getServidor(), "ANT_RMU", rolSeleccionado).isEmpty()) {
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
        completarTareaBtn = true;
        JsfUtil.addSuccessMessage("Información", "Rol " + rolSeleccionado.getMes().getDescripcion() + " Aprobado Correctamente.");
        JsfUtil.addInformationMessage("Información", "Guardar Documentación.");
        //System.out.println("aprobado");
    }

    public void abriDlogo(int aux) {
        codAprobado = aux;
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }
    
     public String egresoNegativo(LiquidacionRolDAO liq) {
        if (liq.getValorAsignacion().getId() != null) {
            if (liq.getValorAsignacion().getValorParametrizable().getClasificacion().getCodigo().equals("E")) {
                return "-";
            }
        }
        return "";
    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            getParamts().put("aprobado", codAprobado);
            if (codAprobado == 1) {
                getParamts().put("usuarioMax", clienteService.getrolsUser(RolUsuario.maximaAutoridad));
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

    public OpcionBusqueda getOpcionBusqueda() {
        return opcionBusqueda;
    }

    public void setOpcionBusqueda(OpcionBusqueda opcionBusqueda) {
        this.opcionBusqueda = opcionBusqueda;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public LazyModel<LiquidacionRol> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<LiquidacionRol> lazy) {
        this.lazy = lazy;
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

    public Boolean getCompletarTareaBtn() {
        return completarTareaBtn;
    }

    public void setCompletarTareaBtn(Boolean completarTareaBtn) {
        this.completarTareaBtn = completarTareaBtn;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public LiquidacionRol getLiquidacionRol() {
        return liquidacionRol;
    }

    public void setLiquidacionRol(LiquidacionRol liquidacionRol) {
        this.liquidacionRol = liquidacionRol;
    }

    public List<LiquidacionRolDAO> getListaLiquidarDAO() {
        return listaLiquidarDAO;
    }

    public void setListaLiquidarDAO(List<LiquidacionRolDAO> listaLiquidarDAO) {
        this.listaLiquidarDAO = listaLiquidarDAO;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

}
