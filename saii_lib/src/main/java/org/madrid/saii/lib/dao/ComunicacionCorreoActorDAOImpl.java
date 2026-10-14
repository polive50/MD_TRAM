package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCom
 *
 * @author nttdata
 */
@Repository("comunicacionCorreoActorDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ComunicacionCorreoActorDAOImpl extends BaseDAOImpl<ComunicacionCorreoActor, Integer>
        implements ComunicacionCorreoActorDAO {
    
  
}
