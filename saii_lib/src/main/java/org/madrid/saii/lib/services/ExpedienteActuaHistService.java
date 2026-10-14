package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteActuaHistDAO;
import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteActuaHist
 *
 * @author nttdata
 */
public interface ExpedienteActuaHistService extends BaseService<ExpedienteActuaHist, Integer, ExpedienteActuaHistDAO> {

	/**
	 * Buscar todos los registros de ExpedienteActuaHist.
	 *
	 * @return List<ExpedienteActuaHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedienteActuaHistDTO> obtener() throws ServiceException;

	/**
	 * Buscar por id ExpedienteActuaHist.
	 *
	 * @param idExpedienteActuaHist the id ExpedienteActuaHist
	 * @return ExpedienteActuaHistDTO
	 * @throws ServiceException the service exception
	 */
	ExpedienteActuaHistDTO buscarId(Integer idExpedienteActuaHist) throws ServiceException;

	/**
	 * Buscar paginadamente ExpedienteActuaHist.
	 *
	 * @param index    the index
	 * @param pageSize the page size
	 * @param filtros  the filtros
	 * @param orden    the orden
	 * @return List<ExpedienteActuaHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedienteActuaHistDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
			final List<AtlasSort> orden) throws ServiceException;

	/**
	 * Guarda un/a expedienteActuaHist.
	 *
	 * @param expedienteActuaHistDTO the expedienteActuaHist DTO
	 * @return expedienteActuaHistDTO
	 * @throws ServiceException the service exception
	 */
	ExpedienteActuaHistDTO guardar(ExpedienteActuaHistDTO expedienteActuaHistDTO) throws ServiceException;

	/**
	 * Borrado logico ExpedienteActuaHist
	 *
	 * @param ExpedienteActuaHistDTO
	 * @return ExpedienteActuaHistDTO
	 * @throws ServiceException
	 */
	void borradoLogico(final ExpedienteActuaHistDTO expedienteactuahistDTO) throws ServiceException;

	/**
	 * Alta logica
	 *
	 * @param expedienteactuahistDTO
	 * @return
	 * @throws ServiceException
	 */
	void altaLogica(final ExpedienteActuaHistDTO expedienteactuahistDTO) throws ServiceException;

	/**
	 * Buscar todos los registros de ExpedienteActuaHist.
	 *
	 * @return List<ExpedienteActuaHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedienteActuaHistDTO> obtenerListaActuacionHist(Integer idExpediente) throws ServiceException;

	/**
	 * Obtiene la lista paginada de ExpedienteActuaHist
	 *
	 * @param listaHistorico
	 * @param index
	 * @param pageSize
	 * @return
	 */
	List<ExpedienteActuaHistDTO> retornarPaginado(List<ExpedienteActuaHistDTO> listaHistorico, int index, int pageSize);
}
