package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaArea
 *
 * @author nttdata
 */
@Repository("convocatoriaAreaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaAreaDAOImpl extends BaseDAOImpl<ConvocatoriaArea, Integer>
        implements ConvocatoriaAreaDAO {

}
