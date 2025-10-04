package com.juanpirir.gestioninventarios.controller;

import com.juanpirir.gestioninventarios.model.Categoria;
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
import java.util.List;

@WebServlet("/categorias")
public class CategoriaController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Categoria> cq = cb.createQuery(Categoria.class);
            Root<Categoria> root = cq.from(Categoria.class);
            cq.select(root);
            List<Categoria> categorias = em.createQuery(cq).getResultList();

            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.print("[");
            for(int i=0;i<categorias.size();i++){
                Categoria c = categorias.get(i);
                out.print("{\"id\":"+c.getId()+",\"nombre\":\""+c.getNombre()+"\"}");
                if(i<categorias.size()-1) out.print(",");
            }
            out.print("]");
        } finally {
            em.close();
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        if(nombre == null || nombre.isEmpty()){
            response.getWriter().write("Nombre es obligatorio");
            return;
        }

        Categoria categoria = new Categoria();
        categoria.setNombre(nombre);

        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(categoria);
            em.getTransaction().commit();
            response.getWriter().write("Categoría agregada correctamente");
        } catch (Exception e){
            em.getTransaction().rollback();
            response.getWriter().write("Error al agregar categoría: "+e.getMessage());
        } finally {
            em.close();
        }
    }
}