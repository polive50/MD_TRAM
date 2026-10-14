package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.TramiteSolicitanteExpediente;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("tramiteSolicitanteExpedienteDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TramiteSolicitanteExpedienteDAOImpl extends BaseDAOImpl<TramiteSolicitanteExpediente, Integer>
implements TramiteSolicitanteExpedienteDAO  {

}
