package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCom
 *
 * @author quental
 */
@Repository("comunicacionNoteActorDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ComunicacionNoteActorDAOImpl extends BaseDAOImpl<ComunicacionNoteActor, Integer>
        implements ComunicacionNoteActorDAO {
    
  
}
