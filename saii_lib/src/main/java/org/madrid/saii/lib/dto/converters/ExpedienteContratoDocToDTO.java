package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteContratoDoc
 *
 * @author Quental
 */
public enum ExpedienteContratoDocToDTO
        implements Function<ExpedienteContratoDoc, ExpedienteContratoDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteContratoDocDTO apply(ExpedienteContratoDoc entity) {

        ExpedienteContratoDocDTO dto = new ExpedienteContratoDocDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            
            if (Objects.nonNull(entity.getExpedienteContrato())) {
                dto.setExpedienteContratoDTO(ExpedienteContratoToDTO.INSTANCE.apply(entity.getExpedienteContrato()));
            }
            
            if (Objects.nonNull(entity.getExpedienteDoc())) {
                dto.setExpedienteDocDTO(ExpedienteDocToDTO.INSTANCE.apply(entity.getExpedienteDoc()));
            }
           
        }

        return dto;
    }

}
