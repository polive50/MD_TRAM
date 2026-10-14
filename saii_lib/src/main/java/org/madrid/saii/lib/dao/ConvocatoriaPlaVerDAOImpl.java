package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaPlaVer;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaPlaVer
 *
 * @author everis
 */
@Repository("convocatoriaPlaVerDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaPlaVerDAOImpl extends BaseDAOImpl<ConvocatoriaPlaVer, Integer>
        implements ConvocatoriaPlaVerDAO {

}
