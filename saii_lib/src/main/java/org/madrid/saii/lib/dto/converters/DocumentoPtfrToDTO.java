package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.DocumentoPtfr;
import org.madrid.saii.lib.dto.DocumentoPtfrDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for DocumentoPtfr
 *
 * @author everis
 */
public enum DocumentoPtfrToDTO implements Function<DocumentoPtfr, DocumentoPtfrDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public DocumentoPtfrDTO apply(DocumentoPtfr entity) {

        DocumentoPtfrDTO dto = new DocumentoPtfrDTO();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);

            if (Objects.nonNull(entity.getTramite())
                    && Objects.nonNull(entity.getTramite().getIdValor())) {
                dto.setTramiteDTO(CatalogosValoresToDTO.INSTANCE.apply(entity.getTramite()));
            }
        }

        return dto;
    }
}
