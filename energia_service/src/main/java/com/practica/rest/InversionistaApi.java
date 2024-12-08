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

import com.practica.controller.dao.services.InversionistaServices;
import com.practica.controller.dao.services.ProyectoServices;
import com.practica.controller.excepction.ListEmptyException;
import com.practica.controller.model.Inversionista;
import com.practica.controller.tda.list.LinkedList;

@Path("/inversionista")
public class InversionistaApi {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getAll() throws ListEmptyException, Exception {
        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        try {
            res.put("status", "OK");
            LinkedList<Inversionista> lista = is.listAll();
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de inversionistas: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @Path("/save")
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response save(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();

        try {
            if (map.get("idProyecto") != null) {
                ProyectoServices ps = new ProyectoServices();
                ps.setProyecto(ps.getProyectoById(Integer.valueOf(map.get("idProyecto").toString())));

                if (ps.getProyecto().getId() != null) {
                    if (map.get("nombre") == null || map.get("nombre").toString().isEmpty()) {
                        throw new IllegalArgumentException("El nombre es obligatorio.");
                    }
                    if (map.get("ruc") == null || map.get("ruc").toString().isEmpty()) {
                        throw new IllegalArgumentException("El registro es obligatorio.");
                    }

                    InversionistaServices is = new InversionistaServices();
                    is.getInversionista().setNombre(map.get("nombre").toString());
                    is.getInversionista().setIdProyecto(ps.getProyecto().getId());
                    is.getInversionista().setRuc(map.get("ruc").toString());

                    is.save();
                    res.put("msg", "OK");
                    res.put("data", "Guardado con éxito");
                    return Response.ok(res).build();
                } else {
                    throw new IllegalArgumentException("El proyecto no existe.");
                }
            } else {
                throw new IllegalArgumentException("El id del proyecto es obligatorio.");
            }
        } catch (IllegalArgumentException e) {
            res.put("msg", "ERROR");
            res.put("error", e.getMessage());
            return Response.status(Status.BAD_REQUEST).entity(res).build();
        } catch (Exception e) {
            e.printStackTrace();
            res.put("msg", "ERROR");
            res.put("error", "Ocurrió un error inesperado: " + e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/get/{id}")
    public Response getInversionistaById(@PathParam("id") Integer id) throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        try {
            map.put("msg", "OK");
            map.put("data", is.getInversionistaById(id));
            if (is.getInversionistaById(id) == null) {
                map.put("msg", "ERROR");
                map.put("data", "No se encontro el inversionista con id: " + id);
                return Response.status(Status.NOT_FOUND).entity(map).build();
            }
            return Response.ok(map).build();
        } catch (Exception e) {
            map.put("msg", "ERROR");
            map.put("error", e.toString());
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity(map).build();
        }
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/update")
    public Response update(HashMap<String, Object> map) throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        try {
            InversionistaServices is = new InversionistaServices();
            is.getInversionista().setId(Integer.valueOf(map.get("id").toString()));
            is.getInversionista().setNombre(map.get("nombre").toString());

            is.update();
            res.put("msg", "OK");
            res.put("data", "Actualizado con exito");
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

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{id}/delete")
    public Response delete(@PathParam("id") Integer id) throws Exception {

        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices ps = new InversionistaServices();
        try {
            ps.getInversionista().setId(id);
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
    @Path("/list/search/{attribute}/{value}")
    public Response buscar(@PathParam("attribute") String attribute, @PathParam("value") String value)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices ps = new InversionistaServices();
        try {
            if (attribute == null || attribute.isEmpty() || value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }

            LinkedList<Inversionista> inversionistas = ps.getInversionistasBy(attribute, value);
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
    public Response buscarInversionista(@PathParam("value") String value)
            throws Exception {
        HashMap<String, Object> res = new HashMap<>();
        InversionistaServices ps = new InversionistaServices();
        try {
            if (value == null || value.isEmpty()) {
                throw new IllegalArgumentException("Los parametros no pueden ser nulos o vacios.");
            }

            Object inversionista = ps.obtenerInversionistaPor("nombre", value);
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
        InversionistaServices ps = new InversionistaServices();
        try {
            res.put("status", "OK");
            LinkedList<Inversionista> lista = ps.selectOrder(attribute, type, method);
            res.put("msg", "Consulta exitosa.");
            res.put("data", lista.toArray());
            if (lista.isEmpty()) {
                res.put("data", new Object[] {});
            }
            return Response.ok(res).build();
        } catch (Exception e) {
            res.put("status", "ERROR");
            res.put("msg", "Error al obtener la lista de inversionistas: " + e.getMessage());
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(res).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("list/criteria")
    public Response getCrrioList() throws Exception {
        HashMap<String, Object> map = new HashMap<>();
        InversionistaServices is = new InversionistaServices();
        map.put("msg", "OK");
        map.put("data", is.getInversionistaAttributeLists());
        return Response.ok(map).build();
    }

}
