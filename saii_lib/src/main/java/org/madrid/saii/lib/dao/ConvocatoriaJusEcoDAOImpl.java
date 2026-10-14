package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaJusEco;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaJusEco
 *
 * @author quental
 */
@Repository("convocatoriaJusEcoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaJusEcoDAOImpl extends BaseDAOImpl<ConvocatoriaJusEco, Integer>
implements ConvocatoriaJusEcoDAO {

}
