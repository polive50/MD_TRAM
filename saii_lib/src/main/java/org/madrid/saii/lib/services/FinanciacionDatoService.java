package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.FinanciacionDatoDAO;
import org.madrid.saii.lib.domain.FinanciacionDato;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.FinanciacionDatoDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Financiacion
 *
 * @author everis
 */
public interface FinanciacionDatoService extends BaseService<FinanciacionDato, Integer, FinanciacionDatoDAO> {

	/**
	 *
	 */
	List<FinanciacionDatoDTO> obtener() throws ServiceException;

	/**
	 *
	 */
	FinanciacionDatoDTO buscarId(Integer idFinanciacionDato) throws ServiceException;

	/**
	 *
	 */
	List<FinanciacionDatoDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
			final List<AtlasSort> orden) throws ServiceException;

	/**
	 * Obtiene la lista de los importes maximos configurados para presupuesto
	 *
	 * @param idConvocatoria
	 * @return List<FinanciacionDatoDTO>
	 * @throws ServiceException
	 */
	List<FinanciacionDatoDTO> importesMaxPresupuesto(Integer idConvocatoria, List<BloqueComponenteDTO> bloques)
			throws ServiceException;

	/**
	 *
	 */
	FinanciacionDatoDTO guardar(FinanciacionDatoDTO financiacionDatoDTO) throws ServiceException;

	/**
	 *
	 */
	void altaLogica(final FinanciacionDatoDTO financiacionDatoDTO) throws ServiceException;

	/**
	 *
	 */
	void borradoLogico(final FinanciacionDatoDTO financiacionDatoDTO) throws ServiceException;
	
	/**
	 * 
	 * @return
	 * @throws ServiceException
	 */
    public void guardarFinanciacionDato(Integer idFinanciacion,
            Integer idPresupuesto, List<Integer> listaAnualidades, Integer nmOrden)
            throws ServiceException;
	
    /**
     * 
     * @param idFinanciacion
     * @return
     */
    public List<FinanciacionDatoDTO> buscarByIdFinanciacion(Integer idFinanciacion, Integer idTipo,
            Integer anualidad) throws ServiceException;
    
}
