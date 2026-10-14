package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.ConvocatoriaGestor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaGestor
 *
 * @author nttdata
 */
@Repository("convocatoriaGestorDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaGestorDAOImpl extends BaseDAOImpl<ConvocatoriaGestor, Integer>
        implements ConvocatoriaGestorDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Integer> obtenerTiposTramite(Integer idConvocatoria, String login) {

        List<Integer> lstTipoTramite = new ArrayList<>();

        StringBuilder queryHql = new StringBuilder();
        queryHql.append(" SELECT cg.tipoTramite ");
        queryHql.append(" FROM ConvocatoriaGestor cg ");
        queryHql.append(" WHERE cg.fcBajaFila IS NULL ");
        queryHql.append(" AND cg.tipoTramite IS NOT NULL ");
        queryHql.append(" AND cg.cdUsuario IS NOT NULL ");
        queryHql.append(" AND cg.idConvocatoria = :idConvocatoria ");
        queryHql.append(" AND cg.cdUsuario = :cdUsuario ");

        Map<String, Object> params = new HashMap<>();
        params.put("idConvocatoria", idConvocatoria);
        params.put("cdUsuario", login);

        List<Object> resultado = (List<Object>) super.executeHQLQueryList(queryHql.toString(),
                params);
        for (Object obj : resultado) {
            lstTipoTramite.add(Integer.valueOf(obj.toString()));
        }

        return lstTipoTramite;
    }

}
