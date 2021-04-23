/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.CatalogueProfile;
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
public class CatalogueProfileJpaController implements Serializable {

    public CatalogueProfileJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CatalogueProfile catalogueProfile) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(catalogueProfile);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(CatalogueProfile catalogueProfile) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            catalogueProfile = em.merge(catalogueProfile);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = catalogueProfile.getId();
                if (findCatalogueProfile(id) == null) {
                    throw new NonexistentEntityException("The catalogueProfile with id " + id + " no longer exists.");
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
            CatalogueProfile catalogueProfile;
            try {
                catalogueProfile = em.getReference(CatalogueProfile.class, id);
                catalogueProfile.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The catalogueProfile with id " + id + " no longer exists.", enfe);
            }
            em.remove(catalogueProfile);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CatalogueProfile> findCatalogueProfileEntities() {
        return findCatalogueProfileEntities(true, -1, -1);
    }

    public List<CatalogueProfile> findCatalogueProfileEntities(int maxResults, int firstResult) {
        return findCatalogueProfileEntities(false, maxResults, firstResult);
    }

    private List<CatalogueProfile> findCatalogueProfileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CatalogueProfile.class));
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

    public CatalogueProfile findCatalogueProfile(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CatalogueProfile.class, id);
        } finally {
            em.close();
        }
    }

    public int getCatalogueProfileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CatalogueProfile> rt = cq.from(CatalogueProfile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}