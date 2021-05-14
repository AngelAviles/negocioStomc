/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.CatalogueBranch;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class CatalogueBranchJpaController implements Serializable {

    public CatalogueBranchJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CatalogueBranch catalogueBranch) throws PreexistingEntityException {
        
        List<CatalogueBranch> catalogueBranchs = null;

        try {
            catalogueBranchs = findCatalogueBranchByBranchName(catalogueBranch.getBranchName());
        } catch (Exception ex) {
            Logger.getLogger(CatalogueBranch.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (catalogueBranchs.isEmpty()) {
        
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.persist(catalogueBranch);
                em.getTransaction().commit();
            } finally {
                if (em != null) {
                    em.close();
                }
            }
            
        } else {
            throw new PreexistingEntityException("Sucursal con datos repetidos.", catalogueBranchs.get(0));
        }
    }

    public void edit(CatalogueBranch catalogueBranch) throws NonexistentEntityException, PreexistingEntityException, Exception {
        
        List<CatalogueBranch> catalogueBranchs = null;

        try {
            catalogueBranchs = findBranchName_NotId(catalogueBranch.getId(), catalogueBranch.getBranchName());
        } catch (Exception ex) {
            Logger.getLogger(CatalogueBranch.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (catalogueBranchs.isEmpty()) {
        
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                catalogueBranch = em.merge(catalogueBranch);
                em.getTransaction().commit();
            } catch (Exception ex) {
                String msg = ex.getLocalizedMessage();
                if (msg == null || msg.length() == 0) {
                    Long id = catalogueBranch.getId();
                    if (findCatalogueBranch(id) == null) {
                        throw new NonexistentEntityException("The catalogueBranch with id " + id + " no longer exists.");
                    }
                }
                throw ex;
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        } else {
            throw new PreexistingEntityException("Punto de Atención con datos repetidos.", catalogueBranchs.get(0));
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CatalogueBranch catalogueBranch;
            try {
                catalogueBranch = em.getReference(CatalogueBranch.class, id);
                catalogueBranch.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The catalogueBranch with id " + id + " no longer exists.", enfe);
            }
            em.remove(catalogueBranch);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CatalogueBranch> findCatalogueBranchEntities() {
        return findCatalogueBranchEntities(true, -1, -1);
    }

    public List<CatalogueBranch> findCatalogueBranchEntities(int maxResults, int firstResult) {
        return findCatalogueBranchEntities(false, maxResults, firstResult);
    }

    private List<CatalogueBranch> findCatalogueBranchEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CatalogueBranch.class));
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
    
        public List<CatalogueBranch> findCatalogueBranchByFolio(Long folio) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueBranch> consultaSucursal = em.createNamedQuery("CatalogueBranch.findByFolio", CatalogueBranch.class);
        consultaSucursal.setParameter("folio", folio);
        
        List<CatalogueBranch> sucursales = new ArrayList<>();
        
        try {
            sucursales = consultaSucursal.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return sucursales;
    }
    
    public List<CatalogueBranch> findCatalogueBranchByBranchName(String branchName) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueBranch> consultaSucursal = em.createNamedQuery("CatalogueBranch.findByBranchName", CatalogueBranch.class);
        consultaSucursal.setParameter("branchName", branchName);
        
        List<CatalogueBranch> sucursales = new ArrayList<>();
        
        try {
            sucursales = consultaSucursal.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return sucursales;
    }
    
    public List<CatalogueBranch> findCatalogueBranchByAddress(String address) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueBranch> consultaSucursal = em.createNamedQuery("CatalogueBranch.findByAddress", CatalogueBranch.class);
        consultaSucursal.setParameter("address", address);
        
        List<CatalogueBranch> sucursales = new ArrayList<>();
        
        try {
            sucursales = consultaSucursal.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return sucursales;
    }
    
    public List<CatalogueBranch> findBranchName_NotId(Long id, String branchName) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueBranch> consultaSucursal = em.createNamedQuery("FindBranchName_NotId", CatalogueBranch.class);
        consultaSucursal.setParameter("id", id);
        consultaSucursal.setParameter("branchName", branchName);
        
        List<CatalogueBranch> sucursales = new ArrayList<>();
        
        try {
            sucursales = consultaSucursal.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return sucursales;
    }

    public CatalogueBranch findCatalogueBranch(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CatalogueBranch.class, id);
        } finally {
            em.close();
        }
    }

    public int getCatalogueBranchCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CatalogueBranch> rt = cq.from(CatalogueBranch.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
