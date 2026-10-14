package org.madrid.saii.lib.dao;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.domain.ConvocatoriaEntidadCerrada;
import org.madrid.saii.lib.dto.ConvocatoriaEntidadCerradaDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEntidadCerradaToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio ConvocatoriaEntidadCerrada
 *
 * @author quental
 */
@Repository("convocatoriaEntidadCerradaDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConvocatoriaEntidadCerradaDAOImpl extends
        BaseDAOImpl<ConvocatoriaEntidadCerrada, Integer> implements ConvocatoriaEntidadCerradaDAO {

    @Override
    public List<ConvocatoriaEntidadCerradaDTO> obtenerPorConvocatoria(Integer idConvocatoria) {
        List<ConvocatoriaEntidadCerradaDTO> lstConvEntCer;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CONVOCATORIA_IDCONVOCATORIA,
                idConvocatoria, Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
        List<ConvocatoriaEntidadCerrada> asd = this.find(filtros, null);
        lstConvEntCer = new ArrayList<>(
                Lists.transform(asd, ConvocatoriaEntidadCerradaToDTO.INSTANCE));
        return lstConvEntCer;
    }

}
