package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.ExpedientePresupHist;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedientePresupHist
 *
 * @author nttdata
 */
public enum ExpedientePresupHistToDTO
        implements Function<ExpedientePresupHist, ExpedientePresupHistDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedientePresupHistDTO apply(ExpedientePresupHist entity) {

        ExpedientePresupHistDTO dto = new ExpedientePresupHistDTO();

        if (Objects.nonNull(entity)) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (Objects.nonNull(entity.getTipoFinanciacion()) && Objects.nonNull(entity.getTipoFinanciacion().getIdValor())) {
                dto.setTipoFinanciacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoFinanciacion()));
            }

            if (Objects.nonNull(entity.getTematica()) && Objects.nonNull(entity.getTematica().getIdValor())) {
                dto.setTematicaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTematica()));
            }
            
            if (entity.getEstado() != null && entity.getEstado().getIdValor() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }
            
            if (entity.getLstExpedientePresMontHist() != null) {
                entity.getLstExpedientePresMontHist().forEach(e -> {
                    dto.getLstExpedientePresMontHistDTO()
                            .add(ExpedientePresMontHistToDTO.INSTANCE.apply(e));
                });
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
    private void replaceNull(ExpedientePresupHistDTO dto, ExpedientePresupHist entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlConcepto())) {
            dto.setTlConcepto(StringUtils.EMPTY);
        }

    }
}
