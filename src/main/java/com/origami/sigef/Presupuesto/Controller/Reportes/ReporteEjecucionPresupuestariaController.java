/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.Presupuesto.Controller.Reportes;

import com.origami.sigef.Presupuesto.Entity.ProformaIngreso;
import com.origami.sigef.Presupuesto.Service.EjecucionPresupuestariaService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.AsientosContables;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.EjecucionPresupuestaria;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.AsientosContablesService;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Sandra Arroba
 */
@Named(value = "reporteEjecucionPresupView")
@ViewScoped
public class ReporteEjecucionPresupuestariaController implements Serializable {

    private OpcionBusqueda busqueda;
    private EjecucionPresupuestaria ejecucionPresupuestaria;
    private AsientosContables asientosContables;

    private List<MasterCatalogo> periodos;
    private List<EjecucionPresupuestaria> listEjecucionPresupuestaria;

    private boolean catalogoSinafip;
    private Date fechadesde;
    private Date fechahasta;

    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private ServletSession servlet;
    @Inject
    private EjecucionPresupuestariaService ejecucionPresService;
    @Inject
    private AsientosContablesService asientosContablesService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;

    @PostConstruct
    public void initView() {
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CP"});
        limpiar();
        asignarRangoPeriodos();
    }

    public void asignarRangoPeriodos() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(busqueda.getAnio(), 0, 1);
        fechadesde = calendar.getTime();
        if (busqueda.getAnio().intValue() == Utils.getAnio(new Date()).intValue()) {
            fechahasta = new Date();
        } else {
            calendar.set(busqueda.getAnio(), 11, 31);
            fechahasta = calendar.getTime();
        }
    }

    private void limpiar() {
        busqueda = new OpcionBusqueda();
        ejecucionPresupuestaria = new EjecucionPresupuestaria();
        listEjecucionPresupuestaria = new ArrayList<>();
        catalogoSinafip = Boolean.TRUE;
        asignarRangoPeriodos();
    }

    public void imprimir(String isExcel) {
        BigDecimal reforma = BigDecimal.ZERO;
        BigDecimal codificado = BigDecimal.ZERO;
        BigDecimal diferencia = BigDecimal.ZERO;
        BigDecimal devengado = BigDecimal.ZERO;
        BigDecimal presupuestoInicial = BigDecimal.ZERO;
        String cuentaContable = "-";
        List<EjecucionPresupuestaria> listEjecucionPresupuestariaComp;
        listEjecucionPresupuestariaComp = ejecucionPresService.findAll();
        if (!listEjecucionPresupuestariaComp.isEmpty()) {
            for (EjecucionPresupuestaria items : listEjecucionPresupuestariaComp) {
                ejecucionPresService.remove(items);
            }
        }
//        listEjecucionPresupuestariaComp = new ArrayList<>();
        List<AsientosContables> listAsientosContables;
        long codigo = 4;
        listAsientosContables = ejecucionPresService.getEstadoSituacionF(codigo);
        int i = 0;
        for (AsientosContables asientoContableOfEP : listAsientosContables) {
            cuentaContable = "-";
            ejecucionPresupuestaria = new EjecucionPresupuestaria();
            Long nivel = 2L;
            PresCatalogoPresupuestario catalogoPresupuestoA = new PresCatalogoPresupuestario();
            System.out.println("asientoContableOfEP: " + asientoContableOfEP.getCodigo());
            catalogoPresupuestoA = ejecucionPresService.getCatalogoPresupuesto(asientoContableOfEP.getCodigo(), nivel.intValue());
            /*REFORMAS PARA SACAR EL CODIFICADO DE CADA CODIGO PRESUPUESTARIO CON SU DEVENGADO RESPECTIVO SACADO DE LAS TRANSACCIONES DE DIARIO GENERAL*/
            if (catalogoPresupuestoA != null) {
                if (catalogoPresupuestoA.getIngreso()) {
                    ProformaIngreso proformaIng = ejecucionPresService.getProformaIngreso(asientoContableOfEP.getCodigo(), nivel.intValue(), busqueda.getAnio());
                    System.out.println("Tipo: " + catalogoPresupuestoA.getIngreso());
                    System.out.println("PresupuestoInicial: " + catalogoPresupuestoA.getPresupuestoInicial());
                    System.out.println("PresupuestoCodificado: " + catalogoPresupuestoA.getPresupuestoCodificado());
                    System.out.println("Código: " + catalogoPresupuestoA.getCodigo());
                    presupuestoInicial = proformaIng.getPresupuestoInicial();
                    reforma = ejecucionPresService.getReformaByPartidaFechaDesdeHastaIngresos(busqueda.getAnio(), fechadesde, fechahasta, asientoContableOfEP.getCodigo(), "EP", "", "");
                    cuentaContable = ejecucionPresService.getCuentaContableByCatalogoPresupuesto(busqueda.getAnio(), asientoContableOfEP.getCodigo(), "113");
                    devengado = ejecucionPresService.getDevengadoByCuenta(busqueda.getAnio(), fechadesde, fechahasta, "113", asientoContableOfEP.getCodigo());
                    System.out.println("REFORMA_: " + reforma);
                } else {
                    BigDecimal reformaPAPP = ejecucionPresService.getReformaByPartidaFechaDesdeHastaPAPP(busqueda.getAnio(), fechadesde, fechahasta, asientoContableOfEP.getCodigo(), "EP", "", "", null);
                    BigDecimal reformaPD = ejecucionPresService.getReformaByPartidaFechaDesdeHastaPD(busqueda.getAnio(), fechadesde, fechahasta, asientoContableOfEP.getCodigo(), "EP", "", "", null);
                    BigDecimal reformaPDA = ejecucionPresService.getReformaByPartidaFechaDesdeHastaPDA(busqueda.getAnio(), fechadesde, fechahasta, asientoContableOfEP.getCodigo(), "EP", "", "");
                    BigDecimal reformaPDI = ejecucionPresService.getReformaByPartidaFechaDesdeHastaPDI(busqueda.getAnio(), fechadesde, fechahasta, asientoContableOfEP.getCodigo(), "EP", "", "", null);
                    System.out.println("reformaPAPP: " + reformaPAPP + " || reformaPD: " + reformaPD + " || reformaPDA: " + reformaPDA + " || reformaPDI: " + reformaPDI);
                    BigDecimal reformaSub = (reformaPAPP.add(reformaPD)).add(reformaPDA);
                    System.out.println("reformaSub: " + reformaSub);
                    reforma = reformaPDI.add(reformaSub);
                    System.out.println("Reforma: >> " + reforma);
                    presupuestoInicial = ejecucionPresService.getPresupuestoInicialEgresosByCatalogoPresupuesto(busqueda.getAnio(), "213", asientoContableOfEP.getCodigo());
                    cuentaContable = ejecucionPresService.getCuentaContableByCatalogoPresupuesto(busqueda.getAnio(), asientoContableOfEP.getCodigo(), "213");
                    devengado = ejecucionPresService.getDevengadoByCuenta(busqueda.getAnio(), fechadesde, fechahasta, "213", asientoContableOfEP.getCodigo());
                }
                System.out.println("Presupuesto Inicial: " + presupuestoInicial);
                System.out.println("REforma: " + reforma);
                codificado = presupuestoInicial.add(reforma);
                diferencia = codificado.subtract(devengado);
            } else {
                reforma = BigDecimal.ZERO;
                codificado = BigDecimal.ZERO;
                devengado = BigDecimal.ZERO;
                diferencia = BigDecimal.ZERO;
                cuentaContable = "-";
            }
            ejecucionPresupuestaria.setAsientoContable(asientoContableOfEP);
            ejecucionPresupuestaria.setPeriodo(busqueda.getAnio());
            if (catalogoPresupuestoA != null) {
                ejecucionPresupuestaria.setCatalogoPresupuesto(catalogoPresupuestoA);
            } else {
                ejecucionPresupuestaria.setCatalogoPresupuesto(null);
            }
            ejecucionPresupuestaria.setCuentaContable(cuentaContable);
            ejecucionPresupuestaria.setCodigo(asientoContableOfEP.getCodigo());
            ejecucionPresupuestaria.setCodificado(codificado);
            ejecucionPresupuestaria.setDiferencia(diferencia);
            ejecucionPresupuestaria.setDevengado(devengado);
            ejecucionPresupuestaria = ejecucionPresService.create(ejecucionPresupuestaria);
            listEjecucionPresupuestaria.add(ejecucionPresupuestaria);
//            }
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.presupuesto));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        servlet.addParametro("ci_presupuesto", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servlet.addParametro("nombre_presupuesto", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        servlet.addParametro("cargo_presupuesto", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        servlet.addParametro("ci_director", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servlet.addParametro("nombre_director", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        servlet.addParametro("cargo_director", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        servlet.addParametro("ic_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("IC", userSession.getNameUser(), true));
        servlet.addParametro("ic_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("IC", userSession.getNameUser(), false));
        servlet.addParametro("gc_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("EC", userSession.getNameUser(), true));
        servlet.addParametro("gc_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("EC", userSession.getNameUser(), false));
        servlet.addParametro("ica_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("ICA", userSession.getNameUser(), true));
        servlet.addParametro("ica_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("ICA", userSession.getNameUser(), false));
        servlet.addParametro("gp_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("GP", userSession.getNameUser(), true));
        servlet.addParametro("gp_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("GP", userSession.getNameUser(), false));
        servlet.addParametro("gi_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("GI", userSession.getNameUser(), true));
        servlet.addParametro("gi_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("GI", userSession.getNameUser(), false));
        servlet.addParametro("gcap_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("GCAP", userSession.getNameUser(), true));
        servlet.addParametro("gcap_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("GCAP", userSession.getNameUser(), false));
        servlet.addParametro("if_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("IF", userSession.getNameUser(), true));
        servlet.addParametro("if_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("IF", userSession.getNameUser(), false));
        servlet.addParametro("af_codificado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("AF", userSession.getNameUser(), true));
        servlet.addParametro("af_devengado", ejecucionPresService.getSumCodificadoOrDevengadoByCodigo("AF", userSession.getNameUser(), false));
//        sumatoria(listEjecucionPresupuestaria);
        servlet.setContentType(isExcel);
        if (isExcel.equalsIgnoreCase("xlsx")) {
            servlet.setOnePagePerSheet(false);
            servlet.setIsIgnorePaginator(true);
        }
        servlet.addParametro("fecha_desde", fechadesde);
        servlet.addParametro("fecha_hasta", fechahasta);
        servlet.addParametro("per", busqueda.getAnio());
        servlet.addParametro("ENTIDAD", userSession.getUsuario().getEmpresaId());
        servlet.setNombreReporte("reporteEjecucionPresupuestaria");
        servlet.setNombreSubCarpeta("reportesPresupuesto");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        limpiar();
        PrimeFaces.current().ajax().update("formMain");
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<EjecucionPresupuestaria> getListEjecucionPresupuestaria() {
        return listEjecucionPresupuestaria;
    }

    public void setListEjecucionPresupuestaria(List<EjecucionPresupuestaria> listEjecucionPresupuestaria) {
        this.listEjecucionPresupuestaria = listEjecucionPresupuestaria;
    }

    public Date getFechadesde() {
        return fechadesde;
    }

    public void setFechadesde(Date fechadesde) {
        this.fechadesde = fechadesde;
    }

    public Date getFechahasta() {
        return fechahasta;
    }

    public void setFechahasta(Date fechahasta) {
        this.fechahasta = fechahasta;
    }

    public boolean isCatalogoSinafip() {
        return catalogoSinafip;
    }

    public void setCatalogoSinafip(boolean catalogoSinafip) {
        this.catalogoSinafip = catalogoSinafip;
    }

    public EjecucionPresupuestaria getEjecucionPresupuestaria() {
        return ejecucionPresupuestaria;
    }

    public void setEjecucionPresupuestaria(EjecucionPresupuestaria ejecucionPresupuestaria) {
        this.ejecucionPresupuestaria = ejecucionPresupuestaria;
    }

    public AsientosContables getAsientosContables() {
        return asientosContables;
    }

    public void setAsientosContables(AsientosContables asientosContables) {
        this.asientosContables = asientosContables;
    }

}
