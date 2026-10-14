package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.madrid.saii.lib.domain.Dato;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Dato
 *
 * @author everis
 */
@Repository("datoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class DatoDAOImpl extends BaseDAOImpl<Dato, Integer> implements DatoDAO {

    @SuppressWarnings("unchecked")
    @Override
    public Map<String, String> obtenerValoresDatos(Integer idExpediente,
            List<Integer> lstIdComponente, List<Integer> lstIdTipoFinanciacion) {

        Map<String, String> mapaDatos = new HashMap<>();

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("SELECT ");
        queryHql.append("bco.componente.idComponente, ");
        queryHql.append("da.dsValor ");
        queryHql.append("FROM ");
        queryHql.append("Dato da ");
        queryHql.append("INNER JOIN BloqueComponente bco ");
        //VMM 01/10/24 MD-673: la entidad Dato ahora tiene una entidad bloqueComponente y expediente
        queryHql.append("ON bco.idBloqueComponente = da.bloqueComponente.idBloqueComponente ");
        queryHql.append("INNER JOIN Expediente ex ");
        queryHql.append("ON ex.idExpediente = da.expediente.idExpediente ");
        queryHql.append("WHERE ex.idExpediente = :idExpediente ");
        queryHql.append("AND da.fcBajaFila IS NULL ");

        if (!lstIdComponente.isEmpty() && !lstIdTipoFinanciacion.isEmpty()) {
            queryHql.append("AND (bco.componente.idComponente IN (:lstIdComponente) OR ");
            queryHql.append("bco.tipoFinanciacion.idValor IN (:lstIdTipoFinanciacion) ) ");
        } else if (!lstIdComponente.isEmpty() && lstIdTipoFinanciacion.isEmpty()) {
            queryHql.append("and bco.componente.idComponente in (:lstIdComponente) ");
        } else if (lstIdComponente.isEmpty() && !lstIdTipoFinanciacion.isEmpty()) {
            queryHql.append("and bco.tipoFinanciacion.idValor in (:lstIdTipoFinanciacion) ");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("idExpediente", idExpediente);
        params.put("lstIdComponente", lstIdComponente);
        params.put("lstIdTipoFinanciacion", lstIdTipoFinanciacion);

        List<Object[]> executeHQLQueryList = (List<Object[]>) super.executeHQLQueryList(
                queryHql.toString(), params);

        Iterator<?> it = executeHQLQueryList.iterator();

        while (it.hasNext()) {
            Object[] object = (Object[]) it.next();

            String key = Objects.nonNull(object[0]) ? String.valueOf(object[0]) : "";
            String value = Objects.nonNull(object[1]) ? String.valueOf(object[1]) : "";

            mapaDatos.put(key, value);

        }

        return mapaDatos;
    }

}
