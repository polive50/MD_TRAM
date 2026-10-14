package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio TipoResolucionConvocatoria
 *
 * @author quental
 */
@Repository("tipoResolModalidadDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class TipoResolModalidadDAOImpl extends BaseDAOImpl<TipoResolModalidad, Integer>
    implements TipoResolModalidadDAO {

    @Override
    public TipoResolModalidad findByIdModalidad(Integer idModalidad) {
        TipoResolModalidad tipoResolModalidad = null;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter("idModalidad.idValor", idModalidad, Operador.EQUALS));
        filtros.add(new AtlasFilter("fcBajaFila", Operador.NULL));
        List<TipoResolModalidad> lstTipoResolModalidad = super.find(filtros, null);
        if (lstTipoResolModalidad != null && !lstTipoResolModalidad.isEmpty()) {
            tipoResolModalidad = lstTipoResolModalidad.get(0);
        }
        return tipoResolModalidad;
    }

}
