package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Entidad;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementacion para el DAO de la clase de dominio Entidad
 *
 * @author everis
 */
@Repository("entidadDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EntidadDAOImpl extends BaseDAOImpl<Entidad, Integer> implements EntidadDAO {

    @SuppressWarnings("unchecked")
    @Override
    public List<Entidad> buscarTodos() {
        StringBuilder queryHql = new StringBuilder();
        queryHql.append("SELECT e FROM Entidad e WHERE e.fcBajaFila IS NULL ORDER BY e.interesado.dsNombre");
        return (List<Entidad>) super.executeHQLQueryList(queryHql.toString(),null);
    }

}
