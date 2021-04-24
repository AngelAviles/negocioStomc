/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.CatalogueProfile;
import java.util.List;
import negocio.CatalogueProfileJpaController;
import negocio.EntityManagerFactoryBase;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ControlCatalogueProfile {

    private CatalogueProfileJpaController getJpa() {
        return new CatalogueProfileJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }

    public void create(CatalogueProfile catalogueProfile) {
        getJpa().create(catalogueProfile);
    }

    public void edit(CatalogueProfile catalogueProfile) throws NonexistentEntityException, Exception {
        getJpa().edit(catalogueProfile);
    }

    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }

    public CatalogueProfile findCatalogueProfile(Long id) throws Exception {
        return getJpa().findCatalogueProfile(id);
    }

    public List<CatalogueProfile> findCatalogueProfileByFolio(Long folio) throws Exception {
        return getJpa().findCatalogueProfileByFolio(folio);
    }

    public List<CatalogueProfile> findCatalogueProfileByProfileName(String profileName) throws Exception {
        return getJpa().findCatalogueProfileByProfileName(profileName);
    }

    public List<CatalogueProfile> findCatalogueProfileEntities() {
        return getJpa().findCatalogueProfileEntities();
    }

    public List<CatalogueProfile> findCatalogueProfileEntities(int maxResults, int firstResult) {
        return getJpa().findCatalogueProfileEntities(maxResults, firstResult);
    }

    public int getCatalogueProfileCount() {
        return getJpa().getCatalogueProfileCount();
    }

}
