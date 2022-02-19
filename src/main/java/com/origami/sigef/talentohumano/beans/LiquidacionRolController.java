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
import com.origami.sigef.common.entities.DistributivoAnexo;
import com.origami.sigef.common.entities.DistributivoEscala;
import com.origami.sigef.common.entities.LiquidacionRol;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.RolRubro;
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
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.talentohumano.UtilsTH.TalentoHumano;
import com.origami.sigef.talentohumano.model.LiquidacionRolDAO;
import com.origami.sigef.talentohumano.services.AnticipoRemuneracionService;
import com.origami.sigef.talentohumano.services.CaucionServidoresService;
import com.origami.sigef.talentohumano.services.DescuentoRubroValorService;
import com.origami.sigef.talentohumano.services.DiasLaboradoService;
import com.origami.sigef.talentohumano.services.DistributivoAnexoService;
import com.origami.sigef.talentohumano.services.DistributivoEscalaService;
import com.origami.sigef.talentohumano.services.LiquidacionRolService;
import com.origami.sigef.talentohumano.services.PrestamoIESService;
import com.origami.sigef.talentohumano.services.ProcesoServidorService;
import com.origami.sigef.talentohumano.services.RetencionImpuestoRentaService;
import com.origami.sigef.talentohumano.services.RolRubroService;
import com.origami.sigef.talentohumano.services.RolesDePagoService;
import com.origami.sigef.talentohumano.services.TipoRolService;
import com.origami.sigef.talentohumano.services.ValoresRolesService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "liquidacionView")
@ViewScoped
public class LiquidacionRolController implements Serializable {

    @Inject
    private RolRubroService rolRubroService;
    @Inject
    private UserSession userSession;
    @Inject
    private LiquidacionRolService liquidacionRolService;
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
    private DescuentoRubroValorService descuentoService;
    @Inject
    private DistributivoAnexoService distAnexoService;
    @Inject
    private AnticipoRemuneracionService anticipoService;
    @Inject
    private PrestamoIESService prestamoService;
    @Inject
    private RetencionImpuestoRentaService retencionService;
    @Inject
    private ServletSession serveltSession;
    @Inject
    private CaucionServidoresService caucionService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private ProcesoServidorService procesoServidorService;

    private LazyModel<LiquidacionRol> lazy;
    private List<TipoRol> listaRol;
    private List<MasterCatalogo> periodos;
    private List<RolesDePago> listaRoles;
    private List<RolesDePago> auxLista;
    private List<ValoresRoles> listaValoresRoles;
    private List<ValoresRoles> listaValoresRolesAnexos;
    private List<LiquidacionRol> listaLiquidar;
    private List<LiquidacionRolDAO> listaLiquidarDAO;
    private TipoRol rolSeleccionado;
    private OpcionBusqueda busqueda;
    private LiquidacionRol liquidacionRol;
    private RolRubro rolRubro;
    private String cedula;
    private CatalogoItem aprobado;
    private DistributivoEscala escala;
    private LiquidacionRolDAO liquidacionDAO;
    private ValoresRoles rubroAnexoSeleccionado;
    private BigDecimal rmuMesGlobal;
    private BigDecimal netoRecibir;
    private BigDecimal totalNeto;
    private BigDecimal totalEgreso;
    private BigDecimal totalIngreso;
    private RolRubro remuneracionNeta;

    @PostConstruct
    public void init() {
        cedula = "";
        totalEgreso = BigDecimal.ZERO;
        totalIngreso = BigDecimal.ZERO;
        totalNeto = BigDecimal.ZERO;
        busqueda = new OpcionBusqueda();
        rolSeleccionado = new TipoRol();
        listaRoles = new ArrayList<>();
        listaLiquidarDAO = new ArrayList<>();
        listaValoresRolesAnexos = new ArrayList<>();
        liquidacionDAO = new LiquidacionRolDAO();
        rubroAnexoSeleccionado = new ValoresRoles();
        auxLista = rolesPagoService.findRolesXPeriodo(busqueda.getAnio());
        aprobado = catalogoItemService.getEstadoRol("aprobado-rol");
        periodos = catalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "CC"});
        listaRol = tipoRolService.listaRolesXanio(busqueda.getAnio());
        listaRol.sort(Comparator.comparing(TipoRol::getId));
    }

//<editor-fold defaultstate="collapsed" desc="LIQUIDAR ROL">
    public void liquidarRol() {
        auxLista = rolesPagoService.findRolesXPeriodo(busqueda.getAnio());
        if (rolSeleccionado == null) {
            return;
        }
        if (getListaLiquidar().isEmpty()) {
            listaRoles = filtrarRolesPagos();
            for (RolesDePago r : listaRoles) {
                escala = new DistributivoEscala();
                newLiquidacionRol();
//                if (!distributivoService.distributivoReformado(r.getServidor().getDistributivo(), "RAU")) {
                if (r.getServidor().getDistributivo().getEstadoActivo()) {
                    escala = distributivoEscaService.getEscalaDistributivoAnio(r.getServidor().getDistributivo(), busqueda);
                    setearLiquidacion(r);
//                liquidacionRol.setSueldo(getSueldoNeto(escala, r));
                    if (("rol_general".equals(rolSeleccionado.getTipoRol().getCodigo())||"rol_adicional".equals(rolSeleccionado.getTipoRol().getCodigo()))
                            && (getDiasLaborado(liquidacionRol.getRolPago().getServidor()) == null
                            || getDiasLaborado(liquidacionRol.getRolPago().getServidor()).getDias().intValue() == 0)
                            && liquidacionRol.getRolPago().getServidor().getEstado() == true) {
                        JsfUtil.addWarningMessage("Información", "Servidor " + liquidacionRol.getRolPago().getServidor().getPersona().getNombreCompleto() + " no registra Días Laborados");
                    } else {
                        if (r.getServidor().getFechaIngreso() != null && r.getServidor().getFechaSalida() != null) {
                            if (procesoServidorService.getLiquidaServidor(r.getServidor())) {
                                if (TalentoHumano.validarFechaFin(r.getServidor().getFechaSalida(), rolSeleccionado)) {
                                    liquidacionRol = liquidacionRolService.create(liquidacionRol);
                                    listaValoresRoles = valorRolesService.FindValoresRolesXRol(liquidacionRol.getRolPago());
//                                    setearListRubros(listaValoresRoles);
                                    liquidacionRolService.setearListRubros(liquidacionRol, listaValoresRoles, escala, rolSeleccionado);
                                }
                            }
                        }
                        if (r.getServidor().getFechaIngreso() != null && r.getServidor().getFechaSalida() == null) {
                            liquidacionRol = liquidacionRolService.create(liquidacionRol);
                            listaValoresRoles = valorRolesService.FindValoresRolesXRol(liquidacionRol.getRolPago());
//                            setearListRubros(listaValoresRoles);
                            liquidacionRolService.setearListRubros(liquidacionRol, listaValoresRoles, escala, rolSeleccionado);
                        }
                    }
                } else {
                    JsfUtil.addWarningMessage("VERIFICAR", "Servidor " + r.getServidor().getPersona().getNombreCompleto() + "NO CUENTA CON UN DISTRIBUTIVO PRESUPUSTADO");
                }
            }
            rolSeleccionado.setUsuarioRegistraAprueba(userSession.getNameUser());
            tipoRolService.edit(rolSeleccionado);
        }
        cancelar();
        calcularValores();
        lazy = new LazyModel<>(LiquidacionRol.class);
        lazy.getFilterss().put("estado", true);
        lazy.getFilterss().put("tipoRol", rolSeleccionado);
        lazy.getSorteds().put("rolPago.servidor.persona.apellido", "ASC");
        lazy.setDistinct(false);

    }

    public void setearLiquidacion(RolesDePago r) {
        liquidacionRol.setRolPago(r);
        liquidacionRol.setRmu(escala);
        liquidacionRol.setTipoRol(rolSeleccionado);
        liquidacionRol.setUsuarioCreacion(userSession.getNameUser());
        liquidacionRol.setFechaCreacion(new Date());
    }

    public void actualizarLiquidacion(LiquidacionRol rol) {
        escala = new DistributivoEscala();
        liquidacionRol = rol;
        escala = distributivoEscaService.getEscalaDistributivoAnio(rol.getRolPago().getServidor().getDistributivo(), busqueda);
        listaValoresRoles = valorRolesService.FindValoresRolesXRol(liquidacionRol.getRolPago());
        int numAct = rolRubroService.actualizarEstadoRubro(Boolean.FALSE, liquidacionRol);
        liquidacionRolService.setearListRubros(liquidacionRol, listaValoresRoles, escala, rolSeleccionado);
        calcularValores();
        cancelar();
        JsfUtil.addSuccessMessage("Datos Actualizados correctamente.", rol.getRolPago().getServidor().getPersona().getNombreCompleto());
    }

//<editor-fold defaultstate="collapsed" desc="Reporte Roles">
    public void impresionRoles(LiquidacionRol liq) {
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        serveltSession.addParametro("id", liq.getId());
        serveltSession.addParametro("mes", liq.getTipoRol().getMes().getCodigo().toUpperCase());
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.setNombreReporte("rol_individual");
        serveltSession.addParametro("SUBREPOR_DIR_VALORRUBRO", JsfUtil.getRealPath("reportes/LiquidacionRol/"));
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void impresionDetalleRol() {
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        serveltSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        serveltSession.addParametro("mes", rolSeleccionado.getMes().getCodigo().toUpperCase());
        serveltSession.setNombreReporte("rol_all");
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("SUBREPOR_DIR_VALORRUBRO", JsfUtil.getRealPath("reportes/LiquidacionRol/"));
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void impresionResumenRol() {
        Boolean etiquetas = Boolean.FALSE;
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (!rolSeleccionado.getEstadoAprobacion().getCodigo().equals("registrado-rol")) {
            etiquetas = Boolean.TRUE;
        }
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        serveltSession.addParametro("id_tipo_roll", rolSeleccionado.getId());
        serveltSession.addParametro("MOSTRAR_ETIQUETA_APROBADO", etiquetas);
        serveltSession.setNombreReporte("rol_resumen");
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void impresionRoGenerall() {
        Boolean etiquetas = Boolean.FALSE;
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Rol");
            return;
        }
        if (!rolSeleccionado.getEstadoAprobacion().getCodigo().equals("registrado-rol")) {
            etiquetas = Boolean.TRUE;
        }
        calcularValores();
        Distributivo distributivo = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.analista));
        Distributivo distributivoMax = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.directorFinanciero));
        Distributivo distributivoRev = clienteService.getuusuarioLogeado(clienteService.getrolsUser(RolUsuario.titularTH));
        serveltSession.addParametro("id_tipo_rol", rolSeleccionado.getId());
        serveltSession.addParametro("MOSTRAR_ETIQUETA_APROBADO", etiquetas);
        serveltSession.addParametro("total_ing", totalIngreso);
        serveltSession.addParametro("total_eg", totalEgreso);
        serveltSession.addParametro("total_neto", totalNeto);
        serveltSession.addParametro("ci_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_resp", distributivo != null ? distributivo.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_resp", distributivo != null ? distributivo.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_max", distributivoMax != null ? distributivoMax.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_max", distributivoMax != null ? distributivoMax.getCargo().getNombreCargo() : "");
        serveltSession.addParametro("ci_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getIdentificacionCompleta() : "");
        serveltSession.addParametro("nombre_revisor", distributivoRev != null ? distributivoRev.getServidorPublico().getPersona().getNombreCompleto() : "");
        serveltSession.addParametro("cargo_revisor", distributivoRev != null ? distributivoRev.getCargo().getNombreCargo() : "");
        serveltSession.setNombreReporte("rol_general");
        serveltSession.setNombreSubCarpeta("LiquidacionRol");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

//<editor-fold defaultstate="collapsed" desc="RUBROS ANEXOS">
    public void agregarLista(LiquidacionRol liq) {
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        List<LiquidacionRolDAO> addList = new ArrayList<>();
        int cont;
        double aux = 0, valorRubro = 0, valorAporte = 0;
        for (LiquidacionRolDAO lr : listaLiquidarDAO) {
            cont = 0;
            for (RolRubro r : lista) {
                cont++;
                if (r.getValorAsignacion().equals(lr.getValorAsignacion())) {
                    cont--;
                }
            }
//            Double aux = null;
            if (cont == lista.size()) {
                if (lr.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("ENC")
                        || lr.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("SR")) {
                    for (RolRubro rr : lista) {
                        if (rr.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("APO_INDIV_CODIGO")
                                || rr.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("APOR_IND_LOSEP")) {
                            BigDecimal valor = rr.getValorRubro();
                            valorAporte = valorAporte + (lr.getValorRubro().doubleValue() * rr.getValorAsignacion().getValorParametrizable().getValor().doubleValue()) / 100;
                            aux = aux + (valor.doubleValue() + ((lr.getValorRubro().doubleValue() * rr.getValorAsignacion().getValorParametrizable().getValor().doubleValue()) / 100));
                            valorRubro = valorRubro + lr.getValorRubro().doubleValue();
                            rr.setValorRubro(new BigDecimal(aux));
                            rolRubroService.edit(rr);
                        }
                    }
                }
                addList.add(lr);
            }
        }
        for (RolRubro rb : lista) {
            if (rb.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("SUELDON-EGRESO")) {
                remuneracionNeta = new RolRubro();
                remuneracionNeta = rb;
                BigDecimal valor = rb.getValorRubro();
//                System.out.println("vcalor actual " + valor);
//                System.out.println("valor rubro " + valorRubro);
//                System.out.println("valor aporte " + valorAporte);
//                System.out.println("valor nuevo " + (valorRubro - valorAporte));
                remuneracionNeta.setValorRubro(valor.add(new BigDecimal(valorRubro - valorAporte)).setScale(2, RoundingMode.HALF_UP));
//                System.out.println("valor actualizado " + remuneracionNeta.getValorRubro());
                rolRubroService.edit(remuneracionNeta);
            }
        }
        setearListaDAO(addList);
    }

    public void setearListaDAO(List<LiquidacionRolDAO> addList) {
        if (!addList.isEmpty()) {
            for (LiquidacionRolDAO lr : addList) {
                newRolRubro();
                rolRubro.setLiquidacionRol(lr.getLiquidacionRol());
                rolRubro.setValorAsignacion(lr.getValorAsignacion());
                rolRubro.setValorRubro(lr.getValorRubro());
                rolRubro = rolRubroService.create(rolRubro);
            }
        }
    }

    public void agregarRubroAnexo() {
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Rol no Seleccionando");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Rol no Seleccionando");
            return;
        }
        if (liquidacionRol == null) {
            JsfUtil.addWarningMessage("Error", "Debe seleccionar un rol");
            return;
        }
        if (liquidacionRol.getId() != null) {
            liquidacionDAO = new LiquidacionRolDAO();
            liquidacionDAO.setLiquidacionRol(new LiquidacionRol());
            liquidacionDAO.setValorAsignacion(new ValoresRoles());
            PrimeFaces.current().executeScript("PF('dlgRubroAne').show()");
            PrimeFaces.current().ajax().update("formBanco");
        } else {
            JsfUtil.addWarningMessage("Información", "Debe Seleccionar un Servidor");
        }
    }

    public void guardarRubro() {
        if (rubroAnexoSeleccionado == null) {
            JsfUtil.addWarningMessage("Error", "Debe Seleccionar un Rubro");
            return;
        }
        liquidacionDAO.setLiquidacionRol(liquidacionRol);
        Double aux;
        if (rubroAnexoSeleccionado.getValorParametrizable().getTipo().getCodigo().equals("HORAS_SUP")) {
            JsfUtil.addWarningMessage("Información", "Rubro de horas Extras no puedes ser asignado");
        } else {
            if (validarRubroAgregado()) {
                JsfUtil.addWarningMessage("Información", "Rubro ya se encuentra registrado");
                return;
            }
            liquidacionDAO.setValorAsignacion(rubroAnexoSeleccionado);
            listaLiquidarDAO.add(liquidacionDAO);
            for (LiquidacionRolDAO lr : listaLiquidarDAO) {
                if (lr.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("APO_INDIV_CODIGO")
                        || lr.getValorAsignacion().getValorParametrizable().getTipo().getCodigo().equals("APOR_IND_LOSEP")) {
                    BigDecimal valor = lr.getValorRubro();
//                    System.out.println("valor actual --> " + valor);
//                    System.out.println("porcentual --> " + (liquidacionDAO.getValorRubro().doubleValue() * lr.getValorAsignacion().getValorParametrizable().getValor().doubleValue()) / 100);
                    aux = valor.doubleValue() + ((liquidacionDAO.getValorRubro().doubleValue() * lr.getValorAsignacion().getValorParametrizable().getValor().doubleValue()) / 100);
                    lr.setValorRubro(new BigDecimal(aux).setScale(2, RoundingMode.HALF_UP));
//                    rolRubroService.edit(lr);
                }
            }
        }
        rubroAnexoSeleccionado = new ValoresRoles();
        PrimeFaces.current().executeScript("PF('dlgRubroAne').hide()");
        PrimeFaces.current().ajax().update("frmDetalleRol");
    }

    public boolean validarRubroAgregado() {
        if (!listaLiquidarDAO.isEmpty()) {
            for (LiquidacionRolDAO lr : listaLiquidarDAO) {
                if (rubroAnexoSeleccionado.equals(lr.getValorAsignacion())) {
                    return true;
                }
            }
        }
        return false;
    }
//</editor-fold>

    public void calcularTotales2(LiquidacionRol liq) {
        netoRecibir = BigDecimal.ZERO;
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        double totaIng = 0, totalEg = 0, totalNeto = 0;
        for (RolRubro r : lista) {
            if (r.getValorAsignacion().getValorParametrizable().getClasificacion().getCodigo().equals("I")) {
                totaIng = totaIng + r.getValorRubro().doubleValue();
            } else {
                if (!"SUELDON-EGRESO".equals(r.getValorAsignacion().getValorParametrizable().getTipo().getCodigo())) {
                    totalEg = totalEg + r.getValorRubro().doubleValue();
                }
            }
        }
//        System.out.println("total ingreso " + totaIng + " total egreso " + totalEg);
        totalNeto = totaIng - totalEg;
        liq.setTotalIngreso(new BigDecimal(totaIng));
        liq.setTotalEgreso(new BigDecimal(totalEg));
        liq.setNetoRecibir(new BigDecimal(totalNeto));
        netoRecibir = liq.getNetoRecibir();
        liquidacionRolService.edit(liq);
    }

    public void calcularValores() {
        totalEgreso = liquidacionRolService.totalEgreso(rolSeleccionado);
        totalIngreso = liquidacionRolService.totalIngreso(rolSeleccionado);
        totalNeto = liquidacionRolService.totalNeto(rolSeleccionado);
    }

    public void newValores() {
        totalEgreso = BigDecimal.ZERO;
        totalIngreso = BigDecimal.ZERO;
        totalNeto = BigDecimal.ZERO;
    }

    public void openDialogRol(LiquidacionRol liq) {
        this.cedula = liq.getRolPago().getServidor().getPersona().getIdentificacion();
        this.liquidacionRol = liq;
//        System.out.println("liqiodacion neto " + liquidacionRol.getNetoRecibir());
        llenasListaDAO(liq);
        PrimeFaces.current().executeScript("PF('dlgDetalle').show()");
        PrimeFaces.current().ajax().update("frmDetalleRol");

    }

    public void guardar() {
        if (liquidacionRol == null) {
            JsfUtil.addErrorMessage("Liquidacion", "No hay datos para guardar");
            return;
        }
        boolean edit = liquidacionRol.getId() != null;
        if (edit) {
            liquidacionRol.setUsuarioModifica(userSession.getName());
            liquidacionRol.setFechaModificacion(new Date());
            agregarLista(liquidacionRol);
            liquidacionRolService.edit(liquidacionRol);
//            System.out.println("guardar " + liquidacionRol.getNetoRecibir());
            calcularTotales2(liquidacionRol);
        }
        newLiquidacionRol();
        calcularValores();
        listaLiquidarDAO = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('dlgDetalle').hide()");
        PrimeFaces.current().ajax().update("frmDetalleRol");
    }

    public void eliminar(LiquidacionRol liq) {
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        for (RolRubro r : lista) {
            r.setEstado(Boolean.FALSE);
            rolRubroService.edit(r);
        }
        liq.setEstado(Boolean.FALSE);
        liq.setFechaModificacion(new Date());
        liq.setUsuarioModifica(userSession.getNameUser());
        liquidacionRolService.edit(liq);
        calcularValores();
    }

    public void actulizarRoles() {
        listaRol = tipoRolService.listaRolesXanio(busqueda.getAnio());
        listaRol.sort(Comparator.comparing(TipoRol::getId));
    }

    private void llenasListaDAO(LiquidacionRol liq) {
        listaValoresRolesAnexos = valorRolesService.rubrosTipoOrigen(rolSeleccionado.getAnio(), liq.getRolPago(), "DIS-ANE");
        listaLiquidarDAO = new ArrayList<>();
        List<RolRubro> lista = rolRubroService.getListaValores(liq, rolSeleccionado);
        for (RolRubro r : lista) {
            if (!"SUELDON-EGRESO".equals(r.getValorAsignacion().getValorParametrizable().getTipo().getCodigo())) {
                liquidacionDAO = new LiquidacionRolDAO();
                liquidacionDAO.setLiquidacionRol(new LiquidacionRol());
                liquidacionDAO.setValorAsignacion(new ValoresRoles());
                liquidacionDAO.setLiquidacionRol(r.getLiquidacionRol());
                liquidacionDAO.setValorRubro(r.getValorRubro());
                liquidacionDAO.setValorAsignacion(r.getValorAsignacion());
                listaLiquidarDAO.add(liquidacionDAO);
            }
        }
    }

    public void eliminarRegistro() {
        int executeLiquidacion = 0;
        List<LiquidacionRol> listRol = liquidacionRolService.getListaXrol(rolSeleccionado);
        if (listRol.isEmpty()) {
            JsfUtil.addWarningMessage("Advertencia", "No existen datos a Actualizar");
            return;
        }
        for (LiquidacionRol lq : listRol) {
            lq.setFechaModificacion(new Date());
            lq.setUsuarioModifica(userSession.getNameUser());
            liquidacionRolService.edit(lq);
            rolRubroService.actualizarEstadoRubro(Boolean.FALSE, lq);
        }
        executeLiquidacion = liquidacionRolService.actualizarEstadoLiquidacion(Boolean.FALSE, rolSeleccionado);
        if (executeLiquidacion == listRol.size()) {
            liquidarRol();
        } else {
            JsfUtil.addWarningMessage("Advertencia", "Verificar o Eliminar Datos");
        }
    }

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="BUSCAR-SELECCIONAR SERVIDOR">
    public void buscarServidor() {
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Debe de seleccionar un Rol");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Debe de seleccionar un Rol");
            return;
        }
        LiquidacionRol rol = liquidacionRolService.liquidacionRegistrada(rolSeleccionado, cedula);
        if (rol != null) {
            this.liquidacionRol = rol;
            this.cedula = rol.getRolPago().getServidor().getPersona().getIdentificacion();
            llenasListaDAO(rol);
        } else {
            Map<String, List<String>> params = new HashMap<>();
            String tipoRolid, anio;
            anio = rolSeleccionado.getAnio() + "";
            tipoRolid = rolSeleccionado.getId() + "";
            params.put("ANIO", Arrays.asList(anio));
            params.put("ROL", Arrays.asList(tipoRolid));
            Utils.openDialog("/facelet/talentoHumano/dialgo/dlgServidporCtaAsig", "800", "400", params);
        }
    }

    private boolean validarRegistroRol(RolesDePago rol) {
        List<LiquidacionRol> lista = liquidacionRolService.getListaXrol(rolSeleccionado);
        if (!lista.isEmpty()) {
            for (LiquidacionRol lr : lista) {
                if (lr.getRolPago().equals(rol)) {
                    return true;
                }
            }
        }
        return false;
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="APRO-ROL">

    public void aprobarRol() {
        if (rolSeleccionado == null) {
            JsfUtil.addWarningMessage("Información", "Rol no Seleccionando");
            return;
        }
        if (rolSeleccionado.getId() == null) {
            JsfUtil.addWarningMessage("Información", "Rol no Seleccionando");
            return;
        }
        if (rolSeleccionado.getEstadoAprobacion().getCodigo().equals("aprobado-rol") || rolSeleccionado.getEstadoAprobacion().getCodigo().equals("pagado-rol")) {
            JsfUtil.addWarningMessage("Información", "Rol se encuentra " + rolSeleccionado.getEstadoAprobacion().getTexto());
            return;
        }
        listaLiquidar = new ArrayList<>();
        listaLiquidar = liquidacionRolService.getListaXrol(rolSeleccionado);
        int cont;
        for (LiquidacionRol lr : listaLiquidar) {
            cont = 0;
            if (rolesPagoService.rubrosAsignado("RET_JUD", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !descuentoService.rubroOtroDescuento("RET_JUD", rolSeleccionado, lr.getRolPago()).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de Retenciones Judiciales " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("OTROS_E", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !descuentoService.rubroOtroDescuento("OTROS_E", rolSeleccionado, lr.getRolPago()).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de OTROS EGRESOS " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("PRES_QUI", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !prestamoService.prestamoRubro(lr.getRolPago().getServidor(), "PRES_QUI", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de PRESTAMO QUIROGRAFARIO " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("PRES_HIP", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !prestamoService.prestamoRubro(lr.getRolPago().getServidor(), "PRES_HIP", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de PRESTAMO HIPOTECARIO " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("IMP_RENTA", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !retencionService.retencionMensualRubro(lr.getRolPago().getServidor(), "IMP_RENTA", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de RETENCION IMPUESTO A LA RENTA " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("CAUCIONES", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && caucionService.caucionRubro(rolSeleccionado, lr.getRolPago().getServidor(), "CAUCIONES") != null) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de CAUCION SERVIDOR " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (rolesPagoService.rubrosAsignado("ANT_RMU", lr.getRolPago(), rolSeleccionado).isEmpty()
                    && !anticipoService.anticipoRubro(lr.getRolPago().getServidor(), "ANT_RMU", rolSeleccionado).isEmpty()) {
                JsfUtil.addWarningMessage("Inofrmacion", "Verifique Rubro de ANTICIPO DE RMU " + lr.getRolPago().getServidor().getPersona().getNombreCompleto());
                cont++;
            }
            if (cont > 0) {
                return;
            }
        }
        rolSeleccionado.setEstadoAprobacion(aprobado);
        rolSeleccionado.setUsuarioRegistraAprueba(userSession.getNameUser());
        tipoRolService.edit(rolSeleccionado);
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Buscar Rubros">

    public DistributivoAnexo getDistributivoAnexo(String cod) {
        DistributivoAnexo dist = new DistributivoAnexo();
        List<DistributivoAnexo> listaDist = distAnexoService.getDisAnexos(busqueda.getAnio());
        if (!listaDist.isEmpty()) {
            for (DistributivoAnexo d : listaDist) {
                if (cod.equals(d.getValorParametrizado().getTipo().getCodigo())) {
                    dist = d;
                    break;
                }
            }
        }
        return dist;
    }

    public DiasLaborado getDiasLaborado(Servidor s) {
        DiasLaborado dias = diasService.diaLaborado(rolSeleccionado, s);
        if (dias != null) {
            return dias;
        }
        return null;
    }

    public List<LiquidacionRol> getListaLiquidar() {
        List<LiquidacionRol> lista = new ArrayList<>();
        return lista = liquidacionRolService.getListaXrol(rolSeleccionado);
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

//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="metodos de cancelar y newObjet's">
    private void newLiquidacionRol() {
        cedula = "";
        liquidacionRol = new LiquidacionRol();
        liquidacionRol.setRmu(new DistributivoEscala());
        liquidacionRol.setRolPago(new RolesDePago());
        liquidacionRol.setTipoRol(new TipoRol());
    }

    private void newRolRubro() {
        rolRubro = new RolRubro();
        rolRubro.setLiquidacionRol(new LiquidacionRol());
        rolRubro.setValorAsignacion(new ValoresRoles());
    }

    public void cancelar() {
        cedula = "";
        liquidacionRol = new LiquidacionRol();
        listaLiquidarDAO = new ArrayList<>();
        PrimeFaces.current().executeScript("PF('dlgDetalle').hide()");
        PrimeFaces.current().ajax().update("frmDetalleRol");
    }
//</editor-fold>
//<editor-fold defaultstate="collapsed" desc="Getter's and Setter's">

    public List<LiquidacionRolDAO> getListaLiquidarDAO() {
        return listaLiquidarDAO;
    }

    public void setListaLiquidarDAO(List<LiquidacionRolDAO> listaLiquidarDAO) {
        this.listaLiquidarDAO = listaLiquidarDAO;
    }

    public RolRubro getRemuneracionNeta() {
        return remuneracionNeta;
    }

    public void setRemuneracionNeta(RolRubro remuneracionNeta) {
        this.remuneracionNeta = remuneracionNeta;
    }

    public BigDecimal getTotalNeto() {
        return totalNeto;
    }

    public void setTotalNeto(BigDecimal totalNeto) {
        this.totalNeto = totalNeto;
    }

    public BigDecimal getTotalEgreso() {
        return totalEgreso;
    }

    public void setTotalEgreso(BigDecimal totalEgreso) {
        this.totalEgreso = totalEgreso;
    }

    public BigDecimal getTotalIngreso() {
        return totalIngreso;
    }

    public void setTotalIngreso(BigDecimal totalIngreso) {
        this.totalIngreso = totalIngreso;
    }

    public LazyModel<LiquidacionRol> getLazy() {
        return lazy;
    }

    public void setLazy(LazyModel<LiquidacionRol> lazy) {
        this.lazy = lazy;
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

    public TipoRol getRolSeleccionado() {
        return rolSeleccionado;
    }

    public void setRolSeleccionado(TipoRol rolSeleccionado) {
        this.rolSeleccionado = rolSeleccionado;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public LiquidacionRol getLiquidacionRol() {
        return liquidacionRol;
    }

    public void setLiquidacionRol(LiquidacionRol liquidacionRol) {
        this.liquidacionRol = liquidacionRol;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public List<ValoresRoles> getListaValoresRolesAnexos() {
        return listaValoresRolesAnexos;
    }

    public void setListaValoresRolesAnexos(List<ValoresRoles> listaValoresRolesAnexos) {
        this.listaValoresRolesAnexos = listaValoresRolesAnexos;
    }

    public LiquidacionRolDAO getLiquidacionDAO() {
        return liquidacionDAO;
    }

    public void setLiquidacionDAO(LiquidacionRolDAO liquidacionDAO) {
        this.liquidacionDAO = liquidacionDAO;
    }

    public ValoresRoles getRubroAnexoSeleccionado() {
        return rubroAnexoSeleccionado;
    }

    public void setRubroAnexoSeleccionado(ValoresRoles rubroAnexoSeleccionado) {
        this.rubroAnexoSeleccionado = rubroAnexoSeleccionado;
    }
//</editor-fold>
}
