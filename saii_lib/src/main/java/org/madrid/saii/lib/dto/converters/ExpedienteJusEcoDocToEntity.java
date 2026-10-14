package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteJusEcoDoc
 *
 * @author Quental
 */
public enum ExpedienteJusEcoDocToEntity implements Function<ExpedienteJusEcoDocDTO, ExpedienteJusEcoDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJusEcoDoc apply(ExpedienteJusEcoDocDTO dto) {

        ExpedienteJusEcoDoc entity = null;

        if (dto != null) {

            entity = new ExpedienteJusEcoDoc();

            BeanUtils.copyProperties(dto, entity);
            if (!Objects.isNull(dto.getCumple()))
                entity.setCumple(dto.getCumple() ? "1" : "0");
            
            if (dto.getExpedienteJusEcoDTO() != null) {
                entity.setExpedienteJusEco(
                        ExpedienteJusEcoToEntity.INSTANCE.apply(dto.getExpedienteJusEcoDTO()));
            }
            
            if (dto.getExpedienteDocDTO() != null) {
                entity.setExpedienteDoc(
                        ExpedienteDocToEntity.INSTANCE.apply(dto.getExpedienteDocDTO()));
            }
            
            if (dto.getExpedientePresupMontoDTO() != null) {
                entity.setExpedientePresupMonto(
                        ExpedientePresupMontoToEntity.INSTANCE.apply(dto.getExpedientePresupMontoDTO()));
            }
        }

        return entity;
    }
}
