/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.activos.controller;

import com.origami.sigef.activos.service.KardexService;
import com.origami.sigef.activos.service.detalleKardexService;
import com.origami.sigef.common.config.CONFIG;
import com.origami.sigef.common.entities.DetallaKardex;
import com.origami.sigef.common.entities.DetalleItem;
import com.origami.sigef.common.entities.Distributivo;
import com.origami.sigef.common.entities.InventarioItems;
import com.origami.sigef.common.entities.Kardex;
import com.origami.sigef.common.lazy.LazyModel;
import com.origami.sigef.common.service.ServletSession;
import com.origami.sigef.common.service.UserSession;
import com.origami.sigef.common.util.JsfUtil;
import com.origami.sigef.common.util.Utils;
import com.origami.sigef.contabilidad.service.ClienteService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import org.primefaces.PrimeFaces;

/**
 *
 * @author Luis Suarez
 */
@Named(value = "KardexVista")
@ViewScoped
public class kardexController implements Serializable {

    @Inject
    private KardexService kardexService;
    @Inject
    private ClienteService clienteService;
    @Inject
    private UserSession user;
    @Inject
    private detalleKardexService detalleKardexService;
    @Inject
    private ServletSession ss;

    private DetallaKardex detalleKardex;
    private Kardex kardex;
    private Distributivo distributivo;
    private List<InventarioItems> listaInventario;
    private List<DetalleItem> listaItems;
    private List<DetallaKardex> listaDetallaKardexs;
    private List<DetallaKardex> listavistaDetalleKardex;
    private boolean mostrarDetalleIemsArticulos;
    private Short fila, columna, estnate, cajon, cuadrante;
    private boolean tipoReporteKrdex;
    private List<InventarioItems> listaCalculoAnteriorInvemtario;
    private LazyModel<DetallaKardex> lazyKardex;
    private int valor;
    private Kardex reporte;
    private static final String estadoAdicionalInv = "COMPLETO";
    private boolean dataOcultar;
    private String anioActual;

    @PostConstruct
    public void inicializar() {
        this.kardex = new Kardex();
        this.distributivo = new Distributivo();
        this.distributivo = clienteService.getuusuarioLogeado(user.getNameUser());
        this.detalleKardex = new DetallaKardex();
        this.listaItems = kardexService.getListaProductos(estadoAdicionalInv);/*oki*/
        this.listavistaDetalleKardex = new ArrayList<>();
        this.listaInventario = new ArrayList<>();
        this.listaDetallaKardexs = new ArrayList<>();
        estnate = 0;
        fila = 0;
        columna = 0;
        cajon = 0;
        cuadrante = 0;
        mostrarDetalleIemsArticulos = false;
        tipoReporteKrdex = true;
        this.listaCalculoAnteriorInvemtario = new ArrayList<>();
        valor = 4;
        lazyKardex = new LazyModel<>(DetallaKardex.class);
        this.reporte = new Kardex();
        dataOcultar = false;
        Calendar c = Calendar.getInstance();
        anioActual = String.valueOf(c.get(Calendar.YEAR));
    }

    public void actualizaUbicacion() {
        if (kardex.getItemsProducto() == null) {
            estnate = 0;
            fila = 0;
            columna = 0;
            cajon = 0;
            cuadrante = 0;
            return;
        }
        InventarioItems i = kardexService.getubicacionIrems(kardex.getItemsProducto().getId());
        estnate = i.getEstante();
        fila = i.getFila();
        columna = i.getColumna();
        cajon = i.getCajon();
        cuadrante = i.getCuadrante();
        listaDetallaKardexs = null;
    }

    public void kardexAccion() {
        mostrarDetalleIemsArticulos = !tipoReporteKrdex;
        if (tipoReporteKrdex) {
            estnate = 0;
            fila = 0;
            columna = 0;
            cajon = 0;
            cuadrante = 0;
            kardex.setItemsProducto(null);
            listaDetallaKardexs.clear();
            valor = 4;
            lazyKardex = new LazyModel<>(DetallaKardex.class);
            dataOcultar = false;
        } else {
            listaDetallaKardexs.clear();
            valor = 3;
            lazyKardex = new LazyModel<>(DetallaKardex.class);
            dataOcultar = true;
        }
        kardex.setFechaDesde(new Date("01/01/" + anioActual));
        kardex.setFechaHasta(new Date());
    }

    public void realzarKardex() {
        listaInventario = new ArrayList<>();
        listaDetallaKardexs = new ArrayList<>();
        lazyKardex = new LazyModel<>(DetallaKardex.class);
        if (tipoReporteKrdex) {
            if (kardex.getFechaDesde() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Seleccione una Fecha Desde donde quiere realizar el KARDEX");
                return;
            }
            if (kardex.getFechaHasta() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Seleccione una Fecha Hasta donde quiere realizar el KARDEX");
                return;
            }
        } else {
            if (kardex.getItemsProducto() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Seleccione un Articulo");
                return;
            }
            if (kardex.getFechaDesde() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Seleccione una Fecha Desde donde quiere realizar el KARDEX");
                return;
            }
            if (kardex.getFechaHasta() == null) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "Seleccione una Fecha Hasta donde quiere realizar el KARDEX");
                return;
            }
        }
        if (!tipoReporteKrdex) {
            Kardex lista = kardexService.getlistaVerificacion(kardex.getItemsProducto(), kardex.getFechaDesde(), kardex.getFechaHasta());
            if (lista != null) {
                kardexService.deleteDetallekardex(lista);
            }
        } else {
            Kardex lis = kardexService.getlistaVerificacion(kardex.getFechaDesde(), kardex.getFechaHasta());
            if (lis != null) {
                kardexService.deleteDetallekardex(lis);
            }
        }
        listaDetallaKardexs = new ArrayList<>();
        kardex.setUnidadAdministrativa(clienteService.getUnidadKardex());
        kardex.setUsuarioCreacion(user.getName());
        kardex.setFechaCreacion(new Date());
        kardex.setUsuarioModificacion(user.getName());
        kardex.setFechaModificacion(new Date());
        kardexCalculado(tipoReporteKrdex);
        kardex.setListadetallaKardexs(listaDetallaKardexs);
        this.kardex = kardexService.create(kardex);
        PrimeFaces.current().ajax().update(":fmvistakardex", "messages");
        JsfUtil.addInformationMessage("Información", "Kardex Realizado con éxito");
        this.reporte = Utils.clone(this.kardex);
        if (tipoReporteKrdex) {
            imprimir();
            this.kardex = new Kardex();
            this.detalleKardex = new DetallaKardex();
        }
    }

    public void kardexCalculado(boolean a) {
        this.listaCalculoAnteriorInvemtario.clear();
        this.listaInventario.clear();
        this.listaCalculoAnteriorInvemtario = new ArrayList<>();
        listaDetallaKardexs.clear();
        this.listaInventario = new ArrayList<>();
        BigDecimal precio = BigDecimal.ZERO;
        BigDecimal total = BigDecimal.ZERO;
        BigDecimal iva = BigDecimal.ZERO;
        BigDecimal precioExistente = BigDecimal.ZERO;
        BigDecimal totalExistente = BigDecimal.ZERO;
        BigDecimal precioAcumulado = BigDecimal.ZERO;

        Integer cant = 0;
        Integer cantidadExistente = 0;
        if (!a) {
            /*realizando el caclulo anterio de s valores para poder realizar el calculo siguiente,
            uno depende del otro*/
            this.listaCalculoAnteriorInvemtario = kardexService.getlistaInventarioCalculoAnteior(kardex.getFechaDesde(), kardex.getItemsProducto(), estadoAdicionalInv);
            if (listaCalculoAnteriorInvemtario.size() > 0) {
                for (InventarioItems i : listaCalculoAnteriorInvemtario) {
                    if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                        iva = BigDecimal.ZERO;
                        cant = i.getCantidad();
                        if (i.getIva() != null) {
                            if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                precio = iva;
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                        total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);;
                        cantidadExistente = cant + cantidadExistente;
                        totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                        precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                    } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                        cant = i.getCantidad();
                        precio = precioExistente.setScale(4, RoundingMode.HALF_EVEN);
                        total = BigDecimal.valueOf(cant * precioExistente.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);

                        cantidadExistente = cantidadExistente - cant;
                        totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                        precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                    } else if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                        cant = i.getCantidad();
                        if (i.getIva() != null) {
                            if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                precio = iva;
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                        total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);
                        cantidadExistente = cantidadExistente + cant;
                        totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                        precioExistente = BigDecimal.valueOf(totalExistente.doubleValue() / cantidadExistente).setScale(4, RoundingMode.HALF_EVEN);

                    } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                        InventarioItems inb = new InventarioItems();
                        cant = i.getCantidad();
                        if (i.getIva() != null) {
                            if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                precio = iva;
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                        total = BigDecimal.valueOf(precio.doubleValue() * cant).setScale(4, RoundingMode.HALF_EVEN);
                        cantidadExistente = cantidadExistente - cant;
                        totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                        if (cantidadExistente > 0) {
                            precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                        }
                    }
                }
            }
            /*realizando el calculo actual que depende del calculo anterio para poder realizar lo mismo*/
            this.listaInventario = kardexService.getlistaInventario(kardex.getItemsProducto(), estadoAdicionalInv, kardex.getFechaDesde(), kardex.getFechaHasta());
            if (listaInventario.size() < 1) {
                PrimeFaces.current().ajax().update("messages");
                JsfUtil.addWarningMessage("Advertencia", "No hay datos en este rango de fecha " + new SimpleDateFormat("yyyy-MM-dd").format(kardex.getFechaDesde()) + " y " + new SimpleDateFormat("yyyy-MM-dd").format(kardex.getFechaHasta()));
                return;
            }
            for (InventarioItems i : listaInventario) {
                iva = BigDecimal.ZERO;
                detalleKardex = new DetallaKardex();
                if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                    cant = i.getCantidad();
                    if (i.getIva() != null) {
                        if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                            iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                            precio = iva;
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                    } else {
                        precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                    }
                    total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);;

                    cantidadExistente = cant + cantidadExistente;
                    totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_UP);
                    precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                    detalleKardex.setCantIngreso(cant);
                    detalleKardex.setPrecioIngreso(precio);
                    detalleKardex.setTotalIngreso(total);
                } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                    cant = i.getCantidad();
                    precio = precioExistente.setScale(4, RoundingMode.HALF_UP);
                    total = BigDecimal.valueOf(cant * precioExistente.doubleValue()).setScale(4, RoundingMode.HALF_UP);

                    cantidadExistente = cantidadExistente - cant;
                    totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_UP);

                    System.out.println("totalExistente: " + totalExistente + " cantidadExistente: " + cantidadExistente);
                    if (totalExistente.compareTo(BigDecimal.ZERO) != 0 && cantidadExistente != 0) {
                        precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                    } else {
                        precioExistente = BigDecimal.ZERO;
                    }
                    detalleKardex.setCantEgreso(cant);
                    detalleKardex.setPrecioEgreso(precio);
                    detalleKardex.setTotalEgreso(total);
                } else if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {

                    cant = i.getCantidad();
                    precio = precioExistente.setScale(4, RoundingMode.HALF_UP);
                    total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_UP);

                    cantidadExistente = cantidadExistente + cant;
                    totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_UP);
                    precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);

                    detalleKardex.setCantIngreso(cant);
                    detalleKardex.setPrecioIngreso(precio);
                    detalleKardex.setTotalIngreso(total);
                } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                    cant = i.getCantidad();
                    if (i.getIva() != null) {
                        if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                            iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                            precio = iva;
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                    } else {
                        precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                    }
                    total = BigDecimal.valueOf(precio.doubleValue() * cant).setScale(4, RoundingMode.HALF_EVEN);
                    cantidadExistente = cantidadExistente - cant;
                    totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                    if (cantidadExistente > 0) {
                        precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                    }
                    detalleKardex.setCantEgreso(cant);
                    detalleKardex.setPrecioEgreso(precio);
                    detalleKardex.setTotalEgreso(total);
                }
                detalleKardex.setCantExistencia(cantidadExistente);
                detalleKardex.setPrecioExistencia(precioExistente);
                detalleKardex.setTotalExistencia(totalExistente);
                detalleKardex.setInventarioItems(i);
                detalleKardex.setKardex(kardex);
                listaDetallaKardexs.add(detalleKardex);
                this.detalleKardex = new DetallaKardex();
            }
        } else {
            this.listaCalculoAnteriorInvemtario.clear();
            this.listaInventario.clear();
            this.listaCalculoAnteriorInvemtario = new ArrayList<>();
            listaDetallaKardexs.clear();
            this.listaInventario = new ArrayList<>();

            List<BigInteger> listaid = kardexService.getListaInventariosGeneralId(kardex.getFechaDesde(), kardex.getFechaHasta(), estadoAdicionalInv);
            for (BigInteger lis : listaid) {
                cant = 0;
                precio = BigDecimal.ZERO;
                total = BigDecimal.ZERO;
                precioExistente = BigDecimal.ZERO;
                totalExistente = BigDecimal.ZERO;
                precioAcumulado = BigDecimal.ZERO;
                cant = 0;
                cantidadExistente = 0;
                this.listaCalculoAnteriorInvemtario = kardexService.getlistaInventarioCalculoAnteior(kardex.getFechaDesde(), lis.longValue(), estadoAdicionalInv);
                if (listaCalculoAnteriorInvemtario.size() > 0) {
                    for (InventarioItems i : listaCalculoAnteriorInvemtario) {
                        if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                            cant = i.getCantidad();
                            if (i.getIva() != null) {
                                if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                    iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                    precio = iva;
                                } else {
                                    precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                                }
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                            total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);;
                            cantidadExistente = cant + cantidadExistente;
                            totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                            if (cantidadExistente > 0) {
                                precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                            }
                        } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                            cant = i.getCantidad();
                            precio = precioExistente.setScale(4, RoundingMode.HALF_EVEN);
                            total = BigDecimal.valueOf(cant * precioExistente.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);

                            cantidadExistente = cantidadExistente - cant;
                            totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                            if (cantidadExistente > 0) {
                                precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                            }

                        } else if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                            cant = i.getCantidad();
                            precio = precioExistente.setScale(4, RoundingMode.HALF_EVEN);
                            total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);

                            cantidadExistente = cantidadExistente + cant;
                            totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                            if (cantidadExistente > 0) {
                                precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                            }

                        } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                            InventarioItems inb = new InventarioItems();

                            cant = i.getCantidad();
                            if (i.getIva() != null) {
                                if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                    iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                    precio = iva;
                                } else {
                                    precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                                }
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                            total = BigDecimal.valueOf(precio.doubleValue() * cant).setScale(4, RoundingMode.HALF_EVEN);

                            cantidadExistente = cantidadExistente - cant;
                            totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                            if (cantidadExistente > 0) {
                                precioExistente = BigDecimal.valueOf(totalExistente.doubleValue() / cantidadExistente).setScale(4, RoundingMode.HALF_EVEN);
                            }
                        }
                    }
                }

                this.listaInventario = kardexService.getlistainbentariosPorItems(lis.longValue(), kardex.getFechaDesde(), kardex.getFechaHasta(), estadoAdicionalInv);
                for (InventarioItems i : listaInventario) {
                    detalleKardex = new DetallaKardex();
                    if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                        cant = i.getCantidad();
                        if (i.getIva() != null) {
                            if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                precio = iva;
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                        total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);;
                        cantidadExistente = cant + cantidadExistente;
                        totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                        if (cantidadExistente > 0) {
                            precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                        }
                        detalleKardex.setCantIngreso(cant);
                        detalleKardex.setPrecioIngreso(precio);
                        detalleKardex.setTotalIngreso(total);

                    } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() == null) {
                        cant = i.getCantidad();
                        precio = precioExistente.setScale(4, RoundingMode.HALF_EVEN);
                        total = BigDecimal.valueOf(cant * precioExistente.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);

                        cantidadExistente = cantidadExistente - cant;
                        totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                        if (cantidadExistente > 0) {
                            precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                        }
                        detalleKardex.setCantEgreso(cant);
                        detalleKardex.setPrecioEgreso(precio);
                        detalleKardex.setTotalEgreso(total);

                    } else if ("INGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                        cant = i.getCantidad();
                        precio = precioExistente.setScale(4, RoundingMode.HALF_EVEN);
                        total = BigDecimal.valueOf(cant * precio.doubleValue()).setScale(4, RoundingMode.HALF_EVEN);

                        cantidadExistente = cantidadExistente + cant;
                        totalExistente = totalExistente.add(total).setScale(4, RoundingMode.HALF_EVEN);
                        if (cantidadExistente > 0) {
                            precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                        }
                        detalleKardex.setCantIngreso(cant);
                        detalleKardex.setPrecioIngreso(precio);
                        detalleKardex.setTotalIngreso(total);

                    } else if ("EGRESO".equals(i.getInventario().getTipoMovimiento()) && i.getInventario().getIngresoEgresoRelacionado() != null) {
                        InventarioItems inb = new InventarioItems();

                        cant = i.getCantidad();
                        if (i.getIva() != null) {
                            if (i.getIva().compareTo(new BigDecimal("0")) == 1) {
                                iva = (i.getPrecio().multiply(i.getIva()).divide(new BigDecimal("100"))).add(i.getPrecio()).setScale(4, RoundingMode.HALF_EVEN);
                                precio = iva;
                            } else {
                                precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                            }
                        } else {
                            precio = i.getPrecio().setScale(4, RoundingMode.HALF_EVEN);
                        }
                        total = BigDecimal.valueOf(precio.doubleValue() * cant).setScale(4, RoundingMode.HALF_EVEN);

                        cantidadExistente = cantidadExistente - cant;
                        totalExistente = totalExistente.subtract(total).setScale(4, RoundingMode.HALF_EVEN);
                        if (cantidadExistente > 0) {
                            precioExistente = totalExistente.divide(BigDecimal.valueOf(cantidadExistente), MathContext.DECIMAL32).setScale(4, RoundingMode.HALF_UP);
                        }
                        detalleKardex.setCantEgreso(cant);
                        detalleKardex.setPrecioEgreso(precio);
                        detalleKardex.setTotalEgreso(total);
                    }
                    detalleKardex.setCantExistencia(cantidadExistente);
                    detalleKardex.setPrecioExistencia(precioExistente);
                    detalleKardex.setTotalExistencia(totalExistente);
                    detalleKardex.setInventarioItems(i);
                    detalleKardex.setKardex(kardex);
                    listaDetallaKardexs.add(detalleKardex);
                    this.detalleKardex = new DetallaKardex();
                }
            }
        }
    }

    public void imprimir() {
        if (this.reporte == null) {
            JsfUtil.addErrorMessage("Reporte", "No se puede generar reporte");
            return;
        }
        ss.borrarDatos();
        try {
            if (tipoReporteKrdex) {
                ss.addParametro("reporteG_desde", this.reporte.getFechaDesde());
                ss.addParametro("reporteG_hasta", this.reporte.getFechaHasta());
                ss.setNombreReporte("kardexGrupal");
                ss.setNombreSubCarpeta("activos");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            } else {
                ss.addParametro("item", this.reporte.getItemsProducto().getId());
                ss.addParametro("desde", this.reporte.getFechaDesde());
                ss.addParametro("hasta", this.reporte.getFechaHasta());
                ss.addParametro("idKardex", kardex.getId());
                ss.setNombreReporte("kardexArticulo");
                ss.setNombreSubCarpeta("activos");
                JsfUtil.redirectNewTab(CONFIG.URL_APP + "Documento");
            }
        } catch (Exception e) {
        }
    }

//<editor-fold defaultstate="collapsed" desc="Setter and Getter">
    public Kardex getKardex() {
        return kardex;
    }

    public void setKardex(Kardex kardex) {
        this.kardex = kardex;
    }

    public boolean isDataOcultar() {
        return dataOcultar;
    }

    public void setDataOcultar(boolean dataOcultar) {
        this.dataOcultar = dataOcultar;
    }

    public Distributivo getDistributivo() {
        return distributivo;
    }

    public void setDistributivo(Distributivo distributivo) {
        this.distributivo = distributivo;
    }

    public List<InventarioItems> getListaInventario() {
        return listaInventario;
    }

    public void setListaInventario(List<InventarioItems> listaInventario) {
        this.listaInventario = listaInventario;
    }

    public DetallaKardex getDetalleKardex() {
        return detalleKardex;
    }

    public void setDetalleKardex(DetallaKardex detalleKardex) {
        this.detalleKardex = detalleKardex;
    }

    public List<DetallaKardex> getListaDetallaKardexs() {
        return listaDetallaKardexs;
    }

    public void setListaDetallaKardexs(List<DetallaKardex> listaDetallaKardexs) {
        this.listaDetallaKardexs = listaDetallaKardexs;
    }

    public List<DetalleItem> getListaItems() {
        return listaItems;
    }

    public void setListaItems(List<DetalleItem> listaItems) {
        this.listaItems = listaItems;
    }

    public List<DetallaKardex> getListavistaDetalleKardex() {
        return listavistaDetalleKardex;
    }

    public void setListavistaDetalleKardex(List<DetallaKardex> listavistaDetalleKardex) {
        this.listavistaDetalleKardex = listavistaDetalleKardex;
    }

    public Short getFila() {
        return fila;
    }

    public void setFila(Short fila) {
        this.fila = fila;
    }

    public Short getColumna() {
        return columna;
    }

    public void setColumna(Short columna) {
        this.columna = columna;
    }

    public Short getEstnate() {
        return estnate;
    }

    public void setEstnate(Short estnate) {
        this.estnate = estnate;
    }

    public Short getCajon() {
        return cajon;
    }

    public void setCajon(Short cajon) {
        this.cajon = cajon;
    }

    public Short getCuadrante() {
        return cuadrante;
    }

    public void setCuadrante(Short cuadrante) {
        this.cuadrante = cuadrante;
    }

    public boolean isMostrarDetalleIemsArticulos() {
        return mostrarDetalleIemsArticulos;
    }

    public void setMostrarDetalleIemsArticulos(boolean mostrarDetalleIemsArticulos) {
        this.mostrarDetalleIemsArticulos = mostrarDetalleIemsArticulos;
    }

    public boolean isTipoReporteKrdex() {
        return tipoReporteKrdex;
    }

    public void setTipoReporteKrdex(boolean tipoReporteKrdex) {
        this.tipoReporteKrdex = tipoReporteKrdex;
    }

    public List<InventarioItems> getListaCalculoAnteriorInvemtario() {
        return listaCalculoAnteriorInvemtario;
    }

    public void setListaCalculoAnteriorInvemtario(List<InventarioItems> listaCalculoAnteriorInvemtario) {
        this.listaCalculoAnteriorInvemtario = listaCalculoAnteriorInvemtario;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public LazyModel<DetallaKardex> getLazyKardex() {
        return lazyKardex;
    }

    public void setLazyKardex(LazyModel<DetallaKardex> lazyKardex) {
        this.lazyKardex = lazyKardex;
    }

//</editor-fold>
}
