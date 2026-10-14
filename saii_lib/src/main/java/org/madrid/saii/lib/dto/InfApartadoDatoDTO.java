package org.madrid.saii.lib.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class InfApartadoDatoDTO {
    /**
     * Representa el campo aparIdApartado del objeto de dominio
     */
    private Integer aparIdApartado;

    /**
     * Representa el campo aparDsTitulo del objeto de dominio
     */
    private String aparDsTitulo;

    /**
     * Representa el campo aparTlDescripcion del objeto de dominio
     */
    private String aparTlDescripcion;

    /**
     * Representa el campo aparTlTexto del objeto de dominio
     */
    private String aparTlTexto;

    /**
     * Representa el campo apaNmOrden del objeto de dominio
     */
    private Integer apaNmOrden;

    /**
     * Representa el campo apaNmCalificacionMax del objeto de dominio
     */
    private Integer apaNmCalificacionMax;

    /**
     * Representa el campo aparNotDsComentario del objeto de dominio
     */
    private String aparNotDsComentario;

    /**
     * Representa el campo aparNotNmNota del objeto de dominio
     */
    private Integer aparNotNmNota;
    
    /**
     * Representa el campo aparItInformativo del objeto de dominio
     */
    private String aparItInformativo;
    
    private String aparItObligatorio;
}
