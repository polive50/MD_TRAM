package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.madrid.saii.lib.dto.ComunicacionNoteActorDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCom
 *
 * @author quental
 */
public enum ComunicacionNoteActorToEntity implements Function<ComunicacionNoteActorDTO, ComunicacionNoteActor> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ComunicacionNoteActor apply(ComunicacionNoteActorDTO dto) {

        ComunicacionNoteActor entity = null;

        if (dto != null) {

            entity = new ComunicacionNoteActor();

            BeanUtils.copyProperties(dto, entity);

        }

        return entity;
    }
}
