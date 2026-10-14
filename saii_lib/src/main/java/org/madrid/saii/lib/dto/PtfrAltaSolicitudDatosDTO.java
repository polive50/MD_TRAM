package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de PtfrAltaSolicitudDatosDTO
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class PtfrAltaSolicitudDatosDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTOR
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public PtfrAltaSolicitudDatosDTO() {
        super();
        this.listaCodigoVerificadores = new ArrayList<>();
        this.listaCodigoFirmantes = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     * Código tipo documento
     */
    private Integer codigoTipoDocumento;

    /**
     * Documento
     */
    private byte[] documento;

    /**
     * Modo de respuesta
     */
    private Integer modoRespuesta;

    /**
     * Lista de verificadores
     */
    private List<String> listaCodigoVerificadores;

    /**
     * Lista de firmantes
     */
    private List<String> listaCodigoFirmantes;

    /**
     * Referencia del solicitante
     */
    private String referenciaSolicitante;

    /**
     * Título
     */
    private String titulo;

    /**
     * Cove
     */
    private String cove;

}
