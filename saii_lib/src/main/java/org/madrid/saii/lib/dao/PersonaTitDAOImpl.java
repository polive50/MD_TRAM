package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.PersonaTit;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio PersonaTit
 *
 * @author nttdata
 */
@Repository("personaTitDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PersonaTitDAOImpl extends BaseDAOImpl<PersonaTit, Integer> implements PersonaTitDAO {

}
