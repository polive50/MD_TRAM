package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.dto.ExpedienteContratoDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteContratoDoc
 *
 * @author Quental
 */
public enum ExpedienteContratoDocToEntity implements Function<ExpedienteContratoDocDTO, ExpedienteContratoDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteContratoDoc apply(ExpedienteContratoDocDTO dto) {

        ExpedienteContratoDoc entity = null;

        if (dto != null) {

            entity = new ExpedienteContratoDoc();

            BeanUtils.copyProperties(dto, entity);
            if (dto.getExpedienteDocDTO()!= null) {
                entity.setExpedienteDoc(ExpedienteDocToEntity.INSTANCE.apply(dto.getExpedienteDocDTO()));
            }
            
            if (dto.getExpedienteContratoDTO()!= null) {
                entity.setExpedienteContrato(ExpedienteContratoToEntity.INSTANCE.apply(dto.getExpedienteContratoDTO()));
            }
            
        }

        return entity;
    }
}
