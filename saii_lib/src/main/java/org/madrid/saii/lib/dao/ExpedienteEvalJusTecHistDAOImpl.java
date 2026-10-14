package org.madrid.saii.lib.dao;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación para el DAO de la clase de dominio ExpedienteEval
 *
 * @author quental
 */

@Repository("expedienteEvalJusTecHistDAO")
public class ExpedienteEvalJusTecHistDAOImpl extends BaseDAOImpl<ExpedienteEvalJusTecHist, Integer> implements ExpedienteEvalJusTecHistDAO{
   
    @SuppressWarnings("unchecked")
    @Override
    public  List<ExpedienteEvalJusTecHist> buscarPorIdExpedienteJusTecAndPersona(Integer idExpedienteJusTec, Integer idPersona) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT expEvalJusTecHist");
        queryHql.append(" FROM ExpedienteEvalJusTecHist expEvalJusTecHist ");
        queryHql.append(" WHERE ");
        queryHql.append(" expEvalJusTecHist.expedienteJusTec.idExpedienteJusTec = :idExpedienteJusTec ");
        queryHql.append(" AND expEvalJusTecHist.persona.idInteresado = :idPersona ");
        queryHql.append(" AND expEvalJusTecHist.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteJusTec", idExpedienteJusTec);
        params.put("idPersona", idPersona);

       return (List<ExpedienteEvalJusTecHist>) super.executeHQLQueryList(queryHql.toString(), params);
    }
    

    @Override
    public  ExpedienteEvalJusTecHistDTO buscarByIdAndPeson(Integer idExpedienteJusTec, Integer idPersona) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT expEvalJusTecHist");
        queryHql.append(" FROM ExpedienteEvalJusTecHist expEvalJusTecHist ");
        queryHql.append(" WHERE ");
        queryHql.append(" expEvalJusTecHist.expedienteJusTec.idExpedienteJusTec = :idExpedienteJusTec ");
        queryHql.append(" AND expEvalJusTecHist.persona.idInteresado = :idPersona ");
        queryHql.append(" AND expEvalJusTecHist.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteJusTec", idExpedienteJusTec);
        params.put("idPersona", idPersona);
        
        ExpedienteEvalJusTecHistDTO executeUniqueHQLQuery =  (ExpedienteEvalJusTecHistDTO) super.executeUniqueHQLQuery(
                queryHql.toString(), params);
        
        return executeUniqueHQLQuery;

    }
    
    @Override
    @Transactional(readOnly = false)
    public void crearHistoricoExpEvalJus(ExpedienteEvalJusTec expedienteEvalJusTec) {

        ExpedienteEvalJusTecHist expedienteEvalJusTecHist = new ExpedienteEvalJusTecHist();
        ExpedienteEvalJusTec expedienteEvalJusTecAux = new ExpedienteEvalJusTec();
        expedienteEvalJusTecAux.setIdExpedienteEvalJusTec(expedienteEvalJusTec.getIdExpedienteEvalJusTec());
        expedienteEvalJusTecHist.setExpedienteEvalJusTec(expedienteEvalJusTecAux);
        expedienteEvalJusTecHist.setPersona(expedienteEvalJusTec.getPersona());
        expedienteEvalJusTecHist.setItNotificado(expedienteEvalJusTec.getItNotificado());
        if (Objects.nonNull(expedienteEvalJusTec.getDsComentario())) {
            expedienteEvalJusTecHist.setDsComentario(expedienteEvalJusTec.getDsComentario());
        }
        if (Objects.nonNull(expedienteEvalJusTec.getNmNotaFinal())) {
            expedienteEvalJusTecHist.setNmNotaFinal(expedienteEvalJusTec.getNmNotaFinal());
        }
        CatalogosValores estado = new CatalogosValores();
        estado = expedienteEvalJusTec.getEstado();
        expedienteEvalJusTecHist.setEstado(estado);
        ExpedienteJusTec expedienteJustec = expedienteEvalJusTec.getExpedienteJustec();
        expedienteEvalJusTecHist.setExpedienteJusTec(expedienteJustec);
        if (Objects.nonNull(expedienteEvalJusTec.getFcIniPlazo())) {
            expedienteEvalJusTecHist.setFcIniPlazo(expedienteEvalJusTec.getFcIniPlazo());
        }
        if (Objects.nonNull(expedienteEvalJusTec.getFcFinPlazo())) {
        expedienteEvalJusTecHist.setFcFinPlazo(expedienteEvalJusTec.getFcFinPlazo());
        }
        // Auditoria
        expedienteEvalJusTecHist.setFcAltaFila(new Date());
        if (Objects.nonNull(expedienteEvalJusTec.getCdUsuModif())) {
            expedienteEvalJusTecHist.setCdUsuAlta(expedienteEvalJusTec.getCdUsuModif());
        } else {
            expedienteEvalJusTecHist.setCdUsuAlta(expedienteEvalJusTec.getCdUsuAlta());
        }
        
        if (Objects.nonNull(expedienteEvalJusTec.getObsEval())) {
            expedienteEvalJusTecHist.setObsEval(expedienteEvalJusTec.getObsEval());
        }
        
        if (Objects.nonNull(expedienteEvalJusTec.getObsGest())) {
            expedienteEvalJusTecHist.setObsGest(expedienteEvalJusTec.getObsGest());
        }

        this.insert(expedienteEvalJusTecHist);
    }
 
}