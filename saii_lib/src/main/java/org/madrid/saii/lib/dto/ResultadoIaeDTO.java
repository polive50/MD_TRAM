package org.madrid.saii.lib.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ResultadoIaeDTO {
    private String codigoCertificado;
    private String descripcionCertificado;
    private String codigoError;
    private String descripcionError;
    private String identificadorTransmision;
    private String fechaEmision;
    private List<ActividadIaeDTO> actividades = new ArrayList<>();

}