package org.madrid.saii.lib.dto;

import java.io.Serializable;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.madrid.core.fica.FicaFichero;
import org.madrid.core.fica.FicaMetadatos;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * DTO de ExpedienteDoc
 *
 * @author everis
 * @version 1.0.0
 */
@Getter
@Setter
@ToString
public class ExpedienteDocDTO extends AuditoriaDTO implements Serializable {
    // ////////////////////////////////////////////////////////
    // /////////////////////////////// CONSTRUCTORES
    // ////////////////////////////////////////////////////////

    /**
     * Constructor sin argumentos
     */
    public ExpedienteDocDTO() {
        super();
        this.tipoDocumentoDTO = new CatalogosValoresDTO();
        this.estadoDTO = new CatalogosValoresDTO();
        this.tramiteDTO = new CatalogosValoresDTO();
        this.expedienteDTO = new ExpedienteDTO();
        this.estilo = StringUtils.EMPTY;
        this.esPresentado = Boolean.FALSE;
        this.esDocumentoRequisito = Boolean.FALSE;
    }
    
   
    public ExpedienteDocDTO(
            Integer idExpedienteDoc, String cdAnexoEreg, Integer cdFica,
            Integer idIdent, String dsIdent,           // Convocatoria Identificación
            Integer idExpediente,
            Integer idTipoPers, String dsTipoPers,     // Convocatoria Tipo Persona
            String tlConvocatoria,                     // Convocatoria Descripción
            String cdNumIden, String dsNombre, String dsApellido1, String dsApellido2, // Interesado
            Integer idTipoDoc, String dsTipoDoc,       // Documento
            Integer idTramite, String dsTramite,       // Trámite
            Integer idEstado, String dsEstado, Integer nmConstanteEstado, // Estado
            String itAutoriza, String codigoEreg, java.util.Date fcRegistro, String tlObservaciones,
            Boolean esRequisito, String dsRequisito, String obsRequisito,Integer idConvocatoriaCau,Date fcAltaFila,
            String itObligatorioStr, String tlObservacionesGestor
        ) {
            this.idExpedienteDoc = idExpedienteDoc;
            this.cdAnexoEreg = (cdAnexoEreg != null) ? cdAnexoEreg : "";
            this.cdFica = (cdFica != null) ? cdFica : null;
            this.itAutoriza = itAutoriza;
            this.codigoEreg = codigoEreg;
            this.fcRegistro = fcRegistro;
            this.tlObservaciones = tlObservaciones;
            this.tlObservacionesGestor = tlObservacionesGestor;

            // --- 1. Árbol de Convocatoria ---
            this.expedienteDTO = new ExpedienteDTO();
            this.expedienteDTO.setIdExpediente(idExpediente);
            ConvocatoriaActDTO conv = new ConvocatoriaActDTO();
            
            // Identificación
            CatalogosValoresDTO ident = new CatalogosValoresDTO();
            ident.setIdValor(idIdent);
            ident.setDsValor(dsIdent);
            conv.setIdentificacionDTO(ident);
            
            // Tipo Persona
            CatalogosValoresDTO tPers = new CatalogosValoresDTO();
            tPers.setIdValor(idTipoPers);
            tPers.setDsValor(dsTipoPers);
            conv.setTipoPersonaFisJurDTO(tPers);
            
            conv.setTlDescripcion(tlConvocatoria);
            this.expedienteDTO.setConvocatoriaActDTO(conv);

            // --- 2. Interesado ---
            InteresadoDTO intDTO = new InteresadoDTO();
            intDTO.setCdNumIden(cdNumIden);
            intDTO.setDsNombre(dsNombre);
            intDTO.setDsApellido1(dsApellido1);
            intDTO.setDsApellido2(dsApellido2);
            this.expedienteDTO.setInteresadoDTO(intDTO);

            // --- 3. Documento (tipoDocumentoDTO) ---
            this.tipoDocumentoDTO = new CatalogosValoresDTO();
            this.tipoDocumentoDTO.setIdValor(idTipoDoc);
            this.tipoDocumentoDTO.setDsValor(dsTipoDoc);

            // --- 4. Trámite (tramiteDTO) ---
            this.tramiteDTO = new CatalogosValoresDTO();
            this.tramiteDTO.setIdValor(idTramite);
            this.tramiteDTO.setDsValor(dsTramite);

            // --- 5. Estado (estadoDTO) ---
            this.estadoDTO = new CatalogosValoresDTO();
            this.estadoDTO.setIdValor(idEstado);
            this.estadoDTO.setDsValor(dsEstado);
            this.estadoDTO.setNmConstante(nmConstanteEstado);
                           
            // --- 6. Requisitos ---
            this.esDocumentoRequisito = (esRequisito != null && esRequisito);
            this.dsDocumentoRequisito = dsRequisito;
            this.dsObservacionRequisito = obsRequisito;
            
            if (idConvocatoriaCau != null) {
                this.convocatoriaCauDTO = new ConvocatoriaCauDTO();
                this.convocatoriaCauDTO.setIdConvocatoriaCau(idConvocatoriaCau);
            } else {
                this.convocatoriaCauDTO = null; 
            }
            
            this.setFcAltaFila(fcAltaFila);
	    	this.bloqueComponenteDTO = new BloqueComponenteDTO();
            
            // Convertimos el String de la BD ('S', '1', 'true', etc.) a un Boolean para el XHTML
            boolean esObligatorio = false;
            if (itObligatorioStr != null) {
                esObligatorio = "S".equalsIgnoreCase(itObligatorioStr) 
                             || "1".equals(itObligatorioStr) 
                             || "true".equalsIgnoreCase(itObligatorioStr);
            }
            
            this.bloqueComponenteDTO.setItObligatorio(esObligatorio);
        }
    
    

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// ATRIBUTOS
    // ////////////////////////////////////////////////////////

    /**
     * Identificador del numero de serie para serialización
     */
    private static final long serialVersionUID = 1L;

    /**
     * Campo idExpedienteDoc
     */
    private Integer idExpedienteDoc;

    /**
     * Campo cdFica
     */
    private Integer cdFica;

    /**
     * Campo cdAnexoEreg
     */
    private String cdAnexoEreg;

    /**
     * Campo cdAnexoEregPadre
     */
    private String cdAnexoEregPadre;

    /**
     * Campo estadoDTO
     */
    private CatalogosValoresDTO estadoDTO;

    /**
     * Campo tlCausaDescripcion
     */
    private String tlCausaDescripcion;

    /**
     * Campo tlObservaciones
     */
    private String tlObservaciones;

    /**
     * Campo idBloqueComponente
     */
    private BloqueComponenteDTO bloqueComponenteDTO;

    /**
     * Campo expedienteDTO
     */
    private ExpedienteDTO expedienteDTO;

    /**
     * Campo idDocumentoPtfr
     */
    private Integer idDocumentoPtfr;

    /**
     * Campo idConvocatoriaCau
     */
    private ConvocatoriaCauDTO convocatoriaCauDTO;

    /**
     * FicaFichero
     */
    private String nombreFicheroFica;

    /**
     * Variable que indica si estado del documento está en estado de Presentado
     */
    @JsonIgnore
    private Boolean esPresentado;

    /**
     * Campo auxiliar para control de registro seleccionado en la interfaz
     */
    @JsonIgnore
    private Boolean registroSeleccionado = Boolean.FALSE;

    @JsonIgnore
    private transient FicaFichero ficaFichero;

    @JsonIgnore
    private transient FicaMetadatos ficaMetadatos;

    @JsonIgnore
    private byte[] contenidoFichero;

    /**
     * Campo auxiliar tipo de documento
     */
    private CatalogosValoresDTO tipoDocumentoDTO;

    /**
     * Variable que representa si el documento pertenece a la subsanacion de
     * requisito
     */
    private Boolean esDocumentoRequisito;

    /**
     * Variable que representa el nombre del requisito asociado al documento
     */
    private String dsDocumentoRequisito;

    /**
     * Variable que representa la observacion de requisito asociado al documento
     */
    private String dsObservacionRequisito;

    /**
     * Indica el estilo del registro
     */
    @JsonIgnore
    private String estilo;

    /**
     * Indica si fue el ultimo en enviarse a Portafirmas
     */
    private Boolean ultimoEnvio;

    /**
     * Campo tramiteDTO
     */
    private CatalogosValoresDTO tramiteDTO;

    /**
     * Representa si es un documento adicional
     */
    private String itAdicional;
    
    /**
     * Representa si el documento cumple con los requisitos de la justificación cientifico Técnica
     */
    private Boolean cumple;
    
    private ExpedienteDocDTO subsanacionDocDTO;
    
    
    /**
     * Campo idCausa
     */
    private Integer idCausa;
    
    private String itAutoriza;
    
    /**
     * Campo tlObservacionesGestor
     */
    private String tlObservacionesGestor;
    
    /**
     * Campo idConvocatoriaCauCont
     */
    private ConvocatoriaCauContDTO convocatoriaCauContDTO;
    
    private DatoDTO datoDTO;
 
    private Date fcRegistro;
    
    private String codigoEreg;
    
    private String fechaRegistroString;
    
    private boolean puedeSubsanar;
    
    private boolean puedeDescargarICDA;
    
}
