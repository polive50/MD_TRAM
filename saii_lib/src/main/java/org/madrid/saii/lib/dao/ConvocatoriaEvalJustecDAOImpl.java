package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaEvalJustec;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaEvalJustec
 *
 * @author quental
 */
@Repository("convocatoriaEvalJustecDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaEvalJustecDAOImpl extends BaseDAOImpl<ConvocatoriaEvalJustec, Integer>
        implements ConvocatoriaEvalJustecDAO {

}
