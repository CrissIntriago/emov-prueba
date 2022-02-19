/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.EntitiesValidacion.ControlValidacion;
import com.gestionTributaria.EntitiesValidacion.Guayas;
import com.gestionTributaria.EntitiesValidacion.HisPersonaReferencias;
import com.gestionTributaria.EntitiesValidacion.PatronElectoral;
import com.gestionTributaria.EntitiesValidacion.ReferencesFkCliente;
import com.gestionTributaria.EntitiesValidacion.SeguimientoContibuyente;
import com.gestionTributaria.EntitiesValidacion.ViewPatronElectoral;
import com.gestionTributaria.EntitiesValidacion.ViewReportes;
import com.gestionTributaria.ServiceValidacionData.HisPersonaReferenciasService;
import com.gestionTributaria.ServiceValidacionData.ReferencesFkClienteService;
import com.gestionTributaria.ServiceValidacionData.SeguimientoContibuyenteService;
import com.gestionTributaria.ServiceValidacionData.ViewPatronElectoralService;
import com.gestionTributaria.ServiceValidacionData.ViewReporteServices;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.ReporteSeguimientoValidadores;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.component.datatable.DataTable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class ValidacionPersonaMB implements Serializable {

    //<editor-fold defaultstate="collapsed" desc="VARIABLES">
    private static final Logger LOG = Logger.getLogger(ValidacionPersonaMB.class.getName());
    @Inject
    private UserSession user;
    @Inject
    private HisPersonaReferenciasService hisPersonaReferenciasService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CantonService cantonService;
    @Inject
    private ProvinciaService provinciaService;
    @Inject
    private ReferencesFkClienteService referencesFkClienteService;
    @Inject
    private SeguimientoContibuyenteService seguimientoContibuyenteService;
    @Inject
    private ViewPatronElectoralService viewPatronElectoralService;
    @Inject
    private ViewReporteServices services;
    @Inject
    private ManagerService manager;
    
    private Cliente cliente;
    private LazyModel<Cliente> listaclientesLazy, lazyClienteGeneral;
    private List<Cliente> lista;
    private Map<String, Object> parametros;
    private Map<String, Object> parametrosCtlogoItem;
    private String tittle;
    private List<CatalogoItem> tiposIdentificacion;
    private Long idTipoIdentificacion = 0L;
    private ReferencesFkCliente referenciaCliente;
    private List<ReferencesFkCliente> listaReferenciaCliente;
    private int contador = 0;
    private List<Cliente> clienteSeleccionados;
    private HisPersonaReferencias logPersonaReferencias;
    private List<BigInteger> listaReferenciaTabla;
    private boolean viewCampso;
    private List<CatalogoItem> listaGeneros;
    private List<Provincia> provincias;
    private List<Canton> cantones;
    private Provincia provincia;
    int index = 0;
    private LazyModel<SeguimientoContibuyente> lazyListaSeguimiento;
    private SeguimientoContibuyente seguimientoContibuyente;
    private Long idFiltro;
    private List<Guayas> listaUserGuayas;
    private LazyModel<PatronElectoral> listPatronElectoral;
    private PatronElectoral patronElectoral;
    private Long registroValidados;
    private String userValid;
    private Long idInicio = 0L;
    private Long idFin = 0L;
    private SeguimientoContibuyente tempSeguimieto;
    private Cliente clienteGuia;
    private ViewReportes viewReportes;
    private LazyModel<ViewReportes> lazyReportes;
    private List<ViewReportes> listaReportes;
    private BigInteger corregidoAdmin;
    private Map<String, Object> param;
    private List<ReporteSeguimientoValidadores> reportesValidadores;
    private LazyModel<ControlValidacion> lazyValidadores;
    private boolean mostrarWEbService;
    private String ciRuc;
//</editor-fold>

    @PostConstruct
    public void initView() {
        if (!JsfUtil.isAjaxRequest()) {
            param = new HashMap<>();
            ciRuc = "";
            viewReportes = new ViewReportes();
            clienteGuia = new Cliente();
            tempSeguimieto = new SeguimientoContibuyente();
            //  tempSeguimieto = seguimientoContibuyenteService.listaSeguimientoUser(user.getUsuario());
            userValid = user.getNameUser();
            seguimientoContibuyente = new SeguimientoContibuyente();
            // lazyListaSeguimiento = new LazyModel<>(SeguimientoContibuyente.class);
            // lazyListaSeguimiento.getSorteds().put("id", "DESC");
            idFiltro = 1L;
            listaclientesLazy = new LazyModel(Cliente.class);
            //listaclientesLazy.getFilterss().put("id:gt", idFiltro - 1);
            //listaclientesLazy.getFilterss().put("estado", true);

            listaclientesLazy.getFilterss().put("validAdmin", false);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            
            listaclientesLazy.getSorteds().put("id", "ASC");
            tiposIdentificacion = catalogoService.MostarTodoCatalogo("tipo_identificacion_beneficiario");
            cliente = new Cliente();
            referenciaCliente = new ReferencesFkCliente();
            parametros = new HashMap<>();
            listaReferenciaCliente = new ArrayList<>();
            listaReferenciaCliente = referencesFkClienteService.findAll();
            //findcaservice.findAll(ReferencesFkCliente.class, null);
            parametros.put("tipoPersona", "S");
            //lista = service.findAll(Cliente.class, parametros);
            tittle = "VALIDACIÓN DE DATOS DEL CONTRIBUYENTE";
            tiposIdentificacion = new ArrayList<>();
            parametrosCtlogoItem = new HashMap<>();
            logPersonaReferencias = new HisPersonaReferencias();
            clienteSeleccionados = new ArrayList<>();
            listaReferenciaTabla = new ArrayList<>();
            mostrarWEbService = false;
            viewCampso = false;
            parametros = new HashMap<>();
            parametros.put("catalogo.codigo", "tipo_genero");
            listaGeneros = new ArrayList<>();
            listaGeneros = catalogoService.MostarTodoCatalogo("tipo_genero");
            cliente = new Cliente();
            provincias = new ArrayList<>();
            provincias = provinciaService.findAll();
            cantones = new ArrayList<>();
            provincia = new Provincia();
            userRegistroValidados();
            patronElectoral = new PatronElectoral();
            
        }
    }
    
    public void cargaLazyGeneral() {
        lazyClienteGeneral = new LazyModel(Cliente.class);
        lazyClienteGeneral.getFilterss().put("validAdmin", false);
        lazyClienteGeneral.getFilterss().put("tipoPersona:ne", "VALI");
        lazyClienteGeneral.getSorteds().put("id", "ASC");
        lazyClienteGeneral.setDistinct(false);
    }
    
    public void clearAllFilters() {
        cargaLazyGeneral();
        
    }
    
    public void refresh() {
        corregidoAdmin = BigInteger.ZERO;
        listaReportes = new ArrayList<>();
        listaclientesLazy = new LazyModel(Cliente.class);
        //listaclientesLazy.getFilterss().put("id:gt", idFiltro - 1);
        //listaclientesLazy.getFilterss().put("estado", true);
        //      listaclientesLazy.getFilterss().put("validNodos", false);
        listaclientesLazy.getFilterss().put("validAdmin", false);
        listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
        listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmMain:data");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();
            
            PrimeFaces.current().ajax().update("frmMain:data");
        }
    }
    
    public void recordUser() {
        // listaReportes = (List<ViewReportes>) services.findAll(ViewReportes.class, null);
        /*
         SELECT row_number() OVER (ORDER BY aa.id_user) AS id,
    aa.id_user,
    aa.usuario,
    migracion.registro_asignados(aa.usuario::text) AS total_asignados,
    aa.registro_validados AS total_realizados,
    migracion.eficiencia_validadores(aa.registro_validados::integer, migracion.registro_asignados(aa.usuario::text)) AS eficiencia_validadores
   FROM ( SELECT u.id AS id_user,
            u.usuario,
            count(c.usuario_validador) AS registro_validados
           FROM cliente c,
            auth.usuarios u
          WHERE c.valid_nodos = true AND c.usuario_validador IS NOT NULL AND u.id = c.usuario_validador AND c.valid_admin = false
          GROUP BY u.id, u.usuario
          ORDER BY (count(c.usuario_validador)) DESC) aa
  WHERE aa.usuario::text <> 'admin'::text
         */
        reportesValidadores = new ArrayList<>();
        reportesValidadores = manager.reporteRecord();
        corregidoAdmin = manager.adminValidos("admin");
        
        lazyValidadores = new LazyModel(ControlValidacion.class);
        lazyValidadores.getFilterss().put("usuario:ne", "admin");
        lazyValidadores.getSorteds().put("fecha", "DESC");
        lazyValidadores.setDistinct(false);
//        lazyReportes = new LazyModel(ViewReportes.class);
//        lazyReportes.getSorteds().put("eficienciaValidadores", "DESC");
//        lazyReportes.setDistinct(false);

        JsfUtil.update("fmRecors");
        JsfUtil.executeJS("PF('dlogoRecords').show()");
    }
    
    public void userRegistroValidados() {
        registroValidados = hisPersonaReferenciasService.totalRegistroUser(user.getUsuario(), Boolean.TRUE);
        JsfUtil.update("frmMain:conteo");
    }
    
    public void openDlogoHistory() {
        seguimientoContibuyente = new SeguimientoContibuyente();
        lazyListaSeguimiento = new LazyModel<>(SeguimientoContibuyente.class);
        lazyListaSeguimiento.getSorteds().put("id", "DESC");
        JsfUtil.update("frmRegisterHistorial");
        JsfUtil.executeJS("PF('dlogoHistorial').show()");
    }
    
    public void aplicarFiltroId() {
        listaclientesLazy = new LazyModel(Cliente.class);
//        listaclientesLazy.getFilterss().put("estado", true);
//        listaclientesLazy.getFilterss().put("validado", true);
        //    listaclientesLazy.getFilterss().put("validNodos", false);
        listaclientesLazy.getFilterss().put("validAdmin", false);
        listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
        listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
        listaclientesLazy.getFilterss().put("id:gt", idFiltro - 1);
        listaclientesLazy.getSorteds().put("id", "ASC");
        
        JsfUtil.update("frmMain:data");
    }
    
    public void saveSeguimiento() {
        try {
            seguimientoContibuyente.setFecha(new Date());
            seguimientoContibuyenteService.create(seguimientoContibuyente);
            lazyListaSeguimiento = new LazyModel<>(SeguimientoContibuyente.class);
            lazyListaSeguimiento.getSorteds().put("id", "DESC");
            JsfUtil.addInformationMessage("Info", "Su registro se realizo con exito");
            JsfUtil.update("dlogoHistorial");
        } catch (Exception e) {
            JsfUtil.addWarningMessage("Aviso", "No se pudo Registrar");
        }
    }
    
    public void opendlogCliente(Cliente c) {
        
        patronElectoral = new PatronElectoral();
        parametrosCtlogoItem = new HashMap<>();
        parametrosCtlogoItem.put("catalogo.codigo", "tipo_identificacion_beneficiario");
        tiposIdentificacion = catalogoService.MostarTodoCatalogo("tipo_identificacion_beneficiario");
        provincia = new Provincia();
        
        if (c != null) {
            cliente = new Cliente();
            cliente = c;
            setCiRuc(cliente.getIdentificacion());
            if (cliente.getCanton() != null) {
                provincia = cliente.getCanton().getIdProvincia();
                cliente.setCanton(c.getCanton());
                catonesProvincia();
            }
        } else {
            cliente = new Cliente();
            ciRuc = null;
            provincia = new Provincia();
        }
        JsfUtil.update("frmContribuyente");
        JsfUtil.executeJS("PF('dlogCliente').show()");
    }
    
    public void aplicarFitlro() {
        index++;
        if (index % 2 != 0) {
            listaclientesLazy = new LazyModel(Cliente.class);
            //    listaclientesLazy.getFilterss().put("validNodos", false);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            listaclientesLazy.getFilterss().put("validAdmin", false);
            listaclientesLazy.getSorteds().put("id", "ASC");
        } else {
            listaclientesLazy = new LazyModel(Cliente.class);
            //      listaclientesLazy.getFilterss().put("validNodos", false);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
//            listaclientesLazy.getFilterss().put("estado", true);
//            listaclientesLazy.getFilterss().put("validado", true);
            listaclientesLazy.getFilterss().put("validAdmin", false);
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            listaclientesLazy.getSorteds().put("id", "ASC");
        }
        
        JsfUtil.update("frmContribuyente");
    }
    
    public void validPropieatiosMany(Cliente c) {
        if (c != null) {
            if (c.getMultiPropietario() != null) {
                if (c.getMultiPropietario()) {
                    c.setMultiPropietario(Boolean.FALSE);
                } else {
                    c.setMultiPropietario(Boolean.TRUE);
                }
                clienteService.edit(c);
            } else {
                c.setMultiPropietario(Boolean.TRUE);
                clienteService.edit(c);
            }
        }
    }
    
    public void validacionNodos() {
        Long datosFiltro = idFiltro == null ? 0L : idFiltro - 1;
        boolean bandera = false;
        
        cliente = new Cliente();
        try {
            String unificacion = "";
            cliente = clienteSeleccionados.get(0);
            cliente.setEstado(Boolean.TRUE);
            cliente.setUsuarioValidador(user.getUsuario());
            cliente.setUsuarioModificacion(user.getNameUser());
            cliente.setFechaModificacion(new Date());
            cliente.setValidNodos(Boolean.TRUE);
            for (int i = 1; i < clienteSeleccionados.size(); i++) {
                unificacion += ":" + clienteSeleccionados.get(i).getId().toString() + ":";
            }
            cliente.setIdReferenciasConsolidados(unificacion);
            clienteService.edit(cliente);
            
            for (int i = 1; i < clienteSeleccionados.size(); i++) {
                
                for (ReferencesFkCliente item : listaReferenciaCliente) {
                    String query = armarQuery(item);
                    
                    int update = clienteService.updateCliente(query, BigInteger.valueOf(cliente.getId()), BigInteger.valueOf(clienteSeleccionados.get(i).getId()));
                    
                    if (update > 0) {
                        logPersonaReferencias = new HisPersonaReferencias();
                        logPersonaReferencias.setNewValue(cliente.getId());
                        logPersonaReferencias.setOldValue(clienteSeleccionados.get(i).getId());
                        logPersonaReferencias.setQuery("update " + item.getTableSchema()
                                + "." + item.getTableName() + " SET "
                                + item.getColumnOrigen() + " = " + cliente.getId()
                                + " WHERE " + item.getColumnCondictional() + " = "
                                + clienteSeleccionados.get(i).getId());
                        logPersonaReferencias.setTabla(item.getTableName());
                        logPersonaReferencias.setEsquema(item.getTableSchema());
                        logPersonaReferencias.setIdUsuario(user.getUsuario());
                        logPersonaReferencias.setFechaCreacion(new Date());
                        hisPersonaReferenciasService.create(logPersonaReferencias);
                        
                    }
                }
                Cliente objectoUpdate = clienteSeleccionados.get(i);
                objectoUpdate.setEstado(Boolean.FALSE);
                objectoUpdate.setUsuarioValidador(user.getUsuario());
                objectoUpdate.setUsuarioModificacion(user.getNameUser());
                objectoUpdate.setValidado(Boolean.TRUE);
                objectoUpdate.setValidNodos(Boolean.TRUE);
                objectoUpdate.setFechaModificacion(new Date());
                clienteService.edit(objectoUpdate);
                
            }
            
            JsfUtil.update("frmNodos");
            JsfUtil.executeJS("PF('dlogoNodos').hide()");
            userRegistroValidados();
            listaclientesLazy = new LazyModel(Cliente.class);
            //  listaclientesLazy.getFilterss().put("validNodos", false);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
            listaclientesLazy.getFilterss().put("validAdmin", false);
//            listaclientesLazy.getFilterss().put("estado", true);
//            listaclientesLazy.getFilterss().put("validado", true);
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            listaclientesLazy.getFilterss().put("id:gt", datosFiltro);
            listaclientesLazy.getSorteds().put("id", "ASC");
            JsfUtil.addInformationMessage("Exito", "Las referencia se actualizaron correctamente");
            JsfUtil.update("frmMain:data");
            
        } catch (Exception e) {
            userRegistroValidados();
            System.out.println("error " + e);
            JsfUtil.executeJS("PF('dlogoNodos').hide()");
            JsfUtil.addWarningMessage("", "referencias");
            listaclientesLazy = new LazyModel(Cliente.class);
            listaclientesLazy.getFilterss().put("validAdmin", false);
            //       listaclientesLazy.getFilterss().put("validNodos", false);
//            listaclientesLazy.getFilterss().put("estado", true);
//            listaclientesLazy.getFilterss().put("validado", true);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            listaclientesLazy.getSorteds().put("id", "ASC");
            JsfUtil.update("frmMain:data");
            
        }
    }
    
    public String armarQuery(ReferencesFkCliente referencia) {
        return "update " + referencia.getTableSchema() + "." + referencia.getTableName() + " SET " + referencia.getColumnOrigen() + " = ?1 WHERE " + referencia.getColumnCondictional() + " = ?2";
    }
    
    public void saveEditar() {
        
        Long datosFiltro = 0L;
        datosFiltro = idFiltro == null ? 0L : idFiltro - 1;
        cliente.setIdentificacion(ciRuc);
        cliente.setFechaModificacion(new Date());
        if (cliente.getNombre().trim() == null || cliente.getNombre().trim().equals("") || cliente.getNombre().trim().equals(" ")) {
            JsfUtil.addWarningMessage("Aviso", "El nombre no puede estar vacio");
            return;
        }
        
        if (cliente.getApellido().trim() == null || cliente.getApellido().trim().equals("") || cliente.getApellido().trim().equals(" ")) {
            JsfUtil.addWarningMessage("Aviso", "El apellido no puede estar vacio");
            return;
        }
        
        try {
            if (cliente.getTieneRuc()) {
                cliente.setRuc("001");
                
            } else {
                cliente.setRuc(null);
            }
            
            cliente.setValidado(Boolean.TRUE);
            
            if (cliente.getIdentificacion().length() != 10) {
                JsfUtil.addWarningMessage("Aviso", "El Num. de Identificación debe tener 10 digitos");
                return;
            }
            
            if (cliente.getId() != null) {
                cliente.setEstado(Boolean.TRUE);
                cliente.setValidado(Boolean.TRUE);
                cliente.setFechaModificacion(new Date());
                cliente.setUsuarioModificacion(user.getNameUser());
                cliente.setUsuarioValidador(user.getUsuario());
                clienteService.edit(cliente);
                
                JsfUtil.addInformationMessage("Info", "Se edito con exito");
                
            } else {
                cliente.setEstado(Boolean.TRUE);
                cliente.setValidado(Boolean.TRUE);
                cliente.setTipoPersona("N");
                cliente.setValiadorAsignado(user.getUsuario());
                cliente.setUsuarioValidador(user.getUsuario());
                cliente.setFechaCreacion(new Date());
                cliente.setValidAdmin(Boolean.FALSE);
                cliente.setFechaModificacion(new Date());
                cliente.setUsuarioModificacion(user.getNameUser());
                cliente.setUsuarioCreacion(user.getNameUser());
                cliente = clienteService.create(cliente);
                JsfUtil.addInformationMessage("Exito", "el registor se guardo con exito");
            }
            userRegistroValidados();
            JsfUtil.update("frmContribuyente");
            JsfUtil.executeJS("PF('dlogCliente').hide()");
            listaclientesLazy = new LazyModel(Cliente.class);
            //    listaclientesLazy.getFilterss().put("validNodos", false);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
//            listaclientesLazy.getFilterss().put("estado", true);
            listaclientesLazy.getFilterss().put("validAdmin", false);
//            listaclientesLazy.getFilterss().put("validado", true);
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            listaclientesLazy.getFilterss().put("id:gt", datosFiltro);
            listaclientesLazy.getSorteds().put("id", "ASC");
            JsfUtil.update("frmMain:data");
        } catch (Exception e) {
            userRegistroValidados();
            System.err.println(e);
            listaclientesLazy = new LazyModel(Cliente.class);
            //     listaclientesLazy.getFilterss().put("validNodos", false);
//            listaclientesLazy.getFilterss().put("estado", true);
//            listaclientesLazy.getFilterss().put("validado", true);
            listaclientesLazy.getFilterss().put("validAdmin", false);
            listaclientesLazy.getFilterss().put("valiadorAsignado", user.getUsuario());
            listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
            listaclientesLazy.getSorteds().put("id", "ASC");
            DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmMain:data");
            if (!dataTable.getFilters().isEmpty()) {
                dataTable.reset();
                
                PrimeFaces.current().ajax().update("frmMain:data");
            }
            
            JsfUtil.update("frmNodos:dataNodos");
            JsfUtil.update("frmMain:data");
            JsfUtil.addWarningMessage("Aviso", "update o save warning");
        }
    }
    
    public void refreshRecords() {
        
        DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("fmRecors:datosControls");
        if (!dataTable.getFilters().isEmpty()) {
            dataTable.reset();
            
            PrimeFaces.current().ajax().update("fmRecors:datosControls");
        }
        lazyValidadores = new LazyModel(ControlValidacion.class);
        lazyValidadores.getFilterss().put("usuario:ne", "admin");
        lazyValidadores.getSorteds().put("fecha", "DESC");
        lazyValidadores.setDistinct(false);
        PrimeFaces.current().ajax().update("fmRecors:datosControls");
    }
    
    public void validacionApiCedula() {
        // cliente.setValidado(Boolean.TRUE);

        if (ciRuc == null && cliente.getRuc() == null) {
            JsfUtil.addWarningMessage("Aviso", "EL numero de identificación no puede ser nula");
            return;
        }
        
        Long id = null;
        boolean tieneRuc = false;
        if (cliente.getId() != null) {
            id = cliente.getId();
        } else {
            id = null;
        }
        if (cliente.getTieneRuc() != null) {
            tieneRuc = cliente.getTieneRuc();
        }
        
        patronElectoral = new PatronElectoral();
        Cliente clienteWs = null;
        cliente.setIdentificacion(ciRuc);
        if (cliente.getTieneRuc() != null) {
            if (!cliente.getTieneRuc()) {
                clienteWs = clienteService.buscarClienteValidador(ciRuc);
            } else {
                clienteWs = clienteService.buscarClienteValidador(ciRuc + "001");
            }
        } else {
            clienteWs = clienteService.buscarClienteValidador(ciRuc);
        }
        
        if (cliente == null) {
            mostrarWEbService = true;
            param = new HashMap<>();
            param.put("cedula", cliente.getIdentificacion().substring(0, 10));
            patronElectoral = manager.findByParameter(PatronElectoral.class, param);
            if (patronElectoral != null) {
                String[] listaSeparadas = patronElectoral.getNombre().split(" ");
                cliente.setIdentificacion(patronElectoral.getCedula());
                cliente.setApellido(listaSeparadas[0] + " " + listaSeparadas[1]);
                cliente.setNombre(listaSeparadas[2] + " " + listaSeparadas[3]);
                cliente.setDireccion(patronElectoral.getZonaNombre());
                provincia = new Provincia();
                parametros = new HashMap<>();
                parametros.put("cedula", cliente.getIdentificacion().trim().substring(0, 10));
                Guayas searchAdicional = manager.findByParameter(Guayas.class, parametros);
                if (searchAdicional == null) {
                    provincia = provinciaService.provinciaHallada(patronElectoral.getCedula().substring(0, 2));
                } else {
                    parametros = new HashMap<>();
                    parametros.put("provincia", searchAdicional.getProvincia().toUpperCase());
                    provincia = manager.findByParameter(Provincia.class, parametros);
                    parametros = new HashMap<>();
                    parametros.put("canton", searchAdicional.getCanton().toUpperCase());
                    cliente.setCanton(manager.findByParameter(Canton.class, parametros));
                }
                catonesProvincia();
                
                if ("M".equals(patronElectoral.getJuntaSexo())) {
                    cliente.setSexo(Boolean.TRUE);
                    cliente.setGenero(manager.find(CatalogoItem.class, 17L));
                } else {
                    cliente.setGenero(manager.find(CatalogoItem.class, 18L));
                    cliente.setSexo(Boolean.FALSE);
                }
                
                JsfUtil.addInformationMessage("Información", "La cedula ha sido validada con exito ");
            } else {
                JsfUtil.addWarningMessage("Aviso", "No hay datos");
            }
        } else {
            cliente.setNombre(clienteWs.getNombre());
            cliente.setApellido(clienteWs.getApellido());
            cliente.setEdad(clienteWs.getEdad());
            cliente.setEmail(clienteWs.getEmail());
            cliente.setApellidoConyuge(clienteWs.getApellidoConyuge());
            cliente.setCanton(clienteWs.getCanton());
            cliente.setCelular(clienteWs.getCelular());
            cliente.setTelefono(clienteWs.getTelefono());
            cliente.setEstadoCivil(clienteWs.getEstadoCivil());
            cliente.setDireccion(clienteWs.getDireccion());
            cliente.setFechaNacimiento(clienteWs.getFechaNacimiento());
            cliente.setRazonSocial(clienteWs.getRazonSocial());
            cliente.setNombreComercial(clienteWs.getNombreComercial());
            cliente.setSexo(clienteWs.getSexo());
            
            if (cliente.getTieneRuc() != null) {
                if (cliente.getTieneRuc()) {
                    cliente.setTipoIdentificacion(new CatalogoItem(11L));
                } else {
                    cliente.setTipoIdentificacion(new CatalogoItem(10L));
                }
            } else {
                cliente.setTipoIdentificacion(new CatalogoItem(10L));
            }
            
            if (cliente.getCanton() != null) {
                provincia = cliente.getCanton().getIdProvincia();
                catonesProvincia();
            }
            
            JsfUtil.addInformationMessage("Información", "La cedula ha sido validada con exito ");
            mostrarWEbService = false;
        }

        // JsfUtil.update("frmContribuyente");
        // JsfUtil.update("datosObject");
        // JsfUtil.update("datosObjectDinardac");
    }
    
    public void validacionApiNombres() {
        
        patronElectoral = new PatronElectoral();
        Provincia pro = new Provincia();
        Canton can = new Canton();
        
        if (cliente.getNombre().toUpperCase() != null || cliente.getApellido().toUpperCase() != null) {
            param = new HashMap<>();
            param.put("nombre", cliente.getNombreCompleto().toUpperCase());
            
            patronElectoral = manager.findByParameter(PatronElectoral.class, param);
            if (patronElectoral != null) {
                
                pro = provinciaService.provinciaHallada(patronElectoral.getCedula().substring(0, 2));
                param = new HashMap<>();
                
                cliente.setIdentificacion(patronElectoral.getCedula());
                ciRuc = cliente.getIdentificacion();
                setProvincia(pro);
                
                cliente.setDireccion(patronElectoral.getZonaNombre());
                provincia = new Provincia();
                parametros = new HashMap<>();
                parametros.put("cedula", cliente.getIdentificacion().trim().substring(0, 10));
                Guayas searchAdicional = manager.findByParameter(Guayas.class, parametros);
                if (searchAdicional == null) {
                    provincia = provinciaService.provinciaHallada(patronElectoral.getCedula().substring(0, 2));
                } else {
                    parametros = new HashMap<>();
                    parametros.put("provincia", searchAdicional.getProvincia().toUpperCase());
                    provincia = manager.findByParameter(Provincia.class, parametros);
                    parametros = new HashMap<>();
                    parametros.put("canton", searchAdicional.getCanton().toUpperCase());
                    cliente.setCanton(manager.findByParameter(Canton.class, parametros));
                }
                catonesProvincia();
                
                mostrarWEbService = true;
                if ("M".equals(patronElectoral.getJuntaSexo())) {
                    cliente.setSexo(Boolean.TRUE);
                    cliente.setGenero(manager.find(CatalogoItem.class, 17L));
                } else {
                    cliente.setGenero(manager.find(CatalogoItem.class, 18L));
                    cliente.setSexo(Boolean.FALSE);
                }
                
                JsfUtil.addInformationMessage("Información", "Los NOmbres han sido validados con exito ");
            } else {
                
                param = new HashMap<>();
                String dataEnviar = "";
                if (cliente.getApellido() != null) {
                    
                    String apellidoInicio = cliente.getApellido().replaceAll("^\\s*", "");
                    String apellidoFinal = apellidoInicio.replaceAll("\\s*$", "");
                    dataEnviar = apellidoFinal;
                    
                }
                if (cliente.getNombre() != null) {
                    
                    String nombreInicio = cliente.getNombre().replaceAll("^\\s*", "");
                    String nombreFinal = nombreInicio.replaceAll("\\s*$", "");
                    dataEnviar += " " + nombreFinal;
                }
                
                listPatronElectoral = new LazyModel(PatronElectoral.class);
                listPatronElectoral.getFilterss().put("nombre:contains", dataEnviar.toUpperCase());
                catonesProvincia();
                JsfUtil.update("fmUserSilmilares");
                JsfUtil.executeJS("PF('dlogoEleccion').show()");
            }
        } else {
            JsfUtil.addWarningMessage("Advertencia", "Coloque los nombres completos y sin tilde");
        }

        /*Canton cantonLocal= service.findByParameter(Canton.class, nameApi);
            cliente.setCanton(cantonLocal);*/
    }
    
    public void eleccionAlterna(PatronElectoral g) {
        Map<String, Object> param = new HashMap<>();
        param.put("codigo", "09");
        cliente.setTipoIdentificacion(new CatalogoItem(10L));
        Provincia prov = provinciaService.provinciaHallada(g.getCedula().substring(0, 2));
        setProvincia(prov);
        cliente.setIdentificacion(g.getCedula());
        String[] data = g.getNombre().split(" ");
        cliente.setApellido(data[0] + " " + data[1]);
        cliente.setNombre(data[2] + " " + data[3]);
        provincia = new Provincia();
        parametros = new HashMap<>();
        
        Guayas searchAdicional = manager.findByParameter(Guayas.class, parametros);
        if (searchAdicional == null) {
            provincia = provinciaService.provinciaHallada(g.getCedula().substring(0, 2));
        } else {
            parametros = new HashMap<>();
            parametros.put("provincia", searchAdicional.getProvincia().toUpperCase());
            provincia = manager.findByParameter(Provincia.class, parametros);
            parametros = new HashMap<>();
            parametros.put("canton", searchAdicional.getCanton().toUpperCase());
            cliente.setCanton(manager.findByParameter(Canton.class, parametros));
        }
        catonesProvincia();
        
        if ("M".equals(g.getJuntaSexo())) {
            cliente.setSexo(Boolean.TRUE);
            cliente.setGenero(manager.find(CatalogoItem.class, 17L));
        } else {
            cliente.setGenero(manager.find(CatalogoItem.class, 18L));
            cliente.setSexo(Boolean.FALSE);
        }
        
        catonesProvincia();
        
        JsfUtil.update("frmContribuyente");
        JsfUtil.update("fmUserSilmilares");
        JsfUtil.executeJS("PF('dlogoEleccion').hide()");
    }
    
    public void view(Cliente c) {
        cliente = new Cliente();
        cliente = c;
        JsfUtil.update("frmContribuyente");
        JsfUtil.executeJS("PF('dlogCliente').show()");
    }
    
    public void catonesProvincia() {
        Map<String, Object> param = new HashMap<>();
        param.put("idProvincia", provincia);
        
        cantones = cantonService.getCantones(provincia);
        //service.findAll(Canton.class, param);
    }
    
    public void openConfirmacionNodos(Cliente c) {
        clienteGuia = new Cliente();
        clienteGuia = c;
        cargaLazyGeneral();
        JsfUtil.update("frmNodos");
        JsfUtil.executeJS("PF('dlogoNodos').show()");
    }
    
    public void viewContribuyente(Cliente c) {
        cliente = new Cliente();
        cliente = c;
        JsfUtil.update("frmContribuyenteView");
        JsfUtil.executeJS("PF('dlogClienteView').show()");
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public Cliente getCliente() {
        return cliente;
    }
    
    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
    
    public LazyModel<Cliente> getListaclientesLazy() {
        return listaclientesLazy;
    }
    
    public void setListaclientesLazy(LazyModel<Cliente> listaclientesLazy) {
        this.listaclientesLazy = listaclientesLazy;
    }
    
    public List<Cliente> getLista() {
        return lista;
    }
    
    public void setLista(List<Cliente> lista) {
        this.lista = lista;
    }
    
    public String getTittle() {
        return tittle;
    }
    
    public void setTittle(String tittle) {
        this.tittle = tittle;
    }
    
    public List<CatalogoItem> getTiposIdentificacion() {
        return tiposIdentificacion;
    }
    
    public void setTiposIdentificacion(List<CatalogoItem> tiposIdentificacion) {
        this.tiposIdentificacion = tiposIdentificacion;
    }
    
    public Long getIdTipoIdentificacion() {
        return idTipoIdentificacion;
    }
    
    public void setIdTipoIdentificacion(Long idTipoIdentificacion) {
        this.idTipoIdentificacion = idTipoIdentificacion;
    }
    
    public int getContador() {
        return contador;
    }
    
    public void setContador(int contador) {
        this.contador = contador;
    }
    
    public List<Cliente> getClienteSeleccionados() {
        return clienteSeleccionados;
    }
    
    public void setClienteSeleccionados(List<Cliente> clienteSeleccionados) {
        this.clienteSeleccionados = clienteSeleccionados;
    }
    
    public boolean isViewCampso() {
        return viewCampso;
    }
    
    public void setViewCampso(boolean viewCampso) {
        this.viewCampso = viewCampso;
    }
    
    public List<CatalogoItem> getListaGeneros() {
        return listaGeneros;
    }
    
    public void setListaGeneros(List<CatalogoItem> listaGeneros) {
        this.listaGeneros = listaGeneros;
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
    
    public Provincia getProvincia() {
        return provincia;
    }
    
    public void setProvincia(Provincia provincia) {
        this.provincia = provincia;
    }
    
    public Map<String, Object> getParametros() {
        return parametros;
    }
    
    public void setParametros(Map<String, Object> parametros) {
        this.parametros = parametros;
    }
    
    public Map<String, Object> getParametrosCtlogoItem() {
        return parametrosCtlogoItem;
    }
    
    public void setParametrosCtlogoItem(Map<String, Object> parametrosCtlogoItem) {
        this.parametrosCtlogoItem = parametrosCtlogoItem;
    }
    
    public ReferencesFkCliente getReferenciaCliente() {
        return referenciaCliente;
    }
    
    public void setReferenciaCliente(ReferencesFkCliente referenciaCliente) {
        this.referenciaCliente = referenciaCliente;
    }
    
    public List<ReferencesFkCliente> getListaReferenciaCliente() {
        return listaReferenciaCliente;
    }
    
    public void setListaReferenciaCliente(List<ReferencesFkCliente> listaReferenciaCliente) {
        this.listaReferenciaCliente = listaReferenciaCliente;
    }
    
    public HisPersonaReferencias getLogPersonaReferencias() {
        return logPersonaReferencias;
    }
    
    public void setLogPersonaReferencias(HisPersonaReferencias logPersonaReferencias) {
        this.logPersonaReferencias = logPersonaReferencias;
    }
    
    public List<BigInteger> getListaReferenciaTabla() {
        return listaReferenciaTabla;
    }
    
    public void setListaReferenciaTabla(List<BigInteger> listaReferenciaTabla) {
        this.listaReferenciaTabla = listaReferenciaTabla;
    }
    
    public int getIndex() {
        return index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public SeguimientoContibuyente getSeguimientoContibuyente() {
        return seguimientoContibuyente;
    }
    
    public void setSeguimientoContibuyente(SeguimientoContibuyente seguimientoContibuyente) {
        this.seguimientoContibuyente = seguimientoContibuyente;
    }
    
    public LazyModel<SeguimientoContibuyente> getLazyListaSeguimiento() {
        return lazyListaSeguimiento;
    }
    
    public void setLazyListaSeguimiento(LazyModel<SeguimientoContibuyente> lazyListaSeguimiento) {
        this.lazyListaSeguimiento = lazyListaSeguimiento;
    }
    
    public Long getIdFiltro() {
        return idFiltro;
    }
    
    public void setIdFiltro(Long idFiltro) {
        this.idFiltro = idFiltro;
    }
    
    public List<Guayas> getListaUserGuayas() {
        return listaUserGuayas;
    }
    
    public void setListaUserGuayas(List<Guayas> listaUserGuayas) {
        this.listaUserGuayas = listaUserGuayas;
    }
    
    public Long getRegistroValidados() {
        return registroValidados;
    }
    
    public void setRegistroValidados(Long registroValidados) {
        this.registroValidados = registroValidados;
    }
    
    public String getUserValid() {
        return userValid;
    }
    
    public void setUserValid(String userValid) {
        this.userValid = userValid;
    }
    
    public Cliente getClienteGuia() {
        return clienteGuia;
    }
    
    public void setClienteGuia(Cliente clienteGuia) {
        this.clienteGuia = clienteGuia;
    }
    
    public LazyModel<Cliente> getLazyClienteGeneral() {
        return lazyClienteGeneral;
    }
    
    public void setLazyClienteGeneral(LazyModel<Cliente> lazyClienteGeneral) {
        this.lazyClienteGeneral = lazyClienteGeneral;
    }
    
    public ViewReportes getViewReportes() {
        return viewReportes;
    }
    
    public void setViewReportes(ViewReportes viewReportes) {
        this.viewReportes = viewReportes;
    }
    
    public LazyModel<ViewReportes> getLazyReportes() {
        return lazyReportes;
    }
    
    public void setLazyReportes(LazyModel<ViewReportes> lazyReportes) {
        this.lazyReportes = lazyReportes;
    }
    
    public List<ViewReportes> getListaReportes() {
        return listaReportes;
    }
    
    public void setListaReportes(List<ViewReportes> listaReportes) {
        this.listaReportes = listaReportes;
    }
    
    public BigInteger getCorregidoAdmin() {
        return corregidoAdmin;
    }
    
    public void setCorregidoAdmin(BigInteger corregidoAdmin) {
        this.corregidoAdmin = corregidoAdmin;
    }
    
    public UserSession getUser() {
        return user;
    }
    
    public void setUser(UserSession user) {
        this.user = user;
    }
    
    public HisPersonaReferenciasService getHisPersonaReferenciasService() {
        return hisPersonaReferenciasService;
    }
    
    public void setHisPersonaReferenciasService(HisPersonaReferenciasService hisPersonaReferenciasService) {
        this.hisPersonaReferenciasService = hisPersonaReferenciasService;
    }
    
    public CatalogoItemService getCatalogoItemService() {
        return catalogoItemService;
    }
    
    public void setCatalogoItemService(CatalogoItemService catalogoItemService) {
        this.catalogoItemService = catalogoItemService;
    }
    
    public CatalogoService getCatalogoService() {
        return catalogoService;
    }
    
    public void setCatalogoService(CatalogoService catalogoService) {
        this.catalogoService = catalogoService;
    }
    
    public ClienteService getClienteService() {
        return clienteService;
    }
    
    public void setClienteService(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    
    public CantonService getCantonService() {
        return cantonService;
    }
    
    public void setCantonService(CantonService cantonService) {
        this.cantonService = cantonService;
    }
    
    public ProvinciaService getProvinciaService() {
        return provinciaService;
    }
    
    public void setProvinciaService(ProvinciaService provinciaService) {
        this.provinciaService = provinciaService;
    }
    
    public ReferencesFkClienteService getReferencesFkClienteService() {
        return referencesFkClienteService;
    }
    
    public void setReferencesFkClienteService(ReferencesFkClienteService referencesFkClienteService) {
        this.referencesFkClienteService = referencesFkClienteService;
    }
    
    public SeguimientoContibuyenteService getSeguimientoContibuyenteService() {
        return seguimientoContibuyenteService;
    }
    
    public void setSeguimientoContibuyenteService(SeguimientoContibuyenteService seguimientoContibuyenteService) {
        this.seguimientoContibuyenteService = seguimientoContibuyenteService;
    }
    
    public ViewPatronElectoralService getViewPatronElectoralService() {
        return viewPatronElectoralService;
    }
    
    public void setViewPatronElectoralService(ViewPatronElectoralService viewPatronElectoralService) {
        this.viewPatronElectoralService = viewPatronElectoralService;
    }
    
    public ViewReporteServices getServices() {
        return services;
    }
    
    public void setServices(ViewReporteServices services) {
        this.services = services;
    }
    
    public ManagerService getManager() {
        return manager;
    }
    
    public void setManager(ManagerService manager) {
        this.manager = manager;
    }
    
    public LazyModel<PatronElectoral> getListPatronElectoral() {
        return listPatronElectoral;
    }
    
    public void setListPatronElectoral(LazyModel<PatronElectoral> listPatronElectoral) {
        this.listPatronElectoral = listPatronElectoral;
    }
    
    public PatronElectoral getPatronElectoral() {
        return patronElectoral;
    }
    
    public void setPatronElectoral(PatronElectoral patronElectoral) {
        this.patronElectoral = patronElectoral;
    }
    
    public Long getIdInicio() {
        return idInicio;
    }
    
    public void setIdInicio(Long idInicio) {
        this.idInicio = idInicio;
    }
    
    public Long getIdFin() {
        return idFin;
    }
    
    public void setIdFin(Long idFin) {
        this.idFin = idFin;
    }
    
    public SeguimientoContibuyente getTempSeguimieto() {
        return tempSeguimieto;
    }
    
    public void setTempSeguimieto(SeguimientoContibuyente tempSeguimieto) {
        this.tempSeguimieto = tempSeguimieto;
    }
    
    public Map<String, Object> getParam() {
        return param;
    }
    
    public void setParam(Map<String, Object> param) {
        this.param = param;
    }
    
    public List<ReporteSeguimientoValidadores> getReportesValidadores() {
        return reportesValidadores;
    }
    
    public void setReportesValidadores(List<ReporteSeguimientoValidadores> reportesValidadores) {
        this.reportesValidadores = reportesValidadores;
    }
    
    public LazyModel<ControlValidacion> getLazyValidadores() {
        return lazyValidadores;
    }
    
    public void setLazyValidadores(LazyModel<ControlValidacion> lazyValidadores) {
        this.lazyValidadores = lazyValidadores;
    }
    
    public boolean isMostrarWEbService() {
        return mostrarWEbService;
    }
    
    public void setMostrarWEbService(boolean mostrarWEbService) {
        this.mostrarWEbService = mostrarWEbService;
    }

//</editor-fold>
    public String getCiRuc() {
        return ciRuc;
    }
    
    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }
}
