package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusEcoDAO;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteJusEcoService
 *
 * @author quental
 */
public interface ExpedienteJusEcoService
        extends BaseService<ExpedienteJusEco, Integer, ExpedienteJusEcoDAO> {

    /**
     * Buscar paginadamente ExpedienteJusEco.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteJusEcoDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteJusEcoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ExpedienteJusEco.
     *
     * @param ExpedienteJusEcoDTO the ExpedienteJusEco DTO
     * @return ExpedienteJusEcoDTO
     * @throws ServiceException the service exception
     */
    ExpedienteJusEcoDTO guardar(ExpedienteJusEcoDTO expedienteJusEcoDTO) throws ServiceException;

    /**
     * Método que busca expedientes por id de expediente 
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusEcoDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    List<ExpedienteJusEcoDocDTO> buscarDocs(int index, int pageSize, List<AtlasFilter> filtrosEntrada,
            List<AtlasSort> ordenamiento) throws ServiceException;
    
    void guardarDocumentoJusEco(ExpedienteJusEcoDocDTO expedienteJusEcoDocDTO);
    
    /**
     * 
     * @param idExpedienteJuseco
     * @return
     * @throws ServiceException
     */
    ExpedienteJusEcoDTO buscarPorIdExpedienteJuseco(Integer idExpedienteJuseco)
            throws ServiceException;

    /**
     * 
     * @param expedienteJusEcoDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteJusEcoDTO update(ExpedienteJusEcoDTO expedienteJusEcoDTO) throws ServiceException;

    /**
     * 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusEcoDTO> buscarPeriodosPendientesSubsanarJusecoPorExpediente(
            Integer idExpediente) throws ServiceException;

    void borradoLogico(Integer idExpedienteJusEco) throws ServiceException;

    /**
      * Actualiza desde el batch el estado de los expdtes justec segun sus
      * estados previos y fechas de JCT
      *
      * @return
      * @throws ServiceException
      */
    void procesarPlazosJUSECO() throws ServiceException;

    /**
     *   Método para buscar ExpedienteJusEcoDTO por el id de expediente
     *   
     *   @param idExpediente id del expediente
     */
    List<ExpedienteJusEcoDTO> buscarPeriodosPendientesJusecoPorExpediente(Integer idExpediente,
            Integer idEstado) throws ServiceException;

    /**
      * Método para buscar ExpedienteJusTecDTO por el id de expediente
      * 
      * @param idExpediente id del expediente
      */
    List<ExpedienteJusEcoDTO> buscarPeriodosFinalizadoPlazoJusEcoPorExpediente(Integer idExpediente,
            Integer idEstado) throws ServiceException;

    /**
      *   Método para buscar ExpedienteJusEcoDTO por el id de expediente
      *   
      *   @param idExpediente id del expediente
      */
    List<ExpedienteJusEcoDTO> buscarPeriodosPendientesJusecoPorExpediente(Integer idExpediente)
            throws ServiceException;
}
