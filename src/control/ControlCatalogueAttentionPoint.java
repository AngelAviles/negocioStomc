/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.CatalogueAttentionPoint;
import java.util.List;
import negocio.CatalogueAttentionPointJpaController;
import negocio.EntityManagerFactoryBase;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class ControlCatalogueAttentionPoint {
    
    private CatalogueAttentionPointJpaController getJpa() {
        return new CatalogueAttentionPointJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(CatalogueAttentionPoint catalogueAttentionPoint) throws PreexistingEntityException {
        getJpa().create(catalogueAttentionPoint);
    }
    
    public void edit(CatalogueAttentionPoint catalogueAttentionPoint) throws NonexistentEntityException, PreexistingEntityException, Exception {
        getJpa().edit(catalogueAttentionPoint);
    }
    
    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }
    
    public CatalogueAttentionPoint findCatalogueAttentionPoint(Long id) throws Exception {
        return getJpa().findCatalogueAttentionPoint(id);
    }
    
    public List<CatalogueAttentionPoint> findCatalogueAttentionPointByFolio(Long folio) throws Exception {
        return getJpa().findCatalogueAttentionPointByFolio(folio);
    }
    
    public List<CatalogueAttentionPoint> findCatalogueAttentionPoint(String point) throws Exception {
        return getJpa().findCatalogueAttentionPointByPoint(point);
    }
    
    public List<CatalogueAttentionPoint> findCatalogueAttentionPointEntities() {
        return getJpa().findCatalogueAttentionPointEntities();
    }
    
    public List<CatalogueAttentionPoint> findCatalogueAttentionPointEntities(int maxResults, int firstResult) {
        return getJpa().findCatalogueAttentionPointEntities(maxResults, firstResult);
    }
    
    public int getCatalogueAttentionPointCount() {
        return getJpa().getCatalogueAttentionPointCount();
    }
}
