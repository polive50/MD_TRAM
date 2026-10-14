package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.madrid.saii.lib.dto.ComunicacionNoteActorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCom
 *
 * @author quental
 */
public enum ComunicacionNoteActorToDTO implements Function<ComunicacionNoteActor, ComunicacionNoteActorDTO> {  

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ComunicacionNoteActorDTO apply(ComunicacionNoteActor entity) {

        ComunicacionNoteActorDTO dto = new ComunicacionNoteActorDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }
}
