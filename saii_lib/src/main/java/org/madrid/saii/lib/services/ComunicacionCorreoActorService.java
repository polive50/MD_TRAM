package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ComunicacionCorreoActorDAO;
import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaCom
 *
 * @author nttdata
 */
public interface ComunicacionCorreoActorService
        extends BaseService<ComunicacionCorreoActor, Integer, ComunicacionCorreoActorDAO> {

    /**
     * Buscar por id ConvocatoriaCom.
     *
     * @param idConvocatoriaCom the id ConvocatoriaCom
     * @return ConvocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ComunicacionCorreoActorDTO buscarId(Integer idConvocatoriaCom) throws ServiceException;

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
    List<ComunicacionCorreoActorDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaCom.
     *
     * @param convocatoriaComDTO the convocatoriaCom DTO
     * @return convocatoriaComDTO
     * @throws ServiceException the service exception
     */
    ComunicacionCorreoActorDTO guardar(ComunicacionCorreoActorDTO convocatoriaComDTO) throws ServiceException;

    /**
    *
    */
    List<ComunicacionCorreoActorDTO> buscarComunicacionCorreoActorPorIdConvocatoriaCom(Integer idConvocatoriaCom) throws ServiceException;
    
    /**
    *
    */
    void borradoLogico(final ComunicacionCorreoActorDTO comunicacionCorreoActorDTO) throws ServiceException;

   

}
