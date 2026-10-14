package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.UsuarioJurFiltrosDTO;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio UsuarioJur
 *
 * @author everis
 */
@Repository("usuarioJurDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class UsuarioJurDAOImpl extends BaseDAOImpl<UsuarioJur, Integer> implements UsuarioJurDAO {

    /**
     *
     */
    @Override
    @SuppressWarnings("unchecked")
    public List<UsuarioJur> filtrar(UsuarioJurFiltrosDTO usuarioJurFiltrosDTO) {

        StringBuilder queryHql = new StringBuilder();

        queryHql.append("Select u from UsuarioJur as u,");
        queryHql.append(" Interesado as i, Interesado as i2");
        queryHql.append(" where u.interesado.idInteresado = i.idInteresado");
        queryHql.append(" and u.entidadJur.idInteresado = i2.idInteresado");
        queryHql.append(" and upper(i2.cdNumIden) = upper(:cif)");

        if (usuarioJurFiltrosDTO.getCdNumIden() != null) {
            queryHql.append(" and upper(i.cdNumIden) = upper(:cdNumIden)");
        }

        if (usuarioJurFiltrosDTO.getDsNombre() != null) {
            queryHql.append(" and upper(i.dsNombre) = upper(:dsNombre)");
        }

        if (usuarioJurFiltrosDTO.getDsApellido1() != null) {
            queryHql.append(" and upper(i.dsApellido1) = upper(:dsApellido1)");
        }

        if (usuarioJurFiltrosDTO.getDsApellido2() != null) {
            queryHql.append(" and upper(i.dsApellido2) = upper(:dsApellido2)");
        }

        Map<String, Object> params = new HashMap<>();
        params.put("cdNumIden", usuarioJurFiltrosDTO.getCdNumIden());
        params.put("dsNombre", usuarioJurFiltrosDTO.getDsNombre());
        params.put("dsApellido1", usuarioJurFiltrosDTO.getDsApellido1());
        params.put("dsApellido2", usuarioJurFiltrosDTO.getDsApellido2());
        params.put("cif", usuarioJurFiltrosDTO.getCif());

        return (List<UsuarioJur>) super.executeHQLQueryList(queryHql.toString(), params);
    }

}
