/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.CatalogueBranch;
import java.util.List;
import negocio.CatalogueBranchJpaController;
import negocio.EntityManagerFactoryBase;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class ControlCatalogueBranch {
    
    private CatalogueBranchJpaController getJpa() {
        return new CatalogueBranchJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(CatalogueBranch catalogueBranch) throws PreexistingEntityException {
        getJpa().create(catalogueBranch);
    }
    
    public void edit(CatalogueBranch catalogueBranch) throws NonexistentEntityException, PreexistingEntityException, Exception {
        getJpa().edit(catalogueBranch);
    }
    
    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }
    
    public CatalogueBranch findCatalogueBranch(Long id) throws Exception {
        return getJpa().findCatalogueBranch(id);
    }
    
    public List<CatalogueBranch> findCatalogueBranchByFolio(Long folio) throws Exception {
        return getJpa().findCatalogueBranchByFolio(folio);
    }
    
    public List<CatalogueBranch> findCatalogueBranchByBranchName(String branchName) throws Exception {
        return getJpa().findCatalogueBranchByBranchName(branchName);
    }
    
    public List<CatalogueBranch> findCatalogueBranchByAddress(String address) throws Exception {
        return getJpa().findCatalogueBranchByAddress(address);
    }
    
    public List<CatalogueBranch> findCatalogueBranchEntities() {
        return getJpa().findCatalogueBranchEntities();
    }
    
    public List<CatalogueBranch> findCatalogueBranchEntities(int maxResults, int firstResult) {
        return getJpa().findCatalogueBranchEntities(maxResults, firstResult);
    }
    
    public int getCatalogueBranchCount() {
        return getJpa().getCatalogueBranchCount();
    }
    
}
