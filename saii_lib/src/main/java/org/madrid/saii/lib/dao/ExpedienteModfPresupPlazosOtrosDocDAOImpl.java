package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("expedienteModfPresupPlazosOtrosDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteModfPresupPlazosOtrosDocDAOImpl extends BaseDAOImpl<ExpedienteModfPresupPlazosOtrosDoc, Integer>
implements ExpedienteModfPresupPlazosOtrosDocDAO {

    @Override
    public List<ExpedienteModfPresupPlazosOtrosDoc> findByExpedienteModifPresPlazoOtra(Integer idExpedienteModifPresPlazoOtra) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" SELECT e FROM ExpedienteModfPresupPlazosOtrosDoc as e "); 
        queryHql.append(" WHERE e.expedienteModifPresPlazoOtra.idExpedienteModifPresPlazoOtra = :idExpedienteModifPresPlazoOtra ");
        queryHql.append(" AND e.fcBajaFila IS NULL "); 
        queryHql.append(" AND e.expedienteModifPresPlazoOtra.fcBajaFila IS NULL "); 
 
        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteModifPresPlazoOtra", idExpedienteModifPresPlazoOtra);
 
        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteModfPresupPlazosOtrosDoc> executeHQLQueryList = (List<ExpedienteModfPresupPlazosOtrosDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);
 
        return executeHQLQueryList;
    }

    

}
