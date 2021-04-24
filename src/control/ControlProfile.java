/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import dominio.Profile;
import java.util.List;
import negocio.ProfileJpaController;
import negocio.EntityManagerFactoryBase;
import negocio.exceptions.NonexistentEntityException;

/**
 *
 * @author angel
 */
public class ControlProfile {
    
    private ProfileJpaController getJpa() {
        return new ProfileJpaController(EntityManagerFactoryBase.getInstance().getEmf());
    }

    public void create(Profile profile) {
        getJpa().create(profile);
    }

    public void edit(Profile profile) throws NonexistentEntityException, Exception {
        getJpa().edit(profile);
    }

    public void destroy(Long id) throws NonexistentEntityException {
        getJpa().destroy(id);
    }

    public Profile findProfile(Long id) throws Exception {
        return getJpa().findProfile(id);
    }

    public List<Profile> findProfileByFolio(Long folio) throws Exception {
        return getJpa().findProfileByFolio(folio);
    }

    public List<Profile> findProfileByProfileName(String profileName) throws Exception {
        return getJpa().findProfileByProfileName(profileName);
    }

    public List<Profile> findProfileEntities() {
        return getJpa().findProfileEntities();
    }

    public List<Profile> findProfileEntities(int maxResults, int firstResult) {
        return getJpa().findProfileEntities(maxResults, firstResult);
    }

    public int getProfileCount() {
        return getJpa().getProfileCount();
    }

}
