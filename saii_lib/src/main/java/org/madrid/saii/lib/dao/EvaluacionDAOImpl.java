package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.Evaluacion;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio Evaluacion
 *
 * @author nttdata
 */
@Repository("evaluacionDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EvaluacionDAOImpl extends BaseDAOImpl<Evaluacion, Integer> implements EvaluacionDAO {

    /**
     * 
     */
    @Override
    public List<Evaluacion> buscarPorInforme(Integer idInforme) {
        List<AtlasFilter> lstFiltros = new ArrayList<>();
        lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_INFORME_ID_INFORME, idInforme,
                Operador.EQUALS));
        lstFiltros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        return this.find(lstFiltros, null);
    }

}
