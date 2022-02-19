/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.models;

import javax.ejb.Local;
import org.primefaces.model.TreeNode;

/**
 *
 * @author Administrator
 */
@Local
public interface TitulosReporteCacheLocal {

    public TreeNode getTree();

    public TreeNode getTreeCobros();

    public TreeNode getTreePlanificacion();
    
    public void clearCachePlanificacion();
    
    public void clearCache();
}
