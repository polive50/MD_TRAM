package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("expedienteContratoDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteContratoDocDAOImpl extends BaseDAOImpl<ExpedienteContratoDoc, Integer>
implements ExpedienteContratoDocDAO {

    @Override
    public List<ExpedienteContratoDoc> findByIdExpedienteContrato(Integer idExpedienteContrato) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" SELECT e FROM ExpedienteContratoDoc as e "); 
        queryHql.append(" WHERE e.expedienteContrato.idExpedienteContrato = :idExpedienteContrato ");
        queryHql.append(" AND e.fcBajaFila IS NULL "); 
        queryHql.append(" AND e.expedienteContrato.fcBajaFila IS NULL "); 
 
        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteContrato", idExpedienteContrato);
 
        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteContratoDoc> executeHQLQueryList = (List<ExpedienteContratoDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);
 
        return executeHQLQueryList;
    }

    
    @Override
    public List<ExpedienteContratoDoc> findByCdEregeContrato(String cdEregPresent) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" SELECT e FROM ExpedienteContratoDoc as e "); 
        queryHql.append(" WHERE e.expedienteContrato.cdEregPresent = :cdEregPresent ");
        queryHql.append(" or e.expedienteContrato.cdEregSubsana = :cdEregPresent ");
        queryHql.append(" or e.expedienteContrato.cdEregRenuncia = :cdEregPresent ");
        queryHql.append(" AND e.fcBajaFila IS NULL "); 
        queryHql.append(" AND e.expedienteContrato.fcBajaFila IS NULL "); 
 
        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
         params.put("cdEregPresent", cdEregPresent);
        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteContratoDoc> executeHQLQueryList = (List<ExpedienteContratoDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);
 
        return executeHQLQueryList;
    }

}
