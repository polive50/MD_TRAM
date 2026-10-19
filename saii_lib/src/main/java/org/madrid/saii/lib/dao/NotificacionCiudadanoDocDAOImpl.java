
package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.NotificacionCiudadanoDoc;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio NotificacionCiudadanoDoc
 *
 * @author everis
 */
@Repository("notificacionCiudadanoDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class NotificacionCiudadanoDocDAOImpl extends BaseDAOImpl<NotificacionCiudadanoDoc, Integer>
        implements NotificacionCiudadanoDocDAO {

}
