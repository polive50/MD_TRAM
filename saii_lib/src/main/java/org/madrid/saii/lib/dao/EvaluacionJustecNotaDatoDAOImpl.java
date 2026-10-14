package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.EvaluacionJustecNotaDato;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio EvaluacionNotaDato
 *
 * @author nttdata
 */
@Repository("evaluacionJustecNotaDatoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionJustecNotaDatoDAOImpl extends BaseDAOImpl<EvaluacionJustecNotaDato, Integer>
        implements EvaluacionJustecNotaDatoDAO {

}
