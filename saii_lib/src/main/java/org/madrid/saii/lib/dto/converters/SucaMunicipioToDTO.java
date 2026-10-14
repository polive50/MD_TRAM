package org.madrid.saii.lib.dto.converters;

import java.io.Serializable;

import org.madrid.saii.lib.domain.SucaMunicipio;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;

import com.google.common.base.Function;

/**
 * Clase que transforma la entidad de dominio en DTO.
 *
 * @author everis
 *
 */
public enum SucaMunicipioToDTO implements Function<SucaMunicipio, SucaMunicipioDTO>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public SucaMunicipioDTO apply(final SucaMunicipio input) {
        SucaMunicipioDTO output = new SucaMunicipioDTO();
        if (input != null && input.getId() != null) {

            output.setCdMunicipio(String.valueOf(input.getId().getCdMuni()));
            output.setDsMunicipio(input.getDsMuni());
            output.setCdProvincia(String.valueOf(input.getId().getCdProv()));

        }
        return output;
    }
}
