package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.AlegacionDato;
import org.madrid.saii.lib.dto.AlegacionDatoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for AlegacionDato
 *
 * @author nttdata
 */
public enum AlegacionDatoToEntity implements Function<AlegacionDatoDTO, AlegacionDato> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public AlegacionDato apply(AlegacionDatoDTO dto) {

        AlegacionDato entity = null;

        if (dto != null) {
            entity = new AlegacionDato();
            BeanUtils.copyProperties(dto, entity);

            if (dto.getAlegacionDTO() != null) {
                entity.setAlegacion(AlegacionToEntity.INSTANCE.apply(dto.getAlegacionDTO()));
            }

        }
        return entity;
    }
}
