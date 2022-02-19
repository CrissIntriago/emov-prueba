package com.gestionTributaria.Controller;

import com.gestionTributaria.Entities.Reportegeneralmultas;
import com.gestionTributaria.Entities.Reporteindivisualmultas;
import com.origami.sigef.auth.services.RolService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Rol;
import com.origami.sigef.common.entities.UsuarioRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.resource.presupuesto.procesos.services.CatalogoItemServices;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

/**
 *
 * @author ORIGAMIEC
 */
@Named
@ViewScoped
public class ReporteMultasIndividualesMB implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReporteMultasIndividualesMB.class.getName());

    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemServices catalogoItemService;
    @Inject
    private ServletSession ss;
    @Inject
    private ClienteService clienteService;
    private int estado;
    private Date fechaDesde;
    private Date fechaHasta;
    private LazyModel<Reporteindivisualmultas> reporteIndLazy;
    private List<UsuarioRol> usuarioRoles;
    private List<CatalogoItem> listaRoles;
    private CatalogoItem comisario;
    private Reporteindivisualmultas contribuyente;
    private Cliente cliente;
    private Boolean bandera;

    @PostConstruct

    public void init() {
        bandera = false;
        cliente = new Cliente();
        contribuyente = new Reporteindivisualmultas();
        usuarioRoles = new ArrayList<>();
        estado = 1;
        fechaDesde = new Date();
        fechaHasta = new Date();
        usuarioRoles = userSession.getUserRoles();
        for (UsuarioRol x : usuarioRoles) {
            CatalogoItem catalogo = x.getRol().getCategoria();
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("38")) {
                comisario = catalogoItemService.traerCatalogo("CONSTRUCCION");
                bandera = true;
                break;
            }
            if (catalogo.getCodigo().equals("")) {
                bandera = true;
                break;
            }
        }
        reporteIndLazy = new LazyModel<>(Reporteindivisualmultas.class);
        if (bandera == true) {
            reporteIndLazy.addFilter("usuario_ingreso", userSession.getNameUser());
            reporteIndLazy.getFilterss().put("comisaria", comisario.getId());
        } else {
            reporteIndLazy.addFilter("usuario_ingreso", userSession.getNameUser());
            reporteIndLazy.getFilterss().put("comisaria", 0);
        }

    }

    public void reporteContribuyente(Reporteindivisualmultas cont) {
        contribuyente = cont;
        generarReporte();
    }

    public void generarReporte() {
        ss.setNombreReporte("MultaIndividual");
        ss.setNombreSubCarpeta("/GestionTributatia/comisaria/");
        ss.addParametro("comisaria", comisario.getId().intValue());
        ss.addParametro("fechaDesde", fechaDesde);
        ss.addParametro("fechaHasta", fechaHasta);
        ss.addParametro("estadoLiquidacion", estado);
        cliente = clienteService.verificarIdentificacionRuc(contribuyente.getIdentificacion());
        ss.addParametro("contribuyente", cliente.getId().intValue());
        ss.setImprimir(Boolean.FALSE);
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Date getFechaDesde() {
        return fechaDesde;
    }

    public void setFechaDesde(Date fechaDesde) {
        this.fechaDesde = fechaDesde;
    }

    public Date getFechaHasta() {
        return fechaHasta;
    }

    public void setFechaHasta(Date fechaHasta) {
        this.fechaHasta = fechaHasta;
    }

    public LazyModel<Reporteindivisualmultas> getReporteIndLazy() {
        return reporteIndLazy;
    }

    public void setReporteIndLazy(LazyModel<Reporteindivisualmultas> reporteIndLazy) {
        this.reporteIndLazy = reporteIndLazy;
    }

}
