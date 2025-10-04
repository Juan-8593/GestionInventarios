package com.juanpirir.gestioninventarios.controller;

import com.juanpirir.gestioninventarios.model.Categoria;
import com.juanpirir.gestioninventarios.model.Movimiento;
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

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = JPAUtil.getEntityManager();

        try {
            // Productos
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Producto> cqProd = cb.createQuery(Producto.class);
            Root<Producto> rootProd = cqProd.from(Producto.class);
            cqProd.select(rootProd);
            List<Producto> productos = em.createQuery(cqProd).getResultList();

            // Categorías
            CriteriaQuery<Categoria> cqCat = cb.createQuery(Categoria.class);
            Root<Categoria> rootCat = cqCat.from(Categoria.class);
            cqCat.select(rootCat);
            List<Categoria> categorias = em.createQuery(cqCat).getResultList();

            // Movimientos de esta semana
            CriteriaQuery<Movimiento> cqMov = cb.createQuery(Movimiento.class);
            Root<Movimiento> rootMov = cqMov.from(Movimiento.class);
            cqMov.select(rootMov);
            List<Movimiento> movimientos = em.createQuery(cqMov).getResultList();
            long semana = movimientos.stream()
                    .filter(m -> m.getFecha().isAfter(LocalDateTime.now().minusDays(7)))
                    .count();

            // Última actualización
            LocalDateTime ultima = productos.stream()
                    .map(Producto::getFechaActualizacion)
                    .max(LocalDateTime::compareTo)
                    .orElse(null);

            // JSON
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("{");
            out.print("\"totalProductos\":" + productos.size() + ",");
            out.print("\"stockBajo\":" + productos.stream().filter(p -> p.getStockActual() < 5).count() + ",");
            out.print("\"productosInactivos\":" + productos.stream().filter(p -> !p.isActivo()).count() + ",");
            out.print("\"totalCategorias\":" + categorias.size() + ",");
            out.print("\"movimientosSemana\":" + semana + ",");
            out.print("\"ultimaActualizacion\":\"" + (ultima != null ? ultima.toString() : "") + "\"");
            out.print("}");

        } finally {
            em.close();
        }
    }
}