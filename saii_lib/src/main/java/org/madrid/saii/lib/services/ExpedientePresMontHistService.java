package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedientePresMontHistDAO;
import org.madrid.saii.lib.domain.ExpedientePresMontHist;
import org.madrid.saii.lib.dto.ExpedientePresMontHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedientePremontoHist
 *
 * @author nttdata
 */
public interface ExpedientePresMontHistService
		extends BaseService<ExpedientePresMontHist, Integer, ExpedientePresMontHistDAO> {

	/**
	 * Buscar paginadamente ExpedientePremontoHist.
	 *
	 * @param index    the index
	 * @param pageSize the page size
	 * @param filtros  the filtros
	 * @param orden    the orden
	 * @return List<ExpedientePremontoHistDTO>
	 * @throws ServiceException the service exception
	 */
	List<ExpedientePresMontHistDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
			final List<AtlasSort> orden) throws ServiceException;

    List<ExpedientePresMontHistDTO> buscarIdExpedientePresup(Integer idExpedientePresup);

}