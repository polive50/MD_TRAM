package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaPlaDoc;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaPlaDoc
 *
 * @author quental
 */
@Repository("convocatoriaPlaDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaPlaDocDAOImpl extends BaseDAOImpl<ConvocatoriaPlaDoc, Integer>
        implements ConvocatoriaPlaDocDAO {

}
