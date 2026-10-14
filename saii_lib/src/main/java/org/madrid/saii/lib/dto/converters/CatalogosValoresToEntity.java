package org.madrid.saii.lib.dto.converters;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for CatalogosValores
 *
 * @author everis
 */
public enum CatalogosValoresToEntity implements Function<CatalogosValoresDTO, CatalogosValores> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public CatalogosValores apply(CatalogosValoresDTO dto) {

        CatalogosValores entity = null;

        if (dto != null) {

            entity = new CatalogosValores();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdValor() != null && dto.getIdValor() != 0) {
                entity.setIdValor(dto.getIdValor());
            }

            if (StringUtils.isNotBlank(dto.getDsValor())) {
                entity.setDsValor(dto.getDsValor());
            }

            if (dto.getNmConstante() != null && dto.getIdValor() != 0) {
                entity.setNmConstante(dto.getNmConstante());
            }

            if (dto.getCatalogoDTO() != null && dto.getCatalogoDTO().getIdCatalogo() != null) {
                entity.setCatalogo(CatalogosToEntity.INSTANCE.apply(dto.getCatalogoDTO()));

                // Si el catálogo no es mantenible, tampoco lo serán los valores
                entity.setItMantenible(entity.getCatalogo().getItMantenible());
                
            }

        }

        return entity;
    }
}
