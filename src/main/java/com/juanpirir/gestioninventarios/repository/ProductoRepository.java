package com.juanpirir.gestioninventarios.repository;

import com.juanpirir.gestioninventarios.model.Producto;
import com.juanpirir.gestioninventarios.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class ProductoRepository {

    public List<Producto> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Producto> query = em.createQuery("SELECT p FROM Producto p", Producto.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Producto findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Producto producto) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (producto.getId() == null) {
                em.persist(producto);
            } else {
                em.merge(producto);
            }
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void delete(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            Producto producto = em.find(Producto.class, id);
            if (producto != null) em.remove(producto);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
