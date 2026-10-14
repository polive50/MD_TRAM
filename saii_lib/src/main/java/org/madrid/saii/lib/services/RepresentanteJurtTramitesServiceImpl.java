package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.RepresentanteJurtTramitesDAO;
import org.madrid.saii.lib.domain.RepresentanteJurtTramites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

@Service("representanteJurtTramitesService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class RepresentanteJurtTramitesServiceImpl  extends BaseServiceImpl<RepresentanteJurtTramites,Integer,RepresentanteJurtTramitesDAO> implements RepresentanteJurtTramitesService{

    
    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param representanteJurDAO
     */
    
    @Autowired
    public RepresentanteJurtTramitesServiceImpl( @Qualifier("representanteJurtTramitesDAO") RepresentanteJurtTramitesDAO representanteJurtTramitesDAO) {
        super();
        this.setDao(representanteJurtTramitesDAO);
    }
}
