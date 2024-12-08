package com.practica.controller.dao.services;

import com.practica.controller.dao.InversionistaDao;
import com.practica.controller.model.Inversionista;
import com.practica.controller.tda.list.LinkedList;

public class InversionistaServices {

    private final InversionistaDao obj;

    public InversionistaServices() {
        obj = new InversionistaDao();
    }

    public Inversionista getInversionista() {
        return obj.getInversionista();
    }

    public Boolean save() throws Exception {
        return obj.save();
    }

    public Boolean delete() throws Exception {
        return obj.delete();
    }

    public LinkedList<Inversionista> listAll() throws Exception {
        return obj.getListAll();
    }

    public void setInversionista(Inversionista inversionista) {
        obj.setInversionista(inversionista);
    }

    public Inversionista getInversionistaById(Integer id) throws Exception {
        return obj.getInversionistaById(id);

    }

    public String toJson() throws Exception {
        return obj.toJson();

    }

    public LinkedList<Inversionista> getInversionistasBy(String atributo, Object valor) throws Exception {
        return obj.buscar(atributo, valor);
    }

    public LinkedList<Inversionista> orderListBy(String atributo, Integer orden) throws Exception {
        return obj.orderList(atributo, orden);
    }

    public LinkedList<Inversionista> selectOrder(String atributo, Integer orden, String method) throws Exception {
        return obj.selectOrder(atributo, orden, method);
    }

    public Inversionista obtenerInversionistaPor(String atributo, Object valor) throws Exception {
        return obj.buscarPor(atributo, valor);
    }

    public Boolean update() throws Exception {
        return obj.update();
    }

    public String[] getInversionistaAttributeLists() {
        return obj.getInversionistaAttributeLists();
    }

}
