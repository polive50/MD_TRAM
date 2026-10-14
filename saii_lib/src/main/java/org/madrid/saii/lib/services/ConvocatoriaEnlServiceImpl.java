package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaEnlDAO;
import org.madrid.saii.lib.domain.ConvocatoriaEnl;
import org.madrid.saii.lib.dto.ConvocatoriaEnlDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEnlToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaEnlToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaEnl
 *
 * @author everis
 */
@Service("convocatoriaEnlService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaEnlServiceImpl
        extends BaseServiceImpl<ConvocatoriaEnl, Integer, ConvocatoriaEnlDAO>
        implements ConvocatoriaEnlService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param convocatoriaEnlDAO
     */
    @Autowired
    public ConvocatoriaEnlServiceImpl(
            @Qualifier("convocatoriaEnlDAO") ConvocatoriaEnlDAO convocatoriaEnlDAO) {
        super();
        this.setDao(convocatoriaEnlDAO);
    }

    /**
     * Buscar.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return the list
     * @throws ServiceException the service exception
     */
    @Override
    public List<ConvocatoriaEnlDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaEnlToDTO.INSTANCE));

    }

    /**
     * Guardar.
     *
     * @param ConvocatoriaEnlDTO the ConvocatoriaEnlDTO
     * @return the ConvocatoriaEnlDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaEnlDTO guardar(final ConvocatoriaEnlDTO convocatoriaEnlDTO)
            throws ServiceException {

        // Insertamos el enlace
        ConvocatoriaEnl convocatoriaEnl = this
                .insertOrUpdate(ConvocatoriaEnlToEntity.INSTANCE.apply(convocatoriaEnlDTO));
        return ConvocatoriaEnlToDTO.INSTANCE.apply(convocatoriaEnl);
    }

    /**
     * Buscar id.
     *
     * @param idEnlace the idEnlace
     * @return the ConvocatoriaEnlDTO DTO
     * @throws ServiceException the service exception
     */
    @Override
    public ConvocatoriaEnlDTO buscarId(final Integer idEnlace) throws ServiceException {

        if (idEnlace == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return ConvocatoriaEnlToDTO.INSTANCE.apply(this.find(idEnlace));
    }

    /**
     * Busca a los actores por id convocatoria que le pasamos por parametro.
     *
     * @param idActor the idActor
     * @return the ConvocatoriaEnlDTO
     * @throws ServiceException the service exception
     */
    @Override
    public List<ConvocatoriaEnlDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaEnlToDTO.INSTANCE));
    }
}
