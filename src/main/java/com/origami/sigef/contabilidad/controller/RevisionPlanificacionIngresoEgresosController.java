/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProgramacionIngresoEgreso;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.models.Correo;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.KatalinaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.ProgramacionIngresoEgresoService;
import com.origami.sigef.resource.presupuesto.entities.PresCatalogoPresupuestario;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
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
@Named(value = "revisionPlanIEView")
@ViewScoped
public class RevisionPlanificacionIngresoEgresosController extends BpmnBaseRoot implements Serializable {

    @Inject
    private KatalinaService katalinaService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession user;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ProgramacionIngresoEgresoService service;
    private CatalogoItem estadoAprobado;
    private CatalogoItem estadoRechazado;
    private LazyModel<ProgramacionIngresoEgreso> lazyIngresos;
    private LazyModel<ProgramacionIngresoEgreso> lazyEgresos;
    private CatalogoItem estadoGeneral;
    private OpcionBusqueda busqueda;
    private List<CatalogoProformaPresupuesto> periodos;
    private BigDecimal meses;
    private Double totalItem;
    private Double totalPIM;
    private Double totalEnero;
    private Double totalFebrero;
    private Double totalMarzo;
    private Double totalAbril;
    private Double totalMayo;
    private Double totalJunio;
    private Double totalJulio;
    private Double totalAgosto;
    private Double totalSeptiembre;
    private Double totalOctubre;
    private Double totalNoviembre;
    private Double totalDiciembre;
    private BigDecimal meses2;
    private Double totalCuatrimestre1;
    private Double totalCuatrimestre2;
    private Double totalCuatrimestre3;
    private Double totalMonto;
    private Double totalPem;
    private List<Producto> productosWithActividades;
    private final BigDecimal porcentaje = new BigDecimal(100);
    private double auxValor1;
    private double auxValor2;
    private double auxValor3;
    private double auxTotal1;
    private double auxTotal2;
    private double auxTotal3;
    double acuCuatrimestre1;
    double acuCuatrimestre2;
    double acuCuatrimestre3;
    private boolean btnAprobar, btnRechazar;
    private String observaciones;

    @Inject
    private ManagerService services;

    @PostConstruct
    public void inicializar() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                busqueda = new OpcionBusqueda();
                lazyIngresos = new LazyModel(ProgramacionIngresoEgreso.class);

                lazyIngresos.getFilterss().put("tipoFlujo:equal", true);
                lazyIngresos.getFilterss().put("periodo:equal", busqueda.getAnio());

                lazyEgresos = new LazyModel(ProgramacionIngresoEgreso.class);

                lazyEgresos.getFilterss().put("tipoFlujo:equal", false);
                lazyEgresos.getFilterss().put("periodo:equal", busqueda.getAnio());

                estadoAprobado = new CatalogoItem();
                estadoAprobado = catalogoService.getItemByCatalogoAndCodigo("estado_pie", "PRO");
                estadoRechazado = new CatalogoItem();
                estadoRechazado = catalogoService.getItemByCatalogoAndCodigo("estado_pie", "RE");
                estadoGeneral = new CatalogoItem();
                estadoGeneral = service.getestado(busqueda.getAnio());
                periodos = service.getProformaIngresoEgreso(Boolean.TRUE);

                actualizarTotalMeses();

                totalCuatrimestre1 = service.getSumaCuatrimestre1(busqueda.getAnio(), Boolean.FALSE).doubleValue();
                totalCuatrimestre2 = service.getSumaCuatrimestre2(busqueda.getAnio(), Boolean.FALSE).doubleValue();
                totalCuatrimestre3 = service.getSumaCuatrimestre3(busqueda.getAnio(), Boolean.FALSE).doubleValue();
                totalMonto = service.getTotalCatalogoProforma(busqueda.getAnio(), Boolean.FALSE).doubleValue();
                totalPem = service.getTotalPIE(busqueda.getAnio(), false).doubleValue();
                btnAprobar = false;
                btnRechazar = false;

            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void actualizarCuatrimestres() {
        totalEnero = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 1).doubleValue();
        totalFebrero = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 2).doubleValue();
        totalMarzo = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 3).doubleValue();
        totalAbril = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 4).doubleValue();
        totalMayo = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 5).doubleValue();
        totalJunio = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 6).doubleValue();
        totalJulio = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 7).doubleValue();
        totalAgosto = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 8).doubleValue();
        totalSeptiembre = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 9).doubleValue();
        totalOctubre = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 10).doubleValue();
        totalNoviembre = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 11).doubleValue();
        totalDiciembre = service.getSumaMesses(busqueda.getAnio(), Boolean.FALSE, 12).doubleValue();
        totalPem = service.getTotalPIE(busqueda.getAnio(), false).doubleValue();
    }

    public List<Producto> viewActividadByProducto(ProgramacionIngresoEgreso p) {
        productosWithActividades = service.getProductoWithAct(p.getCodigoItem(), p.getPeriodo());
        return productosWithActividades;
    }

    public BigDecimal presupuestoInical(Short periodo, PresCatalogoPresupuestario item) {
        return services.getValorPresupuestoInicialPim(periodo, item);
    }

    public String getPartidaIngreso(Short periodo, PresCatalogoPresupuestario item) {
        return services.getPartidaIngreso(periodo, item);
    }

    public void actualizar() {
        lazyIngresos = new LazyModel(ProgramacionIngresoEgreso.class);

        lazyIngresos.getFilterss().put("tipoFlujo:equal", true);
        lazyIngresos.getFilterss().put("periodo:equal", busqueda.getAnio());

        lazyEgresos = new LazyModel(ProgramacionIngresoEgreso.class);

        lazyEgresos.getFilterss().put("tipoFlujo:equal", false);
        lazyEgresos.getFilterss().put("periodo:equal", busqueda.getAnio());
        estadoGeneral = new CatalogoItem();
        estadoGeneral = service.getestado(busqueda.getAnio());

        actualizarTotalMeses();
        actualizarCuatrimestres();

    }

    public void actualizarTotalMeses() {
        if (busqueda.getAnio() == null) {
            return;
        }
        totalEnero = service.getSumaEnero("enero", busqueda.getAnio()).doubleValue();
        totalFebrero = service.getSumaEnero("febrero", busqueda.getAnio()).doubleValue();
        totalMarzo = service.getSumaEnero("marzo", busqueda.getAnio()).doubleValue();
        totalAbril = service.getSumaEnero("abril", busqueda.getAnio()).doubleValue();
        totalMayo = service.getSumaEnero("mayo", busqueda.getAnio()).doubleValue();
        totalJunio = service.getSumaEnero("junio", busqueda.getAnio()).doubleValue();
        totalJulio = service.getSumaEnero("julio", busqueda.getAnio()).doubleValue();
        totalAgosto = service.getSumaEnero("agosto", busqueda.getAnio()).doubleValue();
        totalSeptiembre = service.getSumaEnero("septiembre", busqueda.getAnio()).doubleValue();
        totalOctubre = service.getSumaEnero("octubre", busqueda.getAnio()).doubleValue();
        totalNoviembre = service.getSumaEnero("noviembre", busqueda.getAnio()).doubleValue();
        totalDiciembre = service.getSumaEnero("diciembre", busqueda.getAnio()).doubleValue();
        totalPIM = service.getTotalPIE(busqueda.getAnio(), true).doubleValue();
        totalItem = service.getTotalCatalogoProforma(busqueda.getAnio(), Boolean.TRUE).doubleValue();
    }

    public void resetearValoresPim() {

        totalEnero = 0.00;
        totalFebrero = 0.00;
        totalMarzo = 0.00;
        totalAbril = 0.00;
        totalMayo = 0.00;
        totalJunio = 0.00;
        totalJulio = 0.00;
        totalAgosto = 0.00;
        totalSeptiembre = 0.00;
        totalOctubre = 0.00;
        totalNoviembre = 0.00;
        totalDiciembre = 0.00;
        totalPIM = 0.00;
        totalItem = 0.00;
    }

    public void aprobarSolicitud(boolean a) {

        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        if (a) {
            btnAprobar = true;
            btnRechazar = false;
        } else {
            btnAprobar = false;
            btnRechazar = true;
        }

        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");

    }

    public void completarTarea(int a) {
        try {
            observacion.setObservacion(observaciones);
            if (a == 1) {
                service.updateEstadoProgramacion(busqueda, estadoAprobado);
//                Cliente clienteTesorero = clienteService.getClienteEspecificos("5");
//                Cliente maximaAutoridad = clienteService.getClienteEspecificos("7");
                Distributivo clienteTesorero = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.tesorero));
                Distributivo maximaAutoridad = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.maximaAutoridad));
                if (clienteTesorero != null) {
                    enviarCorreo(clienteTesorero.getServidorPublico().getPersona().getEmail(), tramite.getTipoTramite().getDescripcion().toUpperCase(), clienteTesorero.getServidorPublico().getPersona().getNombreCompleto());
                }
                if (maximaAutoridad != null) {
                    enviarCorreo(maximaAutoridad.getServidorPublico().getPersona().getEmail(), tramite.getTipoTramite().getDescripcion().toUpperCase(), maximaAutoridad.getServidorPublico().getPersona().getNombreCompleto());
                }
            } else {
                service.updateEstadoProgramacion(busqueda, estadoRechazado);

            }
            getParamts().put("aprobado", a);

            JsfUtil.executeJS("PF('dlgObservaciones').hide()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

            if (saveTramite() == null) {
                return;
            }

            this.session.setVarTemp(null);
            super.completeTask((HashMap<String, Object>) getParamts());

            JsfUtil.redirectFaces("/procesos/bandeja-tareas");

        } catch (Exception e) {
            LOG.log(Level.SEVERE, "completas tareas", e);
        }

    }

    public void enviarCorreo(String email, String asunto, String userStart) {

        Correo c = new Correo();
        c.setDestinatario(email);
        c.setAsunto(asunto);
        c.setMensaje("<html lang=\"es\">\n"
                + "<head>\n"
                + "<meta charset=\"utf-8\"/>\n"
                + "</head>\n"
                + "<body>\n"
                + "<p style=\"font-size:12px;\">SR(a). " + userStart.toUpperCase() + " POR MEDIO DE LA PRESENTE SE LE INFORMA  QUE SE HA APROBADO LA PROGRAMACIÓN DEL PRESUPUESTO "
                + " SEGÚN EL NUMERO DE TRÁMITE N° " + tramite.getNumTramite() + " </p>"
                + "</body>\n"
                + "</html>");
        katalinaService.enviarCorreo(c);
        JsfUtil.addSuccessMessage("Correo", "La notificacion fue enviada con exito a la direccion de email: " + email + " relacionada con: " + userStart);

    }

//<editor-fold defaultstate="collapsed" desc="setter and getter">
    public boolean isBtnAprobar() {
        return btnAprobar;
    }

    public void setBtnAprobar(boolean btnAprobar) {
        this.btnAprobar = btnAprobar;
    }

    public boolean isBtnRechazar() {
        return btnRechazar;
    }

    public void setBtnRechazar(boolean btnRechazar) {
        this.btnRechazar = btnRechazar;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getMeses2() {
        return meses2;
    }

    public void setMeses2(BigDecimal meses2) {
        this.meses2 = meses2;
    }

    public Double getTotalCuatrimestre1() {
        return totalCuatrimestre1;
    }

    public void setTotalCuatrimestre1(Double totalCuatrimestre1) {
        this.totalCuatrimestre1 = totalCuatrimestre1;
    }

    public Double getTotalCuatrimestre2() {
        return totalCuatrimestre2;
    }

    public void setTotalCuatrimestre2(Double totalCuatrimestre2) {
        this.totalCuatrimestre2 = totalCuatrimestre2;
    }

    public Double getTotalCuatrimestre3() {
        return totalCuatrimestre3;
    }

    public void setTotalCuatrimestre3(Double totalCuatrimestre3) {
        this.totalCuatrimestre3 = totalCuatrimestre3;
    }

    public Double getTotalMonto() {
        return totalMonto;
    }

    public void setTotalMonto(Double totalMonto) {
        this.totalMonto = totalMonto;
    }

    public Double getTotalPem() {
        return totalPem;
    }

    public void setTotalPem(Double totalPem) {
        this.totalPem = totalPem;
    }

    public List<Producto> getProductosWithActividades() {
        return productosWithActividades;
    }

    public void setProductosWithActividades(List<Producto> productosWithActividades) {
        this.productosWithActividades = productosWithActividades;
    }

    public double getAuxValor1() {
        return auxValor1;
    }

    public void setAuxValor1(double auxValor1) {
        this.auxValor1 = auxValor1;
    }

    public double getAuxValor2() {
        return auxValor2;
    }

    public void setAuxValor2(double auxValor2) {
        this.auxValor2 = auxValor2;
    }

    public double getAuxValor3() {
        return auxValor3;
    }

    public void setAuxValor3(double auxValor3) {
        this.auxValor3 = auxValor3;
    }

    public double getAuxTotal1() {
        return auxTotal1;
    }

    public void setAuxTotal1(double auxTotal1) {
        this.auxTotal1 = auxTotal1;
    }

    public double getAuxTotal2() {
        return auxTotal2;
    }

    public void setAuxTotal2(double auxTotal2) {
        this.auxTotal2 = auxTotal2;
    }

    public double getAuxTotal3() {
        return auxTotal3;
    }

    public void setAuxTotal3(double auxTotal3) {
        this.auxTotal3 = auxTotal3;
    }

    public double getAcuCuatrimestre1() {
        return acuCuatrimestre1;
    }

    public void setAcuCuatrimestre1(double acuCuatrimestre1) {
        this.acuCuatrimestre1 = acuCuatrimestre1;
    }

    public double getAcuCuatrimestre2() {
        return acuCuatrimestre2;
    }

    public void setAcuCuatrimestre2(double acuCuatrimestre2) {
        this.acuCuatrimestre2 = acuCuatrimestre2;
    }

    public double getAcuCuatrimestre3() {
        return acuCuatrimestre3;
    }

    public void setAcuCuatrimestre3(double acuCuatrimestre3) {
        this.acuCuatrimestre3 = acuCuatrimestre3;
    }

    public CatalogoItem getEstadoGeneral() {
        return estadoGeneral;
    }

    public void setEstadoGeneral(CatalogoItem estadoGeneral) {
        this.estadoGeneral = estadoGeneral;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<CatalogoProformaPresupuesto> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<CatalogoProformaPresupuesto> periodos) {
        this.periodos = periodos;
    }

    public BigDecimal getMeses() {
        return meses;
    }

    public void setMeses(BigDecimal meses) {
        this.meses = meses;
    }

    public Double getTotalItem() {
        return totalItem;
    }

    public void setTotalItem(Double totalItem) {
        this.totalItem = totalItem;
    }

    public Double getTotalPIM() {
        return totalPIM;
    }

    public void setTotalPIM(Double totalPIM) {
        this.totalPIM = totalPIM;
    }

    public Double getTotalEnero() {
        return totalEnero;
    }

    public void setTotalEnero(Double totalEnero) {
        this.totalEnero = totalEnero;
    }

    public Double getTotalFebrero() {
        return totalFebrero;
    }

    public void setTotalFebrero(Double totalFebrero) {
        this.totalFebrero = totalFebrero;
    }

    public Double getTotalMarzo() {
        return totalMarzo;
    }

    public void setTotalMarzo(Double totalMarzo) {
        this.totalMarzo = totalMarzo;
    }

    public Double getTotalAbril() {
        return totalAbril;
    }

    public void setTotalAbril(Double totalAbril) {
        this.totalAbril = totalAbril;
    }

    public Double getTotalMayo() {
        return totalMayo;
    }

    public void setTotalMayo(Double totalMayo) {
        this.totalMayo = totalMayo;
    }

    public Double getTotalJunio() {
        return totalJunio;
    }

    public void setTotalJunio(Double totalJunio) {
        this.totalJunio = totalJunio;
    }

    public Double getTotalJulio() {
        return totalJulio;
    }

    public void setTotalJulio(Double totalJulio) {
        this.totalJulio = totalJulio;
    }

    public Double getTotalAgosto() {
        return totalAgosto;
    }

    public void setTotalAgosto(Double totalAgosto) {
        this.totalAgosto = totalAgosto;
    }

    public Double getTotalSeptiembre() {
        return totalSeptiembre;
    }

    public void setTotalSeptiembre(Double totalSeptiembre) {
        this.totalSeptiembre = totalSeptiembre;
    }

    public Double getTotalOctubre() {
        return totalOctubre;
    }

    public void setTotalOctubre(Double totalOctubre) {
        this.totalOctubre = totalOctubre;
    }

    public Double getTotalNoviembre() {
        return totalNoviembre;
    }

    public void setTotalNoviembre(Double totalNoviembre) {
        this.totalNoviembre = totalNoviembre;
    }

    public Double getTotalDiciembre() {
        return totalDiciembre;
    }

    public void setTotalDiciembre(Double totalDiciembre) {
        this.totalDiciembre = totalDiciembre;
    }

    public CatalogoItem getEstadoAprobado() {
        return estadoAprobado;
    }

    public void setEstadoAprobado(CatalogoItem estadoAprobado) {
        this.estadoAprobado = estadoAprobado;
    }

    public CatalogoItem getEstadoRechazado() {
        return estadoRechazado;
    }

    public void setEstadoRechazado(CatalogoItem estadoRechazado) {
        this.estadoRechazado = estadoRechazado;
    }

    public LazyModel<ProgramacionIngresoEgreso> getLazyIngresos() {
        return lazyIngresos;
    }

    public void setLazyIngresos(LazyModel<ProgramacionIngresoEgreso> lazyIngresos) {
        this.lazyIngresos = lazyIngresos;
    }

    public LazyModel<ProgramacionIngresoEgreso> getLazyEgresos() {
        return lazyEgresos;
    }

    public void setLazyEgresos(LazyModel<ProgramacionIngresoEgreso> lazyEgresos) {
        this.lazyEgresos = lazyEgresos;
    }
//</editor-fold>

}
