package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for ExpedienteModfPresupPlazosOtrosDoc
 *
 * @author Quental
 */
public enum ExpedienteModfPresupPlazosOtrosDocToEntity implements Function<ExpedienteModfPresupPlazosOtrosDocDTO, ExpedienteModfPresupPlazosOtrosDoc> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteModfPresupPlazosOtrosDoc apply(ExpedienteModfPresupPlazosOtrosDocDTO dto) {

        ExpedienteModfPresupPlazosOtrosDoc entity = null;

        if (dto != null) {

            entity = new ExpedienteModfPresupPlazosOtrosDoc();

            BeanUtils.copyProperties(dto, entity);
            if (dto.getExpedienteDocDTO()!= null) {
                entity.setExpedienteDoc(ExpedienteDocToEntity.INSTANCE.apply(dto.getExpedienteDocDTO()));
            }
            
            if (dto.getExpedienteModifPresPlazoOtraDTO()!= null) {
                entity.setExpedienteModifPresPlazoOtra(ExpedienteModifPresPlazoOtraToEntity.INSTANCE.apply(dto.getExpedienteModifPresPlazoOtraDTO()));
            }
            
        }

        return entity;
    }
}
