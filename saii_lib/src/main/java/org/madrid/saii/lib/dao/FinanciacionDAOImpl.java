package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Financiacion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Financiacion
 *
 * @author nttdata
 */
@Repository("financiacionDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class FinanciacionDAOImpl extends BaseDAOImpl<Financiacion, Integer> implements FinanciacionDAO {

}