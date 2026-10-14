package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for DocumentoPtfr
 *
 * @author everis
 */
public enum DocumentoPtfrToEntity implements Function<DocumentoPtfrDTO, DocumentoPtfr> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public DocumentoPtfr apply(DocumentoPtfrDTO dto) {

        DocumentoPtfr entity = new DocumentoPtfr();

        if (dto != null) {

            BeanUtils.copyProperties(dto, entity);

            if (Objects.nonNull(dto.getTramiteDTO())) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

        }

        return entity;
    }
}
