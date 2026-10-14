package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.PersonaEspec;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio PersonaEspecialidad
 *
 * @author nttdata
 */
@Repository("personaEspecDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class PersonaEspecDAOImpl extends BaseDAOImpl<PersonaEspec, Integer>
        implements PersonaEspecDAO {

}
