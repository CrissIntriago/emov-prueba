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
import java.util.Date;
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
@Named(value = "fondosReservaView")
@ViewScoped
public class FondosReservaController implements Serializable {

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

    private List<MasterCatalogo> periodos;
    private OpcionBusqueda busqueda;

    private CatalogoItem fondoReservaItem;
    private LazyModel<FondosReserva> lazy;
    private FondosReserva fondosReserva;
    private FondosReserva fondosReservaSeleccionado;
    private List<TipoRol> rolesMensuales;
    private CatalogoItem registrado;
    private TipoRol rolSeleccionado;
    private List<ValoresDistributivo> valores;
    private BigDecimal totalAcumula;
    private BigDecimal totalNoAcumula;
    private int todos;
    private boolean acumula;
    private boolean accion;
    private String tipoArchivo;

    @PostConstruct
    public void init() {
        fondoReservaItem = catalogoItemService.getEstadoRol("ACU-FONDOS-RESERVA");
//        System.out.println("id fondo " + fondoReservaItem.getId());
        fondosReserva = new FondosReserva();
        fondosReservaSeleccionado = new FondosReserva();
        fondosReserva.setAcumulacionFondos(new AcumulacionFondoReserva());
        fondosReserva.setDiasLaborado(new DiasLaborado());
        fondosReserva.setDistributivoEscala(new DistributivoEscala());
        rolSeleccionado = new TipoRol();
//        rolGeneral = catalogoItemService.getEstadoRol("rol_general");
        busqueda = new OpcionBusqueda();
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        rolesMensuales = tipoRolService.listaRolesFondoRe(busqueda.getAnio());
        registrado = catalogoItemService.getEstadoRol("registrado-rol");
        totalAcumula = BigDecimal.ZERO;
        totalNoAcumula = BigDecimal.ZERO;
    }

    public void actualizarRoles() {
        rolesMensuales = tipoRolService.listaRolesFondoRe(busqueda.getAnio());
        rolesMensuales.sort(Comparator.comparing(TipoRol::getId));
    }

    public void buscar() {
        this.rolSeleccionado = fondosReserva.getTipoRol();
        List<FondosReserva> lista = fondosService.getListFondos(rolSeleccionado, fondoReservaItem);
        List<AcumulacionFondoReserva> acumulacionFondosList;
        if (lista.isEmpty()) {
            acumulacionFondosList = acumulacionFondoService.listaAcumulacionFondosEverybody(fondoReservaItem);
            fondosService.addListFR(acumulacionFondosList, rolSeleccionado, rolSeleccionado.getAnio());
        }
        lazy = new LazyModel<>(FondosReserva.class);
        lazy.setDistinct(false);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getFilterss().put("acumulacionFondos.tipoAcumulacion", fondoReservaItem);
        lazy.setDistinct(false);
        if (rolSeleccionado.getEstadoAprobacion().equals(registrado)) {
            actualizarDatos();
        } else {
        }
        lazy.getSorteds().put("acumulacionFondos.servidor.persona.apellido", "ASC");
        totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.TRUE);
        totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.FALSE);
    }

    public void actualizarDatos() {
        if (fondosReserva.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
        List<FondosReserva> fondoActual = fondosService.getListFondos(rolSeleccionado, fondoReservaItem);
        List<AcumulacionFondoReserva> acumulacionFondosLis = acumulacionFondoService.listaAcumulacionFondosEverybody(fondoReservaItem);
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
            fondosService.addListFR(agregar, rolSeleccionado, rolSeleccionado.getAnio());
        }
        fondosService.actualizarValoresFR(rolSeleccionado, fondoReservaItem);
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

    public void eliminar(FondosReserva fondo) {
        fondo.setEstado(Boolean.FALSE);
        fondosService.edit(fondo);
        totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.TRUE);
        totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.FALSE);
    }

    public void eliminarRegistros() {
        if (fondosReserva.getTipoRol() == null) {
            JsfUtil.addWarningMessage("Advertencia", "Debe Seleccionar un Rol.");
            return;
        }
        List<FondosReserva> fondoActual = fondosService.getListFondos(rolSeleccionado, fondoReservaItem);
        fondosService.eliminarList(fondoActual);
        totalAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.TRUE);
        totalNoAcumula = acumulacionFondoService.getValorTotal(rolSeleccionado, fondoReservaItem, Boolean.FALSE);
        JsfUtil.addSuccessMessage("Información", "Datos de Rol Eliminados Correctamente.");
    }

    public void aprobarRol() {
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Rol no Seleccionando");
            return;
        }
        if (rolSeleccionado.getEstadoAprobacion().getCodigo().equals("aprobado-rol") || rolSeleccionado.getEstadoAprobacion().getCodigo().equals("pagado-rol")) {
            JsfUtil.addWarningMessage("Información", "Rol se encuentra " + rolSeleccionado.getEstadoAprobacion().getTexto());
            return;
        }
        rolSeleccionado.setEstadoAprobacion(catalogoItemService.getEstadoRol("aprobado-rol"));
        rolSeleccionado.setUsuarioRegistraAprueba(userSession.getNameUser());
        rolSeleccionado.setUsuarioModifica(userSession.getNameUser());
        rolSeleccionado.setFechaModificacion(new Date());
        tipoRolService.edit(rolSeleccionado);
        JsfUtil.addSuccessMessage("Información", "Rol " + rolSeleccionado.getMes().getDescripcion() + " Aprobado Correctamente.");
    }

    public void imprimirReporte(String tipoArchivo, Boolean accion, FondosReserva fondosReserva) {
        this.accion = accion;
        fondosReservaSeleccionado = fondosReserva;
        this.tipoArchivo = tipoArchivo;
        todos = 1;
        acumula = false;
        PrimeFaces.current().executeScript("PF('parametroReporte').show()");
        PrimeFaces.current().ajax().update("reporteForm");
    }

    public void imprimirReporte() {
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        Distributivo distributivoRev = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        servletSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        servletSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        servletSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        servletSession.addParametro("ci_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        servletSession.addParametro("nombre_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getNombreCompleto() : "");
        servletSession.addParametro("cargo_revisor", distributivoRev != null ? distributivoRev.getCargo().getNombreCargo() : "");
        /*Imprimir reporte seleccionado*/
        if (tipoArchivo.equalsIgnoreCase("xlsx")) {
            servletSession.setOnePagePerSheet(true);
        }
        String nombreReporte = "";
        if (accion) {
            nombreReporte = "fondosReservaIndividual";
//            fondoSeleccionado
        } else {
            nombreReporte = "fondosReservaGeneral";
            servletSession.addParametro("filtroTodos", todos);
            servletSession.addParametro("acumula", acumula);
        }
        servletSession.setNombreReporte(nombreReporte);
        servletSession.setContentType(tipoArchivo);
        servletSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        PrimeFaces.current().executeScript("PF('parametroReporte').hide()");
        PrimeFaces.current().ajax().update("reporteForm");
    }

    public LazyModel<FondosReserva> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<FondosReserva> lazy) {
        this.lazy = lazy;
    }

    public FondosReserva getFondosReserva() {
        return fondosReserva;
    }

    public void setFondosReserva(FondosReserva fondosReserva) {
        this.fondosReserva = fondosReserva;
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

    public FondosReserva getFondosReservaSeleccionado() {
        return fondosReservaSeleccionado;
    }

    public void setFondosReservaSeleccionado(FondosReserva fondosReservaSeleccionado) {
        this.fondosReservaSeleccionado = fondosReservaSeleccionado;
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

    public int getTodos() {
        return todos;
    }

    public void setTodos(int todos) {
        this.todos = todos;
    }

    public boolean isAcumula() {
        return acumula;
    }

    public void setAcumula(boolean acumula) {
        this.acumula = acumula;
    }

}
