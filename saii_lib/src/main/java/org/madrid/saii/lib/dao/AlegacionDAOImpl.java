
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Alegacion;  

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Alegacion
 *
 * @author everis
 */
@Repository("alegacionDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class AlegacionDAOImpl extends BaseDAOImpl<Alegacion, Integer> implements AlegacionDAO {

}