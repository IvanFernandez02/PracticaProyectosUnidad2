package com.practica.controller.dao;

import java.lang.reflect.Method;

import com.google.gson.Gson;
import com.practica.controller.dao.implement.AdapterDao;
import com.practica.controller.model.Proyecto;
import com.practica.controller.tda.list.LinkedList;

public class ProyectoDao extends AdapterDao<Proyecto> {
    private Proyecto proyecto;
    private LinkedList<Proyecto> listAll;

    public ProyectoDao() {
        super(Proyecto.class);
    }

    public Proyecto getProyecto() {
        if (this.proyecto == null) {
            this.proyecto = new Proyecto();
        }
        return this.proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public LinkedList<Proyecto> getListAll() throws Exception {
        if (listAll == null) {
            this.listAll = listAll();
        }
        return listAll;
    }

    public Boolean save() throws Exception {
        Integer id = getListAll().getSize() + 1;
        proyecto.setId(id);
        this.persist(this.proyecto);
        this.listAll = listAll();
        return true;
    }

    public Boolean update() throws Exception {
        this.merge(this.proyecto, this.proyecto.getId());
        listAll = listAll();
        return true;
    }

    public Boolean delete() throws Exception {
        if (listAll == null) {
            listAll = getListAll();
        }
        this.delete(proyecto.getId());
        reindex();
        return true;
    }

    private void reindex() throws Exception {

        @SuppressWarnings("unchecked")

        LinkedList<Proyecto> listAll = listAll();
        Proyecto[] matrix = listAll.toArray();
        int i = 0;
        for (Proyecto p : matrix) {
            p.setId(i + 1);
            this.merge(p, i + 1);
            i++;
        }
    }

    private LinkedList<Proyecto> linearSearch(String attribute, Object value) throws Exception {
        LinkedList<Proyecto> lista = this.listAll().quickSort(attribute, 1);
        LinkedList<Proyecto> proyectos = new LinkedList<>();

        if (!lista.isEmpty()) {
            Proyecto[] aux = lista.toArray();
            Integer index = binarySearch(aux, attribute, value.toString().toLowerCase());

            if (index != -1) {
                addMatchingProyectos(aux, attribute, value.toString().toLowerCase(), index, proyectos);
            }
        }

        return proyectos;
    }

    private Integer binarySearch(Proyecto[] list, String attribute, String searchValue) throws Exception {
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

    private void addMatchingProyectos(Proyecto[] list, String attribute, String searchValue, Integer index,
            LinkedList<Proyecto> proyectos) throws Exception {
        Integer i = index;
        while (i < list.length
                && obtenerAttributeValue(list[i], attribute).toString().toLowerCase().startsWith(searchValue)) {
            proyectos.add(list[i]);
            System.out.println("Agregando: " + list[i].getNombre());
            i++;
        }
    }

    public LinkedList<Proyecto> buscar(String attribute, Object value) throws Exception {
        return linearSearch(attribute, value);
    }

    public Proyecto buscarPor(String attribute, Object value) throws Exception {
        LinkedList<Proyecto> lista = listAll();
        Proyecto p = null;

        if (!lista.isEmpty()) {
            Proyecto[] proyectos = lista.toArray();
            for (Proyecto proyecto1 : proyectos) {
                if (obtenerAttributeValue(proyecto1, attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    p = proyecto1;
                    break;
                }
            }
        }
        return p;
    }

    private Integer getProyectoIndex(String attribute, Object value) throws Exception {
        if (this.listAll == null) {
            this.listAll = listAll();
        }
        Integer index = -1;
        if (!this.listAll.isEmpty()) {
            Proyecto[] proyectos = this.listAll.toArray();
            for (int i = 0; i < proyectos.length; i++) {
                if (obtenerAttributeValue(proyectos[i], attribute).toString().toLowerCase()
                        .equals(value.toString().toLowerCase())) {
                    index = i;
                    break;
                }
            }
        }
        return index;
    }

    public String[] getProyectoAttributeLists() {
        LinkedList<String> attributes = new LinkedList<>();
        for (Method m : Proyecto.class.getDeclaredMethods()) {
            if (m.getName().startsWith("get")) {
                String attribute = m.getName().substring(3);
                if (!attribute.equalsIgnoreCase("id")) {
                    attributes.add(attribute.substring(0, 1).toLowerCase() + attribute.substring(1));
                }
            }
        }
        return attributes.toArray();
    }

    public LinkedList<Proyecto> orderList(String attribute, Integer order) throws Exception {
        LinkedList<Proyecto> lista = listAll();

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

        throw new NoSuchMethodException("Error: " + attribute);
    }

                /************Eleccion tipo de Ordernamiento (Mergesort, Quicksort, ShellSort)*******/

    public LinkedList<Proyecto> selectOrder(String attribute, Integer order, String method) throws Exception {
        LinkedList<Proyecto> lista = listAll();
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
        return g.toJson(this.proyecto);
    }

    public Proyecto getProyectoById(Integer id) throws Exception {
        return get(id);
    }

    public String getProyectoJasonByIndex(Integer index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(index));
    }

    public String getProyectoJson(Integer Index) throws Exception {
        Gson g = new Gson();
        return g.toJson(get(Index));
    }
}
