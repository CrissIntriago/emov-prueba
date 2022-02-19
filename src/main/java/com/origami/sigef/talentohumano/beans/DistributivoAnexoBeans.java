/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoProformaPresupuesto;
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import static com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot.LOG;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "distributivoAnexoView")
@ViewScoped
public class DistributivoAnexoBeans extends BpmnBaseRoot implements Serializable {

    @Inject
    private CupoPresupuestoService cupoPresupuestoService;
    @Inject
    ClienteService clienteService;
    //distributivoAnexo Variables
    private LazyModel<DistributivoAnexo> distributivoAnexoLazy;
    private DistributivoAnexo distributivoAnexo;
    @Inject
    private DistributivoAnexoService distributivoAnexoService;
    //catalogo master y busqueda
    private OpcionBusqueda busqueda;
    private List<MasterCatalogo> periodos;
    @Inject
    private MasterCatalogoService masterService;

    private Boolean bloqueo;
    //bloqueo si esta aprobado
    private boolean aprobado;
    @Inject
    private ProformaPresupuestoPlanificadoService proformaPresupuestoPlanificadoService;
    List<CatalogoProformaPresupuesto> li;
    @Inject
    private ServletSession ss;
    private short anio;
    private Short periodo;
    private String observaciones;
    private BigDecimal totalCupo, cupoAsignado;

    @PostConstruct
    public void inicializate() {
        try {
            if (this.session.getTaskID() != null) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                observacion.setIdTramite(tramite);
                periodo = cupoPresupuestoService.getListaPeriodos(BigInteger.valueOf(tramite.getNumTramite()));

                busqueda = new OpcionBusqueda();
                busqueda.setAnio(periodo);
                periodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo2", new Object[]{"tipo_cuenta", "D", periodo});
                distributivoAnexoLazy = new LazyModel<>(DistributivoAnexo.class);
                distributivoAnexoLazy.getFilterss().put("estado", true);
                distributivoAnexoLazy.getFilterss().put("anio", busqueda.getAnio());
                    distributivoAnexoLazy.setDistinct(false);
//        aprobado =   proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(busqueda.getAnio(),false,true);
                setAprobado(proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(busqueda.getAnio(), false, true));
                updateValoresCupo();
            } else {
                JsfUtil.redirectFaces("/procesos/bandeja-tareas");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, null, e);
        }
    }

    public void form(DistributivoAnexo da) {
        if (da != null) {
            this.distributivoAnexo = da;
            if ("FIJO".equals(distributivoAnexo.getValorParametrizado().getTipoEstado().getTexto())) {
                bloqueo = true;
            }
            if ("VARIABLE".equals(distributivoAnexo.getValorParametrizado().getTipoEstado().getTexto())) {
                bloqueo = false;
            }
        } else {
            distributivoAnexo = new DistributivoAnexo();
        }
        PrimeFaces.current().executeScript("PF('disAnexoDlg').show()");
        PrimeFaces.current().ajax().update(":panelDisAnexo");
    }

    public void updateValoresCupo() {
        totalCupo = BigDecimal.ZERO;
        cupoAsignado = BigDecimal.ZERO;
        totalCupo = cupoPresupuestoService.getCupoOtros("DA", BigInteger.valueOf(tramite.getNumTramite()));
        cupoAsignado = cupoPresupuestoService.getValorAsigandoDistributivoAnexo(periodo);

    }

    public void guardar() {
        if (distributivoAnexo.getValorParametrizado() == null) {
            JsfUtil.addWarningMessage("Información", " Debe Ingresar el Valor que se Quiere agregar.");
            return;
        }
        if (distributivoAnexo.getProyeccion() == null) {
            JsfUtil.addWarningMessage("Información", " Debe Ingresar la proyeccion para sacar el Monto Resultante.");
            return;
        }
        if (distributivoAnexo.getMonto() == null) {
            JsfUtil.addWarningMessage("Información", " Debe Ingresar Tipo de valor y Proyeccion para generar el monto del Rubro.");
            return;
        }
        boolean edit = distributivoAnexo.getId() != null;
        distributivoAnexo.setAnio(busqueda.getAnio());
        distributivoAnexo.setEstado(Boolean.TRUE);
        if (!edit) {
            distributivoAnexo = distributivoAnexoService.create(distributivoAnexo);
        } else {
            distributivoAnexoService.edit(distributivoAnexo);

        }
        updateValoresCupo();
        PrimeFaces.current().executeScript("PF('disAnexoDlg').hide()");
        PrimeFaces.current().ajax().update(":formMain");
        PrimeFaces.current().ajax().update("formMain:totaleCupos");

        JsfUtil.addSuccessMessage("Información", distributivoAnexo.getNombre() + (edit ? " editada" : " registrada") + " con exito para el período " + busqueda.getAnio());
    }

    public void eliminar(DistributivoAnexo dis) {
        dis.setEstado(Boolean.FALSE);
        distributivoAnexoService.edit(dis);
        JsfUtil.addSuccessMessage("Información", dis.getValorParametrizado().getNombre() + " eliminada con éxito");

    }

    public void openDlgValoresParametrizables() {
        Map<String, List<String>> params = new HashMap<>();
        params.put("TIPO", Arrays.asList("ANEXO"));
        Utils.openDialog("/facelet/talentoHumano/dialogValores", "850", "400", params);
    }

    public void selectDataRubros(SelectEvent evt) {
        Boolean saber = false;
        ParametrosTalentoHumano parametroTem;
        parametroTem = (ParametrosTalentoHumano) evt.getObject();
        //para validar que exista
//        if (distributivoAnexoService.getValidateValor(parametroTem, busqueda) != null) {
//            JsfUtil.addWarningMessage("Información", " Este valor ya existe para el Período " + busqueda.getAnio());
//            return;
//        }
        distributivoAnexo.setValorParametrizado(parametroTem);
        distributivoAnexo.setValor(distributivoAnexo.getValorParametrizado().getValor());
        if ("FIJO".equals(distributivoAnexo.getValorParametrizado().getTipoEstado().getTexto())) {
            bloqueo = true;
        }
        if ("VARIABLE".equals(distributivoAnexo.getValorParametrizado().getTipoEstado().getTexto())) {
            bloqueo = false;
        }
        if (distributivoAnexo.getProyeccion() != null) {
            calcular(distributivoAnexo);
            PrimeFaces.current().ajax().update(":montoR");
        }
    }

    public void buscarPeriodo() {
        distributivoAnexoLazy.getFilterss().put("estado", true);
        distributivoAnexoLazy.getFilterss().put("anio", busqueda.getAnio());
        aprobado = proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(busqueda.getAnio(), false, true);
        updateValoresCupo();
    }

    public void calcular(DistributivoAnexo rubro) {
        if (rubro.getId() != null) {
            if (rubro.getValorParametrizado() != null) {
                double valorR;
                double result;
                BigDecimal valorActual = distributivoAnexoService.getValorActualDistributivo(rubro);
                valorR = distributivoAnexo.getValor().doubleValue();
                result = (valorR * rubro.getProyeccion());
                if (cupoAsignado.doubleValue() + (result - valorActual.doubleValue()) <= totalCupo.doubleValue()) {
                    rubro.setMonto(BigDecimal.valueOf(result));
                    JsfUtil.addInformationMessage("Infromacion", "El cupo se asigno correctamente");
                    updateValoresCupo();
                } else {
                    rubro.setValor(valorActual);
                    JsfUtil.addErrorMessage("Infromacion", "No tienes suficiente fondo para brindar esa cantidad ");
                }
            }
        } else {
            if (rubro.getProyeccion() == null) {
                JsfUtil.addErrorMessage("Infromacion", "Debe asignar una proyección.");
                return;
            }
            double valorR;
            double result;
            valorR = distributivoAnexo.getValor().doubleValue();
            result = valorR * rubro.getProyeccion();
            if (cupoAsignado.doubleValue() + result <= totalCupo.doubleValue()) {
                rubro.setMonto(BigDecimal.valueOf(result));
                JsfUtil.addInformationMessage("Infromacion", "El cupo se asigno correctamente");
                updateValoresCupo();
            } else {
                rubro.setValor(BigDecimal.ZERO);
                JsfUtil.addErrorMessage("Infromacion", "No tienes suficiente fondo para brindar esa cantidad ");
            }

        }
    }

    public void opendlgPrint() {
        PrimeFaces.current().executeScript("PF('dlgPrint').show()");
    }

    public void printReport() {
        if (anio == 0) {
            JsfUtil.addWarningMessage("Información", " Ingrese un año para generar Reporte.");
            return;
        }
        boolean blockPrint = false;
        blockPrint = proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(anio, false, true);
        ss.addParametro("anio", anio);
        ss.addParametro("bloqueo", blockPrint);
        ss.setNombreReporte("distributivoAnexo");
        ss.setNombreSubCarpeta("Distributivos");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
        PrimeFaces.current().executeScript("PF('dlgPrint').hide()");

    }

    public void dlogoObservaciones() {
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
            //clienteService.getUnidadUserCodigo("CPR","2")
            getParamts().put("usuario", clienteService.getrolsUser(RolUsuario.presupuesto)); //PRESUPUESTO 2
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

//    public void BloquearSiEsAprobado(Short per) {
//        li = new ArrayList<>();
//        boolean verificar = true;
//        li = proformaPresupuestoPlanificadoService.desactivarBoton(per, false, true);
//        if (li.size() > 0) {
//            verificar = true;
//        } else {
//            verificar = false;
//        }
//        setAprobado(verificar);
//    }
//<editor-fold defaultstate="collapsed" desc="Get and Set">
    public LazyModel<DistributivoAnexo> getDistributivoAnexoLazy() {
        return distributivoAnexoLazy;
    }

    public void setDistributivoAnexoLazy(LazyModel<DistributivoAnexo> distributivoAnexoLazy) {
        this.distributivoAnexoLazy = distributivoAnexoLazy;
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

    public DistributivoAnexo getDistributivoAnexo() {
        return distributivoAnexo;
    }

    public void setDistributivoAnexo(DistributivoAnexo distributivoAnexo) {
        this.distributivoAnexo = distributivoAnexo;
    }

//</editor-fold>
    public Boolean getBloqueo() {
        return bloqueo;
    }

    public void setBloqueo(Boolean Bloqueo) {
        this.bloqueo = Bloqueo;
    }

    public boolean isAprobado() {
        return aprobado;
    }

    public void setAprobado(boolean aprobado) {
        this.aprobado = aprobado;
    }

    public short getAnio() {
        return anio;
    }

    public void setAnio(short anio) {
        this.anio = anio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public BigDecimal getTotalCupo() {
        return totalCupo;
    }

    public void setTotalCupo(BigDecimal totalCupo) {
        this.totalCupo = totalCupo;
    }

    public BigDecimal getCupoAsignado() {
        return cupoAsignado;
    }

    public void setCupoAsignado(BigDecimal cupoAsignado) {
        this.cupoAsignado = cupoAsignado;
    }

}
