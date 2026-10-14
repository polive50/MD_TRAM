package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.SucaMunicipio;
import org.madrid.saii.lib.domain.SucaMunicipioId;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio SucaMunicipio
 *
 * @author everis
 */
@Repository("sucaMunicipioDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class SucaMunicipioDAOImpl extends BaseDAOImpl<SucaMunicipio, SucaMunicipioId>
        implements SucaMunicipioDAO {

}
