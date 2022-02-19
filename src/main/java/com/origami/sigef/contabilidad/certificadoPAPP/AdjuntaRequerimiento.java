/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.certificadoPAPP;

import com.gestionTributaria.Services.ManagerService;
import com.origami.sigef.catalogoItem.service.CatalogoItemService;
import com.origami.sigef.common.config.RolUsuario;
import com.origami.sigef.common.entities.ActividadOperativa;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.MasterCatalogo;
import com.origami.sigef.common.entities.PappProceso;
import com.origami.sigef.common.entities.Producto;
import com.origami.sigef.common.entities.ProductoProceso;
import com.origami.sigef.common.entities.UnidadAdministrativa;
import com.origami.sigef.common.service.MasterCatalogoService;
import com.origami.sigef.common.service.PappProcesoService;
import com.origami.sigef.common.service.ProductoProcesoService;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.interfaces.FileUploadDoc;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.OpcionBusqueda;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import com.origami.sigef.contabilidad.service.PlanAnualProgramaProyectoService;
import com.origami.sigef.contabilidad.service.ProductoService;
import com.origami.sigef.contabilidad.service.UnidadAdministrativaService;
import com.origami.sigef.resource.procesos.controllers.BpmnBaseRoot;
import com.origami.sigef.resource.procesos.entities.Observaciones;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author ORIGAMI2
 */
@Named(value = "adjuntaRequerimientoView")
@ViewScoped
public class AdjuntaRequerimiento extends BpmnBaseRoot implements Serializable {

    /**
     * Creates a new instance of AdjuntaRequerimiento
     */
    @Inject
    private FileUploadDoc uploadDoc;
    @Inject
    private MasterCatalogoService masterCatalogoService;
    @Inject
    private PlanAnualProgramaProyectoService planAnualProgramaProyectoService;
    @Inject
    private UserSession userSession;
    @Inject
    private ClienteService clienteService;
    @Inject
    private CatalogoItemService catalogoItemService;
    @Inject
    private ProductoProcesoService productoProcesoService;
    @Inject
    private PappProcesoService pappService;
    @Inject
    private ProductoService productoService;
    @Inject
    private UnidadAdministrativaService unidadAdministrativaService;

    private List<MasterCatalogo> periodos;
    private List<ActividadOperativa> listaPlan;
    private OpcionBusqueda busqueda;
    private Distributivo d;
    private ActividadOperativa planAnual;
    private UploadedFile files;
    private List<Producto> listProducto;
    private List<ProductoProceso> listProductoProceso;
    private CatalogoItem catalogoItem;
    private CatalogoItem catalogoItem2;
    private MasterCatalogo planProgramaProyecto;
    private UnidadAdministrativa unidad;
    private List<ProductoDAO> listaProductoDAO;
    private List<ProductoDAO> auxDAO;
    private List<UnidadAdministrativa> listUnidad;
    private ProductoDAO productoDao;
    private PappProceso pappProceso;
    private ActividadOperativa pappProcesoAux;
    private ProductoProceso productoProceso;
    private BigInteger numTramite;
    private String email;
    private Date fechaAprobacion;
    private Integer aprobado;
    private Boolean varAnular = false;
    private Boolean bolMaximaAutoridad = false;

    @Inject
    private ManagerService service;

    @PostConstruct
    public void init() {
        if (this.session.getTaskID() != null) {
            if (!JsfUtil.isAjaxRequest()) {
                this.setTaskId(this.session.getTaskID());
                observacion = new Observaciones();
                numTramite = new BigInteger("" + this.tramite.getNumTramite());
            }
        }
        bolMaximaAutoridad = false;
        busqueda = new OpcionBusqueda();
        pappProcesoAux = new ActividadOperativa();
        auxDAO = new ArrayList<>();
        observacion = new Observaciones();
        listaProductoDAO = new ArrayList<>();
        listUnidad = new ArrayList<>();
        pappProceso = new PappProceso();
        productoProceso = new ProductoProceso();
        PappProceso pappTemp = pappService.getPappProceso(busqueda.getAnio(), numTramite);
        if (pappTemp != null) {
            pappProceso = pappTemp;
            listProductoProceso = productoProcesoService.getListEdit(busqueda.getAnio(), numTramite);
            for (ProductoProceso pp : listProductoProceso) {
                productoDao = new ProductoDAO();
                productoDao.setProducto(new Producto());
                productoDao.setProducto(pp.getProducto());
                auxDAO.add(productoDao);
            }
        }
        aprobado = (Integer) this.getVariable("aprobado");

        catalogoItem = catalogoItemService.getEstadoRol("PROEMIACT");
        catalogoItem2 = catalogoItemService.getEstadoRol("PRORECHACT");

        planAnual = new ActividadOperativa();
        periodos = masterCatalogoService.findByNamedQuery("MasterCatalogo.findByCatalogoAndTipo", new Object[]{"tipo_cuenta", "PA"});
        if (periodos != null) {
            int indice = periodos.size();
            if (!periodos.isEmpty() && indice == 1) {
                busqueda.setAnio(periodos.get(0).getAnio());
            } else {
                busqueda.setAnio(periodos.get(indice - 1).getAnio());
            }
        }
        String nameMax = clienteService.getrolsUser(RolUsuario.maximaAutoridad);
        if (nameMax.equals(userSession.getNameUser())) {
            bolMaximaAutoridad = true;
            unidad = clienteService.getUnidadPrincipalUSerSinAdmin(userSession.getNameUser());
            System.out.println("Unidad.. >> "+unidad);
        }
        cargarXanio();
        System.out.println("bolMaximaAutoridad: "+bolMaximaAutoridad);
    }

    public void actualizarListaUnidad() {
        if (busqueda.getAnio() != null) {
            listUnidad = unidadAdministrativaService.listaDeUnidadesXPAPP(busqueda.getAnio());
        } else {
            listUnidad = new ArrayList<>();
        }
    }

    public void editarValorSolicitado(ProductoDAO da) {
        if (da.getMontoSolcitado().setScale(2, RoundingMode.HALF_UP).compareTo(getMontoDisponible(da).setScale(2, RoundingMode.HALF_UP)) == 1) {
            da.setMontoSolcitado(BigDecimal.ZERO);
            JsfUtil.addWarningMessage("", "El monto a solicitar no puede ser mayor al monot disponible");
            return;

        }

        ProductoDAO o = da;
        da.setMontoSolcitado(da.getMontoSolcitado());
        int posicionTemp = this.listaProductoDAO.indexOf(da);
        this.listaProductoDAO.remove(posicionTemp);
        this.listaProductoDAO.add(posicionTemp, o);
    }

    public void cargarXanio() {
        if (busqueda.getAnio() != null) {
            planProgramaProyecto = masterCatalogoService.getMasterCatalogo("tipo_cuenta", "PA", busqueda.getAnio());
            System.out.println("planProgramaProyecto " + planProgramaProyecto.getNombre().toUpperCase());
            if (!bolMaximaAutoridad) {
                unidad = clienteService.getUnidadPrincipalUSerSinAdmin(userSession.getNameUser());
                System.out.println("Unidad: >> "+unidad);
            }
            System.out.println("unidad " + unidad.getNombre().toUpperCase());
            if (unidad != null) {
                email = pappService.getEmail(userSession.getUserId());
                listaPlan = planAnualProgramaProyectoService.planProcesoInicio(unidad, busqueda.getAnio(), catalogoItem2);
            }
        } else {
            listaPlan = new ArrayList<>();
        }

        if (bolMaximaAutoridad) {
            actualizarListaUnidad();
        }
    }

    public void completarTarea(ActividadOperativa plan) {
        if (pappProcesoAux.getId() != null && !plan.equals(pappProcesoAux)) {
            JsfUtil.addWarningMessage("Información", "Verifique que el PAPP sea el correcto");
            return;
        }

        if (listaProductoDAO.isEmpty()) {
            JsfUtil.addWarningMessage("Información", "Verifique que existan Productos asignados");
            return;
        }

        if (addListProducto()) {
            JsfUtil.addWarningMessage("Información", "No se registran Productos");
            return;
        }
        planAnual = new ActividadOperativa();
        if (tramite.getFechaIngreso() != null) {
            fechaAprobacion = tramite.getFechaIngreso();
        } else {
            fechaAprobacion = new Date();
        }
        observacion.setEstado(true);
        observacion.setFecCre(new Date());
        observacion.setTarea(tarea.getName());
        observacion.setUserCre(session.getName());
        PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
        PrimeFaces.current().ajax().update(":frmDlgObser");
    }

    public void closeDlgObservacion() {
        try {
            if (!varAnular) {
                anular();
            } else {
                if (fechaAprobacion == null) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una fecha");
                    return;
                }
                if (observacion.getObservacion() == null || observacion.getObservacion().equals("")) {
                    JsfUtil.addWarningMessage("AVISO!!!", "Debe ingresar una Observacion");
                    return;
                }
                pappProceso.setFechaTramite(fechaAprobacion);
                pappProceso.setActividadOperativa(pappProcesoAux);
                pappProceso.setEstadoProceso(catalogoItem);
                pappProceso.setUnidad(unidad);
                pappProceso.setNumeroTramite(numTramite);
                pappProceso.setUsuarioSolicita(userSession.getNameUser());
                pappProceso.setEmail(email);
                generarCodigo();
                pappProceso = (PappProceso) service.updateEntity(pappProceso);
                getParamts().put("usuario_2", clienteService.getrolsUser(RolUsuario.planificacion));
                if (saveTramite() == null) {
                    return;
                }
                getParamts().put("creacion", 1);
                super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
                this.continuar();
                newPapp();
                planAnual = new ActividadOperativa();
                PrimeFaces.current().executeScript("PF('dlgObservaciones').hide()");
            }
        } catch (Exception e) {
            LOG.log(Level.SEVERE, "Select", e);
        }
    }

    public void anular() {
        List<PappProceso> result = pappService.getListaPappProceso(this.numTramite);
        if (result != null && !result.isEmpty()) {
            for (PappProceso item : result) {
                item.setFechaTramite(fechaAprobacion);
                item.setFechaAprovacion(null);
                item.setActividadOperativa(pappProcesoAux);
                item.setEstadoProceso(catalogoItem2);
                item.setUnidad(unidad);
                item.setNumeroTramite(numTramite);
                item.setUsuarioSolicita(userSession.getNameUser());
                item.setEmail(email);
//        generarCodigo();
                pappService.edit(item);
            }

        }

        List<ProductoProceso> pappProduct = pappService.getListaProductoProceso(this.numTramite);
        if (pappProduct != null && !pappProduct.isEmpty()) {
            for (ProductoProceso item : pappProduct) {
                item.setEstado(new CatalogoItem(415L));
                productoProcesoService.edit(item);
            }
        }

        getParamts().put("creacion", 0);
        super.completeTask(this.session.getTaskID(), (HashMap<String, Object>) getParamts());
        this.continuar();
    }

    public BigDecimal getMontoDisponible(ProductoDAO da) {
        if (da.getProducto() == null) {
            return BigDecimal.ZERO;
        }
        BigDecimal valor = pappService.getValorAproabdo(da.getProducto());
        return da.getProducto().getMontoReformada().subtract(valor);
    }

    public void generarCodigo() {
        PappProceso pappProcesoAux_ = pappService.getSecuencial(busqueda.getAnio());
        if (pappProcesoAux_ != null && pappProcesoAux_.getId() != null && pappProcesoAux_.getSecuencial() != null) {
            pappProceso.setSecuencial((short) (pappProcesoAux_.getSecuencial().intValue() + 1));
        } else {
            pappProceso.setSecuencial((short) 1);
        }
        pappProceso.setCodigo("CPAPP-" + Utils.completarCadenaConCeros(pappProceso.getSecuencial().toString(), 4) + "-" + busqueda.getAnio());
    }

    public void adjuntarDucumento() {
        PrimeFaces.current().executeScript("PF('requisitoDialog').show()");
    }

    public void handleFileUploadInformeTec(FileUploadEvent event) {
        try {
            files = event.getFile();
            if (files != null) {
                uploadDoc.upload(tramite, files);
            }
            PrimeFaces.current().executeScript("PF('requisitoDialog').hide()");
            PrimeFaces.current().ajax().update("formMain");
            JsfUtil.addInformationMessage("Información", "Su archivo se subio exitosamente");
        } catch (Exception e) {
            System.out.println("error al subir el archivo " + e);
        }
    }

    public void addArchivo() {
        if (files != null) {
            uploadDoc.upload(tramite, files);
            JsfUtil.addInformationMessage("Documento", "Datos almacenados correctamente");
            planAnual = new ActividadOperativa();
        }
    }

    public void openDialogAmortizacion(ActividadOperativa plan) {
        if (!plan.equals(pappProcesoAux) && !auxDAO.isEmpty()) {
            JsfUtil.addWarningMessage("Información", "verifique que NO exista Productos asignados");
            return;
        }

        pappProcesoAux = plan;
        listProducto = planAnualProgramaProyectoService.productoPlanPryecto(plan, catalogoItem2);
        if (this.planAnual.getId() == null || this.planAnual == plan) {
            this.planAnual = plan;
            if (listaProductoDAO.isEmpty()) {
                for (Producto p : listProducto) {
                    p.setReserva(productoService.productoReservado(p, false));
                    p.setSaldoDisponible(p.getMontoReformada().subtract(p.getReserva()));
                    productoDao = new ProductoDAO();
                    productoDao.setProducto(new Producto());
                    productoDao.setProducto(p);
                    listaProductoDAO.add(productoDao);
                }
            }
        } else {
            listaProductoDAO.clear();
            auxDAO.clear();
            this.planAnual = plan;
            if (listaProductoDAO.isEmpty()) {
                for (Producto p : listProducto) {
                    productoDao = new ProductoDAO();
                    productoDao.setProducto(new Producto());
                    productoDao.setProducto(p);
                    listaProductoDAO.add(productoDao);
                }
            }
        }
        PrimeFaces.current().executeScript("PF('dlgAnt').show()");
        PrimeFaces.current().ajax().update("frmAnt");
    }

    public void agregarListPro(ProductoDAO pro) {
        if (pro.getEstado() == true) {
            auxDAO.add(pro);
        } else {
            auxDAO.remove(pro);
        }
    }

    public boolean addListProducto() {
        boolean var = true;
        if (!this.auxDAO.isEmpty()) {
            for (ProductoDAO p : auxDAO) {
                newProducto();
                productoProceso.setEstadoProceso(catalogoItem);
                productoProceso.setNumetoTramite(numTramite);
                productoProceso.setProducto(p.getProducto());
                productoProceso.setFechaProceso(new Date());
                productoProceso.setMontoDisponible(p.getProducto().getSaldoDisponible());
                productoProceso.setUsuarioSolicita(userSession.getNombrePersonaLogeada());
                productoProceso.setSolicitado(p.getMontoSolcitado());
                productoProceso = productoProcesoService.create(productoProceso);
            }
            var = false;
        }
        return var;
    }

    public void completarTask(boolean var) {
        varAnular = var;
        if (var) {
            if (pappProcesoAux != null) {
                completarTarea(pappProcesoAux);
            } else {
                JsfUtil.addWarningMessage("", "Ocurrio un incidente, comuniquese con el administrador del sistema");
            }
        } else {

            observacion.setEstado(true);
            observacion.setFecCre(new Date());
            observacion.setTarea(tarea.getName());
            observacion.setUserCre(session.getName());
            PrimeFaces.current().executeScript("PF('dlgObservaciones').show()");
            PrimeFaces.current().ajax().update(":frmDlgObser");

        }
    }

    public void cancelar() {
        if (!listaProductoDAO.isEmpty()) {
            pappProcesoAux = new ActividadOperativa();
            listaProductoDAO.clear();
            auxDAO.clear();
        }
    }

    public void newPapp() {
        pappProceso = new PappProceso();
    }

    public void newProducto() {
        productoProceso = new ProductoProceso();
    }

    public UnidadAdministrativa getUnidad() {
        return unidad;
    }

    public void setUnidad(UnidadAdministrativa unidad) {
        this.unidad = unidad;
    }

    public List<MasterCatalogo> getPeriodos() {
        return periodos;
    }

    public void setPeriodos(List<MasterCatalogo> periodos) {
        this.periodos = periodos;
    }

    public List<ActividadOperativa> getListaPlan() {
        return listaPlan;
    }

    public void setListaPlan(List<ActividadOperativa> listaPlan) {
        this.listaPlan = listaPlan;
    }

    public OpcionBusqueda getBusqueda() {
        return busqueda;
    }

    public void setBusqueda(OpcionBusqueda busqueda) {
        this.busqueda = busqueda;
    }

    public ActividadOperativa getPlanAnual() {
        return planAnual;
    }

    public void setPlanAnual(ActividadOperativa planAnual) {
        this.planAnual = planAnual;
    }

    public List<Producto> getListProducto() {
        return listProducto;
    }

    public void setListProducto(List<Producto> listProducto) {
        this.listProducto = listProducto;
    }

    public MasterCatalogo getPlanProgramaProyecto() {
        return planProgramaProyecto;
    }

    public void setPlanProgramaProyecto(MasterCatalogo planProgramaProyecto) {
        this.planProgramaProyecto = planProgramaProyecto;
    }

    public List<ProductoDAO> getListaProductoDAO() {
        return listaProductoDAO;
    }

    public void setListaProductoDAO(List<ProductoDAO> listaProductoDAO) {
        this.listaProductoDAO = listaProductoDAO;
    }

    public Date getFechaAprobacion() {
        return fechaAprobacion;
    }

    public void setFechaAprobacion(Date fechaAprobacion) {
        this.fechaAprobacion = fechaAprobacion;
    }

    public Integer getAprobado() {
        return aprobado;
    }

    public void setAprobado(Integer aprobado) {
        this.aprobado = aprobado;
    }

    public List<ProductoProceso> getListProductoProceso() {
        return listProductoProceso;
    }

    public void setListProductoProceso(List<ProductoProceso> listProductoProceso) {
        this.listProductoProceso = listProductoProceso;
    }

    public Boolean getVarAnular() {
        return varAnular;
    }

    public void setVarAnular(Boolean varAnular) {
        this.varAnular = varAnular;
    }

    public List<ProductoDAO> getAuxDAO() {
        return auxDAO;
    }

    public void setAuxDAO(List<ProductoDAO> auxDAO) {
        this.auxDAO = auxDAO;
    }

    public List<UnidadAdministrativa> getListUnidad() {
        return listUnidad;
    }

    public void setListUnidad(List<UnidadAdministrativa> listUnidad) {
        this.listUnidad = listUnidad;
    }

    public Boolean getBolMaximaAutoridad() {
        return bolMaximaAutoridad;
    }

    public void setBolMaximaAutoridad(Boolean bolMaximaAutoridad) {
        this.bolMaximaAutoridad = bolMaximaAutoridad;
    }

}
