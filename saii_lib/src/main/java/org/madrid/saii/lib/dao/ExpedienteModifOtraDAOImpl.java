package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ExpedienteModifOtra;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteModifOtra
 *
 * @author quental
 */
@Repository("expedienteModifOtraDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteModifOtraDAOImpl extends BaseDAOImpl<ExpedienteModifOtra, Integer> implements ExpedienteModifOtraDAO {


}