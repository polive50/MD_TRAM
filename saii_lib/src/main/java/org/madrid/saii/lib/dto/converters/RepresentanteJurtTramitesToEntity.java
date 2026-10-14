package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.RepresentanteJurtTramites;
import org.madrid.saii.lib.dto.RepresentanteJurtTramitesDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum RepresentanteJurtTramitesToEntity implements Function<RepresentanteJurtTramitesDTO,RepresentanteJurtTramites>{
    
    /**
    *
    */
   INSTANCE;

   /**
    *
    */
   @Override
   public RepresentanteJurtTramites apply(RepresentanteJurtTramitesDTO dto) {

       RepresentanteJurtTramites entity = new RepresentanteJurtTramites();

       if (dto != null) {
           BeanUtils.copyProperties(dto, entity);

           if(dto.getExpedienteDTO() != null && dto.getExpedienteDTO().getIdExpediente() != null) {
               entity.setExpediente(ExpedienteToEntity.INSTANCE.apply(dto.getExpedienteDTO()));  
           }
          
           if(dto.getRepresentanteJurDTO() != null && dto.getRepresentanteJurDTO().getIdRepresentanteJur() != null) {
               entity.setRepresentanteJur(RepresentanteJurToEntity.INSTANCE.apply(dto.getRepresentanteJurDTO()));
           }
           
           if(dto.getTramiteDTO() != null && dto.getTramiteDTO().getIdValor() != null) {
               entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
           }
       
       }

       return entity;
   }
}


