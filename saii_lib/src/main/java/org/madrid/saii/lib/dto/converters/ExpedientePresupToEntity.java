package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;

import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.dto.ExpedienteModifPresupMontoDTO;
import org.madrid.saii.lib.dto.ExpedientePresMontHistDTO;
import org.madrid.saii.lib.dto.ExpedientePresupDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteFin
 *
 * @author nttdata
 */
public enum ExpedientePresupToEntity implements Function<ExpedientePresupDTO, ExpedientePresup> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedientePresup apply(ExpedientePresupDTO dto) {

        ExpedientePresup entity = null;

        if (dto != null) {

            entity = new ExpedientePresup();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getTipoFinanciacionDTO() != null && dto.getTipoFinanciacionDTO().getIdValor() != null) {
                entity.setTipoFinanciacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoFinanciacionDTO()));
            }

            if (dto.getTematicaDTO() != null && dto.getTematicaDTO().getIdValor() != null) {
                entity.setTematica(CatalogosValoresToEntity.INSTANCE.apply(dto.getTematicaDTO()));
            }
            
            if (dto.getEstadoDTO() != null && dto.getEstadoDTO().getIdValor() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

            if (dto.getLstExpedientePresupMontoDTO() != null) {
                entity.setLstExpedientePresupMonto(new ArrayList<>());
                for (ExpedientePresupMontoDTO montoDTO : dto.getLstExpedientePresupMontoDTO()) {
                    entity.getLstExpedientePresupMonto()
                            .add(ExpedientePresupMontoToEntity.INSTANCE.apply(montoDTO));
                }
            }
            
            if (dto.getLstExpedientePresMontHistDTO() != null) {
                entity.setLstExpedientePresMontHist(new ArrayList<>());
                for (ExpedientePresMontHistDTO montoDTO : dto.getLstExpedientePresMontHistDTO()) {
                    entity.getLstExpedientePresMontHist()
                            .add(ExpedientePresMontHistToEntity.INSTANCE.apply(montoDTO));
                }
            }
            
            if (dto.getLstExpedienteModifPresupMontoDTO() != null) {
                entity.setLstExpedienteModifPresupMonto(new ArrayList<>());
                for (ExpedienteModifPresupMontoDTO montoDTO : dto.getLstExpedienteModifPresupMontoDTO()) {
                    entity.getLstExpedienteModifPresupMonto()
                            .add(ExpedienteModifPresupMontoToEntity.INSTANCE.apply(montoDTO));
                }
            }

        }

        return entity;
    }
}
