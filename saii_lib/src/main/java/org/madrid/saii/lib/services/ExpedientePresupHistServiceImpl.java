package org.madrid.saii.lib.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.madrid.saii.lib.dao.ExpedientePresupHistDAO;
import org.madrid.saii.lib.domain.ExpedientePresupHist;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresupHistToDTO;
import org.madrid.saii.lib.dto.converters.ExpedientePresupHistToEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Lists;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;
import atlas.core.util.UserUtils;

/**
 * Implementación del servicio para la clase de dominio ExpedientePresupHist
 *
 * @author nttdata
 */
@Service("expedientePresupHistService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = { ServiceException.class,
		RuntimeException.class })
public class ExpedientePresupHistServiceImpl extends
		BaseServiceImpl<ExpedientePresupHist, Integer, ExpedientePresupHistDAO> implements ExpedientePresupHistService {
    
    
    /** servicio de expedientePresMontHistService **/
    @Autowired
    @Qualifier("expedientePresMontHistService")
    private ExpedientePresMontHistService expedientePresMontHistService;

	/**
	 * Constructor de la clase que recibe como parámetros el DAO asociado a la
	 * entidad. Está anotado con @Autowired para realizar automáticamente la
	 * inyección y así establecer el valor del DAO genérico en clase la clase padre
	 * (BaseServiceImpl).
	 *
	 * @param expedientePresupHistDAO
	 */
	@Autowired
	public ExpedientePresupHistServiceImpl(
			@Qualifier("expedientePresupHistDAO") ExpedientePresupHistDAO expedientePresupHistDAO) {
		super();
		this.setDao(expedientePresupHistDAO);
	}

	/**
	 * Constructor Vacio
	 */
	public ExpedientePresupHistServiceImpl() {
		super();
	}

	/**
	 *
	 */
	@Override
	public List<ExpedientePresupHistDTO> obtener() throws ServiceException {
		return new ArrayList<>(Lists.transform(this.getDao().findAll(), ExpedientePresupHistToDTO.INSTANCE));
	}

	/**
	 *
	 */
	@Override
	public ExpedientePresupHistDTO buscarId(Integer idExpedientePresupHist) throws ServiceException {
		return ExpedientePresupHistToDTO.INSTANCE.apply(this.getDao().find(idExpedientePresupHist));
	}

	/**
	 *
	 */
	@Override
	public List<ExpedientePresupHistDTO> buscar(final int index, final int pageSize, final List<AtlasFilter> filtros,
			final List<AtlasSort> orden) throws ServiceException {
	    
	    List<ExpedientePresupHistDTO> lstExpedientePresupHistDTO = new ArrayList<>(Lists.transform(
	                                this.find(index, pageSize, filtros, orden), ExpedientePresupHistToDTO.INSTANCE));
	    
//	    for (ExpedientePresupHistDTO dto: lstExpedientePresupHistDTO) {
//	        if (dto.getLstExpedientePresMontHistDTO().isEmpty()) {	            	            
//	            dto.setLstExpedientePresMontHistDTO(
//	                    this.expedientePresMontHistService.buscarIdExpedientePresup(dto.getIdExpedientePresup());
//	        }
//	    }
        
        return lstExpedientePresupHistDTO;
	}

	/**
	 *
	 */
	@Override
	@Transactional(readOnly = false)
	public ExpedientePresupHistDTO guardar(ExpedientePresupHistDTO expedientePresupHistDTO) throws ServiceException {
		return ExpedientePresupHistToDTO.INSTANCE
				.apply(this.insertOrUpdate(ExpedientePresupHistToEntity.INSTANCE.apply(expedientePresupHistDTO)));
	}

	/**
	 *
	 */
	@Override
	public void altaLogica(final ExpedientePresupHistDTO expedientePresupHistDTO) throws ServiceException {
		expedientePresupHistDTO.setCdUsuBaja(null);
		expedientePresupHistDTO.setFcBajaFila(null);
		this.getDao().update(ExpedientePresupHistToEntity.INSTANCE.apply(expedientePresupHistDTO));
	}

	/**
	 *
	 */
	@Override
	public void borradoLogico(final ExpedientePresupHistDTO expedientePresupHistDTO) throws ServiceException {
		expedientePresupHistDTO.setCdUsuBaja(UserUtils.getUserName());
		expedientePresupHistDTO.setFcBajaFila(new Date());
		this.getDao().update(ExpedientePresupHistToEntity.INSTANCE.apply(expedientePresupHistDTO));
	}

	/**
	*
	*/
	@Override
	public List<ExpedientePresupHistDTO> obtenerListaPresupuestoHist(Integer idExpediente) throws ServiceException {
		return this.getDao().buscarPorExpediente(idExpediente);
	}

	/**
	 *
	 */
	@Override
	public List<ExpedientePresupHistDTO> retornarPaginado(List<ExpedientePresupHistDTO> listaHistorico, int index,
			int pageSize) {
		List<ExpedientePresupHistDTO> listaPaginada = new ArrayList<>();

		if (!listaHistorico.isEmpty()) {

			if (pageSize != -1) {

				int lastIndex = 0;

				if (listaHistorico.size() <= 10) {
					index = 0;
					lastIndex = listaHistorico.size();
				} else {
					lastIndex = index + pageSize;
					lastIndex = lastIndex > listaHistorico.size() ? listaHistorico.size() : lastIndex;
				}

				for (int i = index; i < lastIndex; i++) {
					listaPaginada.add(listaHistorico.get(i));
				}
			} else {
				listaPaginada.addAll(listaHistorico);
			}
		}

		return listaPaginada;
	}

}
