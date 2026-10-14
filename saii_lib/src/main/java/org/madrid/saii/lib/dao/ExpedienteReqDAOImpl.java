
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteReq;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteReq
 *
 * @author everis
 */
@Repository("expedienteReqDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteReqDAOImpl extends BaseDAOImpl<ExpedienteReq, Integer>
        implements ExpedienteReqDAO {

}
