package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaSubmod;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaSubmod
 *
 * @author nttdata
 */
@Repository("convocatoriaSubmodDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaSubmodDAOImpl extends BaseDAOImpl<ConvocatoriaSubmod, Integer>
        implements ConvocatoriaSubmodDAO {

}
