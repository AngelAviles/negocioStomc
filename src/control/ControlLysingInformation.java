/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.LysingInformation;
import java.util.List;
import negocio.EntityManagerFactoryBase;
import negocio.LysingInformationJpaController;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ControlLysingInformation {
    
    private LysingInformationJpaController getJpa() {
        return new LysingInformationJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }
    
    public void create(LysingInformation lysingInformation) {
        getJpa().create(lysingInformation);
    }

    public void edit(LysingInformation lysingInformation) throws NonexistentEntityException, Exception {
        getJpa().edit(lysingInformation);
    }

    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }

    public List<LysingInformation> findLysingInformationEntities() {
        return getJpa().findLysingInformationEntities();
    }

    public List<LysingInformation> findLysingInformationEntities(int maxResults, int firstResult) {
        return getJpa().findLysingInformationEntities(maxResults, firstResult);
    }
    
    public List<LysingInformation> findLysingInformationByFolio(Long folio) throws Exception {
        return getJpa().findLysingInformationByFolio(folio);
    }
    
    public List<LysingInformation> findLysingInformationByProcess(String process) throws Exception {
        return getJpa().findLysingInformationByProcess(process);
    }

    public LysingInformation findLysingInformation(Long id) {
        return getJpa().findLysingInformation(id);
    }

    public int getLysingInformationCount() {
        return getJpa().getLysingInformationCount();
    }
    
}
