package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 *
 * ExpedienteSubsanacionDTO
 *
 * @author everis
 *
 */
@Data
public class ExpedienteSubsanacionDTO implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * Fecha limite de subsanación de documento / requisito
     */
    private String fechaLimiteSubsanacion;

    /**
     * Lista de documentos por subsanar
     */
    private List<ExpedienteDocDTO> listaDocumentos;

    /**
     * Lista de requisitos por subsanar
     */
    private List<ExpedienteReqDTO> listaRequisitos;

    /**
     * Verifica si se debe mostrar la fecha limite de subsanacion
     */
    private Boolean puedeMostrarPlazo;
}
