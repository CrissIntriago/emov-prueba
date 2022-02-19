/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans.biometrico;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.biotime.MarcacionService;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Angel Navarro
 */
@Named
@ViewScoped
public class ReporteMensual implements Serializable {

    private static final Logger LOG = Logger.getLogger(ReporteMensual.class.getName());

    @Inject
    private ServidorService servidorService;
    @Inject
    private DiasLaboradoService diasLaboradoService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private MarcacionService eventoBiometrico;

    private LazyModel<DiasLaborado> lazy;
    private List<Servidor> listaServidores;
    private List<Servidor> listaServidoresMostrar;
    private List<MasterCatalogo> periodos;
    private DiasLaborado diaLaborado;
    private OpcionBusqueda busqueda;
    private List<TipoRol> rolesMensuales;
    private List<DiasLaborado> listServidoresDataTable;
    private TipoRol tipoRol;
    private CatalogoItem registrado;
    private boolean disabledbtnBiometrico;

    @PostConstruct
    public void init() {
        if (!JsfUtil.isAjaxRequest()) {
            busqueda = new OpcionBusqueda();
            tipoRol = new TipoRol();
            diaLaborado = new DiasLaborado();
            diaLaborado.setTipoRol(new TipoRol());
            registrado = catalogoItemService.getEstadoRol("registrado-rol");
            periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
            rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
            listServidoresDataTable = new ArrayList<>();
            listaServidores = servidorService.listServidoresPeriodo(busqueda.getAnio());
            disabledbtnBiometrico = true;
        }
    }

    public void mesesRoles() {
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
    }

    public void buscar() {
        listServidoresDataTable = new ArrayList<>();
        lazy = new LazyModel<>(DiasLaborado.class);
        if (diaLaborado.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Error", "Debe de Seleccionar un mes");
            return;
        }
        if (busqueda.getAnio() == null) {
            JsfUtil.addWarningMessage("Error", "Debe de Seleccionar un Período");
            return;
        }
        listServidoresDataTable = diasLaboradoService.getListaDiasLaborado(busqueda.getAnio(), diaLaborado.getTipoRol().getMes().getDescripcion());
        tipoRol = diaLaborado.getTipoRol();
        if (listServidoresDataTable.isEmpty()) {
            for (Servidor s : listaServidores) {
                diaLaborado = new DiasLaborado();
                diaLaborado.setTipoRol(new TipoRol());
                diaLaborado.setServidor(new Servidor());
                diaLaborado.setFechaCreacion(new Date());
                diaLaborado.setUsuarioCreacion(userSession.getNameUser());
                diaLaborado.setServidor(s);
                diaLaborado.setPeriodo(busqueda.getAnio());
                diaLaborado.setTipoRol(tipoRol);
                diaLaborado.setMes(tipoRol.getMes().getDescripcion());
                if (s.getFechaSalida() == null) {
                    if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol)) {
                        diasLaboradoService.create(diaLaborado);
                    }
                }
                if (s.getFechaIngreso() != null && s.getFechaSalida() != null) {
                    if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol) && TalentoHumano.validarFechaFin(s.getFechaSalida(), tipoRol)) {
                        diasLaboradoService.create(diaLaborado);
                    }
                }
            }
        }
        disabledBtnBiometrico(tipoRol);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", tipoRol);
        lazy.getFilterss().put("periodo", busqueda.getAnio());
        lazy.getSorteds().put("servidor.persona.apellido", "DESC");
        lazy.setDistinct(false);
    }

    public void obtenerDatosBiometrico() {
        try {
            diasLaboradoService.actualizarDiasDesdeBiometrico(tipoRol, busqueda.getAnio());
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error al obtener datos del biometrico", e);
        }
    }

    public void guardarDia(DiasLaborado dia) {
        try {
            if (dia.getDias().intValue() < 0) {
                JsfUtil.addSuccessMessage("Error", "La cantidad de días laborados no valido");
                return;
            }
            dia.setFechaModificacion(new Date());
            dia.setUsuarioModifica(userSession.getNameUser());
            diasLaboradoService.edit(dia);
            JsfUtil.addSuccessMessage("Día Laborado", "Día Guardado con Exito");
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Error el guardar dia", e);
        }
    }

    public int diaMax(DiasLaborado dia) {
        return TalentoHumano.getUltimoDiaMes(dia.getTipoRol());
    }

    public void grabarDias(boolean var) {
        listServidoresDataTable = diasLaboradoService.getListaDiasLaborado(busqueda.getAnio(), diaLaborado.getTipoRol().getMes().getDescripcion());
        if (listServidoresDataTable.isEmpty()) {
            JsfUtil.addWarningMessage("Días Laborables", "No cuenta con registros para grabar");
            return;
        }
        if (var) {
            tipoRol.setDiasLaborados(Boolean.TRUE);
        } else {
            tipoRol.setDiasLaborados(Boolean.FALSE);
        }
        String mes = diaLaborado.getTipoRol().getMes().getDescripcion().toUpperCase();
        tipoRolService.edit(tipoRol);
        tipoRol = tipoRolService.rolEncabezado(busqueda.getAnio(), mes, diaLaborado.getTipoRol().getTipoRol());
        JsfUtil.addSuccessMessage("Días Laborables", var ? "Días grabados con éxito" : "Días habilitados para editar");
    }

    public boolean disabledCellEdit() {
        String mes = diaLaborado.getTipoRol().getMes().getDescripcion().toUpperCase();
        tipoRol = tipoRolService.rolEncabezado(busqueda.getAnio(), mes, diaLaborado.getTipoRol().getTipoRol());

        if (tipoRol != null) {
            if (tipoRol.getDiasLaborados()) {
                return true;
            }
        }
        return false;
    }

    public void disabledBtnBiometrico(TipoRol t) {
        disabledbtnBiometrico = true;
        if (t.getId() != null) {
            if ("registrado-rol".equals(t.getEstadoAprobacion().getCodigo()) && "rol_general".equals(t.getTipoRol().getCodigo())) {
                setDisabledbtnBiometrico(false);
            }
        }
    }

    public void eliminar(DiasLaborado dia) {
        if (dia.getTipoRol().getEstadoAprobacion().equals(registrado)) {
            dia.setEstado(Boolean.FALSE);
            diasLaboradoService.edit(dia);
        } else {
            JsfUtil.addWarningMessage("Eliminar Rol", "No puede Eliminar con Roles Aprobados o Pagados");
        }
    }

    public void openDlgServidor() {
        listaServidoresMostrar = new ArrayList<>();
        if (tipoRol.getTipoRol().getCodigo().equals("rol_adicional")) {
            listaServidoresMostrar = servidorService.getServidorXmesNotInDiaLaboradoMes("rol_general", tipoRol);
        }
        if (tipoRol.getTipoRol().getCodigo().equals("rol_general")) {
            listaServidoresMostrar = servidorService.getServidorXmesNotInDiaLaboradoMes("rol_adicional", tipoRol);
        }

        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').show()");
        PrimeFaces.current().ajax().update("formServidorPublico");
        PrimeFaces.current().ajax().update(":servidorPublicoDialog");

    }

    public void selectServidor(Servidor s) {
        diaLaborado = new DiasLaborado();
        diaLaborado.setTipoRol(new TipoRol());
        diaLaborado.setServidor(new Servidor());
        diaLaborado.setFechaCreacion(new Date());
        diaLaborado.setUsuarioCreacion(userSession.getNameUser());
        diaLaborado.setServidor(s);
        diaLaborado.setPeriodo(busqueda.getAnio());
        diaLaborado.setTipoRol(tipoRol);
        diaLaborado.setMes(tipoRol.getMes().getDescripcion());
        if (s.getFechaSalida() == null) {
            if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol)) {
                diasLaboradoService.create(diaLaborado);
                JsfUtil.addInformationMessage("Información", "Servidor Público agregado a período Correctamente.");
            }
        }
        if (s.getFechaIngreso() != null && s.getFechaSalida() != null) {
            if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol) && TalentoHumano.validarFechaFin(s.getFechaSalida(), tipoRol)) {
                diasLaboradoService.create(diaLaborado);
                JsfUtil.addInformationMessage("Información", "Servidor Público agregado a período Correctamente.");
            }
        }
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
    }

    public List<Servidor> getListaServidores() {
        return listaServidores;
    }

    public void setListaServidores(List<Servidor> listaServidores) {
        this.listaServidores = listaServidores;
    }

    public DiasLaboradoService getDiasLaboradoService() {
        return diasLaboradoService;
    }

    public void setDiasLaboradoService(DiasLaboradoService diasLaboradoService) {
        this.diasLaboradoService = diasLaboradoService;
    }

    public DiasLaborado getDiaLaborado() {
        return diaLaborado;
    }

    public void setDiaLaborado(DiasLaborado diaLaborado) {
        this.diaLaborado = diaLaborado;
    }

    public LazyModel<DiasLaborado> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<DiasLaborado> lazy) {
        this.lazy = lazy;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<DiasLaborado> getListServidoresDataTable() {
        return listServidoresDataTable;
    }

    public void setListServidoresDataTable(List<DiasLaborado> listServidoresDataTable) {
        this.listServidoresDataTable = listServidoresDataTable;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public boolean isDisabledbtnBiometrico() {
        return disabledbtnBiometrico;
    }

    public void setDisabledbtnBiometrico(boolean disabledbtnBiometrico) {
        this.disabledbtnBiometrico = disabledbtnBiometrico;
    }

    public List<Servidor> getListaServidoresMostrar() {
        return listaServidoresMostrar;
    }

    public void setListaServidoresMostrar(List<Servidor> listaServidoresMostrar) {
        this.listaServidoresMostrar = listaServidoresMostrar;
    }

}
