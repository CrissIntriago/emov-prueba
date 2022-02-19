/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ec.com.trafficcontrol;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author ANGEL NAVARRO
 */
public class ResultWsdl implements Serializable {

    private ResultData vehiculo;

    public ResultWsdl() {
    }

    public ResultData getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(ResultData vehiculo) {
        this.vehiculo = vehiculo;
    }

    @Override
    public String toString() {
        return "ResultWsdl{" + "vehiculo=" + vehiculo + '}';
    }

    public static class ResultData implements Serializable {

        private String codigoerror;
        private String mensajeerror;
        private Vehiculo datos;

        public ResultData() {
        }

        public String getCodigoerror() {
            return codigoerror;
        }

        public void setCodigoerror(String codigoerror) {
            this.codigoerror = codigoerror;
        }

        public String getMensajeerror() {
            return mensajeerror;
        }

        public void setMensajeerror(String mensajeerror) {
            this.mensajeerror = mensajeerror;
        }

        public Vehiculo getDatos() {
            return datos;
        }

        public void setDatos(Vehiculo datos) {
            this.datos = datos;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 73 * hash + Objects.hashCode(this.codigoerror);
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final ResultData other = (ResultData) obj;
            if (!Objects.equals(this.codigoerror, other.codigoerror)) {
                return false;
            }
            return true;
        }

        @Override
        public String toString() {
            return "ResultData{" + "codigoerror=" + codigoerror + ", mensajeerror=" + mensajeerror + ", datos=" + datos + '}';
        }

        public static class Vehiculo {

            private String placa;
            private String placaactual;
            private Integer anio;
            private String identificacion;
            private String nombrepersona;
            private String chasis;
            private String motor;
            private String cilindraje;
            private String disco;
            private String numpasajero;
            private String color;
            private String marca;
            private String modelo;
            private String codigoclasevehiculo;
            private String clasevehiculo;
            private String codigotipovehiculo;
            private String tipovehiculo;
            private String numeroejes;
            private String pesotonelaje;
            private String rtvvigente;

            public Vehiculo() {
            }

            public String getPlaca() {
                return placa;
            }

            public void setPlaca(String placa) {
                this.placa = placa;
            }

            public String getPlacaactual() {
                return placaactual;
            }

            public void setPlacaactual(String placaactual) {
                this.placaactual = placaactual;
            }

            public Integer getAnio() {
                return anio;
            }

            public void setAnio(Integer anio) {
                this.anio = anio;
            }

            public String getIdentificacion() {
                return identificacion;
            }

            public void setIdentificacion(String identificacion) {
                this.identificacion = identificacion;
            }

            public String getNombrepersona() {
                return nombrepersona;
            }

            public void setNombrepersona(String nombrepersona) {
                this.nombrepersona = nombrepersona;
            }

            public String getChasis() {
                return chasis;
            }

            public void setChasis(String chasis) {
                this.chasis = chasis;
            }

            public String getMotor() {
                return motor;
            }

            public void setMotor(String motor) {
                this.motor = motor;
            }

            public String getCilindraje() {
                return cilindraje;
            }

            public void setCilindraje(String cilindraje) {
                this.cilindraje = cilindraje;
            }

            public String getDisco() {
                return disco;
            }

            public void setDisco(String disco) {
                this.disco = disco;
            }

            public String getNumpasajero() {
                return numpasajero;
            }

            public void setNumpasajero(String numpasajero) {
                this.numpasajero = numpasajero;
            }

            public String getColor() {
                return color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getMarca() {
                return marca;
            }

            public void setMarca(String marca) {
                this.marca = marca;
            }

            public String getModelo() {
                return modelo;
            }

            public void setModelo(String modelo) {
                this.modelo = modelo;
            }

            public String getCodigoclasevehiculo() {
                return codigoclasevehiculo;
            }

            public void setCodigoclasevehiculo(String codigoclasevehiculo) {
                this.codigoclasevehiculo = codigoclasevehiculo;
            }

            public String getClasevehiculo() {
                return clasevehiculo;
            }

            public void setClasevehiculo(String clasevehiculo) {
                this.clasevehiculo = clasevehiculo;
            }

            public String getCodigotipovehiculo() {
                return codigotipovehiculo;
            }

            public void setCodigotipovehiculo(String codigotipovehiculo) {
                this.codigotipovehiculo = codigotipovehiculo;
            }

            public String getTipovehiculo() {
                return tipovehiculo;
            }

            public void setTipovehiculo(String tipovehiculo) {
                this.tipovehiculo = tipovehiculo;
            }

            public String getNumeroejes() {
                return numeroejes;
            }

            public void setNumeroejes(String numeroejes) {
                this.numeroejes = numeroejes;
            }

            public String getPesotonelaje() {
                return pesotonelaje;
            }

            public void setPesotonelaje(String pesotonelaje) {
                this.pesotonelaje = pesotonelaje;
            }

            public String getRtvvigente() {
                return rtvvigente;
            }

            public void setRtvvigente(String rtvvigente) {
                this.rtvvigente = rtvvigente;
            }

            @Override
            public String toString() {
                return "Vehiculo{" + "placa=" + placa + ", placaactual=" + placaactual + ", anio=" + anio + ", identificacion=" + identificacion + ", nombrepersona=" + nombrepersona + ", chasis=" + chasis + ", motor=" + motor + ", cilindraje=" + cilindraje + ", disco=" + disco + ", numpasajero=" + numpasajero + ", color=" + color + ", marca=" + marca + ", modelo=" + modelo + ", codigoclasevehiculo=" + codigoclasevehiculo + ", clasevehiculo=" + clasevehiculo + ", codigotipovehiculo=" + codigotipovehiculo + ", tipovehiculo=" + tipovehiculo + ", numeroejes=" + numeroejes + ", pesotonelaje=" + pesotonelaje + ", rtvvigente=" + rtvvigente + '}';
            }

            @Override
            public int hashCode() {
                int hash = 7;
                hash = 59 * hash + Objects.hashCode(this.placa);
                hash = 59 * hash + Objects.hashCode(this.chasis);
                return hash;
            }

            @Override
            public boolean equals(Object obj) {
                if (this == obj) {
                    return true;
                }
                if (obj == null) {
                    return false;
                }
                if (getClass() != obj.getClass()) {
                    return false;
                }
                final Vehiculo other = (Vehiculo) obj;
                if (!Objects.equals(this.placa, other.placa)) {
                    return false;
                }
                if (!Objects.equals(this.chasis, other.chasis)) {
                    return false;
                }
                return true;
            }

        }

    }
}
