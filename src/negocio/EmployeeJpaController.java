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
import dominio.AttentionPoint;
import dominio.Branch;
import dominio.Employee;
import dominio.Profile;
import dominio.Turn;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class EmployeeJpaController implements Serializable {

    public EmployeeJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Employee employee) throws PreexistingEntityException {

        List<Employee> empleados = null;

        try {
            empleados = findEmployeeComprobarValoresUnicos(employee.getNoEmployee(), employee.getAccount());
        } catch (Exception ex) {
            Logger.getLogger(EmployeeJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (empleados.isEmpty()) {

            if (employee.getTurnList() == null) {
                employee.setTurnList(new ArrayList<Turn>());
            }
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                AttentionPoint idAttentionPoint = employee.getIdAttentionPoint();
//            if (idAttentionPoint != null) {
//                idAttentionPoint = em.getReference(idAttentionPoint.getClass(), idAttentionPoint.getId());
//                employee.setIdAttentionPoint(idAttentionPoint);
//            }
                Branch idBranch = employee.getIdBranch();
//            if (idBranch != null) {
//                idBranch = em.getReference(idBranch.getClass(), idBranch.getId());
//                employee.setIdBranch(idBranch);
//            }
                Profile idProfile = employee.getIdProfile();
//            if (idProfile != null) {
//                idProfile = em.getReference(idProfile.getClass(), idProfile.getId());
//                employee.setIdProfile(idProfile);
//            }
                List<Turn> attachedTurnList = new ArrayList<Turn>();
                for (Turn turnListTurnToAttach : employee.getTurnList()) {
                    turnListTurnToAttach = em.getReference(turnListTurnToAttach.getClass(), turnListTurnToAttach.getUuid());
                    attachedTurnList.add(turnListTurnToAttach);
                }
                employee.setTurnList(attachedTurnList);
                em.persist(employee);
                if (idAttentionPoint != null) {
                    Employee oldEmployeeOfIdAttentionPoint = idAttentionPoint.getEmployee();
                    if (oldEmployeeOfIdAttentionPoint != null) {
                        oldEmployeeOfIdAttentionPoint.setIdAttentionPoint(null);
                        oldEmployeeOfIdAttentionPoint = em.merge(oldEmployeeOfIdAttentionPoint);
                    }
                    idAttentionPoint.setEmployee(employee);
                    idAttentionPoint = em.merge(idAttentionPoint);
                }
                if (idBranch != null) {
                    Employee oldEmployeeOfIdBranch = idBranch.getEmployee();
                    if (oldEmployeeOfIdBranch != null) {
                        oldEmployeeOfIdBranch.setIdBranch(null);
                        oldEmployeeOfIdBranch = em.merge(oldEmployeeOfIdBranch);
                    }
                    idBranch.setEmployee(employee);
                    idBranch = em.merge(idBranch);
                }
                if (idProfile != null) {
                    Employee oldEmployeeOfIdProfile = idProfile.getEmployee();
                    if (oldEmployeeOfIdProfile != null) {
                        oldEmployeeOfIdProfile.setIdProfile(null);
                        oldEmployeeOfIdProfile = em.merge(oldEmployeeOfIdProfile);
                    }
                    idProfile.setEmployee(employee);
                    idProfile = em.merge(idProfile);
                }
                for (Turn turnListTurn : employee.getTurnList()) {
                    Employee oldIdEmployeeOfTurnListTurn = turnListTurn.getIdEmployee();
                    turnListTurn.setIdEmployee(employee);
                    turnListTurn = em.merge(turnListTurn);
                    if (oldIdEmployeeOfTurnListTurn != null) {
                        oldIdEmployeeOfTurnListTurn.getTurnList().remove(turnListTurn);
                        oldIdEmployeeOfTurnListTurn = em.merge(oldIdEmployeeOfTurnListTurn);
                    }
                }
                em.getTransaction().commit();
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        } else {
            throw new PreexistingEntityException("Empleado con datos repetidos.", empleados.get(0));
        }
    }

    public void edit(Employee employee) throws NonexistentEntityException, PreexistingEntityException, Exception {

        List<Employee> empleados = null;

        try {
            empleados = findEmployeeByAccount(employee.getAccount());
        } catch (Exception ex) {
            Logger.getLogger(EmployeeJpaController.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (empleados.isEmpty()) {

            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                Employee persistentEmployee = em.find(Employee.class, employee.getId());
                AttentionPoint idAttentionPointOld = persistentEmployee.getIdAttentionPoint();
                AttentionPoint idAttentionPointNew = employee.getIdAttentionPoint();
                Branch idBranchOld = persistentEmployee.getIdBranch();
                Branch idBranchNew = employee.getIdBranch();
                Profile idProfileOld = persistentEmployee.getIdProfile();
                Profile idProfileNew = employee.getIdProfile();
                List<Turn> turnListOld = persistentEmployee.getTurnList();
                List<Turn> turnListNew = employee.getTurnList();
                if (idAttentionPointNew != null) {
                    idAttentionPointNew = em.getReference(idAttentionPointNew.getClass(), idAttentionPointNew.getId());
                    employee.setIdAttentionPoint(idAttentionPointNew);
                }
                if (idBranchNew != null) {
                    idBranchNew = em.getReference(idBranchNew.getClass(), idBranchNew.getId());
                    employee.setIdBranch(idBranchNew);
                }
                if (idProfileNew != null) {
                    idProfileNew = em.getReference(idProfileNew.getClass(), idProfileNew.getId());
                    employee.setIdProfile(idProfileNew);
                }
                List<Turn> attachedTurnListNew = new ArrayList<Turn>();
                for (Turn turnListNewTurnToAttach : turnListNew) {
                    turnListNewTurnToAttach = em.getReference(turnListNewTurnToAttach.getClass(), turnListNewTurnToAttach.getUuid());
                    attachedTurnListNew.add(turnListNewTurnToAttach);
                }
                turnListNew = attachedTurnListNew;
                employee.setTurnList(turnListNew);
                employee = em.merge(employee);
                if (idAttentionPointOld != null && !idAttentionPointOld.equals(idAttentionPointNew)) {
                    idAttentionPointOld.setEmployee(null);
                    idAttentionPointOld = em.merge(idAttentionPointOld);
                }
                if (idAttentionPointNew != null && !idAttentionPointNew.equals(idAttentionPointOld)) {
                    Employee oldEmployeeOfIdAttentionPoint = idAttentionPointNew.getEmployee();
                    if (oldEmployeeOfIdAttentionPoint != null) {
                        oldEmployeeOfIdAttentionPoint.setIdAttentionPoint(null);
                        oldEmployeeOfIdAttentionPoint = em.merge(oldEmployeeOfIdAttentionPoint);
                    }
                    idAttentionPointNew.setEmployee(employee);
                    idAttentionPointNew = em.merge(idAttentionPointNew);
                }
                if (idBranchOld != null && !idBranchOld.equals(idBranchNew)) {
                    idBranchOld.setEmployee(null);
                    idBranchOld = em.merge(idBranchOld);
                }
                if (idBranchNew != null && !idBranchNew.equals(idBranchOld)) {
                    Employee oldEmployeeOfIdBranch = idBranchNew.getEmployee();
                    if (oldEmployeeOfIdBranch != null) {
                        oldEmployeeOfIdBranch.setIdBranch(null);
                        oldEmployeeOfIdBranch = em.merge(oldEmployeeOfIdBranch);
                    }
                    idBranchNew.setEmployee(employee);
                    idBranchNew = em.merge(idBranchNew);
                }
                if (idProfileOld != null && !idProfileOld.equals(idProfileNew)) {
                    idProfileOld.setEmployee(null);
                    idProfileOld = em.merge(idProfileOld);
                }
                if (idProfileNew != null && !idProfileNew.equals(idProfileOld)) {
                    Employee oldEmployeeOfIdProfile = idProfileNew.getEmployee();
                    if (oldEmployeeOfIdProfile != null) {
                        oldEmployeeOfIdProfile.setIdProfile(null);
                        oldEmployeeOfIdProfile = em.merge(oldEmployeeOfIdProfile);
                    }
                    idProfileNew.setEmployee(employee);
                    idProfileNew = em.merge(idProfileNew);
                }
                for (Turn turnListOldTurn : turnListOld) {
                    if (!turnListNew.contains(turnListOldTurn)) {
                        turnListOldTurn.setIdEmployee(null);
                        turnListOldTurn = em.merge(turnListOldTurn);
                    }
                }
                for (Turn turnListNewTurn : turnListNew) {
                    if (!turnListOld.contains(turnListNewTurn)) {
                        Employee oldIdEmployeeOfTurnListNewTurn = turnListNewTurn.getIdEmployee();
                        turnListNewTurn.setIdEmployee(employee);
                        turnListNewTurn = em.merge(turnListNewTurn);
                        if (oldIdEmployeeOfTurnListNewTurn != null && !oldIdEmployeeOfTurnListNewTurn.equals(employee)) {
                            oldIdEmployeeOfTurnListNewTurn.getTurnList().remove(turnListNewTurn);
                            oldIdEmployeeOfTurnListNewTurn = em.merge(oldIdEmployeeOfTurnListNewTurn);
                        }
                    }
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
        } else {
            throw new PreexistingEntityException("Empleado con datos repetidos.", empleados.get(0));
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
            AttentionPoint idAttentionPoint = employee.getIdAttentionPoint();
            if (idAttentionPoint != null) {
                idAttentionPoint.setEmployee(null);
                idAttentionPoint = em.merge(idAttentionPoint);
            }
            Branch idBranch = employee.getIdBranch();
            if (idBranch != null) {
                idBranch.setEmployee(null);
                idBranch = em.merge(idBranch);
            }
            Profile idProfile = employee.getIdProfile();
            if (idProfile != null) {
                idProfile.setEmployee(null);
                idProfile = em.merge(idProfile);
            }
            List<Turn> turnList = employee.getTurnList();
            for (Turn turnListTurn : turnList) {
                turnListTurn.setIdEmployee(null);
                turnListTurn = em.merge(turnListTurn);
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

    public List<Employee> findEmployeeByaccountAndPassword(String account, String password) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.login", Employee.class);
        consultaEmpleado.setParameter("account", account);
        consultaEmpleado.setParameter("password", password);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }

    public List<Employee> findEmployeeComprobarValoresUnicos(Long noEmployee, String account) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.comprobarValoresUnicos", Employee.class);
        consultaEmpleado.setParameter("account", account);
        consultaEmpleado.setParameter("noEmployee", noEmployee);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }

    public List<Employee> findEmployeeByFolio(Long folio) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.findByFolio", Employee.class);
        consultaEmpleado.setParameter("folio", folio);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }

    public List<Employee> findEmployeeByNoEmployee(Long noEmployee) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.findByNoEmployee", Employee.class);
        consultaEmpleado.setParameter("noEmployee", noEmployee);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }

    public List<Employee> findEmployeeByName(String name) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.findByName", Employee.class);
        consultaEmpleado.setParameter("name", name);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }

    public List<Employee> findEmployeeByAddress(String address) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.findByAddress", Employee.class);
        consultaEmpleado.setParameter("address", address);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }

    public List<Employee> findEmployeeByDepartment(String department) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.findByDepartment", Employee.class);
        consultaEmpleado.setParameter("department", department);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
    }
    
    public List<Employee> findEmployeeByAccount(String account) throws Exception {
        EntityManager em = getEntityManager();

        TypedQuery<Employee> consultaEmpleado = em.createNamedQuery("Employee.findByAccount", Employee.class);
        consultaEmpleado.setParameter("account", account);

        List<Employee> empleados = new ArrayList<>();

        try {
            empleados = consultaEmpleado.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return empleados;
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
