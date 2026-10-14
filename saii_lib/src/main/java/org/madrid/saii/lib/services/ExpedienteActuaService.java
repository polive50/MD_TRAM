package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteActuaDAO;
import org.madrid.saii.lib.domain.ExpedienteActua;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.ExpedienteActuaDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteActua
 *
 * @author nttdata
 */
public interface ExpedienteActuaService
        extends BaseService<ExpedienteActua, Integer, ExpedienteActuaDAO> {

    /**
     * Buscar todos los registros de ExpedienteActua.
     *
     * @return List<ExpedienteActuaDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteActuaDTO> obtener() throws ServiceException;

    /**
     * Buscar por id ExpedienteActua.
     *
     * @param idExpedienteActua the id ExpedienteActua
     * @return ExpedienteActuaDTO
     * @throws ServiceException the service exception
     */
    ExpedienteActuaDTO buscarId(Integer idExpedienteActua) throws ServiceException;

    /**
     * Buscar paginadamente ExpedienteActua.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteActuaDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteActuaDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Guarda un/a expedienteActua.
     *
     * @param expedienteActuaDTO the expedienteActua DTO
     * @return expedienteActuaDTO
     * @throws ServiceException the service exception
     */
    ExpedienteActuaDTO guardar(ExpedienteActuaDTO expedienteActuaDTO) throws ServiceException;

    /**
     * Borrado logico ExpedienteActua
     *
     * @param ExpedienteActuaDTO
     * @return ExpedienteActuaDTO
     * @throws ServiceException
     */
    void borradoLogico(final ExpedienteActuaDTO expedienteactuaDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param expedienteactuaDTO
     * @return
     * @throws ServiceException
     */
    void altaLogica(final ExpedienteActuaDTO expedienteactuaDTO) throws ServiceException;

    /**
     * Alta logica
     *
     * @param expedienteactuaDTO
     * @return ExpedienteActuaDTO
     * @throws ServiceException
     */
    void guardarConHistorico(final List<ExpedienteActuaDTO> lstExpedienteactuaDTO,
            String observacion, Double montoAyuda) throws ServiceException;

    void cargarDatos(List<BloqueComponenteDTO> lstActuacionPresentado, Double montoAyuda,
            Integer idExpediente) throws ServiceException;

}
