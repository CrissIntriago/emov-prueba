/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenLocalCategoria;
import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.util.JsfUtil;
import java.io.Serializable;
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
 * @author Administrator
 */
@Named
@ViewScoped
public class CategoriaLC implements Serializable {

    @Inject
    private ManagerService services;
    private LazyModel<FinaRenLocalCategoria> categoriasLcLazy;
    private FinaRenLocalCategoria categoriaLC;
    private TreeNode root;
    private Map<String, Object> param;

    @PostConstruct
    public void init() {
        param = new HashMap<>();
        categoriasLcLazy = new LazyModel<>(FinaRenLocalCategoria.class);
        categoriasLcLazy.getFilterss().put("padre", 0L);
        categoriasLcLazy.getSorteds().put("id", "asc");
        categoriaLC = new FinaRenLocalCategoria();
    }

    public void llenarHijosMayor(FinaRenLocalCategoria padre) {
        param = new HashMap<>();
        param.put("padre", padre.getId());
        List<FinaRenLocalCategoria> hijoMayor = services.findAllQuery("select s from FinaRenLocalCategoria s where s.padre=:padre  order by s.id asc", param);
        root = new DefaultTreeNode("Subcategoria", null);
        for (FinaRenLocalCategoria item : hijoMayor) {
            TreeNode hijo = new DefaultTreeNode(item, root);
            llenaNodosHijos(item, hijo);
        }
    }

    public void dialogOpenEditar(FinaRenLocalCategoria categoria) {
        if (categoria == null) {
            categoriaLC = new FinaRenLocalCategoria();
            categoriaLC.setPadre(0L);

        } else {
            categoriaLC = new FinaRenLocalCategoria();
            categoriaLC = categoria;
        }
    }

    public void dialogHijo(FinaRenLocalCategoria padre) {
        categoriaLC = new FinaRenLocalCategoria();
        categoriaLC.setPadre(padre.getId());
    }

    public void llenaNodosHijos(FinaRenLocalCategoria padre, TreeNode hijoNode) {
        param = new HashMap<>();
        param.put("padre", padre.getId());
        List<FinaRenLocalCategoria> hijoMayor = services.findAllQuery("select s from FinaRenLocalCategoria s where s.padre=:padre  order by s.id asc", param);
        for (FinaRenLocalCategoria item : hijoMayor) {
            TreeNode hijo = new DefaultTreeNode(item, hijoNode);
            llenaNodosHijos(item, hijo);
        }
    }

    public void save() {

        if (categoriaLC.getCodigo() != null) {
            param = new HashMap<>();
            param.put("codigo", categoriaLC.getCodigo());
            param.put("estado", true);

            List<FinaRenLocalCategoria> list = services.findAllQuery("select f from FinaRenLocalCategoria f where f.codigo=:codigo and f.estado=:estado", param);

            if (!list.isEmpty()) {
                JsfUtil.addWarningMessage("", "El codigo a ingresar ya existe inserte otro código por favor");
            }
        }

        if (categoriaLC.getId()== null) {
            services.save(categoriaLC);
        } else {
            services.update(categoriaLC);
        }

        JsfUtil.addInformationMessage("", "El Resgistro fue exitoso");
    }

    //<editor-fold defaultstate="collapsed" desc="Setter an getter">
    public LazyModel<FinaRenLocalCategoria> getCategoriasLcLazy() {
        return categoriasLcLazy;
    }

    public void setCategoriasLcLazy(LazyModel<FinaRenLocalCategoria> categoriasLcLazy) {
        this.categoriasLcLazy = categoriasLcLazy;
    }

    public FinaRenLocalCategoria getCategoriaLC() {
        return categoriaLC;
    }

    public void setCategoriaLC(FinaRenLocalCategoria categoriaLC) {
        this.categoriaLC = categoriaLC;
    }

    public TreeNode getRoot() {
        return root;
    }

    public void setRoot(TreeNode root) {
        this.root = root;
    }
//</editor-fold>

}
