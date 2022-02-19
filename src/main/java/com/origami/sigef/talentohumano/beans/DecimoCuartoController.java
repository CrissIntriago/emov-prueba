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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "decimoCuartoView")
@ViewScoped
public class DecimoCuartoController implements Serializable {

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

    private CatalogoItem decimoCuartoItem;
    private LazyModel<FondosReserva> lazy;
    private FondosReserva decimoCuarto;
    private FondosReserva decimoCuartoSeleccionado;
    private short periodo;
    private List<TipoRol> rolesMensuales;
    private CatalogoItem registrado;
    private TipoRol rolSeleccionado;
    private List<ValoresDistributivo> valores;
    private BigDecimal salarioBasico;
    private CatalogoItem rolGeneral;

    private BigDecimal totalAcumula;
    private BigDecimal totalNoAcumula;
    private BigDecimal totalTotal;

    private List<MasterCatalogo> periodos;
    private OpcionBusqueda busqueda;

    private int parametroEstado;
    private TipoRol tipoRolSeleccionado;

    @PostConstruct
    public void init() {
        decimoCuartoItem = catalogoItemService.getEstadoRol("ACU-DECIMO-4to");
        decimoCuarto = new FondosReserva();
        decimoCuartoSeleccionado = new FondosReserva();
        decimoCuarto.setAcumulacionFondos(new AcumulacionFondoReserva());
        decimoCuarto.setDiasLaborado(new DiasLaborado());
        decimoCuarto.setDistributivoEscala(new DistributivoEscala());
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

    public void buscar() throws ParseException {
        boolean bandera = false;
        int anio;
        List<FondosReserva> lista;
        this.rolSeleccionado = decimoCuarto.getTipoRol();
        if (rolSeleccionado.getId() != null) {
            salarioBasico = valorService.valorParametrizado("SBU", rolSeleccionado);
        } else {
            return;
        }
        if (salarioBasico.intValue() == 0) {
            JsfUtil.addWarningMessage("Salario Basico Unificado", "Verifique que el Rubro de SBU este creado/Actualizado en mantenimineto de Valores");
            return;
        }
        if (registroDiasLaborador(rolSeleccionado)) {
            lazy = null;
            JsfUtil.addWarningMessage("Información", "No se encontraron registro de días laborados con el Rol Seleccionando");
            return;
        }
        if (rolSeleccionado.getMes().getCodigo().equals("ENERO") || rolSeleccionado.getMes().getCodigo().equals("FEBRERO")) {
            bandera = true;
        }
        if (bandera) {
            //desde 01/03/(año anterior)  hasta (ultimo dia del mes)/02/(año del rol)
            anio = rolSeleccionado.getAnio().intValue() - 1;
            lista = fondosService.getListFondosReserv(rolSeleccionado, decimoCuartoItem, TalentoHumano.fechaInicio((short) anio, 01, 03), TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 02));
        } else {
            //desde 01/03/(año del rol)  hasta (ultimo dia del mes)/02/(año siguiente)
            anio = rolSeleccionado.getAnio().intValue() + 1;
            lista = fondosService.getListFondosReserv(rolSeleccionado, decimoCuartoItem, TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 03), TalentoHumano.fechaCierre((short) anio, 02));
        }
        List<AcumulacionFondoReserva> acumulacionFondosList;
        if (lista.isEmpty()) {
            if (bandera) {
                acumulacionFondosList = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoCuartoItem, TalentoHumano.fechaInicio((short) anio, 01, 03), TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 02));
            } else {
                acumulacionFondosList = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoCuartoItem, TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 03), TalentoHumano.fechaCierre((short) anio, 02));
            }
            fondosService.addList(acumulacionFondosList, rolSeleccionado, salarioBasico, rolSeleccionado.getAnio());
        }

        if (rolSeleccionado.getEstadoAprobacion().equals(registrado)) {
            actualizarDatos();
        }
        lazy = new LazyModel<>(FondosReserva.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getFilterss().put("acumulacionFondos.tipoAcumulacion", decimoCuartoItem);
        lazy.setDistinct(false);
        if (bandera) {
            //desde 01/03/(año anterior)  (hasta ultimo dia del mes)/02/(año del rol)
            lazy.getFilterss().put("acumulacionFondos.fechaInicio:gte", TalentoHumano.fechaInicio((short) anio, 01, 03));
            lazy.getFilterss().put("acumulacionFondos.fechaFin:lte", TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 02));
        } else {
            //desde 01/03/(año del rol)  hasta (ultimo dia del mes)/02/(año siguiente)
            lazy.getFilterss().put("acumulacionFondos.fechaInicio:gte", TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 03));
            lazy.getFilterss().put("acumulacionFondos.fechaFin:lte", TalentoHumano.fechaCierre((short) anio, 02));
        }
        lazy.getSorteds().put("acumulacionFondos.servidor.persona.apellido", "ASC");
        this.valorestotales();
    }

    public void valorestotales() {
        totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, decimoCuartoItem, Boolean.TRUE);
        totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, decimoCuartoItem, Boolean.FALSE);
        totalTotal = acumulacionFondoService.getValorTotalTotal(rolSeleccionado, decimoCuartoItem);
    }

    public void actualizarDatos() {
        if (decimoCuarto.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
//        System.out.println("entro a actualizar datos");

        int anio;
        List<AcumulacionFondoReserva> acumulacionFondosLis;
        List<AcumulacionFondoReserva> aux;
        this.rolSeleccionado = decimoCuarto.getTipoRol();
        if (rolSeleccionado.getMes().getCodigo().equals("ENERO") || rolSeleccionado.getMes().getCodigo().equals("FEBRERO")) {
            //desde 01/03/(año anterior)  (hasta ultimo dia del mes)/02/(año del rol)
            anio = rolSeleccionado.getAnio().intValue() - 1;
            aux = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoCuartoItem, TalentoHumano.fechaInicio((short) anio, 01, 03), TalentoHumano.fechaCierre(rolSeleccionado.getAnio(), 02));
            acumulacionFondosLis = fondosService.filtrarList(aux, rolSeleccionado);
        } else {
            //desde 01/03/(año del rol)  hasta (ultimo dia del mes)/02/(año siguiente)
            anio = rolSeleccionado.getAnio().intValue() + 1;
            aux = acumulacionFondoService.listaAcumulacionFondosReservEverybody(decimoCuartoItem, TalentoHumano.fechaInicio(rolSeleccionado.getAnio(), 01, 03), TalentoHumano.fechaCierre((short) anio, 02));
            acumulacionFondosLis = fondosService.filtrarList(aux, rolSeleccionado);
        }
        List<FondosReserva> fondoActual = fondosService.getListFondos(rolSeleccionado, decimoCuartoItem);
        List<FondosReserva> delete = new ArrayList<>();
        List<AcumulacionFondoReserva> agregar = new ArrayList<>();
        if (salarioBasico.intValue() == 0) {
            JsfUtil.addWarningMessage("", "No Hay datos que Actualizar");
            return;
        }
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
//            eliminarList(delete);
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
            fondosService.addList(agregar, rolSeleccionado, salarioBasico, rolSeleccionado.getAnio());
        }
        fondosService.actualizarValores(rolSeleccionado, decimoCuartoItem, salarioBasico);
        JsfUtil.addSuccessMessage("Información", "Datos de Rol Cargados Correctamente.");

    }

    public boolean registroDiasLaborador(TipoRol rol) {
        List<DiasLaborado> lista = diasService.getDiasxTipoRol(rol);
        return lista.isEmpty();
    }

    public void eliminar(FondosReserva fondo) {
        fondo.setEstado(Boolean.FALSE);
        fondosService.edit(fondo);
        this.valorestotales();
    }

    public void eliminarRegistros() {
        if (decimoCuarto.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
        List<FondosReserva> fondoActual = fondosService.getListFondos(rolSeleccionado, decimoCuartoItem);
        if (salarioBasico.intValue() == 0) {
            return;
        }
        fondosService.eliminarList(fondoActual);
        JsfUtil.addSuccessMessage("Información", "Datos de Rol Eliminados Correctamente.");
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
        servletSession.addParametro("codigo", decimoCuartoItem.getId());
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

    public CatalogoItem getDecimoCuartoItem() {
        return decimoCuartoItem;
    }

    public void setDecimoCuartoItem(CatalogoItem decimoCuartoItem) {
        this.decimoCuartoItem = decimoCuartoItem;
    }

    public LazyModel<FondosReserva> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FondosReserva> lazy) {
        this.lazy = lazy;
    }

    public FondosReserva getDecimoCuarto() {
        return decimoCuarto;
    }

    public void setDecimoCuarto(FondosReserva decimoCuarto) {
        this.decimoCuarto = decimoCuarto;
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

    public FondosReserva getDecimoCuartoSeleccionado() {
        return decimoCuartoSeleccionado;
    }

    public void setDecimoCuartoSeleccionado(FondosReserva decimoCuartoSeleccionado) {
        this.decimoCuartoSeleccionado = decimoCuartoSeleccionado;
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
