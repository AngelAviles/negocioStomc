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
        if (attentionPoint.getEmployeeList() == null) {
            attentionPoint.setEmployeeList(new ArrayList<Employee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Employee> attachedEmployeeList = new ArrayList<Employee>();
            for (Employee employeeListEmployeeToAttach : attentionPoint.getEmployeeList()) {
                employeeListEmployeeToAttach = em.getReference(employeeListEmployeeToAttach.getClass(), employeeListEmployeeToAttach.getId());
                attachedEmployeeList.add(employeeListEmployeeToAttach);
            }
            attentionPoint.setEmployeeList(attachedEmployeeList);
            em.persist(attentionPoint);
            for (Employee employeeListEmployee : attentionPoint.getEmployeeList()) {
                AttentionPoint oldIdAttentionPointOfEmployeeListEmployee = employeeListEmployee.getIdAttentionPoint();
                employeeListEmployee.setIdAttentionPoint(attentionPoint);
                employeeListEmployee = em.merge(employeeListEmployee);
                if (oldIdAttentionPointOfEmployeeListEmployee != null) {
                    oldIdAttentionPointOfEmployeeListEmployee.getEmployeeList().remove(employeeListEmployee);
                    oldIdAttentionPointOfEmployeeListEmployee = em.merge(oldIdAttentionPointOfEmployeeListEmployee);
                }
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
            List<Employee> employeeListOld = persistentAttentionPoint.getEmployeeList();
            List<Employee> employeeListNew = attentionPoint.getEmployeeList();
            List<Employee> attachedEmployeeListNew = new ArrayList<Employee>();
            for (Employee employeeListNewEmployeeToAttach : employeeListNew) {
                employeeListNewEmployeeToAttach = em.getReference(employeeListNewEmployeeToAttach.getClass(), employeeListNewEmployeeToAttach.getId());
                attachedEmployeeListNew.add(employeeListNewEmployeeToAttach);
            }
            employeeListNew = attachedEmployeeListNew;
            attentionPoint.setEmployeeList(employeeListNew);
            attentionPoint = em.merge(attentionPoint);
            for (Employee employeeListOldEmployee : employeeListOld) {
                if (!employeeListNew.contains(employeeListOldEmployee)) {
                    employeeListOldEmployee.setIdAttentionPoint(null);
                    employeeListOldEmployee = em.merge(employeeListOldEmployee);
                }
            }
            for (Employee employeeListNewEmployee : employeeListNew) {
                if (!employeeListOld.contains(employeeListNewEmployee)) {
                    AttentionPoint oldIdAttentionPointOfEmployeeListNewEmployee = employeeListNewEmployee.getIdAttentionPoint();
                    employeeListNewEmployee.setIdAttentionPoint(attentionPoint);
                    employeeListNewEmployee = em.merge(employeeListNewEmployee);
                    if (oldIdAttentionPointOfEmployeeListNewEmployee != null && !oldIdAttentionPointOfEmployeeListNewEmployee.equals(attentionPoint)) {
                        oldIdAttentionPointOfEmployeeListNewEmployee.getEmployeeList().remove(employeeListNewEmployee);
                        oldIdAttentionPointOfEmployeeListNewEmployee = em.merge(oldIdAttentionPointOfEmployeeListNewEmployee);
                    }
                }
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
            List<Employee> employeeList = attentionPoint.getEmployeeList();
            for (Employee employeeListEmployee : employeeList) {
                employeeListEmployee.setIdAttentionPoint(null);
                employeeListEmployee = em.merge(employeeListEmployee);
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
