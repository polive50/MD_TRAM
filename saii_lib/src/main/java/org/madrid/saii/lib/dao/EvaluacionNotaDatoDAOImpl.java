package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.EvaluacionNotaDato;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio EvaluacionNotaDato
 *
 * @author nttdata
 */
@Repository("evaluacionNotaDatoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionNotaDatoDAOImpl extends BaseDAOImpl<EvaluacionNotaDato, Integer>
        implements EvaluacionNotaDatoDAO {

}
