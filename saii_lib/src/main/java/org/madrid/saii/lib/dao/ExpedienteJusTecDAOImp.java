package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteJusTec
 *
 * @author nttdata
 */
@Repository("expedienteJusTecDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteJusTecDAOImp extends BaseDAOImpl<ExpedienteJusTec, Integer>
        implements ExpedienteJusTecDAO {

    @Override
    public List<ExpedienteJusTec> findByIdExpediente(Integer idExpediente) {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("from Expediente ex "); // Asegúrate de que 'Expediente'
                                                // sea el nombre correcto de la
                                                // entidad
        queryHql.append("where ex.idExpediente = :idExpediente "); // Asume que
                                                                   // 'idExpediente'
                                                                   // es el
                                                                   // campo en
                                                                   // la entidad
                                                                   // Expediente
        queryHql.append("and ex.fcBajaFila IS NULL"); // Omitir registros dados
                                                      // de baja

        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<ExpedienteJusTec> executeHQLQueryList = (List<ExpedienteJusTec>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public  List<ExpedienteJusTec> buscarExpJCT(Integer idExpedienteJusTec) {
        
        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT expJusTec");
        queryHql.append(" FROM ExpedienteJusTec expJusTec ");
        queryHql.append(" WHERE ");
        queryHql.append(" expJusTec.idExpedienteJusTec = :idExpedienteJusTec ");
        queryHql.append(" AND expJusTec.fcBajaFila IS NULL ");

        Map<String, Object> params = new HashMap<>();
        params.put("idExpedienteJusTec", idExpedienteJusTec);

        return (List<ExpedienteJusTec>) super.executeHQLQueryList(queryHql.toString(), params);
    }

}
