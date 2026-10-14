package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaFechaTramDAO;
import org.madrid.saii.lib.domain.ConvocatoriaFechaTram;
import org.madrid.saii.lib.dto.ConvocatoriaFechaTramDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaFechaTramToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaFechaTramToEntity;
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
 * Implementación del servicio para la clase de dominio ConvocatoriaFechaTram
 *
 * @author everis
 */
@Service("convocatoriaFechaTramService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class ConvocatoriaFechaTramServiceImpl
        extends BaseServiceImpl<ConvocatoriaFechaTram, Integer, ConvocatoriaFechaTramDAO>
        implements ConvocatoriaFechaTramService {

    /**
     * Constructor de la clase que recibe como parámetros el DAO asociado a la
     * entidad. Está anotado con @Autowired para realizar automáticamente la
     * inyección y así establecer el valor del DAO genérico en clase la clase
     * padre (BaseServiceImpl).
     *
     * @param ConvocatoriaFechaTramDAO
     */
    @Autowired
    public ConvocatoriaFechaTramServiceImpl(
            @Qualifier("convocatoriaFechaTramDAO") ConvocatoriaFechaTramDAO convocatoriaFechaTramDAO) {
        super();
        this.setDao(convocatoriaFechaTramDAO);
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
    public List<ConvocatoriaFechaTramDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException {
        return new ArrayList<>(Lists.transform(this.find(index, pageSize, filtros, orden),
                ConvocatoriaFechaTramToDTO.INSTANCE));

    }

    /**
     * Guardar.
     *
     * @param ConvocatoriaFechaTramDTO the ConvocatoriaFechaTramDTO
     * @return the ConvocatoriaFechaTramDTO
     * @throws ServiceException the service exception
     */
    @Override
    @Transactional(readOnly = false)
    public ConvocatoriaFechaTramDTO guardar(final ConvocatoriaFechaTramDTO convocatoriaFechaTramDTO)
            throws ServiceException {

        // Insertamos la convocatoriaFechaTram
        ConvocatoriaFechaTram convocatoriaFechaTram = this
                .insertOrUpdate(ConvocatoriaFechaTramToEntity.INSTANCE.apply(convocatoriaFechaTramDTO));
        return ConvocatoriaFechaTramToDTO.INSTANCE.apply(convocatoriaFechaTram);
    }

    /**
     * Buscar id.
     *
     * @param idConvocatoriaFechaTram the idConvocatoriaFechaTram
     * @return the ConvocatoriaFechaTramDTO DTO
     * @throws ServiceException the service exception
     */
    @Override
    public ConvocatoriaFechaTramDTO buscarId(final Integer idConvocatoriaFechaTram) throws ServiceException {

        if (idConvocatoriaFechaTram == null) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        return ConvocatoriaFechaTramToDTO.INSTANCE.apply(this.find(idConvocatoriaFechaTram));
    }

    /**
     * Busca a las ConvocatoriaFechaTram por id convocatoria que le pasamos por parametro.
     *
     * @param idConvocatoria the idConvocatoria
     * @return the ConvocatoriaFechaTramDTO
     * @throws ServiceException the service exception
     */
    @Override
    public List<ConvocatoriaFechaTramDTO> buscarPorIdConvocatoria(Integer idConvocatoria)
            throws ServiceException {

        List<AtlasFilter> filtros = new ArrayList<>();

        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA, idConvocatoria,
                Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        return new ArrayList<>(
                Lists.transform(this.find(filtros, null), ConvocatoriaFechaTramToDTO.INSTANCE));
    }
}
