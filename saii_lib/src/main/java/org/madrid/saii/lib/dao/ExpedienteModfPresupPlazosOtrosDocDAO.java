package org.madrid.saii.lib.dao;


import java.util.List;


import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;

public interface ExpedienteModfPresupPlazosOtrosDocDAO extends BaseDAO<ExpedienteModfPresupPlazosOtrosDoc, Integer> {

    List<ExpedienteModfPresupPlazosOtrosDoc> findByExpedienteModifPresPlazoOtra(
            Integer idExpedienteModifPresPlazoOtra);

    

    

}
