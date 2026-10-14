package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Informe;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio informe
 *
 * @author nttdata
 */
@Repository("informeDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class InformeDAOImpl extends BaseDAOImpl<Informe, Integer> implements InformeDAO{

}
