package com.practica.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.practica.controller.dao.implement.AdapterDao;
import com.practica.controller.model.Inversionista;
import com.practica.controller.tda.list.LinkedList;

public class InversionistaDao extends AdapterDao<Inversionista> {
    private Inversionista inversionista;
    private LinkedList<Inversionista> listAll;

    public InversionistaDao() {
        super(Inversionista.class);
    }

    public Inversionista getInversionista() {
        if (this.inversionista == null) {
            this.inversionista = new Inversionista();
        }
        return this.inversionista;
    }

    public void setInversionista(Inversionista inversionista) {
        this.inversionista = inversionista;
    }

    public LinkedList<Inversionista> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public boolean save() throws Exception {
        Integer id = listAll().getSize() + 1;
        this.inversionista.setId(id);
        this.persist(this.inversionista);
        this.listAll = listAll();
        return true;
    }

    @SuppressWarnings("unchecked")
    public Boolean update() throws Exception {
        this.merge(this.inversionista, this.inversionista.getId());
        listAll = listAll();
        return true;
    }

    public Boolean delete() throws Exception {
        if (listAll == null) {
            listAll = getListAll();
        }
        this.delete(inversionista.getId());
        reindex();
        return true;
    }

    private void reindex() throws Exception {

        LinkedList<Inversionista> listAll = listAll();
        Inversionista[] matrix = listAll.toArray();
        int i = 0;
        for (Inversionista p : matrix) {
            p.setId(i + 1);
            this.merge(p, i + 1);
            i++;
        }
    }

    private LinkedList<Inversionista> linearBinarySearch(String attribute, Object value) throws Exception {
        LinkedList<Inversionista> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<Inversionista> inversionistas = new LinkedList<>();

        if (!lista.isEmpty()) {
            Inversionista[] aux = lista.toArray();
            Integer index = binarySearch(aux, attribute, value.toString().toLowerCase());

            if (index != -1) {
                addMatchingInversionistas(aux, attribute, value.toString().toLowerCase(), index, inversionistas);
            }
        }

        return inversionistas;
    }

    private Integer binarySearch(Inversionista[] list, String attribute, String searchValue) throws Exception {
        Integer low = 0;
        Integer high = list.length - 1;
        Integer mid;
        Integer index = -1;

        while (low <= high) {
            mid = (low + high) / 2;
            String midValue = obtenerAttributeValue(list[mid], attribute).toString().toLowerCase();

            System.out.println("Comparando: " + midValue + " con " + searchValue);

            if (midValue.startsWith(searchValue)) {
                if (mid == 0 || !obtenerAttributeValue(list[mid - 1], attribute).toString().toLowerCase()
                        .startsWith(searchValue)) {
                    index = mid;
                    break;
                } else {
                    high = mid - 1;
                }
            } else if (midValue.compareToIgnoreCase(searchValue) < 0) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return index;
    }

    private void addMatchingInversionistas(Inversionista[] list, String attribute, String searchValue, Integer index,
            LinkedList<Inversionista> inversionistas) throws Exception {
        Integer i = index;
        while (i < list.length
                && obtenerAttributeValue(list[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
            inversionistas.add(list[i]);
            System.out.println("Agregando: " + list[i].getNombre());
            i++;
        }
    }

    public LinkedList<Inversionista> buscar(String attribute, Object value) throws Exception {
        return linearBinarySearch(attribute, value);
    }

    public Inversionista buscarPor(String attribute, Object value) throws Exception {
        LinkedList<Inversionista> lista = listAll();
        Inversionista p = null;

        if (!lista.isEmpty()) {
            Inversionista[] inversionistas = lista.toArray();
            for (Inversionista inversionista1 : inversionistas) {
                if (obtenerAttributeValue(inversionista1, attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = inversionista1;
                    break;
                }
            }
        }
        return p;
    }

    private Integer getInversionistaIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            Inversionista[] inversionistas = this.listAll.toArray();
            for (int i = 0; i < inversionistas.length; i++) {
                if (obtenerAttributeValue(inversionistas[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public String[] getInversionistaAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : Inversionista.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                String attribute = m.getName().substring(3);
                if (!attribute.equalsIgnoreCase("id")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    public LinkedList<Inversionista> orderList(String attribute, Integer order) throws Exception {
        LinkedList<Inversionista> lista = listAll();

        if (!lista.isEmpty()) {
            lista.mergeSort(attribute, order);
        }
        return lista;
    }

    private Object obtenerAttributeValue(Object object, String attribute) throws Exception {
        String normalizedAttribute = "get" + attribute.substring(0, 1).toUpperCase()
                + attribute.substring(1).toLowerCase();
        Method[] methods = object.getClass().getMethods();

        for (Method method : methods) {
            if (method.getName().equalsIgnoreCase(normalizedAttribute) && method.getParameterCount() == 0) {
                return method.invoke(object);
            }
        }

        throw new NoSuchMethodException("No se encontor el atributo: " + attribute);
    }

    public LinkedList<Inversionista> selectOrder(String attribute, Integer order, String method) throws Exception {
        LinkedList<Inversionista> lista = listAll();
        if (!lista.isEmpty()) {
            switch (method) {
                case "merge":
                    return lista.mergeSort(attribute, order);
                case "quick":
                    return lista.quickSort(attribute, order);
                case "shell":
                    return lista.shellSort(attribute, order);
                default:
                    throw new Exception("Metodo de ordenamiento no encontrado.");
            }
        }
        return lista;
    }

    public String toJson() throws Exception {
        Gson g = new Gson();
        return g.toJson(this.inversionista);
    }

    public Inversionista getInversionistaById(Integer id) throws Exception {
        return get(id);
    }

    public String getInversionistaJasonByIndex(Integer index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(index));
    }

    public String getInversionistaJson(Integer Index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(Index));
    }

}
