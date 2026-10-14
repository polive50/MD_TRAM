package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.ConfiguracionDAO;
import org.madrid.saii.lib.domain.Configuracion;
import org.madrid.saii.lib.dto.ConfiguracionDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;

import atlas.core.exceptions.ServiceException;

public interface ConfiguracionService  extends BaseService<Configuracion, Integer, ConfiguracionDAO> {

    List<ConfiguracionDTO> obtenerConfiguracion(ConvocatoriaDTO dto) throws ServiceException;

}
