/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.ReportingLog;
import java.util.Date;
import java.util.List;
import negocio.EntityManagerFactoryBase;
import negocio.ReportingLogJpaController;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ControlReportingLog {
    
    private ReportingLogJpaController getJpa() {
        return new ReportingLogJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(ReportingLog reportingLog) {
        getJpa().create(reportingLog);
    }

    public void edit(ReportingLog reportingLog) throws NonexistentEntityException, Exception {
        getJpa().edit(reportingLog);
    }

    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }

    public List<ReportingLog> findReportingLogEntities() {
        return getJpa().findReportingLogEntities();
    }

    public List<ReportingLog> findReportingLogEntities(int maxResults, int firstResult) {
        return getJpa().findReportingLogEntities(maxResults, firstResult);
    }
    
    public List<ReportingLog> findReportingLogByFolio(Long folio) throws Exception {
        return getJpa().findReportingLogByFolio(folio);
    }
    
    public List<ReportingLog> findReportingLogByRequestedReport(String requestedReport) throws Exception {
        return getJpa().findReportingLogByRequestedReport(requestedReport);
    }
    
    public List<ReportingLog> findReportingLogByUserAccount(String userAccount) throws Exception {
        return getJpa().findReportingLogByUserAccount(userAccount);
    }
    
    public List<ReportingLog> findReportingByDateReport(Date fechaInicio, Date fechaFin) throws Exception {
        return getJpa().findReportingByDateReport(fechaInicio, fechaFin);
    }

    public ReportingLog findReportingLog(Long id) {
        return getJpa().findReportingLog(id);
    }

    public int getReportingLogCount() {
        return getJpa().getReportingLogCount();
    }
    
}
