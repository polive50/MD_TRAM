package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.itextpdf.text.Image;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de para objetos id / descripcion.
 *
 * @author everis
 */
@Getter
@Setter
@ToString
public class FichAnexoDocumDTO implements Serializable {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////
    /**
     * Constructor
     */
    public FichAnexoDocumDTO() {
        super();

        this.logo = null;
        this.textoTitulo = StringUtils.EMPTY;
        this.textoCuerpo = StringUtils.EMPTY;
        this.lstDocumentosDTO = new ArrayList<>();
    }

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////
    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Logo
     */
    private transient Image logo;

    /**
     * Texto titulo
     */
    private String textoTitulo;

    /**
     * Texto Cuerpo
     */
    private String textoCuerpo;

    /**
     * Lista documentos anexos
     */
    private List<DocumentoDTO> lstDocumentosDTO;

}
