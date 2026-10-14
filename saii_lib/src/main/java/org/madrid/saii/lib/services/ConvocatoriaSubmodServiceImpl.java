package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaSubmodDAO;
import org.madrid.saii.lib.domain.ConvocatoriaSubmod;
import org.madrid.saii.lib.dto.ConvocatoriaSubmodDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaSubmodToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaSubmodToEntity;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio para la clase de dominio ConvocatoriaSubmod
 *
 * @author nttdata
 */
@Service("convocatoriaSubmodService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaSubmodServiceImpl
        extends BaseServiceImpl<ConvocatoriaSubmod, Integer, ConvocatoriaSubmodDAO>
        implements ConvocatoriaSubmodService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaSubmodDAO
     */
    @Autowired
    public ConvocatoriaSubmodServiceImpl(
            @Qualifier("convocatoriaSubmodDAO") ConvocatoriaSubmodDAO convocatoriaSubmodDAO) {
        super();
        this.setDao(convocatoriaSubmodDAO);
    }

    /**
     *
     */
    @Override
    public ConvocatoriaSubmodDTO buscarId(Integer idConvocatoriaSubmod) throws ServiceException {
        return ConvocatoriaSubmodToDTO.INSTANCE.apply(this.getDao().find(idConvocatoriaSubmod));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaSubmodDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaSubmodToDTO.INSTANCE));
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaSubmodDTO guardar(ConvocatoriaSubmodDTO convocatoriaSubmodDTO)
            throws ServiceException {
        return ConvocatoriaSubmodToDTO.INSTANCE.apply(this
                .insertOrUpdate(ConvocatoriaSubmodToEntity.INSTANCE.apply(convocatoriaSubmodDTO)));
    }

    /**
     *
     */
    @Override
    public List<ConvocatoriaSubmodDTO> obtenerSubmodalidadesPorConvocatoria(Integer idConvocatoria)
            throws ServiceException {
        List<ConvocatoriaSubmodDTO> listaDTO = null;

        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaSubmod> lista = this.find(filtros, null);

        if (lista != null && !lista.isEmpty()) {
            listaDTO = new ArrayList<>();
            for (ConvocatoriaSubmod submod : lista) {
                listaDTO.add(ConvocatoriaSubmodToDTO.INSTANCE.apply(submod));
            }
        }

        return listaDTO;
    }

}
