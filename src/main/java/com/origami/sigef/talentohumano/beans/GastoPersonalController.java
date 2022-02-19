/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.GastoPersonal;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.GastoPersonalService;
import com.origami.sigef.talentohumano.services.ServidorService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author OrigamiEC
 */
@Named(value = "gastoPersonalView")
@ViewScoped
public class GastoPersonalController implements Serializable {

    @Inject
    private GastoPersonalService gastoPersonalService;
    @Inject
    private UserSession userSession;
    @Inject
    private ServidorService servidorService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private CantonService cantonService;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private DistributivoEscalaService distributivoService;

    private String ci;

    private List<MasterCatalogo> periodosFiscales;
    private List<Provincia> provincias;
    private List<Canton> cantones;
    private LazyModel<GastoPersonal> lazy;
    private List<GastoPersonal> listaPersonal;
    private GastoPersonal gastoPersonal;
    private GastoPersonal gastoPersonalSeleccionado;
    private Servidor servidor;
    private BigDecimal rmuPersonal;
    private BigDecimal porcentaje;
    private BigDecimal mesesAnio;
    private boolean collapsed;
    private Date fechaMes;
    private String cedula;
    private OpcionBusqueda busqueda;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        this.cedula = "";
        fechaMes = new Date();
        collapsed = Boolean.TRUE;
        rmuPersonal = BigDecimal.ZERO;
        porcentaje = new BigDecimal(0.5);
        mesesAnio = new BigDecimal(12);
        this.provincias = provinciaService.getProvincias();
        gastoPersonal = new GastoPersonal();
        gastoPersonalSeleccionado = new GastoPersonal();
        servidor = new Servidor();
        gastoPersonal.setServidorPublico(new Servidor());
        gastoPersonal.getServidorPublico().setPersona(new Cliente());
        initDeclaracion();
        lazy = new LazyModel<>(GastoPersonal.class);
        lazy.getFilterss().put("estado", true);
        listaPersonal = gastoPersonalService.findAll();
        periodosFiscales = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
    }

    public void initDeclaracion() {
        gastoPersonal.setProvincia(provinciaService.find(8L));
        this.cantones = cantonService.getCantones(gastoPersonal.getProvincia());
        gastoPersonal.setEjercicioFiscal(busqueda.getAnio());
        gastoPersonal.setFechaEntrega(new Date());
    }

    public void filterLazyPeriodo() {
        lazy = new LazyModel<>(GastoPersonal.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("ejercicioFiscal", gastoPersonal.getEjercicioFiscal());
    }

    public void guardar() {
        if (gastoPersonal.getServidorPublico() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un servidor Público.");
            return;
        }
        boolean edit = gastoPersonal.getId() != null;
        if (edit) {
            gastoPersonal.setFechaModificacion(new Date());
            gastoPersonal.setUsuarioModifica(userSession.getNameUser());
            gastoPersonalService.edit(gastoPersonal);
        } else {
            gastoPersonal.setFechaCreacion(new Date());
            gastoPersonal.setUsuarioCreacion(userSession.getNameUser());
            gastoPersonal.setPeriodo(gastoPersonal.getEjercicioFiscal());
            gastoPersonal = gastoPersonalService.create(gastoPersonal);
        }
        gastoPersonal = new GastoPersonal();
        initDeclaracion();
        this.cedula = "";
        JsfUtil.addInformationMessage("Declaración de Personal", "Información " + (edit ? "Editados" : "Registrados") + " Correctamente");
        PrimeFaces.current().ajax().update("formMain");
    }

    /*Actualizar Cantones dependiendo de la provincia seleccionada*/
    public void actualizarCantones() {
        this.cantones = cantonService.getCantones(gastoPersonal.getProvincia());
    }

    public void editar(GastoPersonal gasto) {
        this.gastoPersonal = gasto;
        this.cantones = cantonService.getCantones(gasto.getProvincia());
        this.cedula = gasto.getServidorPublico().getPersona().getIdentificacion();
        this.collapsed = Boolean.FALSE;
    }

    public void buscarServidor() {
        if (Utils.getAnio(gastoPersonal.getFechaEntrega()) < gastoPersonal.getEjercicioFiscal()) {
            JsfUtil.addWarningMessage("Declaración de Personal", "Verifique que la fecha de Entrega este dentro del Ejercicio Fiscal");
            return;
        }
        Servidor serv = distributivoService.getServidorAnio(gastoPersonal.getEjercicioFiscal(), this.cedula);
        if (serv != null) {
            GastoPersonal res = gastoPersonalService.existeRegistro(serv, busqueda.getAnio());
            if (res != null) {
                JsfUtil.addWarningMessage("Ya Existe Registro de Gasto Personales de :", serv.getPersona().getNombreCompleltoSql().toUpperCase());
                return;
            }
            this.gastoPersonal.setServidorPublico(serv);
            gastoPersonal.setIngresoGravado(ingresoGravado());
            sumaGastos();
        } else {
            Map<String, List<String>> params = new HashMap<>();
            String fechaIng = Utils.dateFormatPattern("dd/MM/yyyy", gastoPersonal.getFechaEntrega());
            params.put("FECHAGASTO", Arrays.asList(fechaIng));
            JsfUtil.addWarningMessage("Declaración de Personal", "Verifique que la información sea correcta");

            Utils.openDialog("/facelet/talentoHumano/dialogServidor", "850", "400", params);

        }
        sumaIngresos();
    }

    public boolean actualizarServidor(Servidor serv) {
        for (GastoPersonal s : listaPersonal) {
            if (s.getServidorPublico().equals(serv) && s.getPeriodo() == gastoPersonal.getEjercicioFiscal() && s.getActualizacion().equals(Boolean.FALSE)) {
                fechaMes = s.getFechaEntrega();
                return true;
            }
        }
        fechaMes = gastoPersonal.getFechaEntrega();
        return false;
    }

    public void selectData(SelectEvent evt) {
        Servidor serv = (Servidor) evt.getObject();
        GastoPersonal res = gastoPersonalService.existeRegistro(serv, busqueda.getAnio());
        if (res != null) {
            JsfUtil.addWarningMessage("Ya Existe Registro de Gasto Personales de:", serv.getPersona().getNombreCompleltoSql().toUpperCase());
            return;
        }
        gastoPersonal.setServidorPublico(serv);
        if (gastoPersonal.getServidorPublico() != null) {
            cedula = gastoPersonal.getServidorPublico().getPersona().getIdentificacion();
            gastoPersonal.setIngresoGravado(ingresoGravado());
        }
        sumaIngresos();
    }

    public BigDecimal ingresoGravado() {
        BigDecimal aux, result, dato;
        int dato_dias = 0;
        aux = rmuServidor(gastoPersonal.getServidorPublico().getPersona());
        if (Utils.getAnio(gastoPersonal.getServidorPublico().getFechaIngreso()).equals(Utils.getAnio(new Date()))) {
            dato = mesesAnio.subtract(BigDecimal.valueOf(Utils.getMes(gastoPersonal.getServidorPublico().getFechaIngreso())));
            result = aux.multiply(dato);
            dato_dias = TalentoHumano.diasCalendarioLaboral - Utils.getDia(gastoPersonal.getServidorPublico().getFechaIngreso());
            aux = aux.divide(new BigDecimal(TalentoHumano.diasCalendarioLaboral), 2, RoundingMode.HALF_UP);
            aux = aux.multiply(new BigDecimal(dato_dias + 1));
            result = result.add(aux);
        } else {
            result = aux.multiply(mesesAnio);
        }
        return result.setScale(2, BigDecimal.ROUND_HALF_EVEN);
    }

    public BigDecimal rmuServidor(Cliente servidor) {
        return rmuPersonal = gastoPersonalService.getRMU(gastoPersonal.getEjercicioFiscal(), servidor);
    }

    public void sumaIngresos() {
        BigDecimal result;
        gastoPersonal.setIngresoGravado(gastoPersonal.getIngresoGravado());
        result = gastoPersonal.getIngresoGravado().add(gastoPersonal.getOtrosIngresos());
        gastoPersonal.setTotalIngreso(result);
    }

    public void sumaGastos() {
        BigDecimal result, rmu;
        if (gastoPersonal.getFechaEntrega() != null) {
            if (actualizarServidor(gastoPersonal.getServidorPublico())) {
                gastoPersonal.setActualizacion(Boolean.TRUE);
            }
            result = gastoPersonal.getGastoAlimentacion().add(gastoPersonal.getGastoEducacion())
                    .add(gastoPersonal.getGastoVivienda()).add(gastoPersonal.getGastoVestimenta())
                    .add(gastoPersonal.getGastoSalud()).add(gastoPersonal.getGastoTurismo());
            if (result.floatValue() >= validacionPorcentual(gastoPersonal.getIngresoGravado()).floatValue()) {
                JsfUtil.addWarningMessage("Declaración Personal", "Verifique que los Gastos Proyectados no sobrepasen a los Ingresos Proyectados: $" + gastoPersonal.getIngresoGravado() + "");
            }
            gastoPersonal.setTotalGasto(result);
        } else {
            JsfUtil.addWarningMessage("Declaración Personal", "Fecha de entrega no asignada");
        }

    }

    public BigDecimal validacionPorcentual(BigDecimal rmu) {
        BigDecimal result;
        result = BigDecimal.ZERO;
        result = rmu.multiply(porcentaje);
        return result;
    }

    public BigDecimal mesInt(Date fecha) {
        BigDecimal months;
        Calendar fechaEntrega = Calendar.getInstance();
        fechaEntrega.setTime(fecha);
        Calendar fechaActual = Calendar.getInstance();
        months = new BigDecimal(fechaEntrega.get(Calendar.MONTH));

        return months;
    }

    public void cancelar() {
        this.collapsed = Boolean.TRUE;
        gastoPersonal = new GastoPersonal();
        this.cedula = "";
    }

    public LazyModel<GastoPersonal> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<GastoPersonal> lazy) {
        this.lazy = lazy;
    }

    public GastoPersonal getGastoPersonal() {
        return gastoPersonal;
    }

    public void setGastoPersonal(GastoPersonal gastoPersonal) {
        this.gastoPersonal = gastoPersonal;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public BigDecimal getRmuPersonal() {
        return rmuPersonal;
    }

    public void setRmuPersonal(BigDecimal rmuPersonal) {
        this.rmuPersonal = rmuPersonal;
    }

    public boolean isCollapsed() {
        return collapsed;
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = collapsed;
    }

    public Date getFechaMes() {
        return fechaMes;
    }

    public void setFechaMes(Date fechaMes) {
        this.fechaMes = fechaMes;
    }

    public List<Provincia> getProvincias() {
        return provincias;
    }

    public void setProvincias(List<Provincia> provincias) {
        this.provincias = provincias;
    }

    public List<Canton> getCantones() {
        return cantones;
    }

    public void setCantones(List<Canton> cantones) {
        this.cantones = cantones;
    }

    public List<MasterCatalogo> getPeriodosFiscales() {
        return periodosFiscales;
    }

    public void setPeriodosFiscales(List<MasterCatalogo> periodosFiscales) {
        this.periodosFiscales = periodosFiscales;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public GastoPersonalService getGastoPersonalService() {
        return gastoPersonalService;
    }

    public void setGastoPersonalService(GastoPersonalService gastoPersonalService) {
        this.gastoPersonalService = gastoPersonalService;
    }

    public GastoPersonal getGastoPersonalSeleccionado() {
        return gastoPersonalSeleccionado;
    }

    public void setGastoPersonalSeleccionado(GastoPersonal gastoPersonalSeleccionado) {
        this.gastoPersonalSeleccionado = gastoPersonalSeleccionado;
    }

}
