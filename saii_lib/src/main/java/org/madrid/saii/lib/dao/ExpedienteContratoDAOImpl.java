package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.DataAccessException;

@Repository("expedienteContratoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteContratoDAOImpl extends BaseDAOImpl<ExpedienteContrato, Integer>
implements ExpedienteContratoDAO {
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Override
    public List<ExpedienteContrato> findByIdExpediente(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT e FROM ExpedienteContrato as e "); 
        queryHql.append(" WHERE e.idExpediente = :idExpediente ");
        queryHql.append(" AND e.fcBajaFila IS NULL ");

 
        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
 
        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteContrato> executeHQLQueryList = (List<ExpedienteContrato>) super.executeHQLQueryList(
                queryHql.toString(), params);
 
        return executeHQLQueryList;
    }

    @Override
    public List<ExpedienteContratoDTO> findAllExpContract(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT e from ExpedienteContrato as e "); 
        queryHql.append("where e.idExpediente = :idExpediente ");
        queryHql.append("and e.fcBajaFila IS NULL");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteContratoDTO> executeHQLQueryList = (List<ExpedienteContratoDTO>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

    @Override
    public ExpedienteContrato findAllExpContractById(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT e from ExpedienteContrato as e "); 
        queryHql.append("where e.idExpediente = :idExpediente ");
        queryHql.append("and e.fcBajaFila IS NULL");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        ExpedienteContrato executeUniqueHQLQuery =  (ExpedienteContrato) super.executeUniqueHQLQuery(
                queryHql.toString(), params);

        return executeUniqueHQLQuery;
    }
    
    
    @Override
    public List<ExpedienteContratoHist> findByIdExpedienteContrato(Integer idExpedienteContrato) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT ec from ExpedienteContratoHist as ec "); 
        queryHql.append("where ec.idExpedienteContrato = :idExpedienteContrato ");
        queryHql.append("and ec.fcBajaFila IS NULL");

 
        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteContrato", idExpedienteContrato);
 
        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteContratoHist> executeHQLQueryList = (List<ExpedienteContratoHist>) super.executeHQLQueryList(
                queryHql.toString(), params);
 
        return executeHQLQueryList;
    }
    
    @Override
    public List<ExpedienteContrato> findAllByEregPresent(String cdEregPresent) {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append("SELECT e from ExpedienteContrato as e "); 
        queryHql.append("where e.cdEregPresent = :cdEregPresent ");
        queryHql.append(" or e.cdEregSubsana = :cdEregPresent ");
        queryHql.append(" or e.cdEregRenuncia = :cdEregPresent ");
        


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("cdEregPresent", cdEregPresent);

        @SuppressWarnings("unchecked")
        List<ExpedienteContrato> resultQuery = (List<ExpedienteContrato>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return resultQuery;
    }
    
    public ExpedienteContrato buscarContratoInicialSustitucion(Integer idExpediente) throws DataAccessException {
        StringBuilder queryHql = new StringBuilder();
        
        // Construye la consulta HQL
        queryHql.append(" SELECT e from ExpedienteContrato as e "); 
        queryHql.append(" WHERE e.idExpediente = :idExpediente ");
        queryHql.append(" AND e.idTipoContrato.idValor = :tipoContrato ");
        queryHql.append(" AND e.fcBajaFila IS NOT NULL ");
        queryHql.append(" ORDER BY e.fcBajaFila DESC");


        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("tipoContrato", utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_INICIAL));

        ExpedienteContrato expContrato =  (ExpedienteContrato) super.executeUniqueHQLQuery(
                queryHql.toString(), params);
        
        return expContrato;
    }

}
