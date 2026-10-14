package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ExpedienteAnualidad;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteAnualidad
 *
 * @author Quental
 */
@Repository("expedienteAnualidadDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedienteAnualidadDAOImp extends BaseDAOImpl<ExpedienteAnualidad, Integer>
        implements ExpedienteAnualidadDAO {

    @Override
    public List<ExpedienteAnualidad> findByIdExpediente(Integer idExpediente) {
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
        List<ExpedienteAnualidad> executeHQLQueryList = (List<ExpedienteAnualidad>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

}
