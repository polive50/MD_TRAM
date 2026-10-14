package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Componente;
import org.madrid.saii.lib.dto.ComponenteDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Componente
 *
 * @author everis
 */
public enum ComponenteToEntity implements Function<ComponenteDTO, Componente> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Componente apply(ComponenteDTO dto) {

        Componente entity = null;

        if (dto != null) {

            entity = new Componente();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getTipoDTO() != null) {
                entity.setTipo(CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDTO()));
            }

            if (dto.getSubTipoDTO() != null) {
                entity.setSubTipo(CatalogosValoresToEntity.INSTANCE.apply(dto.getSubTipoDTO()));
            }
            
            if (dto.getSubTipoCatalogoDTO() != null) {
                entity.setSubTipoCatalogo(CatalogosToEntity.INSTANCE.apply(dto.getSubTipoCatalogoDTO()));
            }

            if (dto.getTipoDocumentoDTO() != null) {
                entity.setTipoDocumento(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoDocumentoDTO()));
            }

            if (dto.getCategoriaDTO() != null) {
                entity.setCategoria(CatalogosValoresToEntity.INSTANCE.apply(dto.getCategoriaDTO()));
            }

            if (dto.getReglaDTO() != null) {
                entity.setRegla(CatalogosValoresToEntity.INSTANCE.apply(dto.getReglaDTO()));
            }

            if (Boolean.TRUE.equals(dto.getItEditable())) {
                entity.setItEditable(Constantes.AfirmacionNegacion.S);
            } else {
                entity.setItEditable(Constantes.AfirmacionNegacion.N);
            }
        }

        return entity;
    }
}
