package org.madrid.saii.lib.dto.converters;

import java.io.Serializable;
import java.util.function.Function;

import org.madrid.saii.lib.domain.SucaMunicipio;
import org.madrid.saii.lib.domain.SucaMunicipioId;
import org.madrid.saii.lib.dto.SucaMunicipioDTO;

/**
 * Parser DTO to Entity for SucaMunicipio
 *
 *
 */
public enum SucaMunicipioToEntity
        implements Function<SucaMunicipioDTO, SucaMunicipio>, Serializable {

    /**
     *
     */
    INSTANCE;

    /**
     *
     */
    @Override
    public SucaMunicipio apply(final SucaMunicipioDTO input) {
        SucaMunicipio output = new SucaMunicipio();
        if (input != null && input.getCdMunicipio() != null && !input.getCdMunicipio().isEmpty()) {
            output.setId(new SucaMunicipioId(input.getCdProvincia(), input.getCdMunicipio()));
            output.setCdPais("ESP");
            output.setNmProv(Integer.parseInt(input.getCdProvincia()));
            output.setNmMuni(Integer.parseInt(input.getCdMunicipio()));
            output.setDsMuni(input.getDsMunicipio());
        }
        return output;
    }
}
