package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteModifPresPlazoOtra
 *
 * @author quental
 */
@Repository("expedienteModifPresPlazoOtraDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteModifPresPlazoOtraDAOImpl extends BaseDAOImpl<ExpedienteModifPresPlazoOtra, Integer> implements ExpedienteModifPresPlazoOtraDAO {

    @Override
    public List<ExpedientePresupDTO> buscarPresupModifTemp(Integer idExpedienteModifPresPlazoOtra) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT e from ExpedientePresup as e "); 
        queryHql.append("left join ExpedienteModifPresupMonto as m "); 
        queryHql.append("where m.idExpedienteModifPresPlazoOtra = :idExpedienteModifPresPlazoOtra ");
        queryHql.append("and e.idExpedientePresup = m.idExpedientePresup ");
        queryHql.append("and e.fcBajaFila IS NULL");
        queryHql.append("and m.fcBajaFila IS NULL");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteModifPresPlazoOtra", idExpedienteModifPresPlazoOtra);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedientePresupDTO> executeHQLQueryList = (List<ExpedientePresupDTO>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }


}