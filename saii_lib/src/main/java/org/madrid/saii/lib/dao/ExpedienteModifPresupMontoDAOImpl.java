package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteModifPresupMonto;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteModifPresupMonto
 *
 * @author quental
 */
@Repository("expedienteModifPresupMontoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteModifPresupMontoDAOImpl extends BaseDAOImpl<ExpedienteModifPresupMonto, Integer>
        implements ExpedienteModifPresupMontoDAO {

}
