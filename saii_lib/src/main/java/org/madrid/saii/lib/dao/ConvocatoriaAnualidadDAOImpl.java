package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaAnualidad;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaAnualidad
 *
 * @author Quental
 */
@Repository("convocatoriaAnualidadDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaAnualidadDAOImpl extends BaseDAOImpl<ConvocatoriaAnualidad, Integer>
implements ConvocatoriaAnualidadDAO {

}
