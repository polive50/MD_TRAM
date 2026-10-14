package org.madrid.saii.lib.dto.converters;

import java.util.Objects;


import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteContratoDoc
 *
 * @author Quental
 */
public enum ExpedienteModfPresupPlazosOtrosDocToDTO
        implements Function<ExpedienteModfPresupPlazosOtrosDoc, ExpedienteModfPresupPlazosOtrosDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModfPresupPlazosOtrosDocDTO apply(ExpedienteModfPresupPlazosOtrosDoc entity) {

        ExpedienteModfPresupPlazosOtrosDocDTO dto = new ExpedienteModfPresupPlazosOtrosDocDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            
            if (Objects.nonNull(entity.getExpedienteModifPresPlazoOtra())) {
                dto.setExpedienteModifPresPlazoOtraDTO(ExpedienteModifPresPlazoOtraToDTO.INSTANCE.apply(entity.getExpedienteModifPresPlazoOtra()));
            }
            
            if (Objects.nonNull(entity.getExpedienteDoc())) {
                dto.setExpedienteDocDTO(ExpedienteDocToDTO.INSTANCE.apply(entity.getExpedienteDoc()));
            }
           
        }

        return dto;
    }

}
