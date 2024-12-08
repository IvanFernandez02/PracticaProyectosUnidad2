/**
 * Clase que contiene los servicios REST para la entidad Proyecto.
 * metodos: guardar, listar, obtener por id, obtener provincias, obtener tipos de energia, obtener estados, guardar inversionista en proyecto, obtener inversionistas por proyecto.
 */

package com.practica.rest;

import java.util.HashMap;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.practica.controller.dao.services.ProyectoServices;
import com.practica.controller.excepction.ListEmptyException;
import com.practica.controller.model.Proyecto;
import com.practica.controller.tda.list.LinkedList;

@Path("/proyecto")
public class ProyectoApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAllProyects() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        try {
            res.put("status", "OK");
            LinkedList<Proyecto> lista = ps.listAll();
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de proyectos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) throws Exception {

        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();

        try {
            System.out.println("hasta aqui 1");
            ps.getProyecto().setNombre(map.get("nombre").toString());
            ps.getProyecto().setInversion(Double.valueOf(map.get("inversion").toString()));
            ps.getProyecto().setFechaInicio(map.get("fechaInicio").toString());
            ps.getProyecto().setTiempoDeVida(Integer.valueOf(map.get("tiempoDeVida").toString()));
            ps.getProyecto()
                    .setCapacidad(map.get("capacidad") != null ? Integer.valueOf(map.get("capacidad").toString()) : 0);
            if (map.containsKey("fechaFin") && map.get("fechaFin") != null) {
                ps.getProyecto().setFechaFin(map.get("fechaFin").toString());
            } else {
                ps.getProyecto().setFechaFin("none");
            }

            ps.save();
            res.put("msg", "OK");
            res.put("data", "Guardado con exito.");
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("msg", "ERROR");
            res.put("error", "Error inesperado: " + e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        try {
            ps.getProyecto().setId(id);
            ps.delete();
            res.put("estado", "Ok");
            res.put("data", "Registro eliminado con exito.");
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("estado", "error");
            res.put("data", "Error interno del servidor: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getProyectoById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        try {
            Object proyecto = ps.getProyectoById(id);

            if (proyecto == null) {
                map.put("msg", "ERROR");
                map.put("data", "No se encontro el proyecto con id: " + id);
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            map.put("msg", "OK");
            map.put("data", proyecto);
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "ERROR");
            map.put("error", "Error inesperado: " + e.getMessage());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/criteria")
    public Response getCrrioList() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        map.put("msg", "OK");
        map.put("data", ps.getProyectoAttributeLists());
        return Response.ok(map).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response update(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();

        try {

            ProyectoServices ps = new ProyectoServices();
            ps.setProyecto(ps.getProyectoById(Integer.valueOf(map.get("id").toString())));
            ps.getProyecto().setNombre(map.get("nombre").toString());
            ps.getProyecto().setInversion(Double.valueOf(map.get("inversion").toString()));
            ps.getProyecto().setFechaInicio(map.get("fechaInicio").toString());
            ps.getProyecto().setTiempoDeVida(Integer.valueOf(map.get("tiempoDeVida").toString()));
            ps.getProyecto()
                    .setCapacidad(map.get("capacidad") != null ? Integer.valueOf(map.get("capacidad").toString()) : 0);
            if (map.containsKey("fechaFin") && map.get("fechaFin") != null) {
                ps.getProyecto().setFechaFin(map.get("fechaFin").toString());
            } else {
                ps.getProyecto().setFechaFin("none");
            }

            ps.update();
            res.put("msg", "OK");
            res.put("data", "Registro actualizado con exito.");
            return Response.ok(res).build();

        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            res.put("msg", "ERROR");
            res.put("error", "Ocurrio un error inesperado: " + e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/{attribute}/{value}")
    public Response buscar(@PathParam("attribute") String attribute, @PathParam("value") String value)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        try {
            if (attribute == null || attribute.isEmpty() || value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }

            LinkedList<Proyecto> inversionistas = ps.getProyectosBy(attribute, value);
            res.put("status", "OK");
            res.put("msg", "Consulta exitosa.");
            res.put("data", inversionistas.isEmpty() ? new Object[] {} : inversionistas.toArray());

            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error realizar la busqueda: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list/search/nombre/{value}")
    public Response buscarProyecto(@PathParam("value") String value)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        try {

            Object inversionista = ps.obtenerProyectoPor("nombre", value);
            if (inversionista == null) {
                res.put("status", "ERROR");
                res.put("msg", "No se encontró el inversionista con : " + value);
                return Response.status(Response.Status.BAD_REQUEST).entity(res).build();
            } else {
                res.put("status", "OK");
                res.put("msg", "Consulta exitosa.");
                res.put("data", inversionista);
                return Response.ok(res).build();
            }
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error realizar la busqueda: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/order/{attribute}/{type}/{method}")
    public Response listOrder(@PathParam("attribute") String attribute, @PathParam("type") Integer type,
            @PathParam("method") String method)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        ProyectoServices ps = new ProyectoServices();
        try {
            res.put("status", "OK");
            LinkedList<Proyecto> lista = ps.selectOrder(attribute, type, method);
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de proyectos: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

}
