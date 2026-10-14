package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteActua;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteActua
 *
 * @author nttdata
 */
@Repository("expedienteActuaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteActuaDAOImpl extends BaseDAOImpl<ExpedienteActua, Integer> implements ExpedienteActuaDAO {

}