package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.TramiteSolicitanteExpediente;
import org.madrid.saii.lib.dto.TramiteSolicitanteExpedienteDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum TramiteSolicitanteExpedienteToDTO implements Function<TramiteSolicitanteExpediente,TramiteSolicitanteExpedienteDTO>{
    
    /**
    *
    */
   INSTANCE;

   /**
    *
    */
   @Override
   
   public TramiteSolicitanteExpedienteDTO apply(TramiteSolicitanteExpediente entity) {
       TramiteSolicitanteExpedienteDTO dto= new TramiteSolicitanteExpedienteDTO();
       if (entity != null) {
           BeanUtils.copyProperties(entity, dto);

           if (entity.getTramite() != null && entity.getTramite().getIdValor() != null) {
               dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
           }
           
           if(entity.getInteresado() != null && entity.getInteresado().getIdInteresado() != null) {
               dto.setInteresadoDTO(InteresadoToDTO.INSTANCE.apply(entity.getInteresado()));
           }
           
           if(entity.getExpediente() != null && entity.getExpediente().getIdExpediente() !=null) {
               dto.setExpedienteDTO(ExpedienteToDTO.INSTANCE.apply(entity.getExpediente()));
           }

       }

       return dto;
       
   }

}
