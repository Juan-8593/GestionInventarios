package com.juanpirir.gestioninventarios.controller;

import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/productos")
public class ProductoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
            Root<Producto> root = cq.from(Producto.class);
            cq.select(root);
            List<Producto> productos = em.createQuery(cq).getResultList();

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("[");
            for (int i = 0; i < productos.size(); i++) {
                Producto p = productos.get(i);
                out.print("{\"id\":" + p.getId() +
                        ",\"nombre\":\"" + p.getNombre() + "\"" +
                        ",\"descripcion\":\"" + (p.getDescripcion() != null ? p.getDescripcion() : "") + "\"" +
                        ",\"precio\":" + p.getPrecio() +
                        ",\"stockActual\":" + p.getStockActual() +
                        ",\"activo\":" + p.isActivo() +
                        ",\"fechaActualizacion\":\"" + p.getFechaActualizacion() + "\"}");
                if (i < productos.size() - 1) out.print(",");
            }
            out.print("]");
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String descripcion = request.getParameter("descripcion");
        String precioStr = request.getParameter("precio");
        String stockStr = request.getParameter("stock");

        if(nombre == null || nombre.isEmpty() || precioStr == null || stockStr == null){
            response.getWriter().write("Faltan datos obligatorios");
            return;
        }

        double precio = Double.parseDouble(precioStr);
        int stock = Integer.parseInt(stockStr);

        if(precio < 0 || stock < 0){
            response.getWriter().write("Precio o stock no pueden ser negativos");
            return;
        }

        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setDescripcion(descripcion);
        producto.setPrecio(precio);
        producto.setStockActual(stock);
        producto.setActivo(true);
        producto.setFechaActualizacion(LocalDateTime.now());

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(producto);
            em.getTransaction().commit();
            response.getWriter().write("Producto agregado correctamente");
        } catch (Exception e) {
            em.getTransaction().rollback();
            response.getWriter().write("Error al agregar producto: "+e.getMessage());
        } finally {
            em.close();
        }
    }
}