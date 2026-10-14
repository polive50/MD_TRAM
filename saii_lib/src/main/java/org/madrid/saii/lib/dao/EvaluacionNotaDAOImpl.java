package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.EvaluacionNota;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio EvaluacionNota
 *
 * @author nttdata
 */
@Repository("evaluacionNotaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionNotaDAOImpl extends BaseDAOImpl<EvaluacionNota, Integer>
        implements EvaluacionNotaDAO {

}
