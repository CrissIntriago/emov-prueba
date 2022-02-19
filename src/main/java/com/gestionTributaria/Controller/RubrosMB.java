/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.FinaRenTipoValor;
import com.gestionTributaria.Entities.CtlgSalario;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author DEVELOPER
 */
@Named("rubrosView")
@ViewScoped
public class RubrosMB implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private UserSession uSession;
    @Inject
    private ManagerService services;
    @Inject
    private CatalogoItemService catalogoService;
    @Inject
    private ServletSession ss;

    private FinaRenTipoLiquidacion tipoLiquidacion;
    private List<FinaRenRubrosLiquidacion> rubrosList;
    private LazyModel<FinaRenRubrosLiquidacion> rubrosLazy;
    private FinaRenRubrosLiquidacion rubro;
    private FinaRenTipoValor tipoValor;
    private BigDecimal valorporcentual;
    private Map<String, Object> param;
    private List<CatalogoItem> tiposRubrosEsp;
    private CatalogoItem tipoRubro;
//    private List<ContCuentas> cuentasContablesList;
//    private List<CatalogoPresupuesto> cuentasPresupuestoList;

    @PostConstruct
    public void initView() {

        try {
            param = new HashMap<>();
//            cuentasContablesList = services.findAll(ContCuentas.class, null);
//            cuentasPresupuestoList = services.findAll(CatalogoPresupuesto.class, null);
            valorporcentual = BigDecimal.ZERO;
            tipoLiquidacion = new FinaRenTipoLiquidacion();
            tipoLiquidacion = (FinaRenTipoLiquidacion) ss.getValor("idTipoLiquidacion");
            rubrosList = services.getRubrosByTipoLiquidacionCodRubroASC(tipoLiquidacion.getId());
            rubrosLazy = new LazyModel<>(FinaRenRubrosLiquidacion.class);
            //  rubrosLazy.getFilterss().put("tipoLiquidacion", null);
            JsfUtil.update("frmMain");
        } catch (Exception e) {
            JsfUtil.redirect(CONFIG.URL_APP + "/moduloGestionTributario/mantenimientos/rubros.xhtml");
        }

    }

    public List<FinaRenTipoValor> getTipoValoresById(Long id) {
        param = new HashMap<>();
        param.put("id", id);
        param.put("estado", true);
        return services.findAll(FinaRenTipoValor.class, param);
    }

    public List<FinaRenTipoValor> getTipoValores(String prefijo) {
        param = new HashMap<>();

        if (!prefijo.isEmpty()) {
            param.put("prefijo", prefijo);
            param.put("estado", true);
            return (List<FinaRenTipoValor>) services.findAll(FinaRenTipoValor.class, param);
        } else {
            param.put("estado", true);
            return services.findAll(FinaRenTipoValor.class, param);

        }
    }

    public void nuevoRubro() {
        rubro = new FinaRenRubrosLiquidacion();
        rubro.setRubroDelMunicipio(Boolean.TRUE);
        rubro.setEstado(Boolean.TRUE);
        tipoRubro = new CatalogoItem();
        if (tipoLiquidacion.getTipo().equals("ESP")) {
            tiposRubrosEsp = catalogoService.findByCatalogo("tipo_tasa_tramite");
        }
        tipoValor = new FinaRenTipoValor();
    }

    public void editarRubro(FinaRenRubrosLiquidacion rl) {
        try {
            this.rubro = rl;
            tipoValor = services.find(FinaRenTipoValor.class, rl.getTipoValor().getId());
            tipoRubro = new CatalogoItem();
            if (tipoLiquidacion.getTipo().equals("ESP")) {
                tiposRubrosEsp = catalogoService.findByCatalogo("tipo_tasa_tramite");
            }
            this.rubro.setTipoValor(tipoValor);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void calcularPorcentajeSBU() {
        Map<String, Object> map = new HashMap();
        map.put("anio", new BigInteger(Utils.getAnio(new Date()).toString()));
        CtlgSalario salario = (CtlgSalario) services.findByParameter(CtlgSalario.class, map);
        rubro.setValor((salario.getValor().multiply(valorporcentual)).divide(new BigDecimal(100)));

    }

    public void guardarRubro() {
        try {
//            if (rubro.getCuentaContable() == null || rubro.getCuentaOrden() == null || rubro.getCuentaPresupuesto() == null
//                    || rubro.getDescripcion() == null) {
//                JsfUtil.addWarningMessage("Info", "Faltan datos del rubro");
//                return;
//            }
            System.out.println("tipoValor " + tipoValor);
            rubro.setTipoLiquidacion(tipoLiquidacion);
            rubro.setTipoValor(tipoValor);
            rubro = services.guardarRubroNuevo(rubro, rubro.getTipoLiquidacion());
            if (rubrosList == null || rubrosList.isEmpty()) {
                rubrosList = new ArrayList();
            }
            rubrosList.add(rubro);
            //           initView();
            //  JsfUtil.redirectFaces(CONFIG.URL_APP + "moduloGestionTributario/mantenimientos/_asignacionrubros.xhtml");
            JsfUtil.addInformationMessage("Info", "Rubro guardado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarEdicionRubro() {
        try {
            rubro.setTipoValor(tipoValor);
            if (rubro.getId() == null) {
                services.save(rubro);
            } else {
                services.update(rubro);
            }

            JsfUtil.addInformationMessage("Info", "Rubro guardado correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
        rubrosLazy = new LazyModel<>(FinaRenRubrosLiquidacion.class);
    }

    public void eliminarRubro(Integer index) {
        FinaRenRubrosLiquidacion temp = rubrosList.get(index);
        rubrosList.remove(temp);
        if (services.eliminarRubro(temp, tipoLiquidacion)) {
            JsfUtil.addInformationMessage("Info", "Rubro eliminado correctamente");
        } else {
            JsfUtil.addInformationMessage("Info", "Ocurrió un problema al eliminar el rubro");
        }

    }

    public void guardarRubros() {
        try {
            services.guardarRubrosPorTipoLiquidacion(rubrosList);
            JsfUtil.addInformationMessage("Info", "Rubros guardados correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void agregarRubro(FinaRenRubrosLiquidacion rubro) {
        try {
            rubro.setEstado(Boolean.TRUE);
            this.rubro = services.guardarRubroNuevo(rubro, tipoLiquidacion);
            //this.rubro.setTipoLiquidacion(tipoLiquidacion);
            //services.update(this.rubro);
            if (rubrosList == null) {
                rubrosList = new ArrayList();
            }
            if (!rubrosList.contains(rubro)) {
                rubrosList.add(this.rubro);
            }

            JsfUtil.addInformationMessage("Info", "Se agregó el rubro correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public FinaRenTipoLiquidacion getTipoLiquidacion() {
        return tipoLiquidacion;
    }

    public void setTipoLiquidacion(FinaRenTipoLiquidacion tipoLiquidacion) {
        this.tipoLiquidacion = tipoLiquidacion;
    }

    public List<FinaRenRubrosLiquidacion> getRubrosList() {
        return rubrosList;
    }

    public void setRubrosList(List<FinaRenRubrosLiquidacion> rubrosList) {
        this.rubrosList = rubrosList;
    }

    public LazyModel<FinaRenRubrosLiquidacion> getRubrosLazy() {
        return rubrosLazy;
    }

    public void setRubrosLazy(LazyModel<FinaRenRubrosLiquidacion> rubrosLazy) {
        this.rubrosLazy = rubrosLazy;
    }

    public FinaRenRubrosLiquidacion getRubro() {
        return rubro;
    }

    public void setRubro(FinaRenRubrosLiquidacion rubro) {
        this.rubro = rubro;
    }

    public FinaRenTipoValor getTipoValor() {
        return tipoValor;
    }

    public void setTipoValor(FinaRenTipoValor tipoValor) {
        this.tipoValor = tipoValor;
    }

    public BigDecimal getValorporcentual() {
        return valorporcentual;
    }

    public void setValorporcentual(BigDecimal valorporcentual) {
        this.valorporcentual = valorporcentual;
    }

//    public List<ContCuentas> getCuentasContablesList() {
//        return cuentasContablesList;
//    }
//    
//    public void setCuentasContablesList(List<ContCuentas> cuentasContablesList) {
//        this.cuentasContablesList = cuentasContablesList;
//    }
//    
//    public List<CatalogoPresupuesto> getCuentasPresupuestoList() {
//        return cuentasPresupuestoList;
//    }
//    
//    public void setCuentasPresupuestoList(List<CatalogoPresupuesto> cuentasPresupuestoList) {
//        this.cuentasPresupuestoList = cuentasPresupuestoList;
//    }
    public List<CatalogoItem> getTiposRubrosEsp() {
        return tiposRubrosEsp;
    }

    public void setTiposRubrosEsp(List<CatalogoItem> tiposRubrosEsp) {
        this.tiposRubrosEsp = tiposRubrosEsp;
    }

    public CatalogoItem getTipoRubro() {
        return tipoRubro;
    }

    public void setTipoRubro(CatalogoItem tipoRubro) {
        this.tipoRubro = tipoRubro;
    }

}
