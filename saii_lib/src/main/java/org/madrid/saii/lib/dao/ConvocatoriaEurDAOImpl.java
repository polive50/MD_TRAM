package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaEur;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaEur
 *
 * @author everis
 */
@Repository("convocatoriaEurDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaEurDAOImpl extends BaseDAOImpl<ConvocatoriaEur, Integer>
        implements ConvocatoriaEurDAO {

}
