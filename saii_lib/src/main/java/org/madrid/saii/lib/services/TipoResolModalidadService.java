package org.madrid.saii.lib.services;

import atlas.core.exceptions.ServiceException;
import org.madrid.saii.lib.dao.TipoResolModalidadDAO;
import org.madrid.saii.lib.domain.TipoResolModalidad;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.TipoResolModalidadDTO;

/**
 * Interfaz del servicio TipoResolucionConvocatoria
 *
 * @author quental
 */
public interface TipoResolModalidadService
    extends BaseService<TipoResolModalidad, Integer, TipoResolModalidadDAO>{

    TipoResolModalidadDTO buscarPorIdModalidad(final Integer id) throws ServiceException;
    
}
