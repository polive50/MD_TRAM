package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaPlaFir;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaPlaFir
 *
 * @author everis
 */
@Repository("convocatoriaPlaFirDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaPlaFirDAOImpl extends BaseDAOImpl<ConvocatoriaPlaFir, Integer>
        implements ConvocatoriaPlaFirDAO {

}
