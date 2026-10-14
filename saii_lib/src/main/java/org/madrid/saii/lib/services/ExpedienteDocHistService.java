package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteDocHist
 *
 * @author everis
 */
public interface ExpedienteDocHistService
        extends BaseService<ExpedienteDocHist, Integer, ExpedienteDocHistDAO> {

    /**
     * Buscar paginadamente ExpedienteDocHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDocHistDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteDocHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Crear un historico
     *
     * @param expedienteDocDTO the expedienteDocDTO
     * @param cdUsuAlta the codigo del usuario que realiza la modificación
     * @throws ServiceException the service exception
     */
    public ExpedienteDocHist crearHistorico(ExpedienteDocDTO expedienteDocDTO, String cdUsuAlta)
            throws ServiceException;
    
    public ExpedienteDocHist crearHistoricoExpedienteDoc(ExpedienteDoc expedienteDoc, String cdUsuAlta)
            throws ServiceException;

}
