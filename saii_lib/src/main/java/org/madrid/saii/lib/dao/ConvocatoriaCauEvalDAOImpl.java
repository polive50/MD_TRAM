package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaCauEval;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaCauEval
 *
 * @author nttdata
 */
@Repository("convocatoriaCauEvalDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaCauEvalDAOImpl extends BaseDAOImpl<ConvocatoriaCauEval, Integer>
        implements ConvocatoriaCauEvalDAO {

}
