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
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.services.FondosReservaService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author jesus
 */
@Named(value = "reporteRubroNominaView")
@ViewScoped
public class ReporteRubroNominaController implements Serializable {

    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private FondosReservaService fondosReservaService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private ClienteService clienteService;

    private List<TipoRol> rolesMensuales;
    private List<FondosReserva> fondosReservas;
    private List<CatalogoItem> tipoNominaList;
    private List<CatalogoItem> mesList;
    private List<ParametrosTalentoHumano> valoresRubrosList;
    private List<ParametrosTalentoHumano> planillaIESSList;
    private short periodo;
    private CatalogoItem rolGeneral;
    private CatalogoItem estadoAprobacion;
    private TipoRol tipoRol;
    private CatalogoItem decimoItem;
    private CatalogoItem tipoNomina;
    private ParametrosTalentoHumano rubroSeleccionando;
    private ParametrosTalentoHumano planillaSeleccionada;

    private String tipo;
    private String reporte;
    private String tipoReporte;
    private String mesSeleccionado;
    private boolean tipoReport;
    private boolean reportRubro;
    private Short periodoIngresado;

    private final String DECIMOTERCERO = "DT3";
    private final String DECIMOCUARTO = "DT4";
    private final String FONDODERESERVA = "FND";
    private final String CONTABLEPARTIDA = "CP";

    private final String CATALOGODECIMOTERCER = "ACU-DECIMO-3ro";
    private final String CATALOGODECIMOCUARTO = "ACU-DECIMO-4to";
    private final String CATALOGOFONDORESERVA = "ACU-FONDOS-RESERVA";

    @PostConstruct
    public void init() {
        loadModel();
        planillaSeleccionada = new ParametrosTalentoHumano();
        planillaIESSList = parametroService.valoreRolesXplanillaIESS();
        decimoItem = new CatalogoItem();
        fondosReservas = new ArrayList<>();
        tipoNomina = new CatalogoItem();
        rubroSeleccionando = new ParametrosTalentoHumano();
        rolGeneral = catalogoItemService.getEstadoRol("rol_general");
        estadoAprobacion = catalogoItemService.findByNamedQuery1("CatalogoItem.findByCodigo", new Object[]{"aprobado-rol"});
        periodo = Utils.getAnio(new Date()).shortValue();
        rolesMensuales = tipoRolService.getTipoRolAprobado("aprobado-rol", "pagado-rol");
        tipoNominaList = catalogoItemService.findCatalogoItems("tipo_presupuesto");
        mesList = catalogoItemService.findCatalogoItems("meses_anio");
        periodoIngresado = periodo;
    }

    private void loadModel() {
        tipoRol = new TipoRol();
        tipo = "";
        reporte = "";
        tipoReporte = "";
    }

    public void generarReporte(String ext, boolean excel) {
        if (validar()) {
            if (tipo.equalsIgnoreCase("provisiones")) {
                switch (reporte) {
                    case DECIMOTERCERO:
                        if (tipoReporte.equalsIgnoreCase(CONTABLEPARTIDA)) {
                            decimoItem = catalogoItemService.getEstadoRol(CATALOGODECIMOTERCER);
                            fondosReservas = fondosReservaService.findAllFondosReservaByTipoAcumulacion(decimoItem);
                            isEmptyFondos();
                            imprimirReporte("DETALLADO CONTABLE - PARTIDA", "reporteProvisionesDecimoTercero" + ext, "reporteRubrosNomina", excel);
                        }
                        break;
                    case DECIMOCUARTO:
                        if (tipoReporte.equalsIgnoreCase(CONTABLEPARTIDA)) {
                            decimoItem = catalogoItemService.getEstadoRol(CATALOGODECIMOCUARTO);
                            fondosReservas = fondosReservaService.findAllFondosReservaByTipoAcumulacion(decimoItem);
                            isEmptyFondos();
                            imprimirReporte("DETALLADO CONTABLE - PARTIDA", "reporteProvisionesDecimoCuarto" + ext, "reporteRubrosNomina", excel);
                        }
                        break;
                    case FONDODERESERVA:
                        if (tipoReporte.equalsIgnoreCase(CONTABLEPARTIDA)) {
                            decimoItem = catalogoItemService.getEstadoRol(CATALOGOFONDORESERVA);
                            fondosReservas = fondosReservaService.findAllFondosReservaByTipoAcumulacion(decimoItem);
                            isEmptyFondos();
                            imprimirReporte("DETALLADO CONTABLE - PARTIDA", "reporteProvisionesFondoReserva" + ext, "reporteRubrosNomina", excel);
                        }
                        break;
                }
            } else {
                if (tipoNomina != null && (rubroSeleccionando != null)) {
                    serveltSession.addParametro("id_rubro", rubroSeleccionando.getId());
                    serveltSession.addParametro("id_rol", tipoRol.getId());
                    serveltSession.addParametro("id_clase_rubro", tipoNomina.getId());
                    if (excel) {
                        serveltSession.setContentType("xlsx");
                    }
                    if (tipoNomina.getCodigo().equals("I")) {
                        serveltSession.setNombreReporte("rubroIngreso" + ext);
                    } else {
                        serveltSession.setNombreReporte("rubroEgreso" + ext);
                    }
                    serveltSession.setNombreSubCarpeta("reporteRubrosNomina");
                    JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                }
                if (tipoNomina != null && rubroSeleccionando == null) {
                    serveltSession.addParametro("id_rol", tipoRol.getId());
                    serveltSession.addParametro("id_clase_rubro", tipoNomina.getId());
                    if (excel) {
                        serveltSession.setContentType("xlsx");
                    }
                    if (tipoNomina.getCodigo().equals("I")) {
                        serveltSession.setNombreReporte("rubroIngresoAll" + ext);
                    } else {
                        serveltSession.setNombreReporte("rubroEgresoAll" + ext);
                    }
                    serveltSession.setNombreSubCarpeta("reporteRubrosNomina");
                    JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
                }
            }
        }
    }

    public void generarReportePlanilla(String ext, boolean excel) {
        if (mesSeleccionado == null || "".equals(mesSeleccionado)) {
            JsfUtil.addWarningMessage("AVISO!!!", "Seleccione un mes");
            return;
        }
        if (periodoIngresado == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Ingrese un período");
            return;
        }
        serveltSession.addParametro("mes", mesSeleccionado);
        serveltSession.addParametro("periodo", periodoIngresado);
        if (planillaSeleccionada != null) {
            serveltSession.addParametro("id_rubro", planillaSeleccionada.getId());
            if (excel) {
                serveltSession.setContentType(ext);
            }
            if (planillaSeleccionada.getTipo().getCodigo().equals("FR")) {
                serveltSession.setNombreReporte("planillaFondos");
            } else {
                serveltSession.setNombreReporte("planillaIESS");
            }
        } else {
            if (excel) {
                serveltSession.setContentType(ext);
            }
            serveltSession.setNombreReporte("planillaAporteIESS");
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        Distributivo distributivoRev = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        servletSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        servletSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        servletSession.addParametro("ci_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_revisor", distributivoRev != null ? distributivoRev.getCargo().getNombreCargo() : "");
        serveltSession.setNombreSubCarpeta("reporteRubrosNomina");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        periodoIngresado = periodo;
        mesSeleccionado = null;
        PrimeFaces.current().ajax().update("formMaster");
    }

    public void renderdNomina() {
        if (tipo.equalsIgnoreCase("provisiones")) {
            tipoReport = true;
            reportRubro = false;
            tipoNomina = new CatalogoItem();
            rubroSeleccionando = new ParametrosTalentoHumano();
        } else {
            tipoReport = false;
            reportRubro = true;
            reporte = "";
            tipoReporte = "";
        }
    }

    public void llenarListaRubros() {
        if (tipoNomina != null) {
            valoresRubrosList = parametroService.valoreRolesXclasificacion(tipoRol, tipoNomina);
        }
    }

    public void isEmptyFondos() {
        if (fondosReservas.isEmpty()) {
            JsfUtil.addInformationMessage("", "No Existe información para el reporte");
            return;
        }
    }

    public void imprimirReporte(String tipoReporte, String nombreReporte, String nombreSubCarpeta, boolean excel) {
        servletSession.addParametro("tipo_acumulacion", decimoItem.getId());
        servletSession.addParametro("tipo_rol", tipoRol.getId());
        servletSession.addParametro("periodo", tipoRol.getAnio());
        servletSession.addParametro("valor_parametrizado", fondosReservas.get(0).getAcumulacionFondos().getValorParametrizado().getId());
        servletSession.addParametro("tipo_reporte", tipoReporte);
        servletSession.addParametro("rol", tipoRol.getDescripcion());
        if (excel) {
            serveltSession.setContentType("xlsx");
        }
        servletSession.setNombreReporte(nombreReporte);
        servletSession.setNombreSubCarpeta(nombreSubCarpeta);
        /*se guarda el tipo de reporte a generar*/
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public Boolean validar() {
        if (tipo.equalsIgnoreCase("provisiones")) {
            if (tipo.isEmpty()) {
                JsfUtil.addInformationMessage("Información", "Seleccione un tipo");
                return false;
            }
            if (reporte.isEmpty()) {
                JsfUtil.addInformationMessage("Información", "Seleccione un reporte");
                return false;
            }
            if (tipoReporte.isEmpty()) {
                JsfUtil.addInformationMessage("Información", "Seleccione un tipo de reporte");
                return false;
            }
            if (tipoRol == null) {
                JsfUtil.addInformationMessage("Información", "Seleccione un rol");
                return false;
            }
        } else {
            if (tipoRol == null) {
                JsfUtil.addInformationMessage("Información", "Seleccione un rol");
                return false;
            }
        }
        return true;
    }

    public void cancelarReporte() {
        tipoNomina = new CatalogoItem();
        rubroSeleccionando = new ParametrosTalentoHumano();
        loadModel();
    }

    public void cancelarReportePlanilla() {
        tipoRol = new TipoRol();
        planillaSeleccionada = new ParametrosTalentoHumano();
        periodoIngresado = periodo;
        mesSeleccionado = null;
        loadModel();
    }

    //<editor-fold defaultstate="collapsed" desc="Getters and Setters">
    public List<CatalogoItem> getMesList() {
        return mesList;
    }

    public void setMesList(List<CatalogoItem> mesList) {
        this.mesList = mesList;
    }

    public String getMesSeleccionado() {
        return mesSeleccionado;
    }

    public void setMesSeleccionado(String mesSeleccionado) {
        this.mesSeleccionado = mesSeleccionado;
    }

    public Short getPeriodoIngresado() {
        return periodoIngresado;
    }

    public void setPeriodoIngresado(Short periodoIngresado) {
        this.periodoIngresado = periodoIngresado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public List<ParametrosTalentoHumano> getPlanillaIESSList() {
        return planillaIESSList;
    }

    public void setPlanillaIESSList(List<ParametrosTalentoHumano> planillaIESSList) {
        this.planillaIESSList = planillaIESSList;
    }

    public ParametrosTalentoHumano getPlanillaSeleccionada() {
        return planillaSeleccionada;
    }

    public void setPlanillaSeleccionada(ParametrosTalentoHumano planillaSeleccionada) {
        this.planillaSeleccionada = planillaSeleccionada;
    }

    public boolean isReportRubro() {
        return reportRubro;
    }

    public void setReportRubro(boolean reportRubro) {
        this.reportRubro = reportRubro;
    }

    public List<ParametrosTalentoHumano> getValoresRubrosList() {
        return valoresRubrosList;
    }

    public void setValoresRubrosList(List<ParametrosTalentoHumano> valoresRubrosList) {
        this.valoresRubrosList = valoresRubrosList;
    }

    public ParametrosTalentoHumano getRubroSeleccionando() {
        return rubroSeleccionando;
    }

    public void setRubroSeleccionando(ParametrosTalentoHumano rubroSeleccionando) {
        this.rubroSeleccionando = rubroSeleccionando;
    }

    public boolean isTipoReport() {
        return tipoReport;
    }

    public void setTipoReport(boolean tipoReport) {
        this.tipoReport = tipoReport;
    }

    public String getReporte() {
        return reporte;
    }

    public void setReporte(String reporte) {
        this.reporte = reporte;
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public CatalogoItem getRolGeneral() {
        return rolGeneral;
    }

    public void setRolGeneral(CatalogoItem rolGeneral) {
        this.rolGeneral = rolGeneral;
    }

    public List<CatalogoItem> getTipoNominaList() {
        return tipoNominaList;
    }

    public void setTipoNominaList(List<CatalogoItem> tipoNominaList) {
        this.tipoNominaList = tipoNominaList;
    }

    public CatalogoItem getTipoNomina() {
        return tipoNomina;
    }

    public void setTipoNomina(CatalogoItem tipoNomina) {
        this.tipoNomina = tipoNomina;
    }

    public CatalogoItem getEstadoAprobacion() {
        return estadoAprobacion;
    }

    public void setEstadoAprobacion(CatalogoItem estadoAprobacion) {
        this.estadoAprobacion = estadoAprobacion;
    }
//</editor-fold>

}
