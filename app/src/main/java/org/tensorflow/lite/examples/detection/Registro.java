package org.tensorflow.lite.examples.detection;

import java.util.Date;

public class Registro {

    int cantidad,estimado_centro_abastos,estimado_marcados_centro,estimado_san_gil,estimado_socorro;
    Date fecha;

    public Registro(){}

    public Registro(int cantidad, int estimado_centro_abastos, int estimado_marcados_centro, int estimado_san_gil, int estimado_socorro, Date fecha) {
        this.cantidad = cantidad;
        this.estimado_centro_abastos = estimado_centro_abastos;
        this.estimado_marcados_centro = estimado_marcados_centro;
        this.estimado_san_gil = estimado_san_gil;
        this.estimado_socorro = estimado_socorro;
        this.fecha = fecha;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getEstimado_centro_abastos() {
        return estimado_centro_abastos;
    }

    public void setEstimado_centro_abastos(int estimado_centro_abastos) {
        this.estimado_centro_abastos = estimado_centro_abastos;
    }

    public int getEstimado_marcados_centro() {
        return estimado_marcados_centro;
    }

    public void setEstimado_marcados_centro(int estimado_marcados_centro) {
        this.estimado_marcados_centro = estimado_marcados_centro;
    }

    public int getEstimado_san_gil() {
        return estimado_san_gil;
    }

    public void setEstimado_san_gil(int estimado_san_gil) {
        this.estimado_san_gil = estimado_san_gil;
    }

    public int getEstimado_socorro() {
        return estimado_socorro;
    }

    public void setEstimado_socorro(int estimado_socorro) {
        this.estimado_socorro = estimado_socorro;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

}
