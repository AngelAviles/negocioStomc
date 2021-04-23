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
import dominio.Turn;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class TurnJpaController implements Serializable {

    public TurnJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Turn turn) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Employee idEmployee = turn.getIdEmployee();
            if (idEmployee != null) {
                idEmployee = em.getReference(idEmployee.getClass(), idEmployee.getId());
                turn.setIdEmployee(idEmployee);
            }
            em.persist(turn);
            if (idEmployee != null) {
                idEmployee.getTurnList().add(turn);
                idEmployee = em.merge(idEmployee);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findTurn(turn.getUuid()) != null) {
                throw new PreexistingEntityException("Turn " + turn + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Turn turn) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turn persistentTurn = em.find(Turn.class, turn.getUuid());
            Employee idEmployeeOld = persistentTurn.getIdEmployee();
            Employee idEmployeeNew = turn.getIdEmployee();
            if (idEmployeeNew != null) {
                idEmployeeNew = em.getReference(idEmployeeNew.getClass(), idEmployeeNew.getId());
                turn.setIdEmployee(idEmployeeNew);
            }
            turn = em.merge(turn);
            if (idEmployeeOld != null && !idEmployeeOld.equals(idEmployeeNew)) {
                idEmployeeOld.getTurnList().remove(turn);
                idEmployeeOld = em.merge(idEmployeeOld);
            }
            if (idEmployeeNew != null && !idEmployeeNew.equals(idEmployeeOld)) {
                idEmployeeNew.getTurnList().add(turn);
                idEmployeeNew = em.merge(idEmployeeNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                String id = turn.getUuid();
                if (findTurn(id) == null) {
                    throw new NonexistentEntityException("The turn with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Turn turn;
            try {
                turn = em.getReference(Turn.class, id);
                turn.getUuid();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The turn with id " + id + " no longer exists.", enfe);
            }
            Employee idEmployee = turn.getIdEmployee();
            if (idEmployee != null) {
                idEmployee.getTurnList().remove(turn);
                idEmployee = em.merge(idEmployee);
            }
            em.remove(turn);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Turn> findTurnEntities() {
        return findTurnEntities(true, -1, -1);
    }

    public List<Turn> findTurnEntities(int maxResults, int firstResult) {
        return findTurnEntities(false, maxResults, firstResult);
    }

    private List<Turn> findTurnEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Turn.class));
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
    
    public List<Turn> findTurnByDate(Date fechaInicio, Date fechaFin) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<Turn> consultaTurno = em.createNamedQuery("Turn.findByDateTimeCreated", Turn.class);
        consultaTurno.setParameter("fechaInicio", fechaInicio);
        consultaTurno.setParameter("fechaFin", fechaFin);
        
        List<Turn> turnos = new ArrayList<>();
        
        try {
            turnos = consultaTurno.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return turnos;
    }
    
    public List<Turn> findTurnByStatus(String status) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<Turn> consultaTurno = em.createNamedQuery("Turn.findByStatus", Turn.class);
        consultaTurno.setParameter("status", status);
        
        List<Turn> turnos = new ArrayList<>();
        
        try {
            turnos = consultaTurno.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return turnos;
    }
    
    public List<Turn> findTurnByIsActive(Boolean isActive) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<Turn> consultaTurno = em.createNamedQuery("Turn.findByIsActive", Turn.class);
        consultaTurno.setParameter("isActive", isActive);
        
        List<Turn> turnos = new ArrayList<>();
        
        try {
            turnos = consultaTurno.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return turnos;
    }
    
    public List<Turn> findTurnByType(Turn.Type type) throws Exception {
        EntityManager em = getEntityManager();
        
        TypedQuery<Turn> consultaTurno = em.createNamedQuery("Turn.findByType", Turn.class);
        consultaTurno.setParameter("type", type);
        
        List<Turn> turnos = new ArrayList<>();
        
        try {
            turnos = consultaTurno.getResultList();
        } catch (Exception exception) {
            throw new Exception("Ocurrio un error.");
        } finally {
            em.close();
        }
        return turnos;
    }

    public Turn findTurn(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Turn.class, id);
        } finally {
            em.close();
        }
    }

    public int getTurnCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Turn> rt = cq.from(Turn.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
