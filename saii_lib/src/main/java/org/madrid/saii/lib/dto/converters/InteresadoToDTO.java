package org.madrid.saii.lib.dto.converters;

import java.util.ArrayList;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.domain.PersonaArea;
import org.madrid.saii.lib.domain.PersonaEspec;
import org.madrid.saii.lib.domain.PersonaOrg;
import org.madrid.saii.lib.domain.PersonaTit;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.springframework.beans.BeanUtils;

import com.google.common.base.Function;

/**
 * Parser Entity to DTO for Interesado
 *
 * @author everis
 */
public enum InteresadoToDTO implements Function<Interesado, InteresadoDTO> {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public InteresadoDTO apply(Interesado entity) {

        InteresadoDTO dto = new InteresadoDTO();
        StringBuilder stringInteresado = new StringBuilder();

        if (entity != null) {

            BeanUtils.copyProperties(entity, dto);
            this.replaceNull(dto, entity);

            if (entity.getTipoIdentificacion() != null) {
                dto.setTipoIdentificacionDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoIdentificacion()));
            }

            if (entity.getTipoActor() != null) {
                dto.setTipoActorDTO(
                        CatalogosValoresToDTO.INSTANCE.apply(entity.getTipoActor()));
            }

            if (entity.getDsNombre() != null) {
                stringInteresado.append(entity.getDsNombre());
            }
            if (entity.getDsApellido1() != null) {
                stringInteresado.append(" ");
                stringInteresado.append(entity.getDsApellido1());
            }
            if (entity.getDsApellido2() != null) {
                stringInteresado.append(" ");
                stringInteresado.append(entity.getDsApellido2());
            }
            dto.setDsInteresado(stringInteresado.toString());

            this.applyListaArea(entity, dto);
            this.applyListaEspecialidad(entity, dto);
            this.applyListaOrganismo(entity, dto);
            this.applyListaTitulacion(entity, dto);
            this.applyListaAreasCientificas(entity, dto);

        }
        return dto;
    }

    /**
     * Metodo para convertir listas de áreas científicas DTO a Entity
     *
     * @param entity
     * @param dto
     */
    private void applyListaArea(Interesado entity, InteresadoDTO dto) {
        if (entity.getListaPersonaArea() != null && !entity.getListaPersonaArea().isEmpty()) {
            dto.setListaPersonaAreaDTO(new ArrayList<>());
            for (PersonaArea area : entity.getListaPersonaArea()) {
                dto.getListaPersonaAreaDTO().add(PersonaAreaToDTO.INSTANCE.apply(area));
            }
        }
    }

    /**
     * Metodo para convertir listas de especialidad DTO a Entity
     *
     * @param entity
     * @param dto
     */
    private void applyListaEspecialidad(Interesado entity, InteresadoDTO dto) {
        if (entity.getListaPersonaEspecialidad() != null
                && !entity.getListaPersonaEspecialidad().isEmpty()) {
            dto.setListaPersonaEspecialidadDTO(new ArrayList<>());
            for (PersonaEspec espec : entity.getListaPersonaEspecialidad()) {
                dto.getListaPersonaEspecialidadDTO().add(PersonaEspecToDTO.INSTANCE.apply(espec));
            }
        }
    }

    /**
     * Metodo para convertir listas de organismo DTO a Entity
     *
     * @param entity
     * @param dto
     */
    private void applyListaOrganismo(Interesado entity, InteresadoDTO dto) {
        if (entity.getListaPersonaOrganismo() != null
                && !entity.getListaPersonaOrganismo().isEmpty()) {
            dto.setListaPersonaOrganismoDTO(new ArrayList<>());
            for (PersonaOrg org : entity.getListaPersonaOrganismo()) {
                dto.getListaPersonaOrganismoDTO().add(PersonaOrgToDTO.INSTANCE.apply(org));
            }
        }
    }

    /**
     * Metodo para convertir listas de titulacion DTO a Entity
     *
     * @param entity
     * @param dto
     */
    private void applyListaTitulacion(Interesado entity, InteresadoDTO dto) {
        if (entity.getListaPersonaTitulacion() != null
                && !entity.getListaPersonaTitulacion().isEmpty()) {
            dto.setListaPersonaTitulacionDTO(new ArrayList<>());
            for (PersonaTit tit : entity.getListaPersonaTitulacion()) {
                dto.getListaPersonaTitulacionDTO().add(PersonaTitToDTO.INSTANCE.apply(tit));
            }
        }
    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNull(InteresadoDTO dto, Interesado entity) {

        if (StringUtils.isBlank(entity.getCdNumIden())) {
            dto.setCdNumIden(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsNombre())) {
            dto.setDsNombre(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsApellido1())) {
            dto.setDsApellido1(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsApellido2())) {
            dto.setDsApellido2(StringUtils.EMPTY);
        }

        if (StringUtils.isBlank(entity.getDsEmail())) {
            dto.setDsEmail(StringUtils.EMPTY);
        }

        this.replaceNullGrupo2(dto, entity);

    }

    /**
     * Reemplaza los null de los Strings no obligatorios por EMPTY
     *
     * @param dto
     * @param entity
     */
    private void replaceNullGrupo2(InteresadoDTO dto, Interesado entity) {
        if (StringUtils.isBlank(entity.getCdSexo())) {
            dto.setCdSexo(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsMovil())) {
            dto.setDsMovil(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsTelefono())) {
            dto.setDsTelefono(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getCdProvincia())) {
            dto.setCdProvincia(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getCdLocalidad())) {
            dto.setCdLocalidad(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getCdTipoVia())) {
            dto.setCdTipoVia(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsDireccion())) {
            dto.setDsDireccion(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getCdCodigoPostal())) {
            dto.setCdCodigoPostal(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsNumero())) {
            dto.setDsNumero(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsPiso())) {
            dto.setDsPiso(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsPuerta())) {
            dto.setDsPuerta(StringUtils.EMPTY);
        }
        if (StringUtils.isBlank(entity.getDsEscalera())) {
            dto.setDsEscalera(StringUtils.EMPTY);
        }
    }

    private void applyListaAreasCientificas(Interesado entity, InteresadoDTO dto) {
        StringBuilder areas = new StringBuilder();
        Integer contador = 0;

        if (Objects.nonNull(entity.getListaPersonaArea())
                && !entity.getListaPersonaArea().isEmpty()) {

            for (PersonaArea area : entity.getListaPersonaArea()) {
                contador++;
                if (contador == entity.getListaPersonaArea().size()) {
                    areas.append(area.getArea().getDsValor());
                } else {
                    areas.append(area.getArea().getDsValor() + " / ");
                }

            }
            dto.setAreasCientificas(areas.toString());
        } else {
            dto.setAreasCientificas(StringUtils.EMPTY);
        }
    }
}
