/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.ConnectSQLite.ConnectionSQLite;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.ParametrosTalentoHumano;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.model.MarcacionModel;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.HoraExtraSuplementariaService;
import com.origami.sigef.talentohumano.services.ParametrizableService;
import com.origami.sigef.talentohumano.services.ServidorService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import com.origami.sigef.talentohumano.services.biotime.MarcacionService;
import java.io.Serializable;
import java.sql.Timestamp;
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
import javax.persistence.Temporal;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "horaExtraView")
@ViewScoped
public class HoraExtraSuplementariaController implements Serializable {

    @Inject
    private ServidorService servidorService;
    @Inject
    private DistributivoService distributivoService;
    @Inject
    private HoraExtraSuplementariaService horaExtraService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private CatalogoItemService itemService;
    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ValoresRolesService valoresRolsService;
    @Inject
    private ParametrizableService parametroService;
    @Inject
    private MarcacionService marcacionService;
    @Inject
    private DiasLaboradoService diasLaboradoService;

    private List<HoraExtraSuplementaria> listaDataTable;
    private List<HoraExtraSuplementaria> listHorasExtras;
    private LazyModel<HoraExtraSuplementaria> lazy;
    private HoraExtraSuplementaria horaExtraSupl;
    private HoraExtraSuplementaria horaExt;
    private DualListModel<Servidor> listServidor;
    private List<Servidor> lista;
    private List<Servidor> servidorSource;
    private List<Servidor> servidorTarget;
    private OpcionBusqueda busqueda;
    private List<TipoRol> rolesMensuales;
    private List<MasterCatalogo> periodos;
    private TipoRol tipoRol;
    private CatalogoItem registrado;
    private CatalogoItem tipoRolMensual;
//    private CatalogoItem rolGeneral;
    private boolean disabledbtnBiometrico;
    private ParametrosTalentoHumano valoPrametro;
    private MarcacionModel marcacionModel;
    private List<MarcacionModel> listMarcacionModel;
    private ConnectionSQLite conect;

    private Timestamp time;

    @Temporal(javax.persistence.TemporalType.TIMESTAMP)
    private Date horas;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        tipoRol = new TipoRol();
        horaExtraSupl = new HoraExtraSuplementaria();
        horaExtraSupl.setTipoRol(new TipoRol());
        listaDataTable = new ArrayList<>();
        listHorasExtras = new ArrayList<>();
//        rolGeneral  = catalogoItemService.getEstadoRol("rol_general");
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        tipoRolMensual = catalogoItemService.getEstadoRol("ROL-HORAS-EXTRAS");
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        rolesMensuales = tipoRolService.listaRoles(busqueda.getAnio(), tipoRolMensual);
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
        servidorTarget = new ArrayList<>();
        servidorSource = new ArrayList<>();
        lista = servidorService.listServidoresPeriodo(busqueda.getAnio());
//        servidorSource = servidorService.listServidoresPeriodo(busqueda.getAnio());
//        System.out.println("lista de servidores " + servidorSource);
        listServidor = new DualListModel(servidorSource, servidorTarget);
        disabledbtnBiometrico = true;
        valoPrametro = parametroService.valorParametroTipo("HORAS_SUP");
        listMarcacionModel = new ArrayList<>();
        horaExt = new HoraExtraSuplementaria();
        conect = new ConnectionSQLite();
    }

    public void obtenerDatosBiometrico() {
        System.out.println("entro");
        System.out.println("object--> " + horaExtraSupl);
        listHorasExtras = horaExtraService.getHorasSuplemxTipoRol(tipoRol);
        if (!listHorasExtras.isEmpty()) {
            for (HoraExtraSuplementaria hora : listHorasExtras) {
                Thread data = new Thread() {
                    @Override
                    public void run() {
                        if (hora.getServidor().getCodBiometrico() != null && hora.getServidor().getPuntoMarcacion() != null) {
                            int horasExtras = 0, horas = 0, minutos = 0, minutosExtras = 0;
                            Calendar cal = Calendar.getInstance();
                            for (int i = 1; i <= TalentoHumano.getUltimoDiaMes(hora.getTipoRol()); i++) {
                                marcacionModel = new MarcacionModel();
                                marcacionModel = marcacionService.marcacionIngresoSalidaLaborados(TalentoHumano.ParseFecha(mesAnio() + i, "yyyy-MM-dd"), hora, "0", "1");
                                Boolean var = null;
                                if (marcacionModel != null) {
                                    MarcacionModel mHoras = new MarcacionModel();
                                    if (marcacionModel.getHoras_laboras() != null) {
                                        var = Boolean.TRUE;
                                    } else {
                                        var = Boolean.FALSE;
                                    }
                                    mHoras = marcacionService.marcacionHorasExtras(TalentoHumano.ParseFecha(mesAnio() + i, "yyyy-MM-dd"), hora, "0", "1", var);
                                    if (mHoras != null) {
                                        if (isSabadoDomingo(TalentoHumano.ParseFecha(mHoras.getDate(), "yyyy/MM/dd"))) {
                                            if (mHoras.getTotal_hora() != null) {
                                                cal.setTime(TalentoHumano.convertStringToTimestamp(mHoras.getTotal_hora(), "HH:mm"));
                                                horasExtras = horasExtras + cal.get(Calendar.HOUR_OF_DAY);
                                                minutosExtras = minutosExtras + cal.get(Calendar.MINUTE);
                                            }
                                        } else {
                                            if (mHoras.getHoras_extras_desc() != null) {
                                                cal.setTime(TalentoHumano.ParseFecha(mHoras.getHoras_extras_desc(), "HH:mm"));
                                                horas = horas + cal.get(Calendar.HOUR_OF_DAY);
                                                minutos = minutos + cal.get(Calendar.MINUTE);
                                            } else {
                                                if (mHoras.getHoras_extras() != null) {
                                                    cal.setTime(mHoras.getHoras_extras());
                                                    horas = horas + cal.get(Calendar.HOUR_OF_DAY);
                                                    minutos = minutos + cal.get(Calendar.MINUTE);
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            if (minutosExtras > 0) {
                                horasExtras = horasExtras + (minutosExtras / 60);
                            }
                            if (minutos > 0) {
                                horas = horas + (minutos / 60);
                            }
                            hora.setHoraSuplementaria(Short.parseShort(horas + ""));
                            hora.setHoraExtras(Short.parseShort(horasExtras + ""));
                            horaExtraService.edit(hora);
                        } else if (hora.getServidor().getCodBiometrico() != null) {
                            try {
                                List<MarcacionModel> listMarcacion = diasLaboradoService.getListMarcacion(hora.getServidor().getCodBiometrico(), TalentoHumano.convertirMesByNumString(hora.getTipoRol().getMes().getCodigo()), hora.getTipoRol().getAnio() + "");
                                listMarcacionModel = conect.calcularHoras(listMarcacion);
                                int horasExtras = 0, horas = 0, minutos = 0, minutosExtras = 0;
                                if (!listMarcacionModel.isEmpty()) {
                                    for (MarcacionModel mm : listMarcacionModel) {
                                        Calendar cal = Calendar.getInstance();
                                        if (isSabadoDomingo(TalentoHumano.ParseFecha(mm.getDate(), "yyyy/MM/dd"))) {
                                            if (mm.getHoras_extras() != null) {
                                                cal.setTime(mm.getHoras_extras());
                                                horasExtras = horasExtras + cal.get(Calendar.HOUR_OF_DAY);
                                                if (cal.get(Calendar.HOUR_OF_DAY) > 30) {
                                                    minutosExtras = minutosExtras + cal.get(Calendar.MINUTE);
                                                }
                                            }
                                        } else if (mm.getHoras_extras() != null) {
                                            cal.setTime(mm.getHoras_extras());
                                            horas = horas + cal.get(Calendar.HOUR_OF_DAY);
                                            if (cal.get(Calendar.HOUR_OF_DAY) > 30) {
                                                minutos = minutos + cal.get(Calendar.MINUTE);
                                            }
                                        }
                                    }
                                    if (minutosExtras > 0) {
                                        horasExtras = horasExtras + (minutosExtras / 60);
                                    }
                                    if (minutos > 0) {
                                        horas = horas + (minutos / 60);
                                    }
                                    hora.setHoraSuplementaria(Short.parseShort(horas + ""));
                                    hora.setHoraExtras(Short.parseShort(horasExtras + ""));
                                    horaExtraService.edit(hora);
                                }
                            } catch (Exception ex) {
                                Logger.getLogger(HoraExtraSuplementariaController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                };
                data.start();
            }
        }
        //PrimeFaces.current().ajax().update("formMain");
    }

    public Boolean isSabadoDomingo(Date fecha) {
        switch (TalentoHumano.getDayOfTheWeek(fecha)) {
            case 1://Domingo
                return Boolean.TRUE;
            case 7://sabado
                return Boolean.TRUE;
            default:
                return Boolean.FALSE;
        }
    }

    public void openDlgMarcacion(HoraExtraSuplementaria hora) {
        listMarcacionModel.clear();
        horaExt = hora;
        if (hora.getServidor().getCodBiometrico() != null && hora.getServidor().getPuntoMarcacion() != null) {
            for (int i = 1; i <= TalentoHumano.getUltimoDiaMes(hora.getTipoRol()); i++) {
                marcacionModel = new MarcacionModel();
                marcacionModel = marcacionService.marcacionIngresoSalidaLaborados(TalentoHumano.ParseFecha(mesAnio() + i, "yyyy-MM-dd"), hora, "0", "1");
                System.out.println("marcacion--> " + marcacionModel);
                Boolean var = null;
                if (marcacionModel != null) {
                    MarcacionModel mHoras = new MarcacionModel();
                    if (marcacionModel.getHoras_laboras() != null) {
                        var = Boolean.TRUE;
                    } else {
                        var = Boolean.FALSE;
                    }
                    mHoras = marcacionService.marcacionHorasExtras(TalentoHumano.ParseFecha(mesAnio() + i, "yyyy-MM-dd"), hora, "0", "1", var);
                    if (mHoras != null) {
                        listMarcacionModel.add(mHoras);
                    }
                }
            }
        } else if (hora.getServidor().getCodBiometrico() != null) {
            try {
                System.out.println("entro a else if");
                List<MarcacionModel> listMarcacion = diasLaboradoService.getListMarcacion(hora.getServidor().getCodBiometrico(), TalentoHumano.convertirMesByNumString(hora.getTipoRol().getMes().getCodigo()), hora.getTipoRol().getAnio() + "");
                listMarcacionModel = conect.calcularHoras(listMarcacion);
            } catch (Exception ex) {
                Logger.getLogger(HoraExtraSuplementariaController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        PrimeFaces.current().executeScript("PF('dlgMarcacionBiometrico').show()");
        PrimeFaces.current().ajax().update("frmMarcacion");
    }

    public String mesAnio() {
        Short anio = tipoRol.getAnio();
        Integer mes = TalentoHumano.convertirMesLetraNum(tipoRol.getMes().getCodigo()) + 1;
        return anio + "-" + mes + "-";
    }

    public void addServidor() {
        if (horaExtraSupl.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Horas Extras y Suplementarias", "Debe de seleccionar una cabecera de Rol.");
            return;
        }
        String mes = horaExtraSupl.getTipoRol().getMes().getDescripcion().toUpperCase();
        servidorTarget = listServidor.getTarget();
        PrimeFaces.current().executeScript("PF('dlgListServidor').show()");
    }

    public void onTransfer() {
        servidorSource = listServidor.getSource();
        servidorTarget = listServidor.getTarget();
    }

    public void mesesRoles() {
        rolesMensuales = tipoRolService.listaRoles(busqueda.getAnio(), tipoRolMensual);
    }

    public void guardarServidores() {
        listaDataTable = horaExtraService.getListaHoraExtrSupl(busqueda.getAnio(), horaExtraSupl.getTipoRol());
        if (listaDataTable.isEmpty()) {
            for (Servidor s : servidorTarget) {
                setearServidor(s);
            }
        } else {
            List<Servidor> aux = new ArrayList<>();
            for (Servidor s : servidorTarget) {
                int cont = 0;
                for (HoraExtraSuplementaria h : listaDataTable) {
                    cont++;
                    if (s.equals(h.getServidor())) {
                        JsfUtil.addWarningMessage("Servidor", "Servidor " + s.getPersona().getNombreCompleto() + " ya se encuentra registrado");
                        cont--;
                    }
                    if (cont == listaDataTable.size()) {
                        aux.add(s);
                    }
                }

            }
            for (Servidor s : aux) {
                if (validarFechaIngreso(s, tipoRol)) {
                    setearServidor(s);
                } else {
                    JsfUtil.addWarningMessage("Información", "Servidor " + s.getPersona().getNombreCompleto() + " su fecha de ingreso no entra en el rol Seleccionado");
                }
            }
        }
        PrimeFaces.current().executeScript("PF('dlgListServidor').hide()");
        PrimeFaces.current().ajax().update("formDlgServ");
        listaDataTable = horaExtraService.getListaHoraExtrSupl(busqueda.getAnio(), horaExtraSupl.getTipoRol());
        servidorSource = servidorService.listServidoresPeriodo(busqueda.getAnio());
        servidorTarget = new ArrayList<>();
        listServidor = new DualListModel(servidorSource, servidorTarget);
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

    public boolean validarFechaIngreso(Servidor s, TipoRol r) {
        int mes, anio, mesRol;
        mes = obtenerMes(s.getFechaIngreso());
        anio = obtenerAnio(s.getFechaIngreso());
        mesRol = getMesRol(r);
        if (anio < r.getAnio().intValue()) {
            return true;
        }
        if (anio == r.getAnio().intValue() && mes <= mesRol) {
            return true;
        }
        return false;
    }

    public int obtenerMes(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.MONTH);
    }

    public int obtenerAnio(Date fecha) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(fecha);
        return cal.get(Calendar.YEAR);
    }

    public int getMesRol(TipoRol rol) {
        int mesNum = 0;
        String mes = rol.getMes().getCodigo().toUpperCase();
        switch (mes) {
            case "ENERO":
                mesNum = 0;
                break;
            case "FEBRERO":
                mesNum = 1;
                break;
            case "MARZO":
                mesNum = 2;
                break;
            case "ABRIL":
                mesNum = 3;
                break;
            case "MAYO":
                mesNum = 4;
                break;
            case "JUNIO":
                mesNum = 5;
                break;
            case "JULIO":
                mesNum = 6;
                break;
            case "AGOSTO":
                mesNum = 7;
                ;
                break;
            case "SEPTIEMBRE":
                mesNum = 8;
                break;
            case "OCTUBRE":
                mesNum = 9;
                break;
            case "NOVIEMBRE":
                mesNum = 10;
                break;
            case "DICIEMBRE":
                mesNum = 11;
                break;
            default:
                JsfUtil.addErrorMessage("Error", "Mes inválido");
                break;
        }
        return mesNum;
    }

    public void setearServidor(Servidor s) {
        horaExtraSupl = new HoraExtraSuplementaria();
        if (buscarRubroAsignado(s, valoPrametro, busqueda.getAnio())) {
            if (s.getRealizaHorasExtras()) {
                horaExtraSupl.setTipoRol(new TipoRol());
                horaExtraSupl.setServidor(new Servidor());
                horaExtraSupl.setValorParametrizado(new ParametrosTalentoHumano());
                horaExtraSupl.setValorParametrizado(valoPrametro);
                horaExtraSupl.setServidor(s);
                horaExtraSupl.setTipoRol(tipoRol);
                horaExtraSupl.setMes(tipoRol.getMes().getDescripcion());
                horaExtraSupl.setPeriodo(busqueda.getAnio());
                horaExtraSupl.setFechaCreacion(new Date());
                horaExtraService.create(horaExtraSupl);
            } else {
                JsfUtil.addWarningMessage("Información", "Verifique que el Servidor" + s.getPersona().getNombreCompleto().toUpperCase() + " tenga habilitada la opción de Horas Extras");
            }
        } else {
            JsfUtil.addWarningMessage("Información", "Verifique que el Servidor" + s.getPersona().getNombreCompleto().toUpperCase() + " tenga Asignado el rubro " + valoPrametro.getNombre());
        }
    }

    public void buscar() {

        if (horaExtraSupl.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Horas Extras y Suplementarias", "Debe de seleccionar un una cabecera de Rol.");
            return;
        }
        servidorTarget = new ArrayList<>();
        servidorSource = new ArrayList<>();
        servidorSource = servidorService.getServidorInDiaLaborado(horaExtraSupl.getTipoRol());
        listServidor = new DualListModel(servidorSource, servidorTarget);
        tipoRol = horaExtraSupl.getTipoRol();
        String mes = horaExtraSupl.getTipoRol().getMes().getDescripcion().toUpperCase();
        disabledBtnBiometrico(tipoRol);
        lazy = new LazyModel<>(HoraExtraSuplementaria.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", tipoRol);
        lazy.getFilterss().put("periodo", busqueda.getAnio());
        lazy.getSorteds().put("servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
    }

    public void guardar(HoraExtraSuplementaria hora) {
        try {
            if (hora.getHoraExtras().intValue() < 0 || hora.getHoraSuplementaria().intValue() < 0) {
                JsfUtil.addWarningMessage("Horas extras Suplementaria", "Horas no valida");
                hora.setHoraExtras((short) 0);
                return;
            }
            if (hora.getHoraExtras().intValue() > TalentoHumano.horasExtrasLOSEP //40
                    && hora.getServidor().getDistributivo().getRegimen().getCodigo().equals("LOSEP")) {
                JsfUtil.addWarningMessage("Horas extras Suplementaria", "Horas Extras No Puede superar las " + TalentoHumano.horasExtrasLOSEP + " horas");
                hora.setHoraExtras((short) 0);
                return;
            }
            if (hora.getHoraExtras().intValue() > TalentoHumano.horasExtrasCT //120
                    && hora.getServidor().getDistributivo().getRegimen().getCodigo().equals("CT")) {
                JsfUtil.addWarningMessage("Horas extras Suplementaria", "Horas Extras No Puede superar las " + TalentoHumano.horasExtrasCT + " horas");
                hora.setHoraExtras((short) 0);
                return;
            }
            if (hora.getHoraSuplementaria().intValue() > TalentoHumano.horasSuplementariasLOSEP //60
                    && hora.getServidor().getDistributivo().getRegimen().getCodigo().equals("LOSEP")) {
                JsfUtil.addWarningMessage("Horas extras Suplementaria", "Horas Suplementarias no puede superar las " + TalentoHumano.horasSuplementariasLOSEP + " horas");
                hora.setHoraSuplementaria((short) 0);
                return;
            }
            if (hora.getHoraSuplementaria().intValue() > TalentoHumano.horasSuplementariasCT //48
                    && hora.getServidor().getDistributivo().getRegimen().getCodigo().equals("CT")) {
                JsfUtil.addWarningMessage("Horas extras Suplementaria", "Horas Suplementarias no puede superar las " + TalentoHumano.horasSuplementariasCT + " horas");
                hora.setHoraSuplementaria((short) 0);
                return;
            }
            int totalHora = hora.getHoraExtras().intValue() + hora.getHoraSuplementaria().intValue();
            if (hora.getServidor().getMaximoHorasExtras() == null || hora.getServidor().getMaximoHorasExtras() <= 0) {
                JsfUtil.addWarningMessage("Advertencia", "El maximo de horas debe ser mayor a CERO");
                return;
            }
            if (hora.getServidor().getMaximoHorasExtras() < totalHora) {
                JsfUtil.addWarningMessage("Advertencia", "El Total de Horas No puede sobrepasar El Máximo de horas Establecido en el Servidor(a): " + hora.getServidor().getPersona().getNombreCompleto() + " la cual es " + hora.getServidor().getMaximoHorasExtras());
                hora.setHoraSuplementaria((short) 0);
                hora.setHoraExtras((short) 0);
                return;
            }
            hora.setFechaCreacion(new Date());
            hora.setUsuarioCreacion(userSession.getNameUser());
            horaExtraService.edit(hora);
            JsfUtil.addSuccessMessage("Horas Extras Suplementarias", "Horas Guarda con éxito");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void grabarHoras(boolean var) {
        if (horaExtraSupl.getTipoRol() == null) {
            JsfUtil.addErrorMessage("Horas Extras Suplementarias", "Debe seleccionarl el Rol");
            return;
        }
        String mes = horaExtraSupl.getTipoRol().getMes().getDescripcion().toUpperCase();
        tipoRol = tipoRolService.rolEncabezado(busqueda.getAnio(), mes, horaExtraSupl.getTipoRol().getTipoRol());
        listaDataTable = horaExtraService.getListaHoraExtrSupl(busqueda.getAnio(), horaExtraSupl.getTipoRol());
        if (listaDataTable.isEmpty()) {
            JsfUtil.addWarningMessage("Horas Extras Suplementarias", "No cuenta con registros para grabar");
            return;
        }
        if (var) {
            tipoRol.setHoraExtSupl(Boolean.TRUE);
        } else {
            tipoRol.setHoraExtSupl(Boolean.FALSE);
        }
        tipoRolService.edit(tipoRol);
        tipoRol = tipoRolService.rolEncabezado(busqueda.getAnio(), mes, horaExtraSupl.getTipoRol().getTipoRol());
        JsfUtil.addSuccessMessage("Horas Extras Suplementarias", var ? "Horas grabadas con éxito" : "Horas habilitadas para editar");
    }

    public boolean disabledCellEdit() {
        if (tipoRol != null) {
            if (tipoRol.getHoraExtSupl()) {
                return true;
            }
        }
        return false;
    }

    public void disabledBtnBiometrico(TipoRol t) {
        disabledbtnBiometrico = true;
        if (t.getId() != null) {
            if ("registrado-rol".equals(t.getEstadoAprobacion().getCodigo()) && "ROL-HORAS-EXTRAS".equals(t.getTipoRol().getCodigo())) {
                setDisabledbtnBiometrico(false);
            }
        }
    }

    public boolean renderedBtn() {
        return tipoRol.getHoraExtSupl();
    }

    public int horaExtraMax() {
        if (horaExtraSupl != null) {
            if (horaExtraSupl.getServidor().getDistributivo().getRegimen().getCodigo().equals("LOSEP")) {
                return 40;
            }
            if (horaExtraSupl.getServidor().getDistributivo().getRegimen().getCodigo().equals("CT")) {
                return 120;
            }
        }
        return 0;
    }

    public int horaSuplMax() {
        if (horaExtraSupl != null) {
            if (horaExtraSupl.getServidor().getDistributivo().getRegimen().getCodigo().equals("LOSEP")) {
                return 60;
            }
            if (horaExtraSupl.getServidor().getDistributivo().getRegimen().getCodigo().equals("CT")) {
                return 48;
            }
        }
        return 0;
    }

    public void eliminar(HoraExtraSuplementaria hora) {
        if (hora.getTipoRol().getEstadoAprobacion().equals(registrado)) {
            hora.setEstado(Boolean.FALSE);
            horaExtraService.edit(hora);
            listaDataTable = horaExtraService.getListaHoraExtrSupl(busqueda.getAnio(), horaExtraSupl.getTipoRol());
        } else {
            JsfUtil.addWarningMessage("Eliminar Rol", "No puede Eliminar con Roles Aprobados o Pagados");
        }
    }

    public List<HoraExtraSuplementaria> getListaDataTable() {
        return listaDataTable;
    }

    public void setListaDataTable(List<HoraExtraSuplementaria> listaDataTable) {
        this.listaDataTable = listaDataTable;
    }

    public HoraExtraSuplementaria getHoraExtraSupl() {
        return horaExtraSupl;
    }

    public void setHoraExtraSupl(HoraExtraSuplementaria horaExtraSupl) {
        this.horaExtraSupl = horaExtraSupl;
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

    public DualListModel<Servidor> getListServidor() {
        return listServidor;
    }

    public void setListServidor(DualListModel<Servidor> listServidor) {
        this.listServidor = listServidor;
    }

    public List<Servidor> getServidorSource() {
        return servidorSource;
    }

    public void setServidorSource(List<Servidor> servidorSource) {
        this.servidorSource = servidorSource;
    }

    public List<Servidor> getServidorTarget() {
        return servidorTarget;
    }

    public void setServidorTarget(List<Servidor> servidorTarget) {
        this.servidorTarget = servidorTarget;
    }

    public TipoRol getTipoRol() {
        return tipoRol;
    }

    public void setTipoRol(TipoRol tipoRol) {
        this.tipoRol = tipoRol;
    }

    public LazyModel<HoraExtraSuplementaria> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<HoraExtraSuplementaria> lazy) {
        this.lazy = lazy;
    }

    public List<Servidor> getLista() {
        return lista;
    }

    public void setLista(List<Servidor> lista) {
        this.lista = lista;
    }

    public boolean isDisabledbtnBiometrico() {
        return disabledbtnBiometrico;
    }

    public void setDisabledbtnBiometrico(boolean disabledbtnBiometrico) {
        this.disabledbtnBiometrico = disabledbtnBiometrico;
    }

    public MarcacionModel getMarcacionModel() {
        return marcacionModel;
    }

    public void setMarcacionModel(MarcacionModel marcacionModel) {
        this.marcacionModel = marcacionModel;
    }

    public HoraExtraSuplementaria getHoraExt() {
        return horaExt;
    }

    public void setHoraExt(HoraExtraSuplementaria horaExt) {
        this.horaExt = horaExt;
    }

    public List<MarcacionModel> getListMarcacionModel() {
        return listMarcacionModel;
    }

    public void setListMarcacionModel(List<MarcacionModel> listMarcacionModel) {
        this.listMarcacionModel = listMarcacionModel;
    }

}
