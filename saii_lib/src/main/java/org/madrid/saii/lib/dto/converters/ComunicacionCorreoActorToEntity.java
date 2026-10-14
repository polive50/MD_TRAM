package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCom
 *
 * @author nttdata
 */
public enum ComunicacionCorreoActorToEntity implements Function<ComunicacionCorreoActorDTO, ComunicacionCorreoActor> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ComunicacionCorreoActor apply(ComunicacionCorreoActorDTO dto) {

        ComunicacionCorreoActor entity = null;

        if (dto != null) {

            entity = new ComunicacionCorreoActor();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
