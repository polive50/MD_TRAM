
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.UnidadAdmin;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio UnidadAdmin
 *
 * @author everis
 */
@Repository("unidadAdminDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UnidadAdminDAOImpl extends BaseDAOImpl<UnidadAdmin, Integer>
        implements UnidadAdminDAO {

}
