/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.ConnectSQLite.ConnectionSQLite;
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
import com.origami.sigef.talentohumano.model.MarcacionModel;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.biotime.MarcacionService;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
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
 * @author ORIGAMI2
 */
@Named(value = "diasLaboradosView")
@ViewScoped
public class DiasLaboradosController implements Serializable {

    @Inject
    private ServidorService servidorService;
    @Inject
    private DistributivoService distributivoService;
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
    private MarcacionService marcacionService;

    private LazyModel<DiasLaborado> lazy;
    private List<Servidor> listaServidores;
    private List<Servidor> listaServidoresMostrar;
    private List<MasterCatalogo> periodos;
    private DiasLaborado diaLaborado;
    private DiasLaborado diaLaboradoSeleccion;
    private OpcionBusqueda busqueda;
    private List<TipoRol> rolesMensuales;
    private List<DiasLaborado> listServidoresDataTable;
    private TipoRol tipoRol;
    private CatalogoItem registrado;
    private ConnectionSQLite conect;

    private boolean disabledbtnBiometrico;
    private MarcacionModel marcacionModel;
    private List<MarcacionModel> listMarcacionModel;
    private List<Servidor> servidorList;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        tipoRol = new TipoRol();
        diaLaborado = new DiasLaborado();
        diaLaboradoSeleccion = new DiasLaborado();
        diaLaborado.setTipoRol(new TipoRol());
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
        listServidoresDataTable = new ArrayList<>();
        disabledbtnBiometrico = true;
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
        marcacionModel = new MarcacionModel();
        listMarcacionModel = new ArrayList<>();
        conect = new ConnectionSQLite();
    }

    public void mesesRoles() {
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
    }

    public static Date ParseFecha(String fecha) {
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Date fechaDate = null;
        try {
            fechaDate = new Date(formato.parse(fecha).getTime());
        } catch (ParseException ex) {
            System.out.println(ex);
        }
        return fechaDate;
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
        listServidoresDataTable = diasLaboradoService.getDiasxTipoRol(diaLaborado.getTipoRol());
        tipoRol = diaLaborado.getTipoRol();
        if (listServidoresDataTable.isEmpty()) {
            listaServidores = servidorService.listServidoresPeriodo(busqueda.getAnio());
            diasLaboradoService.guardarDiasLaborados(listaServidores, tipoRol);
        }
        disabledBtnBiometrico(tipoRol);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", tipoRol);
        lazy.getFilterss().put("periodo", busqueda.getAnio());
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
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
            e.printStackTrace();
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
            if ("registrado-rol".equals(t.getEstadoAprobacion().getCodigo()) && ("rol_general".equals(t.getTipoRol().getCodigo()) || "rol_adicional".equals(t.getTipoRol().getCodigo()))) {
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
        servidorList = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').show()");
        PrimeFaces.current().ajax().update("formServidorPublico");
        PrimeFaces.current().ajax().update(":servidorPublicoDialog");

    }

    public void servidoresSeleccionados() {
        if (!servidorList.isEmpty()) {
            for (Servidor item : servidorList) {
                selectServidor(item);
            }
            JsfUtil.addInformationMessage("Información", "Servidor/es Público agregado a período Correctamente.");
        } else {
            JsfUtil.addWarningMessage("AVISO!!", "No hay servidores seleccionados");
        }
        PrimeFaces.current().executeScript("PF('servidorPublicoDialog').hide()");
        PrimeFaces.current().ajax().update(":formMain");
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
            }
        }
        if (s.getFechaIngreso() != null && s.getFechaSalida() != null) {
            if (TalentoHumano.validarFechaInicio(s.getFechaIngreso(), tipoRol) && TalentoHumano.validarFechaFin(s.getFechaSalida(), tipoRol)) {
                diasLaboradoService.create(diaLaborado);
            }
        }
    }

//<editor-fold defaultstate="collapsed" desc="METODOS DE CONSULTA BIOMETRICO">
    public void conectBiometrico() {
        System.out.println("entro a metodo--> ");
        if (listServidoresDataTable.isEmpty()) {
            listServidoresDataTable = diasLaboradoService.getListaDiasLaborado(busqueda.getAnio(), diaLaborado.getTipoRol().getMes().getDescripcion());
        }
        for (DiasLaborado dia : listServidoresDataTable) {
            Thread data = new Thread() {
                @Override
                public void run() {
                    System.out.println("hilo---> ");
                    //int hours = 0, dias = 0, horas_laboradas = 0;
                    if (dia.getServidor().getCodBiometrico() != null && dia.getServidor().getPuntoMarcacion() != null) {
                        int hours = 0, dias = 0, horas_laboradas = 0;
                        Calendar cal = Calendar.getInstance();
                        for (int i = 1; i <= TalentoHumano.getUltimoDiaMes(dia.getTipoRol()); i++) {
                            marcacionModel = new MarcacionModel();
                            marcacionModel = marcacionService.marcacionIngresoSalidaLaborados(ParseFecha(mesAnio() + i), dia, "0", "1");
                            if (marcacionModel != null) {
                                if (marcacionModel.getHoras_laboras() != null) {
                                    cal.setTime(TalentoHumano.convertStringToTimestamp(marcacionModel.getHoras_laboras(), "HH:mm"));
                                    horas_laboradas = cal.get(Calendar.HOUR_OF_DAY);
                                    if (horas_laboradas >= TalentoHumano.HORAS_LABORABLES) {
                                        hours = hours + TalentoHumano.HORAS_LABORABLES;
                                        dias++;
                                    } else {
                                        hours = hours + cal.get(Calendar.HOUR_OF_DAY);
                                    }
                                } else {
                                    cal.setTime(TalentoHumano.convertStringToTimestamp(marcacionModel.getTotal_hora(), "HH:mm"));
                                    horas_laboradas = cal.get(Calendar.HOUR_OF_DAY);
                                    if (horas_laboradas >= TalentoHumano.HORAS_LABORABLES + 1) {
                                        hours = hours + TalentoHumano.HORAS_LABORABLES;
                                        dias++;
                                    } else {
                                        hours = hours + cal.get(Calendar.HOUR_OF_DAY);
                                    }
                                }
                            }
                        }
                        if (hours > 0) {
                            setearDiasBiometrico(dia, dias, hours);
                        }
                    } else {
                        System.out.println("entro al else-->");
                        if (dia.getServidor().getCodBiometrico() != null) {
                            try {
                                listMarcacionModel = diasLaboradoService.getListMarcacion(dia.getServidor().getCodBiometrico(), TalentoHumano.convertirMesByNumString(dia.getTipoRol().getMes().getCodigo()), dia.getTipoRol().getAnio() + "");
                            } catch (Exception ex) {
                                Logger.getLogger(DiasLaboradosController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            if (!listMarcacionModel.isEmpty()) {
                                int dias = 0, horas_laboradas = 0, hours = 0;
                                Calendar cal = Calendar.getInstance();
                                for (MarcacionModel mm : listMarcacionModel) {
                                    if (mm.getHoras_laboras() != null) {
                                        cal.setTime(TalentoHumano.convertStringToTimestamp(mm.getHoras_laboras(), "HH:mm"));
                                        horas_laboradas = cal.get(Calendar.HOUR_OF_DAY);
                                        if (horas_laboradas >= TalentoHumano.HORAS_LABORABLES) {
                                            hours = hours + TalentoHumano.HORAS_LABORABLES;
                                            dias++;
                                        } else {
                                            hours = hours + cal.get(Calendar.HOUR_OF_DAY);
                                        }
                                    }
                                }
                                System.out.println("nombre--> " + dia.getServidor().getPersona().getNombreCompleto());
                                setearDiasBiometrico(dia, dias, hours);
                            }
                        }
                    }

                }
            };
            data.start();
        }
        PrimeFaces.current().ajax().update("formMain");
    }

    public void setearDiasBiometrico(DiasLaborado dia, int dias, int hours) {
        if (dias >= TalentoHumano.diasHabilesEntreFechas(ParseFecha(mesAnio() + 1), ParseFecha(mesAnio() + TalentoHumano.getUltimoDiaMes(dia.getTipoRol())))) {
            dia.setDias(Short.parseShort(TalentoHumano.diasCalendarioLaboral + ""));
            diasLaboradoService.edit(dia);
        } else {
            int dias_promedio = (hours / TalentoHumano.HORAS_LABORABLES);
            int dias_semana = 0, dias_ = 0, dias_trabajados = 0;
            if (dias > 4) {
                dias_semana = (dias_promedio / TalentoHumano.DIAS_LABORABLES) * TalentoHumano.DIAS_SEMANA;
                dias_ = (dias_promedio % TalentoHumano.DIAS_LABORABLES) / 2;
                dias_trabajados = (dias_semana + dias_);
            } else {
                dias_trabajados = (dias_promedio + dias_);
            }
            dia.setDias(Short.parseShort(dias_trabajados + ""));
            diasLaboradoService.edit(dia);
        }
    }

    public void openDlgMarcacion(DiasLaborado dia) {
        listMarcacionModel.clear();
        diaLaborado = dia;
        if (dia.getServidor().getCodBiometrico() != null) {
            for (int i = 1; i <= TalentoHumano.getUltimoDiaMes(dia.getTipoRol()); i++) {
                marcacionModel = new MarcacionModel();
                marcacionModel = marcacionService.marcacionIngresoSalidaLaborados(ParseFecha(mesAnio() + i), dia, "0", "1");
                if (marcacionModel != null) {
                    listMarcacionModel.add(marcacionModel);
                    if (marcacionModel.getTotal_hora() != null) {

                    }
                }
            }
        }
        PrimeFaces.current().executeScript("PF('dlgMarcacionBiometrico').show()");
        PrimeFaces.current().ajax().update("frmMarcacion");
    }

    public void conectSQLiteOpenDLG(DiasLaborado dia) {
        listMarcacionModel.clear();
        diaLaborado = dia;
        if (dia.getServidor().getCodBiometrico() != null) {
            listMarcacionModel = diasLaboradoService.getListMarcacion(dia.getServidor().getCodBiometrico(), TalentoHumano.convertirMesByNumString(dia.getTipoRol().getMes().getCodigo()), dia.getTipoRol().getAnio() + "");
        }
        PrimeFaces.current().executeScript("PF('dlgMarcacionBiometrico').show()");
        PrimeFaces.current().ajax().update("frmMarcacion");
    }

    public void openDialogoBiometrico(DiasLaborado dia) {
        try {
            conectSQLiteOpenDLG(dia);
            if (listMarcacionModel.isEmpty()) {
                openDlgMarcacion(dia);
            }
        } catch (Exception ex) {
            Logger.getLogger(DiasLaboradosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String mesAnio() {
        Short anio = diaLaborado.getTipoRol().getAnio();
        Integer mes = TalentoHumano.convertirMesLetraNum(diaLaborado.getTipoRol().getMes().getCodigo()) + 1;
        return anio + "-" + mes + "-";
    }
//</editor-fold>

//<editor-fold defaultstate="collapsed" desc="GETTER AND SETTER">
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

    public List<MarcacionModel> getListMarcacionModel() {
        return listMarcacionModel;
    }

    public void setListMarcacionModel(List<MarcacionModel> listMarcacionModel) {
        this.listMarcacionModel = listMarcacionModel;
    }

    public DiasLaborado getDiaLaboradoSeleccion() {
        return diaLaboradoSeleccion;
    }

    public void setDiaLaboradoSeleccion(DiasLaborado diaLaboradoSeleccion) {
        this.diaLaboradoSeleccion = diaLaboradoSeleccion;
    }

    public List<Servidor> getServidorList() {
        return servidorList;
    }

    public void setServidorList(List<Servidor> servidorList) {
        this.servidorList = servidorList;
    }
//</editor-fold>
}
