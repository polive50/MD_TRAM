package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.EvaluacionJustecAparta;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Apartado
 *
 * @author nttdata
 */
@Repository("evaluacionJustecApartaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionJustecApartaDAOImpl extends BaseDAOImpl<EvaluacionJustecAparta, Integer>
        implements EvaluacionJustecApartaDAO {

}
