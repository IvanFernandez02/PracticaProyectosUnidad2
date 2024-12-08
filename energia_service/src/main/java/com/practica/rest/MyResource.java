package com.practica.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.practica.controller.model.Proyecto;
import com.practica.controller.tda.list.LinkedList;

@Path("myresource")
public class MyResource {
    
    private Proyecto[] generarDatosAleatorios(int size) {
        Proyecto[] proyectos = new Proyecto[size];
        for (int i = 0; i < size; i++) {
            Proyecto p = new Proyecto();
            p.setId(i + 1);
            p.setNombre("Proyecto" + (int)(Math.random() * 1000000));
            proyectos[i] = p;
        }
        return proyectos;
    }

    private void medirTiempos(int size) throws Exception {
        LinkedList<Proyecto> listaOriginal = new LinkedList<>();
        Proyecto[] datos = generarDatosAleatorios(size);
        
        for (Proyecto p : datos) {
            listaOriginal.add(p);
        }

        Proyecto target = datos[size/2];
        System.out.println("\n\t\t****PRUEBAS DE RENDIMIENTO CON " + size + " ELEMENTOS****\n");
        
        // Búsqueda Secuencial
        LinkedList<Proyecto> lista = listaOriginal.cloneList();
        long inicio = System.currentTimeMillis();
        lista.linearSearch(target);
        long fin = System.currentTimeMillis();
        System.out.println("--------------Metodos de Busqueda--------------\n");
        System.out.println("Con la busqueda lineal se demora: " + (fin - inicio) + " ms");

        // Búsqueda Lineal Binaria
        lista = listaOriginal.cloneList();
        inicio = System.currentTimeMillis();
        lista.binarySarch(target);
        fin = System.currentTimeMillis();
        System.out.println("Con la busqueda binaria se demora: " + (fin - inicio) + " ms");

        // QuickSort
        lista = listaOriginal.cloneList();
        inicio = System.currentTimeMillis();
        lista.quickSort("nombre", 1);
        fin = System.currentTimeMillis();
        System.out.println("\n--------------Metodos de Ordenamiento--------------\n");
        System.out.println("Con el metodo de ordenacion QuickSort demora: " + (fin - inicio) + " ms");

        // MergeSort
        lista = listaOriginal.cloneList();
        inicio = System.currentTimeMillis();
        lista.mergeSort("nombre", 1);
        fin = System.currentTimeMillis();
        System.out.println("Con el metodo de ordenacion MergeSort demora: " + (fin - inicio) + " ms");

        // ShellSort
        lista = listaOriginal.cloneList();
        inicio = System.currentTimeMillis();
        lista.shellSort("nombre", 1);
        fin = System.currentTimeMillis();
        System.out.println("Con el metodo de ordenacion ShellSort demora: " + (fin - inicio) + " ms");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getIt() throws Exception {
        medirTiempos(10000);
        medirTiempos(20000);
        medirTiempos(25000);
        return "Go it!!";
    }
}