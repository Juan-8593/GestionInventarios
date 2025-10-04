package com.juanpirir.gestioninventarios.controller;

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

@WebServlet("/movimientos")
public class MovimientoController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Movimiento> cq = cb.createQuery(Movimiento.class);
            Root<Movimiento> root = cq.from(Movimiento.class);
            cq.select(root);
            List<Movimiento> movimientos = em.createQuery(cq).getResultList();

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("[");
            for(int i=0;i<movimientos.size();i++){
                Movimiento m = movimientos.get(i);
                out.print("{\"id\":"+m.getId()+
                        ",\"productoId\":"+m.getProducto().getId()+
                        ",\"productoNombre\":\""+m.getProducto().getNombre()+"\""+
                        ",\"cantidad\":"+m.getCantidad()+
                        ",\"tipo\":\""+m.getTipo()+"\""+
                        ",\"motivo\":\""+(m.getMotivo()!=null?m.getMotivo():"")+"\""+
                        ",\"fecha\":\""+m.getFecha()+"\"}");
                if(i<movimientos.size()-1) out.print(",");
            }
            out.print("]");
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String productoIdStr = request.getParameter("productoId");
        String cantidadStr = request.getParameter("cantidad");
        String tipo = request.getParameter("tipo");
        String motivo = request.getParameter("motivo");

        if(productoIdStr == null || cantidadStr == null || tipo == null){
            response.getWriter().write("Faltan datos obligatorios");
            return;
        }

        Long productoId = Long.parseLong(productoIdStr);
        int cantidad = Integer.parseInt(cantidadStr);

        EntityManager em = JPAUtil.getEntityManager();
        try {
            Producto producto = em.find(Producto.class, productoId);
            if(producto==null){
                response.getWriter().write("Producto no encontrado");
                return;
            }

            if(tipo.equalsIgnoreCase("Salida") && producto.getStockActual() < cantidad){
                response.getWriter().write("No se puede hacer salida, stock insuficiente");
                return;
            }

            Movimiento movimiento = new Movimiento();
            movimiento.setProducto(producto);
            movimiento.setCantidad(cantidad);
            movimiento.setTipo(tipo);
            movimiento.setMotivo(motivo);
            movimiento.setFecha(LocalDateTime.now());

            // Actualizar stock
            if(tipo.equalsIgnoreCase("Entrada")) producto.setStockActual(producto.getStockActual()+cantidad);
            else producto.setStockActual(producto.getStockActual()-cantidad);

            producto.setFechaActualizacion(LocalDateTime.now());

            em.getTransaction().begin();
            em.persist(movimiento);
            em.merge(producto);
            em.getTransaction().commit();
            response.getWriter().write("Movimiento registrado correctamente");
        } catch(Exception e){
            em.getTransaction().rollback();
            response.getWriter().write("Error al registrar movimiento: "+e.getMessage());
        } finally {
            em.close();
        }
    }
}
