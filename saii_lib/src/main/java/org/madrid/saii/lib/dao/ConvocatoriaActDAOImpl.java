
package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ConvocatoriaAct;
import org.madrid.saii.lib.dto.ConvocatoriaActDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaActToDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaAct
 *
 * @author everis
 */
@Repository("convocatoriaActDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaActDAOImpl extends BaseDAOImpl<ConvocatoriaAct, Integer>
        implements ConvocatoriaActDAO {

    @Override
    public List<ConvocatoriaActDTO> buscarActoresAsociados(ConvocatoriaActDTO convocatoriaActDTO) {
        
        List<ConvocatoriaActDTO> lstConvocatoriaActDTO = new ArrayList<>();
        
        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ca ");
        queryHql.append("FROM ConvocatoriaAct ca ");
        queryHql.append("WHERE ca.idConvocatoria = :idConvocatoria ");
        queryHql.append(
                "AND (ca.idActorSolicitante = :idConvocatoriaAct OR ca.idActorCoordinador = :idConvocatoriaAct) ");
        queryHql.append("AND ca.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", convocatoriaActDTO.getIdConvocatoria());
        params.put("idConvocatoriaAct", convocatoriaActDTO.getIdActor());

        @SuppressWarnings("unchecked")
        List<ConvocatoriaAct> executeHQLQueryList = (List<ConvocatoriaAct>) super.executeHQLQueryList(
                queryHql.toString(), params);
        
        for (ConvocatoriaAct ca : executeHQLQueryList) {
            lstConvocatoriaActDTO.add(ConvocatoriaActToDTO.INSTANCE.apply(ca));
        }

        return lstConvocatoriaActDTO;
    }

}
