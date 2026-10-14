package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.SucaProvincia;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio SucaProvincia
 *
 * @author everis
 */
@Repository("sucaProvinciaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SucaProvinciaDAOImpl extends BaseDAOImpl<SucaProvincia, String>
        implements SucaProvinciaDAO {

}
