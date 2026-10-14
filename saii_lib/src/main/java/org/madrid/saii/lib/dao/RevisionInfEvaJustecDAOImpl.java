package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.RevisionInfEvaJustec;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio RevisionInfEvaJustec
 *
 * @author quental
 */
@Repository("revisionInfEvaJustecDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class RevisionInfEvaJustecDAOImpl extends BaseDAOImpl<RevisionInfEvaJustec, Integer>
        implements RevisionInfEvaJustecDAO {

    @Override
    public List<RevisionInfEvaJustec> findByIdRevInfEvaJustec(Integer idRevInfEvaJustec) {
        StringBuilder queryHql = new StringBuilder();

        // Construye la consulta HQL
        queryHql.append("from Revision rev "); // Asegúrate de que 'Revision'
                                                // sea el nombre correcto de la
                                                // entidad
        queryHql.append("where rev.idExpediente = :idRevision "); // Asume que
                                                                   // 'idRevision'
                                                                   // es el
                                                                   // campo en
                                                                   // la entidad
                                                                   // Expediente
        queryHql.append("and rev.fcBajaFila IS NULL"); // Omitir registros dados
                                                      // de baja

        // Crea un mapa para los parámetros
        Map<String, Object> params = new HashMap<>();
        params.put("idRevision", idRevInfEvaJustec);

        // Ejecuta la consulta HQL
        @SuppressWarnings("unchecked")
        List<RevisionInfEvaJustec> executeHQLQueryList = (List<RevisionInfEvaJustec>) super.executeHQLQueryList(
                queryHql.toString(), params);

        return executeHQLQueryList;
    }

}
