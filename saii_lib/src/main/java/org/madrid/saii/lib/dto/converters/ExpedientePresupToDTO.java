package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

import org.apache.commons.lang3.StringUtils;

/**
 * Parser Entity to DTO for ExpedienteFin
 *
 * @author nttdata
 */
public enum ExpedientePresupToDTO implements Function<ExpedientePresup, ExpedientePresupDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedientePresupDTO apply(ExpedientePresup entity) {

        ExpedientePresupDTO dto = new ExpedientePresupDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (entity.getTipoFinanciacion() != null && entity.getTipoFinanciacion().getIdValor() != null) {
                dto.setTipoFinanciacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoFinanciacion()));
            }

            if (entity.getTematica() != null && entity.getTematica().getIdValor() != null) {
                dto.setTematicaDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTematica()));
            }
            
            if (entity.getEstado() != null && entity.getEstado().getIdValor() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }

            if (entity.getLstExpedientePresupMonto() != null) {
                entity.getLstExpedientePresupMonto().forEach(e -> {
                    dto.getLstExpedientePresupMontoDTO()
                            .add(ExpedientePresupMontoToDTO.INSTANCE.apply(e));
                });
            }
                
            if (entity.getLstExpedientePresMontHist() != null) {
                entity.getLstExpedientePresMontHist().forEach(e -> {
                    dto.getLstExpedientePresMontHistDTO()
                            .add(ExpedientePresMontHistToDTO.INSTANCE.apply(e));
                });
            }
            
            if (entity.getLstExpedienteModifPresupMonto() != null) {
                entity.getLstExpedienteModifPresupMonto().forEach(e -> {
                    dto.getLstExpedienteModifPresupMontoDTO()
                            .add(ExpedienteModifPresupMontoToDTO.INSTANCE.apply(e));
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
    private void replaceNull(ExpedientePresupDTO dto, ExpedientePresup entity) {

        if (StringUtils.isBlank(entity.getTlObservacion())) {
            dto.setTlObservacion(StringUtils.EMPTY);
        }
        
        if (StringUtils.isBlank(entity.getTlConcepto())) {
            dto.setTlConcepto(StringUtils.EMPTY);
        }
    }
}
