/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.ReportingLog;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ReportingLogJpaController implements Serializable {

    public ReportingLogJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public ReportingLogJpaController() {
        this.emf = Persistence.createEntityManagerFactory("stomcPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ReportingLog reportingLog) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            em.persist(reportingLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ReportingLog reportingLog) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            reportingLog = em.merge(reportingLog);
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = reportingLog.getId();
                if (findReportingLog(id) == null) {
                    throw new NonexistentEntityException("The reportingLog with id " + id + " no longer exists.");
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
            ReportingLog reportingLog;
            try {
                reportingLog = em.getReference(ReportingLog.class, id);
                reportingLog.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The reportingLog with id " + id + " no longer exists.", enfe);
            }
            em.remove(reportingLog);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<ReportingLog> findReportingLogEntities() {
        return findReportingLogEntities(true, -1, -1);
    }

    public List<ReportingLog> findReportingLogEntities(int maxResults, int firstResult) {
        return findReportingLogEntities(false, maxResults, firstResult);
    }

    private List<ReportingLog> findReportingLogEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ReportingLog.class));
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

    public ReportingLog findReportingLog(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ReportingLog.class, id);
        } finally {
            em.close();
        }
    }

    public int getReportingLogCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ReportingLog> rt = cq.from(ReportingLog.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
