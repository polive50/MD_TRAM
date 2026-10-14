package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.domain.Configuracion;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.dto.ConfiguracionDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

public enum ConfiguracionToDTO implements Function<Configuracion, ConfiguracionDTO>{
    /**
    *
    */
   INSTANCE;

   /**
    *
    */
   @Override
    public  ConfiguracionDTO apply( Configuracion entity) {
        ConfiguracionDTO dto = new ConfiguracionDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);
        }
            
            return dto;
    }
   
   /**
    * Reemplaza los null de los Strings no obligatorios por EMPTY
    *
    * @param dto
    * @param entity
    */
   private void replaceNull(ConfiguracionDTO dto, Configuracion entity) {

       if (StringUtils.isBlank(entity.getCdParametro())) {
           dto.setCdParametro(StringUtils.EMPTY);
       }
       
       if (StringUtils.isBlank(entity.getCdValor())) {
           dto.setCdValor(StringUtils.EMPTY);
       }
   }

}
