package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteModifPresPlazoOtra
 *
 * @author quental
 */
public enum ExpedienteModifPresPlazoOtraToDTO implements Function<ExpedienteModifPresPlazoOtra, ExpedienteModifPresPlazoOtraDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModifPresPlazoOtraDTO apply(ExpedienteModifPresPlazoOtra entity) {

        ExpedienteModifPresPlazoOtraDTO dto = new ExpedienteModifPresPlazoOtraDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
            
            if (Objects.nonNull(entity.getExpediente())) {
                dto.setExpedienteDTO(ExpedienteToDTO.INSTANCE.apply(entity.getExpediente()));
            }

            if (entity.getEstado() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }

            if (entity.getEstadoResolucion() != null) {
                dto.setEstadoResolucionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoResolucion()));
            }
            
            if (entity.getEstadoRevAdmin() != null) {
                dto.setEstadoRevAdminDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getEstadoRevAdmin()));
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
    private void replaceNull(ExpedienteModifPresPlazoOtraDTO dto, ExpedienteModifPresPlazoOtra entity) {

        if (StringUtils.isBlank(entity.getTlObservaciones())) {
            dto.setTlObservaciones(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getCdEregPresent())) {
            dto.setCdEregPresent(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getCdEregSubsana())) {
            dto.setCdEregSubsana(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getDsDestinoEreg())) {
            dto.setDsDestinoEreg(StringUtils.EMPTY);
        }
    }

}
