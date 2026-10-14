package org.madrid.saii.lib.services.mail;

import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;

import atlas.core.exceptions.ServiceException;

/**
 * The Interface MailService.
 */
public interface MailService {

    /**
     * Enviar correo.
     *
     * @param strEmail Email de destino
     * @param emailContenido
     * @param convocatoriaDTO
     * @param expedienteDTO
     * @throws ServiceException the service exception
     */
    void enviarCorreo(String strEmail, ConvocatoriaPlaDTO emailContenido,
            ConvocatoriaDTO convocatoriaDTO, ExpedienteDTO expedienteDTO) throws ServiceException;

}
