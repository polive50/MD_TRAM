package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.madrid.saii.lib.domain.ExpedientePresup;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ExpedienteFin
 *
 * @author nttdata
 */
@Repository("expedientePresupDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ExpedientePresupDAOImpl extends BaseDAOImpl<ExpedientePresup, Integer>
        implements ExpedientePresupDAO {

    @Override
    @SuppressWarnings("unchecked")
    public Map<Integer, Integer> obtenerTiposFinanciacionPorIds(Set<Integer> idsExpedientePresup) {
        Map<Integer, Integer> mapaTipos = new HashMap<>();

        if (idsExpedientePresup == null || idsExpedientePresup.isEmpty()) {
            return mapaTipos;
        }

        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT ep.idExpedientePresup, ep.tipoFinanciacion.idValor ");
        queryHql.append("FROM ExpedientePresup ep ");
        queryHql.append("WHERE ep.idExpedientePresup IN (:idsExpedientePresup)");

        Map<String, Object> params = new HashMap<>();
        params.put("idsExpedientePresup", idsExpedientePresup);

        List<Object[]> resultados = (List<Object[]>) super.executeHQLQueryList(queryHql.toString(), params);

        for (Object[] fila : resultados) {
            Integer id = (Integer) fila[0];
            Integer tipoFinanciacion = (Integer) fila[1];
            mapaTipos.put(id, tipoFinanciacion);
        }

        return mapaTipos;
    }

}
