/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.talentohumano.beans;

import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.DiasLaborado;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.HoraExtraSuplementaria;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.RolHorasExtrasSuplementarias;
import com.origami.sigef.common.entities.RolHorasValores;
import com.origami.sigef.common.entities.RolesDePago;
import com.origami.sigef.resource.talento_humano.entities.Servidor;
import com.origami.sigef.common.entities.TipoRol;
import com.origami.sigef.common.entities.ValoresRoles;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.DistributivoService;
import com.origami.sigef.talentohumano.services.HoraExtraSuplementariaService;
import com.origami.sigef.talentohumano.services.RolHoraExtrasSuplementariaService;
import com.origami.sigef.talentohumano.services.RolHoraValoresService;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
 * @author OrigamiEC
 */
@Named(value = "rolHorasExtras")
@ViewScoped
public class RolHorasExtrasBeans implements Serializable {

    @Inject
    private RolHoraExtrasSuplementariaService horaExtrasService;
    @Inject
    private RolHoraValoresService rolHoraValoresService;
    @Inject
    private UserSession userSession;
    @Inject
    private RolesDePagoService rolesPagoService;
    @Inject
    private ValoresRolesService valorRolesService;
    @Inject
    private TipoRolService tipoRolService;
    @Inject
    private MasterCatalogoService catalogoService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private DiasLaboradoService diasService;
    @Inject
    private DistributivoEscalaService distributivoEscaService;
    @Inject
    private HoraExtraSuplementariaService horasService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private DistributivoService distributivoService;

    private LazyModel<RolHorasExtrasSuplementarias> lazy;
    private RolHorasExtrasSuplementarias horaExtra;
    private RolHorasExtrasSuplementarias horaExtraSeleccionada;
    private RolHorasValores rolHoraValor;
    private List<TipoRol> listaRol;
    private CatalogoItem tipoRol;
    private List<MasterCatalogo> periodos;
    private List<RolesDePago> listRolesPago;
    private List<RolesDePago> auxLista;
    private ValoresRoles valorRol;
    private TipoRol rolSeleccionado;
    private OpcionBusqueda busqueda;
    private DistributivoEscala escala;

    private BigDecimal totalValor;
    private Short totalHoras;

    @PostConstruct
    public void init() {
        busqueda = new OpcionBusqueda();
        rolSeleccionado = new TipoRol();
        tipoRol = catalogoItemService.getEstadoRol("ROL-HORAS-EXTRAS");
        auxLista = rolesPagoService.findRolesXPeriodo(busqueda.getAnio());
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listaRol = tipoRolService.listaRoles(tipoRol);
        listaRol.sort(Comparator.comparing(TipoRol::getId));
    }

    public void generarRol() {
        if (getListaLiquidar().isEmpty()) {
            listRolesPago = filtrarRolesPagos();
            for (RolesDePago r : listRolesPago) {
                escala = new DistributivoEscala();
                newObject();
                if (!distributivoService.distributivoReformado(r.getServidor().getDistributivo(), "RAU")) {
                    escala = distributivoEscaService.getEscalaDistributivoAnio(r.getServidor().getDistributivo(), busqueda);
                    if (getDiasLaborado(r.getServidor())
                            && getHorasExtras(r.getServidor())) {
                        cargarDatosRol(r);
                    }
                }
            }
        }
        lazy = new LazyModel<>(RolHorasExtrasSuplementarias.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getSorteds().put("servidorPartida.servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);
        getTotalValores();
        PrimeFaces.current().ajax().update("formMain");
    }

    public void cargarDatosRol(RolesDePago r) {
        horaExtra.setFechaCreacion(new Date());
        horaExtra.setServidorPartida(r);
        horaExtra.setTipoRol(rolSeleccionado);
        horaExtra.setUsuarioCreacion(userSession.getNameUser());
        horaExtra.setSueldo(escala.getRemuneracionDolares());
        horaExtra.setTotalHora(getTotalHorasExtras(horaExtra.getServidorPartida().getServidor()));
        if (r.getServidor().getFechaIngreso() != null && r.getServidor().getFechaSalida() != null) {
            if (TalentoHumano.validarFechaFin(r.getServidor().getFechaSalida(), rolSeleccionado)) {
                valorRol = valorRolesService.FindValoresTipo(r, "HORAS_SUP");
                if (valorRol != null) {
                    horaExtra = horaExtrasService.create(horaExtra);
                    setearValorHoras(valorRol);
                }
            }
        }
        if (r.getServidor().getFechaIngreso() != null && r.getServidor().getFechaSalida() == null) {
//            System.out.println("servidor-->> " + r.getServidor().getPersona().getNombreCompleto());
            valorRol = valorRolesService.FindValoresTipo(r, "HORAS_SUP");
            if (valorRol != null) {
                horaExtra = horaExtrasService.create(horaExtra);
                setearValorHoras(valorRol);
            }
        }
    }

    public void setearValorHoras(ValoresRoles valorRol) {
        HoraExtraSuplementaria hora = new HoraExtraSuplementaria();
        hora = horasService.getHoraExtraSuplementaria(valorRol.getRolPago().getServidor(), rolSeleccionado);
        Double totalValorHora;
        BigDecimal valorHorasExtras = BigDecimal.ZERO;
        BigDecimal valorHorasSuple = BigDecimal.ZERO;
        if (hora.getHoraExtras() > 0) {
            newValorHoraRol();
            valorHorasExtras = getHorasExtras(horaExtra.getServidorPartida(), hora);
            rolHoraValor.setValorHora(valorHorasExtras);
            rolHoraValor.setRolHora(horaExtra);
            rolHoraValor.setValoresRoles(valorRol);
            rolHoraValor.setTipoHora("RECARGO 100%");
            rolHoraValor.setNumHoras(hora.getHoraExtras());
            rolHoraValor = rolHoraValoresService.create(rolHoraValor);
        }
        if (hora.getHoraSuplementaria() > 0) {
            newValorHoraRol();
            valorHorasSuple = getHorasSuplementaria(horaExtra.getServidorPartida(), hora);
            rolHoraValor.setValorHora(valorHorasSuple);
            rolHoraValor.setRolHora(horaExtra);
            rolHoraValor.setValoresRoles(valorRol);
            rolHoraValor.setTipoHora("RECARGO 50%");
            rolHoraValor.setNumHoras(hora.getHoraSuplementaria());
            rolHoraValor = rolHoraValoresService.create(rolHoraValor);
        }
        totalValorHora = getHorasExtras(horaExtra.getServidorPartida(), hora).doubleValue() + getHorasSuplementaria(horaExtra.getServidorPartida(), hora).doubleValue();
        horaExtra.setNetoRecibir(new BigDecimal(totalValorHora));
        horaExtrasService.edit(horaExtra);
    }

    public void mesesRoles() {
        listaRol = tipoRolService.listaRoles(busqueda.getAnio(), tipoRol);
    }

    public void eliminar(RolHorasExtrasSuplementarias hora) {
        List<RolHorasValores> rubros = getListaValorHora(hora);
        for (RolHorasValores rv : rubros) {
            rv.setEstado(Boolean.FALSE);
            rolHoraValoresService.edit(rv);
        }
        hora.setEstado(Boolean.FALSE);
        horaExtrasService.edit(hora);
        getTotalValores();
    }

    public void generarReportes(int num, RolHorasExtrasSuplementarias hora) {
        Boolean etiqueta = Boolean.FALSE;
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (!rolSeleccionado.getEstadoAprobacion().getCodigo().equals("registrado-rol")) {
            etiqueta = Boolean.TRUE;
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.maximaAutoridad));
        switch (num) {
            case 1:
                serveltSession.setNombreReporte("rol_resumen");
                break;
            case 2:
                serveltSession.setNombreReporte("rol_all");
                serveltSession.addParametro("mes", rolSeleccionado.getMes().getCodigo().toUpperCase());
                break;
            case 3:
                serveltSession.setNombreReporte("rol_general");
                break;
            case 4:
                serveltSession.setNombreReporte("rubroCuentaPartida");
                break;
            default:
                serveltSession.setNombreReporte("rol_individual");
                serveltSession.addParametro("id", hora.getId());
                serveltSession.addParametro("mes", rolSeleccionado.getMes().getCodigo().toUpperCase());
                break;
        }
        serveltSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        serveltSession.addParametro("MOSTRAR_ETIQUETA_APROBADO", etiqueta);
        serveltSession.addParametro("ci_resp", distributivo.getServidorPublico().getPersona().getIdentificacionCompleta());
        serveltSession.addParametro("nombre_resp", distributivo.getServidorPublico().getPersona().getNombreCompleto());
        serveltSession.addParametro("cargo_resp", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.titularTH).getTexto());
        serveltSession.addParametro("ci_max", distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta());
        serveltSession.addParametro("nombre_max", distributivoMax.getServidorPublico().getPersona().getNombreCompleto());
        serveltSession.addParametro("cargo_max", catalogoItemService.getCatalogoI("ROL-CATEGORIA", RolUsuario.maximaAutoridad).getTexto());
        serveltSession.setNombreSubCarpeta("RolHorasExtrasSup");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void eliminarRegistro() {
        int executeLiquidacion = 0;
        List<RolHorasExtrasSuplementarias> listRol = horaExtrasService.getListaRolHoras(rolSeleccionado);
        if (listRol.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No existen datos a Actualizar");
            return;
        }
        for (RolHorasExtrasSuplementarias lq : listRol) {
            lq.setFechaModificacion(new Date());
            lq.setUsuarioModifica(userSession.getNameUser());
            horaExtrasService.edit(lq);
            rolHoraValoresService.actualizarEstadoRubro(Boolean.FALSE, lq);
        }
        executeLiquidacion = horaExtrasService.actualizarEstadoLiquidacion(Boolean.FALSE, rolSeleccionado);
        if (executeLiquidacion > listRol.size()) {
            generarRol();
        } else {
            JsfUtil.addWarningMessage("Advertencia", "Verificar o Eliminar Datos");
        }
    }

    public void newObject() {
        horaExtra = new RolHorasExtrasSuplementarias();
        horaExtra.setTipoRol(new TipoRol());
        horaExtra.setServidorPartida(new RolesDePago());
    }

    public void newValorHoraRol() {
        rolHoraValor = new RolHorasValores();
        rolHoraValor.setRolHora(new RolHorasExtrasSuplementarias());
        rolHoraValor.setValoresRoles(new ValoresRoles());
    }

    public List<RolesDePago> filtrarRolesPagos() {
        List<RolesDePago> result = new ArrayList<>();
        if (!auxLista.isEmpty()) {
            for (RolesDePago r : auxLista) {
                if (TalentoHumano.validarFechaInicio(r.getServidor().getFechaIngreso(), rolSeleccionado)) {
                    result.add(r);
                }
            }
        }
        return result;
    }

    public Boolean getDiasLaborado(Servidor s) {
        DiasLaborado dias = getDias(s);
        return dias != null && dias.getDias() > 0;
    }

    public HoraExtraSuplementaria getHoras(Servidor s) {
        return horasService.getHoraExtraSuplementaria(s, rolSeleccionado);
    }

    public DiasLaborado getDias(Servidor s) {
        return diasService.diaLaboradoHoras(rolSeleccionado, s);
    }

    public Boolean getHorasExtras(Servidor s) {
        HoraExtraSuplementaria hora = getHoras(s);
        if (hora != null) {
            return true;
        } else {
            return false;
        }
    }

    public Short getTotalHorasExtras(Servidor s) {
        HoraExtraSuplementaria hora = getHoras(s);
        return (short) (hora.getHoraExtras().intValue() + hora.getHoraSuplementaria().intValue());
    }

    public List<RolHorasExtrasSuplementarias> getListaLiquidar() {
        return horaExtrasService.getListaRolHoras(rolSeleccionado);
    }

    public BigDecimal getHorasExtras(RolesDePago s, HoraExtraSuplementaria hora) {
        escala = distributivoEscaService.getEscalaDistributivoAnio(s.getServidor().getDistributivo(), busqueda);
        double rmu = escala.getRemuneracionDolares().doubleValue();
        double horasEx, vHora = rmu / 240;
        if (hora != null) {
            horasEx = ((vHora * 2) * hora.getHoraExtras());
            return new BigDecimal(horasEx).setScale(2, RoundingMode.UP);
        }
        return BigDecimal.ZERO;
    }

    public BigDecimal getHorasSuplementaria(RolesDePago s, HoraExtraSuplementaria hora) {
        escala = distributivoEscaService.getEscalaDistributivoAnio(s.getServidor().getDistributivo(), busqueda);
        double rmu = escala.getRemuneracionDolares().doubleValue();
        double horasSup, vHora = rmu / 240;
        if (hora != null) {
            horasSup = ((vHora * 1.5) * hora.getHoraSuplementaria());
            return new BigDecimal(horasSup).setScale(2, RoundingMode.UP);
        }
        return BigDecimal.ZERO;
    }

    public void getTotalValores() {
        this.totalValor = horaExtrasService.totalIngresoHora(rolSeleccionado);
        this.totalHoras = horaExtrasService.totalHora(rolSeleccionado);
    }

    public List<RolHorasValores> getListaValorHora(RolHorasExtrasSuplementarias hora) {
        return rolHoraValoresService.getListaValorRolHoras(hora);
    }

    public List<TipoRol> getListaRol() {
        return listaRol;
    }

    public void setListaRol(List<TipoRol> listaRol) {
        this.listaRol = listaRol;
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

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public LazyModel<RolHorasExtrasSuplementarias> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<RolHorasExtrasSuplementarias> lazy) {
        this.lazy = lazy;
    }

    public RolHorasExtrasSuplementarias getHoraExtra() {
        return horaExtra;
    }

    public void setHoraExtra(RolHorasExtrasSuplementarias horaExtra) {
        this.horaExtra = horaExtra;
    }

    public RolHorasExtrasSuplementarias getHoraExtraSeleccionada() {
        return horaExtraSeleccionada;
    }

    public void setHoraExtraSeleccionada(RolHorasExtrasSuplementarias horaExtraSeleccionada) {
        this.horaExtraSeleccionada = horaExtraSeleccionada;
    }

    public BigDecimal getTotalValor() {
        return totalValor;
    }

    public void setTotalValor(BigDecimal totalValor) {
        this.totalValor = totalValor;
    }

    public Short getTotalHoras() {
        return totalHoras;
    }

    public void setTotalHoras(Short totalHoras) {
        this.totalHoras = totalHoras;
    }

}
