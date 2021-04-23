/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.CatalogueAttentionPoint;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class CatalogueAttentionPointJpaController implements Serializable {

    public CatalogueAttentionPointJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CatalogueAttentionPoint catalogueAttentionPoint) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(catalogueAttentionPoint);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CatalogueAttentionPoint catalogueAttentionPoint) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            catalogueAttentionPoint = em.merge(catalogueAttentionPoint);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = catalogueAttentionPoint.getId();
                if (findCatalogueAttentionPoint(id) == null) {
                    throw new NonexistentEntityException("The catalogueAttentionPoint with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CatalogueAttentionPoint catalogueAttentionPoint;
            try {
                catalogueAttentionPoint = em.getReference(CatalogueAttentionPoint.class, id);
                catalogueAttentionPoint.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The catalogueAttentionPoint with id " + id + " no longer exists.", enfe);
            }
            em.remove(catalogueAttentionPoint);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CatalogueAttentionPoint> findCatalogueAttentionPointEntities() {
        return findCatalogueAttentionPointEntities(true, -1, -1);
    }

    public List<CatalogueAttentionPoint> findCatalogueAttentionPointEntities(int maxResults, int firstResult) {
        return findCatalogueAttentionPointEntities(false, maxResults, firstResult);
    }

    private List<CatalogueAttentionPoint> findCatalogueAttentionPointEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CatalogueAttentionPoint.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public CatalogueAttentionPoint findCatalogueAttentionPoint(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CatalogueAttentionPoint.class, id);
        } finally {
            em.close();
        }
    }

    public int getCatalogueAttentionPointCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CatalogueAttentionPoint> rt = cq.from(CatalogueAttentionPoint.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}