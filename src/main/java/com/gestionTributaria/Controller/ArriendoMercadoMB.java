/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.origami.sigef.arrendamiento.entities.Arrendamiento;
import com.origami.sigef.arrendamiento.entities.Arrendatarios;
import com.origami.sigef.arrendamiento.entities.Locales;
import com.origami.sigef.arrendamiento.service.ArrendamientoService;
import com.origami.sigef.arrendamiento.service.ArrendatariosService;
import com.origami.sigef.arrendamiento.service.LocalesService;
import com.origami.sigef.arrendamiento.service.OrdenesEmitidasService;
import com.origami.sigef.arrendamiento.service.TarifasArriendoService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.entities.Mercado;
import com.origami.sigef.common.entities.Proveedor;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.CatalogoService;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.service.ValoresService;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.tesoreria.comprobantelectronico.sri.util.Constantes;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
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
@Named
@ViewScoped
public class ArriendoMercadoMB implements Serializable {

    @Inject
    private UserSession userSession;
    @Inject
    private CatalogoService catalogoService;
    @Inject
    private LocalesService localService;
    @Inject
    private ArrendamientoService arrendamientoService;
    @Inject
    private ValoresService valService;
    @Inject
    private ArrendatariosService arrendatariosService;
    @Inject
    private OrdenesEmitidasService ordenesEmitidasService;
    @Inject
    private ServletSession servletSession;
    @Inject
    private TarifasArriendoService tarifasArriendoService;

    private Arrendamiento arrendamiento;

    private LazyModel<Arrendamiento> arrendamientoLazyModel;
    private LazyModel<Proveedor> proveedorLazyModel;
    private LazyModel<Cliente> clienteLazy;
    private LazyModel<Locales> localesLazyModel;

    private List<CatalogoItem> periodoCobroList;
    private List<Arrendatarios> arrendatariosList;
    private List<Arrendatarios> arrendatariosListDelete;
    private List<Mercado> mercadoList;
    private Mercado mercado;

    private Boolean ver;

    private String searchIdentificacion;

    private double iva;
    private Boolean tieneIVA = Boolean.FALSE;

    @PostConstruct
    public void initialize() {
        CONFIG.IVA = Integer.parseInt(valService.findByCodigo(Constantes.IVA));
        iva = CONFIG.IVA;
        this.arrendamiento = new Arrendamiento();
        this.arrendamientoLazyModel = new LazyModel<>(Arrendamiento.class);
        this.arrendamientoLazyModel.getSorteds().put("id", "ASC");
        this.arrendamientoLazyModel.getFilterss().put("estado", true);
        //Periodos de cobro
        mercado = new Mercado();
        this.periodoCobroList = catalogoService.getItemsByCatalogo("periodos_cobro");
        mercadoList = tarifasArriendoService.getMercadosActivos();
    }

    public void form(Arrendamiento arriendo, Boolean accion) {
        this.ver = accion;
        this.searchIdentificacion = "";
        this.arrendamiento = new Arrendamiento();
        this.arrendatariosList = new ArrayList<>();
        this.arrendatariosListDelete = new ArrayList<>();
        if (arriendo != null) {
            this.arrendamiento = arriendo;
            List<Arrendatarios> aux = arrendatariosService.getArrendatariosList(arrendamiento);
            for (Arrendatarios resultado : aux) {
                arrendatariosList.add(resultado);
            }
        }
        PrimeFaces.current().executeScript("PF('arriendoDlg').show()");
        PrimeFaces.current().ajax().update("formArriendo");
    }

    public void saveEdit() {
        if (arrendamiento.getLocal() == null) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe seleccionar un Local");
            return;
        }
        if (arrendatariosList.isEmpty()) {
            JsfUtil.addWarningMessage("AVISO!!!", "Debe añadir un operador antes de guardar");
            return;
        }
        boolean edit = arrendamiento.getId() != null;
        if (edit) {
            edit(arrendamiento);
        } else {
            arrendamiento.setUsuarioCreacion(userSession.getNameUser());
            arrendamiento = arrendamientoService.create(arrendamiento);
            //editamos el estado del local
            arrendamiento.getLocal().setEstadoLocal(Boolean.FALSE);
            localService.edit(arrendamiento.getLocal());
        }
        if (!arrendatariosList.isEmpty()) {
            for (Arrendatarios arrendatario : arrendatariosList) {
                if (arrendatario.getId() == null) {
                    arrendatario.setIdArriendamiento(arrendamiento);
                    arrendatariosService.create(arrendatario);
                } else {
                    arrendatariosService.edit(arrendatario);
                }
            }
        }
        if (!arrendatariosListDelete.isEmpty()) {
            for (Arrendatarios arrendatario : arrendatariosListDelete) {
                if (arrendatario.getId() != null) {
                    arrendatario.setEstado(Boolean.FALSE);
                    arrendatariosService.edit(arrendatario);
                }
            }
        }
        PrimeFaces.current().executeScript("PF('arriendoDlg').hide()");
        PrimeFaces.current().ajax().update("arriendoTable");
        JsfUtil.addSuccessMessage("Arrendamiento", (edit ? "Editado" : " Registrado") + " con éxito.");
    }

    public void delete(Arrendamiento arriendo) {
        arriendo.setEstado(Boolean.FALSE);
        edit(arriendo);
        arriendo.getLocal().setEstadoLocal(Boolean.TRUE);
        localService.edit(arriendo.getLocal());
        JsfUtil.addSuccessMessage("Arrendamiento", "Eliminada con éxito");
    }

    public void edit(Arrendamiento arriendo) {
        arriendo.setFechaModificacion(new Date());
        arriendo.setUsuarioModificacion(userSession.getNameUser());
        arrendamientoService.edit(arriendo);
    }

    public void loadLocales() {
        this.localesLazyModel = new LazyModel<>(Locales.class);
        this.localesLazyModel.getSorteds().put("id", "ASC");
        this.localesLazyModel.getFilterss().put("estado", true);
        this.localesLazyModel.getFilterss().put("estadoLocal", true);
        if (this.mercado != null && this.mercado.getId() != null) {
            this.localesLazyModel.getFilterss().put("mercado", mercado);
        }
        PrimeFaces.current().ajax().update("tableLocales");
    }

    public void opendlg(Boolean accion) {
        if (accion) {
            //Locales
            this.localesLazyModel = new LazyModel<>(Locales.class);
            this.localesLazyModel.getSorteds().put("id", "ASC");
            this.localesLazyModel.getFilterss().put("estado", true);
            this.localesLazyModel.getFilterss().put("estadoLocal", true);
            PrimeFaces.current().executeScript("PF('localesDlg').show()");
            PrimeFaces.current().ajax().update("tableLocales");
        } else {
            if (searchIdentificacion.equals("")) {
                callLazyCliente();
            } else {
                if (validacionesIdentificacion(searchIdentificacion)) {
                    return;
                }
                String identificacion = "";
                String ruc = "";
                if (searchIdentificacion.length() > 10) {
                    identificacion = searchIdentificacion.substring(0, 10);
                    ruc = searchIdentificacion.substring(10, 13);
                } else {
                    identificacion = searchIdentificacion;
                }
                addArrendatario(arrendamientoService.findProveedor(identificacion, ruc));
                searchIdentificacion = "";
                PrimeFaces.current().ajax().update("otrosProveedoresTable");
//                PrimeFaces.current().ajax().update("buscarOperadora");
            }
        }
    }

    private Boolean validacionesIdentificacion(String identificacion) {
        Boolean resultado = false;
        if (identificacion.length() < 10) {
            JsfUtil.addWarningMessage("AVISO!!", "El tamaño de identificación es incorrecta");
            resultado = true;
        } else {
            if (!Utils.validateCCRuc(identificacion)) {
                JsfUtil.addWarningMessage("AVISO!!", "La identificación es incorrecta");
                resultado = true;
            }
        }
        return resultado;
    }

    private void callLazyProveedor() {
        //Proveedores
        this.proveedorLazyModel = new LazyModel<>(Proveedor.class);
        this.proveedorLazyModel.getSorteds().put("id", "ASC");
        this.proveedorLazyModel.getFilterss().put("estado", true);
        PrimeFaces.current().executeScript("PF('proveedoresDlg').show()");
        PrimeFaces.current().ajax().update("tableProveedores");
    }

    private void callLazyCliente() {
        //Proveedores
        this.clienteLazy = new LazyModel<>(Cliente.class);
        this.clienteLazy.getSorteds().put("id", "ASC");
        this.clienteLazy.getFilterss().put("estado", true);
        this.clienteLazy.getFilterss().put("validado", true);
        PrimeFaces.current().executeScript("PF('clienteDlg').show()");
        PrimeFaces.current().ajax().update("formCliente");
    }

    public void closeDlg(Locales local, Cliente operadoras) {
        if (local != null) {
            arrendamiento.setLocal(local);
            recorrerOperadores(operadoras);
            PrimeFaces.current().executeScript("PF('localesDlg').hide()");
        } else {
            addArrendatario(operadoras);
            PrimeFaces.current().executeScript("PF('proveedoresDlg').hide()");
        }
        PrimeFaces.current().ajax().update("fielsetLocal");
        PrimeFaces.current().ajax().update("otrosProveedoresTable");
    }

    public void closeDlgCliente(Cliente cliente) {

        addArrendatario(cliente);
        PrimeFaces.current().executeScript("PF('clienteDlg').hide()");
        PrimeFaces.current().ajax().update("formCliente");

        PrimeFaces.current().ajax().update("fielsetLocal");
        PrimeFaces.current().ajax().update("otrosProveedoresTable");
    }

    private void addArrendatario(Cliente arrendador) {
        if (arrendador.getId() != null) {
            Boolean accion = false;
            Arrendatarios arrendatario = new Arrendatarios();
            arrendatario.setPersona(arrendador);
            if (arrendatariosList.isEmpty()) {
                arrendatariosList.add(arrendatario);
                recorrerOperadores(arrendador);
            } else {
                for (Arrendatarios _arrendatario : arrendatariosList) {
                    if (Objects.equals(_arrendatario.getIdOperador(), arrendador)) {
                        accion = false;
                        JsfUtil.addWarningMessage("AVISO!!!", "Ya se encuentra en el listado");
                        break;
                    } else {
                        accion = true;
                    }
                }
                if (accion) {
                    arrendatariosList.add(arrendatario);
                    recorrerOperadores(arrendador);
                }
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "El RUC ingresado no se encuentra registrado en los proveedores");
        }
    }

    public void recorrerOperadores(Cliente operador) {
        if (!arrendatariosList.isEmpty()) {
            for (Arrendatarios arrendatario : arrendatariosList) {
                valorCompartido(arrendatario, operador);
            }
        }
    }

    private void valorCompartido(Arrendatarios arrendatario, Cliente operador) {
        double valor = valorTarifa().doubleValue();
        System.out.println("valor>>>" + valor);
        if (arrendamiento.getValorCompartido()) {
            if (valor > 0) {
                valorOperador(arrendatario, operador, valor / arrendatariosList.size());
            } else {
                valorOperador(arrendatario, operador, valor);
            }
        } else {
            valorOperador(arrendatario, operador, valor);
        }
    }

    public void calculosArriendo() {
        if (!arrendatariosList.isEmpty()) {
            for (Arrendatarios arr : arrendatariosList) {
                this.calcularIVA(arr);
            }
        }
    }

    private void valorOperador(Arrendatarios arrendatario, Cliente operador, double valor) {
        if (arrendatario.getIdOperador() != null) {
            arrendatario.setValorArriendo(new BigDecimal(valor));
            calcularIVASubtotal(arrendatario);
        } else {
            arrendatario.setValorArriendo(new BigDecimal(valor));
            calcularIVASubtotal(arrendatario);
        }
        System.out.println("valor arriendo>>" + arrendatario.getValorArriendo());
        System.out.println("valor getIva>>" + arrendatario.getIva());
        System.out.println("valor getSubtotal>>" + arrendatario.getSubtotal());
    }

    private BigDecimal valorTarifa() {
        if (arrendamiento.getLocal() != null) {
            if (arrendamiento.getLocal().getIdTarifa() != null) {
                return arrendamiento.getLocal().getIdTarifa().getValores();
            } else {
                return BigDecimal.ZERO;
            }
        } else {
            return BigDecimal.ZERO;
        }
    }

    public void calcularCanon(Arrendatarios arrendatarios) {
        arrendatarios.setCanonArriendo(arrendatarios.getValorArriendo().add(arrendatarios.getAlicuota()));
    }

    public void calcularIVASubtotal(Arrendatarios arrendatarios) {
        if (tieneIVA) {
            arrendatarios.setSubtotal(new BigDecimal(arrendatarios.getValorArriendo().doubleValue() / (1 + (iva / 100))));
            arrendatarios.setIva(new BigDecimal(arrendatarios.getSubtotal().doubleValue() * (iva / 100)));
            calcularCanon(arrendatarios);
        } else {
            arrendatarios.setSubtotal(new BigDecimal(arrendatarios.getValorArriendo().doubleValue()));
            arrendatarios.setIva(new BigDecimal(arrendatarios.getSubtotal().doubleValue()));
            calcularCanon(arrendatarios);
        }

    }

    public void calcularIVA(Arrendatarios arrendatarios) {
        if (tieneIVA) {
            arrendatarios.setIva(new BigDecimal(arrendatarios.getSubtotal().doubleValue() * (iva / 100)));
            arrendatarios.setValorArriendo(arrendatarios.getSubtotal().add(arrendatarios.getIva()));
            calcularCanon(arrendatarios);
        } else {
            arrendatarios.setIva(BigDecimal.ZERO);
            arrendatarios.setValorArriendo(arrendatarios.getSubtotal().add(arrendatarios.getIva()));
            calcularCanon(arrendatarios);
        }

    }

    public void eliminarArrendatario(Arrendatarios arrendatario) {
        if (arrendatario.getId() != null) {
            arrendatariosListDelete.add(arrendatario);
        }
        arrendatariosList.remove(arrendatario);
    }

    public void generarOrdenPago(Arrendamiento arrendamiento, Boolean accion) {
        if ((new Date()).before(arrendamiento.getFechaVigencia())) {
            int i = 2;
            arrendatariosList = arrendatariosService.getArrendatariosList(arrendamiento);
            for (Arrendatarios arrendatario : arrendatariosList) {
                if (!ordenesEmitidasService.getConsultarOrdenPorMes(arrendatario, Utils.getMes(new Date()), Utils.getAnio(new Date()))) {
                    i = ordenesEmitidasService.getGenerarOrdenPagoArriendo(arrendatario, Utils.getMes(new Date()), Utils.getAnio(new Date()).shortValue());
                }
            }
            if (accion) {
                if (i == 2) {
                    JsfUtil.addWarningMessage("AVISO!!", "Las ordenes de pago ya han sido generadas");
                } else if (i == 0) {
                    JsfUtil.addSuccessMessage("INFO!!", "Se ha generado correctamente la(s) orden(es) de pago del mes " + Utils.convertirMesALetra(Utils.getMes(new Date())).toUpperCase());
                } else if (i == 1) {
                    JsfUtil.addErrorMessage("ERROR!!", "No se pudo generar la orden de pago");
                }
            }
        } else {
            JsfUtil.addWarningMessage("AVISO!!!", "EL ARRIENDO FINALIZO SU FECHA DE VIGENCIA");
        }
    }

    public void GenerarFacturas() {
        List<Arrendamiento> arrendamientoList = arrendamientoService.findArrendamiento();
        for (Arrendamiento item : arrendamientoList) {
            generarOrdenPago(item, false);
        }
    }

    public void GenerarFacturasReporte() {
        servletSession.addParametro("mesBusqueda", Utils.getMes(new Date()));
        servletSession.addParametro("mesLetraBusqueda", Utils.convertirMesALetra(Utils.getMes(new Date())).toUpperCase());
        servletSession.addParametro("anioBusqueda", Utils.getAnio(new Date()).shortValue());
        servletSession.addParametro("fechaBusqueda", new Date());
        servletSession.setNombreReporte("ReporteArendamientosPorGenerar");
        servletSession.setNombreSubCarpeta("Arrendamiento");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void generarReporteArriendos() {
        servletSession.setNombreReporte("ReporteArrendamientoDetalle");
        servletSession.setNombreSubCarpeta("Arrendamiento");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public Arrendamiento getArrendamiento() {
        return arrendamiento;
    }

    public void setArrendamiento(Arrendamiento arrendamiento) {
        this.arrendamiento = arrendamiento;
    }

    public LazyModel<Arrendamiento> getArrendamientoLazyModel() {
        return arrendamientoLazyModel;
    }

    public void setArrendamientoLazyModel(LazyModel<Arrendamiento> arrendamientoLazyModel) {
        this.arrendamientoLazyModel = arrendamientoLazyModel;
    }

    public LazyModel<Proveedor> getProveedorLazyModel() {
        return proveedorLazyModel;
    }

    public void setProveedorLazyModel(LazyModel<Proveedor> proveedorLazyModel) {
        this.proveedorLazyModel = proveedorLazyModel;
    }

    public LazyModel<Locales> getLocalesLazyModel() {
        return localesLazyModel;
    }

    public void setLocalesLazyModel(LazyModel<Locales> localesLazyModel) {
        this.localesLazyModel = localesLazyModel;
    }

    public List<CatalogoItem> getPeriodoCobroList() {
        return periodoCobroList;
    }

    public void setPeriodoCobroList(List<CatalogoItem> periodoCobroList) {
        this.periodoCobroList = periodoCobroList;
    }

    public Boolean getVer() {
        return ver;
    }

    public void setVer(Boolean ver) {
        this.ver = ver;
    }

    public String getSearchIdentificacion() {
        return searchIdentificacion;
    }

    public void setSearchIdentificacion(String searchIdentificacion) {
        this.searchIdentificacion = searchIdentificacion;
    }

    public double getIva() {
        return iva;
    }

    public void setIva(double iva) {
        this.iva = iva;
    }

    public List<Arrendatarios> getArrendatariosList() {
        return arrendatariosList;
    }

    public void setArrendatariosList(List<Arrendatarios> arrendatariosList) {
        this.arrendatariosList = arrendatariosList;
    }

    public LazyModel<Cliente> getClienteLazy() {
        return clienteLazy;
    }

    public void setClienteLazy(LazyModel<Cliente> clienteLazy) {
        this.clienteLazy = clienteLazy;
    }

    public List<Mercado> getMercadoList() {
        return mercadoList;
    }

    public void setMercadoList(List<Mercado> mercadoList) {
        this.mercadoList = mercadoList;
    }

    public Mercado getMercado() {
        return mercado;
    }

    public void setMercado(Mercado mercado) {
        this.mercado = mercado;
    }

    public Boolean getTieneIVA() {
        return tieneIVA;
    }

    public void setTieneIVA(Boolean tieneIVA) {
        this.tieneIVA = tieneIVA;
    }

}
