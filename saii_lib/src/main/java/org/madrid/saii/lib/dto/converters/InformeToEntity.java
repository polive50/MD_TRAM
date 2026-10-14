package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Informe;
import org.madrid.saii.lib.dto.InformeDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;



/**
 * Parser DTO to Entity for Informe
 *
 * @author quental
 */
public enum InformeToEntity implements Function<InformeDTO, Informe>{

    /*
     * 
     */
    INSTANCE;
    
    /*
     * 
     */
    @Override
    public Informe apply(InformeDTO dto) {
        
        Informe entity = null;

        if (dto != null) {
            entity = new Informe();
            BeanUtils.copyProperties(dto, entity);
            if (dto.getTipoInformeDTO() != null && dto.getTipoInformeDTO().getIdValor() != null) {
                entity.setTipoInforme(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoInformeDTO()));
            }
            if (dto.getImagenLogoDTO() != null && dto.getImagenLogoDTO().getIdImagen() != null) {
                entity.setImagenLogo(ImagenToEntity.INSTANCE.apply(dto.getImagenLogoDTO()));
            }
            if (dto.getImagenLogo2DTO() != null && dto.getImagenLogo2DTO().getIdImagen() != null) {
                entity.setImagenLogo2(ImagenToEntity.INSTANCE.apply(dto.getImagenLogo2DTO()));
            }
        }
        
        return entity;
    }

}
