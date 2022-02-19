/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.config.CONFIG;
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
import com.origami.sigef.contabilidad.service.CupoPresupuestoService;
import com.origami.sigef.contabilidad.service.ProformaPresupuestoPlanificadoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "disributivoAnexoSinProcesoView")
@ViewScoped
public class distributivoAnexoSinProcesoController implements Serializable {

    @Inject
    private CupoPresupuestoService cupoPresupuestoService;

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

    @PostConstruct
    public void inicializate() {

        busqueda = new OpcionBusqueda();

        periodos = masterService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "D"});
        distributivoAnexoLazy = new LazyModel<>(DistributivoAnexo.class);
        distributivoAnexoLazy.getFilterss().put("estado", true);
        distributivoAnexoLazy.getFilterss().put("anio", busqueda.getAnio());
        distributivoAnexoLazy.setDistinct(false);
        setAprobado(proformaPresupuestoPlanificadoService.BloquearSiEsAprobado(busqueda.getAnio(), false, true));
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
        PrimeFaces.current().executeScript("PF('disAnexoDlg').hide()");
        PrimeFaces.current().ajax().update(":formMain");
        JsfUtil.addSuccessMessage("Información", distributivoAnexo.getNombre() + (edit ? " editada" : " registrada") + " con exito para el período " + busqueda.getAnio());
    }

    public void eliminar(DistributivoAnexo dis) {
        Boolean resultado = distributivoAnexoService.getComprobarPartida(dis);
        if (resultado) {
            JsfUtil.addErrorMessage("ERROR!!", "No se puede eliminar porque tiene una partida asociada");
        } else {
            dis.setEstado(Boolean.FALSE);
            distributivoAnexoService.edit(dis);
            distributivoAnexoService.getDelePartidaValorDistributivo(dis);
            JsfUtil.addSuccessMessage("Información", dis.getValorParametrizado().getNombre() + " eliminada con éxito");
        }
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

    }

    public void calcular(DistributivoAnexo rubro) {
        if (rubro.getProyeccion() == null) {
            JsfUtil.addErrorMessage("Infromacion", "Debe asignar una proyección.");
            return;
        }
        if (rubro.getValorParametrizado() != null) {
            double valorR;
            double result;
            valorR = distributivoAnexo.getValor().doubleValue();
            result = valorR * rubro.getProyeccion();
            rubro.setMonto(BigDecimal.valueOf(result));
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
    //</editor-fold>

}
