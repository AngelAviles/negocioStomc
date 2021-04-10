/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import dominio.User;
import dominio.AttentionPoint;
import dominio.Branch;
import dominio.Employee;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class EmployeeJpaController implements Serializable {

    public EmployeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public EmployeeJpaController() {
        this.emf = Persistence.createEntityManagerFactory("stomcPU");
    }
    
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User user = employee.getUser();
            if (user != null) {
                user = em.getReference(user.getClass(), user.getId());
                employee.setUser(user);
            }
            AttentionPoint idAttentionPoint = employee.getIdAttentionPoint();
            if (idAttentionPoint != null) {
                idAttentionPoint = em.getReference(idAttentionPoint.getClass(), idAttentionPoint.getId());
                employee.setIdAttentionPoint(idAttentionPoint);
            }
            Branch idBranch = employee.getIdBranch();
            if (idBranch != null) {
                idBranch = em.getReference(idBranch.getClass(), idBranch.getId());
                employee.setIdBranch(idBranch);
            }
            em.persist(employee);
            if (user != null) {
                Employee oldIdEmployeeOfUser = user.getIdEmployee();
                if (oldIdEmployeeOfUser != null) {
                    oldIdEmployeeOfUser.setUser(null);
                    oldIdEmployeeOfUser = em.merge(oldIdEmployeeOfUser);
                }
                user.setIdEmployee(employee);
                user = em.merge(user);
            }
            if (idAttentionPoint != null) {
                idAttentionPoint.getEmployeeList().add(employee);
                idAttentionPoint = em.merge(idAttentionPoint);
            }
            if (idBranch != null) {
                idBranch.getEmployeeList().add(employee);
                idBranch = em.merge(idBranch);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Employee employee) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee persistentEmployee = em.find(Employee.class, employee.getId());
            User userOld = persistentEmployee.getUser();
            User userNew = employee.getUser();
            AttentionPoint idAttentionPointOld = persistentEmployee.getIdAttentionPoint();
            AttentionPoint idAttentionPointNew = employee.getIdAttentionPoint();
            Branch idBranchOld = persistentEmployee.getIdBranch();
            Branch idBranchNew = employee.getIdBranch();
            if (userNew != null) {
                userNew = em.getReference(userNew.getClass(), userNew.getId());
                employee.setUser(userNew);
            }
            if (idAttentionPointNew != null) {
                idAttentionPointNew = em.getReference(idAttentionPointNew.getClass(), idAttentionPointNew.getId());
                employee.setIdAttentionPoint(idAttentionPointNew);
            }
            if (idBranchNew != null) {
                idBranchNew = em.getReference(idBranchNew.getClass(), idBranchNew.getId());
                employee.setIdBranch(idBranchNew);
            }
            employee = em.merge(employee);
            if (userOld != null && !userOld.equals(userNew)) {
                userOld.setIdEmployee(null);
                userOld = em.merge(userOld);
            }
            if (userNew != null && !userNew.equals(userOld)) {
                Employee oldIdEmployeeOfUser = userNew.getIdEmployee();
                if (oldIdEmployeeOfUser != null) {
                    oldIdEmployeeOfUser.setUser(null);
                    oldIdEmployeeOfUser = em.merge(oldIdEmployeeOfUser);
                }
                userNew.setIdEmployee(employee);
                userNew = em.merge(userNew);
            }
            if (idAttentionPointOld != null && !idAttentionPointOld.equals(idAttentionPointNew)) {
                idAttentionPointOld.getEmployeeList().remove(employee);
                idAttentionPointOld = em.merge(idAttentionPointOld);
            }
            if (idAttentionPointNew != null && !idAttentionPointNew.equals(idAttentionPointOld)) {
                idAttentionPointNew.getEmployeeList().add(employee);
                idAttentionPointNew = em.merge(idAttentionPointNew);
            }
            if (idBranchOld != null && !idBranchOld.equals(idBranchNew)) {
                idBranchOld.getEmployeeList().remove(employee);
                idBranchOld = em.merge(idBranchOld);
            }
            if (idBranchNew != null && !idBranchNew.equals(idBranchOld)) {
                idBranchNew.getEmployeeList().add(employee);
                idBranchNew = em.merge(idBranchNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = employee.getId();
                if (findEmployee(id) == null) {
                    throw new NonexistentEntityException("The employee with id " + id + " no longer exists.");
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
            Employee employee;
            try {
                employee = em.getReference(Employee.class, id);
                employee.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The employee with id " + id + " no longer exists.", enfe);
            }
            User user = employee.getUser();
            if (user != null) {
                user.setIdEmployee(null);
                user = em.merge(user);
            }
            AttentionPoint idAttentionPoint = employee.getIdAttentionPoint();
            if (idAttentionPoint != null) {
                idAttentionPoint.getEmployeeList().remove(employee);
                idAttentionPoint = em.merge(idAttentionPoint);
            }
            Branch idBranch = employee.getIdBranch();
            if (idBranch != null) {
                idBranch.getEmployeeList().remove(employee);
                idBranch = em.merge(idBranch);
            }
            em.remove(employee);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Employee> findEmployeeEntities() {
        return findEmployeeEntities(true, -1, -1);
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return findEmployeeEntities(false, maxResults, firstResult);
    }

    private List<Employee> findEmployeeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Employee.class));
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

    public Employee findEmployee(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Employee.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmployeeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Employee> rt = cq.from(Employee.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
