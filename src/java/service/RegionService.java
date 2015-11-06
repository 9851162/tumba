/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CountryDao;
import dao.LocalityDao;
import dao.RegionDao;
import dao.StateDao;
import dao.UserDao;
import entities.Country;
import entities.Locality;
import entities.Region;
import entities.State;
import entities.User;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
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
    private UserDao userDao;
    
    @Autowired
    private CountryDao cDao;

    @Autowired
    private StateDao stateDao;

    @Autowired
    private LocalityDao locDao;
    
    @Autowired
    private RegionDao regDao;

    public void createCountry(String name) {
        if (name != null && !name.equals("")) {
            if (cDao.isAvailableName(name)) {
                Country c = new Country();
                c.setName(name);
                if (validate(c)) {
                    cDao.save(c);
                }
            } else {
                addError("Такое название страны уже добавлено");
            }
        } else {
            addError("Введите название страны");
        }
    }

    public void createState(String name, Long countryId) {
        if (name != null && !name.equals("")) {
            if (stateDao.isAvailableName(name, countryId)) {
                Country c = cDao.find(countryId);
                State s = new State();
                s.setName(name);
                s.setCountry(c);
                if (validate(s)) {
                    stateDao.save(s);
                }
            } else {
                addError("Такое название адм. округа уже добавлено в эту страну");
            }
        } else {
            addError("Введите название административного округа");
        }
    }

    public void createLocality(String name, Long sateId) {
        if (name != null && !name.equals("")) {
            if (locDao.isAvailableName(name, sateId)) {
                State s = stateDao.find(sateId);
                Locality l = new Locality();
                l.setName(name);
                l.setState(s);
                l.setCountry(s.getCountry());
                if (validate(l)) {
                    locDao.save(l);
                }
            } else {
                addError("Такое название нас. пункта уже добавлено в этот адм. округ");
            }
        } else {
            addError("Введите название нас.пункта");
        }
    }

    public void deleteCountry(Long countryId) {
        Country c = cDao.find(countryId);
        for (State s : c.getStates()) {
            for (Locality l : s.getLocalities()) {
                locDao.delete(l);
            }
            stateDao.delete(s);
        }
        cDao.delete(c);
    }

    public void deleteState(Long sateId) {
        State s = stateDao.find(sateId);
        for (Locality l : s.getLocalities()) {
            locDao.delete(l);
        }
        stateDao.delete(s);
    }

    public void deleteLocality(Long localityId) {
        Locality l = locDao.find(localityId);
        l.setAds(new HashSet());
        locDao.update(l);
        locDao.delete(l);
    }

    public void changeCountryName(Long countryId, String newName) {
        if (newName != null && !newName.equals("")) {
            if (cDao.isAvailableName(newName)) {
                Country c = cDao.find(countryId);
                c.setName(newName);
                if (validate(c)) {
                    cDao.save(c);
                }
            } else {
                addError("Такое название страны уже добавлено");
            }
        }
    }

    public void changeStateName(Long stateId, String newName) {
        if (newName != null && !newName.equals("")) {
            State s = stateDao.find(stateId);
            if (stateDao.isAvailableName(newName, s.getCountry().getId())) {
                s.setName(newName);
                if (validate(s)) {
                    stateDao.update(s);
                }
            } else {
                addError("Введите название административного округа");
            }
        }
    }

    public void changeLocalityName(Long localityId, String newName) {
        if (newName != null && !newName.equals("")) {
            Locality l = locDao.find(localityId);
            if (locDao.isAvailableName(newName, l.getState().getId())) {
                l.setName(newName);
                if (validate(l)) {
                    locDao.save(l);
                }
            } else {
                addError("Такое название нас. пункта уже добавлено в этот адм. округ");
            }
        }
    }

    public List<Country> getCountries() {
        return cDao.getAll();
    }

    public List<State> getStates(Long countryId) {
        if (countryId != null) {
            Country c = cDao.find(countryId);
            if (c != null) {
                return c.getStates();
            }
        }
        return new ArrayList();
    }

    public List<Locality> getLocalities(Long stateId) {
        if (stateId != null) {
            State s = stateDao.find(stateId);
            if (s != null) {
                return s.getLocalities();
            }
        }
        return new ArrayList();
    }

    public Country getCountry(Long countryId) {
        if (countryId != null) {
            return cDao.find(countryId);
        } else {
            return null;
        }
    }

    public State getState(Long stateId) {
        if (stateId != null) {
            return stateDao.find(stateId);
        } else {
            return null;
        }
    }
    
    public List<State> getAllStates(){
        return stateDao.getAllSortedByName();
    }
    
    /*public Long getDefaultRegionId(Long userId){
        if(userId!=null){
            Region r = userDao.getHomeRegion(userId);
            if(r!=null){
                return r.getId();
            }
        }
        return Region.ALLRUSSIAID;
    }*/

    public Region getDefaultRegion(Long userId){
        if(userId!=null){
            Region r = userDao.getHomeRegion(userId);
            if(r!=null){
                return r;
            }
        }
            Region reg = new Region();
            reg.setAllRussia(Boolean.TRUE);
            return reg;
    }
    
    public Region getRegion(Long localIds[],Long stateIds[],User user,String name){
        Set<Locality>locals = new HashSet();
        Set<State>states = new HashSet();
        Region r = new Region();
        if(stateIds!=null){
            for(Long id:stateIds){
                State s = stateDao.find(id);
                states.add(s);
            }
        }
        if(localIds!=null){
            for(Long id:localIds){
                Locality l = locDao.find(id);
                locals.add(l);
            }
        }
        r.setStates(states);
        r.setLocalities(locals);
        r.setUser(user);
        r.setName(name);
        return r;
    }
    
    public List<Region>getAvailableRegions(Region region,User user){
        List<Region>res = new ArrayList();
        if(user!=null){
            res.addAll(user.getRegions());
        }else if(!region.isAllRussia()){
            if(region.getName()==null||region.getName().equals("")){
                region.setName("свой регион");
            }
            res.add(region);
        }
        return res;
    }
    
    public Region getRegion(Long regionId){
        return regDao.find(regionId);
    }
    
    public void addRegion(User u,Region r){
        if(u!=null){
            List<Region>regs = u.getRegions();
            if(validate(r)){
                regs.add(r);
            }
            userDao.update(u);
        }
    }
    
}
