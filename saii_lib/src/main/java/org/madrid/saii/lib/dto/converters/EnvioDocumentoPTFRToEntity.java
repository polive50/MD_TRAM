package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.EnvioDocumentoPTFR;
import org.madrid.saii.lib.dto.EnvioDocumentoPtfrDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Catalogos
 *
 * @author everis
 */
public enum EnvioDocumentoPTFRToEntity implements Function<EnvioDocumentoPtfrDTO, EnvioDocumentoPTFR> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public EnvioDocumentoPTFR apply(EnvioDocumentoPtfrDTO dto) {

        EnvioDocumentoPTFR entity = null;

        if (dto != null) {

            entity = new EnvioDocumentoPTFR();

            BeanUtils.copyProperties(dto, entity);

            entity.setidDocumentoPTFR(dto.getIdDocumentoPTFR());
            

        }

        return entity;
    }
}
