package org.madrid.saii.lib.dto;

import java.util.List;
import java.util.Map;

import lombok.Data;

/**
 * Clase de apoyo para enviar datos a generar PDF de Tramite
 *
 * @author nttdata
 *
 */
@Data
public class TramiteComunicacionDTO {

    private Map<String, byte[]> lstLogos;

    private String dsConvocatoria;

    private String dsExpediente;

    private String dsAsunto;

    private String dsDescripcion;

    private List<String> lstSubExpedientes;

    private List<Map<String, Object>> lstDocumentos;

    private List<Map<String, Object>> lstRequisitos;

    private String dsFirma;

    private String dsArea;

    private String dsSubDireccion;

    private String dsDireccion;

    private String dsConsejeria;

}
