/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.LysingInformation;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class LysingInformationJpaController implements Serializable {

    public LysingInformationJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(LysingInformation lysingInformation) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(lysingInformation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(LysingInformation lysingInformation) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            lysingInformation = em.merge(lysingInformation);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = lysingInformation.getId();
                if (findLysingInformation(id) == null) {
                    throw new NonexistentEntityException("The lysingInformation with id " + id + " no longer exists.");
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
            LysingInformation lysingInformation;
            try {
                lysingInformation = em.getReference(LysingInformation.class, id);
                lysingInformation.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The lysingInformation with id " + id + " no longer exists.", enfe);
            }
            em.remove(lysingInformation);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<LysingInformation> findLysingInformationEntities() {
        return findLysingInformationEntities(true, -1, -1);
    }

    public List<LysingInformation> findLysingInformationEntities(int maxResults, int firstResult) {
        return findLysingInformationEntities(false, maxResults, firstResult);
    }

    private List<LysingInformation> findLysingInformationEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(LysingInformation.class));
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
    
    public List<LysingInformation> findLysingInformationByFolio(Long folio) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<LysingInformation> consultaLysingInformation = em.createNamedQuery("LysingInformation.findByFolio", LysingInformation.class);
        consultaLysingInformation.setParameter("folio", folio);
        
        List<LysingInformation> lysingInformations = new ArrayList<>();
        
        try {
            lysingInformations = consultaLysingInformation.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return lysingInformations;
    }
    
    public List<LysingInformation> findLysingInformationByTitle(String title) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<LysingInformation> consultaLysingInformation = em.createNamedQuery("LysingInformation.findByTitle", LysingInformation.class);
        consultaLysingInformation.setParameter("title", title);
        
        List<LysingInformation> lysingInformations = new ArrayList<>();
        
        try {
            lysingInformations = consultaLysingInformation.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return lysingInformations;
    }
    
    public List<LysingInformation> findLysingInformationByProcess(String process) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<LysingInformation> consultaLysingInformation = em.createNamedQuery("LysingInformation.findByProcess", LysingInformation.class);
        consultaLysingInformation.setParameter("process", process);
        
        List<LysingInformation> lysingInformations = new ArrayList<>();
        
        try {
            lysingInformations = consultaLysingInformation.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return lysingInformations;
    }

    public LysingInformation findLysingInformation(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(LysingInformation.class, id);
        } finally {
            em.close();
        }
    }

    public int getLysingInformationCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<LysingInformation> rt = cq.from(LysingInformation.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
