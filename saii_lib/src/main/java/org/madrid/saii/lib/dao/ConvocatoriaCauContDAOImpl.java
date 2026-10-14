package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaCauCont;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCauCont
 *
 * @author quental
 */
@Repository("convocatoriaCauContDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaCauContDAOImpl extends BaseDAOImpl<ConvocatoriaCauCont, Integer>
        implements ConvocatoriaCauContDAO {

}
