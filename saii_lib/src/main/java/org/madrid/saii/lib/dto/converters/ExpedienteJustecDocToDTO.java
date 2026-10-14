package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteJustecDoc
 *
 * @author Quental
 */
public enum ExpedienteJustecDocToDTO
        implements Function<ExpedienteJustecDoc, ExpedienteJustecDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJustecDocDTO apply(ExpedienteJustecDoc entity) {

        ExpedienteJustecDocDTO dto = new ExpedienteJustecDocDTO();

        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            if (!Objects.isNull(entity.getCumple())) {
                Boolean cumple=null;
                if("1".equals(entity.getCumple())) {
                    cumple=Boolean.TRUE;
                }else if("0".equals(entity.getCumple())){
                    cumple=Boolean.FALSE;
                }
                dto.setCumple(cumple);
            }
        }

        return dto;
    }

}
