package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ComunicacionNoteActorDAO;
import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.madrid.saii.lib.dto.ComunicacionNoteActorDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCom
 *
 * @author quental
 */
public interface ComunicacionNoteActorService
        extends BaseService<ComunicacionNoteActor, Integer, ComunicacionNoteActorDAO> {

    /**
     * Buscar por id ConvocatoriaCom.
     *
     * @param idConvocatoriaCom the id ConvocatoriaCom
     * @return ConvocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ComunicacionNoteActorDTO buscarId(Integer idConvocatoriaCom) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaCom.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaComDTO>
     * @throws ServiceException the service exception
     */
    List<ComunicacionNoteActorDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaCom.
     *
     * @param convocatoriaComDTO the convocatoriaCom DTO
     * @return convocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ComunicacionNoteActorDTO guardar(ComunicacionNoteActorDTO convocatoriaComDTO) throws ServiceException;

    /**
    *
    */
    List<ComunicacionNoteActorDTO> buscarComunicacionNoteActorPorIdConvocatoriaCom(Integer idConvocatoriaCom) throws ServiceException;

    /**
    *
    */
    void borradoLogico(final ComunicacionNoteActorDTO comunicacionNoteActorDTO) throws ServiceException;

}
