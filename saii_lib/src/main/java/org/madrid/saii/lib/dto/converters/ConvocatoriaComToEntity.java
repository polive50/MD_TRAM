package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.dto.ComunicacionCorreoActorDTO;
import org.madrid.saii.lib.dto.ComunicacionNoteActorDTO;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ConvocatoriaCom
 *
 * @author nttdata
 */
public enum ConvocatoriaComToEntity implements Function<ConvocatoriaComDTO, ConvocatoriaCom> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaCom apply(ConvocatoriaComDTO dto) {

        ConvocatoriaCom entity = null;

        if (dto != null) {

            entity = new ConvocatoriaCom();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTramiteDTO())) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

            if (Objects.nonNull(dto.getConvocatoriaActNoteDTO())
                    && Objects.nonNull(dto.getConvocatoriaActNoteDTO().getIdActor())) {
                entity.setConvocatoriaActNote(
                        ConvocatoriaActToEntity.INSTANCE.apply(dto.getConvocatoriaActNoteDTO()));
            }

            if (Objects.nonNull(dto.getConvocatoriaActCorreoDTO())
                    && Objects.nonNull(dto.getConvocatoriaActCorreoDTO().getIdActor())) {
                entity.setConvocatoriaActCorreo(
                        ConvocatoriaActToEntity.INSTANCE.apply(dto.getConvocatoriaActCorreoDTO()));
            }

            entity.setItNote(Constantes.AfirmacionNegacion.N);
            if (Boolean.TRUE.equals(dto.getBlNote())) {
                entity.setItNote(Constantes.AfirmacionNegacion.S);
            }

            entity.setItCorreo(Constantes.AfirmacionNegacion.N);
            if (Boolean.TRUE.equals(dto.getBlCorreo())) {
                entity.setItCorreo(Constantes.AfirmacionNegacion.S);
            }
            
            //VMM 23/06/25 MD-1696: ahora tenemos la lista de actores note y correo bien mapeada con las nuevas tablas de comunicaciones por actores
            if (Objects.nonNull(dto.getLstActoresNoteDTO())
                    && !dto.getLstActoresNoteDTO().isEmpty()) {
                entity.setLstActoresNote(new ArrayList<>());
                Iterator<ComunicacionNoteActorDTO> it = dto.getLstActoresNoteDTO().iterator();
                while (it.hasNext()) {
                    ComunicacionNoteActorDTO noteActorDTO = it.next();
                    // Verificar si fcBajaFila es nulo antes de agregar el DTO a la lista
                    if (Objects.isNull(noteActorDTO.getFcBajaFila())) {
                        entity.getLstActoresNote()
                                .add(ComunicacionNoteActorToEntity.INSTANCE.apply(noteActorDTO));
                    }
                }
            }
            
            if (Objects.nonNull(dto.getLstActoresCorreoDTO())
                    && !dto.getLstActoresCorreoDTO().isEmpty()) {
                entity.setLstActoresCorreo(new ArrayList<>());
                Iterator<ComunicacionCorreoActorDTO> it = dto.getLstActoresCorreoDTO().iterator();
                while (it.hasNext()) {
                    ComunicacionCorreoActorDTO noteCorreoDTO = it.next();
                    // Verificar si fcBajaFila es nulo antes de agregar el DTO a la lista
                    if (Objects.isNull(noteCorreoDTO.getFcBajaFila())) {
                        entity.getLstActoresCorreo()
                                .add(ComunicacionCorreoActorToEntity.INSTANCE.apply(noteCorreoDTO));
                    }
                }
            }
        }

        return entity;
    }
}
