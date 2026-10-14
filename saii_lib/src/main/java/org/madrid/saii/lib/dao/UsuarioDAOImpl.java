package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio UsuarioJur
 *
 * @author everis
 */
@Repository("usuarioDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UsuarioDAOImpl extends BaseDAOImpl<Usuario, Integer> implements UsuarioDAO {

    /**
    *
    */
    @Override
    public Boolean consultarTieneRolesUsuarioSaii(String cdUsuario) {

        Boolean coincidencias = Boolean.FALSE;
        StringBuilder querySql = new StringBuilder();

        querySql.append("SELECT (CASE WHEN EXISTS( ");
        querySql.append("select us.COD_USUARIO ");
        querySql.append("from usuario us ");
        querySql.append("inner join usuario_grupo gu ");
        querySql.append("on us.COD_USUARIO = gu.COD_USUARIO ");
        querySql.append("inner join grupo gr ");
        querySql.append("on gu.COD_GRUPO = gr.COD_GRUPO ");
        querySql.append("where gr.COD_APLICACION = 'SAII' ");
        querySql.append("and gu.COD_APLICACION = 'SAII' ");
        querySql.append("and us.COD_USUARIO = :codUsuario ");
        querySql.append("and us.FC_BAJA is null ");
        querySql.append(") THEN '1' ");
        querySql.append("ELSE '0' ");
        querySql.append("END) FROM dual ");

        Map<String, Object> params = new HashMap<>();
        params.put("codUsuario", cdUsuario);

        Object resultado = super.executeUniqueSQLQuery(querySql.toString(), params);

        String existe = String.valueOf(resultado);

        if (StringUtils.isNotBlank(existe) && "1".equals(existe)) {
            coincidencias = Boolean.TRUE;
        }

        return coincidencias;

    }

    /**
    *
    */
    @Override
    public Boolean consultarEsUsuarioGestor(String cdUsuario) {

        Boolean coincidencias = Boolean.FALSE;
        StringBuilder querySql = new StringBuilder();

        querySql.append("SELECT (CASE WHEN EXISTS( ");
        querySql.append("select us.COD_USUARIO ");
        querySql.append("from usuario us ");
        querySql.append("inner join usuario_grupo gu ");
        querySql.append("on us.COD_USUARIO = gu.COD_USUARIO ");
        querySql.append("inner join grupo gr ");
        querySql.append("on gu.COD_GRUPO = gr.COD_GRUPO ");
        querySql.append("where gr.COD_APLICACION = 'SAII' ");
        querySql.append("and gu.COD_APLICACION = 'SAII' ");
        querySql.append("and us.COD_USUARIO = :codUsuario ");
        querySql.append("and us.FC_BAJA is null ");
        querySql.append("and gr.COD_GRUPO IN  (4, 6) ");
        querySql.append(") THEN '1' ");
        querySql.append("ELSE '0' ");
        querySql.append("END) FROM dual ");

        Map<String, Object> params = new HashMap<>();
        params.put("codUsuario", cdUsuario);

        Object resultado = super.executeUniqueSQLQuery(querySql.toString(), params);

        String existe = String.valueOf(resultado);

        if (StringUtils.isNotBlank(existe) && "1".equals(existe)) {
            coincidencias = Boolean.TRUE;
        }

        return coincidencias;

    }

}
