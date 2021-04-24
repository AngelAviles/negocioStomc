/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.AttentionPoint;
import java.util.List;
import negocio.AttentionPointJpaController;
import negocio.EntityManagerFactoryBase;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ControlAttentionPoint {
    
    private AttentionPointJpaController getJpa() {
        return new AttentionPointJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(AttentionPoint attentionPoint) {
        getJpa().create(attentionPoint);
    }
    
    public void edit(AttentionPoint attentionPoint) throws NonexistentEntityException, Exception {
        getJpa().edit(attentionPoint);
    }
    
    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }
    
    public AttentionPoint findAttentionPoint(Long id) throws Exception {
        return getJpa().findAttentionPoint(id);
    }
    
    public List<AttentionPoint> findAttentionPointByFolio(Long folio) throws Exception {
        return getJpa().findAttentionPointByFolio(folio);
    }
    
    public List<AttentionPoint> findAttentionPoint(String point) throws Exception {
        return getJpa().findAttentionPointByPoint(point);
    }
    
    public List<AttentionPoint> findAttentionPointEntities() {
        return getJpa().findAttentionPointEntities();
    }
    
    public List<AttentionPoint> findAttentionPointEntities(int maxResults, int firstResult) {
        return getJpa().findAttentionPointEntities(maxResults, firstResult);
    }
    
    public int getAttentionPointCount() {
        return getJpa().getAttentionPointCount();
    }
    
}
