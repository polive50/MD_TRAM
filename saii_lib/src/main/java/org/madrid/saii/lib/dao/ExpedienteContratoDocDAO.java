package org.madrid.saii.lib.dao;


import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteContratoDoc;

public interface ExpedienteContratoDocDAO extends BaseDAO<ExpedienteContratoDoc, Integer> {

    List<ExpedienteContratoDoc> findByIdExpedienteContrato(Integer idExpedienteContrato);

    List<ExpedienteContratoDoc> findByCdEregeContrato(String cdEregPresent);

    

}
