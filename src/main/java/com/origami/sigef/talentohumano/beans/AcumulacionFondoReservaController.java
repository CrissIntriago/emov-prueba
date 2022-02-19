/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.services.AcumulacionFondoReservaService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "fondosView")
@ViewScoped
public class AcumulacionFondoReservaController implements Serializable {

    @Inject
    private AcumulacionFondoReservaService acumulacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private DistributivoEscalaService distributivoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private ValoresRolesService valoresRolsService;

    private AcumulacionFondoReserva fondoReserva;
    private LazyModel<AcumulacionFondoReserva> lazy;
    private Short periodo;
    private String cedula;
    private CatalogoItem fondoReservaItem;
    private ParametrosTalentoHumano valoPrametro;

    @Inject
    private ValoresDistributivoService valorService;
    private List<ValoresDistributivo> valores;

    @PostConstruct
    public void init() {
        cedula = "";
        fondoReserva = new AcumulacionFondoReserva();
        fondoReserva.setServidor(new Servidor());
        fondoReserva.setValorParametrizado(new ParametrosTalentoHumano());
        fondoReservaItem = catalogoItemService.getEstadoRol("ACU-FONDOS-RESERVA");
        valoPrametro = parametroService.valorParametroTipo("FR");
        lazy = new LazyModel<>(AcumulacionFondoReserva.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoAcumulacion", fondoReservaItem);
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
        periodo = Utils.getAnio(new Date()).shortValue();
    }

    public void guardar() {
        boolean edit = fondoReserva.getId() != null;
        List<AcumulacionFondoReserva> lista = acumulacionService.getListAcumulacionRubro(fondoReservaItem, periodo);
        if (fondoReserva.getServidor() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Servidor Público.");
            return;
        }
        if (buscarServidor(lista, fondoReserva, edit)) {
            JsfUtil.addWarningMessage("Registro de Servidor", "El Servidor ya se encuentra registrado");
            return;
        }
        if (this.fondoReserva.getFechaInicio() == null) {
            JsfUtil.addWarningMessage("Información", "La fecha de Inicio es requerida");
            return;
        }
        if (fondoReserva.getFechaFin() != null) {
            if (this.fondoReserva.getFechaInicio().compareTo(fondoReserva.getFechaFin()) > 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de Inico no puede ser mayor a la fecha Fin");
                return;
            }
            if (this.fondoReserva.getFechaFin().compareTo(fondoReserva.getFechaInicio()) < 0) {
                JsfUtil.addWarningMessage("Información", "La fecha de Fin no puede ser menor a la fecha Inicio");
                return;
            }
        }
        if (this.fondoReserva.getServidor().getFechaIngreso().compareTo(fondoReserva.getFechaInicio()) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de de ingreso del Servidor no puede ser menor a la fecha Inicio");
            return;
        }
        if (edit) {
            acumulacionService.edit(fondoReserva);
        } else {
            fondoReserva.setTipoAcumulacion(fondoReservaItem);
            fondoReserva.setValorParametrizado(valoPrametro);
            fondoReserva = acumulacionService.create(fondoReserva);
        }
        resetValue();
        PrimeFaces.current().executeScript("PF('fondosDialog').hide()");
        PrimeFaces.current().ajax().update("frmFondos");
    }

    public boolean buscarRubroAsignado(Servidor s, ParametrosTalentoHumano ph, Short anio) {
        List<ValoresRoles> lista = valoresRolsService.FindValoresXServidor(s, anio);
        for (ValoresRoles vr : lista) {
            if (vr.getValorParametrizable().equals(ph)) {
                return true;
            }
        }
        return false;
    }

    public boolean buscarServidor(List<AcumulacionFondoReserva> lista, AcumulacionFondoReserva servidor, boolean edit) {
        if (!lista.isEmpty()) {
            if (servidor.getServidor() != null) {
                return lista.stream().anyMatch((p) -> (servidor.getServidor().equals(p.getServidor()) && !edit && p.getFechaInicio() == null));
            }
        }
        return false;
    }

    public boolean buscarRubro(AcumulacionFondoReserva fondo) {
        valores = new ArrayList<>();
        if (fondo.getServidor() != null) {
            valores = valorService.findvaloresDistributivoXperiodo(fondo.getServidor().getDistributivo(), periodo);
        }
        if (!valores.isEmpty()) {
            for (ValoresDistributivo item : valores) {
                if ("FR".equals(item.getValoresParametrizados().getTipo().getCodigo())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void eliminar(AcumulacionFondoReserva acu) {
        acu.setEstado(Boolean.FALSE);
        acumulacionService.edit(acu);
    }

    public void resetValue() {
        this.fondoReserva = new AcumulacionFondoReserva();
        this.cedula = "";
    }

    public void form(AcumulacionFondoReserva fondo) {
        if (fondo != null) {
            fondoReserva = fondo;
            cedula = fondo.getServidor().getPersona().getIdentificacion();
            fondoReserva.setFechaModificacion(new Date());
            fondoReserva.setUsuarioModifica(userSession.getNameUser());
        } else {
            fondoReserva = new AcumulacionFondoReserva();
            fondoReserva.setFechaCreacion(new Date());
            fondoReserva.setPeriodo(periodo);
            fondoReserva.setUsuarioCreacion(userSession.getName());
        }
        PrimeFaces.current().executeScript("PF('fondosDialog').show()");
        PrimeFaces.current().ajax().update("frmFondos");
    }

    public void buscar() {
        Servidor serv = distributivoService.getServidorAnio(periodo, this.cedula);
        if (serv != null) {
            fondoReserva.setServidor(serv);
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "400", null);
        }

    }

    public void selectData(SelectEvent evt) {
        fondoReserva.setServidor((Servidor) evt.getObject());
        this.cedula = fondoReserva.getServidor().getPersona().getIdentificacion();
    }

    public AcumulacionFondoReserva getFondoReserva() {
        return fondoReserva;
    }

    public void setFondoReserva(AcumulacionFondoReserva fondoReserva) {
        this.fondoReserva = fondoReserva;
    }

    public LazyModel<AcumulacionFondoReserva> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<AcumulacionFondoReserva> lazy) {
        this.lazy = lazy;
    }

    public Short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Short periodo) {
        this.periodo = periodo;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

}
