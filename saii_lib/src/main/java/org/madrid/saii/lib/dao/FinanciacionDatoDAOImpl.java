package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.FinanciacionDato;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Financiacion
 *
 * @author nttdata
 */
@Repository("financiacionDatoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class FinanciacionDatoDAOImpl extends BaseDAOImpl<FinanciacionDato, Integer> implements FinanciacionDatoDAO {

}