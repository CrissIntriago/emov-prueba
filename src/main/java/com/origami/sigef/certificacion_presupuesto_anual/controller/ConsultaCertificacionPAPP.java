/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.certificacion_presupuesto_anual.controller;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.ProductoProceso;
import com.origami.sigef.common.entities.Usuarios;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ProductoProcesoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "consultaCertificadoPAPPView")
@ViewScoped
public class ConsultaCertificacionPAPP implements Serializable {

    @Inject
    private PappProcesoService pappService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private MasterCatalogoService masterCatalogoService;

    @Inject
    private ProductoProcesoService productoProcesoService;

    private List<MasterCatalogo> periodos;
    private List<PappProceso> listaPlanPappConsulta;
    private OpcionBusqueda busqueda;
    private CatalogoItem catalogoItem3;
    private MasterCatalogo planProgramaProyecto;
    private CatalogoItem catalogoItem2;

    @PostConstruct
    public void init() {
        catalogoItem2 = catalogoItemService.getEstadoRol("PRORECHACT");
        busqueda = new OpcionBusqueda();
        catalogoItem3 = catalogoItemService.getEstadoRol("PROEMIACT");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        actualizarConsulta();
    }

    public void actualizarConsulta() {
        planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
        listaPlanPappConsulta = pappService.getListConsulta(busqueda.getAnio(), catalogoItem3);
    }

    public void generarCertificado(PappProceso plan) {
        serveltSession.addParametro("id_plan", plan.getId());
        serveltSession.addParametro("user_solicita", usuarioSolicita(plan));
        serveltSession.addParametro("num_tramite", plan.getNumeroTramite().intValue());
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.planificacion));
        serveltSession.addParametro("plan_identificacion", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("plan_nombre", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("plan_cargo", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.setNombreReporte("certificadoPAPP");
        serveltSession.setNombreSubCarpeta("CertificadoPAPP");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public String usuarioSolicita(PappProceso plan) {
        Usuarios user = clienteService.getUsuarioServidor(plan.getUsuarioSolicita());
        if (user != null) {
            return user.getFuncionario().getPersona().getApellido() + " " + user.getFuncionario().getPersona().getNombre();
        } else {
            return plan.getUsuarioSolicita();
        }
    }

    public void anulacion(PappProceso pr) {
        pr.setEstadoProceso(catalogoItem2);
        pappService.edit(pr);
        List<ProductoProceso> result = pappService.getListaProductoProceso(pr.getNumeroTramite());
        if (result != null && !result.isEmpty()) {
            for (ProductoProceso item : result) {
                item.setEstadoProceso(catalogoItem2);
                item.setEstado(new CatalogoItem(415L));
                productoProcesoService.edit(item);
            }
        }

        JsfUtil.addSuccessMessage("", "La Transacción se realizó con exito");
    }

    public List<PappProceso> getListaPlanPappConsulta() {
        return listaPlanPappConsulta;
    }

    public void setListaPlanPappConsulta(List<PappProceso> listaPlanPappConsulta) {
        this.listaPlanPappConsulta = listaPlanPappConsulta;
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

    public MasterCatalogo getPlanProgramaProyecto() {
        return planProgramaProyecto;
    }

    public void setPlanProgramaProyecto(MasterCatalogo planProgramaProyecto) {
        this.planProgramaProyecto = planProgramaProyecto;
    }

}
