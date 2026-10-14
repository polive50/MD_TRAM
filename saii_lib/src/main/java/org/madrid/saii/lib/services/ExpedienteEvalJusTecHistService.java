package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteEvalJusTecHistDAO;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio ExpedienteEvalJusTecHist
 *
 * @author quental
 */
public interface ExpedienteEvalJusTecHistService
extends BaseService<ExpedienteEvalJusTecHist, Integer, ExpedienteEvalJusTecHistDAO> {
    
    List<ExpedienteEvalJusTecHistDTO> buscar(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteJusTecHist) throws ServiceException;
    
    List<ExpedienteEvalJusTecHistDTO> buscarHistoricoEvalJCT(final int index, final int pageSize,
            final List<AtlasFilter> filtros, final List<AtlasSort> orden, int idExpedienteEvalJusTec, int idPersona) throws ServiceException;
    
    /**
     * Guarda el expedienteEvalJusTecHist
     *
     * @param expedienteEvalJusTecHist
     * @return ExpedienteEvalJusTecHistDTO
     * @throws ServiceException
     */
    ExpedienteEvalJusTecHistDTO guardar(ExpedienteEvalJusTecHistDTO expedienteEvalJusTecHist) throws ServiceException;
    
    /**
     * Actualiza el expedienteEvalJusTecHist
     *
     * @param expedienteEvalJusTecHist
     * @return ExpedienteEvalJusTecHistDTO
     * @throws ServiceException
     */
    ExpedienteEvalJusTecHistDTO actualizar(ExpedienteEvalJusTecHistDTO expedienteEvalJusTecHist) throws ServiceException;
    
    
    /**
     * Borrado logico expedienteEvalJusTecHist
     *
     * @param expedienteEvalJusTecHist
     * @return ExpedienteEvalJusTecHistDTO
     * @throws ServiceException
     */
    ExpedienteEvalJusTecHistDTO borradoLogico(ExpedienteEvalJusTecHistDTO expedienteEvalJusTecHist)
            throws ServiceException;
    /**
     * @throws ServiceException 
     * 
     */
    List<ExpedienteEvalJusTecHistDTO> buscarByIdExpJustec(List<ExpedienteEvalJusTecHistDTO> listExpedienteJusTecHistDTO) throws ServiceException;

    /**
     * 
     */
    ExpedienteEvalJusTecHistDTO buscarByIdExpedienteJustec(Integer idExpedienteJusTec) throws ServiceException;
    
    
    /**
     * 
     */
    ExpedienteEvalJusTecHistDTO buscarId(Integer idExpedienteEvalJusTec) throws ServiceException;

}