package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCom
 *
 * @author nttdata
 */
public enum ComunicacionCorreoActorToDTO implements Function<ComunicacionCorreoActor, ComunicacionCorreoActorDTO> {  

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ComunicacionCorreoActorDTO apply(ComunicacionCorreoActor entity) {

        ComunicacionCorreoActorDTO dto = new ComunicacionCorreoActorDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
