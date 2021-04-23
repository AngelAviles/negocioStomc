/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.AttentionPoint;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dominio.Employee;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class AttentionPointJpaController implements Serializable {

    public AttentionPointJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(AttentionPoint attentionPoint) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee = attentionPoint.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getId());
                attentionPoint.setEmployee(employee);
            }
            em.persist(attentionPoint);
            if (employee != null) {
                AttentionPoint oldIdAttentionPointOfEmployee = employee.getIdAttentionPoint();
                if (oldIdAttentionPointOfEmployee != null) {
                    oldIdAttentionPointOfEmployee.setEmployee(null);
                    oldIdAttentionPointOfEmployee = em.merge(oldIdAttentionPointOfEmployee);
                }
                employee.setIdAttentionPoint(attentionPoint);
                employee = em.merge(employee);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(AttentionPoint attentionPoint) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            AttentionPoint persistentAttentionPoint = em.find(AttentionPoint.class, attentionPoint.getId());
            Employee employeeOld = persistentAttentionPoint.getEmployee();
            Employee employeeNew = attentionPoint.getEmployee();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getId());
                attentionPoint.setEmployee(employeeNew);
            }
            attentionPoint = em.merge(attentionPoint);
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.setIdAttentionPoint(null);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                AttentionPoint oldIdAttentionPointOfEmployee = employeeNew.getIdAttentionPoint();
                if (oldIdAttentionPointOfEmployee != null) {
                    oldIdAttentionPointOfEmployee.setEmployee(null);
                    oldIdAttentionPointOfEmployee = em.merge(oldIdAttentionPointOfEmployee);
                }
                employeeNew.setIdAttentionPoint(attentionPoint);
                employeeNew = em.merge(employeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = attentionPoint.getId();
                if (findAttentionPoint(id) == null) {
                    throw new NonexistentEntityException("The attentionPoint with id " + id + " no longer exists.");
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
            AttentionPoint attentionPoint;
            try {
                attentionPoint = em.getReference(AttentionPoint.class, id);
                attentionPoint.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The attentionPoint with id " + id + " no longer exists.", enfe);
            }
            Employee employee = attentionPoint.getEmployee();
            if (employee != null) {
                employee.setIdAttentionPoint(null);
                employee = em.merge(employee);
            }
            em.remove(attentionPoint);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<AttentionPoint> findAttentionPointEntities() {
        return findAttentionPointEntities(true, -1, -1);
    }

    public List<AttentionPoint> findAttentionPointEntities(int maxResults, int firstResult) {
        return findAttentionPointEntities(false, maxResults, firstResult);
    }

    private List<AttentionPoint> findAttentionPointEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(AttentionPoint.class));
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
    
    public List<AttentionPoint> findAttentionPointByFolio(Long folio) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<AttentionPoint> consultaPuntosDeAtencion = em.createNamedQuery("AttentionPoint.findByFolio", AttentionPoint.class);
        consultaPuntosDeAtencion.setParameter("folio", folio);
        
        List<AttentionPoint> puntosDeAtencion = new ArrayList<>();
        
        try {
            puntosDeAtencion = consultaPuntosDeAtencion.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return puntosDeAtencion;
    }
    
    public List<AttentionPoint> findAttentionPointByPoint(String point) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<AttentionPoint> consultaPuntosDeAtencion = em.createNamedQuery("AttentionPoint.findByPoint", AttentionPoint.class);
        consultaPuntosDeAtencion.setParameter("point", point);
        
        List<AttentionPoint> puntosDeAtencion = new ArrayList<>();
        
        try {
            puntosDeAtencion = consultaPuntosDeAtencion.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return puntosDeAtencion;
    }

    public AttentionPoint findAttentionPoint(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(AttentionPoint.class, id);
        } finally {
            em.close();
        }
    }

    public int getAttentionPointCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<AttentionPoint> rt = cq.from(AttentionPoint.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
