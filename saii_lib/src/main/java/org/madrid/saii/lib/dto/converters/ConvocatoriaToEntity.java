package org.madrid.saii.lib.dto.converters;

import java.util.Objects;

import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser DTO to Entity for Convocatoria
 *
 * @author everis
 */
public enum ConvocatoriaToEntity implements Function<ConvocatoriaDTO, Convocatoria> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public Convocatoria apply(ConvocatoriaDTO dto) {

        Convocatoria entity = null;

        if (dto != null) {

            entity = new Convocatoria();

            BeanUtils.copyProperties(dto, entity);

            entity.setIdEstado(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdEstado()));                     
            entity.setIdTipoSolicitud(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoSolicitud()));
            entity.setIdTipoConcesion(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoConcesion()));
            
            entity.setItResProvConcDeneg(Boolean.TRUE.equals(dto.getItResProvConcDeneg())
                    ? Constantes.AfirmacionNegacion.S
                    : Constantes.AfirmacionNegacion.N);

            entity.setItResProvExclusion(Boolean.TRUE.equals(dto.getItResProvExclusion())
                    ? Constantes.AfirmacionNegacion.S
                    : Constantes.AfirmacionNegacion.N);

            entity.setItResProvDesistimiento(Boolean.TRUE.equals(dto.getItResProvDesistimiento())
                    ? Constantes.AfirmacionNegacion.S
                    : Constantes.AfirmacionNegacion.N);
            
            // Realizamos las siguientes comparaciones y asignaciones para
            // evitar el error "references an unsaved transient instance "
            this.transformarUnidadAdmin(dto, entity);
            this.transformarCatalogosValores(dto, entity);

        }

        return entity;
    }

    /**
     * Metodo que transforma direccion,subdireccion y unidad administrativa
     *
     * @param dto
     * @param entity
     */
    private void transformarUnidadAdmin(ConvocatoriaDTO dto, Convocatoria entity) {

        if (Objects.isNull(dto.getIdDireccion())
                || Objects.isNull(dto.getIdDireccion().getIdUnidadAdmin())) {
            entity.setIdDireccion(null);
        } else {
            entity.setIdDireccion(UnidadAdminToEntity.INSTANCE.apply(dto.getIdDireccion()));
        }

        if (Objects.isNull(dto.getIdSubdireccion())
                || Objects.isNull(dto.getIdSubdireccion().getIdUnidadAdmin())) {
            entity.setIdSubdireccion(null);
        } else {
            entity.setIdSubdireccion(UnidadAdminToEntity.INSTANCE.apply(dto.getIdSubdireccion()));
        }

        if (Objects.isNull(dto.getIdAreaAdministrativa())
                || Objects.isNull(dto.getIdAreaAdministrativa().getIdUnidadAdmin())) {
            entity.setIdAreaAdministrativa(null);
        } else {
            entity.setIdAreaAdministrativa(
                    UnidadAdminToEntity.INSTANCE.apply(dto.getIdAreaAdministrativa()));
        }
        
        if (Objects.isNull(dto.getIdAreaAdministrativaJCT())
                || Objects.isNull(dto.getIdAreaAdministrativaJCT().getIdUnidadAdmin())) {
            entity.setIdAreaAdministrativaJCT(null);
        } else {
            entity.setIdAreaAdministrativaJCT(
                    UnidadAdminToEntity.INSTANCE.apply(dto.getIdAreaAdministrativaJCT()));
        }
    }

    /**
     * Metodo que transforma lineaAyuda,modalidadConvocatoria y tipoModalidad
     *
     * @param dto
     * @param entity
     */
    private void transformarCatalogosValores(ConvocatoriaDTO dto, Convocatoria entity) {
        if (dto.getIdLineaAyuda() == null || dto.getIdLineaAyuda().getIdValor() == null) {
            entity.setIdLineaAyuda(null);
        } else {
            entity.setIdLineaAyuda(CatalogosValoresToEntity.INSTANCE.apply(dto.getIdLineaAyuda()));
        }

        if (dto.getIdModalidadConvocatoria() == null
                || dto.getIdModalidadConvocatoria().getIdValor() == null) {
            entity.setIdModalidadConvocatoria(null);
        } else {
            entity.setIdModalidadConvocatoria(
                    CatalogosValoresToEntity.INSTANCE.apply(dto.getIdModalidadConvocatoria()));
        }
        if (dto.getIdTipoModalidad() == null || dto.getIdTipoModalidad().getIdValor() == null) {
            entity.setIdTipoModalidad(null);
        } else {
            entity.setIdTipoModalidad(
                    CatalogosValoresToEntity.INSTANCE.apply(dto.getIdTipoModalidad()));
        }
        
        if (dto.getIdAceptacionDesistimientoAyuda() == null
                || dto.getIdAceptacionDesistimientoAyuda().getIdValor() == null) {
            entity.setIdAceptacionDesistimientoAyuda(null);
        } else {
            entity.setIdAceptacionDesistimientoAyuda(CatalogosValoresToEntity.INSTANCE
                    .apply(dto.getIdAceptacionDesistimientoAyuda()));
        }

    }

}
