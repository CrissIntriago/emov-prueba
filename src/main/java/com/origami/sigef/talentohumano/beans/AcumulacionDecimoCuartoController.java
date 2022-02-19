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
@Named(value = "AcuDecimoCuartoView")
@ViewScoped
public class AcumulacionDecimoCuartoController implements Serializable {

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

    private AcumulacionFondoReserva decimoCuarto;
    private LazyModel<AcumulacionFondoReserva> lazy;
    private Short periodo;
    private String cedula;
    private CatalogoItem decimoCuartoItem;
    private ParametrosTalentoHumano valoPrametro;
    private Date fechaIniOld;
    private Date fechaFinOld;
    private Date fechaIniNew;
    private Date fechaFinNew;

    @Inject
    private ValoresDistributivoService valorService;
    private List<ValoresDistributivo> valores;

    @PostConstruct
    public void init() {
        cedula = "";
        decimoCuarto = new AcumulacionFondoReserva();
        decimoCuarto.setServidor(new Servidor());
        decimoCuarto.setValorParametrizado(new ParametrosTalentoHumano());
        decimoCuartoItem = catalogoItemService.getEstadoRol("ACU-DECIMO-4to");
        valoPrametro = parametroService.valorParametroTipo("DC");
        lazy = new LazyModel<>(AcumulacionFondoReserva.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("estadoVigente", true);
        lazy.getFilterss().put("tipoAcumulacion", decimoCuartoItem);
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
        periodo = Utils.getAnio(new Date()).shortValue();
    }

    public void guardar() {
        boolean edit = this.decimoCuarto.getId() != null;
        List<AcumulacionFondoReserva> lista = acumulacionService.getListAcumulacionRubro(decimoCuartoItem, periodo);
        if (decimoCuarto.getServidor() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Servidor Público.");
            return;
        }
        if (this.decimoCuarto.getFechaInicio() == null) {
            JsfUtil.addWarningMessage("Información", "La fecha de Inicio requerida");
            return;
        }
        if (this.decimoCuarto.getFechaFin() == null) {
            JsfUtil.addWarningMessage("Información", "La fecha de Fin requerida");
            return;
        }
        if (this.decimoCuarto.getFechaInicio().compareTo(decimoCuarto.getFechaFin()) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de Inico no puede ser mayor a la fecha Fin");
            return;
        }
        if (this.decimoCuarto.getFechaFin().compareTo(decimoCuarto.getFechaInicio()) < 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de Fin no puede ser menor a la fecha Inicio");
            return;
        }
        if (this.decimoCuarto.getServidor().getFechaIngreso().compareTo(decimoCuarto.getFechaInicio()) > 0) {
            JsfUtil.addWarningMessage("Información", "La fecha de de ingreso del Servidor no puede ser menor a la fecha Inicio");
            return;
        }
        if (!edit) {
            for (AcumulacionFondoReserva a : lista) {
                if (decimoCuarto.getServidor().equals(a.getServidor())) {
                    if (a.getFechaFin() == null) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "Servidor ya se encuentra registrado con fecha Clausura no definida.");
                        return;
                    }
                    if (decimoCuarto.getFechaInicio().compareTo(a.getFechaInicio()) < 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha de INICIO no puede ser menor al registro existente: ");
                        return;
                    }
                    if (decimoCuarto.getFechaInicio().compareTo(a.getFechaInicio()) == 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha de INICIO no puede ser igual al registro existente: ");
                        return;
                    }
                    if (decimoCuarto.getFechaFin().compareTo(a.getFechaFin()) < 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha FIN no puede ser menor al registro existente: ");
                        return;
                    }
                    if (decimoCuarto.getFechaFin().compareTo(a.getFechaFin()) == 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha FIN no puede ser igual al registro existente: ");
                        return;
                    }
                    if (decimoCuarto.getFechaInicio().compareTo(a.getFechaFin()) < 0) {
                        JsfUtil.addWarningMessage("Registro de Servidor", "La fecha Inicio no puede ser meno a la fecha FIN al registro existente: ");
                        return;
                    }
                }
            }
        }
        if (edit) {
            acumulacionService.edit(decimoCuarto);
        } else {
            decimoCuarto.setValorParametrizado(valoPrametro);
            this.decimoCuarto = acumulacionService.create(decimoCuarto);
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

    public boolean buscarRubro(AcumulacionFondoReserva decimo) {
        valores = new ArrayList<>();
        valores = valorService.findvaloresDistributivoXperiodo(decimo.getServidor().getDistributivo(), periodo);
        if (!valores.isEmpty()) {
            for (ValoresDistributivo item : valores) {
                if ("DC".equals(item.getValoresParametrizados().getTipo().getCodigo())) {
                    return true;
                }
            }
        }
        return false;
    }

    public Date fechaCierre() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date fecha = new Date();
        int anio = periodo.intValue() + 1;
        String dia = "" + obtenerUltimoDiaMes(anio, 1) + "/02/" + anio;
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

    public void eliminar(AcumulacionFondoReserva acu) {
        acu.setEstado(Boolean.FALSE);
        acumulacionService.edit(acu);
    }

    public void resetValue() {
        this.cedula = "";
        this.decimoCuarto = new AcumulacionFondoReserva();
    }

    public void form(AcumulacionFondoReserva dec) {
        if (dec != null) {
            this.decimoCuarto = dec;
            this.cedula = dec.getServidor().getPersona().getIdentificacion();
            this.decimoCuarto.setFechaModificacion(new Date());
            this.decimoCuarto.setUsuarioModifica(userSession.getNameUser());
        } else {
            this.decimoCuarto = new AcumulacionFondoReserva();
            this.decimoCuarto.setFechaCreacion(new Date());
            this.decimoCuarto.setFechaFin(fechaCierre());
            this.decimoCuarto.setPeriodo(periodo);
            this.decimoCuarto.setUsuarioCreacion(userSession.getName());
            this.decimoCuarto.setTipoAcumulacion(decimoCuartoItem);
            this.decimoCuarto.setDerecho(Boolean.TRUE);
        }
        PrimeFaces.current().executeScript("PF('decimoDialog').show()");
        PrimeFaces.current().ajax().update("frmdecimo");
    }

    public void form() {
        Calendar cal = Calendar.getInstance();
        int mes = cal.get(Calendar.MONTH);
        int dia = cal.get(Calendar.DAY_OF_MONTH);
        int anio = cal.get(Calendar.YEAR);
        if (mes == 0 || mes == 1) {
            fechaIniOld = TalentoHumano.ParseFecha("1/03/" + (anio - 1), "dd/MM/yyyy");
            fechaFinOld = TalentoHumano.ParseFecha(TalentoHumano.obtenerUltimoDiaMes(anio, 1) + "/02/" + (anio), "dd/MM/yyyy");
            fechaIniNew = TalentoHumano.ParseFecha("1/03/" + anio, "dd/MM/yyyy");
            fechaIniOld = TalentoHumano.ParseFecha(TalentoHumano.obtenerUltimoDiaMes(anio, 1) + "/02/" + (anio + 1), "dd/MM/yyyy");
        } else {
            fechaIniOld = TalentoHumano.ParseFecha("1/03/" + (anio), "dd/MM/yyyy");
            fechaFinOld = TalentoHumano.ParseFecha(TalentoHumano.obtenerUltimoDiaMes((anio + 1), 1) + "/02/" + (anio + 1), "dd/MM/yyyy");
            fechaIniNew = TalentoHumano.ParseFecha("1/03/" + (anio + 1), "dd/MM/yyyy");
            fechaFinNew = TalentoHumano.ParseFecha(TalentoHumano.obtenerUltimoDiaMes((anio + 2), 1) + "/02/" + (anio + 2), "dd/MM/yyyy");
        }
        PrimeFaces.current().executeScript("PF('dialogCopia').show()");
        PrimeFaces.current().ajax().update(":formDecimo");
    }

    public void generarCopia() {
        int executeUpdate, actualizar;
        List<AcumulacionFondoReserva> listAux = acumulacionService.getListAcumulacion(decimoCuartoItem, fechaIniOld, fechaFinOld);
        if (listAux == null || listAux.isEmpty()) {
            JsfUtil.addWarningMessage("Error", "No Existen Datos en el Periodo a Copiar");
            return;
        }
        executeUpdate = acumulacionService.getcopia(fechaIniOld, fechaFinOld, fechaIniNew, fechaFinNew, decimoCuartoItem, userSession.getNameUser());
        actualizar = acumulacionService.updateAcumulacion(fechaIniOld, fechaFinOld, decimoCuartoItem, Boolean.FALSE, userSession.getNameUser());
        if (executeUpdate > 0 && actualizar > 0) {
            JsfUtil.addSuccessMessage("Datos", "Los Datos Fueron cargados Correctamente");
        }
        PrimeFaces.current().executeScript("PF('dialogCopia').hide()");
        PrimeFaces.current().ajax().update(":formDecimo");
    }

    public void buscar() {
        Servidor serv = distributivoService.getServidorAnio(periodo, this.cedula);
        if (serv != null) {
            this.decimoCuarto.setServidor(serv);
        } else {
            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "400", null);
        }

    }

    public void handleCloseForm(CloseEvent event) {
        this.cedula = "";
        this.decimoCuarto = new AcumulacionFondoReserva();
        PrimeFaces.current().ajax().update("formdecimo");
    }

    public void selectData(SelectEvent evt) {
        this.decimoCuarto.setServidor((Servidor) evt.getObject());
        this.cedula = decimoCuarto.getServidor().getPersona().getIdentificacion();
    }

    public AcumulacionFondoReserva getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(AcumulacionFondoReserva decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
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

    public CatalogoItem getDecimoCuartoItem() {
        return decimoCuartoItem;
    }

    public void setDecimoCuartoItem(CatalogoItem decimoCuartoItem) {
        this.decimoCuartoItem = decimoCuartoItem;
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
