/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import dao.CountryDao;
import dao.LocalityDao;
import dao.StateDao;
import entities.Country;
import entities.Locality;
import entities.State;
import java.util.ArrayList;
import java.util.List;
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
    private StateDao stateDao;

    @Autowired
    private LocalityDao locDao;

    public void createCountry(String name) {
        if (name != null && !name.equals("")) {
            Country c = new Country();
            c.setName(name);
            if (validate(c)) {
                cDao.save(c);
            }
        } else {
            addError("Введите название страны");
        }
    }

    public void createState(String name, Long countryId) {
        if (name != null && !name.equals("")) {
            Country c = cDao.find(countryId);
            State s = new State();
            s.setName(name);
            s.setCountry(c);
            if (validate(s)) {
                stateDao.save(s);
            }
        } else {
            addError("Введите название административного округа");
        }
    }

    public void createLocality(String name, Long sateId) {
        if (name != null && !name.equals("")) {
            State s = stateDao.find(sateId);
            Locality l = new Locality();
            l.setName(name);
            l.setState(s);
            l.setCountry(s.getCountry());
            if (validate(l)) {
                locDao.save(l);
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
        locDao.delete(l);
    }

    public void changeCountryName(Long countryId, String newName) {
        if (newName != null && !newName.equals("")) {
            Country c = cDao.find(countryId);
            c.setName(newName);
            if (validate(c)) {
                cDao.save(c);
            }
        }
    }

    public void changeStateName(Long stateId, String newName) {
        if (newName != null && !newName.equals("")) {
            State s = stateDao.find(stateId);
            s.setName(newName);
            if (validate(s)) {
                stateDao.update(s);
            }
        }
    }

    public void changeLocalityName(Long localityId, String newName) {
        if (newName != null && !newName.equals("")) {
            Locality l = locDao.find(localityId);
            l.setName(newName);
            if (validate(l)) {
                locDao.save(l);
            }
        }
    }

    public List<Country> getCountries() {
        return cDao.getAll();
    }

    public List<State> getStates(Long countryId) {
        if (countryId != null) {
            Country c = cDao.find(countryId);
            return c.getStates();
        } else {
            return new ArrayList();
        }
    }
    
    public List<Locality> getLocalities(Long stateId) {
        if (stateId != null) {
            State s = stateDao.find(stateId);
            return s.getLocalities();
        } else {
            return new ArrayList();
        }
    }

}
