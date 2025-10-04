package com.juanpirir.gestioninventarios.repository;

import com.juanpirir.gestioninventarios.model.Movimiento;
import com.juanpirir.gestioninventarios.util.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class MovimientoRepository {

    public List<Movimiento> findAll() {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            TypedQuery<Movimiento> query = em.createQuery("SELECT m FROM Movimiento m", Movimiento.class);
            return query.getResultList();
        } finally {
            em.close();
        }
    }

    public Movimiento findById(Long id) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.find(Movimiento.class, id);
        } finally {
            em.close();
        }
    }

    public void save(Movimiento movimiento) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            em.getTransaction().begin();
            if (movimiento.getId() == null) {
                em.persist(movimiento);
            } else {
                em.merge(movimiento);
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
            Movimiento movimiento = em.find(Movimiento.class, id);
            if (movimiento != null) em.remove(movimiento);
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}
