package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Informe;
import org.madrid.saii.lib.dto.InformeDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Informe
 *
 * @author quental
 */
public enum InformeToDTO implements Function<Informe, InformeDTO> {

    /*
     * 
     */
    INSTANCE;

    /*
     * 
     */
    @Override
    public InformeDTO apply(Informe entity) {
        InformeDTO dto = new InformeDTO();
        if (entity != null) {
            BeanUtils.copyProperties(entity, dto);
            if (entity.getTipoInforme() != null) {
                dto.setTipoInformeDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoInforme()));
            }
            if (entity.getImagenLogo() != null) {
                dto.setImagenLogoDTO(ImagenToDTO.INSTANCE.apply(entity.getImagenLogo()));
            }
            if (entity.getImagenLogo2() != null) {
                dto.setImagenLogo2DTO(ImagenToDTO.INSTANCE.apply(entity.getImagenLogo2()));
            }
        }
        return dto;
    }
    
}
