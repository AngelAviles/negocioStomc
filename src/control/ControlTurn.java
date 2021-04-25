/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.Employee;
import dominio.Turn;
import java.util.Date;
import java.util.List;
import negocio.EntityManagerFactoryBase;
import negocio.TurnJpaController;
import negocio.exceptions.NonexistentEntityException;
import negocio.exceptions.PreexistingEntityException;

/**
 *
 * @author angel
 */
public class ControlTurn {
    
    private TurnJpaController getJpa() {
        return new TurnJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(Turn turn) throws PreexistingEntityException, Exception {
        getJpa().create(turn);
    }

    public void edit(Turn turn) throws NonexistentEntityException, Exception {
        getJpa().edit(turn);
    }

    public void destroy(String id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }

    public List<Turn> findTurnEntities() {
        return getJpa().findTurnEntities();
    }

    public List<Turn> findTurnEntities(int maxResults, int firstResult) {
        return getJpa().findTurnEntities(maxResults, firstResult);
    }
    
    public List<Turn> findTurnByIdEmployee(Employee idEmployee) throws Exception {
        return getJpa().findTurnByIdEmployee(idEmployee);
    }

    public List<Turn> findTurnByDate(Date fechaInicio, Date fechaFin) throws Exception {
        return getJpa().findTurnByDate(fechaInicio, fechaFin);
    }
    
    public List<Turn> findTurnByStatus(String status) throws Exception {
        return getJpa().findTurnByStatus(status);
    }
    
    public List<Turn> findTurnByIsActive(Boolean isActive) throws Exception {
        return getJpa().findTurnByIsActive(isActive);
    }
    
    public List<Turn> findTurnByType(Turn.Type type) throws Exception {
        return getJpa().findTurnByType(type);
    }
    
    public List<Turn> Query_EntreFechasYEstado(String status, Date fechaInicio, Date fechaFin) throws Exception {
        return getJpa().Query_EntreFechasYEstado(status, fechaInicio, fechaFin);
    }
    
    public List<Turn> Query_FechaInicioYEstado(String status, Date fechaInicio) throws Exception {
        return getJpa().Query_FechaInicioYEstado(status, fechaInicio);
    }

    public List<Turn> Query_FechaFinYEstado(String status, Date fechaFin) throws Exception {
        return getJpa().Query_FechaFinYEstado(status, fechaFin);
    }
    
    public List<Turn> Query_FechaInicio(Date fechaInicio) throws Exception {
        return getJpa().Query_FechaInicio(fechaInicio);
    }
    
    public List<Turn> Query_FechaFin(Date fechaFin) throws Exception {
        return getJpa().Query_FechaFin(fechaFin);
    }
    
    public Turn findTurn(String id) {
        return findTurn(id);
    }

    public int getTurnCount() {
        return getTurnCount();
    }
    
}
