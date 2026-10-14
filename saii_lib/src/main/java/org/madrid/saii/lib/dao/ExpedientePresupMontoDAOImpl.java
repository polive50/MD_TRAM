package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteFin
 *
 * @author nttdata
 */
@Repository("expedientePresupMontoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedientePresupMontoDAOImpl extends BaseDAOImpl<ExpedientePresupMonto, Integer>
        implements ExpedientePresupMontoDAO {

}
