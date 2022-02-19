/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.FinaRenTipoTransaccion;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author DEVELOPER
 */
@Named("transaccionView")
@ViewScoped
public class TransaccionesMB implements Serializable {

    private static final long serialVersionUID = 1L;
    @Inject
    private UserSession uSession;
    @Inject
    protected ManagerService services;

    private Boolean tienePadre;
    private List<FinaRenTipoTransaccion> tiposTransaccion;
    private FinaRenTipoLiquidacion transaccion, transaccionPadre;
    private TreeNode root;
    private List<FinaRenTipoLiquidacion> tipoLiqSel;
    private List<FinaRenTipoLiquidacion> tipoLiqHijos;
    private List<FinaRenTipoLiquidacion> tiposLiquidaciones;
    private LazyModel<FinaRenTipoLiquidacion> tiposLazy;
    private Map<String, Object> param;

    @PostConstruct
    public void initView() {

        try {

            param = new HashMap<>();
            param.put("estado", true);
            tiposTransaccion = new ArrayList();

            for (Object temp : services.findAll(FinaRenTipoTransaccion.class, param)) {
                tiposTransaccion.add((FinaRenTipoTransaccion) temp);
            }
            tiposLiquidaciones = new ArrayList<>();
            tiposLiquidaciones = services.getRenTipoLiquidacionList();
            loandingLazyTipoLiquidacion();

            root = new DefaultTreeNode();
            llenarArbol();

            //root.getChildren().add(root)
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void nuevoTipoLiquidacion() {
        transaccion = new FinaRenTipoLiquidacion();
    }

    public void llenarArbol() {
        try {
            param = new HashMap<>();
            param.put("idPadre", 0L);
            List<FinaRenTipoLiquidacion> raices;
            raices = services.getRenTransaccionesPadres(0L);

            for (FinaRenTipoLiquidacion temp : raices) {
                if (!temp.getTomado()) {
                    temp.setTomado(true);
                    TreeNode node = new DefaultTreeNode(temp, root);
                    llenarHijosArbol(temp, node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void llenarHijosArbol(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            List<FinaRenTipoLiquidacion> hijos;
            param = new HashMap<>();
            param.put("idPadre", hoja.getId());

            hijos = services.getRenTransaccionesPadres(hoja.getId());
            if (hijos == null || hijos.isEmpty()) {
                return;
            }

            for (FinaRenTipoLiquidacion temp2 : hijos) {
                if (!temp2.getTomado()) {
                    TreeNode node = new DefaultTreeNode(temp2, padre);
                    temp2.setTomado(true);
                    llenarHijosArbol(temp2, node);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void guardarTransaccionesHijas() {
        try {
            for (FinaRenTipoLiquidacion temp : tipoLiqSel) {
                if (!temp.equals(transaccionPadre)) {
                    temp.setTransaccionPadre(transaccionPadre.getId());
                    services.save(temp);
                }
            }
            JsfUtil.addInformationMessage("Info", "Se guardaron las subtransacciones correctamente");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void eliminarTransaccion(FinaRenTipoLiquidacion rtl) {
        try {
            rtl.setTransaccionPadre(0L);
            services.save(rtl);
            this.tipoLiqHijos.remove(rtl);
            JsfUtil.addInformationMessage("Info", "Se quitó la transacción");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void selTransaccionEdit(FinaRenTipoLiquidacion rtl) {
        transaccion = rtl;
        if (transaccion.getTransaccionPadre() != null) {
            transaccionPadre = (FinaRenTipoLiquidacion) services.find(FinaRenTipoLiquidacion.class, transaccion.getId());
        }
    }

    public void guardarNuevoTitulo() {
        try {
            if (transaccion.getPrefijo().trim().length() > 3) {
                JsfUtil.addErrorMessage("Error", "Prefijo debe contener maximo 3 caracteres.");
                return;
            }
            if (transaccion.getNombreTitulo().equals("") || transaccion.getPrefijo().equals("")) {
                JsfUtil.addErrorMessage("Error", "Campos Vacios.");
                return;
            }
            transaccion.setNombreTitulo(transaccion.getNombreTitulo().toUpperCase());
            transaccion.setPrefijo(transaccion.getPrefijo().trim().toUpperCase());
            param = new HashMap<>();
            param.put("prefijo", transaccion.getPrefijo());
            param.put("nombreTitulo", transaccion.getNombreTitulo());

            FinaRenTipoLiquidacion temp = (FinaRenTipoLiquidacion) services.findByParameter(FinaRenTipoLiquidacion.class, param);
            System.out.println("tem-->" + temp);
            if (temp == null) {
                System.out.println("entrando");
                transaccion.setFechaIngreso(new Date());
                transaccion.setUsuarioIngreso(uSession.getNameUser());
                transaccion.setEstado(true);
                transaccion.setMostrarTransaccion(true);
                transaccion.setTransaccionPadre(0L);
                transaccion.setCodigoTituloReporte(services.generarCodTitRep());
                transaccion = (FinaRenTipoLiquidacion) services.save(transaccion);
                if (transaccion != null && transaccion.getId() != null) {
                    JsfUtil.addInformationMessage("Info", "Título de reporte creado correctamente");
                    loandingLazyTipoLiquidacion();
                    tiposLiquidaciones = services.getRenTipoLiquidacionList();
                }
            }

        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

    }

    public void loandingLazyTipoLiquidacion() {
        tiposLazy = new LazyModel(FinaRenTipoLiquidacion.class);
        tiposLazy.getFilterss().put("estado", true);
        //tiposLazy.getFilterss().put("tipo", 2);
        tiposLazy.getSorteds().put("nombreTransaccion", "ASC");

        //tiposLazy = new TipoLiquidacionLazy(2, true);
    }

    public void nuevaTransaccion() {
        transaccion = new FinaRenTipoLiquidacion();
    }

    public void guardarEdicionTransaccion() {
        try {
            if (services.editarRenTipoLiquidacion(transaccion)) {
                JsfUtil.addInformationMessage("Info", "Se actualizó la transacción correctamente");
            } else {
                JsfUtil.addErrorMessage("Error", "Ocurrió un error al actualizar los datos. Inténtelo nuevamente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void eliminarTitulo(FinaRenTipoLiquidacion rtl) {
        try {
            rtl.setEstado(Boolean.FALSE);
            if (services.update(rtl)) {

                tiposLiquidaciones = services.getRenTipoLiquidacionList();
                JsfUtil.addInformationMessage("Info", "Se actualizó la transacción correctamente");
            } else {
                JsfUtil.addErrorMessage("Error", "Ocurrió un error al actualizar los datos. Inténtelo nuevamente.");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Boolean getTienePadre() {
        return tienePadre;
    }

    public void setTienePadre(Boolean tienePadre) {
        this.tienePadre = tienePadre;
    }

    public List<FinaRenTipoTransaccion> getTiposTransaccion() {
        return tiposTransaccion;
    }

    public void setTiposTransaccion(List<FinaRenTipoTransaccion> tiposTransaccion) {
        this.tiposTransaccion = tiposTransaccion;
    }

    public FinaRenTipoLiquidacion getTransaccion() {
        return transaccion;
    }

    public void setTransaccion(FinaRenTipoLiquidacion transaccion) {
        this.transaccion = transaccion;
    }

    public FinaRenTipoLiquidacion getTransaccionPadre() {
        return transaccionPadre;
    }

    public void setTransaccionPadre(FinaRenTipoLiquidacion transaccionPadre) {
        this.transaccionPadre = transaccionPadre;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiqSel() {
        return tipoLiqSel;
    }

    public void setTipoLiqSel(List<FinaRenTipoLiquidacion> tipoLiqSel) {
        this.tipoLiqSel = tipoLiqSel;
    }

    public List<FinaRenTipoLiquidacion> getTipoLiqHijos() {
        return tipoLiqHijos;
    }

    public void setTipoLiqHijos(List<FinaRenTipoLiquidacion> tipoLiqHijos) {
        this.tipoLiqHijos = tipoLiqHijos;
    }

    public List<FinaRenTipoLiquidacion> getTiposLiquidaciones() {
        return tiposLiquidaciones;
    }

    public void setTiposLiquidaciones(List<FinaRenTipoLiquidacion> tiposLiquidaciones) {
        this.tiposLiquidaciones = tiposLiquidaciones;
    }

    public LazyModel<FinaRenTipoLiquidacion> getTiposLazy() {
        return tiposLazy;
    }

    public void setTiposLazy(LazyModel<FinaRenTipoLiquidacion> tiposLazy) {
        this.tiposLazy = tiposLazy;
    }

    public Map<String, Object> getParam() {
        return param;
    }

    public void setParam(Map<String, Object> param) {
        this.param = param;
    }

}
