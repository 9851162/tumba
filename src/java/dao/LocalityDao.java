/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.Locality;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class LocalityDao extends Dao<Locality>  {
    
    @Override
    public Class getSupportedClass() {
        return Locality.class;
    }
    
}
