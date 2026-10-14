package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de Auditoria
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class AuditoriaDTO implements Serializable {

    /**
     * serial version UID
     */
    private static final long serialVersionUID = 1L;

    /**
     *
     */
    private String cdUsuAlta;

    /**
     *
     */
    private Date fcAltaFila;

    /**
     *
     */
    private String cdUsuModif;

    /**
     *
     */
    private Date fcModifFila;

    /**
     *
     */
    private String cdUsuBaja;

    /**
     *
     */
    private Date fcBajaFila;

}
