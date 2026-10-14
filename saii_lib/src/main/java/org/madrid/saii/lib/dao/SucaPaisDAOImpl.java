package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.SucaPais;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio SucaPais
 *
 * @author Quental
 */
@Repository("sucaPaisDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SucaPaisDAOImpl extends BaseDAOImpl<SucaPais, String>
        implements SucaPaisDAO {


}
