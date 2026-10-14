package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.EvaluacionJustecNota;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio EvaluacionJustecNota
 *
 * @author nttdata
 */
@Repository("evaluacionJustecNotaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionJustecNotaDAOImpl extends BaseDAOImpl<EvaluacionJustecNota, Integer>
        implements EvaluacionJustecNotaDAO {

}
