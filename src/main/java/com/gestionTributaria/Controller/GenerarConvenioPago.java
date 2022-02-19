/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gestionTributaria.Controller;

import com.asgard.Entity.FinaRenDetLiquidacion;
import com.asgard.Entity.FinaRenEstadoLiquidacion;
import com.asgard.Entity.FinaRenLiquidacion;
import com.asgard.Entity.FinaRenPago;
import com.asgard.Entity.FinaRenRubrosLiquidacion;
import com.asgard.Entity.FinaRenTipoLiquidacion;
import com.gestionTributaria.Commons.NumberToLatter;
import com.gestionTributaria.Commons.SisVars;
import com.gestionTributaria.Entities.FnConvenioPago;
import com.gestionTributaria.Entities.FnConvenioPagoDetalle;
import com.gestionTributaria.Entities.FnConvenioPagoObservacion;
import com.gestionTributaria.Entities.RenParametrosInteresMulta;
import com.gestionTributaria.Recaudacion.RecaudacionInteface;
import com.gestionTributaria.Services.FinaRenLiquidacionService;
import com.gestionTributaria.Services.FnConvenioPagoService;
import com.gestionTributaria.Services.ManagerService;
import com.gestionTributaria.models.DetalleConvenio.DetalleConvenioModel;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.Cliente;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

/**
 *
 * @author Administrator
 */
@Named(value = "generarConvenioView")
@ViewScoped
public class GenerarConvenioPago implements Serializable {

    public static final Long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(GenerarConvenioPago.class.getName());
    @Inject
    private ManagerService services;
    @Inject
    private UserSession session;
    @Inject
    private ServletSession ss;
    @Inject
    private FinaRenLiquidacionService liquidacionService;
    @Inject
    private FnConvenioPagoService convenioPagoService;
    @Inject
    private RecaudacionInteface recaudacionService;

    private RenParametrosInteresMulta interesMulta;
    private LazyModel<FinaRenLiquidacion> liquidaciones;
    private List<FinaRenLiquidacion> seleccionadas;
    private LazyModel<Cliente> solicitantes;
    private FnConvenioPago convenioPago;
    private BigDecimal deudaTotal;

    private LazyModel<FnConvenioPago> convenios, conveniosAgua;
    private String observaciones;
    private boolean crear = false;

    private String observacionesAguaPotable;

    private String solicitante = null;
    private Map<String, Object> param;

    /*AGREGADO*/
    protected List<String> user = new ArrayList<>();

    @PostConstruct
    public void initView() {
        param = new HashMap();
        deudaTotal = BigDecimal.ZERO;
        seleccionadas = new ArrayList<>();
        solicitantes = new LazyModel(Cliente.class);
        solicitantes.getSorteds().put("id", "DESC");

        liquidaciones = new LazyModel(FinaRenLiquidacion.class);
        liquidaciones.getFilterss().put("estadoLiquidacion", new FinaRenEstadoLiquidacion(6L));
        if (session.getDepts().contains(8L)) {
            convenios = new LazyModel(FnConvenioPago.class);
            convenios.getFilterss().put("estado", new Short[]{0, 1, 2, 3, 4, 5, 6});

        } else {
            if (session.getDepts().contains(51L)) {
//                conveniosAgua = new FnConvenioPagoLazy(new Short[]{0, 1, 2, 3, 4, 5, 6}, Boolean.TRUE);
                conveniosAgua = new LazyModel(FnConvenioPago.class);
                convenios.getFilterss().put("estado", new Short[]{0, 1, 2, 3, 4, 5, 6});
                //convenios.getFilterss().put("convenioAgua", Boolean.TRUE);
            } else {
//                convenios = new FnConvenioPagoLazy(new Short[]{0, 1, 2, 3, 4, 5, 6}, Boolean.FALSE);
                convenios = new LazyModel(FnConvenioPago.class);
                convenios.getFilterss().put("estado", new Short[]{0, 1, 2, 3, 4, 5, 6});
                //convenios.getFilterss().put("convenioAgua", Boolean.FALSE);
            }
        }
        crear = false;
    }

    public void generarTitulosCredito(Long idCv) {
        if (idCv != null) {
            ss.addParametro("IDCV", idCv);
        } else {
            ss.addParametro("IDCV", 0L);
        }
        ss.addParametro("LOGO", JsfUtil.getRealPath("resources/images/duranLogo.png"));
        ss.setNombreSubCarpeta("/GestionTributatia/Recaudacion/");
        ss.setNombreReporte("titulosConvenio");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
    }

    public void openDlgConvenio(FnConvenioPago convenioPago) {
        if (!tienePagoInicial(convenioPago)) {
            Map<String, List<String>> params = new HashMap<>();
            List<String> p = new ArrayList<>();
            if (convenioPago != null) {
                p.add(convenioPago.getId().toString());
                params.put("idConvenio", p);
                p = new ArrayList<>();
                p.add(convenioPago.getDescripcion());
                params.put("descripcion", p);
                crear = false;
                p = new ArrayList<>();
                p.add(convenioPago.getContribuyente().getId().toString());
                params.put("contribuyente", p);

            } else {
                crear = true;
            }
            p = new ArrayList<>();
            p.add("1");
            params.put("nuevo", p);

            p = new ArrayList<>();
            p.add(deudaTotal.toString());
            params.put("deudaInicial", p);
            p = new ArrayList<>();
            p.add("5");
            params.put("calculaInteres", p);
            p = new ArrayList<>();
            p.add("true");
            params.put("aplTasa", p);
            if (!seleccionadas.isEmpty()) {
                if (seleccionadas.get(0).getTipoLiquidacion().getId() != 13) {
                    p = new ArrayList<>();
                    p.add(seleccionadas.get(0).getComprador().getId().toString());
                    params.put("contribuyente", p);
                }
            }
            Map<String, Object> options = new HashMap<>();
            options.put("resizable", false);
            options.put("draggable", false);
            options.put("modal", true);
            options.put("width", "50%");
            options.put("height", "85%");
            options.put("closable", true);
            options.put("contentWidth", "100%");
            options.put("contentHeight", "100%");
            PrimeFaces.current().dialog().openDynamic("/resources/dialog/dlgConvenioPago", options, params);
        }
    }

    public Boolean tienePagoInicial(FnConvenioPago convenioPago) {
        Boolean tienePagoInicial = Boolean.FALSE;
        if (convenioPago != null) {
            if (convenioPago.getPorcientoInicial().compareTo(BigDecimal.ZERO) == 0) {
                JsfUtil.addErrorMessage("Info", "El convenio es por medio de Remision de Intereses no necesita pago inicial");
                tienePagoInicial = Boolean.TRUE;
            } else {
                FinaRenLiquidacion temp = (FinaRenLiquidacion) services.getLiqInicialByConvenioAndTipoLiquidacion(convenioPago.getId(), 256L);
                if (temp != null) {
                    if (temp.getEstadoLiquidacion().getId() == 1L || temp.getEstadoLiquidacion().getId() == 2L) {
                        JsfUtil.addErrorMessage("Info", "El convenio esta en proceso del cobro Inicial");
                        tienePagoInicial = Boolean.TRUE;
                    }
                }
            }
        }
        return tienePagoInicial;
    }

    public Boolean estadoPagoinicial(FnConvenioPago convenioPago) {
        Boolean tienePagoInicial = Boolean.FALSE;
        FinaRenLiquidacion temp = (FinaRenLiquidacion) (FinaRenLiquidacion) services.getLiqInicialByConvenioAndTipoLiquidacion(convenioPago.getId(), 256L);
        if (temp != null) {
            if (temp.getEstadoLiquidacion().getId() == 1L || temp.getEstadoLiquidacion().getId() == 2L) {
                JsfUtil.addErrorMessage("Info", "El convenio esta en proceso del cobro Inicial");
                tienePagoInicial = Boolean.TRUE;
            }
        }
        return tienePagoInicial;
    }

    public BigDecimal calcularInteres(FinaRenLiquidacion liq) {
        BigDecimal interes = BigDecimal.ZERO;
        Map<String, BigDecimal> interesMap = new HashMap<>();
        interesMap = services.valoresInteres(liq, new Date());
        interes = interesMap.get("interesCalculado");
        System.out.println("interes 1  " + interes);
        if (liq.getTipoLiquidacion().getId().equals(2L) || liq.getTipoLiquidacion().getId().equals(3L)) {
            liq.setDescuento(interesMap.get("descuento"));
            liq.setRecargo(interesMap.get("recargo"));
        }
        if (liq.getRecargo() == null) {
            liq.setRecargo(BigDecimal.ZERO);
        }
        if (liq.getDescuento() == null) {
            liq.setDescuento(BigDecimal.ZERO);
        }

        System.out.println(" liq.getEstadoLiquidacion().getId()  " + liq.getEstadoLiquidacion().getId());
        if (!liq.getEstadoLiquidacion().getId().equals(2L) && !liq.getEstadoLiquidacion().getId().equals(6L)) {
            System.out.println("ingreando " + liq.getInteresFina());
            interes = liq.getInteresFina();

        }

        liq.setInteres(interes);
        liq.calcularPago();
        return liq.getInteres();
    }

    public void procesarConvenio(SelectEvent event) {
        convenioPago = (FnConvenioPago) event.getObject();
        convenioPagoService.edit(convenioPago);
        seleccionadas.stream().map((l) -> {
            l.setValidada(Boolean.TRUE);
            return l;
        }).map((l) -> {
            l.setUsuarioValida(session.getNameUser());
            return l;
        }).map((l) -> {
            this.calcularInteres(l);
            recaudacionService.saveLiquidacionConvenio(l, convenioPago);
            l.setConvenioPago(convenioPago);
            return l;
        }).map((l) -> {
            l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(7L));
            return l;
        }).forEachOrdered((l) -> {
            liquidacionService.edit(l);
        });
        this.imprimirCuotasTemporal(convenioPago);
        initView();
        JsfUtil.addInformationMessage("Info", "El convenio se ha elaborado con exito.");
    }

    public void procesarConvenioAgua(SelectEvent event) {
        convenioPago = (FnConvenioPago) event.getObject();
        convenioPago.setConvenioAgua(Boolean.TRUE);
        convenioPago = (FnConvenioPago) services.save(convenioPago);
        seleccionadas.stream().map((l) -> {
            l.setValidada(Boolean.TRUE);
            return l;
        }).map((l) -> {
            l.setUsuarioValida(session.getNameUser());
            return l;
        }).map((l) -> {
            l.setConvenioPago(convenioPago);
            return l;
        }).map((l) -> {
            l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(7L));
            return l;
        }).forEachOrdered((l) -> {
            services.save(l);
        });

        initView();

        JsfUtil.addInformationMessage("Info", "El convenio se ha elaborado con exito.");
    }

    public void editarConvenio(SelectEvent event) {
        convenioPago = (FnConvenioPago) event.getObject();
        JsfUtil.addInformationMessage("Info", "Convenio editado con exito.");
    }

    public void aprobarConvenio(FnConvenioPago convenio, boolean aprobar) {
        Boolean tienePagoInicial = tienePagoInicial(convenio);
        FnConvenioPagoObservacion observacionConvenio = new FnConvenioPagoObservacion();
        observacionConvenio.setConvenio(convenio);
        observacionConvenio.setEstado(Boolean.TRUE);
        observacionConvenio.setUsuarioIngreso(session.getNameUser());
        observacionConvenio.setFechaIngreso(new Date());
        if (!tienePagoInicial && aprobar) {
            //activarConvenio(convenio, aprobar);
//            System.out.println("generar liquidacion inicial");
            generarLiquidacionInicialConvenio(convenio);
            convenio.setEstado((short) 2);
            convenio = (FnConvenioPago) services.save(convenio);
            JsfUtil.addInformationMessage("Info", "Pago del porcentaje inicial para procesar el convenio generado con exito.");
        } else {

            if (!tienePagoInicial && !aprobar) {
                List<FinaRenLiquidacion> liqs = services.getRenLiquidacionesByConvenioPago(convenio.getId(), 1L);
                //List<RenLiquidacion> liqInicial = manager.findAll(QuerysFinanciero.getRenLiquidacionesByConvenioPago, new String[]{"convenio", "estadoLiquidacion"}, new Object[]{convenio.getId(), 2L});
                for (FinaRenLiquidacion l : liqs) {
                    if (l.getTipoLiquidacion().getPrefijo().equals("ICP") || l.getTipoLiquidacion().getPrefijo().equals("CCP")) {
                        if (l.getEstadoLiquidacion().getId() != 1L) {
                            l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));
                        }
                    } else {
                        l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                    }
                    l.setConvenioPago(null);
                    services.save(l);
                }
                if (convenio.getEstado() == 3) {
                    convenio.setEstado((short) 5);
                } else {
                    convenio.setEstado((short) 4);
                }
                convenio = (FnConvenioPago) services.save(convenio);
                JsfUtil.addInformationMessage("Info", "El convenio se ha cancelado con exito.");
            }
        }
        ///VARIABLEE PARA LA ELIMINACION = O
        if (!tienePagoInicial) {
            observacionConvenio.setObservacion(convenio.getObservacion());
            observacionConvenio.setEstadoConvenio(convenio.getEstado());
            services.save(observacionConvenio);
        }

    }

    public void activarConvenio(FnConvenioPago convenio, boolean activar) {
        if (!deshabilitarOpcionAprobar(convenio)) {
            FnConvenioPagoObservacion observacionConvenio = new FnConvenioPagoObservacion();
            observacionConvenio.setConvenio(convenio);
            observacionConvenio.setEstado(Boolean.TRUE);

            observacionConvenio.setUsuarioIngreso(session.getNameUser());
            observacionConvenio.setFechaIngreso(new Date());
            if (activar) {
                param = new HashMap<>();
                param.put("liquidacion.convenioPago.id", convenio.getId());
                param.put("liquidacion.tipoLiquidacion", 260);
                FinaRenPago pagoInicial = (FinaRenPago) services.findByParameter(FinaRenPago.class, param);
//                if (convenio.getConvenioAgua()) {
////                    cabeceraMemoConvenioAgua(convenio);
//                } else {
                cabeceraMemoConvenio(convenio);
//                }
                convenio.setPagoInicial(pagoInicial);
                JsfUtil.addInformationMessage("Info", "El convenio se ecuentra activado.");
            } else {
                //SELECT liquidacion FROM RenLiquidacion liquidacion WHERE liquidacion.convenioPago.id = :convenio AND liquidacion.estadoLiquidacion.id <> :estadoLiquidacion
                param = new HashMap<>();
                param.put("convenioPago.id", convenio.getId());
                param.put("estadoLiquidacion.id", 1L);
                List<FinaRenLiquidacion> liqs = services.findAll(FinaRenLiquidacion.class, param);

                for (FinaRenLiquidacion l : liqs) {
                    if (l.getTipoLiquidacion().getPrefijo().equals("ICP") || l.getTipoLiquidacion().getPrefijo().equals("CCP")) {
                        l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(3L));

                    } else {
                        l.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
                    }

                    services.save(l);
                }

                convenio.setEstado((short) 5);
                convenio = (FnConvenioPago) services.save(convenio);
                JsfUtil.addInformationMessage("Info", "Convenio CANCELADO con exito.");
            }

            observacionConvenio.setEstadoConvenio(convenio.getEstado());
            observacionConvenio.setObservacion(convenio.getObservacion());
            services.save(observacionConvenio);
            JsfUtil.update("frmTexto");
            JsfUtil.executeJS("PF('dlgMemoConvenio').show();");
        } else {
            JsfUtil.addErrorMessage("Info", "El convenio esta en proceso del cobro Inicial");
        }

    }

    public void imprimirConvenioCuotaInicial(FnConvenioPago fnc) {
        ss.borrarParametros();
        ss.instanciarParametros();
        ss.setNombreSubCarpeta("GestionTributatia/convenios");
        ss.addParametro("ID", fnc.getId());
        ss.setNombreReporte("sConveniodePago");
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public void onRowSelect(SelectEvent event) {
        BigDecimal suma = BigDecimal.ZERO;
        for (FinaRenLiquidacion liq : seleccionadas) {
            suma = suma.add(liq.getTotalPago()).add(calcularInteres(liq));
        }
        deudaTotal = suma;
    }

    public void onRowUnselect(UnselectEvent event) {
        BigDecimal suma = BigDecimal.ZERO;
        for (FinaRenLiquidacion liq : seleccionadas) {
            suma = suma.add(liq.getTotalPago()).add(calcularInteres(liq));
        }
        deudaTotal = suma;
    }

    public void onRowToggle() {
        BigDecimal suma = BigDecimal.ZERO;
        for (FinaRenLiquidacion liq : seleccionadas) {
            suma = suma.add(liq.getTotalPago()).add(calcularInteres(liq));
        }
        deudaTotal = suma;
    }

    public boolean generarLiquidacionInicialConvenio(FnConvenioPago convenio) {
        if (convenio != null) {
            try {
                Calendar calendar = Calendar.getInstance();
                FinaRenLiquidacion liq = new FinaRenLiquidacion();
                FinaRenTipoLiquidacion tipoLiq = (FinaRenTipoLiquidacion) services.getRenTipoLiquidacionByCodigoReporte(256L);
                liq.setTipoLiquidacion(tipoLiq);
                liq.setAnio(calendar.get(Calendar.YEAR));
                liq.setTotalPago(convenio.getValorPorcientoInicial());
                liq.setUsuarioIngreso(session.getNameUser());
                liq.setValidada(Boolean.TRUE);
                liq.setComprador(convenio.getContribuyente());
                liq.setVendedor(convenio.getContribuyente());
                liq.setConvenioPago(convenio);
                liq.setSaldo(convenio.getValorPorcientoInicial());
                if (convenio.getPredio() != null) {
                    liq.setPredio(convenio.getPredio());
                }
                FinaRenDetLiquidacion det = new FinaRenDetLiquidacion();
                List<FinaRenDetLiquidacion> listDet = new ArrayList<>();

                det.setRubro(new FinaRenRubrosLiquidacion(761L));
                det.setValor(liq.getTotalPago());
                det.setEstado(true);
                det.setValorRecaudado(BigDecimal.ZERO);
                listDet.add(det);
                liquidacionService.crearLiquidacion(liq, listDet);

                return true;
            } catch (Exception ex) {
                return false;
            }
        }
        return false;
    }

    private List<DetalleConvenioModel> getDetalleConvenioTemp(FnConvenioPago convenio) {
        List<DetalleConvenioModel> listDetalle = new ArrayList<>();
        BigDecimal valorCuota = convenio.getDeudaDiferir().divide(new BigDecimal(convenio.getCantidadMesesDiferir()), 2, RoundingMode.HALF_UP);
        BigDecimal diferencia = convenio.getDeudaDiferir().subtract(valorCuota.multiply(new BigDecimal(convenio.getCantidadMesesDiferir())));

        Calendar calendar = Calendar.getInstance();
        Calendar c = Calendar.getInstance();
        c.setTime(convenio.getFechaPrimeraCuota());
        FinaRenTipoLiquidacion tipoLiq = services.getRenTipoLiquidacionByCodigoReporte(257L);
        BigDecimal deudaFiferencial = convenio.getDiferenciaFinanciar();
        BigDecimal interesDeuda = BigDecimal.ZERO;
        for (int i = 0; i < convenio.getCantidadMesesDiferir(); i++) {
            BigDecimal interes = deudaFiferencial.multiply(convenio.getTasaInteresMensual()).setScale(2, RoundingMode.HALF_UP).divide(new BigDecimal("100"));
            System.out.println("capital>> " + deudaFiferencial + " interes>>" + interes);
            interesDeuda = interesDeuda.add(interes);
            DetalleConvenioModel dt = new DetalleConvenioModel();
            dt.setAbono(valorCuota);
            dt.setCuota(i + 1);
            if (i == 0) {
                dt.setDividendo(valorCuota.add(interes).add(diferencia));
                dt.setFechaPago(c.getTime());
            } else {
                dt.setDividendo(valorCuota.add(interes));
                c.add(Calendar.MONTH, i);
                dt.setFechaPago(c.getTime());
            }
            dt.setCapitalReducido(deudaFiferencial);
            dt.setInteres(interes);
            dt.setEstado("PENDIENTE");
            deudaFiferencial = deudaFiferencial.subtract(valorCuota);
            listDetalle.add(dt);
        }
        return listDetalle;
    }

    private void imprimirCuotasTemporal(FnConvenioPago convenio) {
        System.out.println("detalle size>>" + this.getDetalleConvenioTemp(convenio).size());
        ss.borrarParametros();
        ss.instanciarParametros();
        ss.setNombreReporte("convenioPagoTemp");
        ss.setNombreSubCarpeta("GestionTributatia/convenios");
        ss.addParametro("ID", convenio.getId());
        ss.addParametro("DETALLE", this.getDetalleConvenioTemp(convenio));
//        ss.setDataSource(this.getDetalleConvenioTemp(convenio));
        JsfUtil.redirectNewTab(CONFIG.URL_APP + "/Documento");
    }

    public boolean generarDetallesConvenio(FnConvenioPago convenio) {
        if (convenio != null) {
            try {
                BigDecimal valorCuota = convenio.getDeudaDiferir().divide(new BigDecimal(convenio.getCantidadMesesDiferir()), 2, RoundingMode.HALF_UP);
                BigDecimal diferencia = convenio.getDeudaDiferir().subtract(valorCuota.multiply(new BigDecimal(convenio.getCantidadMesesDiferir())));

                Calendar calendar = Calendar.getInstance();
                Calendar c = Calendar.getInstance();
                c.setTime(convenio.getFechaPrimeraCuota());
                FinaRenTipoLiquidacion tipoLiq = services.getRenTipoLiquidacionByCodigoReporte(257L);
                BigDecimal deudaFiferencial = convenio.getDiferenciaFinanciar();
                BigDecimal interesDeuda = BigDecimal.ZERO;
                for (int i = 0; i < convenio.getCantidadMesesDiferir(); i++) {
                    BigDecimal interes = deudaFiferencial.multiply(convenio.getTasaInteresMensual()).setScale(2, RoundingMode.HALF_UP).divide(new BigDecimal("100"));
                    System.out.println("capital>> " + deudaFiferencial + " interes>>" + interes);

                    interesDeuda = interesDeuda.add(interes);
                    FinaRenLiquidacion liq = new FinaRenLiquidacion();
                    liq.setTipoLiquidacion(tipoLiq);
                    liq.setAnio(calendar.get(Calendar.YEAR));

                    liq.setUsuarioIngreso(session.getNameUser());
                    liq.setValidada(Boolean.TRUE);
                    liq.setComprador(convenio.getContribuyente());
                    liq.setVendedor(convenio.getContribuyente());
                    liq.setNombreComprador(convenio.getContribuyente().getNombreCompleltoSql());
                    liq.setConvenioPago(convenio);
                    liq.setSaldo(valorCuota.add(interes));
                    liq.setTotalPago(valorCuota.add(interes));
                    liq.setCoactiva(Boolean.FALSE);
                    liq.setEstadoCoactiva(1);
                    if (i == 0) {
                        liq.setSaldo(valorCuota.add(interes).add(diferencia));
                        liq.setTotalPago(valorCuota.add(interes).add(diferencia));
                    }
                    liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(8L));
                    liq.setUsuarioValida(session.getNameUser());
                    liq.setIpUserSession(session.getIpClient());
                    liq.setMacAddresUsuarioIngreso(session.getMACAddressEquipo());
//                    if (convenio.getConvenioAgua()) {
//                        if (convenio.getCuentaAgua() != null) {
//                            liq.setCuenta(convenio.getCuentaAgua());
//                        }
//                    } else {
                    if (convenio.getPredio() != null) {
                        liq.setPredio(convenio.getPredio());
                    }
//                    }
                    liq = (FinaRenLiquidacion) services.save(liq);
                    liq.setNumLiquidacion(services.getMaxSecuenciaTipoLiquidacion(calendar.get(Calendar.YEAR), tipoLiq.getId()));
                    liq.setIdLiquidacion(Utils.getAnio(new Date()).toString().concat("-").concat(Utils.completarCadenaConCeros(liq.getNumLiquidacion().toString(), 6)).concat("-").concat(tipoLiq.getPrefijo()));

                    liq = (FinaRenLiquidacion) services.save(liq);

                    FinaRenDetLiquidacion det = new FinaRenDetLiquidacion();
                    det.setLiquidacion(liq);
                    det.setRubro(new FinaRenRubrosLiquidacion(765L));
                    det.setValor(liq.getTotalPago());
                    det.setEstado(true);
                    det.setValorRecaudado(BigDecimal.ZERO);
                    services.save(det);
                    FnConvenioPagoDetalle detalle = new FnConvenioPagoDetalle();
                    detalle.setConvenio(convenio);
                    detalle.setMes(i + 1);
                    detalle.setCapitalReducido(deudaFiferencial);
                    detalle.setInteres(interes);
                    detalle.setDeuda(liq.getTotalPago().subtract(interes));
                    detalle.setEstado(Boolean.TRUE);
                    detalle.setLiquidacion(liq);
                    if (i == 0) {
                        detalle.setFechaMaximaPago(c.getTime());
                    } else {
                        c.add(Calendar.MONTH, 1);
                        detalle.setFechaMaximaPago(c.getTime());
                    }
                    deudaFiferencial = deudaFiferencial.subtract(valorCuota);
                    services.save(detalle);
                }

                return true;
            } catch (Exception ex) {
                return false;
            }
        }

        return false;
    }

    public boolean deshabilitarOpcionEditar(FnConvenioPago convenio) {
        FinaRenLiquidacion temp = services.getLiqInicialByConvenioAndTipoLiquidacion(convenio.getId(), 256L);
        if (temp != null) {
            if (temp.getEstadoLiquidacion().getId() == 1L || temp.getEstadoLiquidacion().getId() == 2L) {
                return true;
            }
        }
        return false;
    }

    public boolean deshabilitarOpcionCancelar(FnConvenioPago convenio) {

        return convenio.getEstado() >= 4;
    }

    public boolean deshabilitarOpcionPagoInicial(FnConvenioPago convenio) {

        FinaRenLiquidacion temp = services.getLiqInicialByConvenioAndTipoLiquidacion(convenio.getId(), 256L);
        if (temp != null) {
            if (temp.getEstadoLiquidacion().getId() == 1L || temp.getEstadoLiquidacion().getId() == 2L) {
                return true;
            }
        }
        return false;
    }

    public boolean deshabilitarOpcionMemo(FnConvenioPago convenio) {
        if (convenio.getEstado() == 3 || convenio.getEstado() == 6) {
            return false;
        }
        return true;
    }

    public boolean deshabilitarOpcionAprobar(FnConvenioPago convenio) {
        if (convenio.getPorcientoInicial().compareTo(BigDecimal.ZERO) == 0) {
            return false;
        } else {
            FinaRenLiquidacion temp = (FinaRenLiquidacion) services.getLiqInicialByConvenioAndTipoLiquidacion(convenio.getId(), 256L);
            if (temp != null) {
                if (temp.getEstadoLiquidacion().getId() == 1L && convenio.getEstado() == 3) {
                    return true;
                }
                if (temp.getEstadoLiquidacion().getId() == 1L && (convenio.getEstado() == 0 || convenio.getEstado() == 1 || convenio.getEstado() == 2)) {
                    return false;
                }
                if (temp.getEstadoLiquidacion().getId() == 2L) {
                    return true;
                }
            }
            return false;
        }
    }

    public String cabeceraMemoConvenio(FnConvenioPago convenio) {
        BigInteger numComprobanteRentas = (BigInteger) services.getRenLiquidacionComprobanteCuentaPagoInicial(convenio.getId());
        if (numComprobanteRentas == null) {
            numComprobanteRentas = BigInteger.ZERO;
        }
        convenio.getFnConvenioPagoDetalleList();
        String representante = "";
        this.convenioPago = convenio;
        boolean personaNatural = false;
        solicitante = convenioPago.getContribuyente() == null ? "" : convenioPago.getContribuyente().getNombreCompleto();
        if (convenio.getContribuyente().getRuc() == null || convenio.getContribuyente().getRuc().length() < 1) {
            personaNatural = true;
            representante = solicitante;
        } else {
            if (convenioPago.getContribuyente().getRepresentanteLegal() != null) {
                param = new HashMap<>();
                Cliente rep = services.find(Cliente.class, convenioPago.getContribuyente().getIdRepresentanteLegal());
                representante = rep.getNombreCompleto();
            } else {
                representante = "NO TIENE REPRESENTANTE";
            }
        }
        List<FinaRenLiquidacion> ls = services.getLiqInicialByConvenioAndTipoLiquidacionJC(convenioPago.getId());
        String impuestos = "";
        List<String> nombresLiquidaciones = new ArrayList<>();
        String result = "";
        int cant = 0;
        if (ls.size() == 1) {
            nombresLiquidaciones.add(ls.get(0).getTipoLiquidacion().getNombreTransaccion());
            impuestos = ls.get(0).getTipoLiquidacion().getNombreTransaccion();
        } else {
            for (FinaRenLiquidacion l : ls) {
                if (!nombresLiquidaciones.contains(ls.get(cant).getTipoLiquidacion().getNombreTransaccion())) {
                    nombresLiquidaciones.add(ls.get(cant).getTipoLiquidacion().getNombreTransaccion());
                }
                cant++;
            }
        }
        for (int i = 0; i < nombresLiquidaciones.size(); i++) {
            if (i == 0) {
                impuestos = nombresLiquidaciones.get(i);
            } else {
                if (i < nombresLiquidaciones.size() - 1) {
                    impuestos += (", " + nombresLiquidaciones.get(i));
                } else {
                    impuestos += (" y " + nombresLiquidaciones.get(i));
                }
            }
        }

        if (cant == 0) {
            result = " impuesto de <span style=\"font-weight: bold;\">" + impuestos;
        } else {
            result = " impuestos de <span style=\"font-weight: bold;\">" + impuestos;
        }

        String fecha_Cadena = NumberToLatter.convertNumberToLetter(Utils.getPartDate(convenio.getFechaInicio(), 1), false);
        fecha_Cadena += " días  del mes " + Utils.convertirMesALetra(Utils.getPartDate(convenio.getFechaInicio(), 2));
        fecha_Cadena += " del año " + NumberToLatter.convertNumberToLetter(Utils.getPartDate(convenio.getFechaInicio(), 3), false);
        String cantida_inicial = NumberToLatter.convertNumberToLetter(convenio.getDeudaInicial().doubleValue(), true);

        //String nombrePersona1Cabecera = " DEUDOR";
        String nombrePersona1Cabecera = " ";
        if (!personaNatural) {
            nombrePersona1Cabecera = " en su calidad de Representante Legal de la Compañía <span style=\"font-weight: bold;\"> " + representante;
        }
        String Cabecera = "En la ciudad de " + SisVars.NOMBRECANTON + ", a los " + fecha_Cadena + ", comparecen, por una parte <span style=\"font-weight: bold;\"> Ing." + "</span>"
                + ", en su calidad de <span style=\"font-weight: bold;\">TESORERO MUNICIPAL - JUEZ DE COACTIVA; </span>" + "Ab. Cesar Demera Santos " + "<span style=\"font-weight: bold;\"> JEFE SECRETARIO DE COACTIVAS</span> "
                + "y , por otra parte el Sr(a).<span style=\"font-weight: bold;\">" + solicitante + " , " + convenio.getContribuyente().getIdentificacion() + convenio.getContribuyente().getRuc() + "</span>" + nombrePersona1Cabecera + "</span>"
                + " quienes celebran el convenio de pago al tenor de las siguientes cláusulas:<br><br>";

        String nombrePersona1RA = " ";
        //String nombrePersona1RA = " DEUDOR";
        if (!personaNatural) {
            nombrePersona1RA = " en calidad de Representante Legal de <span style=\"font-weight: bold;\">" + representante + "</span> DEUDOR";
        }
        String Primera = "<span style=\"font-weight: bold;\">PRIMERA Antecedentes.-</span>El Sr(a).<span style=\"font-weight: bold;\">" + solicitante + "</span>" + nombrePersona1RA
                + " , Mantiene una deuda con " + session.getUsuario().getEmpresaId().getNombreEntidad().toUpperCase() + ", por concepto de " + result + "</span>\n"
                + " la suma de <span style=\"font-weight: bold;\">$" + convenio.getDeudaInicial() + "</span>" + "(" + cantida_inicial + ")" + " por DEUDA de los años "
                + (convenio.getPredio() != null ? convenio.getPredio().getClaveCat() : "") + ".</span><br><br>";

        String nombrePersona2DA = "";
        if (!personaNatural) {
            nombrePersona2DA = " Representante legal de <span style=\"font-weight: bold;\">" + representante + "</span>";
        }

        String Segunda = "<span style=\"font-weight: bold;\">SEGUNDA: OBJETO DEL CONVENIO.-</span> De conformidad con lo que dispone el articulo 46 del código"
                + "Orgánico Tributario, en concordancia con el articulo 152 y 153 del mismo cuerpo legal del convenio, solicita mediante escrito se le conceda facilidades"
                + "para el pago del Título de crédito por los conceptos especificados en la cláusula anterior.<br><br>";

        String Segunda_b = "<span style=\"font-weight: bold;\"> ING." + representante + "</span>, en su calidad de "
                + "TESORERO MUNICIPAL, acepta la forma de pago impuesta mediante sumilla inserta en el trámite por parte"
                + "de la Dirección Financiera,la misma que será liquidada de conformidad a lo establecido en el ART.46 del"
                + " Código Organico Orgánico Tributario, deuda que incluira el (" + convenio.getPorcientoInicial() + "%) de "
                + "cuota inicial y en los (" + convenio.getCantidadMesesDiferir() + ") quedando establecida de las siguente manera:";
        String Segunda_c = "<br><br>Una vez efectuado el Utimo pago establecido, la obligación tributaria contraida con el <span style=\"font-weight: bold;\">" + session.getUsuario().getEmpresaId().getNombreEntidad().toUpperCase() + "</span>"
                + " quedará cancelada en su totalidad, en lo que se refiere al pago de los titulos señalados en la clausula anterior.";

        String Tercera = "<span style=\"font-weight: bold;\">TERCERA: INCUMPLIMIENTO.-</span>En caso de que, Sr(a) <span style=\"font-weight: bold;\">" + solicitante + "</span>"
                + " incumpliera alguna parte del convenio, se sujetara a lo dispuesto en el ART. 156 del Código Organico Tributario, pódra iniciarce la acción de coactiva con todos las"
                + " medidas cautelares que permita la ley."
                + "<br><br>Para Constancia de todo lo actuado, suscriben las partes al presnte convenio por cinco copias"
                + "a los " + fecha_Cadena + " <br><br>";
        this.observaciones = Cabecera + Primera + Segunda + Segunda_b + "###" + Segunda_c + Tercera;
        return Cabecera + Primera + Segunda + Tercera;

    }

    public void generarMemoConvenio(FnConvenioPago c) {
        try {
            String detalle;
            String detalle2 = "";
            String[] array;
            if (c != null) {
                convenioPago = c;
                array = convenioPago.getMemoDetalle().split("###");
                detalle = array[0];
                detalle2 = array[1];
            } else {
                convenioPago.setEstado((short) 3);
                generarDetallesConvenio(convenioPago);
                convenioPago.setMemoDetalle(this.observaciones);
                services.save(convenioPago);
                array = convenioPago.getMemoDetalle().split("###");
                detalle = array[0];
                detalle2 = array[1];
            }
//            System.out.println("PPPP" + detalle);
            SimpleDateFormat df = new SimpleDateFormat("MMMMM", new Locale("es", "ES"));
            String Fecha = SisVars.NOMBRECANTON + ", " + String.format("%02d", Utils.getPartDate(this.convenioPago.getFechaInicio(), 1))
                    + " de " + df.format(new Date()) + " del " + Utils.getPartDate(this.convenioPago.getFechaInicio(), 3);
            String path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/");
            ss.borrarParametros();
            ss.instanciarParametros();
//            ss.setTieneDatasource(Boolean.TRUE);
            ss.addParametro("USUARIO_ELABORA", convenioPago.getUsuarioIngreso());
            ss.addParametro("USUARIO_INGRESO", convenioPago.getUsuarioIngreso());
            ss.addParametro("DETALLE", detalle);
            ss.addParametro("DETALLE_2", detalle2);
            ss.addParametro("ID", convenioPago.getId());
            ss.addParametro("TESORERO", "SERVIDOR NO ENCONTRADO");
            ss.addParametro("ABOGADO", "ABOGADO NO ENCONTRADO");
            ss.addParametro("REPRESENTANTE", convenioPago.getContribuyente().getNombreCompleto());
            ss.addParametro("CONTRIBUYENTE", convenioPago.getContribuyente().getNombreCompleto());
            ss.addParametro("CI/RUC", convenioPago.getContribuyente().getIdentificacion());
            ss.addParametro("FECHA_MEMO", Fecha);

            ss.setNombreSubCarpeta("GestionTributatia/convenios");
            ss.setNombreReporte("sMemoConvenioPagoFinal");

            JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
        } catch (Exception e) {
        }
    }

    /*AGUA*/
//    public String cabeceraMemoConvenioAgua(FnConvenioPago convenio) {
//        this.convenioPago = convenio;
//        /*FECHA*/
//        String fecha_Cadena = NumberToLatter.convertNumberToLetter(Utils.getPartDate(convenio.getFechaInicio(), 1), false);
//        fecha_Cadena += " dias del mes " + Utils.convertirMesALetra(Utils.getPartDate(convenio.getFechaInicio(), 2));
//        fecha_Cadena += " del año " + NumberToLatter.convertNumberToLetter(Utils.getPartDate(convenio.getFechaInicio(), 3), false);
//
////        Cuenta c = null;
//        List<FinaRenLiquidacion> cu = services.findAll(QuerysFinanciero.getRenLiquidacionObtenerCuenta, new String[]{"convenio"}, new Object[]{convenio.getId()});
//        BigInteger numComprobante = (BigInteger) services.find(QuerysFinanciero.getRenLiquidacionComprobanteCuentaPagoInicial, new String[]{"convenio"}, new Object[]{convenio.getId()});
//        if (numComprobante == null) {
//            numComprobante = BigInteger.ZERO;
//        }
//
//        if (cu != null) {
//            c = cu.get(0).getCuenta();
//        }
//
//        /*NOMBRE SOLICITANTE*/
//        solicitante = convenio.getContribuyente().getNombreCompleto();
//
//        String CabeceraAgua = "La Dirección de <span style=\"font-weight: bold;\">Agua Potable y Alcantarillado </span>"
//                + "entrega servicios de calidad en agua potable y alcantarillado a los usuarios del <span style=\"font-weight: bold;\">Cantón " + SisVars.NOMBRECANTON + "</span><br><br>";
//
//        String CuerpoAgua = "En la ciudad de " + SisVars.NOMBRECANTON + ", a los " + fecha_Cadena + ", comparecen, por una parte <span style=\"font-weight: bold;\"> Ing." + service.getNameUserByRol(104L) + "</span>"
//                + ", en su calidad de <span style=\"font-weight: bold;\">TESORERO MUNICIPAL - JUEZ DE COACTIVA; </span>" + "Ing. José Bello Sión, en su calidad de " + "<span style=\"font-weight: bold;\"> DIRECTOR DE AGUA POTABLE Y ALCANTARILLADO</span> "
//                + "y , por otra parte el Sr(a).<span style=\"font-weight: bold;\">" + solicitante + " , " + convenio.getContribuyente().getCiRuc() + "</span> DEUDOR </span>"
//                + " , quienes celebran el convenio de pago  al tenor de las siguientes cláusulas:<br><br>";
//
//        String PrimeraAgua = "<span style=\"font-weight: bold;\">PRIMERA.-</span> El Sr(a).<span style=\"font-weight: bold;\">" + solicitante + "</span>"
//                + " DEUDOR, debe al Gobierno Autónomo Municipal del cantón " + SisVars.NOMBRECANTON + ",  por concepto de una <span style=\"font-weight: bold;\">TASA - SERVICIO DE AGUA POTABLE Y ALCANTARILLADO</span>"
//                + " la suma de <span style=\"font-weight: bold;\">$" + convenio.getDeudaInicial() + "</span>" + " dólares correspondientes al ejercicio fiscal año <span style=\"font-weight: bold;\">" + Utils.getPartDate(convenio.getFechaInicio(), 3) + ".</span>, "
//                + getFechasConvenioLiquidaciones(c)
//                + " de la cuenta <span style=\"font-weight: bold;\"> " + c.getCuenta()
//                + " Código " + (c.getCuentaAnterior() != null ? c.getCuentaAnterior() : " Sin Código")
//                + " </span> de la Calle <span style=\"font-weight: bold;\">" + (c.getDireccion() != null ? c.getDireccion() : " SIN DIRECCION")
//                + "</span> perteneciente al cantón " + SisVars.NOMBRECANTON + ".<br><br>";
//
//        String SegundaAgua = "<span style=\"font-weight: bold;\">SEGUNDA.-</span> CONVENIO .- Al efecto el Sr.<span style=\"font-weight: bold;\">" + solicitante + "</span>"
//                + " solicita mediante petición verbal al <span style=\"font-weight: bold;\"> Ing." + service.getNameUserByRol(104L) + "</span> Tesorero del GAD. Municipal de " + SisVars.NOMBRECANTON + ", encargado de recaudar los tributos en mención y en su calidad de Juez de Coactivas,solicita se le conceda las facilidades de pago conforme lo dispuesto en los Arts.152 y 153 de la Nueva Codificación del "
//                + "Código Tributario, ofreciendo cancelar una entrada del  <span style=\"font-weight: bold;\">" + convenio.getPorcientoInicial() + "</span>% porciento del monto de la deuda y el saldo de la liquidación  previa aceptación y firma de convenio. Petición aceptada por el Tesorero Municipal y amparada en el art 46 del código tributario, se procede a realizar dicho Convenio.<br><br>";
//
//        String TerceraAgua = "<span style=\"font-weight: bold;\">TERCERA.-</span> Forma de Pago.-Con el recibo <span style=\"font-weight: bold;\">#" + numComprobante.toString() + "</span> cancela la suma de <span style=\"font-weight: bold;\">$" + convenio.getValorPorcientoInicial() + "</span> dólares americano como cuota inicial sobre el monto del valor a pagar por concepto de Deuda por " + "<span style=\"font-weight: bold;\">  TASA  DE AGUA POTABLE Y ALCANTARILLADO</span>, se establece que el obligado ha cancelado en la Recaudación de la Tesorería Municipal dicho valor,  con un saldo pendiente de pago de la suma de "
//                + " <span style=\"font-weight: bold;\">$" + convenio.getDiferenciaFinanciar() + " </span> dólares, lo mismo que cancelara en <span style=\"font-weight: bold;\">" + NumberToLatter.convertNumberToLetter(convenio.getCantidadMesesDiferir(), false) + "</span>"
//                + "meses plazo. <br><br>";
//
//        String CuartaAgua = "<span style=\"font-weight: bold;\">CUARTA.-</span> Aceptación.- El Sr(a).<span style=\"font-weight: bold;\">" + solicitante + "</span>"
//                + " Acepta la suscripción del presente convenio por convenir y sus intereses  y se compromete al pago puntual de las cuotas establecidas para fiel cumplimiento de la obligación, una vez cumplida se emitirá el título de crédito objeto del presente convenio. <br><br>";
//
//        String QuintaAgua = "<span style=\"font-weight: bold;\">QUINTA.-</span> Jurisdicción y Competencia.-El Sr. <span style=\"font-weight: bold;\">" + solicitante + "</span>"
//                + ", en caso de incumplimiento del convenio por la falta de pago, de uno o más dividendos, se somete a la jurisdicción coactiva , a la suspensión del servicio  y al trámite establecido en la Ley.<br><br>En el cuadro se detalla a continuación: <br><br>";
//
//        this.observaciones = CabeceraAgua + CuerpoAgua + PrimeraAgua + SegundaAgua + TerceraAgua + CuartaAgua + QuintaAgua;
//        return CabeceraAgua + CuerpoAgua + PrimeraAgua + SegundaAgua + TerceraAgua + CuartaAgua + QuintaAgua;
//    }
//    public String getFechasConvenioLiquidaciones(Cuenta c) {
//        String mesInicio = (String) manager.getNativeQuery(QuerysAguaPotable.mesInicioConvenio, new Object[]{c.getId()});
//        String mesFin = (String) manager.getNativeQuery(QuerysAguaPotable.mesFinConvenio, new Object[]{c.getId()});
//
//        return "Deuda desde: " + mesInicio + " Hasta: " + mesFin;
//    }
    public void restaurarLiquidacion(FinaRenLiquidacion liq) {
        liq.setEstadoLiquidacion(new FinaRenEstadoLiquidacion(2L));
        services.save(liq);
    }

    //<editor-fold defaultstate="collapsed" desc="SETTER AND GETTER">
    public RenParametrosInteresMulta getInteresMulta() {
        return interesMulta;
    }

    public void setInteresMulta(RenParametrosInteresMulta interesMulta) {
        this.interesMulta = interesMulta;
    }

    public LazyModel<FinaRenLiquidacion> getLiquidaciones() {
        return liquidaciones;
    }

    public void setLiquidaciones(LazyModel<FinaRenLiquidacion> liquidaciones) {
        this.liquidaciones = liquidaciones;
    }

    public List<FinaRenLiquidacion> getSeleccionadas() {
        return seleccionadas;
    }

    public void setSeleccionadas(List<FinaRenLiquidacion> seleccionadas) {
        this.seleccionadas = seleccionadas;
    }

    public LazyModel<Cliente> getSolicitantes() {
        return solicitantes;
    }

    public void setSolicitantes(LazyModel<Cliente> solicitantes) {
        this.solicitantes = solicitantes;
    }

    public FnConvenioPago getConvenioPago() {
        return convenioPago;
    }

    public void setConvenioPago(FnConvenioPago convenioPago) {
        this.convenioPago = convenioPago;
    }

    public BigDecimal getDeudaTotal() {
        return deudaTotal;
    }

    public void setDeudaTotal(BigDecimal deudaTotal) {
        this.deudaTotal = deudaTotal;
    }

    public LazyModel<FnConvenioPago> getConvenios() {
        return convenios;
    }

    public void setConvenios(LazyModel<FnConvenioPago> convenios) {
        this.convenios = convenios;
    }

    public LazyModel<FnConvenioPago> getConveniosAgua() {
        return conveniosAgua;
    }

    public void setConveniosAgua(LazyModel<FnConvenioPago> conveniosAgua) {
        this.conveniosAgua = conveniosAgua;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isCrear() {
        return crear;
    }

    public void setCrear(boolean crear) {
        this.crear = crear;
    }

    public String getObservacionesAguaPotable() {
        return observacionesAguaPotable;
    }

    public void setObservacionesAguaPotable(String observacionesAguaPotable) {
        this.observacionesAguaPotable = observacionesAguaPotable;
    }

    public String getSolicitante() {
        return solicitante;
    }

    public void setSolicitante(String solicitante) {
        this.solicitante = solicitante;
    }

    public List<String> getUser() {
        return user;
    }

    public void setUser(List<String> user) {
        this.user = user;
    }
//</editor-fold>

}
