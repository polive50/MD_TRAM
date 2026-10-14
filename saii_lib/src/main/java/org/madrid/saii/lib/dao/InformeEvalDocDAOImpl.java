package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.InformeEvalDoc;
import org.madrid.saii.lib.dto.InformeEvalDocDTO;
import org.madrid.saii.lib.dto.converters.InformeEvalDocToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio InformeEvalDoc
 *
 * @author quental
 */
@Repository("informeEvalDocDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class InformeEvalDocDAOImpl extends BaseDAOImpl<InformeEvalDoc, Integer>
        implements InformeEvalDocDAO {

    @Override
    public InformeEvalDocDTO obtenerPorExpedienteEvalEInforme(Integer idExpedienteEval, Integer idInforme) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ied");
        queryHql.append("   FROM InformeEvalDoc ied");
        queryHql.append("  WHERE ied.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idExpedienteEval.idExpedienteEval = :idExpedienteEval");
        queryHql.append("    AND ied.idInforme.idInforme = :idInforme");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", idExpedienteEval);
        params.put("idInforme", idInforme);

        @SuppressWarnings("unchecked")
        List<InformeEvalDoc> result = (List<InformeEvalDoc>) super.executeHQLQueryList(queryHql.toString(), params);

        if (result == null || result.isEmpty()) {
            return null;
        }
        return InformeEvalDocToDTO.INSTANCE.apply(result.get(0));
    }

    @Override
    public List<InformeEvalDocDTO> obtenerListaPorExpedienteEval(Integer idExpedienteEval) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ied");
        queryHql.append("   FROM InformeEvalDoc ied");
        queryHql.append("  WHERE ied.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idExpedienteEval.idExpedienteEval = :idExpedienteEval");
        queryHql.append("    AND ied.idEstado.nmConstante = :nmConstante");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", idExpedienteEval);
        params.put("nmConstante", Constantes.EstadoEvaluador.CERRADO_EVALUADOR);

        @SuppressWarnings("unchecked")
        List<InformeEvalDoc> result = (List<InformeEvalDoc>) super.executeHQLQueryList(queryHql.toString(), params);

        List<InformeEvalDocDTO> tempDTO = new ArrayList<>();
        if (result != null) {
            for (InformeEvalDoc ied : result) {
                tempDTO.add(InformeEvalDocToDTO.INSTANCE.apply(ied));
            }
        }
        return tempDTO;
    }
    
    
    @Override
    public List<InformeEvalDocDTO> obtenerListaPorExpedienteEvalSinEstado(Integer idExpedienteEval) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ied");
        queryHql.append("   FROM InformeEvalDoc ied");
        queryHql.append("  WHERE ied.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idExpedienteEval.idExpedienteEval = :idExpedienteEval");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", idExpedienteEval);
        
        @SuppressWarnings("unchecked")
        List<InformeEvalDoc> result = (List<InformeEvalDoc>) super.executeHQLQueryList(queryHql.toString(), params);

        List<InformeEvalDocDTO> tempDTO = new ArrayList<>();
        if (result != null) {
            for (InformeEvalDoc ied : result) {
                tempDTO.add(InformeEvalDocToDTO.INSTANCE.apply(ied));
            }
        }
        return tempDTO;
    }

    @Override
    public InformeEvalDocDTO obtenerPorExpedienteEvalInformeItFirmado(Integer idExpedienteEval,
            Integer idInforme, String itFirmado) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ied");
        queryHql.append("   FROM InformeEvalDoc ied");
        queryHql.append("  WHERE ied.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idExpedienteEval.idExpedienteEval = :idExpedienteEval");
        queryHql.append("    AND ied.idInforme.idInforme = :idInforme");
        if(itFirmado != null) {
            queryHql.append("    AND ied.itFirmado = :itFirmado");
        }else {
            queryHql.append("    AND ied.itFirmado IS NULL");
        }
       

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEval", idExpedienteEval);
        params.put("idInforme", idInforme);
        if(itFirmado != null) {
            params.put("itFirmado", itFirmado);
        }
       

        @SuppressWarnings("unchecked")
        List<InformeEvalDoc> result = (List<InformeEvalDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);

        if (result == null || result.isEmpty()) {
            return null;
        }
        return InformeEvalDocToDTO.INSTANCE.apply(result.get(0));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<InformeEvalDocDTO> obtenerIdExpedienteItCoorEvalItFirmado(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ied ");
        queryHql.append(" FROM InformeEvalDoc ied ");
        queryHql.append(" WHERE ied.fcBajaFila IS NULL ");
        queryHql.append(" AND ied.idExpedienteEval.fcBajaFila IS NULL ");
        queryHql.append(" AND ied.idExpedienteEval.idExpediente = :idExpediente ");
//        queryHql.append(" AND ied.idExpedienteEval.itCoorEval = :itCoorEval ");
        queryHql.append(" AND ied.itFirmado IS NULL ");
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
//        params.put("itCoorEval", itCoorEval);
//        params.put("itFirmado", itFirmado);
        List<InformeEvalDoc> result = (List<InformeEvalDoc>) super.executeHQLQueryList(
                queryHql.toString(), params);
        List<InformeEvalDocDTO> tempDTO = new ArrayList<>();
        if (result != null) {
            for (InformeEvalDoc ied : result) {
                tempDTO.add(InformeEvalDocToDTO.INSTANCE.apply(ied));
            }
        }
        return tempDTO;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<InformeEvalDocDTO> obtenerListaInformesEvaluadoresPorExpediente(Integer idExpedienteEvalPadre) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT ied");
        queryHql.append("   FROM InformeEvalDoc ied");
        queryHql.append("  WHERE ied.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idExpedienteEval.fcBajaFila IS NULL");
        queryHql.append("    AND ied.idExpedienteEval.idExpedienteEvalPadre = :idExpedienteEvalPadre");
        queryHql.append("    AND ied.idExpedienteEval.itCoorEval = :itCoorEval");
        queryHql.append("    AND ied.itFirmado = :itFirmado");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteEvalPadre", idExpedienteEvalPadre);
        params.put("itCoorEval", Constantes.TipoCoorEval.EVALUADOR);
        params.put("itFirmado", Constantes.AfirmacionNegacion.N);

        List<InformeEvalDoc> result = (List<InformeEvalDoc>) super.executeHQLQueryList(queryHql.toString(), params);

        List<InformeEvalDocDTO> tempDTO = new ArrayList<>();
        if (result != null) {
            for (InformeEvalDoc ied : result) {
                tempDTO.add(InformeEvalDocToDTO.INSTANCE.apply(ied));
            }
        }
        return tempDTO;
    }

}
