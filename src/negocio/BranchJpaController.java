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
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class BranchJpaController implements Serializable {

    public BranchJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public BranchJpaController() {
        this.emf = Persistence.createEntityManagerFactory("stomcPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Branch branch) {
        if (branch.getEmployeeList() == null) {
            branch.setEmployeeList(new ArrayList<Employee>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Employee> attachedEmployeeList = new ArrayList<Employee>();
            for (Employee employeeListEmployeeToAttach : branch.getEmployeeList()) {
                employeeListEmployeeToAttach = em.getReference(employeeListEmployeeToAttach.getClass(), employeeListEmployeeToAttach.getId());
                attachedEmployeeList.add(employeeListEmployeeToAttach);
            }
            branch.setEmployeeList(attachedEmployeeList);
            em.persist(branch);
            for (Employee employeeListEmployee : branch.getEmployeeList()) {
                Branch oldIdBranchOfEmployeeListEmployee = employeeListEmployee.getIdBranch();
                employeeListEmployee.setIdBranch(branch);
                employeeListEmployee = em.merge(employeeListEmployee);
                if (oldIdBranchOfEmployeeListEmployee != null) {
                    oldIdBranchOfEmployeeListEmployee.getEmployeeList().remove(employeeListEmployee);
                    oldIdBranchOfEmployeeListEmployee = em.merge(oldIdBranchOfEmployeeListEmployee);
                }
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
            List<Employee> employeeListOld = persistentBranch.getEmployeeList();
            List<Employee> employeeListNew = branch.getEmployeeList();
            List<Employee> attachedEmployeeListNew = new ArrayList<Employee>();
            for (Employee employeeListNewEmployeeToAttach : employeeListNew) {
                employeeListNewEmployeeToAttach = em.getReference(employeeListNewEmployeeToAttach.getClass(), employeeListNewEmployeeToAttach.getId());
                attachedEmployeeListNew.add(employeeListNewEmployeeToAttach);
            }
            employeeListNew = attachedEmployeeListNew;
            branch.setEmployeeList(employeeListNew);
            branch = em.merge(branch);
            for (Employee employeeListOldEmployee : employeeListOld) {
                if (!employeeListNew.contains(employeeListOldEmployee)) {
                    employeeListOldEmployee.setIdBranch(null);
                    employeeListOldEmployee = em.merge(employeeListOldEmployee);
                }
            }
            for (Employee employeeListNewEmployee : employeeListNew) {
                if (!employeeListOld.contains(employeeListNewEmployee)) {
                    Branch oldIdBranchOfEmployeeListNewEmployee = employeeListNewEmployee.getIdBranch();
                    employeeListNewEmployee.setIdBranch(branch);
                    employeeListNewEmployee = em.merge(employeeListNewEmployee);
                    if (oldIdBranchOfEmployeeListNewEmployee != null && !oldIdBranchOfEmployeeListNewEmployee.equals(branch)) {
                        oldIdBranchOfEmployeeListNewEmployee.getEmployeeList().remove(employeeListNewEmployee);
                        oldIdBranchOfEmployeeListNewEmployee = em.merge(oldIdBranchOfEmployeeListNewEmployee);
                    }
                }
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
            List<Employee> employeeList = branch.getEmployeeList();
            for (Employee employeeListEmployee : employeeList) {
                employeeListEmployee.setIdBranch(null);
                employeeListEmployee = em.merge(employeeListEmployee);
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
