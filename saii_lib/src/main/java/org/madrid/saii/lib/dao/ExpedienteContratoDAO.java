package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;

import atlas.core.exceptions.DataAccessException;


public interface ExpedienteContratoDAO extends BaseDAO<ExpedienteContrato, Integer> {

    List<ExpedienteContrato> findByIdExpediente(Integer idExpediente);

    List<ExpedienteContratoDTO> findAllExpContract(Integer idExpediente);
    
    ExpedienteContrato findAllExpContractById(Integer idExpediente);
    
    List<ExpedienteContratoHist> findByIdExpedienteContrato(Integer idExpedienteContrato);

    List<ExpedienteContrato> findAllByEregPresent(String cdEregPresent);
    
    ExpedienteContrato buscarContratoInicialSustitucion(Integer idExpediente) throws DataAccessException;

}
