package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.dto.ExpedienteModifPresPlazoOtraDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteModifPresPlazoOtra
 *
 * @author quental
 */
public enum ExpedienteModifPresPlazoOtraToEntity implements Function<ExpedienteModifPresPlazoOtraDTO, ExpedienteModifPresPlazoOtra> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModifPresPlazoOtra apply(ExpedienteModifPresPlazoOtraDTO dto) {

        ExpedienteModifPresPlazoOtra entity = null;

        if (dto != null) {
            entity = new ExpedienteModifPresPlazoOtra();

            BeanUtils.copyProperties(dto, entity);
            
            if (Objects.nonNull(dto.getExpedienteDTO())) {
                entity.setExpediente(ExpedienteToEntity.INSTANCE.apply(dto.getExpedienteDTO()));
            }

            if (dto.getEstadoDTO() != null && dto.getEstadoDTO().getIdValor() != null) {
                entity.setEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoDTO()));
            }

            if (dto.getEstadoResolucionDTO() != null
                    && dto.getEstadoResolucionDTO().getIdValor() != null) {
                entity.setEstadoResolucion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoResolucionDTO()));
            }
            
            if (Objects.nonNull(dto.getEstadoRevAdminDTO()) && dto.getEstadoRevAdminDTO().getIdValor() != null) {
                entity.setEstadoRevAdmin(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getEstadoRevAdminDTO()));
            }
        }

        return entity;
    }
}
