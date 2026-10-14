package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.Apartado;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio Apartado
 *
 * @author nttdata
 */
@Repository("apartadoDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ApartadoDAOImpl extends BaseDAOImpl<Apartado, Integer> implements ApartadoDAO {

    @Override
    public List<Apartado> buscarPorEvaluacion(Integer idEvaluacion) {
        List<AtlasFilter> lstFiltros = new ArrayList<>();
        lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDEVALUACION, idEvaluacion,
                Operador.EQUALS));
        lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return this.find(lstFiltros, null);
    }

}
