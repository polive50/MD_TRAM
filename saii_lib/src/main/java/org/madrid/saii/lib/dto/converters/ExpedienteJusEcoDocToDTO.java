package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.ExpedienteJusEcoDoc;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for ExpedienteJusEcoDoc
 *
 * @author Quental
 */
public enum ExpedienteJusEcoDocToDTO
        implements Function<ExpedienteJusEcoDoc, ExpedienteJusEcoDocDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public ExpedienteJusEcoDocDTO apply(ExpedienteJusEcoDoc entity) {

        ExpedienteJusEcoDocDTO dto = new ExpedienteJusEcoDocDTO();

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
            
            if (entity.getExpedienteJusEco()!= null) {
                dto.setExpedienteJusEcoDTO(ExpedienteJusEcoToDTO.INSTANCE.apply(entity.getExpedienteJusEco()));
            }
            
            if (entity.getExpedienteDoc()!= null) {
                dto.setExpedienteDocDTO(ExpedienteDocToDTO.INSTANCE.apply(entity.getExpedienteDoc()));
            }
            
            if (entity.getExpedientePresupMonto()!= null) {
                dto.setExpedientePresupMontoDTO(ExpedientePresupMontoToDTO.INSTANCE.apply(entity.getExpedientePresupMonto()));
            }
        }

        return dto;
    }

}
