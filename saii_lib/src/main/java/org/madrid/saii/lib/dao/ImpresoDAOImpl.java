package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;


/**
 * Implementacion para el DAO de la clase de dominio Impreso
 *
 * @author everis
 */
@Repository("impresoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ImpresoDAOImpl extends BaseDAOImpl<Impreso, Integer> implements ImpresoDAO {

    @Override
    public List<Impreso> buscarPorConvActorContrato(Integer idConvocatoria, Integer idActor,
            List<Integer> lstTipoImpreso) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT i ");
        queryHql.append("FROM ");
        queryHql.append("ConvocatoriaAct a, Impreso i ");
        queryHql.append("WHERE ");

        queryHql.append(" a.idActor = :idActor ");
        queryHql.append("AND a.itContrato = :itContrato ");
        queryHql.append("AND a.idConvocatoria = :idConvocatoria ");
        queryHql.append("AND a.idConvocatoria = i.convocatoria.idConvocatoria ");
        queryHql.append("AND i.convocatoriaAct.idActor IN ( a.idActorSolicitante,a.idActorCoordinador) ");
        queryHql.append("AND i.tipo.idValor IN (:lstTipoImpreso) ");
        queryHql.append("AND a.fcBajaFila is null ");
        queryHql.append("AND i.fcBajaFila is null ");

        Map<String, Object> params = new HashMap<>();
        params.put("itContrato", Constantes.AfirmacionNegacion.S);
        params.put("idConvocatoria", idConvocatoria);
        params.put("idActor", idActor);
        params.put("lstTipoImpreso", lstTipoImpreso);

        return (List<Impreso>) super.executeHQLQueryList(
                queryHql.toString(), params);
    }
    
    @Override
    public List<Impreso> buscarPorExpedienteTipo(Integer idExpediente, List<Integer> lstTipoImpreso) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT I ");
        queryHql.append("FROM Convocatoria AS C, Solicitud AS S, Expediente AS E, Impreso AS I ");
        queryHql.append("WHERE C.idConvocatoria = S.convocatoria.idConvocatoria ");
        queryHql.append("AND S.idSolicitud = E.solicitud.idSolicitud ");
        queryHql.append("AND C.idConvocatoria = I.convocatoria.idConvocatoria ");
        queryHql.append("AND E.idExpediente = :idExpediente ");
        queryHql.append("AND E.convocatoriaAct.idActor = I.convocatoriaAct.idActor ");
        queryHql.append("AND I.tipo.idValor IN (:lstTipoImpreso) ");
        queryHql.append("AND I.fcBajaFila is null ");
        
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("lstTipoImpreso", lstTipoImpreso);

        return (List<Impreso>) super.executeHQLQueryList(
                queryHql.toString(), params);
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<Impreso> buscarPorIdConvocatoria(Integer idConvocatoria) {

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("Select i from Impreso as i "); 
        queryHql.append("where i.convocatoria.idConvocatoria = :idConvocatoria ");
        queryHql.append("and i.fcBajaFila IS NULL"); 
       
        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);

        return (List<Impreso>) super.executeHQLQueryList(
                queryHql.toString(), params);
    }

}
