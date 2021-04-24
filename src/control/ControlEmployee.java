/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.Employee;
import java.util.List;
import negocio.EntityManagerFactoryBase;
import negocio.EmployeeJpaController;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class ControlEmployee {
    
    private EmployeeJpaController getJpa() {
        return new EmployeeJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }

    public void create(Employee employee) throws PreexistingEntityException {
        getJpa().create(employee);
    }

    public void edit(Employee employee) throws NonexistentEntityException, Exception {
        getJpa().edit(employee);
    }

    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }

    public Employee findEmployee(Long id) throws Exception {
        return getJpa().findEmployee(id);
    }

    public List<Employee> findEmployeeByFolio(Long folio) throws Exception {
        return getJpa().findEmployeeByFolio(folio);
    }

    public List<Employee> findEmployeeByName(String name) throws Exception {
        return getJpa().findEmployeeByName(name);
    }
    
    public List<Employee> findEmployeeByNoEmployee(Long noEmployee) throws Exception {
        return getJpa().findEmployeeByNoEmployee(noEmployee);
    }
    
    public List<Employee> findEmployeeByAddress(String address) throws Exception {
        return getJpa().findEmployeeByAddress(address);
    }
    
    public List<Employee> findEmployeeByDepartment(String department) throws Exception {
        return getJpa().findEmployeeByDepartment(department);
    }
    
    public List<Employee> findEmployeeByaccountAndPassword(String account, String password) throws Exception {
        return getJpa().findEmployeeByaccountAndPassword(account, password);
    }

    public List<Employee> findEmployeeEntities() {
        return getJpa().findEmployeeEntities();
    }

    public List<Employee> findEmployeeEntities(int maxResults, int firstResult) {
        return getJpa().findEmployeeEntities(maxResults, firstResult);
    }

    public int getEmployeeCount() {
        return getJpa().getEmployeeCount();
    }

}
