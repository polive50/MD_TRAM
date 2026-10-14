package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteContratoDAO;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

public interface ExpedienteContratoService extends BaseService<ExpedienteContrato, Integer, ExpedienteContratoDAO> {
    
    ExpedienteContratoDTO guardarExpedienteContrato(ExpedienteContratoDTO expedienteContratoDTO) throws ServiceException;

    ExpedienteContratoDTO actualizarExpedienteContrato(ExpedienteContratoDTO expedienteContratoDTO) throws ServiceException;
    
    ExpedienteContratoDTO obtenerExpedienteContratoByExpediente(ExpedienteDTO expedienteDTO)  throws ServiceException;

    /**
    *
    */
    ExpedienteContratoDTO guardar(ExpedienteContratoDTO expContratoDTO) throws ServiceException;

    ExpedienteContratoDTO cambiarEstadoContrato(ExpedienteContratoDTO expContratoDTO, Integer idExpediente, Integer idEstado,
            Integer idEstadoRevAdmin) throws ServiceException;

    List<ExpedienteContratoDTO> buscarPorExpediente(Integer idExpediente) throws ServiceException;

    ExpedienteContratoDTO buscarContratoInicialSustitucion(ExpedienteDTO expedienteDTO) throws ServiceException;

    void borradoLogico(Integer idExpedienteContrato) throws ServiceException;

    List<ExpedienteContratoDocDTO> buscarDocs(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException;

    
}
