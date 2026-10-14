
package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.UnidadAdmin;
import org.madrid.saii.lib.dto.GcatusuarsDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO
 *
 * @author everis
 */
@Repository("gcatusuarsDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class GcatusuarsDAOImpl extends BaseDAOImpl<UnidadAdmin, Integer> implements GcatusuarsDAO {

    /**
     *
     */
    @Override
    public GcatusuarsDTO buscarCdUsuario(String cdUsuario) {

        GcatusuarsDTO gcatusuarsDTO = new GcatusuarsDTO();

        StringBuilder querySql = new StringBuilder();

        querySql.append("SELECT u.CDUSUARIO, ");
        querySql.append("u.CDUNIDAD, ");
        querySql.append("a.DSEXTEN ");
        querySql.append("FROM ");
        querySql.append("GCATUSUARS u, ");
        querySql.append("GENTIADMIN a ");
        querySql.append("WHERE ");
        querySql.append("u.CDUNIDAD = a.CDUNIDAD ");
        querySql.append("AND u.CDUSUARIO IN (:cdUsuario) ");

        Map<String, Object> params = new HashMap<>();
        params.put("cdUsuario", cdUsuario);

        List<?> lst = super.executeSQLQueryList(querySql.toString(), params);

        if (!lst.isEmpty()) {
            Object[] object = (Object[]) lst.get(0);
            gcatusuarsDTO.setCdusuario(cdUsuario);
            gcatusuarsDTO.setCdunidad(object[1].toString());
            gcatusuarsDTO.setDsnombre(object[2].toString());
        }

        return gcatusuarsDTO;
    }

    @Override
    public String descripcionUnidad(String cdUnidad) {

        String strDesUnidad = StringUtils.EMPTY;

        StringBuilder querySql = new StringBuilder();

        querySql.append("SELECT DSEXTEN ");
        querySql.append("FROM ");
        querySql.append("GENTIADMIN ");
        querySql.append("WHERE ");
        querySql.append("CDUNIDAD = :cdUnidad ");

        Map<String, Object> params = new HashMap<>();
        params.put("cdUnidad", cdUnidad);

        List<?> lst = super.executeSQLQueryList(querySql.toString(), params);

        if (!lst.isEmpty()) {
            strDesUnidad = lst.get(0).toString();
        }

        return strDesUnidad;
    }

}
