package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedientePresupHistDAO;
import org.madrid.saii.lib.domain.ExpedientePresupHist;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedientePresupHist
 *
 * @author nttdata
 */
public interface ExpedientePresupHistService
		extends BaseService<ExpedientePresupHist, Integer, ExpedientePresupHistDAO> {

	/**
	 * Buscar todos los registros de ExpedientePresupHist.
	 *
	 * @return List<ExpedientePresupHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedientePresupHistDTO> obtener() throws ServiceException;

	/**
	 * Buscar por id ExpedientePresupHist.
	 *
	 * @param idExpedientePresupHist the id ExpedientePresupHist
	 * @return ExpedientePresupHistDTO
	 * @throws ServiceException the service exception
	 */
	ExpedientePresupHistDTO buscarId(Integer idExpedientePresupHist) throws ServiceException;

	/**
	 * Buscar paginadamente ExpedientePresupHist.
	 *
	 * @param index    the index
	 * @param pageSize the page size
	 * @param filtros  the filtros
	 * @param orden    the orden
	 * @return List<ExpedientePresupHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedientePresupHistDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
			final List<AtlasSort> orden) throws ServiceException;

	/**
	 * Guarda un/a expedientePresupHist.
	 *
	 * @param expedientePresupHistDTO the expedientePresupHist DTO
	 * @return expedientePresupHistDTO
	 * @throws ServiceException the service exception
	 */
	ExpedientePresupHistDTO guardar(ExpedientePresupHistDTO expedientePresupHistDTO) throws ServiceException;

	/**
	 * Borrado logico ExpedientePresupHist
	 *
	 * @param ExpedientePresupHistDTO
	 * @return ExpedientePresupHistDTO
	 * @throws ServiceException
	 */
	void borradoLogico(final ExpedientePresupHistDTO expedientepresuphistDTO) throws ServiceException;

	/**
	 * Alta logica
	 *
	 * @param expedientepresuphistDTO
	 * @return
	 * @throws ServiceException
	 */
	void altaLogica(final ExpedientePresupHistDTO expedientepresuphistDTO) throws ServiceException;

	/**
	 * Buscar todos los registros de ExpedientePresupHist.
	 *
	 * @return List<ExpedientePresupHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedientePresupHistDTO> obtenerListaPresupuestoHist(Integer idExpediente) throws ServiceException;

	/**
	 * Obtiene la lista paginada de ExpedientePresupHist
	 *
	 * @param listaHistorico
	 * @param index
	 * @param pageSize
	 * @return
	 */
	List<ExpedientePresupHistDTO> retornarPaginado(List<ExpedientePresupHistDTO> listaHistorico, int index,
			int pageSize);

}
