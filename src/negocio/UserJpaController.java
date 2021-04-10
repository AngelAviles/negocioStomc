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
import dominio.Turn;
import dominio.User;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class UserJpaController implements Serializable {

    public UserJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(User user) {
        if (user.getTurnList() == null) {
            user.setTurnList(new ArrayList<Turn>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee idEmployee = user.getIdEmployee();
            if (idEmployee != null) {
                idEmployee = em.getReference(idEmployee.getClass(), idEmployee.getId());
                user.setIdEmployee(idEmployee);
            }
            Profile idProfile = user.getIdProfile();
            if (idProfile != null) {
                idProfile = em.getReference(idProfile.getClass(), idProfile.getId());
                user.setIdProfile(idProfile);
            }
            List<Turn> attachedTurnList = new ArrayList<Turn>();
            for (Turn turnListTurnToAttach : user.getTurnList()) {
                turnListTurnToAttach = em.getReference(turnListTurnToAttach.getClass(), turnListTurnToAttach.getUuid());
                attachedTurnList.add(turnListTurnToAttach);
            }
            user.setTurnList(attachedTurnList);
            em.persist(user);
            if (idEmployee != null) {
                User oldUserOfIdEmployee = idEmployee.getUser();
                if (oldUserOfIdEmployee != null) {
                    oldUserOfIdEmployee.setIdEmployee(null);
                    oldUserOfIdEmployee = em.merge(oldUserOfIdEmployee);
                }
                idEmployee.setUser(user);
                idEmployee = em.merge(idEmployee);
            }
            if (idProfile != null) {
                User oldUserOfIdProfile = idProfile.getUser();
                if (oldUserOfIdProfile != null) {
                    oldUserOfIdProfile.setIdProfile(null);
                    oldUserOfIdProfile = em.merge(oldUserOfIdProfile);
                }
                idProfile.setUser(user);
                idProfile = em.merge(idProfile);
            }
            for (Turn turnListTurn : user.getTurnList()) {
                User oldIdUserOfTurnListTurn = turnListTurn.getIdUser();
                turnListTurn.setIdUser(user);
                turnListTurn = em.merge(turnListTurn);
                if (oldIdUserOfTurnListTurn != null) {
                    oldIdUserOfTurnListTurn.getTurnList().remove(turnListTurn);
                    oldIdUserOfTurnListTurn = em.merge(oldIdUserOfTurnListTurn);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(User user) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            User persistentUser = em.find(User.class, user.getId());
            Employee idEmployeeOld = persistentUser.getIdEmployee();
            Employee idEmployeeNew = user.getIdEmployee();
            Profile idProfileOld = persistentUser.getIdProfile();
            Profile idProfileNew = user.getIdProfile();
            List<Turn> turnListOld = persistentUser.getTurnList();
            List<Turn> turnListNew = user.getTurnList();
            if (idEmployeeNew != null) {
                idEmployeeNew = em.getReference(idEmployeeNew.getClass(), idEmployeeNew.getId());
                user.setIdEmployee(idEmployeeNew);
            }
            if (idProfileNew != null) {
                idProfileNew = em.getReference(idProfileNew.getClass(), idProfileNew.getId());
                user.setIdProfile(idProfileNew);
            }
            List<Turn> attachedTurnListNew = new ArrayList<Turn>();
            for (Turn turnListNewTurnToAttach : turnListNew) {
                turnListNewTurnToAttach = em.getReference(turnListNewTurnToAttach.getClass(), turnListNewTurnToAttach.getUuid());
                attachedTurnListNew.add(turnListNewTurnToAttach);
            }
            turnListNew = attachedTurnListNew;
            user.setTurnList(turnListNew);
            user = em.merge(user);
            if (idEmployeeOld != null && !idEmployeeOld.equals(idEmployeeNew)) {
                idEmployeeOld.setUser(null);
                idEmployeeOld = em.merge(idEmployeeOld);
            }
            if (idEmployeeNew != null && !idEmployeeNew.equals(idEmployeeOld)) {
                User oldUserOfIdEmployee = idEmployeeNew.getUser();
                if (oldUserOfIdEmployee != null) {
                    oldUserOfIdEmployee.setIdEmployee(null);
                    oldUserOfIdEmployee = em.merge(oldUserOfIdEmployee);
                }
                idEmployeeNew.setUser(user);
                idEmployeeNew = em.merge(idEmployeeNew);
            }
            if (idProfileOld != null && !idProfileOld.equals(idProfileNew)) {
                idProfileOld.setUser(null);
                idProfileOld = em.merge(idProfileOld);
            }
            if (idProfileNew != null && !idProfileNew.equals(idProfileOld)) {
                User oldUserOfIdProfile = idProfileNew.getUser();
                if (oldUserOfIdProfile != null) {
                    oldUserOfIdProfile.setIdProfile(null);
                    oldUserOfIdProfile = em.merge(oldUserOfIdProfile);
                }
                idProfileNew.setUser(user);
                idProfileNew = em.merge(idProfileNew);
            }
            for (Turn turnListOldTurn : turnListOld) {
                if (!turnListNew.contains(turnListOldTurn)) {
                    turnListOldTurn.setIdUser(null);
                    turnListOldTurn = em.merge(turnListOldTurn);
                }
            }
            for (Turn turnListNewTurn : turnListNew) {
                if (!turnListOld.contains(turnListNewTurn)) {
                    User oldIdUserOfTurnListNewTurn = turnListNewTurn.getIdUser();
                    turnListNewTurn.setIdUser(user);
                    turnListNewTurn = em.merge(turnListNewTurn);
                    if (oldIdUserOfTurnListNewTurn != null && !oldIdUserOfTurnListNewTurn.equals(user)) {
                        oldIdUserOfTurnListNewTurn.getTurnList().remove(turnListNewTurn);
                        oldIdUserOfTurnListNewTurn = em.merge(oldIdUserOfTurnListNewTurn);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Long id = user.getId();
                if (findUser(id) == null) {
                    throw new NonexistentEntityException("The user with id " + id + " no longer exists.");
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
            User user;
            try {
                user = em.getReference(User.class, id);
                user.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The user with id " + id + " no longer exists.", enfe);
            }
            Employee idEmployee = user.getIdEmployee();
            if (idEmployee != null) {
                idEmployee.setUser(null);
                idEmployee = em.merge(idEmployee);
            }
            Profile idProfile = user.getIdProfile();
            if (idProfile != null) {
                idProfile.setUser(null);
                idProfile = em.merge(idProfile);
            }
            List<Turn> turnList = user.getTurnList();
            for (Turn turnListTurn : turnList) {
                turnListTurn.setIdUser(null);
                turnListTurn = em.merge(turnListTurn);
            }
            em.remove(user);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<User> findUserEntities() {
        return findUserEntities(true, -1, -1);
    }

    public List<User> findUserEntities(int maxResults, int firstResult) {
        return findUserEntities(false, maxResults, firstResult);
    }

    private List<User> findUserEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(User.class));
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

    public User findUser(Long id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(User.class, id);
        } finally {
            em.close();
        }
    }

    public int getUserCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<User> rt = cq.from(User.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
