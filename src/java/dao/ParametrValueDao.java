/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import dao.parent.Dao;
import entities.ParametrValue;
import org.springframework.stereotype.Repository;

/**
 *
 * @author bezdatiuzer
 */
@Repository
public class ParametrValueDao extends Dao<ParametrValue>  {
    
    @Override
    public Class getSupportedClass() {
        return ParametrValue.class;
    }
}
