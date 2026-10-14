package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaCauContDAO;
import org.madrid.saii.lib.domain.ConvocatoriaCauCont;
import org.madrid.saii.lib.dto.ConvocatoriaCauContDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCauCont
 *
 * @author nttdata
 */
public interface ConvocatoriaCauContService
        extends BaseService<ConvocatoriaCauCont, Integer, ConvocatoriaCauContDAO> {

    /**
     * Buscar por id ConvocatoriaCauCont.
     *
     * @param idConvocatoriaCauCont the id ConvocatoriaCauCont
     * @return ConvocatoriaCauContDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCauContDTO buscarId(Integer idConvocatoriaCauCont) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCauCont.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaCauContDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCauContDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ConvocatoriaCauCont.
     *
     * @param ConvocatoriaCauContDTO the ConvocatoriaCauCont DTO
     * @return ConvocatoriaCauContDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaCauContDTO agregar(Integer idConvocatoria, Integer idCausaCont)
            throws ServiceException;
    
    /**
     * Buscar por id idConvocatoria.
     *
     * @param idConvocatoria the id Convocatoria
     * @return List<ConvocatoriaCauCont>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaCauCont> buscarIdConvocatoria(Integer idConvocatoria) throws ServiceException;

}
