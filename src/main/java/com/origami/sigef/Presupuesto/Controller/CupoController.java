/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller;

import com.origami.sigef.Presupuesto.Entity.Cupos;
import com.origami.sigef.Presupuesto.Entity.ReformaIngresoSuplemento;
import com.origami.sigef.Presupuesto.Service.CuposService;
import com.origami.sigef.Presupuesto.Service.ReformaSuplementoIngresoService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoPresupuesto;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.CupoPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.FuenteFinanciamiento;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PlanAnualPoliticaPublica;
import com.origami.sigef.common.entities.PlanAnualProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalObjetivo;
import com.origami.sigef.common.entities.PlanLocalPolitica;
import com.origami.sigef.common.entities.PlanLocalProgramaProyecto;
import com.origami.sigef.common.entities.PlanLocalSistema;
import com.origami.sigef.common.entities.PlanNacionalEje;
import com.origami.sigef.common.entities.PlanNacionalObjetivo;
import com.origami.sigef.common.entities.PlanNacionalPolitica;
import com.origami.sigef.common.entities.PlanProgramatico;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProformaPresupuestoPlanificado;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.view.VistaGeneralPlanAnual;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.lazy.PlanAnualPoliticaPublicaLazy;
import com.origami.sigef.contabilidad.lazy.PlanAnualProgramaProyectoLazy;
import com.origami.sigef.contabilidad.lazy.ProductoLazy;
import com.origami.sigef.contabilidad.model.ProformaDTO;
import com.origami.sigef.contabilidad.model.ProformaPDTO;
import com.origami.sigef.contabilidad.model.ReporteDeActividades;
import com.origami.sigef.contabilidad.service.ActividadOperativaService;
import com.origami.sigef.contabilidad.service.CatalogoPresupuestoService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;

import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
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
 * @author ORIGAMIEC
 */
@Named(value = "cupoView")
@ViewScoped
public class CupoController extends BpmnBaseRoot implements Serializable {

//<editor-fold defaultstate="collapsed" desc="VARIBALES E INJECT">
    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession userSession;
    @Inject
    private CuposService cupoService;
    @Inject
    private UnidadAdministrativaService unidadService;
    @Inject
    private CupoPresupuestoService cupoPresupuestoService;

    private Long idUnidad;
    private List<Cupos> listaCupos;
    /*Objetos*/

    private Cupos cupo;
    /*Variables Globales*/
    private String programaProyectoLocal;
    private String productoGlobal;
    private int indiceDeAcordian;
    private Short anioProforma;
    private double valorTotalPresupuestoEgresos;
    private BigDecimal totalActividades;
    private BigDecimal totalCupo;
    private BigDecimal totalSaldoSuplemento;

    private List<Cupos> listaCuposAsignacion;

    private boolean bloqueo;

    private boolean registranuevo;
    private boolean consultarPapp;
    private boolean consultaRe;
    private LazyModel<Cupos> lazyOtrosReforma;
    private ReformaIngresoSuplemento reformaSuplemento;
    private LazyModel<Cupos> lazyCupos;
    private Distributivo unidadGeneral;
//</editor-fold>
    @Inject
    private ReformaSuplementoIngresoService suplementoIngresoService;
    @Inject
    ClienteService clienteService;
    private List<Cupos> listaReformas;

    private LazyModel<ReformaIngresoSuplemento> lazyAsignacion;

    private BigDecimal totalSuplementoIngreso;
    private BigDecimal cupoUnidadValor;
    private CatalogoItem estadoMostrar;
    private List<CatalogoItem> estadoFiltros;
    private String observaciones;
    private BigDecimal valorTotal, valorAsignado;
    List<Cupos> verificacionUser;

    @PostConstruct
    public void init() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                consultarPapp = false;
                listaCupos = new ArrayList<>();
                registranuevo = false;
                consultaRe = true;
                cupo = new Cupos();
                this.listaReformas = new ArrayList<>();

                unidadGeneral = new Distributivo();

                this.totalActividades = BigDecimal.ZERO;
                this.cupo = new Cupos();
                /*List*/

                bloqueo = false;

                consultarPapp = false;
                registranuevo = false;
                consultaRe = true;
                totalCupo = BigDecimal.ZERO;
                /*Variables Globales*/
                setIndiceDeAcordian(-1);

                lazyAsignacion = new LazyModel(ReformaIngresoSuplemento.class);
                lazyAsignacion.getFilterss().put("numTramite:equal", BigInteger.valueOf(tramite.getNumTramite()));
                verificacionUser = new ArrayList<>();

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void showaDatable() {
        consultarPapp = true;
        registranuevo = false;
        consultaRe = false;
    }

    public List<String> filtrarLista(Cupos c) {
        return cupoPresupuestoService.getListaRoles(c.getUnidadAdministrativa());

    }

    public void asignacionCupo(ReformaIngresoSuplemento r) {

        List<Cupos> lista = cupoService.getVerificacionCupos(r);
        listaCuposAsignacion = new ArrayList<>();
        reformaSuplemento = r;
        if (lista.isEmpty()) {

            this.reformaSuplemento = new ReformaIngresoSuplemento();
            this.reformaSuplemento = r;

            List<UnidadAdministrativa> unidades = unidadService.findByNamedQuery("UnidadAdministrativa.findByEstado");

            for (UnidadAdministrativa data : unidades) {
                this.cupo = new Cupos();
                this.cupo.setReforma(reformaSuplemento);
                this.cupo.setUnidadAdministrativa(data);
                this.cupo.setMontoCupo(BigDecimal.ZERO);
                cupo = cupoService.create(cupo);
            }

            this.cupo = new Cupos();
            this.cupo.setReforma(reformaSuplemento);
            this.cupo.setMontoCupo(BigDecimal.ZERO);
            this.cupo.setOtros("D");
            cupo = cupoService.create(cupo);
            this.cupo = new Cupos();
            this.cupo.setReforma(reformaSuplemento);
            this.cupo.setMontoCupo(BigDecimal.ZERO);
            this.cupo.setOtros("DA");
            cupo = cupoService.create(cupo);
            this.cupo = new Cupos();
            this.cupo.setReforma(reformaSuplemento);
            this.cupo.setMontoCupo(BigDecimal.ZERO);
            this.cupo.setOtros("PD");
            cupo = cupoService.create(cupo);

            this.cupo = new Cupos();
        }

        lazyCupos = new LazyModel(Cupos.class);
        lazyCupos.getFilterss().put("reforma:equal", r);
        lazyCupos.getFilterss().put("otros:equal", null);
        lazyOtrosReforma = new LazyModel(Cupos.class);
        lazyOtrosReforma.getFilterss().put("reforma:equal", r);
        lazyOtrosReforma.getFilterss().put("unidadAdministrativa:equal", null);
        listaCupos.clear();
        listaCuposAsignacion = cupoService.getVerificacionCupos(r);
        getRetornaTotal();
        this.cupo = new Cupos();
        PrimeFaces.current().executeScript("PF('DloCupos').show()");
        PrimeFaces.current().ajax().update(":formCupos");

    }

    public void editarCell(Cupos c) {
        BigDecimal valor = cupoService.getSumaVerificacion(reformaSuplemento).add(c.getMontoCupo());
        BigDecimal valorActual = cupoService.getCupoActual(c);
        BigDecimal suplementoValor = getRetornaTotal(reformaSuplemento);

        if (valor.subtract(valorActual).doubleValue() > suplementoValor.doubleValue()) {
            c.setMontoCupo(valorActual);
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("Advertencia", "No puede asignarle ese valor");

        } else {

            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addInformationMessage("Exitoso", "Cupo agregado Correctamente");
        }
        cupoService.edit(c);
        getRetornaTotal();
    }

    public void showReformas() {
        consultarPapp = false;
        registranuevo = false;
        consultaRe = true;
        totalCupo = BigDecimal.ZERO;
    }

    public void editarResponsable(Cupos c) {
        if (c.getResponsable() == null) {
            c.setResponsable(null);
            c.setUsuario(null);
        } else {
            c.setUsuario(cupoPresupuestoService.getRolesUsuariosNameUser(c.getUnidadAdministrativa(), c.getResponsable()));
            if (c.getUsuario() == null) {
                c.setResponsable(null);
                JsfUtil.addErrorMessage("ERROR!!!", "No tiene un usuario relacionado");
                return;
            }
            if (c.getUsuario().toUpperCase().equals("ADMIN")) {
                c.setUsuario(null);
                c.setResponsable(null);
                JsfUtil.addWarningMessage("AVISO!!!", "No se puede seleccionar este tipo de responsable");
            }
        }
        cupoService.edit(c);
        PrimeFaces.current().ajax().update("messages");
        if (c.getResponsable() != null) {
            JsfUtil.addInformationMessage("Información", "Editado con éxito");
        }
//        if (c.getResponsable() == null || c.getResponsable().length() == 0) {
//            c.setUsuario(null);
//            c.setResponsable(null);
//        } else {
//            c.setUsuario(cupoPresupuestoService.getRolesUsuariosNameUser(c.getUnidadAdministrativa(), c.getResponsable()));
//        }
//        cupoService.edit(c);
//        PrimeFaces.current().ajax().update("messages");
//        JsfUtil.addInformationMessage("Información", "Editado con éxito");
    }

    public void abriDlogo(ReformaIngresoSuplemento r) {
        List<Cupos> listaVerificadora = cupoService.getVerificadorCupos(r);
        boolean bandera = false;
        if (listaVerificadora.isEmpty()) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "NO PUEDE ENVIAR LA TAREA SI NO HAY CUPOS");
            return;

        }

        for (Cupos cupos : listaVerificadora) {
            if (cupos.getMontoCupo().doubleValue() > 0) {
                if (cupos.getResponsable() == null) {
                    bandera = true;
                    break;
                }

            }
        }

        if (bandera) {
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addErrorMessage("ERROR", "HAY CUPOS EN UNIDADES PERO NO HA ASIGNADO UN RESPOSABLES ...!");
            return;
        }

        verificacionUser = new ArrayList<>();
        List<Cupos> getlista = cupoService.getRevisionAsignacion(r);
        verificacionUser = getlista;

        if (!getlista.isEmpty()) {
            PrimeFaces.current().executeScript("PF('dlgresponsables').show()");
            PrimeFaces.current().ajax().update(":frmDlgresponsables");
            PrimeFaces.current().ajax().update("messages");
            JsfUtil.addWarningMessage("AVISO", "Verifique la asignaciòn a responsabeles ya que hay usuario que se le repsonsabiliza en varias unidades");
            return;
        }

        reformaSuplemento = new ReformaIngresoSuplemento();
        reformaSuplemento = r;

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea() {
        try {
            observacion.setObservacion(observaciones);
            //clienteService.getUnidadUserCodigo("JA", "6")
            getParamts().put("asistente_financiero", clienteService.getrolsUser(RolUsuario.financiero));
            getParamts().put("formReceptaTramite", "/proceso/recepcionReforma");
            if (saveTramite() == null) {
                return;
            }

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            reformaSuplemento = new ReformaIngresoSuplemento();
            PrimeFaces.current().ajax().update(":frmDlgObser");
            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public List<Cupos> getVerificacionUser() {
        return verificacionUser;
    }

    public void setVerificacionUser(List<Cupos> verificacionUser) {
        this.verificacionUser = verificacionUser;
    }

    public List<Cupos> getListaCuposAsignacion() {
        return listaCuposAsignacion;
    }

    public BigDecimal getRetornaTotal(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalSuplemento(r);
    }

    public void getRetornaTotal() {

        valorTotal = suplementoIngresoService.getTotalSuplemento(reformaSuplemento);
        valorAsignado = suplementoIngresoService.getTotalSuplementoAsignado(reformaSuplemento);
    }

    public List<CatalogoItem> getEstadoFiltros() {
        return estadoFiltros;
    }

    public void setEstadoFiltros(List<CatalogoItem> estadoFiltros) {
        this.estadoFiltros = estadoFiltros;
    }

    public BigDecimal getRetornaTotalReduccion(ReformaIngresoSuplemento r) {
        return suplementoIngresoService.getTotalReduccionReforma(r);
    }

    public void setListaCuposAsignacion(List<Cupos> listaCuposAsignacion) {
        this.listaCuposAsignacion = listaCuposAsignacion;
    }

    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public BigDecimal getTotalSuplementoIngreso() {
        return totalSuplementoIngreso;
    }

    public void setTotalSuplementoIngreso(BigDecimal totalSuplementoIngreso) {
        this.totalSuplementoIngreso = totalSuplementoIngreso;
    }

    public LazyModel<Cupos> getLazyOtrosReforma() {
        return lazyOtrosReforma;
    }

    public void setLazyOtrosReforma(LazyModel<Cupos> lazyOtrosReforma) {
        this.lazyOtrosReforma = lazyOtrosReforma;
    }

    public BigDecimal getTotalSaldoSuplemento() {
        return totalSaldoSuplemento;
    }

    public void setTotalSaldoSuplemento(BigDecimal totalSaldoSuplemento) {
        this.totalSaldoSuplemento = totalSaldoSuplemento;
    }

    public LazyModel<ReformaIngresoSuplemento> getLazyAsignacion() {
        return lazyAsignacion;
    }

    public void setLazyAsignacion(LazyModel<ReformaIngresoSuplemento> lazyAsignacion) {
        this.lazyAsignacion = lazyAsignacion;
    }

    public ReformaIngresoSuplemento getReformaSuplemento() {
        return reformaSuplemento;
    }

    public void setReformaSuplemento(ReformaIngresoSuplemento reformaSuplemento) {
        this.reformaSuplemento = reformaSuplemento;
    }

    public Distributivo getUnidadGeneral() {
        return unidadGeneral;
    }

    public void setUnidadGeneral(Distributivo unidadGeneral) {
        this.unidadGeneral = unidadGeneral;
    }

    public List<Cupos> getListaReformas() {
        return listaReformas;
    }

    public void setListaReformas(List<Cupos> listaReformas) {
        this.listaReformas = listaReformas;
    }

    public BigDecimal getCupoUnidadValor() {
        return cupoUnidadValor;
    }

    public void setCupoUnidadValor(BigDecimal cupoUnidadValor) {
        this.cupoUnidadValor = cupoUnidadValor;
    }

    public List<Cupos> getListaCupos() {
        return listaCupos;
    }

    public void setListaCupos(List<Cupos> listaCupos) {
        this.listaCupos = listaCupos;
    }

    public Cupos getCupo() {
        return cupo;
    }

    public void setCupo(Cupos cupo) {
        this.cupo = cupo;
    }

    public LazyModel<Cupos> getLazyCupos() {
        return lazyCupos;
    }

    public void setLazyCupos(LazyModel<Cupos> lazyCupos) {
        this.lazyCupos = lazyCupos;
    }

    public BigDecimal getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(BigDecimal valorTotal) {
        this.valorTotal = valorTotal;
    }

    public BigDecimal getValorAsignado() {
        return valorAsignado;
    }

    public void setValorAsignado(BigDecimal valorAsignado) {
        this.valorAsignado = valorAsignado;
    }

    public boolean isConsultaRe() {
        return consultaRe;
    }

    public void setConsultaRe(boolean consultaRe) {
        this.consultaRe = consultaRe;
    }

    public boolean isRegistranuevo() {
        return registranuevo;
    }

    public void setRegistranuevo(boolean registranuevo) {
        this.registranuevo = registranuevo;
    }

    public boolean isConsultarPapp() {
        return consultarPapp;
    }

    public void setConsultarPapp(boolean consultarPapp) {
        this.consultarPapp = consultarPapp;
    }

    public Long getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(Long idUnidad) {
        this.idUnidad = idUnidad;
    }

    public BigDecimal getTotalActividades() {
        return totalActividades;
    }

    public void setTotalActividades(BigDecimal totalActividades) {
        this.totalActividades = totalActividades;
    }

    public String getProgramaProyectoLocal() {
        return programaProyectoLocal;
    }

    public void setProgramaProyectoLocal(String programaProyectoLocal) {
        this.programaProyectoLocal = programaProyectoLocal;
    }

    public String getProductoGlobal() {
        return productoGlobal;
    }

    public void setProductoGlobal(String productoGlobal) {
        this.productoGlobal = productoGlobal;
    }

    public ReformaSuplementoIngresoService getSuplementoIngresoService() {
        return suplementoIngresoService;
    }

    public void setSuplementoIngresoService(ReformaSuplementoIngresoService suplementoIngresoService) {
        this.suplementoIngresoService = suplementoIngresoService;
    }

    public Short getAnioProforma() {
        return anioProforma;
    }

    public void setAnioProforma(Short anioProforma) {
        this.anioProforma = anioProforma;
    }

    public double getValorTotalPresupuestoEgresos() {
        return valorTotalPresupuestoEgresos;
    }

    public void setValorTotalPresupuestoEgresos(double valorTotalPresupuestoEgresos) {
        this.valorTotalPresupuestoEgresos = valorTotalPresupuestoEgresos;
    }

    public boolean isBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(boolean bloqueo) {
        this.bloqueo = bloqueo;
    }

    public int getIndiceDeAcordian() {
        return indiceDeAcordian;
    }

    public void setIndiceDeAcordian(int indiceDeAcordian) {
        this.indiceDeAcordian = indiceDeAcordian;
    }

    public CatalogoItem getEstadoMostrar() {
        return estadoMostrar;
    }

    public void setEstadoMostrar(CatalogoItem estadoMostrar) {
        this.estadoMostrar = estadoMostrar;
    }

//</editor-fold>
}
