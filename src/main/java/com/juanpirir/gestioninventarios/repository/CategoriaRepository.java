package com.juanpirir.gestioninventarios.repository;

import com.juanpirir.gestioninventarios.model.Categoria;
import com.juanpirir.gestioninventarios.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class CategoriaRepository {

    public List<Categoria> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Categoria> query = em.createQuery("SELECT c FROM Categoria c", Categoria.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Categoria findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Categoria.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Categoria categoria) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (categoria.getId() == null) {
                em.persist(categoria);
            } else {
                em.merge(categoria);
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
            Categoria categoria = em.find(Categoria.class, id);
            if (categoria != null) em.remove(categoria);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
