package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteDocEvalDAO;
import org.madrid.saii.lib.domain.ExpedienteDocEval;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteDocEval
 *
 * @author nttdata
 */
public interface ExpedienteDocEvalService
        extends BaseService<ExpedienteDocEval, Integer, ExpedienteDocEvalDAO> {

    /**
     * Buscar por id Entidad.
     *
     * @param idEntidad the id Entidad
     * @return EntidadDTO
     * @throws ServiceException the service exception
     */
    public List<ExpedienteDocEvalDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden) throws ServiceException;

    /**
     * Buscar por id Entidad.
     *
     * @param idEntidad the id Entidad
     * @return EntidadDTO
     * @throws ServiceException the service exception
     */
    ExpedienteDocEvalDTO buscarId(Integer idExpedienteEval) throws ServiceException;

    List<ExpedienteDocEvalDTO> obtenerExpedienteDocEvalByIdExpEval(Integer idExpedienteEval) throws ServiceException;

    public ExpedienteEvalDTO guardar(ExpedienteEvalDTO expedienteDocEvalDTO) throws ServiceException;

    public void guardarInformeEvaluacion(ExpedienteDocEvalDTO docFirmado) throws ServiceException;

    public List<ExpedienteDocEvalDTO> obtenerDocumentoExpedienteEval(
            ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException;

    public List<ExpedienteDocEvalDTO> buscarPorFica(Integer cdFica) throws ServiceException;

   
  
}
