package org.madrid.saii.lib.dto.converters;

import org.madrid.saii.lib.domain.Bloque;
import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Componente
 *
 * @author everis
 */
public enum BloqueComponenteToEntity implements Function<BloqueComponenteDTO, BloqueComponente> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public BloqueComponente apply(BloqueComponenteDTO dto) {

        BloqueComponente entity = null;

        if (dto != null) {

            entity = new BloqueComponente();

            BeanUtils.copyProperties(dto, entity);

            if (dto.getIdBloque() != null) {
                Bloque bloque = new Bloque();
                bloque.setIdBloque(dto.getIdBloque());
                entity.setBloque(bloque);
            }

            if (dto.getComponenteDTO() != null) {
                entity.setComponente(ComponenteToEntity.INSTANCE.apply(dto.getComponenteDTO()));
            }

            if (dto.getTipoFinanciacionDTO() != null) {
                entity.setTipoFinanciacion(
                        CatalogosValoresToEntity.INSTANCE.apply(dto.getTipoFinanciacionDTO()));
            }

            if (dto.getTramiteDTO() != null) {
                entity.setTramite(CatalogosValoresToEntity.INSTANCE.apply(dto.getTramiteDTO()));
            }

            entity.setItFila(Constantes.AfirmacionNegacion.N);
            entity.setItColumna(Constantes.AfirmacionNegacion.N);
            entity.setItObligatorio(Constantes.AfirmacionNegacion.N);
            entity.setItRelacion(Constantes.AfirmacionNegacion.N);

            if (Boolean.TRUE.equals(dto.getItFila())) {
                entity.setItFila(Constantes.AfirmacionNegacion.S);
            }

            if (Boolean.TRUE.equals(dto.getItColumna())) {
                entity.setItColumna(Constantes.AfirmacionNegacion.S);
            }

            if (Boolean.TRUE.equals(dto.getItObligatorio())) {
                entity.setItObligatorio(Constantes.AfirmacionNegacion.S);
            }

            if (Boolean.TRUE.equals(dto.getItRelacion())) {
                entity.setItRelacion(Constantes.AfirmacionNegacion.S);
            }
        }

        return entity;
    }
}
