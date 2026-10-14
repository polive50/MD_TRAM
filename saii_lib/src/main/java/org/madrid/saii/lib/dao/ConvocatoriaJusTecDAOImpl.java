package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaJusTec;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaJusTec
 *
 * @author nttdata
 */
@Repository("convocatoriaJusTecDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaJusTecDAOImpl extends BaseDAOImpl<ConvocatoriaJusTec, Integer>
implements ConvocatoriaJusTecDAO {

}
