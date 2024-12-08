package com.practica.controller.dao.services;

import com.practica.controller.dao.ProyectoDao;
import com.practica.controller.model.Proyecto;
import com.practica.controller.tda.list.LinkedList;

public class ProyectoServices {

    private final ProyectoDao obj;

    public ProyectoServices() {
        obj = new ProyectoDao();
    }

    public Proyecto getProyecto() {
        return obj.getProyecto();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Proyecto> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setProyecto(Proyecto proyecto) {
        obj.setProyecto(proyecto);
    }

    public Proyecto getProyectoById(Integer id) throws Exception {
        return obj.getProyectoById(id);

    }

    public String toJson() throws Exception {
        return obj.toJson();

    }

    public LinkedList<Proyecto> getProyectosBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Proyecto> orderListBy(String atributo, Integer orden) throws Exception {
        return obj.orderList(atributo, orden);
    }

    public LinkedList<Proyecto> selectOrder(String atributo, Integer orden, String method) throws Exception {
        return obj.selectOrder(atributo, orden, method);
    }

    public Proyecto obtenerProyectoPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public String[] getProyectoAttributeLists() {
        return obj.getProyectoAttributeLists();
    }

}
