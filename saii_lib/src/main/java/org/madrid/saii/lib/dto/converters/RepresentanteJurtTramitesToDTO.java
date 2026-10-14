package org.madrid.saii.lib.dto.converters;

import org.checkerframework.checker.nullness.qual.Nullable;
import org.madrid.saii.lib.domain.RepresentanteJurtTramites;
import org.madrid.saii.lib.dto.RepresentanteJurtTramitesDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum RepresentanteJurtTramitesToDTO implements Function<RepresentanteJurtTramites,RepresentanteJurtTramitesDTO> {
    

    /**
     *
     */
    INSTANCE;

    /**
     *
     */


    @Override
    public  RepresentanteJurtTramitesDTO apply( RepresentanteJurtTramites entity) {
       
        RepresentanteJurtTramitesDTO dto= new RepresentanteJurtTramitesDTO();
        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (entity.getTramite() != null && entity.getTramite().getIdValor() != null) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }
            if (entity.getRepresentanteJur() != null && entity.getRepresentanteJur().getIdRepresentanteJur() != null) {
                dto.setRepresentanteJurDTO(RepresentanteJurToDTO.INSTANCE.apply(entity.getRepresentanteJur()));
            }
            
            if(entity.getExpediente() != null && entity.getExpediente().getIdExpediente() != null) {
                dto.setExpedienteDTO(ExpedienteToDTO.INSTANCE.apply(entity.getExpediente()));
            }
        }

        return dto;
       
    }   

}
