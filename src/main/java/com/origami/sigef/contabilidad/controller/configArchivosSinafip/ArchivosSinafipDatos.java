/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.origami.sigef.contabilidad.controller.configArchivosSinafip;

import com.origami.sigef.common.entities.CatalogoItem;
import com.origami.sigef.common.entities.CuentaContable;
import com.origami.sigef.common.entities.DetalleTransaccion;
import com.origami.sigef.common.entities.DiarioGeneral;
import com.origami.sigef.contabilidad.model.EstructuraArchivosPlanos;
import com.origami.sigef.contabilidad.service.CuentaContableService;
import com.origami.sigef.contabilidad.service.DetalleTransaccionService;
import com.origami.sigef.resource.conf.entities.PlanCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContCuentas;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneral;
import com.origami.sigef.resource.contabilidad.entities.ContDiarioGeneralDetalle;
import com.origami.sigef.resource.contabilidad.entities.ContSaldoInicial;
import com.origami.sigef.resource.contabilidad.services.ContCuentasService;
import com.origami.sigef.resource.contabilidad.services.ContDiarioGeneralDetalleService;
import com.origami.sigef.resource.contabilidad.services.ContSaldoInicialService;
import ir.cafebabe.math.utils.BigDecimalUtils;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.inject.Inject;

/**
 *
 * @author ORIGAMI
 */
public class ArchivosSinafipDatos implements Serializable {

    @Inject
//    private CuentaContableService cuentaContableService;
    private ContCuentasService cuentaContableService;
    @Inject
//    private DetalleTransaccionService detalleTransaccionService;
    private ContDiarioGeneralDetalleService detalleTransaccionService;
    @Inject
    private ContSaldoInicialService contSaldoInicialService;

    private final DecimalFormat formatInt = new DecimalFormat("00");

    public ContDiarioGeneralDetalle consultaHijasByDiarioGeneral(List<ContCuentas> hijas, ContDiarioGeneral diarioApertura) {
        ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
        if (!hijas.isEmpty()) {
            detalle.setDebe(BigDecimal.ZERO);
            detalle.setHaber(BigDecimal.ZERO);
            for (ContCuentas hija : hijas) {
                ContDiarioGeneralDetalle hijaDetalleDB = detalleTransaccionService.findByPadreDiarioGeneral(diarioApertura.getId(), hija.getId());
                if (hijaDetalleDB.getDebe() != null) {
                    detalle.setDebe(detalle.getDebe().add(hijaDetalleDB.getDebe()));
                    detalle.setHaber(detalle.getHaber().add(hijaDetalleDB.getHaber()));
                }
            }
        }
        return detalle;
    }

    public ContDiarioGeneralDetalle consultaHijasExceptoDiarioApertura(List<ContCuentas> hijas, DiarioGeneral diarioApertura,
            String fechaDesde, String fechaHasta, CatalogoItem tipoCierreItem) {
        ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
        if (!hijas.isEmpty()) {
            detalle.setDebe(BigDecimal.ZERO);
            detalle.setHaber(BigDecimal.ZERO);
            for (ContCuentas hija : hijas) {
                ContDiarioGeneralDetalle hijaDetalleDB = detalleTransaccionService.findByPadreDetalleTransaccionExceptoDiarioApertura(
                        fechaDesde, fechaHasta, diarioApertura.getId(), hija.getId(), tipoCierreItem.getId());
                if (hijaDetalleDB.getDebe() != null) {
                    detalle.setDebe(detalle.getDebe().add(hijaDetalleDB.getDebe()));
                    detalle.setHaber(detalle.getHaber().add(hijaDetalleDB.getHaber()));
                }
            }
        }
        return detalle;
    }

    public ContDiarioGeneralDetalle consultaHijasNoExisteDiarioApertura(List<ContCuentas> hijas, String fechaDesde, String fechaHasta,
            CatalogoItem tipoCierreItem) {
        ContDiarioGeneralDetalle detalle = new ContDiarioGeneralDetalle();
        if (!hijas.isEmpty()) {
            detalle.setDebe(BigDecimal.ZERO);
            detalle.setHaber(BigDecimal.ZERO);
            for (ContCuentas hija : hijas) {
                ContDiarioGeneralDetalle hijaDetalleDB = detalleTransaccionService.findByPadreDetalleTransaccionNoExisteDiarioApertura(
                        fechaDesde, fechaHasta, hija.getId(), tipoCierreItem.getId());
                if (hijaDetalleDB.getDebe() != null) {
                    detalle.setDebe(detalle.getDebe().add(hijaDetalleDB.getDebe()));
                    detalle.setHaber(detalle.getHaber().add(hijaDetalleDB.getHaber()));
                }
            }
        }
        return detalle;
    }

//    public ContCuentas consultaHijasNoExisteDiarioAperturaByComprobanteMensual(List<ContCuentas> hijas) {
//        ContCuentas cuenta = new ContCuentas();
//        if (!hijas.isEmpty()) {
//            cuenta.setSaldoInicialDebe(BigDecimal.ZERO);
//            cuenta.setSaldoInicialHaber(BigDecimal.ZERO);
//            for (ContCuentas hija : hijas) {
//                CuentaContable cuentaDB = detalleTransaccionService.findCuentaContableHijasDebeAndHaber(hija.getId());
//                if (cuentaDB.getSaldoInicialDebe() != null && cuentaDB.getSaldoInicialHaber() != null) {
//                    cuenta.setSaldoInicialDebe(cuenta.getSaldoInicialDebe().add(cuentaDB.getSaldoInicialDebe()));
//                    cuenta.setSaldoInicialHaber(cuenta.getSaldoInicialHaber().add(cuentaDB.getSaldoInicialHaber()));
//                }
//            }
//        }
//        return cuenta;
//    }
    public Map<String, BigDecimal> consultaHijasNoExisteDiarioAperturaByComprobanteMensualHas(List<ContCuentas> hijas, Short anio) {
        Map<String, BigDecimal> saldosIniciales = new HashMap<>();
        BigDecimal inicialDebe = BigDecimal.ZERO, inicialHaber = BigDecimal.ZERO;
        if (!hijas.isEmpty()) {
            for (ContCuentas hija : hijas) {
                ContSaldoInicial cuentaDB = contSaldoInicialService.findContSaldoInicialByIdCuentaAndPeriodo(hija, anio);

//                CuentaContable cuentaDB = detalleTransaccionService.findCuentaContableHijasDebeAndHaber(hija.getId());
                if (cuentaDB.getSaldoDebe() != null && cuentaDB.getSaldoHaber() != null) {
                    inicialDebe = inicialDebe.add(cuentaDB.getSaldoDebe());
                    inicialHaber = inicialHaber.add(cuentaDB.getSaldoHaber());
                }
            }
        }
        saldosIniciales.put("inicialDebe", inicialDebe);
        saldosIniciales.put("inicialHaber", inicialHaber);
        return saldosIniciales;
    }

    public List<ContDiarioGeneralDetalle> unirListaSemejantesDetalleTransaccion(List<ContDiarioGeneralDetalle> listDetalleTransaccionesReciprocas) {
        List<ContDiarioGeneralDetalle> newList = listDetalleTransaccionesReciprocas;
        int i;
        int x;
        for (i = 0; i < newList.size(); i++) {
            for (x = 0; x < newList.size(); x++) {
                if (!Objects.equals(newList.get(x).getId(), newList.get(i).getId())) {
                    if (newList.get(x).getIdContCuentas().equals(newList.get(i).getIdContCuentas())
                            && newList.get(x).getIdContDiarioGeneral().getOtorgante().equals(newList.get(i).getIdContDiarioGeneral().getOtorgante())
                            && newList.get(x).getIdContDiarioGeneral().getReceptor().equals(newList.get(i).getIdContDiarioGeneral().getReceptor())) {
                        newList.get(i).setDebe(newList.get(i).getDebe().add(newList.get(x).getDebe()));
                        newList.get(i).setHaber(newList.get(i).getHaber().add(newList.get(x).getHaber()));
                        newList.remove(x);
                    }
                }
            }
        }
        return newList;
    }

    public EstructuraArchivosPlanos returnEstructuraAcrchivoDiarioApertura(ContDiarioGeneralDetalle padreDetalleDB,
            ContDiarioGeneralDetalle detalleCuentaHija, ContCuentas padre) {

        if (padreDetalleDB.getDebe() != null && detalleCuentaHija.getDebe() == null) {
            return setEstructuraBalanceInicial(padre, padreDetalleDB);
        } else if (padreDetalleDB.getDebe() == null && detalleCuentaHija.getDebe() != null) {
            return setEstructuraBalanceInicial(padre, detalleCuentaHija);
        } else if (padreDetalleDB.getDebe() != null && detalleCuentaHija.getDebe() != null) {
            padreDetalleDB.setDebe(padreDetalleDB.getDebe().add(detalleCuentaHija.getDebe()));
            padreDetalleDB.setHaber(padreDetalleDB.getHaber().add(detalleCuentaHija.getHaber()));
            return setEstructuraBalanceInicial(padre, padreDetalleDB);
        }
        if (padreDetalleDB.getDebe() == null && detalleCuentaHija.getDebe() == null) {
            padreDetalleDB.setDebe(BigDecimal.ZERO);
            padreDetalleDB.setHaber(BigDecimal.ZERO);
            return setEstructuraBalanceInicial(padre, padreDetalleDB);
        }
        return null;
    }

    public EstructuraArchivosPlanos setEstructuraBalanceInicial(ContCuentas padre, ContDiarioGeneralDetalle d) {
        Map<String, String> codigoSeparado = getNivelesCuenta(padre);
        EstructuraArchivosPlanos plano = new EstructuraArchivosPlanos("01",
                codigoSeparado.get("cuentaMayor"),
                codigoSeparado.get("nivel1"),
                codigoSeparado.get("nivel2"),
                d.getDebe().doubleValue(), d.getHaber().doubleValue());
        return plano;
    }

    public EstructuraArchivosPlanos setEstructuraBalanceInicialCuentaContable(ContCuentas padre, Short anio, Double saldoInicialDebe, Double saldoInicialHaber) {
        Map<String, String> codigoSeparado = getNivelesCuenta(padre);
//        ContSaldoInicial saldoInicial = contSaldoInicialService.findContSaldoInicialByIdCuentaAndPeriodo(padre, anio);
        EstructuraArchivosPlanos plano = new EstructuraArchivosPlanos("01",
                codigoSeparado.get("cuentaMayor"),
                codigoSeparado.get("nivel1"),
                codigoSeparado.get("nivel2"),
                saldoInicialDebe, saldoInicialHaber);
        return plano;
    }

//    public EstructuraArchivosPlanos setEstructuraBalanceInicialCuentaContableSinDiarioApertura(ContCuentas padre, Short anio, Double saldoInicialDebe, Double saldoInicialHaber) {
//        EstructuraArchivosPlanos plano = new EstructuraArchivosPlanos("01",
//                codigoIsNull(padre.getTitulo()) + codigoIsNull(padre.getGrupo()) + codigoIsNull(padre.getSubGrupo()),
//                completarCeros(padre.getCuentaNivel1()),
//                completarCeros(padre.getCuentaNivel2()), saldoInicialDebe, saldoInicialHaber);
//        return plano;
//    }
    public EstructuraArchivosPlanos setEstructuraBalanceComprobacionMensual(EstructuraArchivosPlanos balanceInicial, ContDiarioGeneralDetalle flujo, Integer mes) {
        flujo.setSumaAcuDeudor(flujo.getDebe().add(new BigDecimal(balanceInicial.getSaldoInicialDeudor())));
        flujo.setSumaAcuAcreedor(flujo.getHaber().add(new BigDecimal(balanceInicial.getSaldoInicialAcreedor())));

        flujo.setSaldoFinalDeudor(flujo.getSumaAcuDeudor().subtract(flujo.getSumaAcuAcreedor()));
        flujo.setSaldoFinalAcreedor(BigDecimal.ZERO);

        if (BigDecimalUtils.is(flujo.getSaldoFinalDeudor()).isNegative()) {
            flujo.setSaldoFinalAcreedor(flujo.getSaldoFinalDeudor().abs());
            flujo.setSaldoFinalDeudor(BigDecimal.ZERO);
        }

        EstructuraArchivosPlanos plano = new EstructuraArchivosPlanos(formatInt.format(mes),
                balanceInicial.getCuentaMayor(),
                balanceInicial.getCuentaNivel1(),
                balanceInicial.getCuentaNivel2(),
                balanceInicial.getSaldoInicialDeudor(), balanceInicial.getSaldoInicialAcreedor(),
                flujo.getDebe().doubleValue(), flujo.getHaber().doubleValue(), flujo.getSumaAcuDeudor().doubleValue(),
                flujo.getSumaAcuAcreedor().doubleValue(), flujo.getSaldoFinalDeudor().doubleValue(),
                flujo.getSaldoFinalAcreedor().doubleValue());
        return plano;
    }

    public EstructuraArchivosPlanos setEstructuraTransaccionesReciprocas(ContCuentas padre, ContDiarioGeneralDetalle d, Integer mes) {
        Map<String, String> codigoSeparado = getNivelesCuenta(padre);
        EstructuraArchivosPlanos plano = new EstructuraArchivosPlanos(formatInt.format(mes), codigoSeparado.get("cuentaMayor"),
                codigoSeparado.get("nivel1"),
                codigoSeparado.get("nivel2"),
                d.getIdContDiarioGeneral().getReceptor().getIdentificacionCompleta(),
                d.getIdContDiarioGeneral().getOtorgante().getIdentificacionCompleta(),
                d.getDebe().doubleValue(), d.getHaber().doubleValue(),
                d.getCuentaMonetaria());
        return plano;
    }

    /*
     * Se quemaron los 5 niveles para el reporte sinafig
     */
    public Map<String, String> getNivelesCuenta(ContCuentas cuenta) {
        Map<String, String> result = new HashMap<>();
        String titulo, grupo, subgrupo, nivel1, nivel2;

        titulo = cuenta.getCodigo().length() >= 1 ? cuenta.getCodigo().substring(0, 1) : "00";
        grupo = cuenta.getCodigo().length() >= 2 ? cuenta.getCodigo().substring(1, 2) : "00";
        subgrupo = cuenta.getCodigo().length() >= 3 ? cuenta.getCodigo().substring(2, 3) : "00";
        nivel1 = cuenta.getCodigo().length() >= 5 ? cuenta.getCodigo().substring(3, 5) : "00";
        nivel2 = cuenta.getCodigo().length() >= 7 ? cuenta.getCodigo().substring(5, 7) : "00";

        result.put("cuentaMayor", titulo + grupo + subgrupo);
        result.put("nivel1", nivel1);
        result.put("nivel2", nivel2);
        return result;

    }

    public EstructuraArchivosPlanos setEstructuraTransaccionesReciprocasEmpty(CatalogoItem item, Integer mes) {
        EstructuraArchivosPlanos plano = new EstructuraArchivosPlanos(formatInt.format(mes),
                item.getCodigo().substring(0, 3), item.getCodigo().substring(3, 5), "00",
                "9999999999996", "9999999999996", new Double(0), new Double(0), 0);
        return plano;
    }

//    private String codigoIsNull(Short c) {
//        if (c == null) {
//            return "00";
//        }
//        return "" + c;
//    }
//
//    private String completarCeros(Short c) {
//        if (c == null) {
//            return "00";
//        }
//        if (c.toString().length() == 1) {
//            return "0" + c;
//        }
//        return "" + c;
//    }
    public List<ContCuentas> returnDetalleCuentaHijas(ContCuentas padre) {
        List<ContCuentas> hijasCuentaContable = new ArrayList<>();

//        if (!padre.getMovimiento() && padre.getNivel().getOrden() == 1) {
        if (!padre.getMovimiento() && padre.getConfId().getNivel() == 1) {
            List<ContCuentas> hijasNivel2 = cuentaContableService.getHijosByPadre(padre);
            if (!hijasNivel2.isEmpty()) {
                for (ContCuentas hija2 : hijasNivel2) {
                    if (!hija2.getGobierno()) {
                        if (!hija2.getMovimiento()) {
                            List<ContCuentas> hijasNivel3 = cuentaContableService.getHijosByPadre(hija2);
                            if (!hijasNivel3.isEmpty()) {
                                for (ContCuentas hija3 : hijasNivel3) {
                                    if (!hija3.getGobierno()) {
                                        if (!hija3.getMovimiento()) {
                                            List<ContCuentas> hijasNivel4 = cuentaContableService.getHijosByPadre(hija3);
                                            if (!hijasNivel4.isEmpty()) {
                                                for (ContCuentas hija4 : hijasNivel4) {
                                                    if (!hija4.getGobierno()) {
                                                        if (!hija4.getMovimiento()) {
                                                            List<ContCuentas> hijasNivel5 = cuentaContableService.getHijosByPadre(hija4);
                                                            if (!hijasNivel5.isEmpty()) {
                                                                for (ContCuentas hija5 : hijasNivel5) {
                                                                    if (!hija5.getGobierno()) {
                                                                        if (!hija5.getMovimiento()) {
                                                                            List<ContCuentas> hijasNivel6 = cuentaContableService.getHijosByPadre(hija5);
                                                                            if (!hijasNivel6.isEmpty()) {
                                                                                for (ContCuentas hija6 : hijasNivel6) {
                                                                                    if (!hija6.getGobierno()) {
                                                                                        if (!hija6.getMovimiento()) {
                                                                                            List<ContCuentas> hijasNivel7 = cuentaContableService.getHijosByPadre(hija6);
                                                                                            if (!hijasNivel7.isEmpty()) {
                                                                                                for (ContCuentas hija7 : hijasNivel7) {
                                                                                                    if (!hija7.getGobierno()) {
                                                                                                        hijasCuentaContable.add(hija7);
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        hijasCuentaContable.add(hija6);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        hijasCuentaContable.add(hija5);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        hijasCuentaContable.add(hija4);
                                                    }
                                                }
                                            }
                                        }
                                        hijasCuentaContable.add(hija3);
                                    }
                                }
                            }
                        }
                        hijasCuentaContable.add(hija2);
                    }
                }
            }
        } else if (!padre.getMovimiento() && padre.getConfId().getNivel() == 2) {
            List<ContCuentas> hijasNivel3 = cuentaContableService.getHijosByPadre(padre);
            if (!hijasNivel3.isEmpty()) {
                for (ContCuentas hija3 : hijasNivel3) {
                    if (!hija3.getGobierno()) {
                        if (!hija3.getMovimiento()) {
                            List<ContCuentas> hijasNivel4 = cuentaContableService.getHijosByPadre(hija3);
                            if (!hijasNivel4.isEmpty()) {
                                for (ContCuentas hija4 : hijasNivel4) {
                                    if (!hija4.getGobierno()) {
                                        if (!hija4.getMovimiento()) {
                                            List<ContCuentas> hijasNivel5 = cuentaContableService.getHijosByPadre(hija4);
                                            if (!hijasNivel5.isEmpty()) {
                                                for (ContCuentas hija5 : hijasNivel5) {
                                                    if (!hija5.getGobierno()) {
                                                        if (!hija5.getMovimiento()) {
                                                            List<ContCuentas> hijasNivel6 = cuentaContableService.getHijosByPadre(hija5);
                                                            if (!hijasNivel6.isEmpty()) {
                                                                for (ContCuentas hija6 : hijasNivel6) {
                                                                    if (!hija6.getGobierno()) {
                                                                        if (!hija6.getMovimiento()) {
                                                                            List<ContCuentas> hijasNivel7 = cuentaContableService.getHijosByPadre(hija6);
                                                                            if (!hijasNivel7.isEmpty()) {
                                                                                for (ContCuentas hija7 : hijasNivel7) {
                                                                                    if (!hija7.getGobierno()) {
                                                                                        hijasCuentaContable.add(hija7);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                        hijasCuentaContable.add(hija6);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        hijasCuentaContable.add(hija5);
                                                    }
                                                }
                                            }
                                        }
                                        hijasCuentaContable.add(hija4);
                                    }
                                }
                            }
                        }
                        hijasCuentaContable.add(hija3);
                    }
                }
            }
        } else if (!padre.getMovimiento() && padre.getConfId().getNivel() == 3) {
            List<ContCuentas> hijasNivel4 = cuentaContableService.getHijosByPadre(padre);
            if (!hijasNivel4.isEmpty()) {
                for (ContCuentas hija4 : hijasNivel4) {
                    if (!hija4.getGobierno()) {
                        if (!hija4.getMovimiento()) {
                            List<ContCuentas> hijasNivel5 = cuentaContableService.getHijosByPadre(hija4);
                            if (!hijasNivel5.isEmpty()) {
                                for (ContCuentas hija5 : hijasNivel5) {
                                    if (!hija5.getGobierno()) {
                                        if (!hija5.getMovimiento()) {
                                            List<ContCuentas> hijasNivel6 = cuentaContableService.getHijosByPadre(hija5);
                                            if (!hijasNivel6.isEmpty()) {
                                                for (ContCuentas hija6 : hijasNivel6) {
                                                    if (!hija6.getGobierno()) {
                                                        if (!hija6.getMovimiento()) {
                                                            List<ContCuentas> hijasNivel7 = cuentaContableService.getHijosByPadre(hija6);
                                                            if (!hijasNivel7.isEmpty()) {
                                                                for (ContCuentas hija7 : hijasNivel7) {
                                                                    if (!hija7.getGobierno()) {
                                                                        hijasCuentaContable.add(hija7);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                        hijasCuentaContable.add(hija6);
                                                    }
                                                }
                                            }
                                        }
                                        hijasCuentaContable.add(hija5);
                                    }
                                }
                            }
                        }
                        hijasCuentaContable.add(hija4);
                    }
                }
            }
        } else if (!padre.getMovimiento() && padre.getConfId().getNivel() == 4) {
            List<ContCuentas> hijasNivel5 = cuentaContableService.getHijosByPadre(padre);
            if (!hijasNivel5.isEmpty()) {
                for (ContCuentas hija5 : hijasNivel5) {
                    if (!hija5.getGobierno()) {
                        if (!hija5.getMovimiento()) {
                            List<ContCuentas> hijasNivel6 = cuentaContableService.getHijosByPadre(hija5);
                            if (!hijasNivel6.isEmpty()) {
                                for (ContCuentas hija6 : hijasNivel6) {
                                    if (!hija6.getGobierno()) {
                                        if (!hija6.getMovimiento()) {
                                            List<ContCuentas> hijasNivel7 = cuentaContableService.getHijosByPadre(hija6);
                                            if (!hijasNivel7.isEmpty()) {
                                                for (ContCuentas hija7 : hijasNivel7) {
                                                    if (!hija7.getGobierno()) {
                                                        hijasCuentaContable.add(hija7);
                                                    }
                                                }
                                            }
                                        }
                                        hijasCuentaContable.add(hija6);
                                    }
                                }
                            }
                        }
                        hijasCuentaContable.add(hija5);
                    }
                }
            }
        } else if (!padre.getMovimiento() && padre.getConfId().getNivel() == 5) {
            List<ContCuentas> hijasNivel6 = cuentaContableService.getHijosByPadre(padre);
            if (!hijasNivel6.isEmpty()) {
                for (ContCuentas hija6 : hijasNivel6) {
                    if (!hija6.getGobierno()) {
                        if (!hija6.getMovimiento()) {
                            List<ContCuentas> hijasNivel7 = cuentaContableService.getHijosByPadre(hija6);
                            if (!hijasNivel7.isEmpty()) {
                                for (ContCuentas hija7 : hijasNivel7) {
                                    if (!hija7.getGobierno()) {
                                        hijasCuentaContable.add(hija7);
                                    }
                                }
                            }
                        }
                        hijasCuentaContable.add(hija6);
                    }
                }
            }
        } else if (!padre.getMovimiento() && padre.getConfId().getNivel() == 6) {
            List<ContCuentas> hijasNivel7 = cuentaContableService.getHijosByPadre(padre);
            if (!hijasNivel7.isEmpty()) {
                for (ContCuentas hija7 : hijasNivel7) {
                    if (!hija7.getGobierno()) {
                        hijasCuentaContable.add(hija7);
                    }
                }
            }
        }
        return hijasCuentaContable;
    }
}
