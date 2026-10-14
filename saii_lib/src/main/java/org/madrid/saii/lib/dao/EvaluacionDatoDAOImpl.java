package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.dto.EvaluacionDatoDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio EvaluacionDato
 *
 * @author nttdata
 */
@Repository("evaluacionDatoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionDatoDAOImpl extends BaseDAOImpl<EvaluacionDatoDTO, Integer>
        implements EvaluacionDatoDAO {

    /**
    *
    */
    @SuppressWarnings("unchecked")
    @Override
    public List<EvaluacionDatoDTO> obtenerEvaluacionDato(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ");
        queryHql.append("al.idAlegacion, ");
        queryHql.append("al.tipo.idValor, ");
        queryHql.append("al.nmOrden, ");
        queryHql.append("al.dsTitulo ");
        queryHql.append("FROM ");
        queryHql.append("Alegacion al ");
        queryHql.append("INNER JOIN Solicitud sol ");
        queryHql.append("ON sol.convocatoria.idConvocatoria = al.idConvocatoria ");
        queryHql.append("INNER JOIN Expediente ex ");
        queryHql.append("ON ex.solicitud.idSolicitud = sol.idSolicitud ");
        queryHql.append("WHERE  ");
        queryHql.append("ex.idExpediente = :idExpediente AND al.cdUsuBaja is null ");
        queryHql.append("ORDER BY al.nmOrden ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);
        Iterator<?> it = executeHQLQueryList.iterator();

        List<EvaluacionDatoDTO> lstEvaluacionDatoDTO = new ArrayList<>();
        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            EvaluacionDatoDTO evalDatoDTO = new EvaluacionDatoDTO();
            evalDatoDTO.setIdAlegacion((Integer) object[0]);
            evalDatoDTO.setIdTipo((Integer) object[1]);
            evalDatoDTO.setNmOrden((Integer) object[2]);
            evalDatoDTO.setDsTitulo((String) object[3]);

            lstEvaluacionDatoDTO.add(evalDatoDTO);
        }

        return lstEvaluacionDatoDTO;
    }

}
