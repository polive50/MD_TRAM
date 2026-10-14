package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteEvalJusTec
 *
 * @author quental
 */
public enum ExpedienteEvalJusTecToDTO implements Function<ExpedienteEvalJusTec, ExpedienteEvalJusTecDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteEvalJusTecDTO apply(ExpedienteEvalJusTec entity) {

        ExpedienteEvalJusTecDTO dto = new ExpedienteEvalJusTecDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getPersona())) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getPersona()));
            }
            
            if (Objects.nonNull(entity.getExpedienteJustec())) {
                dto.setExpedienteJustecDTO(ExpedienteJusTecToDTO.INSTANCE.apply(entity.getExpedienteJustec()));
            }
            
            if (Objects.nonNull(entity.getEstado())) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }
    
        }

        return dto;
    }

}
