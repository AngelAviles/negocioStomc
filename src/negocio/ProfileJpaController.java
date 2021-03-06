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
import dominio.Employee;
import dominio.Profile;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ProfileJpaController implements Serializable {

    public ProfileJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Profile profile) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee employee = profile.getEmployee();
//            if (employee != null) {
//                employee = em.getReference(employee.getClass(), employee.getId());
//                profile.setEmployee(employee);
//            }
            em.persist(profile);
            if (employee != null) {
                Profile oldIdProfileOfEmployee = employee.getIdProfile();
                if (oldIdProfileOfEmployee != null) {
                    oldIdProfileOfEmployee.setEmployee(null);
                    oldIdProfileOfEmployee = em.merge(oldIdProfileOfEmployee);
                }
                employee.setIdProfile(profile);
                employee = em.merge(employee);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Profile profile) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Profile persistentProfile = em.find(Profile.class, profile.getId());
            Employee employeeOld = persistentProfile.getEmployee();
            Employee employeeNew = profile.getEmployee();
            if (employeeNew != null) {
                employeeNew = em.getReference(employeeNew.getClass(), employeeNew.getId());
                profile.setEmployee(employeeNew);
            }
            profile = em.merge(profile);
            if (employeeOld != null && !employeeOld.equals(employeeNew)) {
                employeeOld.setIdProfile(null);
                employeeOld = em.merge(employeeOld);
            }
            if (employeeNew != null && !employeeNew.equals(employeeOld)) {
                Profile oldIdProfileOfEmployee = employeeNew.getIdProfile();
                if (oldIdProfileOfEmployee != null) {
                    oldIdProfileOfEmployee.setEmployee(null);
                    oldIdProfileOfEmployee = em.merge(oldIdProfileOfEmployee);
                }
                employeeNew.setIdProfile(profile);
                employeeNew = em.merge(employeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = profile.getId();
                if (findProfile(id) == null) {
                    throw new NonexistentEntityException("The profile with id " + id + " no longer exists.");
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
            Profile profile;
            try {
                profile = em.getReference(Profile.class, id);
                profile.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The profile with id " + id + " no longer exists.", enfe);
            }
            Employee employee = profile.getEmployee();
            if (employee != null) {
                employee.setIdProfile(null);
                employee = em.merge(employee);
            }
            em.remove(profile);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Profile> findProfileEntities() {
        return findProfileEntities(true, -1, -1);
    }

    public List<Profile> findProfileEntities(int maxResults, int firstResult) {
        return findProfileEntities(false, maxResults, firstResult);
    }

    private List<Profile> findProfileEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Profile.class));
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
    
    public List<Profile> findProfileByFolio(Long folio) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<Profile> consultaPerfil = em.createNamedQuery("Profile.findByFolio", Profile.class);
        consultaPerfil.setParameter("folio", folio);
        
        List<Profile> perfiles = new ArrayList<>();
        
        try {
            perfiles = consultaPerfil.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return perfiles;
    }
    
    public List<Profile> findProfileByProfileName(String profileName) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<Profile> consultaPerfil = em.createNamedQuery("Profile.findByProfileName", Profile.class);
        consultaPerfil.setParameter("profileName", profileName);
        
        List<Profile> perfiles = new ArrayList<>();
        
        try {
            perfiles = consultaPerfil.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return perfiles;
    }

    public Profile findProfile(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Profile.class, id);
        } finally {
            em.close();
        }
    }

    public int getProfileCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Profile> rt = cq.from(Profile.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
