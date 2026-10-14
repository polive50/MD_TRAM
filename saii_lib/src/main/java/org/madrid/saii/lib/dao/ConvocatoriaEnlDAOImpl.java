
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaEnl;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaEnl
 *
 * @author everis
 */
@Repository("convocatoriaEnlDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaEnlDAOImpl extends BaseDAOImpl<ConvocatoriaEnl, Integer>
        implements ConvocatoriaEnlDAO {

}
