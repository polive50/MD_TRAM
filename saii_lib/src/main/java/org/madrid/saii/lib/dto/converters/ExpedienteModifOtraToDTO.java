package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteModifOtra;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteModifOtra
 *
 * @author quental
 */
public enum ExpedienteModifOtraToDTO implements Function<ExpedienteModifOtra, ExpedienteModifOtraDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModifOtraDTO apply(ExpedienteModifOtra entity) {

        ExpedienteModifOtraDTO dto = new ExpedienteModifOtraDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
            
            if (Objects.nonNull(entity.getExpedienteModifPresPlazoOtra())) {
                dto.setExpedienteModifPresPlazoOtraDTO
                    (ExpedienteModifPresPlazoOtraToDTO.INSTANCE.apply(entity.getExpedienteModifPresPlazoOtra()));
            }
        }

        return dto;
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(ExpedienteModifOtraDTO dto, ExpedienteModifOtra entity) {

        if (StringUtils.isBlank(entity.getTlOtrasModif())) {
            dto.setTlOtrasModif(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlOtrasModifObservaciones())) {
            dto.setTlOtrasModifObservaciones(StringUtils.EMPTY);
        }
        
        
        
    }

}
