package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.AlegacionDato;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio AlegacionDato
 *
 * @author nttdata
 */
@Repository("alegacionDatoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class AlegacionDatoDAOImpl extends BaseDAOImpl<AlegacionDato, Integer>
        implements AlegacionDatoDAO {

}
