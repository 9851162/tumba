/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CountryDao;
import dao.LocalityDao;
import dao.RegionDao;
import entities.Country;
import entities.Locality;
import entities.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import service.parent.PrimService;

/**
 *
 * @author bezdatiuzer
 */
@Service
@Transactional
@Scope(value = "request", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class RegionService extends PrimService {
    
    @Autowired
    private CountryDao cDao;
    
    @Autowired
    private RegionDao regDao;
    
    @Autowired
    private LocalityDao locDao;
    
    public void createCountry(String name) {
        if (name != null && !name.equals("")) {
            Country c = new Country();
            c.setName(name);
            if (validate(c)) {
                cDao.save(c);
            }
        }else{
            addError("Введите название страны");
        }
    }
    
    public void createRegion(String name,Long countryId) {
        if (name != null && !name.equals("")) {
            Country c = cDao.find(countryId);
            Region r = new Region();
            r.setName(name);
            r.setCountry(c);
            if (validate(r)) {
                regDao.save(r);
            }
        }else{
            addError("Введите название региона");
        }
    }
    
    public void createLocality(String name,Long regionId) {
        if (name != null && !name.equals("")) {
            Region r =  regDao.find(regionId);
            Locality l = new Locality();
            l.setName(name);
            if (validate(l)) {
                locDao.save(l);
            }
        }else{
            addError("Введите название нас.пункта");
        }
    }
    
    public void deleteCountry(Long countryId){
        Country c = cDao.find(countryId);
        for(Region r:c.getRegions()){
            for(Locality l:r.getLocalities()){
                locDao.delete(l);
            }
            regDao.delete(r);
        }
        cDao.delete(c);
    }
    
    public void deleteRegion(Long regionId){
        Region r = regDao.find(regionId);
        for(Locality l:r.getLocalities()){
            locDao.delete(l);
        }
        regDao.delete(r);
    }
    
    public void deleteLocality(Long localityId){
        Locality l = locDao.find(localityId);
        locDao.delete(l);
    }
    
    public void changeCountryName(Long countryId,String newName){
        if (newName != null && !newName.equals("")) {
            Country c = cDao.find(countryId);
            c.setName(newName);
            if(validate(c)){
                cDao.save(c);
            }
        }
    }
    
    public void changeRegionName(Long regionId,String newName){
        if (newName != null && !newName.equals("")) {
            Region r = regDao.find(regionId);
            r.setName(newName);
            if(validate(r)){
                regDao.save(r);
            }
        }
    }
    
    public void changeLocalityName(Long localityId,String newName){
        if (newName != null && !newName.equals("")) {
            Locality l = locDao.find(localityId);
            l.setName(newName);
            if(validate(l)){
                locDao.save(l);
            }
        }
    }
    
}
