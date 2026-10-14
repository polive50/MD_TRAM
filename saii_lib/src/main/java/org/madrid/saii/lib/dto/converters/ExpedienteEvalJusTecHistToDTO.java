package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteEvalJusTec;
import org.madrid.saii.lib.domain.ExpedienteEvalJusTecHist;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteEvalJusTec
 *
 * @author quental
 */
public enum ExpedienteEvalJusTecHistToDTO implements Function<ExpedienteEvalJusTecHist, ExpedienteEvalJusTecHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteEvalJusTecHistDTO apply(ExpedienteEvalJusTecHist entity) {

        ExpedienteEvalJusTecHistDTO dto = new ExpedienteEvalJusTecHistDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            
            if (Objects.nonNull(entity.getExpedienteEvalJusTec())) {
                dto.setExpedienteEvalJusTecDTO(ExpedienteEvalJusTecToDTO.INSTANCE.apply(entity.getExpedienteEvalJusTec()));
            }

            if (Objects.nonNull(entity.getPersona())) {
                dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getPersona()));
            }
            
            if (Objects.nonNull(entity.getExpedienteJusTec())) {
                dto.setExpedienteJusTecDTO(ExpedienteJusTecToDTO.INSTANCE.apply(entity.getExpedienteJusTec()));
            }
            
            if (Objects.nonNull(entity.getEstado())) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }
    
        }

        return dto;
    }

}

