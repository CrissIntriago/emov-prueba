/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ventanilla.Controller;

import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.util.JsfUtil;
import com.ventanilla.Services.VentanillaService;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author Arturo
 */
@Named
@ViewScoped
public class AsignarResponsableMB implements Serializable {

    @Inject
    private VentanillaService ventanillaService;

    private List<UnidadAdministrativa> departamentos;

    private UnidadAdministrativa departamento;

    private List<UsuarioRol> listRolUsuario;

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            departamentos = ventanillaService.findAllQuery("SELECT u FROM UnidadAdministrativa u WHERE u.estado = TRUE AND u.estadoActivo = true ORDER BY nombre ASC", null);
            departamento = new UnidadAdministrativa();
            listRolUsuario = ventanillaService.findAllQuery("SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r  where r.unidadAdministrativa.id=27", null);
        }
    }

    public void initListUsuarios() {

        Map<String, Object> params = new HashMap<>();
        params.put("departamento", departamento.getId());

        listRolUsuario = ventanillaService.findAllQuery("SELECT ur FROM UsuarioRol ur INNER JOIN ur.usuario u INNER JOIN ur.rol r  where r.unidadAdministrativa.id=:departamento  ", params);
    }

    public void editResponsable(UsuarioRol rol, String tipo) {
        UsuarioRol rolEdit = (UsuarioRol) ventanillaService.updateEntity(rol);;
        if (rolEdit != null) {
            JsfUtil.addInformationMessage("Responsable departamento", "Datos actualizados");
        } else {
            JsfUtil.addErrorMessage("Responsable departamento", "Datos no actualizados intente nuevamente");
            rol.setAsistente(Boolean.FALSE);
        }

        JsfUtil.update("mainForm");
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public List<UnidadAdministrativa> getDepartamentos() {
        return departamentos;
    }

    public void setDepartamentos(List<UnidadAdministrativa> departamentos) {
        this.departamentos = departamentos;
    }

    public UnidadAdministrativa getDepartamento() {
        return departamento;
    }

    public void setDepartamento(UnidadAdministrativa departamento) {
        this.departamento = departamento;
    }

    public List<UsuarioRol> getListRolUsuario() {
        return listRolUsuario;
    }

    public void setListRolUsuario(List<UsuarioRol> listRolUsuario) {
        this.listRolUsuario = listRolUsuario;
    }

    //</editor-fold>
}
