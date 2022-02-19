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
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.AcumulacionFondoReservaService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.CloseEvent;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "AcuDecimoTercerView")
@ViewScoped
public class AcumulacionDecimoTerceroController implements Serializable {

    @Inject
    private AcumulacionFondoReservaService acumulacionService;
    @Inject
    private UserSession userSession;
    @Inject
    private DistributivoEscalaService distributivoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ValoresDistributivoService valorService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private ValoresRolesService valoresRolsService;

    private AcumulacionFondoReserva decimotercero;
    private LazyModel<AcumulacionFondoReserva> lazy;
    private Short periodo;
    private String cedula;
    private CatalogoItem decimotercerItem;
    private ParametrosTalentoHumano valoPrametro;
    private List<ValoresDistributivo> valores;
    private Date fechaIniOld;
    private Date fechaFinOld;
    private Date fechaIniNew;
    private Date fechaFinNew;

    @PostConstruct
    public void init() {
        cedula = "";
        decimotercero = new AcumulacionFondoReserva();
        decimotercero.setServidor(new Servidor());
        decimotercero.setValorParametrizado(new ParametrosTalentoHumano());
        decimotercerItem = catalogoItemService.getEstadoRol("ACU-DECIMO-3ro");
        valoPrametro = parametroService.valorParametroTipo("DT");
        lazy = new LazyModel<>(AcumulacionFondoReserva.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("estadoVigente", true);
        lazy.getFilterss().put("tipoAcumulacion", decimotercerItem);
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
        periodo = Utils.getAnio(new Date()).shortValue();
    }

    public void guardar() {
        boolean validar = false;
        boolean edit = this.decimotercero.getId() != null;
        List<AcumulacionFondoReserva> lista = acumulacionService.getListAcumulacionRubro(decimotercerItem, periodo);
        if (decimotercero.getServidor() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Servidor Público.");
            return;
        }
        if (this.decimotercero.getFechaInicio() == null) {
            JsfUtil.addWarningMessage("Información", "La fecha de Inicio requerida");
            return;
        }
        if (this.decimotercero.getFechaFin() == null) {
            JsfUtil.addWarningMessage("Información", "La fecha de Fin requerida");
            return;
        }
        if (this.decimotercero.getFechaInicio().compareTo(decimotercero.getFechaFin()) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de Inico no puede ser mayor a la fecha Fin");
            return;
        }
        if (this.decimotercero.getFechaFin().compareTo(decimotercero.getFechaInicio()) < 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de Fin no puede ser menor a la fecha Inicio");
            return;
        }
        if (this.decimotercero.getServidor().getFechaIngreso().compareTo(decimotercero.getFechaInicio()) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de de ingreso del Servidor no puede ser menor a la fecha Inicio");
            return;
        }
        if (!edit) {
            for (AcumulacionFondoReserva a : lista) {
                if (decimotercero.getServidor().equals(a.getServidor())) {
                    if (a.getFechaFin() == null) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "Servidor ya se encuentra registrado con fecha Clausura no definida.");
                        return;
                    }
                    if (decimotercero.getFechaInicio().compareTo(a.getFechaInicio()) < 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha de INICIO no puede ser menor al registro existente: ");
                        return;
                    }
                    if (decimotercero.getFechaInicio().compareTo(a.getFechaInicio()) == 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha de INICIO no puede ser igual al registro existente: ");
                        return;
                    }
                    if (decimotercero.getFechaFin().compareTo(a.getFechaFin()) < 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha FIN no puede ser menor al registro existente: ");
                        return;
                    }
                    if (decimotercero.getFechaFin().compareTo(a.getFechaFin()) == 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha FIN no puede ser igual al registro existente: ");
                        return;
                    }
                    if (decimotercero.getFechaInicio().compareTo(a.getFechaFin()) < 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha Inicio no puede ser meno a la fecha FIN al registro existente: ");
                        return;
                    }
                }
            }
        }
        if (edit) {
            acumulacionService.edit(decimotercero);
        } else {
            decimotercero.setValorParametrizado(valoPrametro);
            this.decimotercero = acumulacionService.create(decimotercero);
        }
        resetValue();
        PrimeFaces.current().executeScript("PF('decimoDialog').hide()");
        PrimeFaces.current().ajax().update("frmdecimo");
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

    public Date fechaCierre() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        int anio = periodo.intValue();
        String dia = "" + obtenerUltimoDiaMes(anio, 10) + "/11/" + anio;
        try {
            fecha = sdf.parse(dia);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return fecha;
    }

    public int obtenerUltimoDiaMes(int anio, int mes) {
        Calendar cal = Calendar.getInstance();
        cal.set(anio, mes, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    public boolean buscarRubro(AcumulacionFondoReserva decimo) {
        valores = new ArrayList<>();
        valores = valorService.findvaloresDistributivoXperiodo(decimo.getServidor().getDistributivo(), periodo);
        if (!valores.isEmpty()) {
            for (ValoresDistributivo item : valores) {
                if ("DT".equals(item.getValoresParametrizados().getTipo().getCodigo())) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean buscarServidor(List<AcumulacionFondoReserva> lista, AcumulacionFondoReserva servidor) {
        return lista.stream().anyMatch((p) -> (servidor.getServidor().equals(p.getServidor())));
    }

    public void eliminar(AcumulacionFondoReserva acu) {
        acu.setEstado(Boolean.FALSE);
        acumulacionService.edit(acu);
    }

    public void resetValue() {
        this.cedula = "";
        this.decimotercero = new AcumulacionFondoReserva();
    }

    public void form(AcumulacionFondoReserva dec) {
        if (dec != null) {
            this.decimotercero = dec;
            this.cedula = dec.getServidor().getPersona().getIdentificacion();
            this.decimotercero.setFechaModificacion(new Date());
            this.decimotercero.setUsuarioModifica(userSession.getNameUser());
        } else {
            this.decimotercero = new AcumulacionFondoReserva();
            this.decimotercero.setFechaCreacion(new Date());
            this.decimotercero.setFechaFin(fechaCierre());
            this.decimotercero.setDerecho(Boolean.TRUE);
            this.decimotercero.setPeriodo(periodo);
            this.decimotercero.setUsuarioCreacion(userSession.getName());
            this.decimotercero.setTipoAcumulacion(decimotercerItem);
        }
        PrimeFaces.current().executeScript("PF('decimoDialog').show()");
        PrimeFaces.current().ajax().update("frmdecimo");
    }

    public void buscar() {
        Servidor serv = distributivoService.getServidorAnio(periodo, this.cedula);
        if (serv != null) {
            this.decimotercero.setServidor(serv);
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "400", null);
        }
    }

    public void generarCopia() {
        int executeUpdate, actualizar;
        List<AcumulacionFondoReserva> listAux = acumulacionService.getListAcumulacion(decimotercerItem, fechaIniOld, fechaFinOld);
        if (listAux == null || listAux.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "No Existen Datos en el Periodo a Copiar");
            return;
        }
        executeUpdate = acumulacionService.getcopia(fechaIniOld, fechaFinOld, fechaIniNew, fechaFinNew, decimotercerItem, userSession.getNameUser());
        actualizar = acumulacionService.updateAcumulacion(fechaIniOld, fechaFinOld, decimotercerItem, Boolean.FALSE, userSession.getNameUser());
        if (executeUpdate > 0 && actualizar > 0) {
            JsfUtil.addSuccessMessage("Datos", "Los Datos Fueron cargados Correctamente");
        }
        PrimeFaces.current().executeScript("PF('dialogCopia').hide()");
        PrimeFaces.current().ajax().update(":formDecimo");
    }

    public void selectData(SelectEvent evt) {
        this.decimotercero.setServidor((Servidor) evt.getObject());
        this.cedula = decimotercero.getServidor().getPersona().getIdentificacion();
    }

    public void handleCloseForm(CloseEvent event) {
        this.cedula = "";
        this.decimotercero = new AcumulacionFondoReserva();
        PrimeFaces.current().ajax().update("formdecimo");
    }

    public void form() {
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int anio = cal.get(Calendar.YEAR);
        System.out.println("mes --> " + mes);
        if (mes == 11) {
            fechaIniOld = TalentoHumano.ParseFecha("1/12/" + (anio-1), "dd/MM/yyyy");
            fechaFinOld = TalentoHumano.ParseFecha("30/11/" + (anio), "dd/MM/yyyy");
            fechaIniNew = TalentoHumano.ParseFecha("1/12/" + anio, "dd/MM/yyyy");
            fechaIniOld = TalentoHumano.ParseFecha("30/11/" + (anio + 1), "dd/MM/yyyy");
        } else {
            fechaIniOld = TalentoHumano.ParseFecha("1/12/" + (anio - 1), "dd/MM/yyyy");
            fechaFinOld = TalentoHumano.ParseFecha("30/11/" + anio, "dd/MM/yyyy");
            fechaIniNew = TalentoHumano.ParseFecha("1/12/" + (anio), "dd/MM/yyyy");
            fechaFinNew = TalentoHumano.ParseFecha("30/11/" + (anio + 1), "dd/MM/yyyy");
        }
        PrimeFaces.current().executeScript("PF('dialogCopia').show()");
        PrimeFaces.current().ajax().update(":formDecimo");
    }

    public AcumulacionFondoReserva getDecimotercero() {
        return decimotercero;
    }

    public void setDecimotercero(AcumulacionFondoReserva decimotercero) {
        this.decimotercero = decimotercero;
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

    public CatalogoItem getDecimotercerItem() {
        return decimotercerItem;
    }

    public void setDecimotercerItem(CatalogoItem decimotercerItem) {
        this.decimotercerItem = decimotercerItem;
    }

    public List<ValoresDistributivo> getValores() {
        return valores;
    }

    public void setValores(List<ValoresDistributivo> valores) {
        this.valores = valores;
    }

    public Date getFechaIniOld() {
        return fechaIniOld;
    }

    public void setFechaIniOld(Date fechaIniOld) {
        this.fechaIniOld = fechaIniOld;
    }

    public Date getFechaFinOld() {
        return fechaFinOld;
    }

    public void setFechaFinOld(Date fechaFinOld) {
        this.fechaFinOld = fechaFinOld;
    }

    public Date getFechaIniNew() {
        return fechaIniNew;
    }

    public void setFechaIniNew(Date fechaIniNew) {
        this.fechaIniNew = fechaIniNew;
    }

    public Date getFechaFinNew() {
        return fechaFinNew;
    }

    public void setFechaFinNew(Date fechaFinNew) {
        this.fechaFinNew = fechaFinNew;
    }

}
