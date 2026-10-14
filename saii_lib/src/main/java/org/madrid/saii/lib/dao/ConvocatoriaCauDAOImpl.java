package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaCau;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCau
 *
 * @author everis
 */
@Repository("convocatoriaCauDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaCauDAOImpl extends BaseDAOImpl<ConvocatoriaCau, Integer>
        implements ConvocatoriaCauDAO {

}
