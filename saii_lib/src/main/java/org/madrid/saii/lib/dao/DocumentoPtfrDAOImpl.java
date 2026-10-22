
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.madrid.saii.lib.dto.converters.DocumentoPtfrToDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio DocumentoPtfr
 *
 * @author everis
 */
@Repository("documentoPtfrDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class DocumentoPtfrDAOImpl extends BaseDAOImpl<DocumentoPtfr, Integer>
        implements DocumentoPtfrDAO {

    @Override
    public List<DocumentoPtfrDTO> obtenerListaHistoricoNotif(Integer idExpediente) {

        List<DocumentoPtfrDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("Select dp ");
        queryHql.append(" from DocumentoPtfr dp ");
        queryHql.append(" where dp.idExpediente = " + idExpediente + " ");
        queryHql.append(" and (dp.fcEnvioNote is not null or dp.fcEnvioCorreo is not null)");

        @SuppressWarnings("unchecked")
        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), new HashMap<>());

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            DocumentoPtfr docPtfr = (DocumentoPtfr) it.next();
            DocumentoPtfrDTO docPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(docPtfr);
            if (Objects.nonNull(docPtfr.getFcEnvioNote())) {
                docPtfrDTO.setFcEnvio(docPtfr.getFcEnvioNote());
            } else {
                docPtfrDTO.setFcEnvio(docPtfr.getFcEnvioCorreo());
            }

            lst.add(docPtfrDTO);
        }

        return lst;
    }

    
    /**
     * 
     * @param idExpediente
     * @param lstTramites
     * @return
     */
    @Override
    public List<DocumentoPtfrDTO> obtenerPorExpedienteYTramites(Integer idExpediente, List<Integer> lstTramites) {
        
        List<DocumentoPtfrDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT dp ");
        queryHql.append(" FROM DocumentoPtfr dp ");
        queryHql.append(" WHERE dp.idExpediente = :idExpediente ");
        queryHql.append(" AND dp.tramite.idValor IN (:idTramites) ");
        queryHql.append(" AND dp.fcBajaFila IS NULL ");
        queryHql.append(" ORDER BY idDocumentoPtfr DESC ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idTramites", lstTramites);

        List<?> lstDocumentoPtfr = super.executeHQLQueryList(queryHql.toString(), params);

        if (lstDocumentoPtfr != null && !lstDocumentoPtfr.isEmpty()) {
            for (Object object : lstDocumentoPtfr) {
                DocumentoPtfr docPtfr = (DocumentoPtfr) object;
                DocumentoPtfrDTO docPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(docPtfr);
                lst.add(docPtfrDTO);
            }
        }

        return lst;

    }

    
    @Override
    public List<DocumentoPtfrDTO> obtenerPorExpedienteModifPresupPlazosTramites(Integer idExpediente, Integer tramite, Integer idModificacion) {
        
        List<DocumentoPtfrDTO> lst = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();

        queryHql.append(" SELECT dp ");
        queryHql.append(" FROM DocumentoPtfr dp ");
        queryHql.append(" WHERE dp.idExpediente = :idExpediente ");
        queryHql.append(" AND dp.tramite.idValor = :idTramite ");
        queryHql.append(" AND dp.idPresupuestoModif = :idModificacion ");
        queryHql.append(" AND dp.fcBajaFila IS NULL ");
        queryHql.append(" ORDER BY idDocumentoPtfr DESC ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("idTramite", tramite);
        params.put("idModificacion", idModificacion);

        List<?> lstDocumentoPtfr = super.executeHQLQueryList(queryHql.toString(), params);

        if (lstDocumentoPtfr != null && !lstDocumentoPtfr.isEmpty()) {
            for (Object object : lstDocumentoPtfr) {
                DocumentoPtfr docPtfr = (DocumentoPtfr) object;
                DocumentoPtfrDTO docPtfrDTO = DocumentoPtfrToDTO.INSTANCE.apply(docPtfr);
                lst.add(docPtfrDTO);
            }
        }

        return lst;

    }
    
    @Override
    @SuppressWarnings("unchecked")
    public List<Integer> obtenerTramitesResolucionEnviados(Integer idConvocatoria,
            List<Integer> tramites) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT DISTINCT dp.tramite.nmConstante ");
        queryHql.append(" FROM DocumentoPtfr dp, Expediente exp ");
        queryHql.append(" WHERE dp.idExpediente = exp.idExpediente ");
        queryHql.append(" AND exp.solicitud.convocatoria.idConvocatoria = :idConvocatoria ");
        queryHql.append(" AND dp.tramite.nmConstante IN (:tramites) ");
        queryHql.append(" AND dp.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("tramites", tramites);
        return (List<Integer>) super.executeHQLQueryList(queryHql.toString(), params);
    }

    
}
