package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.dto.EnvioNotificacionPortafirmasDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Catalogos
 *
 * @author everis
 */
public enum EnvioNotificacionesPortafirmasToDTO implements Function<EnvioNotificacionesPortaFirma, EnvioNotificacionPortafirmasDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    public EnvioNotificacionPortafirmasDTO apply(EnvioNotificacionesPortaFirma entity) {

        EnvioNotificacionPortafirmasDTO dto = new EnvioNotificacionPortafirmasDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }

   
}
