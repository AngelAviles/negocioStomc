/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.Branch;
import java.util.List;
import negocio.BranchJpaController;
import negocio.EntityManagerFactoryBase;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ControlBranch {
    
    private BranchJpaController getJpa() {
        return new BranchJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(Branch branch) {
        getJpa().create(branch);
    }
    
    public void edit(Branch branch) throws NonexistentEntityException, Exception {
        getJpa().edit(branch);
    }
    
    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }
    
    public Branch findBranch(Long id) throws Exception {
        return getJpa().findBranch(id);
    }
    
    public List<Branch> findBranchByFolio(Long folio) throws Exception {
        return getJpa().findBranchByFolio(folio);
    }
    
    public List<Branch> findBranchByBranchName(String branchName) throws Exception {
        return getJpa().findBranchByBranchName(branchName);
    }
    
    public List<Branch> findBranchEntities() {
        return getJpa().findBranchEntities();
    }
    
    public List<Branch> findBranchEntities(int maxResults, int firstResult) {
        return getJpa().findBranchEntities(maxResults, firstResult);
    }
    
    public int getBranchCount() {
        return getJpa().getBranchCount();
    }
    
}
