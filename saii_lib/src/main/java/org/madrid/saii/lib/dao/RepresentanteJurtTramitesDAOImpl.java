package org.madrid.saii.lib.dao;


import org.madrid.saii.lib.domain.RepresentanteJurtTramites;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("representanteJurtTramitesDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class RepresentanteJurtTramitesDAOImpl extends BaseDAOImpl<RepresentanteJurtTramites, Integer> implements RepresentanteJurtTramitesDAO{
    
}
  