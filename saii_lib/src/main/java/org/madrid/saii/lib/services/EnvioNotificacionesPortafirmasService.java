package org.madrid.saii.lib.services;

import java.util.List;

import org.madrid.saii.lib.dao.EnvioNotificacionesPortafirmasDAO;
import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.EnvioNotificacionPortafirmasDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasSort;
import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio Expediente
 *
 * @author everis
 */
public interface EnvioNotificacionesPortafirmasService extends BaseService<EnvioNotificacionesPortaFirma, Integer, EnvioNotificacionesPortafirmasDAO> {

    

    /**
    *
    */
    List<EnvioNotificacionPortafirmasDTO> buscarEnviosNotificaciones(int index, int pageSize,List<AtlasFilter> filtros, List<AtlasSort> orden, Integer idConvocatoria,
            Integer estado) throws ServiceException;

  
  
//    Boolean guardarEnvio(ConvocatoriaDTO convocatoria, ExpedienteDTO expediente,
//            Integer idExpediente, Integer idtramite, byte[] documentoAdjunto) throws ServiceException;

    /**
       *
       */
    EnvioNotificacionPortafirmasDTO guardar(EnvioNotificacionPortafirmasDTO expedienteReqDTO)
            throws ServiceException;

 

}
