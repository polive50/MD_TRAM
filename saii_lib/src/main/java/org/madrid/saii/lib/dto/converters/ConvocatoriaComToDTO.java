package org.madrid.saii.lib.dto.converters;

import java.util.Iterator;
import java.util.Objects;

import org.madrid.saii.lib.domain.ComunicacionCorreoActor;
import org.madrid.saii.lib.domain.ComunicacionNoteActor;
import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.dto.ConvocatoriaComDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ConvocatoriaCom
 *
 * @author nttdata
 */
public enum ConvocatoriaComToDTO implements Function<ConvocatoriaCom, ConvocatoriaComDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ConvocatoriaComDTO apply(ConvocatoriaCom entity) {

        ConvocatoriaComDTO dto = new ConvocatoriaComDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getTramite())) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }

            if (Objects.nonNull(entity.getConvocatoriaActNote())
                    && Objects.nonNull(entity.getConvocatoriaActNote().getIdActor())) {
                dto.setConvocatoriaActNoteDTO(
                        ConvocatoriaActToDTO.INSTANCE.apply(entity.getConvocatoriaActNote()));
            }

            if (Objects.nonNull(entity.getConvocatoriaActCorreo())
                    && Objects.nonNull(entity.getConvocatoriaActCorreo().getIdActor())) {
                dto.setConvocatoriaActCorreoDTO(
                        ConvocatoriaActToDTO.INSTANCE.apply(entity.getConvocatoriaActCorreo()));
            }

            dto.setBlNote(Boolean.FALSE);
            if (Constantes.AfirmacionNegacion.S.equals(entity.getItNote())) {
                dto.setBlNote(Boolean.TRUE);
            }

            dto.setBlCorreo(Boolean.FALSE);
            if (Constantes.AfirmacionNegacion.S.equals(entity.getItCorreo())) {
                dto.setBlCorreo(Boolean.TRUE);
            }

            //VMM 23/06/25 MD-1696: ahora tenemos la lista de actores note y correo bien mapeada con las nuevas tablas de comunicaciones por actores
            if (Objects.nonNull(entity.getLstActoresNote())
                    && !entity.getLstActoresNote().isEmpty()) {
                Iterator<ComunicacionNoteActor> it = entity.getLstActoresNote().iterator();
                while (it.hasNext()) {
                    ComunicacionNoteActor noteActor = it.next();
                    // Verificar si fcBajaFila es nulo antes de agregar el DTO a la lista
                    if (Objects.isNull(noteActor.getFcBajaFila())) {
                        dto.getLstActoresNoteDTO()
                                .add(ComunicacionNoteActorToDTO.INSTANCE.apply(noteActor));
                    }
                }
            }
            
            if (Objects.nonNull(entity.getLstActoresCorreo())
                    && !entity.getLstActoresCorreo().isEmpty()) {
                Iterator<ComunicacionCorreoActor> it = entity.getLstActoresCorreo().iterator();
                while (it.hasNext()) {
                    ComunicacionCorreoActor noteCorreo = it.next();
                    // Verificar si fcBajaFila es nulo antes de agregar el DTO a la lista
                    if (Objects.isNull(noteCorreo.getFcBajaFila())) {
                        dto.getLstActoresCorreoDTO()
                                .add(ComunicacionCorreoActorToDTO.INSTANCE.apply(noteCorreo));
                    }
                }
            }
        }

        return dto;
    }
}
