package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedientePresupHist;
import org.madrid.saii.lib.dto.ExpedientePresMontHistDTO;
import org.madrid.saii.lib.dto.ExpedientePresupHistDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedientePresupHist
 *
 * @author nttdata
 */
public enum ExpedientePresupHistToEntity
        implements Function<ExpedientePresupHistDTO, ExpedientePresupHist> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedientePresupHist apply(ExpedientePresupHistDTO dto) {

        ExpedientePresupHist entity = null;

        if (Objects.nonNull(dto)) {

            entity = new ExpedientePresupHist();

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTipoFinanciacionDTO())) {
                entity.setTipoFinanciacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoFinanciacionDTO()));
            }

            if (Objects.nonNull(dto.getTematicaDTO())) {
                entity.setTematica(CatalogosValoresToEntity.INSTANCE.apply(dto.getTematicaDTO()));
            }
            
            if (entity.getEstado() != null) {
                dto.setEstadoDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getEstado()));
            }
            
            if (Objects.nonNull(dto.getLstExpedientePresMontHistDTO())) {
                entity.setLstExpedientePresMontHist(new ArrayList<>());
                for (ExpedientePresMontHistDTO montoDTO : dto.getLstExpedientePresMontHistDTO()) {
                    entity.getLstExpedientePresMontHist()
                            .add(ExpedientePresMontHistToEntity.INSTANCE.apply(montoDTO));
                }
            }
        }

        return entity;
    }
}
