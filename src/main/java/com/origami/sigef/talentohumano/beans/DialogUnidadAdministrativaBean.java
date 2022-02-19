/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Angel Navarro
 */
@Named(value = "dialogUnidadAdministrativaBean")
@ViewScoped
public class DialogUnidadAdministrativaBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @Inject
    private UnidadAdministrativaService unidadAdmiinistrativaService;

    private TreeNode nodeMenus;
    private TreeNode selectedNode;

    @PostConstruct
    public void inicializar() {
        List<UnidadAdministrativa> allPadre = unidadAdmiinistrativaService.getAllPadre();
        UnidadAdministrativa u = new UnidadAdministrativa();
        u.setCodigo("00000");
        u.setNombre("Unidades Adm");
        nodeMenus = new DefaultTreeNode(u, null);
        nodeMenus.setExpanded(true);
        if (Utils.isNotEmpty(allPadre)) {
            for (UnidadAdministrativa item : allPadre) {
                if (item.getEstado() && item.getEstadoActivo()) {
                    createItems(nodeMenus, item, true);
                }
            }
        }
    }

    private TreeNode createItems(TreeNode node, UnidadAdministrativa menu, boolean expanded) {
        TreeNode menus = new DefaultTreeNode(menu, node);
        menus.setRowKey("id");
        menus.setSelectable(true);
        menus.setExpanded(expanded);
        List<UnidadAdministrativa> padreUnidad = null;
        if (Utils.isNotEmpty(menu.getUnidadAdministrativaList())) {
            if (padreUnidad == null) {
                padreUnidad = new ArrayList<>(menu.getUnidadAdministrativaList().size());
            }
            for (UnidadAdministrativa uap : menu.getUnidadAdministrativaList()) {
                if (uap.getEstado() && uap.getEstadoActivo()) {
                    padreUnidad.add(uap);
                }
            }
        } else {
            padreUnidad = unidadAdmiinistrativaService.getPadreUnidad(menu);
        }

        if (Utils.isNotEmpty(padreUnidad)) {
            for (UnidadAdministrativa item : padreUnidad) {
                createItems(menus, item, true);
            }
        }
        return menus;
    }

    public void onNodeSelect(NodeSelectEvent event) {
        PrimeFaces.current().dialog().closeDynamic(event.getTreeNode().getData());
    }

    public TreeNode getNodeMenus() {
        return nodeMenus;
    }

    public void setNodeMenus(TreeNode nodeMenus) {
        this.nodeMenus = nodeMenus;
    }

    public TreeNode getSelectedNode() {
        return selectedNode;
    }

    public void setSelectedNode(TreeNode selectedNode) {
        this.selectedNode = selectedNode;
    }

}
