package com.juanpirir.gestioninventarios.repository;

import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.util.JPAUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class ProductoRepository {

    public List<Producto> listarTodos() {
        EntityManager em = JPAUtil.getEntityManager();
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Producto> cq = cb.createQuery(Producto.class);
        Root<Producto> root = cq.from(Producto.class);
        cq.select(root);
        List<Producto> productos = em.createQuery(cq).getResultList();
        em.close();
        return productos;
    }

    public void guardar(Producto producto) {
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(producto);
        em.getTransaction().commit();
        em.close();
    }
}
