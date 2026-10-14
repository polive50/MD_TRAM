package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Catalogos;
import org.madrid.saii.lib.dto.CatalogosDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Catalogos
 *
 * @author everis
 */
public enum CatalogosToDTO implements Function<Catalogos, CatalogosDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public CatalogosDTO apply(Catalogos entity) {

        CatalogosDTO dto = new CatalogosDTO();

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
    private void replaceNull(CatalogosDTO dto, Catalogos entity) {

        if (StringUtils.isBlank(entity.getTlDescripcion())) {
            dto.setTlDescripcion(StringUtils.EMPTY);
        }

    }
}
