package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;
import org.madrid.saii.lib.dto.EnvioDocumentoPtfrDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Catalogos
 *
 * @author everis
 */
public enum EnvioDocumentoPTFRToDTO implements Function<EnvioDocumentoPTFR, EnvioDocumentoPtfrDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    public EnvioDocumentoPtfrDTO apply(EnvioDocumentoPTFR entity) {

        EnvioDocumentoPtfrDTO dto = new EnvioDocumentoPtfrDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

        }

        return dto;
    }

   
}
