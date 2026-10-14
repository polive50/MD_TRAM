package org.madrid.saii.lib.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de InformeEvalDoc
 *
 * @author quental
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class InformeEvalDocDTO extends AuditoriaDTO implements Serializable {

    public InformeEvalDocDTO() {
        super();
    }

    private static final long serialVersionUID = 1L;

    /**
     * Identificador del registro
     */
    private Integer idEvalInfrmDoc;

    /**
     * Identificador del expediente de evaluación
     */
    private Integer idExpedienteEval;

    /**
     * Informe asociado (contiene tipoInforme)
     */
    private InformeDTO idInforme;

    /**
     * Estado del registro
     */
    private CatalogosValoresDTO idEstado;

    /**
     * Código de fichero generado
     */
    private String cdFica;

    /**
     * Comentario del coordinador
     */
    private String dsComentario;

    /**
     * Nota final del coordinador
     */
    private Integer nmNotaFinal;

    /**
     * Causa de denegación
     */
    private CatalogosValoresDTO idCausa;

    /**
     * Indicador si el coordinador rechaza el informe
     */
    private String itRechazado;

    /**
     * Indicador si el documento está firmado (S) o sin firmar (N)
     */
    private String itFirmado;

    /**
     * Observación del rechazo del informe de evaluación
     */
    private String dsObsRechazoInfEval;
    
    /**
     * Observación del rechazo del informe de evaluación
     */
    private String nombreFicheroFica;

    /**
     * Datos del expediente de evaluación (evaluador)
     */
    private ExpedienteEvalDTO idExpedienteEvalDTO;

    /**
     * Código de fichero del informe firmado por el coordinador (itFirmado='S')
     */
    private Integer cdFicaInformeFirmado;

}
