package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConvocatoriaReqDAO;
import org.madrid.saii.lib.domain.ConvocatoriaReq;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaReqDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ConvocatoriaReq
 *
 * @author nttdata
 */
public interface ConvocatoriaReqService
        extends BaseService<ConvocatoriaReq, Integer, ConvocatoriaReqDAO> {

    /**
     * Buscar por id ConvocatoriaReq.
     *
     * @param idConvocatoriaReq the id ConvocatoriaReq
     * @return ConvocatoriaReqDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaReqDTO buscarId(Integer idConvocatoriaReq) throws ServiceException;

    /**
     * Buscar paginadamente ConvocatoriaReq.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ConvocatoriaReqDTO>
     * @throws ServiceException the service exception
     */
    List<ConvocatoriaReqDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a convocatoriaReq.
     *
     * @param convocatoriaReqDTO the convocatoriaReq DTO
     * @return convocatoriaReqDTO
     * @throws ServiceException the service exception
     */
    ConvocatoriaReqDTO guardar(ConvocatoriaReqDTO convocatoriaReqDTO) throws ServiceException;

    /**
     * Borrado logico ConvocatoriaReq
     *
     * @param ConvocatoriaReqDTO
     * @return ConvocatoriaReqDTO
     * @throws ServiceException
     */
    void borradoLogico(final ConvocatoriaReqDTO convocatoriareqDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param convocatoriareqDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ConvocatoriaReqDTO convocatoriareqDTO) throws ServiceException;

    /**
     * Borrado logico ConvocatoriaReq por idConvocatoria
     *
     * @param idConvocatoria
     * @throws ServiceException
     */
    ConvocatoriaDTO borradoLogicoPorConvocatoria(final Integer idConvocatoria)
            throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @param idRequisito
     * @return
     * @throws ServiceException
     */
    ConvocatoriaReqDTO agregar(Integer idConvocatoria, Integer idRequisito) throws ServiceException;

    /**
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    public List<ConvocatoriaReqDTO> obtenerRequisitoPorConvocatoria(Integer idConvocatoria)
            throws ServiceException;
    
    /**
     * 
     * @param idConvocatoria
     * @param idRequisito
     * @return
     * @throws ServiceException
     */
    public ConvocatoriaReqDTO buscarExistencia(final Integer idConvocatoria, final Integer idRequisito)
            throws ServiceException;
    
    /**
     * Guarda un/a convocatoriaReq.
     *
     * @param convocatoriaReq the convocatoriaReq 
     * @return convocatoriaReq
     * @throws ServiceException the service exception
     */
    ConvocatoriaReq guardarActor(ConvocatoriaReq convocatoriaReq) throws ServiceException;

    List<ConvocatoriaReqDTO> obtenerRequisitoPorConvocatoriaIdActor(Integer idConvocatoria,
            Integer idActor) throws ServiceException;

}
