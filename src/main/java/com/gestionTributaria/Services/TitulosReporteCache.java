/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Services;

import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.asgard.Entity.FinaRenTipoTransaccion;
import com.asgard.Entity.RenTipoLiquidacionDepartamento;
import com.gestionTributaria.models.TitulosReporteCacheLocal;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.Utils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
@Stateless
public class TitulosReporteCache implements TitulosReporteCacheLocal {

    @javax.inject.Inject
    private ManagerService manager;
    @Inject
    protected UserSession sess;

    protected ConcurrentMap<String, TreeNode> treeMap;
    protected ConcurrentMap<String, String> lockerMap = new ConcurrentHashMap<>();
    private Map<String, Object> parametros;

    @Override
    public void clearCache() {
        treeMap.remove("arbol_recaudaciones");
        treeMap.remove("arbol_recaudaciones_generales");
        treeMap.remove("arbol_planificacion");
    }

    @Override
    public void clearCachePlanificacion() {
        treeMap.remove("arbol_planificacion");
    }
    
    @Override
    public TreeNode getTree() {
        TreeNode root = treeMap.get("arbol_recaudaciones_generales");
        if (root == null) {
            generarArbol(1);
            return null;
        }
        root.setExpanded(true);
        return (TreeNode) Utils.clone(root);
    }

    @Override
    public TreeNode getTreeCobros() {
        TreeNode root = treeMap.get("arbol_recaudaciones");
        if (root == null) {
            generarArbol(2);
            return getTreeCobros();
        }

        return (TreeNode) Utils.clone(root);
    }

    @Override
    public TreeNode getTreePlanificacion() {
        TreeNode root = treeMap.get("arbol_planificacion");
        if (root == null) {
            generarArbol(3);
            return getTreePlanificacion();
        }

        return (TreeNode) Utils.clone(root);
    }

    private TreeNode createTree(int val) {
        TreeNode root = new DefaultTreeNode("Titulos", null);
        this.llenarArbol(root, val);
        return root;
    }

    private void llenarArbol(TreeNode root, int val) {
        try {
            List<FinaRenTipoLiquidacion> raices = null;
            switch (val) {
                case 1:
                    parametros = new HashMap<>();
                    parametros.put("idPadre", 0L);
                    raices = manager.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre = :idPadre AND r.nombreTransaccion IS NOT NULL AND r.mostrarTransaccion = true and r.estado = true"
                            + " ORDER BY r.id, r.transaccionPadre, r.nombreTransaccion ASC", parametros);
                    break;
                case 2:
                    raices = manager.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre is null AND r.nombreTransaccion IS NOT NULL AND r.mostrarTransaccion = true AND r.prefijo = 'GEN' "
                            + " ORDER BY r.id, r.transaccionPadre, r.nombreTransaccion ASC", null);
                    break;
                case 3:
//                    parametros = new HashMap<>();
//                    parametros.put("usuario", sess.getNameUser());
                    List<RenTipoLiquidacionDepartamento> departamentos = manager.listaRubrosPadres(sess.getNameUser());
                    FinaRenTipoLiquidacion rtl = null;
                    System.out.println("user " + sess.getNameUser());
                    System.out.println("departamentos " + departamentos.size());
                    raices = new ArrayList<>();
                    if (departamentos != null && Utils.isNotEmpty(departamentos)) {
                        for (RenTipoLiquidacionDepartamento rtld : departamentos) {
                            System.out.println("ingreaando " + rtld);
                            raices.add(rtld.getTipoLiquidacion());
                        }
                    }
                    break;
                default:
                    break;
            }

            for (FinaRenTipoLiquidacion temp : raices) {
                if (!temp.getTomado()) {
                    temp.setTomado(true);
                    TreeNode node = new DefaultTreeNode(temp, root);
                    if (val != 3) {
                        llenarHijosArbol(temp, node);
                    } else {
                        llenarHijosArbol2(temp, node);
                    }

                }
//                System.out.println("temp " + temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void llenarHijosArbol(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            List<FinaRenTipoLiquidacion> hijos = new ArrayList<>();
            FinaRenTipoTransaccion t = (FinaRenTipoTransaccion) manager.find(FinaRenTipoTransaccion.class, 3L);
            parametros = new HashMap<>();
            parametros.put("idPadre", hoja.getId());
            hijos = manager.findAllQuery("SELECT r FROM FinaRenTipoLiquidacion r WHERE r.transaccionPadre=:idPadre AND r.nombreTransaccion IS NOT NULL AND r.mostrarTransaccion = true", parametros);

            if (hijos == null || hijos.isEmpty()) {
                return;
            }

            for (FinaRenTipoLiquidacion temp2 : hijos) {
                if (!temp2.getTomado()) {
                    TreeNode node = new DefaultTreeNode(temp2, padre);
                    temp2.setTomado(true);
                    node.setExpanded(true);
                    llenarHijosArbol(temp2, node);
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }

    private void llenarHijosArbol2(FinaRenTipoLiquidacion hoja, TreeNode padre) {
        try {
            parametros = new HashMap<>();
            parametros.put("usuario", sess.getUserId());
            parametros.put("idPadre", hoja.getId());
            List<FinaRenTipoLiquidacion> hijos = new ArrayList();
            List<RenTipoLiquidacionDepartamento> departamentos = manager.findAllQuery("SELECT r FROM RenTipoLiquidacionDepartamento r WHERE r.usuario.id =:usuario AND r.estado = true AND r.tipoLiquidacion.transaccionPadre=:idPadre",
                    parametros);
            FinaRenTipoLiquidacion rtl = null;
            if (departamentos != null && !departamentos.isEmpty()) {
                for (RenTipoLiquidacionDepartamento rtld : departamentos) {
                    rtl = manager.find(FinaRenTipoLiquidacion.class, rtld.getTipoLiquidacion().getId());
                    hijos.add(rtl);
                }
            } else {
                return;
            }

            if (hijos.isEmpty()) {
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
            System.err.println(e);
        }
    }

    protected void generarArbol(int val) {

        synchronized (getLockerObject()) {
            TreeNode tree = null;
            // comprobar si no se entró en espera y ya existe el "arbol_recaudaciones" mapeado:
            switch (val) {
                case 1:
                    tree = treeMap.get("arbol_recaudaciones_generales");
                    break;
                case 2:
                    tree = treeMap.get("arbol_recaudaciones");
                    break;
                case 3:
                    tree = treeMap.get("arbol_planificacion");
                    break;
                default:
                    break;
            }

            if (tree == null) {
                this.loadTree(val);
            }

        }
    }

    private void loadTree(int val) {
        //TreeNode tree = this.getTree();

        TreeNode tree = createTree(val);
        // si se encontro menubar, realizar la clonacion
        if (tree != null) {
            tree.setExpanded(true);
            switch (val) {
                case 1:
                    treeMap.putIfAbsent("arbol_recaudaciones_generales", tree);
                    break;
                case 2:
                    treeMap.putIfAbsent("arbol_recaudaciones", tree);
                    break;
                case 3:
                    treeMap.putIfAbsent("arbol_planificacion", tree);
                    break;
                default:
                    break;
            }

        }
    }

    protected String getLockerObject() {
        lockerMap.putIfAbsent("arbol_recaudaciones", "arbol_recaudaciones");

        return lockerMap.get("arbol_recaudaciones");
    }

    /**
     * Inicializa el map de menubars en vacio
     */
    protected void initMenubarsMap() {
        this.treeMap = new ConcurrentHashMap<>();
    }

    /**
     * Inicializa el singleton ejb
     */
    @PostConstruct
    protected void init() {
        this.initMenubarsMap();
    }

}
