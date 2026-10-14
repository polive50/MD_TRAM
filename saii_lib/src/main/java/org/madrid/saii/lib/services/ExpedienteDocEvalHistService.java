package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteDocEvalHistDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteDocEvalHist;
import org.madrid.saii.lib.domain.ExpedienteDocHist;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalHistDTO;
import org.madrid.saii.lib.dto.ExpedienteDocHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteDocEvalHist
 *
 * @author everis
 */
public interface ExpedienteDocEvalHistService
        extends BaseService<ExpedienteDocEvalHist, Integer, ExpedienteDocEvalHistDAO> {

    /**
     * Buscar paginadamente ExpedienteDocEvalHist.
     *
     * @param index the index
     * @param pageSize the page size
     * @param filtros the filtros
     * @param orden the orden
     * @return List<ExpedienteDocEvalHistDTO>
     * @throws ServiceException the service exception
     */
    List<ExpedienteDocEvalHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Crear un historico
     *
     * @param ExpedienteDocEvalDTO the ExpedienteDocEvalDTO
     * @param cdUsuAlta the codigo del usuario que realiza la modificación
     * @throws ServiceException the service exception
     */
    public ExpedienteDocEvalHist crearHistorico(ExpedienteDocEvalDTO expedienteDocEvalDTO)
            throws ServiceException;
    
    
}
