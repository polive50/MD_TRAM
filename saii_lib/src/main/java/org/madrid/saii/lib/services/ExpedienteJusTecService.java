package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteJusTecService
 *
 * @author nttdata
 */
public interface ExpedienteJusTecService
        extends BaseService<ExpedienteJusTec, Integer, ExpedienteJusTecDAO> {

    /**
     * Buscar paginadamente ExpedienteJusTec.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteJusTecDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteJusTecDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a ExpedienteJusTec.
     *
     * @param ExpedienteJusTecDTO the ExpedienteJusTec DTO
     * @return ExpedienteJusTecDTO
     * @throws ServiceException the service exception
     */
    ExpedienteJusTecDTO guardar(ExpedienteJusTecDTO expedienteJusTecDTO) throws ServiceException;

    /**
     * Método que busca expedientes por id de expediente 
     * @param idConvocatoria
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusTecDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    List<ExpedienteJustecDocDTO> buscarDocs(int index, int pageSize, List<AtlasFilter> filtrosEntrada,
            List<AtlasSort> ordenamiento) throws ServiceException;
    
    void guardarDocumentoJustec(ExpedienteJustecDocDTO expedienteJustecDocDTO);
    
    /**
     *   Método para buscar ExpedienteJusTecDTO por el id de expediente
     *   
     *   @param idExpediente id del expediente
     *   @param idEstado id estado justec
     */
    List<ExpedienteJusTecDTO> buscarPeriodosPendientesJustecPorExpediente(Integer idExpediente, Integer idEstado)
            throws ServiceException;

    /**
     * 
     * @param idExpedienteJustec
     * @return
     * @throws ServiceException
     */
    ExpedienteJusTecDTO buscarPorIdExpedienteJustec(Integer idExpedienteJustec)
            throws ServiceException;

    /**
     * 
     * @param expedienteJusTecDTO
     * @return
     * @throws ServiceException
     */
    ExpedienteJusTecDTO update(ExpedienteJusTecDTO expedienteJusTecDTO) throws ServiceException;

    /**
     * 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusTecDTO> buscarPeriodosPendientesSubsanarJustecPorExpediente(
            Integer idExpediente) throws ServiceException;

    /**
     * 
     * @param idExpediente
     * @return
     * @throws ServiceException
     */
    List<ExpedienteJusTecDTO> buscarByIdExp(Integer idExpediente)throws ServiceException ;
    
    
    /**
     * Guarda un/a ExpedienteJusTec.
     *
     * @param ExpedienteJusTecDTO the ExpedienteJusTec DTO
     * @return ExpedienteJusTecDTO
     * @throws ServiceException the service exception
     */
    ExpedienteJusTecDTO actualizarIdEstado(ExpedienteJusTecDTO expedienteJustecDTOAux) throws ServiceException;
    
    
    /**
     * 
     * @param idExpedienteJustec
     * @return
     * @throws ServiceException
     */
    ExpedienteJusTec buscarIdExpJustec(Integer idExpedienteJustec)
            throws ServiceException;


    /**
     *   Método para buscar ExpedienteJusTecDTO por el id de expediente
     *   
     *   @param idExpediente id del expediente
     */
    List<ExpedienteJusTecDTO> buscarPeriodosFinalizadoPlazoJustecPorExpediente(Integer idExpediente, Integer idEstado)
            throws ServiceException;

    
    /**
     * Actualiza desde el batch el estado de los expdtes justec segun sus estados previos y fechas de JCT
     *
     * @return
     * @throws ServiceException
     */
    void procesarPlazosJCT() throws ServiceException;

    List<ExpedienteJusTecDTO> buscarJustecPdteEvaluacion() throws ServiceException;

    void borradoLogico(Integer idExpedienteJustec) throws ServiceException;

    List<ExpedienteJusTecDTO> obtenerExpedientesJustecByIdExp(Integer idExpediente)
            throws ServiceException;
}
