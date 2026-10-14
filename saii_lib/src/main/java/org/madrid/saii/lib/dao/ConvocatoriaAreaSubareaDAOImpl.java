package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.ConvocatoriaAreaSubarea;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaAreaSubarea
 *
 * @author quental
 */
@Repository("convocatoriaAreaSubareaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaAreaSubareaDAOImpl extends BaseDAOImpl<ConvocatoriaAreaSubarea, Integer>
        implements ConvocatoriaAreaSubareaDAO {

}
