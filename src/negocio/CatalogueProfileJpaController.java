/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import dominio.CatalogueProfile;
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
public class CatalogueProfileJpaController implements Serializable {

    public CatalogueProfileJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(CatalogueProfile catalogueProfile) throws PreexistingEntityException {
        
        List<CatalogueProfile> catalogueProfiles = null;

        try {
            catalogueProfiles = findCatalogueProfileByProfileName(catalogueProfile.getProfileName());
        } catch (Exception ex) {
            Logger.getLogger(CatalogueProfile.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (catalogueProfiles.isEmpty()) {
            
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                em.persist(catalogueProfile);
                em.getTransaction().commit();
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        
        } else {
            throw new PreexistingEntityException("Perfil con datos repetidos.", catalogueProfiles.get(0));
        }
    }

    public void edit(CatalogueProfile catalogueProfile) throws NonexistentEntityException, PreexistingEntityException, Exception {
        
        List<CatalogueProfile> catalogueProfiles = null;

        try {
            catalogueProfiles = findByProfileName_NotId(catalogueProfile.getId(), catalogueProfile.getProfileName());
        } catch (Exception ex) {
            Logger.getLogger(CatalogueProfile.class.getName()).log(Level.SEVERE, null, ex);
        }

        if (catalogueProfiles.isEmpty()) {
        
            EntityManager em = null;
            try {
                em = getEntityManager();
                em.getTransaction().begin();
                catalogueProfile = em.merge(catalogueProfile);
                em.getTransaction().commit();
            } catch (Exception ex) {
                String msg = ex.getLocalizedMessage();
                if (msg == null || msg.length() == 0) {
                    Long id = catalogueProfile.getId();
                    if (findCatalogueProfile(id) == null) {
                        throw new NonexistentEntityException("The catalogueProfile with id " + id + " no longer exists.");
                    }
                }
                throw ex;
            } finally {
                if (em != null) {
                    em.close();
                }
            }
        } else {
            throw new PreexistingEntityException("Perfil con datos repetidos.", catalogueProfiles.get(0));
        }
    }

    public void destroy(Long id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            CatalogueProfile catalogueProfile;
            try {
                catalogueProfile = em.getReference(CatalogueProfile.class, id);
                catalogueProfile.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The catalogueProfile with id " + id + " no longer exists.", enfe);
            }
            em.remove(catalogueProfile);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<CatalogueProfile> findCatalogueProfileEntities() {
        return findCatalogueProfileEntities(true, -1, -1);
    }

    public List<CatalogueProfile> findCatalogueProfileEntities(int maxResults, int firstResult) {
        return findCatalogueProfileEntities(false, maxResults, firstResult);
    }

    private List<CatalogueProfile> findCatalogueProfileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(CatalogueProfile.class));
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
    
        public List<CatalogueProfile> findCatalogueProfileByFolio(Long folio) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueProfile> consultaPerfil = em.createNamedQuery("CatalogueProfile.findByFolio", CatalogueProfile.class);
        consultaPerfil.setParameter("folio", folio);
        
        List<CatalogueProfile> perfiles = new ArrayList<>();
        
        try {
            perfiles = consultaPerfil.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return perfiles;
    }
        
    public List<CatalogueProfile> findCatalogueProfileByProfileName(String profileName) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueProfile> consultaPerfil = em.createNamedQuery("CatalogueProfile.findByProfileName", CatalogueProfile.class);
        consultaPerfil.setParameter("profileName", profileName);
        
        List<CatalogueProfile> perfiles = new ArrayList<>();
        
        try {
            perfiles = consultaPerfil.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return perfiles;
    }
    
    public List<CatalogueProfile> findByProfileName_NotId(Long id, String profileName) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<CatalogueProfile> consultaPerfil = em.createNamedQuery("FindByProfileName_NotId", CatalogueProfile.class);
        consultaPerfil.setParameter("id", id);
        consultaPerfil.setParameter("profileName", profileName);
        
        List<CatalogueProfile> perfiles = new ArrayList<>();
        
        try {
            perfiles = consultaPerfil.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return perfiles;
    }

    public CatalogueProfile findCatalogueProfile(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(CatalogueProfile.class, id);
        } finally {
            em.close();
        }
    }

    public int getCatalogueProfileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<CatalogueProfile> rt = cq.from(CatalogueProfile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
