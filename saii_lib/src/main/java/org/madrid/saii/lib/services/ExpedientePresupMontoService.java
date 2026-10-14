package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedientePresupMontoDAO;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedientePresupMonto
 *
 * @author nttdata
 */
public interface ExpedientePresupMontoService
        extends BaseService<ExpedientePresupMonto, Integer, ExpedientePresupMontoDAO> {

    /**
     * Buscar todos los registros de ExpedientePresupMonto.
     *
     * @return List<ExpedientePresupMontoDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedientePresupMontoDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ExpedienteFin.
     *
     * @param idExpedientePresupMonto the id idExpedientePresupMonto
     * @return ExpedientePresupMontoDTO
     * @throws ServiceException the service exception
     */
    ExpedientePresupMontoDTO buscarId(Integer idExpedientePresupMonto) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteFin.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteFinDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedientePresupMontoDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a expedienteFin.
     *
     * @param expedientePresupDTO the expedienteFin DTO
     * @return expedienteFinDTO
     * @throws ServiceException the service exception
     */
    ExpedientePresupMontoDTO guardar(ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException;

    /**
     * Borrado logico ExpedienteFin
     *
     * @param ExpedientePresupDTO
     * @return ExpedienteFinDTO
     * @throws ServiceException
     */
    void borradoLogico(final ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException;

    /**
     * Alta logica
     *
     * @param expedientefinDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ExpedientePresupMontoDTO expedientePresupMontoDTO)
            throws ServiceException;

    ExpedientePresupMontoDTO guardarConHistorico(ExpedientePresupMontoDTO expedientePresupMontoDTO) throws ServiceException;

    void eliminarMonto(ExpedientePresupMontoDTO montoDTO);

    ExpedientePresupMontoDTO crearNuevoConceptoHijo(ExpedientePresupMontoDTO expPresupMontoDTO)
            throws ServiceException;

}
