/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package negocio;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author angel
 */
public class EntityManagerFactoryBase {
    
    private static EntityManagerFactoryBase instance;
    private EntityManagerFactory emf;
    
    private EntityManagerFactoryBase() {
        emf = Persistence.createEntityManagerFactory("stomcPU");
    }

    public static EntityManagerFactoryBase getInstance() {
        if (instance == null) {
            instance = new EntityManagerFactoryBase();
        }
        return instance;
    }

    public EntityManagerFactory getEmf() {
        emf.getCache().evictAll();
        return emf;
    }
}
