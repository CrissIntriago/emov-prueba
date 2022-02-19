/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.AcumulacionFondoReserva;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.FondosReserva;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresDistributivo;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.AcumulacionFondoReservaService;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.FondosReservaService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresDistributivoService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "decimoTerceroView")
@ViewScoped
public class DecimoTerceroController implements Serializable {

    @Inject
    private FondosReservaService fondosService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private AcumulacionFondoReservaService acumulacionFondoService;
    @Inject
    private DiasLaboradoService diasService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private UserSession userSession;
    @Inject
    private DistributivoEscalaService distributivoEscalaService;
    @Inject
    private ValoresDistributivoService valorService;
    @Inject
    private MasterCatalogoService catalogoService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private ClienteService clienteService;

    private CatalogoItem decimoTercerItem;
    private LazyModel<FondosReserva> lazy;
    private FondosReserva decimotercero;
    private FondosReserva decimoterceroSeleccionado;
    private Short periodo;
    private List<TipoRol> rolesMensuales;
    private CatalogoItem registrado;
    private TipoRol rolSeleccionado;
    private List<ValoresDistributivo> valores;
    private BigDecimal salarioBasico;
    private BigDecimal totalAcumula;
    private BigDecimal totalNoAcumula;
    private BigDecimal totalTotal;

    private List<MasterCatalogo> periodos;
    private OpcionBusqueda busqueda;
    private int parametroEstado;
    private TipoRol tipoRolSeleccionado;

    @PostConstruct
    public void init() {
        decimoTercerItem = catalogoItemService.getEstadoRol("ACU-DECIMO-3ro");
        decimotercero = new FondosReserva();
        decimoterceroSeleccionado = new FondosReserva();
        decimotercero.setAcumulacionFondos(new AcumulacionFondoReserva());
        decimotercero.setDiasLaborado(new DiasLaborado());
        decimotercero.setDistributivoEscala(new DistributivoEscala());
        rolSeleccionado = new TipoRol();
        busqueda = new OpcionBusqueda();
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        totalAcumula = BigDecimal.ZERO;
        totalNoAcumula = BigDecimal.ZERO;
        totalTotal = BigDecimal.ZERO;
    }

    public void actualizarRoles() {
        rolesMensuales = tipoRolService.listaRolesXanio(busqueda.getAnio());
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
    }

    public void buscar() {
        boolean bandera = false;
        int anio;
        List<FondosReserva> lista;
        this.rolSeleccionado = decimotercero.getTipoRol();
        if (registroDiasLaborador(rolSeleccionado)) {
            lazy = null;
            JsfUtil.addWarningMessage("Información", "No se encontraron registro de días laborados con el Rol Seleccionando");
            return;
        }
        if (rolSeleccionado.getMes().getCodigo().equals("DICIEMBRE")) {
            bandera = true;
        }
        if (bandera) {
            //desde 01/12((año del rol)  hasta (ultimo dia del mes)/11/(año siguiente)
            anio = rolSeleccionado.getAnio().intValue() + 1;
            lista = fondosService.getListFondosReserv(rolSeleccionado, decimoTercerItem, TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 12), TalentoHumano.fechaCierre((short) anio, 11));
        } else {
            //desde 01/12((año anterior)  hasta (ultimo dia del mes)/11/(año del rol)
            anio = rolSeleccionado.getAnio().intValue() - 1;
            lista = fondosService.getListFondosReserv(rolSeleccionado, decimoTercerItem, TalentoHumano.fechaInicio((short) anio, 01, 12), TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 11));
        }
        List<AcumulacionFondoReserva> acumulacionFondosList;
        if (lista.isEmpty()) {
            if (bandera) {
                //desde 01/12((año del rol)  hasta (ultimo dia del mes)/11/(año siguiente)
                acumulacionFondosList = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoTercerItem, TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 1, 12), TalentoHumano.fechaCierre((short) anio, 11));
            } else {
                //desde 01/12((año anterior)  hasta (ultimo dia del mes)/11/(año del rol)
                acumulacionFondosList = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoTercerItem, TalentoHumano.fechaInicio((short) anio, 1, 12), TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 11));
            }
            fondosService.addList(acumulacionFondosList, rolSeleccionado, null, rolSeleccionado.getAnio());
        }
        if (Objects.equals(registrado, rolSeleccionado.getEstadoAprobacion())) {
            actualizarDatos();
        }
        lazy = new LazyModel<>(FondosReserva.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getFilterss().put("acumulacionFondos.tipoAcumulacion", decimoTercerItem);
        lazy.setDistinct(false);
        if (bandera) {
            //desde 01/12((año del rol)  hasta (ultimo dia del mes)/11/(año siguiente)
            lazy.getFilterss().put("acumulacionFondos.fechaInicio:gte", TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 12));
            lazy.getFilterss().put("acumulacionFondos.fechaFin:lte", TalentoHumano.fechaCierre((short) anio, 11));
        } else {
            //desde 01/12((año anterior)  hasta (ultimo dia del mes)/11/(año del rol)
            lazy.getFilterss().put("acumulacionFondos.fechaInicio:gte", TalentoHumano.fechaInicio((short) anio, 01, 12));
            lazy.getFilterss().put("acumulacionFondos.fechaFin:lte", TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 11));
        }
        lazy.getSorteds().put("acumulacionFondos.servidor.persona.apellido", "ASC");
        this.valoresTotales();
    }

    public void valoresTotales() {
        totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, decimoTercerItem, Boolean.TRUE);
        totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, decimoTercerItem, Boolean.FALSE);
        totalTotal = acumulacionFondoService.getValorTotalTotal(rolSeleccionado, decimoTercerItem);
    }

    public void actualizarDatos() {
        if (decimotercero.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
        int anio;
        List<AcumulacionFondoReserva> acumulacionFondosLis;
        List<AcumulacionFondoReserva> aux;
        this.rolSeleccionado = decimotercero.getTipoRol();
        if (rolSeleccionado.getMes().getCodigo().equals("DICIEMBRE")) {
            anio = rolSeleccionado.getAnio().intValue() + 1;
            aux = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoTercerItem, TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 12), TalentoHumano.fechaCierre((short) anio, 11));
            acumulacionFondosLis = fondosService.filtrarList(aux, rolSeleccionado);
        } else {
            anio = rolSeleccionado.getAnio().intValue() - 1;
            aux = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoTercerItem, TalentoHumano.fechaInicio((short) anio, 01, 12), TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 11));
            acumulacionFondosLis = fondosService.filtrarList(aux, rolSeleccionado);
        }
        List<FondosReserva> fondoActual = fondosService.getListFondos(rolSeleccionado, decimoTercerItem);
        List<FondosReserva> delete = new ArrayList<>();
        List<AcumulacionFondoReserva> agregar = new ArrayList<>();
        if (fondoActual.size() > acumulacionFondosLis.size()) {
            for (FondosReserva f : fondoActual) {
                int cont = 0;
                for (AcumulacionFondoReserva a : acumulacionFondosLis) {
                    cont++;
                    if (f.getAcumulacionFondos().equals(a)) {
                        cont--;
                    }
                }
                if (cont == acumulacionFondosLis.size()) {
                    delete.add(f);
                }
            }
            fondosService.eliminarList(delete);
        }
        if (fondoActual.size() < acumulacionFondosLis.size()) {
            for (AcumulacionFondoReserva a : acumulacionFondosLis) {
                int cont = 0;
                for (FondosReserva f : fondoActual) {
                    cont++;
                    if (a.equals(f.getAcumulacionFondos())) {
                        cont--;
                    }
                }
                if (cont == fondoActual.size()) {
                    agregar.add(a);
                }
            }
            fondosService.addList(agregar, rolSeleccionado, null, rolSeleccionado.getAnio());
        }
        fondosService.actualizarValores(rolSeleccionado, decimoTercerItem, null);
        JsfUtil.addSuccessMessage("Información", "Datos de Rol Cargados Correctamente.");

    }

    public boolean validarDiasLab(Servidor s) {
        List<DiasLaborado> lista = diasService.getDiasxTipoRol(rolSeleccionado);
        for (DiasLaborado d : lista) {
            if (s.equals(d.getServidor())) {
                return true;
            }
        }
        return false;
    }

    public void generarReporte() {
        parametroEstado = 0;
        PrimeFaces.current().executeScript("PF('dlgGenerarReporte').show()");
        PrimeFaces.current().ajax().update("formReporte");
    }

    public void closeDlgReporte() {
        if (tipoRolSeleccionado == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un rol");
            return;
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        servletSession.addParametro("id_tipo_rol", tipoRolSeleccionado.getId());
        servletSession.addParametro("parametro", parametroEstado);
        servletSession.addParametro("codigo", decimoTercerItem.getId());
        servletSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        servletSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        servletSession.setNombreReporte("rol_decimos");
        servletSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        PrimeFaces.current().executeScript("PF('dlgGenerarReporte').hide()");
        parametroEstado = 0;
        tipoRolSeleccionado = null;
    }

    public boolean registroDiasLaborador(TipoRol rol) {
        List<DiasLaborado> lista = diasService.getDiasxTipoRol(rol);
        return lista.isEmpty();
    }

    public void eliminar(FondosReserva fondo) {
        fondo.setEstado(Boolean.FALSE);
        fondosService.edit(fondo);
        this.valoresTotales();
    }

    public void eliminarRegistros() {
        if (decimotercero.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
        List<FondosReserva> fondoActual = fondosService.getListFondos(rolSeleccionado, decimoTercerItem);
        fondosService.eliminarList(fondoActual);
        JsfUtil.addSuccessMessage("Información", "Datos de Rol Eliminados Correctamente.");

    }

    public CatalogoItem getDecimoTercerItem() {
        return decimoTercerItem;
    }

    public void setDecimoTercerItem(CatalogoItem decimoTercerItem) {
        this.decimoTercerItem = decimoTercerItem;
    }

    public LazyModel<FondosReserva> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FondosReserva> lazy) {
        this.lazy = lazy;
    }

    public FondosReserva getDecimotercero() {
        return decimotercero;
    }

    public void setDecimotercero(FondosReserva decimotercero) {
        this.decimotercero = decimotercero;
    }

    public short getPeriodo() {
        return periodo;
    }

    public void setPeriodo(short periodo) {
        this.periodo = periodo;
    }

    public List<TipoRol> getRolesMensuales() {
        return rolesMensuales;
    }

    public void setRolesMensuales(List<TipoRol> rolesMensuales) {
        this.rolesMensuales = rolesMensuales;
    }

    public CatalogoItem getRegistrado() {
        return registrado;
    }

    public void setRegistrado(CatalogoItem registrado) {
        this.registrado = registrado;
    }

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public List<ValoresDistributivo> getValores() {
        return valores;
    }

    public void setValores(List<ValoresDistributivo> valores) {
        this.valores = valores;
    }

    public FondosReserva getDecimoterceroSeleccionado() {
        return decimoterceroSeleccionado;
    }

    public void setDecimoterceroSeleccionado(FondosReserva decimoterceroSeleccionado) {
        this.decimoterceroSeleccionado = decimoterceroSeleccionado;
    }

    public BigDecimal getTotalAcumula() {
        return totalAcumula;
    }

    public void setTotalAcumula(BigDecimal totalAcumula) {
        this.totalAcumula = totalAcumula;
    }

    public BigDecimal getTotalNoAcumula() {
        return totalNoAcumula;
    }

    public void setTotalNoAcumula(BigDecimal totalNoAcumula) {
        this.totalNoAcumula = totalNoAcumula;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public BigDecimal getTotalTotal() {
        return totalTotal;
    }

    public void setTotalTotal(BigDecimal totalTotal) {
        this.totalTotal = totalTotal;
    }

    public int getParametroEstado() {
        return parametroEstado;
    }

    public void setParametroEstado(int parametroEstado) {
        this.parametroEstado = parametroEstado;
    }

    public TipoRol getTipoRolSeleccionado() {
        return tipoRolSeleccionado;
    }

    public void setTipoRolSeleccionado(TipoRol tipoRolSeleccionado) {
        this.tipoRolSeleccionado = tipoRolSeleccionado;
    }

}
