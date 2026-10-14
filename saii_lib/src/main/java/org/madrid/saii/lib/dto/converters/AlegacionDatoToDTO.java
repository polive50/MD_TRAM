package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.AlegacionDato;
import org.madrid.saii.lib.dto.AlegacionDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for AlegacionDato
 *
 * @author nttdata
 */
public enum AlegacionDatoToDTO implements Function<AlegacionDato, AlegacionDatoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public AlegacionDatoDTO apply(AlegacionDato entity) {

        AlegacionDatoDTO dto = new AlegacionDatoDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);

            if (entity.getAlegacion() != null) {
                dto.setAlegacionDTO(AlegacionToDTO.INSTANCE.apply(entity.getAlegacion()));
            }

        }

        return dto;
    }
}
