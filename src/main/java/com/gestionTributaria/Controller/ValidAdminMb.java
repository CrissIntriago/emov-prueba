/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.EntitiesValidacion.Guayas;
import com.gestionTributaria.EntitiesValidacion.HisPersonaReferencias;
import com.gestionTributaria.EntitiesValidacion.PatronElectoral;
import com.gestionTributaria.EntitiesValidacion.ReferencesFkCliente;
import com.gestionTributaria.EntitiesValidacion.SeguimientoContibuyente;
import com.gestionTributaria.EntitiesValidacion.ViewPatronElectoral;
import com.gestionTributaria.EntitiesValidacion.ViewReporteAdminControl;
import com.gestionTributaria.ServiceValidacionData.HisPersonaReferenciasService;
import com.gestionTributaria.ServiceValidacionData.ReferencesFkClienteService;
import com.gestionTributaria.ServiceValidacionData.SeguimientoContibuyenteService;
import com.gestionTributaria.ServiceValidacionData.ViewPatronElectoralService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.CaberasExcelModels;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Canton;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Provincia;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CantonService;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ProvinciaService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.tesoreria.comprobantelectronico.service.ws.model.Cabecera;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.function.Predicate;
import java.util.logging.Level;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;

/**
 *
 * @author DEVELOPER
 */
@Named
@ViewScoped
public class ValidAdminMb implements Serializable {

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
    private ManagerService service;
    @Inject
    private ServletSession ss;
    @Inject
    private ManagerService manager;

    private Cliente cliente;
    private LazyModel<Cliente> listaclientesLazy;
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
    private Map<String, Object> param;
    private boolean mostrarWEbService;
    private String ciRuc;
//</editor-fold>

    @PostConstruct
    public void initView() {
        service.updateViewMaterializadas();
        if (!JsfUtil.isAjaxRequest()) {
            param = new HashMap<>();
            clienteGuia = new Cliente();
            tempSeguimieto = new SeguimientoContibuyente();
            //  tempSeguimieto = seguimientoContibuyenteService.listaSeguimientoUser(user.getUsuario());
            userValid = user.getNameUser();
            seguimientoContibuyente = new SeguimientoContibuyente();
            // lazyListaSeguimiento = new LazyModel<>(SeguimientoContibuyente.class);
            // lazyListaSeguimiento.getSorteds().put("id", "DESC");
            idFiltro = 1L;
            listaclientesLazy = new LazyModel(Cliente.class);

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
            ciRuc = "";

        }
    }

    public void cargaLazyGeneral() {
        listaclientesLazy = new LazyModel(Cliente.class);

        listaclientesLazy.getSorteds().put("id", "ASC");
        listaclientesLazy.setDistinct(false);

    }

    public void refresh() {
        listaclientesLazy = new LazyModel(Cliente.class);

     //   listaclientesLazy.getFilterss().put("tipoPersona:ne", "VALI");
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

    public void armandoExcel() throws FileNotFoundException {
        int conteoColumna = 0;
        int totalRealizados = 0;
        List<Date> resultFechas = service.findAllQuery("SELECT distinct(c.fecha) FROM ViewReporteAdminControl c order by c.fecha asc", null);
        List<String> usuariosSinRepetir = service.findAllQuery("SELECT distinct(c.usuario) FROM ViewReporteAdminControl c", null);

        System.out.println("resultFechas " + resultFechas.size());
        System.out.println("usuariosSinRepetir " + usuariosSinRepetir.size());

        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "HojaExcel");
        HSSFRow headerRow = sheet.createRow(0);

        int conteo = 0;
        HSSFCell cell = headerRow.createCell(conteo++);
        cell.setCellValue("usuario");
        HSSFCell cellUser = headerRow.createCell(conteo++);
        cellUser.setCellValue("Nombres");
        List<CaberasExcelModels> cabeceras = new ArrayList<>();
        int conteoCabcera = 0;
        HSSFCell celdas = null;
        for (int i = 0; i < resultFechas.size(); i++) {

            celdas = headerRow.createCell(conteo++);
            celdas.setCellValue(resultFechas.get(i).toString());
            conteoColumna++;
            cabeceras.add(new CaberasExcelModels(resultFechas.get(i).toString(), conteo));

        }
        HSSFCell celltotalSaginado = headerRow.createCell(conteo++);
        celltotalSaginado.setCellValue("Total Asignados");
        HSSFCell celltotalRealizados = headerRow.createCell(conteo++);
        celltotalRealizados.setCellValue("Total Realizados");
        HSSFCell celltEficiencia = headerRow.createCell(conteo++);
        celltEficiencia.setCellValue("Eficiencia");

        List<ViewReporteAdminControl> filtroList = new ArrayList<>();
        int filas = 1;
        int cont = 0;
        for (int j = 0; j < usuariosSinRepetir.size(); j++) {
            filtroList = new ArrayList<>();
            parametros = new HashMap<>();
            parametros.put("usuario", usuariosSinRepetir.get(j));

            filtroList = service.findAllQuery("SELECT c FROM ViewReporteAdminControl c where c.usuario=:usuario order by c.fecha asc", parametros);
            HSSFRow dataRow = sheet.createRow(j + 1);
            for (int k = 0; k < filtroList.size(); k++) {
                if (k == 0 || k == 1) {
                    dataRow.createCell(0).setCellValue(filtroList.get(k).getUsuario());
                    dataRow.createCell(1).setCellValue(filtroList.get(k).getNombreCompleto());
                }
                cont = j;
                totalRealizados = 0;
                int col = 0;
                for (int x = 0; x < filtroList.size(); x++) {

                    col = col + 1;
                    conteoCabcera = 0;

                    int columa_temp = 0;
                    for (CaberasExcelModels item : cabeceras) {
                        if (item.getCabecera().equals(filtroList.get(x).getFecha().toString())) {
                            columa_temp = item.getColumna();

                        }
                    }

                    dataRow.createCell(columa_temp - 1).setCellValue(filtroList.get(x).getRegistroValidados().toString());
                    totalRealizados += filtroList.get(x).getRegistroValidados().intValue();

                }

                dataRow.createCell(conteoColumna + 2).setCellValue(filtroList.get(k).getRegistrosAsgnados());
                dataRow.createCell(conteoColumna + 3).setCellValue(totalRealizados);

                if (totalRealizados == 0 || filtroList.get(k).getRegistrosAsgnados() == 0) {
                    dataRow.createCell(conteoColumna + 4).setCellValue(0);
                } else {

                    BigDecimal result = new BigDecimal((totalRealizados / filtroList.get(k).getRegistrosAsgnados()) * 100);

                    dataRow.createCell(conteoColumna + 4).setCellValue(result.doubleValue());
                }
                k = filtroList.size();
            }
        }

//        HSSFRow dataRow = sheet.createRow(1 + DATA.size());
//        HSSFCell total = dataRow.createCell(1);
//        total.setCellType(CellType.FORMULA);
//        total.setCellStyle(style);
//        total.setCellFormula(String.format("SUM(B2:B%d)", 1 + DATA.size()));
        String ruta = SisVars.RUTA_REPORTE_PRACTICAS_PRODUCCION + "reporte_pasante" + new Date() + ".xls";;
        //String rutaProduccion = SisVars.RUTA_REPORTE_PRACTICAS_PRODUCCION + "reporte_pasante" + new Date() + ".xls";
        try {
            ss.borrarParametros();
            ss.borrarDatos();
            FileOutputStream file = new FileOutputStream(ruta);

            workbook.write(file);
            file.close();

            File fichero = new File(ruta);

            ss.addParametro("download", true);
            System.out.println("ruta" + ruta);
            ss.setNombreDocumento(ruta);
            ss.setContentType("xls");
            JsfUtil.redirectNewTab(CONFIG.URL_APP + "ViewSystemDocs");
            Thread data = new Thread() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ValidAdminMb.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (fichero.delete()) {
                        System.out.println("El fichero ha sido borrado satisfactoriamente");
                    } else {
                        System.out.println("El fichero no puede ser borrado");
                    }
                }
            };
            data.start();

        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public static Predicate<ViewReporteAdminControl> filtroCategoria(String categoria) {
        return (ViewReporteAdminControl l) -> {
            return l.getUsuario().equals(categoria);
        };
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
            listaclientesLazy = new LazyModel(Cliente.class
            );

            listaclientesLazy.getSorteds().put("id", "ASC");
        } else {
            listaclientesLazy = new LazyModel(Cliente.class
            );

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
            listaclientesLazy = new LazyModel(Cliente.class
            );
            listaclientesLazy.getFilterss().put("id:gt", datosFiltro);
            listaclientesLazy.getSorteds().put("id", "ASC");
            JsfUtil.addInformationMessage("Exito", "Las referencia se actualizaron correctamente");
            JsfUtil.update("frmMain:data");

        } catch (Exception e) {
            userRegistroValidados();
            System.out.println("error " + e);
            JsfUtil.executeJS("PF('dlogoNodos').hide()");
            JsfUtil.addWarningMessage("Adveretencia", "referencias");
            listaclientesLazy = new LazyModel(Cliente.class
            );
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
            listaclientesLazy = new LazyModel(Cliente.class
            );
            listaclientesLazy.getFilterss().put("id:gt", datosFiltro);
            listaclientesLazy.getSorteds().put("id", "ASC");
            JsfUtil.update("frmMain:data");
        } catch (Exception e) {
            userRegistroValidados();
            System.err.println(e);
            listaclientesLazy = new LazyModel(Cliente.class
            );

            listaclientesLazy.getSorteds().put("id", "ASC");
            JsfUtil.update("frmMain:data");
            JsfUtil.addWarningMessage("Aviso", "update o save warning");
        }
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

    public void openNodos() {

        JsfUtil.update("fmNodosAdmin");
        JsfUtil.executeJS("PF('dlogoNodosAdmin').show()");
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

    public ManagerService getService() {
        return service;
    }

    public void setService(ManagerService service) {
        this.service = service;
    }

    public ServletSession getSs() {
        return ss;
    }

    public void setSs(ServletSession ss) {
        this.ss = ss;
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

//</editor-fold>
    public boolean isMostrarWEbService() {
        return mostrarWEbService;
    }

    public void setMostrarWEbService(boolean mostrarWEbService) {
        this.mostrarWEbService = mostrarWEbService;
    }

    public String getCiRuc() {
        return ciRuc;
    }

    public void setCiRuc(String ciRuc) {
        this.ciRuc = ciRuc;
    }

}
