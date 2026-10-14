package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.dto.EnvioNotificacionPortafirmasDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Catalogos
 *
 * @author everis
 */
public enum EnvioNotificacionesPortafirmasToEntity implements Function<EnvioNotificacionPortafirmasDTO, EnvioNotificacionesPortaFirma> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EnvioNotificacionesPortaFirma apply(EnvioNotificacionPortafirmasDTO dto) {

        EnvioNotificacionesPortaFirma entity = null;

        if (dto != null) {

            entity = new EnvioNotificacionesPortaFirma();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdCodigo(dto.getIdCodigo());

        }

        return entity;
    }
}
