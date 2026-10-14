package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ExpedienteContratoHistDAO;
import org.madrid.saii.lib.domain.ExpedienteContratoHist;
import org.madrid.saii.lib.dto.ExpedienteContratoHistDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

public interface ExpedienteContratoHistService extends BaseService<ExpedienteContratoHist, Integer, ExpedienteContratoHistDAO> {

    List<ExpedienteContratoHistDTO> buscar(int index, int pageSize, List<AtlasFilter> filtros,
            List<AtlasSort> orden) throws ServiceException;

}
