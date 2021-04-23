/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.Branch;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dominio.Employee;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class BranchJpaController implements Serializable {

    public BranchJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Branch branch) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee = branch.getEmployee();
            if (employee != null) {
                employee = em.getReference(employee.getClass(), employee.getId());
                branch.setEmployee(employee);
            }
            em.persist(branch);
            if (employee != null) {
                Branch oldIdBranchOfEmployee = employee.getIdBranch();
                if (oldIdBranchOfEmployee != null) {
                    oldIdBranchOfEmployee.setEmployee(null);
                    oldIdBranchOfEmployee = em.merge(oldIdBranchOfEmployee);
                }
                employee.setIdBranch(branch);
                employee = em.merge(employee);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Branch branch) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Branch persistentBranch = em.find(Branch.class, branch.getId());
            Employee employeeOld = persistentBranch.getEmployee();
            Employee employeeNew = branch.getEmployee();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getId());
                branch.setEmployee(employeeNew);
            }
            branch = em.merge(branch);
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.setIdBranch(null);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                Branch oldIdBranchOfEmployee = employeeNew.getIdBranch();
                if (oldIdBranchOfEmployee != null) {
                    oldIdBranchOfEmployee.setEmployee(null);
                    oldIdBranchOfEmployee = em.merge(oldIdBranchOfEmployee);
                }
                employeeNew.setIdBranch(branch);
                employeeNew = em.merge(employeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = branch.getId();
                if (findBranch(id) == null) {
                    throw new NonexistentEntityException("The branch with id " + id + " no longer exists.");
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
            Branch branch;
            try {
                branch = em.getReference(Branch.class, id);
                branch.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The branch with id " + id + " no longer exists.", enfe);
            }
            Employee employee = branch.getEmployee();
            if (employee != null) {
                employee.setIdBranch(null);
                employee = em.merge(employee);
            }
            em.remove(branch);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Branch> findBranchEntities() {
        return findBranchEntities(true, -1, -1);
    }

    public List<Branch> findBranchEntities(int maxResults, int firstResult) {
        return findBranchEntities(false, maxResults, firstResult);
    }

    private List<Branch> findBranchEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Branch.class));
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

    public Branch findBranch(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Branch.class, id);
        } finally {
            em.close();
        }
    }

    public int getBranchCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Branch> rt = cq.from(Branch.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
