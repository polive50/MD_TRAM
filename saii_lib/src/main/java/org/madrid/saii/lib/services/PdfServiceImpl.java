package org.madrid.saii.lib.services;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.exceptions.COSVisitorException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.madrid.core.fica.FicaFichero;
import org.madrid.saii.lib.dao.ConvocatoriaComDAO;
import org.madrid.saii.lib.dao.ExpedienteDAO;
import org.madrid.saii.lib.dao.ExpedienteDocDAO;
import org.madrid.saii.lib.dao.ExpedienteDocHistDAO;
import org.madrid.saii.lib.dao.ExpedienteHistDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecDAO;
import org.madrid.saii.lib.dao.ExpedienteJusTecHistDAO;
import org.madrid.saii.lib.dao.ExpedientePresMontHistDAO;
import org.madrid.saii.lib.dao.ExpedientePresupHistDAO;
import org.madrid.saii.lib.dao.ExpedientePresupMontoDAO;
import org.madrid.saii.lib.dao.SolicitudDAO;
import org.madrid.saii.lib.domain.CatalogosValores;
import org.madrid.saii.lib.domain.ConvocatoriaCom;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.domain.ExpedienteContrato;
import org.madrid.saii.lib.domain.ExpedienteContratoDoc;
import org.madrid.saii.lib.domain.ExpedienteDoc;
import org.madrid.saii.lib.domain.ExpedienteJusEco;
import org.madrid.saii.lib.domain.ExpedienteJusTec;
import org.madrid.saii.lib.domain.ExpedienteJustecDoc;
import org.madrid.saii.lib.domain.ExpedienteModfPresupPlazosOtrosDoc;
import org.madrid.saii.lib.domain.ExpedienteModifPresPlazoOtra;
import org.madrid.saii.lib.domain.ExpedientePresup;
import org.madrid.saii.lib.domain.ExpedientePresupMonto;
import org.madrid.saii.lib.domain.Impreso;
import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.domain.RepresentanteJurtTramites;
import org.madrid.saii.lib.domain.Solicitud;
import org.madrid.saii.lib.dto.AlegacionDTO;
import org.madrid.saii.lib.dto.AlegacionDatoDTO;
import org.madrid.saii.lib.dto.ApartadoDTO;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;
import org.madrid.saii.lib.dto.BloqueDTO;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDocDTO;
import org.madrid.saii.lib.dto.DatoDTO;
import org.madrid.saii.lib.dto.DocumentoDTO;
import org.madrid.saii.lib.dto.EvaluacionDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecApartaDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDTO;
import org.madrid.saii.lib.dto.EvaluacionJustecNotaDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.madrid.saii.lib.dto.ExpedienteContratoDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteDocEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalDTO;
import org.madrid.saii.lib.dto.ExpedienteEvalJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ExpedienteJustecDocDTO;
import org.madrid.saii.lib.dto.ExpedienteModfPresupPlazosOtrosDocDTO;
import org.madrid.saii.lib.dto.ExpedienteModifOtraDTO;
import org.madrid.saii.lib.dto.ExpedientePresupMontoDTO;
import org.madrid.saii.lib.dto.ImagenDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;
import org.madrid.saii.lib.dto.InformeDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionDTO;
import org.madrid.saii.lib.dto.InteresadoDTO;
import org.madrid.saii.lib.dto.RepresentanteJurDTO;
import org.madrid.saii.lib.dto.UnidadAdminDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteContratoToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteDocToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteJusEcoDocToEntity;
import org.madrid.saii.lib.dto.converters.ExpedienteModifPresPlazoOtraToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.dto.converters.ImpresoToDTO;
import org.madrid.saii.lib.dto.converters.RepresentanteJurToEntity;
import org.madrid.saii.lib.services.adme.EregAdmeService;
import org.madrid.saii.lib.tramite.Tramite;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Constantes.FormularioPdf;
import org.madrid.saii.lib.util.UtilConstantes;
import org.madrid.saii.lib.util.Utils;
import org.madrid.saii.lib.util.pdf.HeaderFooterPdfEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.FdfWriter;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;
import atlas.core.exceptions.ServiceException;
import atlas.fica.services.FicaService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class PdfServiceImpl.
 */
@Slf4j
@Service("pdfService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class PdfServiceImpl implements PdfService {

    /** The documento service. */
    @Autowired
    @Qualifier("pdfCamposService")
    private PdfCamposService pdfCamposService;

    /** The documento service. */
    @Autowired
    @Qualifier("documentoService")
    private DocumentoService documentoService;

    /** Servicio de Ereg. */
    @Autowired
    @Qualifier("eregAdmeService")
    private EregAdmeService eregAdmeService;

    /** Servicio de Impreso */
    @Autowired
    @Qualifier("impresoService")
    private ImpresoService impresoService;

    /** Servicio de Expediente */
    @Autowired
    @Qualifier("expedienteService")
    private ExpedienteService expedienteService;

    /** Servicio de Expediente */
    @Lazy
    @Autowired
    private ExpedienteDocService expedienteDocService;

    /** Servicio de UnidadAdmin */
    @Autowired
    @Qualifier("unidadAdminService")
    private UnidadAdminService unidadAdminService;

    /** Servicio de DatoService */
    @Autowired
    @Qualifier("datoService")
    private DatoService datoService;

    /** Servicio de DatoService */
    @Autowired
    @Qualifier("evaluacionService")
    private EvaluacionService evaluacionService;

    @Autowired
    @Qualifier("evaluacionJustecService")
    private EvaluacionJustecService evaluacionJustecService;

    /** Servicio de convocatoriaService */
    @Autowired
    @Qualifier("convocatoriaService")
    private ConvocatoriaService convocatoriaService;

    /** Servicio de alegacionService */
    @Autowired
    @Qualifier("alegacionService")
    private AlegacionService alegacionService;

    /** DAO de Solicitud */
    @Autowired
    @Qualifier("solicitudDAO")
    private SolicitudDAO solicitudDAO;

    /** Repositorio expedienteDocDAO */
    @Autowired
    @Qualifier("expedienteDocDAO")
    private ExpedienteDocDAO expedienteDocDAO;

    /** Repositorio expedienteDocDAO */
    @Autowired
    @Qualifier("expedienteDAO")
    private ExpedienteDAO expedienteDAO;

    /** Repositorio expedienteDocHistDAO */
    @Autowired
    @Qualifier("expedienteDocHistDAO")
    private ExpedienteDocHistDAO expedienteDocHistDAO;

    /** Servicio de expedienteEval */
    @Autowired
    @Qualifier("expedienteEvalService")
    private ExpedienteEvalService expedienteEvalService;

    /** Servicio de evaluacionNotaService */
    @Autowired
    @Qualifier("evaluacionNotaService")
    private EvaluacionNotaService evaluacionNotaService;

    /** Servicio de evaluacionNotaService */
    @Autowired
    @Qualifier("evaluacionNotaDatoService")
    private EvaluacionNotaDatoService evaluacionNotaDatoService;

    @Autowired
    @Qualifier("evaluacionJustecNotaDatoService")
    private EvaluacionJustecNotaDatoService evaluacionJustecNotaDatoService;

    @Autowired
    @Qualifier("alegacionDatoService")
    private AlegacionDatoService alegacionDatoService;

    @Autowired
    @Qualifier("imagenService")
    private ImagenService imagenService;

    @Autowired
    @Qualifier("ficaService")
    private FicaService ficaService;

    /** DAO concovocatoria comunicaciones */
    @Autowired
    @Qualifier("convocatoriaComDAO")
    private ConvocatoriaComDAO convocatoriaComDao;

    @Autowired
    @Qualifier("catalogosValoresService")
    private CatalogosValoresService catalogosValoresService;

    @Autowired
    @Qualifier("interesadoService")
    private InteresadoService interesadoService;

    @Autowired
    @Qualifier("convocatoriaPlaService")
    private ConvocatoriaPlaService convocatoriaPlaService;

    @Lazy
    @Autowired
    @Qualifier("convocatoriaComService")
    private ConvocatoriaComService convocatoriaComService;

    @Autowired
    @Qualifier("expedienteJusTecService")
    private ExpedienteJusTecService expedienteJusTecService;

    @Autowired
    @Qualifier("expedienteJusTecDocService")
    private ExpedienteJusTecDocService expedienteJusTecDocService;

    @Autowired
    @Qualifier("expedienteHistDAO")
    private ExpedienteHistDAO expedienteHistDAO;

    /**
     * Servicio de ExpedienteEvalJusTecService
     */
    @Autowired
    @Qualifier("expedienteEvalJusTecService")
    private ExpedienteEvalJusTecService expedienteEvalJusTecService;

    /**
     * Servicio de expedienteContratoService
     */
    @Autowired
    @Qualifier("expedienteContratoService")
    private ExpedienteContratoService expedienteContratoService;

    /**
     * Servicio de expedienteContratoDocService
     */
    @Autowired
    @Qualifier("expedienteContratoDocService")
    private ExpedienteContratoDocService expedienteContratoDocService;

    /** Repositorio expedienteDocDAO */
    @Autowired
    @Qualifier("expedienteJusTecDAO")
    private ExpedienteJusTecDAO expedienteJusTecDAO;

    @Autowired
    @Qualifier("expedienteJusTecHistDAO")
    private ExpedienteJusTecHistDAO expedienteJusTecHistDAO;

    @Autowired
    @Qualifier("expedientePresupService")
    private ExpedientePresupService expedientePresupService;

    @Autowired
    @Qualifier("expedienteModifOtraService")
    private ExpedienteModifOtraService expedienteModifOtraService;

    @Autowired
    @Qualifier("expedienteAnualidadService")
    private ExpedienteAnualidadService expedienteAnualidadService;

    @Autowired
    @Qualifier("expedientePresupHistDAO")
    private ExpedientePresupHistDAO expedientePresupHistDAO;

    @Autowired
    @Qualifier("expedientePresupMontoDAO")
    private ExpedientePresupMontoDAO expedientePresupMontoDAO;

    @Autowired
    @Qualifier("expedientePresMontHistDAO")
    private ExpedientePresMontHistDAO expedientePresMontHistDAO;

    @Autowired
    @Qualifier("expedienteModfPresupPlazosOtrosDocService")
    private ExpedienteModfPresupPlazosOtrosDocService expedienteModfPresupPlazosOtrosDocService;

    @Autowired
    @Qualifier("expedienteModifPresPlazoOtraService")
    private ExpedienteModifPresPlazoOtraService expedienteModifPresPlazoOtraService;

    @Autowired
    @Qualifier("representanteJurtTramitesService")
    private RepresentanteJurtTramitesService representanteJurtTramitesService;

    @Autowired
    @Qualifier("representanteJurService")
    private RepresentanteJurService representanteJurService;
    
    /**
     * Servicio Tramite
     */
    @Autowired
    @Qualifier("tramiteService")
    private TramiteService tramiteService;
    
    /**
     * Servicio informe Helper PDF
     */
    @Autowired
    @Qualifier("informeHelperPdfService")
    private InformeHelperPdfService informeHelperPdfService;
    
    @Autowired
    @Qualifier("utilConstantes")
    private UtilConstantes utilConstantes;
    
    @Autowired
    @Qualifier("convocatoriaPlaDocService")
    private ConvocatoriaPlaDocService convocatoriaPlaDocService;
     
    @Autowired
    @Qualifier("expedienteJusEcoService")
    private ExpedienteJusEcoService expedienteJusEcoService;
    
    @Autowired
    @Qualifier("expedientePresupMontoService")
    private ExpedientePresupMontoService expedientePresupMontoService;  
    
    @Autowired
    @Qualifier("expedienteJusEcoDocService")
    private ExpedienteJusEcoDocService expedienteJusEcoDocService;
    
    @Autowired
    @Qualifier("expedienteDocEvalService")
    private ExpedienteDocEvalService expedienteDocEvalService;
    
    @Autowired
    @Qualifier("informeService")
    private InformeService informeService;
    /**
     * Tipo de letra en negrita de tamaño 10
     */
    static Font tipoLetra10Bold = new Font(FontFamily.HELVETICA, 10, Font.BOLD);

    /**
     * Tipo de letra de tamaño 9
     */
    static Font tipoLetra9 = new Font(FontFamily.HELVETICA, 9);

    /**
     * Tipo de letra en negrita de tamaño 16
     */
    static Font tipoLetra16Bold = new Font(FontFamily.HELVETICA, 16, Font.BOLD);

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public ExpedienteDTO crearFirmar(ExpedienteDTO dto) throws ServiceException {

        dto.setFcFirma(new Date());

        // Se recupera el asunto del dto
        String cdAsunto = dto.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        Integer unidadAdmin = calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG");
        dto = this.eregAdmeService.generarEntrada(dto, cdAsunto, cdUnidad);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + dto.getCdEreg());
        log.info("Destino: " + dto.getDsDestinoEreg());

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCampos(dto);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.obtenerPlantilla(dto, hamCampos);

        // ///////////////////////////////////////////
        byte[] t1 = this.crearFirmarArchivoPdfFirmado(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmado(Boolean.TRUE, dto, plantilla, hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDF(Boolean.TRUE, dto, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEreg(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEreg(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEreg(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEreg(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEreg(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la solicitud en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        this.insertarDocumento(dto, cdAnexoT2,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_EXPEDIENTE_FIRMADO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION),
                Constantes.AfirmacionNegacion.N, Boolean.FALSE);
        log.info("Código de ereg expediente: " + dto.getCdEreg());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info("[PASO 8] - Generamos el listado de anexos que adjuntan a la solicitud");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico
        this.generarFirmarListadoAnexos(dto,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION),
                null, null);

        return dto;

    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoFDF(Boolean conDatosEreg, ExpedienteDTO dto, byte[] plantilla,
            HashMap<String, String> hamCampos) throws ServiceException {

        byte[] fdf = null;

        try {
            dto.setFcFirma(new Date());

            // completamos los datos en la plantilla
            byte[] pdfRellenoDatos = this.rellenarPdf(hamCampos, plantilla, conDatosEreg, dto);

            PdfReader reader = new PdfReader(pdfRellenoDatos);

            // FDF del pdf de solicitud
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FdfWriter fdfWriter = new FdfWriter();
            fdfWriter.setFields(reader.getAcroFields());
            fdfWriter.writeTo(baos);
            fdf = baos.toByteArray();
            baos.close();
            reader.close();

        } catch (IOException e) {
            log.error("Error al presentar el expediente en EREG: " + e.getMessage());
            throw new ServiceException(e);
        }

        return fdf;
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoFDFJCT(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException {

        byte[] fdf = null;

        try {
            expedienteJustec.setFcFirmaPresent(new Date());

            // completamos los datos en la plantilla
            byte[] pdfRellenoDatos = this.rellenarPdfJCT(hamCampos, plantilla, conDatosEreg, dto,
                    expedienteJustec);

            PdfReader reader = new PdfReader(pdfRellenoDatos);

            // FDF del pdf de solicitud
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FdfWriter fdfWriter = new FdfWriter();
            fdfWriter.setFields(reader.getAcroFields());
            fdfWriter.writeTo(baos);
            fdf = baos.toByteArray();
            baos.close();
            reader.close();

        } catch (IOException e) {
            log.error("Error al presentar el expediente en EREG: " + e.getMessage());
            throw new ServiceException(e);
        }

        return fdf;
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoFDFContrato(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos) throws ServiceException {

        byte[] fdf = null;

        try {
            dto.getExpedienteContrato().setFcFirmaPresent(new Date());

            // completamos los datos en la plantilla
            byte[] pdfRellenoDatos = this.rellenarPdfContrato(hamCampos, plantilla, conDatosEreg,
                    dto);

            PdfReader reader = new PdfReader(pdfRellenoDatos);

            // FDF del pdf de solicitud
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            FdfWriter fdfWriter = new FdfWriter();
            fdfWriter.setFields(reader.getAcroFields());
            fdfWriter.writeTo(baos);
            fdf = baos.toByteArray();
            baos.close();
            reader.close();

        } catch (IOException e) {
            log.error("Error al presentar el expediente en EREG: " + e.getMessage());
            throw new ServiceException(e);
        }

        return fdf;
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoPdfFirmado(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos) throws ServiceException {

        // completamos los datos en la plantilla
        byte[] pdfRellenoDatos = this.rellenarPdf(hamCampos, plantilla, conDatosEreg, dto);
        log.info("impreso tramite con sus datos: " + Base64.getEncoder().encodeToString(pdfRellenoDatos));

        // firmamos PDF
        return this.eregAdmeService.firmarDatos(pdfRellenoDatos);
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoPdfFirmadoJCT(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException {

        // completamos los datos en la plantilla
        byte[] pdfRellenoDatos = this.rellenarPdfJCT(hamCampos, plantilla, conDatosEreg, dto,
                expedienteJustec);

        // firmamos PDF
        return this.eregAdmeService.firmarDatos(pdfRellenoDatos);
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoPdfFirmadoContrato(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos) throws ServiceException {

        // completamos los datos en la plantilla
        byte[] pdfRellenoDatos = this.rellenarPdfContrato(hamCampos, plantilla, conDatosEreg, dto);

        // firmamos PDF
        return this.eregAdmeService.firmarDatos(pdfRellenoDatos);
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoXFDF(Boolean conDatosEreg, ExpedienteDTO dto, byte[] plantilla,
            HashMap<String, String> hamCampos) throws ServiceException {

        byte[] xfdfSolicitud = null;

        try {

            // completamos los datos en la plantilla
            byte[] pdfRellenoDatos = this.rellenarPdf(hamCampos, plantilla, conDatosEreg, dto);

            // XFDF del pdf de solicitud
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(baos);
            PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfRellenoDatos));

            document.getDocumentCatalog().getAcroForm().exportFDF().saveXFDF(writer);

            writer.close();
            document.close();

            xfdfSolicitud = baos.toByteArray();
            String tmpXfdf = new String(xfdfSolicitud);
            tmpXfdf = tmpXfdf.replaceAll("\\s*[\\r\\n]+\\s*", "").trim();
            xfdfSolicitud = tmpXfdf.getBytes();

            baos.close();

        } catch (IOException | COSVisitorException e) {
            log.error("Error al presentar el expediente en EREG: " + e.getMessage());
            throw new ServiceException(e);
        }

        return xfdfSolicitud;
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoXFDFJCT(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException {

        byte[] xfdfSolicitud = null;

        try {

            // completamos los datos en la plantilla
            byte[] pdfRellenoDatos = this.rellenarPdfJCT(hamCampos, plantilla, conDatosEreg, dto,
                    expedienteJustec);

            // XFDF del pdf de solicitud
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(baos);
            PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfRellenoDatos));

            document.getDocumentCatalog().getAcroForm().exportFDF().saveXFDF(writer);

            writer.close();
            document.close();

            xfdfSolicitud = baos.toByteArray();
            String tmpXfdf = new String(xfdfSolicitud);
            tmpXfdf = tmpXfdf.replaceAll("\\s*[\\r\\n]+\\s*", "").trim();
            xfdfSolicitud = tmpXfdf.getBytes();

            baos.close();

        } catch (IOException | COSVisitorException e) {
            log.error("Error al presentar el expediente en EREG: " + e.getMessage());
            throw new ServiceException(e);
        }

        return xfdfSolicitud;
    }

    /**
     *
     * @param conDatosEreg
     * @param dto
     * @param plantilla
     * @param hamCampos
     * @return
     * @throws ServiceException
     */
    private byte[] crearFirmarArchivoXFDFContrato(Boolean conDatosEreg, ExpedienteDTO dto,
            byte[] plantilla, HashMap<String, String> hamCampos) throws ServiceException {

        byte[] xfdfSolicitud = null;

        try {

            // completamos los datos en la plantilla
            byte[] pdfRellenoDatos = this.rellenarPdfContrato(hamCampos, plantilla, conDatosEreg,
                    dto);

            // XFDF del pdf de solicitud
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(baos);
            PDDocument document = PDDocument.load(new ByteArrayInputStream(pdfRellenoDatos));

            document.getDocumentCatalog().getAcroForm().exportFDF().saveXFDF(writer);

            writer.close();
            document.close();

            xfdfSolicitud = baos.toByteArray();
            String tmpXfdf = new String(xfdfSolicitud);
            tmpXfdf = tmpXfdf.replaceAll("\\s*[\\r\\n]+\\s*", "").trim();
            xfdfSolicitud = tmpXfdf.getBytes();

            baos.close();

        } catch (IOException | COSVisitorException e) {
            log.error("Error al presentar el expediente en EREG: " + e.getMessage());
            throw new ServiceException(e);
        }

        return xfdfSolicitud;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public String generarFirmarListadoAnexos(ExpedienteDTO dto, Integer idTramite, String asunto,
            List<ExpedienteDocDTO> lstExpDocDTO) throws ServiceException {
        log.info("Se va a generar el documento de anexos que acompañan a la solicitud");
        // Generamos el documento, firmamos y subimos a ereg
        String cdListadoDocEreg = null;
        Date fcFirma = null;
        String cdEreg = null;
        List<ExpedienteDoc> listaDocumentos = new ArrayList<>();
        if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES)
                .equals(idTramite)
                // MD-2190 Quitamos de la condición el trámite de alegación exclusión provisional
//                || utilConstantes.getGlobalCatalogosValores().get(
//                                Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL)
//                        .equals(idTramite)
                        ) {
            cdListadoDocEreg = this.generaFirmaReciboPresentacionAleg(dto, asunto, idTramite);
        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS)
                .equals(idTramite)) {
            cdListadoDocEreg = this.generaFirmaReciboPresentacion(dto,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS),
                    lstExpDocDTO);

        } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(idTramite)
                || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(idTramite)) {
            cdListadoDocEreg = this
                    .generaFirmaReciboPresentacionReq(dto,
                           idTramite,
                            lstExpDocDTO);

        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA)
                .equals(idTramite)) {
            cdListadoDocEreg = this.generaFirmaReciboPresentacion(dto,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA),
                    lstExpDocDTO);

        }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)
                    .equals(idTramite)) {
                cdListadoDocEreg = this.generaFirmaReciboPresentacion(dto,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA),
                        lstExpDocDTO);

            
        }else {
            cdListadoDocEreg = this.generaFirmaReciboPresentacion(dto, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION), lstExpDocDTO);
        }
        log.info("Código del listado de anexos: " + cdListadoDocEreg);

        if (StringUtils.isNotBlank(cdListadoDocEreg)) {
            // Creamos el documento de recibo de presentacion
            ExpedienteDoc pdfReciboPresentacion = this.insertarDocumento(dto, cdListadoDocEreg,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION),
                    idTramite, Constantes.AfirmacionNegacion.N, Boolean.TRUE);
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA)
                    .equals(idTramite)
                 || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA)
                    .equals(idTramite)) {
                pdfReciboPresentacion.setCodigoEreg(dto.getCdEreg());
                pdfReciboPresentacion.setFcRegistro(new Date());
                this.expedienteDocDAO.insertOrUpdate(pdfReciboPresentacion);
            
            }
            
            log.info("TEST PRESENTAR: Insertó documento expedienteDoc");

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS).equals(idTramite)) {
                
                pdfReciboPresentacion.setCodigoEreg(dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent());
                pdfReciboPresentacion.setFcRegistro(new Date());
                
                pdfReciboPresentacion.setCdAnexoEreg(cdListadoDocEreg);
                pdfReciboPresentacion.setTramite(catalogosValoresService.find(idTramite));
                this.expedienteDocDAO.insertOrUpdate(pdfReciboPresentacion);
                
                
                ExpedienteModfPresupPlazosOtrosDoc expedienteModfPresupPlazosOtrosDoc = new ExpedienteModfPresupPlazosOtrosDoc();
                expedienteModfPresupPlazosOtrosDoc.setExpedienteDoc(pdfReciboPresentacion);
                ExpedienteModifPresPlazoOtra expModifOtra = new ExpedienteModifPresPlazoOtra();

                expModifOtra.setIdExpedienteModifPresPlazoOtra(dto.getExpedienteModifOtraDTO()
                        .getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra());
                expedienteModfPresupPlazosOtrosDoc.setExpedienteModifPresPlazoOtra(expModifOtra);
                expedienteModfPresupPlazosOtrosDoc.setFcAltaFila(new Date());
                expedienteModfPresupPlazosOtrosDoc.setCdUsuAlta(dto.getCdUsuAlta());
                this.expedienteModfPresupPlazosOtrosDocService.getDao()
                        .insert(expedienteModfPresupPlazosOtrosDoc);
                

                for (ExpedienteDocDTO docDTO : lstExpDocDTO) {
                    ExpedienteDoc docAnexo = this.expedienteDocDAO
                            .find(docDTO.getIdExpedienteDoc());
                    docAnexo.setEstado(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)));
                    docAnexo.getEstado().setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION);
                    docAnexo.setCodigoEreg(dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                            .getCdEregPresent());
                    docAnexo.setFcRegistro(dto.getFcFirma());
                    listaDocumentos.add(docAnexo);
                }

            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(idTramite)) {
                pdfReciboPresentacion.setCodigoEreg(dto.getCdEregSubsanaReq());
                pdfReciboPresentacion.setFcRegistro(dto.getFcFirmaSubsanaReq());
                this.expedienteDocDAO.insertOrUpdate(pdfReciboPresentacion);

                for (ExpedienteDocDTO docDTO : lstExpDocDTO) {
                    ExpedienteDoc docAnexo = this.expedienteDocDAO
                            .find(docDTO.getIdExpedienteDoc());
                    docAnexo.setEstado(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)));
                    docAnexo.getEstado().setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);
                    docAnexo.setCodigoEreg(dto.getCdEregSubsanaReq());
                    docAnexo.setFcRegistro(dto.getFcFirmaSubsanaReq());
                    listaDocumentos.add(docAnexo);
                }
            }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA).equals(idTramite)) {
                for (ExpedienteDocDTO docDTO : lstExpDocDTO) {
                    ExpedienteDoc docAnexo = this.expedienteDocDAO
                            .find(docDTO.getIdExpedienteDoc());
                    docAnexo.setEstado(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
                    docAnexo.getEstado().setNmConstante(Constantes.EstadoDocumento.REVISION_OK);
                    docAnexo.setCodigoEreg(dto.getCdEreg());
                    docAnexo.setFcRegistro(dto.getFcFirma());
                    listaDocumentos.add(docAnexo);
                }
            
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(idTramite)) {
                pdfReciboPresentacion.setCodigoEreg(dto.getCdEregSubsanaAdm());
                pdfReciboPresentacion.setFcRegistro(dto.getFcFirmaSubsanaAdm());
                this.expedienteDocDAO.insertOrUpdate(pdfReciboPresentacion);
                    for (ExpedienteDocDTO docDTO : lstExpDocDTO) {
                        ExpedienteDoc docAnexo = this.expedienteDocDAO
                                .find(docDTO.getIdExpedienteDoc());
                        docAnexo.setEstado(
                                new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION)));
                        docAnexo.getEstado().setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);
                        docAnexo.setCodigoEreg(dto.getCdEregSubsanaAdm());
                        docAnexo.setFcRegistro(dto.getFcFirmaSubsanaAdm());
                        listaDocumentos.add(docAnexo);
                    }
            } else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA).equals(idTramite)) {

                for (ExpedienteDocDTO docDTO : lstExpDocDTO) {
                    ExpedienteDoc docAnexo = this.expedienteDocDAO
                            .find(docDTO.getIdExpedienteDoc());
                    docAnexo.setEstado(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION)));
                    docAnexo.getEstado().setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION);
                    docAnexo.setCodigoEreg(dto.getCdEreg());
                    docAnexo.setFcRegistro(dto.getFcFirma());
                    listaDocumentos.add(docAnexo);
                }

                    
                
            } else {
                // Marcamos los documentos con padre
                List<AtlasFilter> filtros = new ArrayList<>();
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                        dto.getIdExpediente(), Operador.EQUALS));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREG, Operador.NULL));
                filtros.add(
                        new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREGPADRE, Operador.NULL));
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

                listaDocumentos = this.expedienteDocDAO.find(filtros, null);
                // tramite solicitud
                fcFirma= dto.getFcFirma();
                cdEreg= dto.getCdEreg(); 
            }
            
            // tramite alegaciones
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES)
                    .equals(idTramite)) {
                fcFirma= dto.getFcFirmaAlegaciones();
                cdEreg= dto.getCdEregAlegaciones();
            }
                

            // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
            // debe usar una constante en lugar de su identificador.
            Boolean esInteresadoExtraComunitario = dto.getInteresadoDTO().getTipoIdentificacionDTO() != null
                    && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                            .equals(dto.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());
            // Salvaguarda (defensa en profundidad): si el identificador no cabe en la
            // columna (VARCHAR2(12)) aunque el catálogo no lo marque como
            // extracomunitario, se usa igualmente la constante para evitar ORA-12899.
            String cdUsuAuditoria = Boolean.TRUE.equals(esInteresadoExtraComunitario)
                    ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                    : Utils.valorAuditoria(dto.getInteresadoDTO().getCdNumIden());

            for (ExpedienteDoc expedienteDoc : listaDocumentos) {
                expedienteDoc.setCdAnexoEregPadre(cdListadoDocEreg);
                expedienteDoc.setCdUsuModif(cdUsuAuditoria);
                // solo si es tramite de alegaciones o de solicitud, en otro tramite ya el documento lleva su cdEreg del tramite que corresponda.
                if(cdEreg != null) {
                    expedienteDoc.setCodigoEreg(cdEreg);
                    expedienteDoc.setFcRegistro(fcFirma); 
                }
               
                expedienteDoc.setFcModifFila(new Date());
                this.expedienteDocDAO.update(expedienteDoc);
                this.expedienteDocHistDAO.crearHistorico(expedienteDoc, cdUsuAuditoria);
            }
            log.info("TEST PRESENTAR: Insertó historico expedienteDoc");
        }

        return cdListadoDocEreg;
    }

    /**
     * Inserta el documento en ExpedienteDoc
     *
     * @param dto del expediente
     * @param cdEreg del documento
     * @param tipoDocumento catálogo valores
     */
    private ExpedienteDoc insertarDocumento(ExpedienteDTO dto, String cdEreg, Integer tipoDocumento,
            Integer idTramite, String esAdicional, Boolean insertarAutoriaRepresentante)
            throws ServiceException {
        ExpedienteDoc expedienteDoc = new ExpedienteDoc();
        InteresadoDTO representanteLegal = dto.getRepresentanteLegal();

        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // debe usar una constante en lugar de su identificador.
        Boolean esInteresadoExtraComunitario = dto.getInteresadoDTO().getTipoIdentificacionDTO() != null
                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                        .equals(dto.getInteresadoDTO().getTipoIdentificacionDTO().getIdValor());
        // Salvaguarda (defensa en profundidad): ver comentario anterior.
        String cdUsuAltaInteresado = Boolean.TRUE.equals(esInteresadoExtraComunitario)
                ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                : Utils.valorAuditoria(dto.getInteresadoDTO().getCdNumIden());

        Expediente expAux = new Expediente();
        expAux.setIdExpediente(dto.getIdExpediente());
        expedienteDoc.setExpediente(expAux);
        expedienteDoc.setCdAnexoEreg(cdEreg);
        expedienteDoc.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REVISION_OK)));
        expedienteDoc.getEstado().setNmConstante(Constantes.EstadoDocumento.REVISION_OK);
        expedienteDoc.setTipoDocumento(catalogosValoresService.find(tipoDocumento));

        expedienteDoc.setTramite(catalogosValoresService.find(idTramite));
        expedienteDoc.setItAdicional(esAdicional);

        if (insertarAutoriaRepresentante.equals(Boolean.TRUE)
                && representanteLegal != null
                && representanteLegal.getCdNumIden() != null
                && !representanteLegal.getCdNumIden()
                        .equals(dto.getInteresadoDTO().getCdNumIden())) {
            try {
                RepresentanteJurDTO representante = this.representanteJurService
                        .obtenerPorInteresado(representanteLegal.getCdNumIden(),
                                dto.getInteresadoDTO().getCdNumIden());
                if (representante != null && representante.getIdRepresentanteJur() != null) {
                    RepresentanteJurtTramites representanteJurtTramites = new RepresentanteJurtTramites();
                    representanteJurtTramites.setExpediente(expedienteDoc.getExpediente());
                    representanteJurtTramites.setTramite(expedienteDoc.getTramite());
                    representanteJurtTramites.setRepresentanteJur(
                            RepresentanteJurToEntity.INSTANCE.apply(representante));
                    representanteJurtTramites
                            .setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
                    representanteJurtTramites.setFcAltaFila(new Date());
                    this.representanteJurtTramitesService.getDao()
                            .insertOrUpdate(representanteJurtTramites);

                    expedienteDoc
                            .setCdUsuAlta(Utils.valorAuditoria(representanteLegal.getCdNumIden()));
                } else {
                    expedienteDoc.setCdUsuAlta(cdUsuAltaInteresado);
                }

            } catch (ServiceException e) {
                throw new ServiceException(e);
            }
        } else {
            expedienteDoc.setCdUsuAlta(cdUsuAltaInteresado);
        }

        expedienteDoc.setFcAltaFila(new Date());

        return this.expedienteDocDAO.insert(expedienteDoc);

    }

    /**
     *
     * @param fields
     * @param key
     * @param value
     * @throws ServiceException
     */
    private void prepararCampoCheck(AcroFields fields, String key, String value)
            throws ServiceException {
        try {
            // Comprobamos si es un check de documentación
            if (key.contains("_doc_")) {
                if (Boolean.FALSE.equals(StringUtils.EMPTY.equals(value))) {
                    fields.setField(key, "Yes");
                } else {
                    fields.setField(key, "Off");
                }
            } else {
                if (Boolean.TRUE.toString().equals(value)) {
                    fields.setField(key, "Yes");
                } else {
                    fields.setField(key, "Off");
                }
            }
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    /**
     *
     * @param fields
     * @param campos
     * @throws ServiceException
     */
    private void prepararCampos(AcroFields fields, HashMap<String, String> campos)
            throws ServiceException {
        try {
            Set<Entry<String, String>> set = campos.entrySet();
            Iterator<Entry<String, String>> iterator = set.iterator();

            while (iterator.hasNext()) {
                Map.Entry<String, String> mentry = iterator.next();
                String key = String.valueOf(mentry.getKey());
                String value = Objects.toString(mentry.getValue(), StringUtils.EMPTY);

                // Es un check
                if (key.startsWith("chk_")) {

                    this.prepararCampoCheck(fields, key, value);

                } else {

                    BaseFont bf = BaseFont.createFont("/font/arial.ttf", BaseFont.IDENTITY_H,
                            BaseFont.EMBEDDED);

                    if(key.startsWith("radio_") && value != null && value.length() >127) {
                        value = value.substring(0,127);
                    }
                    fields.setField(key, value);
                    fields.addSubstitutionFont(bf);
                }
            }

        } catch (DocumentException | IOException | ServiceException e) {
            throw new ServiceException(e);
        }

    }

    /**
     * Método que rellena el PDF.
     *
     * @param campos the campos
     * @param plantillaPdf the plantilla pdf
     * @param firmaLocal the firma local
     * @param firmaServ the firma serv
     * @param dto the dto
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    private byte[] rellenarPdf(HashMap<String, String> campos, byte[] plantilla,
            Boolean incluirDatosEreg, ExpedienteDTO dto) throws ServiceException {
        byte[] archivo = null;
        try {
            PdfReader reader = new PdfReader(plantilla);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            AcroFields fields = stamper.getAcroFields();

            this.prepararCampos(fields, campos);

            // inicializamos valores vacios
            campos.put(FormularioPdf.CDNUMREFDOCUM, StringUtils.EMPTY);
            campos.put(FormularioPdf.DSDESTINO, StringUtils.EMPTY);
            campos.put(FormularioPdf.FCREGISTRO, StringUtils.EMPTY);
            campos.put(FormularioPdf.HUELLA, StringUtils.EMPTY);

            if (Boolean.TRUE.equals(incluirDatosEreg)) {
                Date fechaActual = new Date();

                // reemplazamos valores para los campos del registro de entrada
                campos.replace(FormularioPdf.CDNUMREFDOCUM, Utils.formatearNmEreg(dto.getCdEreg()));
                campos.replace(FormularioPdf.DSDESTINO, dto.getDsDestinoEreg());

                DateFormat df = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA);

                if (dto.getFcFirma() != null) {
                    fechaActual = dto.getFcFirma();
                }

                campos.replace(FormularioPdf.FCREGISTRO, df.format(fechaActual));

                campos.replace(FormularioPdf.HUELLA,
                        "Firmado por: " + dto.getInteresadoDTO().getDsNombre() + " "
                                + dto.getInteresadoDTO().getDsApellido1() + " "
                                + dto.getInteresadoDTO().getDsApellido2() + " // " + "NIF: "
                                + dto.getInteresadoDTO().getCdNumIden() + " // " + " Fecha: "
                                + fechaActual);
            }

            // inyectamos los valores a los campos del pdf
            fields.setField(FormularioPdf.CDNUMREFDOCUM, campos.get("cdNumRefDocum"));
            fields.setField(FormularioPdf.DSDESTINO, campos.get("dsDestino"));
            fields.setField(FormularioPdf.FCREGISTRO, campos.get("fcRegistro"));
            fields.setField(FormularioPdf.HUELLA, campos.get("HUELLA"));

            stamper.setFormFlattening(true);
            stamper.close();

            reader.close();
            archivo = baos.toByteArray();
            baos.close();

        } catch (DocumentException | IOException | ServiceException e) {
            log.error("Error al rellenar el PDF: " + e.getMessage());
            throw new ServiceException(e);
        }

        return archivo;
    }

    /**
     * Método que rellena el PDF de la JCT.
     *
     * @param campos the campos
     * @param plantillaPdf the plantilla pdf
     * @param firmaLocal the firma local
     * @param firmaServ the firma serv
     * @param dto the dto
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    private byte[] rellenarPdfJCT(HashMap<String, String> campos, byte[] plantilla,
            Boolean incluirDatosEreg, ExpedienteDTO dto, ExpedienteJusTecDTO expedienteJustec)
            throws ServiceException {
        byte[] archivo = null;
        try {
            PdfReader reader = new PdfReader(plantilla);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            AcroFields fields = stamper.getAcroFields();

            this.prepararCampos(fields, campos);

            // inicializamos valores vacios
            campos.put(FormularioPdf.CDNUMREFDOCUM, StringUtils.EMPTY);
            campos.put(FormularioPdf.DSDESTINO, StringUtils.EMPTY);
            campos.put(FormularioPdf.FCREGISTRO, StringUtils.EMPTY);
            campos.put(FormularioPdf.HUELLA, StringUtils.EMPTY);

            if (Boolean.TRUE.equals(incluirDatosEreg)) {
                Date fechaActual = new Date();

                // reemplazamos valores para los campos del registro de entrada
                campos.replace(FormularioPdf.CDNUMREFDOCUM,
                        Utils.formatearNmEreg(expedienteJustec.getCdEregPresent()));
                campos.replace(FormularioPdf.DSDESTINO, expedienteJustec.getDsDestinoEreg());

                DateFormat df = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA);

                if (expedienteJustec.getFcFirmaPresent() != null) {
                    fechaActual = expedienteJustec.getFcFirmaPresent();
                }

                campos.replace(FormularioPdf.FCREGISTRO, df.format(fechaActual));

                campos.replace(FormularioPdf.HUELLA,
                        "Firmado por: " + dto.getInteresadoDTO().getDsNombre() + " "
                                + dto.getInteresadoDTO().getDsApellido1() + " "
                                + dto.getInteresadoDTO().getDsApellido2() + " // " + "NIF: "
                                + dto.getInteresadoDTO().getCdNumIden() + " // " + " Fecha: "
                                + fechaActual);
            }

            // inyectamos los valores a los campos del pdf
            fields.setField(FormularioPdf.CDNUMREFDOCUM, campos.get("cdNumRefDocum"));
            fields.setField(FormularioPdf.DSDESTINO, campos.get("dsDestino"));
            fields.setField(FormularioPdf.FCREGISTRO, campos.get("fcRegistro"));
            fields.setField(FormularioPdf.HUELLA, campos.get("HUELLA"));

            stamper.setFormFlattening(true);
            stamper.close();

            reader.close();
            archivo = baos.toByteArray();
            baos.close();

        } catch (DocumentException | IOException | ServiceException e) {
            log.error("Error al rellenar el PDF: " + e.getMessage());
            throw new ServiceException(e);
        }

        return archivo;
    }

    /**
     * Método que rellena el PDF del contrato.
     *
     * @param campos the campos
     * @param plantillaPdf the plantilla pdf
     * @param firmaLocal the firma local
     * @param firmaServ the firma serv
     * @param dto the dto
     * @return the byte[]
     * @throws ServiceException the service exception
     */
    private byte[] rellenarPdfContrato(HashMap<String, String> campos, byte[] plantilla,
            Boolean incluirDatosEreg, ExpedienteDTO dto) throws ServiceException {
        byte[] archivo = null;
        try {
            PdfReader reader = new PdfReader(plantilla);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfStamper stamper = new PdfStamper(reader, baos);
            AcroFields fields = stamper.getAcroFields();

            this.prepararCampos(fields, campos);

            // inicializamos valores vacios
            campos.put(FormularioPdf.CDNUMREFDOCUM, StringUtils.EMPTY);
            campos.put(FormularioPdf.DSDESTINO, StringUtils.EMPTY);
            campos.put(FormularioPdf.FCREGISTRO, StringUtils.EMPTY);
            campos.put(FormularioPdf.HUELLA, StringUtils.EMPTY);

            if (Boolean.TRUE.equals(incluirDatosEreg)) {
                Date fechaActual = new Date();

                // VMM 28/10/24 MD-913: buscamos el contrato del expdte por si
                // no llega relleno
                if (Objects.isNull(dto.getExpedienteContrato())) {
                    ExpedienteDTO expdteAuxDTO = this.expedienteService
                            .buscarIdSimple(dto.getIdExpediente(), Boolean.TRUE);
                    dto.setExpedienteContrato(expdteAuxDTO.getExpedienteContrato());
                }
                // reemplazamos valores para los campos del registro de entrada
                campos.replace(FormularioPdf.CDNUMREFDOCUM,
                        Utils.formatearNmEreg(dto.getExpedienteContrato().getCdEregPresent()));
                campos.replace(FormularioPdf.DSDESTINO,
                        dto.getExpedienteContrato().getDsDestinoEreg());

                DateFormat df = new SimpleDateFormat(Constantes.Utiles.FORMATO_FECHA_HORA);

                if (dto.getExpedienteContrato().getFcFirmaPresent() != null) {
                    fechaActual = dto.getExpedienteContrato().getFcFirmaPresent();
                }

                campos.replace(FormularioPdf.FCREGISTRO, df.format(fechaActual));

                campos.replace(FormularioPdf.HUELLA,
                        "Firmado por: " + dto.getInteresadoDTO().getDsNombre() + " "
                                + dto.getInteresadoDTO().getDsApellido1() + " "
                                + dto.getInteresadoDTO().getDsApellido2() + " // " + "NIF: "
                                + dto.getInteresadoDTO().getCdNumIden() + " // " + " Fecha: "
                                + fechaActual);
            }

            // inyectamos los valores a los campos del pdf
            fields.setField(FormularioPdf.CDNUMREFDOCUM, campos.get("cdNumRefDocum"));
            fields.setField(FormularioPdf.DSDESTINO, campos.get("dsDestino"));
            fields.setField(FormularioPdf.FCREGISTRO, campos.get("fcRegistro"));
            fields.setField(FormularioPdf.HUELLA, campos.get("HUELLA"));

            stamper.setFormFlattening(true);
            stamper.close();

            reader.close();
            archivo = baos.toByteArray();
            baos.close();

        } catch (DocumentException | IOException | ServiceException e) {
            log.error("Error al rellenar el PDF: " + e.getMessage());
            throw new ServiceException(e);
        }

        return archivo;
    }

    /**
     *
     */
    @Override
    public byte[] obtenerPlantilla(ExpedienteDTO dto) throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        return this.impresoService.crearInforme(dto.getImpresoDTO(), null, dto);
    }

    @Override
    public byte[] obtenerPlantilla(ExpedienteDTO dto, Map<String, String> mapaDatos)
            throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        return this.impresoService.crearInforme(dto.getImpresoDTO(), mapaDatos, dto);
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarExpediente(ExpedienteDTO expedienteDTO) throws ServiceException {
        byte[] expedientePdf = null;

        // Si hay código de EREG lo descargamos de EREG
        if (StringUtils.isNotBlank(expedienteDTO.getCdEreg())) {
            // Obtenemos el código de ereg del impreso
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    expedienteDTO.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_EXPEDIENTE_FIRMADO), Operador.EQUALS));

            List<ExpedienteDoc> listaExpedientes = this.expedienteDocDAO.find(filtros, null);

            if (!listaExpedientes.isEmpty()) {
                // Recuperamos el pdf
                expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(
                        expedienteDTO.getCdEreg(), listaExpedientes.get(0).getCdAnexoEreg());
            }

        } else {

            // Recuperamos los valores a insertar en el PDF
            HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCampos(expedienteDTO);

            // Construimos la plantilla PDF
            byte[] plantillaPdf = this.obtenerPlantilla(expedienteDTO, hamCampos);

            // Inyectamos los valores a la plantilla PDF
            expedientePdf = this.rellenarPdf(hamCampos, plantillaPdf, Boolean.FALSE, expedienteDTO);
        }

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(
                    expedienteDTO.getCdExpediente() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    /**
     *
     */
    @Override
    public List<DocumentoDTO> descargarListadoDocumentos(Integer idTramite,
            ExpedienteDTO expedienteDTO, String cdEreg) throws ServiceException {

        List<byte[]> listaDocAnexados = new ArrayList<>();

        List<Integer> lstTipoDoc = new ArrayList<>();
        lstTipoDoc.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION));

        // Obtenemos el código de ereg de cada listado de documentos anexos
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                expedienteDTO.getIdExpediente(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO, lstTipoDoc,
                Operador.IN));

        if (Objects.nonNull(idTramite)) {
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE, idTramite, Operador.EQUALS));
        }

        List<ExpedienteDoc> listaExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

        // Si hay código de EREG lo descargamos de EREG
        if (StringUtils.isNotBlank(cdEreg)) {
            // Recuperamos el pdf de cada uno
            for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
                
                if(Objects.nonNull(idTramite) && idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA))){
                    if (StringUtils.isNotBlank(expedienteDoc.getCodigoEreg())
                            && StringUtils.isNotBlank(expedienteDoc.getCdAnexoEreg())) {

                        byte[] result = this.eregAdmeService.descargarAnexoPorCodigo(expedienteDoc.getCodigoEreg(),
                                expedienteDoc.getCdAnexoEreg());
                        if (result != null && result.length > 0) {
                            listaDocAnexados.add(result);
                        }

                    }
                    
                }else if(Objects.nonNull(idTramite) && idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA))){
                    if (StringUtils.isNotBlank(expedienteDoc.getCodigoEreg()) && expedienteDoc.getCodigoEreg().equalsIgnoreCase(cdEreg)
                            && StringUtils.isNotBlank(expedienteDoc.getCdAnexoEreg())) {

                        byte[] result = this.eregAdmeService.descargarAnexoPorCodigo(expedienteDoc.getCodigoEreg(),
                                expedienteDoc.getCdAnexoEreg());
                        if (result != null && result.length > 0) {
                            listaDocAnexados.add(result);
                        }

                    }
                }else {
                    if (StringUtils.isNotBlank(cdEreg)
                            && StringUtils.isNotBlank(expedienteDoc.getCdAnexoEreg())) {

                        byte[] result = this.eregAdmeService.descargarAnexoPorCodigo(cdEreg,
                                expedienteDoc.getCdAnexoEreg());
                        if (result != null && result.length > 0) {
                            listaDocAnexados.add(result);
                        }

                    }
                }
                
            }
        }

        List<DocumentoDTO> listaDocumentoDTO = new ArrayList<>();
        log.info(" El total de listaDocAnexados:" + listaDocAnexados.size());
        for (byte[] anexo : listaDocAnexados) {
            DocumentoDTO documento = new DocumentoDTO();
            documento.setContenidoFichero(anexo);
            documento.setNombreFichero(
                    expedienteDTO.getCdExpediente() + "." + Constantes.Extensiones.PDF);
            listaDocumentoDTO.add(documento);
        }

        return listaDocumentoDTO;
    }

    /**
     * Método para recuperar los documentos anexos al expediente
     *
     * @param expedienteDTO
     * @return byte[]
     * @throws ServiceException
     */
    private byte[] generarAnexoDocumentacion(ExpedienteDTO expedienteDTO, Integer idTramite,
            List<ExpedienteDocDTO> lstDoc) throws ServiceException {

        ImpresoDTO impresoDTO = null;
        // Recuperamos la convocatoria
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // Recuperamos el interesado
        InteresadoDTO interesado = this.interesadoService
                .buscarId(expedienteDTO.getInteresadoDTO().getIdInteresado());

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        // Recuperamos la plantilla por convocatoria y tramite
        ConvocatoriaPlaDTO convocatoriaPlaDTO = this.convocatoriaComService
                .obtenerConvocatoriaPla(convocatoriaDTO.getIdConvocatoria(), idTramite);
        
        if(convocatoriaPlaDTO== null || convocatoriaPlaDTO.getTramiteDTO()== null || convocatoriaPlaDTO.getTramiteDTO().getIdValor()==null) {
            CatalogosValoresDTO tramite = new CatalogosValoresDTO();
            tramite.setIdValor(idTramite);
            convocatoriaPlaDTO = new ConvocatoriaPlaDTO();
            convocatoriaPlaDTO.setTramiteDTO(tramite);
        }

        // Recuperamos el listado de documentos adjuntos del expediente
        List<DocumentoDTO> lstDocumentosDTO = null;
        if (idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS))) {
            lstDocumentosDTO = this.documentoService.obtenerDocumentosExpModifPresupPlazosOtros(
                    expedienteDTO, lstDoc, Boolean.FALSE);
            log.info("Documentos recuperados DocumentosExpModifPresupPlazosOtros: "
                    + lstDocumentosDTO.size());
            List<Integer> lstTipoImpreso = new ArrayList<>();
            lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_MODIF_PRESUP_PLAZOS_OTROS));
            impresoDTO = this.impresoService.buscarPorConvActor(convocatoriaDTO.getIdConvocatoria(),
                    expedienteDTO.getConvocatoriaActDTO().getIdActor(), lstTipoImpreso);

            impresoDTO = impresoDTO == null ? new ImpresoDTO() : impresoDTO;
            
        }else if (idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA))) {
            lstDocumentosDTO = this.documentoService.obtenerDocumentosExpModifPresupPlazosOtros(
                    expedienteDTO, lstDoc, Boolean.FALSE);
            log.info("Documentos recuperados de aceptacion de ayuda: "
                    + lstDocumentosDTO.size());
            
            List<Integer> lstTipoImpreso = new ArrayList<>();
            lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));
            impresoDTO = this.impresoService.buscarPorConvActor(convocatoriaDTO.getIdConvocatoria(),
                    expedienteDTO.getConvocatoriaActDTO().getIdActor(), lstTipoImpreso);

            impresoDTO = impresoDTO == null ? new ImpresoDTO() : impresoDTO;
            
        } else {
            lstDocumentosDTO = this.documentoService.obtenerDocumentosExpediente(expedienteDTO,
                    Boolean.FALSE, idTramite, lstDoc);
            log.info("Documentos recuperados: " + lstDocumentosDTO.size());

            List<Integer> lstTipoImpreso = new ArrayList<>();
            lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
            impresoDTO = this.impresoService.buscarPorConvActor(convocatoriaDTO.getIdConvocatoria(),
                    expedienteDTO.getConvocatoriaActDTO().getIdActor(), lstTipoImpreso);

            impresoDTO = impresoDTO == null ? new ImpresoDTO() : impresoDTO;
        }

        // Quitamos los que ya estén en EREG
        /**
         * POM: MD-956 [SOL] No se muestra la relación de documentos
         * presentados. Subsanación administrativa lstDocumentosDTO.removeIf(x
         * -> Boolean.TRUE.equals(x.getEnEreg()));
         */
        log.info("Documentos pendientes presentar: " + lstDocumentosDTO.size());

        // Creamos el anexo
        return this.generarRecibo(convocatoriaDTO, convocatoriaPlaDTO, null, expedienteDTO,
                interesado, lstDocumentosDTO, impresoDTO);
    }

    /**
     * Método para recuperar los documentos anexos al expediente
     *
     * @param expedienteDTO
     * @return byte[]
     * @throws ServiceException
     */
    private byte[] generarAnexoDocumentacionAleg(ExpedienteDTO expedienteDTO, String asunto,
            Integer idTramite) throws ServiceException {

        // Recuperamos el listado de documentos adjuntos del expediente
        List<DocumentoDTO> lstDocumentosDTO = this.documentoService
                .obtenerDocumentosExpediente(expedienteDTO, Boolean.FALSE, idTramite, null);

        log.info("Documentos recuperados: " + lstDocumentosDTO.size());

        // Quitamos los que ya estén en EREG
        lstDocumentosDTO.removeIf(x -> Boolean.TRUE.equals(x.getEnEreg()));

        log.info("Documentos pendientes presentar: " + lstDocumentosDTO.size());

        // Recuperamos la convocatoria
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // Recuperamos el interesado
        InteresadoDTO interesado = this.interesadoService
                .buscarId(expedienteDTO.getInteresadoDTO().getIdInteresado());

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        List<Integer> lstTipoImpreso = new ArrayList<>();
        if (idTramite.equals(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES))) {
            lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
        } else {
            lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_COMPLEMENTARIO));
        }

        ImpresoDTO impresoDTO = this.impresoService.buscarPorConvActor(
                convocatoriaDTO.getIdConvocatoria(),
                expedienteDTO.getConvocatoriaActDTO().getIdActor(), lstTipoImpreso);

        ConvocatoriaPlaDTO convocatoriaPlaDTO = new ConvocatoriaPlaDTO();
        if (idTramite.equals(
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES))) {
            CatalogosValoresDTO tramiteDTO = new  CatalogosValoresDTO(idTramite);
            tramiteDTO.setNmConstante(Constantes.Tramite.ALEGACIONES);
            convocatoriaPlaDTO.setTramiteDTO(tramiteDTO);
            
        }else {
            convocatoriaPlaDTO.setTramiteDTO(new CatalogosValoresDTO(idTramite));
        }
        
        
        expedienteDTO.setFcFirmaAlegaciones(new Date());
        
        // Si existe impreso se recupera el asunto
        if (impresoDTO != null
                && StringUtils.isNotBlank(impresoDTO.getCdIdentificacion())) {
            Integer indexContains = impresoDTO.getCdIdentificacion().indexOf('_');
            if (indexContains < 0) {
                expedienteDTO.setCdAsunto(impresoDTO.getCdIdentificacion());
            } else {
                expedienteDTO.setCdAsunto(
                        impresoDTO.getCdIdentificacion().substring(0, indexContains));
            }
        }
        
     // Se recupera el asunto del dto
        String cdAsunto = expedienteDTO.getCdAsunto();
        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);
        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();
        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG de presentacion de alegaciones");
        expedienteDTO = this.eregAdmeService.generarEntradaAlegaciones(expedienteDTO, cdAsunto, cdUnidad);
        expedienteDTO.setCdEregAlegaciones(expedienteDTO.getCdEregAlegaciones());
        expedienteDTO.setDsDestinoEreg(expedienteDTO.getDsDestinoEreg());
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + expedienteDTO.getCdEregAlegaciones());
        log.info("Destino: " + expedienteDTO.getDsDestinoEreg());
        
        Expediente espedienteEdit = this.expedienteDAO.find(expedienteDTO.getIdExpediente());
        espedienteEdit.setCdEregAlegaciones(expedienteDTO.getCdEregAlegaciones());
        espedienteEdit.setFcFirmaAlegaciones(expedienteDTO.getFcFirmaAlegaciones());
        this.expedienteDAO.update(espedienteEdit);
        // Usuario de persona física extracomunitario: no tiene NIF/NIE, la auditoría
        // debe usar una constante en lugar de su identificador.
        this.expedienteHistDAO.crearHistorico(espedienteEdit,
                solicitud.getInteresado().getTipoIdentificacion() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
 
        // Creamos el anexo
        return this.generarRecibo(convocatoriaDTO, convocatoriaPlaDTO, asunto, expedienteDTO,
                interesado, lstDocumentosDTO, impresoDTO);
    }

    /**
     * Método que genera el documento pdf de listado de documentacion para el
     * anexo
     *
     * @param lstDocumentoDTO
     * @return byte[]
     * @throws ServiceException
     */
    private byte[] generarRecibo(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, String asuntoAlegacion,
            ExpedienteDTO expedienteDTO, InteresadoDTO interesado,
            List<DocumentoDTO> lstDocumentoDTO, ImpresoDTO impresoDTO) throws ServiceException {

        byte[] bytesSalida = null;

        try {

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String cdEreg;
            Map<String, String> mapaDatosEntrada = new HashMap<>();
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(expedienteDTO.getCdEregAlegaciones()));
                cdEreg=expedienteDTO.getCdEregAlegaciones();
            }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(expedienteDTO.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent()));
                cdEreg=expedienteDTO.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent();
            }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(expedienteDTO.getCdEregSubsanaReq()));
                cdEreg=expedienteDTO.getCdEregSubsanaReq();
            }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(expedienteDTO.getCdEregSubsanaAdm()));
                cdEreg=expedienteDTO.getCdEregSubsanaAdm();
            }
            
            else {
                mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(expedienteDTO.getCdEreg()));
                cdEreg=expedienteDTO.getCdEreg();
            }

            mapaDatosEntrada.put("destino", expedienteDTO.getDsDestinoEreg());
            mapaDatosEntrada.put("fecha",
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));

            this.generarCabeceraPdf(convocatoriaDTO, impresoDTO.getImagenLogoDTO(), document, baos,
                    Boolean.TRUE, mapaDatosEntrada);

            document.open();

            // MD-2869: si la convocatoria tiene logos de fondos europeos (o logo de
            // organismo), la cabecera es más alta y el "registro de entrada" se
            // posiciona más abajo (ver HeaderFooterPdfEvent.margenSuperior=110); hay
            // que añadir el mismo espacio compensatorio que en crearInforme() para
            // que no tape el título del documento.
            Boolean esFondoEuropeo = Constantes.AfirmacionNegacion.S
                    .equals(convocatoriaDTO.getItFondosEuropa());
            Boolean agregarEspacioLogos = Boolean.TRUE.equals(esFondoEuropeo)
                    || (impresoDTO.getImagenLogoDTO() != null
                            && StringUtils.isNotBlank(impresoDTO.getImagenLogoDTO().getCdFica()));
            
            String tituloImpreso=impresoDTO.getDsDescripcion();
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                tituloImpreso="Formulario de alegaciones";
            }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())
                    || utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                tituloImpreso="Formulario de subsanación";
            }

            informeHelperPdfService.agregarTitulo(document, Constantes.ReciboPresentacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),tituloImpreso,expedienteDTO.getCdExpediente(), agregarEspacioLogos, Boolean.TRUE);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())
                    // MD-2190 Quitamos la condición del trámite de alegación exclusión provisional
//                    || Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL.equals(convocatoriaPlaDTO.getTramiteDTO().getNmConstante())
                    ) {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        asuntoAlegacion, tipoLetra10Bold, tipoLetra9, 2);
            } else {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        convocatoriaPlaDTO.getDsAsunto(), tipoLetra10Bold, tipoLetra9, 2);
            }

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DATOS_INTERESADO, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.INTERESADO,
                    interesado.getDsNombre() + " " + interesado.getDsApellido1() + " "
                            + interesado.getDsApellido2() + "     NIF: "
                            + interesado.getCdNumIden(),
                    tipoLetra9, tipoLetra9, 2);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.EXPEDIENTE,
                    expedienteDTO.getCdExpediente() + "     Anexo: " + cdEreg,
                    tipoLetra9, tipoLetra9, 2);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DOCUMENTACION, null, tipoLetra10Bold, null, 1);

            if(lstDocumentoDTO!= null && !lstDocumentoDTO.isEmpty()) {
                informeHelperPdfService.agregarSubTituloDes(document,
                        "  " + Constantes.ReciboPresentacion.DOCUMENTACION_DESCRIPCION, null,
                        tipoLetra9, null, 1);
            }else {
                informeHelperPdfService.agregarSubTituloDes(document,
                        "  " + Constantes.ReciboPresentacion.NO_DOCUMENTACION_DESCRIPCION, null,
                        tipoLetra9, null, 1);
            }
            

            informeHelperPdfService.agregarListadoDocumentos(document, lstDocumentoDTO);

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error("Error al crear el anexo de documentación: " + e.getMessage());
           // throw new ServiceException(e);
        }

        return bytesSalida;
    }

    /**
     * Método que genera el documento pdf de listado de documentacion para el
     * anexo JCT
     *
     * @param lstDocumentoDTO
     * @return byte[]
     * @throws ServiceException
     */
    private byte[] generarReciboJCT(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, String asuntoAlegacion,
            ExpedienteDTO expedienteDTO, InteresadoDTO interesado,
            List<DocumentoDTO> lstDocumentoDTO, ImpresoDTO impresoDTO,
            ExpedienteJusTecDTO expedienteJustec, Boolean subsanar) throws ServiceException {

        byte[] bytesSalida = null;

        try {

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Map<String, String> mapaDatosEntrada = new HashMap<>();

            // VMM MD-913 28/10/24: comprobamos si es subsanacion o presentacion
            String cdEreg = expedienteJustec.getCdEregPresent();
            if (Boolean.TRUE.equals(subsanar)) {
                cdEreg = expedienteJustec.getCdEregSubsana();
            }
            mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(cdEreg));
            mapaDatosEntrada.put("destino", expedienteJustec.getDsDestinoEreg());
            mapaDatosEntrada.put("fecha",
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));

            this.generarCabeceraPdf(convocatoriaDTO, impresoDTO.getImagenLogoDTO(), document, baos,
                    Boolean.TRUE, mapaDatosEntrada);

            document.open();

            informeHelperPdfService.agregarTitulo(document, Constantes.ReciboPresentacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),impresoDTO.getDsDescripcion(),expedienteDTO.getCdExpediente(), Boolean.FALSE, Boolean.FALSE);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            if (Constantes.Tramite.ALEGACIONES.equals(convocatoriaPlaDTO.getTramiteDTO().getNmConstante())) {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        asuntoAlegacion, tipoLetra10Bold, tipoLetra9, 2);
            } else {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        convocatoriaPlaDTO.getDsAsunto(), tipoLetra10Bold, tipoLetra9, 2);
            }

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DATOS_INTERESADO, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.INTERESADO,
                    interesado.getDsNombre() + " " + interesado.getDsApellido1() + " "
                            + interesado.getDsApellido2() + "     NIF: "
                            + interesado.getCdNumIden(),
                    tipoLetra9, tipoLetra9, 2);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.EXPEDIENTE,
                    expedienteDTO.getCdExpediente() + "     Anexo: " + cdEreg, tipoLetra9,
                    tipoLetra9, 2);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DOCUMENTACION, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.DOCUMENTACION_DESCRIPCION, null,
                    tipoLetra9, null, 1);

            informeHelperPdfService.agregarListadoDocumentos(document, lstDocumentoDTO);

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error("Error al crear el anexo de documentación: " + e.getMessage());
            throw new ServiceException(e);
        }

        return bytesSalida;
    }

    /**
     * Método que genera el documento pdf de listado de documentacion para el
     * anexo contrato
     *
     * @param lstDocumentoDTO
     * @return byte[]
     * @throws ServiceException
     */
    private byte[] generarReciboContrato(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, String asuntoAlegacion,
            ExpedienteDTO expedienteDTO, InteresadoDTO interesado,
            List<DocumentoDTO> lstDocumentoDTO, ImpresoDTO impresoDTO) throws ServiceException {

        byte[] bytesSalida = null;

        try {

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            Map<String, String> mapaDatosEntrada = new HashMap<>();

            // VMM MD-913 28/10/24: comprobamos si es subsanacion o presentacion
            String cdEreg = expedienteDTO.getExpedienteContrato().getCdEregPresent();

            mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(cdEreg));
            mapaDatosEntrada.put("destino",
                    expedienteDTO.getExpedienteContrato().getDsDestinoEreg());
            mapaDatosEntrada.put("fecha",
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));

            this.generarCabeceraPdf(convocatoriaDTO, impresoDTO.getImagenLogoDTO(), document, baos,
                    Boolean.TRUE, mapaDatosEntrada);

            document.open();

            informeHelperPdfService.agregarTitulo(document, Constantes.ReciboPresentacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),impresoDTO.getDsDescripcion(),expedienteDTO.getCdExpediente(), Boolean.FALSE, Boolean.FALSE);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            if (Constantes.Tramite.ALEGACIONES.equals(convocatoriaPlaDTO.getTramiteDTO().getNmConstante())) {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        asuntoAlegacion, tipoLetra10Bold, tipoLetra9, 2);
            } else {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        convocatoriaPlaDTO.getDsAsunto(), tipoLetra10Bold, tipoLetra9, 2);
            }

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DATOS_INTERESADO, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.INTERESADO,
                    interesado.getDsNombre() + " " + interesado.getDsApellido1() + " "
                            + interesado.getDsApellido2() + "     NIF: "
                            + interesado.getCdNumIden(),
                    tipoLetra9, tipoLetra9, 2);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.EXPEDIENTE,
                    expedienteDTO.getCdExpediente() + "     Anexo: " + cdEreg, tipoLetra9,
                    tipoLetra9, 2);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DOCUMENTACION, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.DOCUMENTACION_DESCRIPCION, null,
                    tipoLetra9, null, 1);

            informeHelperPdfService.agregarListadoDocumentos(document, lstDocumentoDTO);

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error("Error al crear el anexo de documentación: " + e.getMessage());
            throw new ServiceException(e);
        }

        return bytesSalida;
    }

    private byte[] generarReciboSubsanacionReq(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, String asuntoAlegacion,
            ExpedienteDTO expedienteDTO, InteresadoDTO interesado,
            List<DocumentoDTO> lstDocumentoDTO, ImpresoDTO impresoDTO) throws ServiceException {

        byte[] bytesSalida = null;

        try {

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            String cdEreg="";
            String titDesImpreso="";
            
            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                titDesImpreso="Formulario de alegación";
                cdEreg =expedienteDTO.getCdEregAlegaciones();
            }else if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())
                   ) {
                titDesImpreso="Formulario de subsanación";
                cdEreg = expedienteDTO.getCdEregSubsanaReq();
            }else if ( utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())) {
                titDesImpreso="Formulario de subsanación";
                cdEreg = expedienteDTO.getCdEregSubsanaAdm();
            }

            Map<String, String> mapaDatosEntrada = new HashMap<>();
            mapaDatosEntrada.put("codigo", Utils.formatearNmEreg(cdEreg));
            mapaDatosEntrada.put("destino", expedienteDTO.getDsDestinoEreg());
            mapaDatosEntrada.put("fecha",
                    Utils.dateToString(new Date(), Constantes.Utiles.FORMATO_FECHA_HORA));

            this.generarCabeceraPdf(convocatoriaDTO, impresoDTO.getImagenLogoDTO(), document, baos,
                    Boolean.TRUE, mapaDatosEntrada);

            document.open();
            
           
            
            informeHelperPdfService.agregarTitulo(document, Constantes.ReciboPresentacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),titDesImpreso,expedienteDTO.getCdExpediente(), Boolean.FALSE, Boolean.TRUE);

            // Agregamos bloque con la informacion del solicitante
            informeHelperPdfService.agregarBloqueDatosInteresado(document, expedienteDTO, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            if (utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES).equals(convocatoriaPlaDTO.getTramiteDTO().getIdValor())
                    // MD-2190 Quitamos la condición del trámite de alegación exclusión provisional
//                    || Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL.equals(convocatoriaPlaDTO.getTramiteDTO().getNmConstante())
                    ) {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        asuntoAlegacion, tipoLetra10Bold, tipoLetra9, 2);
            } else {
                informeHelperPdfService.agregarSubTituloDes(document, Constantes.ReciboPresentacion.ASUNTO,
                        convocatoriaPlaDTO.getDsAsunto(), tipoLetra10Bold, tipoLetra9, 2);
            }

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DATOS_INTERESADO, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.INTERESADO,
                    interesado.getDsNombre() + " " + interesado.getDsApellido1() + " "
                            + interesado.getDsApellido2() + "     NIF: "
                            + interesado.getCdNumIden(),
                    tipoLetra9, tipoLetra9, 2);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.EXPEDIENTE,
                    expedienteDTO.getCdExpediente() + "     Anexo: " + expedienteDTO.getCdEreg(),
                    tipoLetra9, tipoLetra9, 2);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    Constantes.ReciboPresentacion.DOCUMENTACION, null, tipoLetra10Bold, null, 1);

            informeHelperPdfService.agregarSubTituloDes(document,
                    "  " + Constantes.ReciboPresentacion.DOCUMENTACION_DESCRIPCION, null,
                    tipoLetra9, null, 1);

            informeHelperPdfService.agregarListadoDocumentos(document, lstDocumentoDTO);

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error("Error al crear el anexo de documentación: " + e.getMessage());
            throw new ServiceException(e);
        }

        return bytesSalida;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarZipSolicitud(Integer idSolicitud) throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            // Obtenemos los expedientes de la solicitud
            List<ExpedienteDTO> lstExpedientesDTO = this.expedienteService
                    .obtenerExpedientesPorSolicitud(idSolicitud);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            for (ExpedienteDTO dto : lstExpedientesDTO) {
                this.empaquetarZip(dto, zos);
            }

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(idSolicitud + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip solicitud, ", ioex);
            throw new ServiceException("Error al crear el zip solicitud.");
        }

        return documentoDTO;
    }

    @Override
    public DocumentoDTO descargarZipAlegacion(Integer idExpedienteDoc) throws ServiceException {

        ExpedienteDoc expedienteDoc = this.expedienteDocDAO.find(idExpedienteDoc);
        Expediente expediente = this.expedienteService
                .find(expedienteDoc.getExpediente().getIdExpediente());

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE.apply(expediente);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipTramite(expedienteDoc.getTramite().getIdValor(),
                    expedienteDoc.getCdAnexoEreg(), expedienteDTO, zos, idExpedienteDoc);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO
                    .setNombreFichero(expedienteDTO.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip alegación, ", ioex);
            throw new ServiceException("Error al crear el zip alegación.");
        }

        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarZipTramite(Integer idExpedienteDoc) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        ExpedienteDoc expedienteDoc = this.expedienteDocDAO.find(idExpedienteDoc);
        Expediente expediente = this.expedienteService
                .find(expedienteDoc.getExpediente().getIdExpediente());
        String cdEreg = expedienteDoc.getCdAnexoEreg();
        try {
            ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE.apply(expediente);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipTramite(expedienteDoc.getTramite().getIdValor(), cdEreg,
                    expedienteDTO, zos, idExpedienteDoc);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO
                    .setNombreFichero(expedienteDTO.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip alegación, ", ioex);
            throw new ServiceException("Error al crear el zip alegación.");
        }

        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarZipExpediente(Integer idExpediente) throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);

            if (dto != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ZipOutputStream zos = new ZipOutputStream(baos);

                this.empaquetarZip(dto, zos);

                // Agregamos expedientes hijos
                List<ExpedienteDTO> hijosDto = new ArrayList<>();
                if (dto.getIdSolicitud() != null) {
                    hijosDto = this.expedienteService
                            .buscarExpedientePorExpedientePadre(dto.getIdSolicitud(), idExpediente);
                }
                for (ExpedienteDTO hijoDto : hijosDto) {
                    // VMM 05/12/24 MD-1066: obtenemos los datos de los impresos
                    // de los expdtes hijos
                    ExpedienteDTO expdteHijoDTO = this.expedienteService
                            .buscarId(hijoDto.getIdExpediente());
                    this.empaquetarZip(expdteHijoDTO, zos);
                }

                zos.closeEntry();
                zos.close();
                String nombreDocu = dto.getCdExpediente();
                if (nombreDocu != null && nombreDocu.indexOf("/") != -1) {
                    nombreDocu = nombreDocu.replace("/", "_") + ".zip";
                }
                documentoDTO.setNombreFichero(nombreDocu);
                documentoDTO.setContenidoFichero(baos.toByteArray());

            }

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;
    }
    
    @Override
    public DocumentoDTO descargarZipExpedienteEval(Integer idExpediente) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);

            if (dto != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ZipOutputStream zos = new ZipOutputStream(baos);

                this.empaquetarZipEval(dto, zos);

                // Agregamos expedientes hijos
                List<ExpedienteDTO> hijosDto = new ArrayList<>();
                if (dto.getIdSolicitud() != null) {
                    hijosDto = this.expedienteService
                            .buscarExpedientePorExpedientePadre(dto.getIdSolicitud(), idExpediente);
                }
                for (ExpedienteDTO hijoDto : hijosDto) {
                    // VMM 05/12/24 MD-1066: obtenemos los datos de los impresos
                    // de los expdtes hijos
                    ExpedienteDTO expdteHijoDTO = this.expedienteService
                            .buscarId(hijoDto.getIdExpediente());
                    this.empaquetarZipEval(expdteHijoDTO, zos);
                }

                zos.closeEntry();
                zos.close();
                String nombreDocu = dto.getCdExpediente();
                if (nombreDocu != null && nombreDocu.indexOf("/") != -1) {
                    nombreDocu = nombreDocu.replace("/", "_") + ".zip";
                }
                documentoDTO.setNombreFichero(nombreDocu);
                documentoDTO.setContenidoFichero(baos.toByteArray());

            }

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarInformeValAlegacion(Integer idExpediente) throws ServiceException {
        ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);
        ExpedienteDoc expedienteDoc = this.expedienteDocService.buscarPorExpedienteTipoDocumento(
                idExpediente,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.ALEGACIONES),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES));

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        String cdExpediente = dto.getCdExpediente().replace("/", "_") + "-documentos-de-alegacion";
        try {
            ZipOutputStream zos = new ZipOutputStream(baos);

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            DocumentoDTO docAleg = this.documentoService
                    .obtenerDocAleg(dto.getCdFicaValoracionAleg());

            ZipEntry ficaAlegZIP = new ZipEntry(cdExpediente + "/" + dto.getCdFicaValoracionAleg()
                    + "_" + docAleg.getNombreFichero() + ".pdf");
            zos.putNextEntry(ficaAlegZIP);
            zos.write(docAleg.getContenidoFichero());

            if (Objects.nonNull(expedienteDoc)) {
                DocumentoDTO docInforme = this.documentoService
                        .obtenerDocAleg(expedienteDoc.getCdFica());

                ZipEntry ficaInformeAlegZIP = new ZipEntry(cdExpediente + "/"
                        + expedienteDoc.getCdFica() + "_" + docInforme.getNombreFichero() + ".pdf");
                zos.putNextEntry(ficaInformeAlegZIP);
                zos.write(docInforme.getContenidoFichero());
            }

            zos.closeEntry();
            zos.close();
        } catch (IOException ioex) {
            log.error("Error al crear el zip informe valoracion alegacion, ", ioex);
            throw new ServiceException("Error al crear el zip informe valoracion alegacion.");
        }
        DocumentoDTO documentoDTO = new DocumentoDTO();
        documentoDTO.setContenidoFichero(baos.toByteArray());
        documentoDTO.setNombreFichero(dto.getCdExpediente().replace("/", "_") + ".zip");
        return documentoDTO;
    }

    /**
     * Método que genera el documento PDF con el anexo, lo firma y lo sube a
     * EREG
     *
     * @param dto ExpedienteDTDo
     * @return String código del anexo
     * @throws ServiceException
     */
    private String generaFirmaReciboPresentacion(ExpedienteDTO dto, Integer idTramite,
            List<ExpedienteDocDTO> lstDoc) throws ServiceException {
        String cdEreg = dto.getCdEreg();
        if (idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS))) {
            cdEreg = dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                    .getCdEregPresent();
        }
        // Se genera el listado de documentos y se anexa
        byte[] listadoDoc = this.generarAnexoDocumentacion(dto, idTramite, lstDoc);
        listadoDoc = this.eregAdmeService.firmarDatos(listadoDoc);
        log.info("Se va a anexar el listado. Tamaño: " + listadoDoc.length);

        return this.eregAdmeService.anexarFicheroAnexo(cdEreg, listadoDoc,
                EregAdmeService.ConstantesEregTipoAnexos.LISTADO_DOC_ANEXOS);

    }

    /**
     * Método que genera el documento PDF con el anexo, lo firma y lo sube a
     * EREG
     *
     * @param dto ExpedienteDTDo
     * @return String código del anexo
     * @throws ServiceException
     */
    private String generaFirmaReciboPresentacionAleg(ExpedienteDTO dto, String asunto,
            Integer idTramite) throws ServiceException {

        // Se genera el listado de documentos y se anexa
        byte[] listadoDoc = this.generarAnexoDocumentacionAleg(dto, asunto, idTramite);
        listadoDoc = this.eregAdmeService.firmarDatos(listadoDoc);
        log.info("Se va a anexar el listado. Tamaño: " + listadoDoc.length);

        return this.eregAdmeService.anexarFicheroAnexo(dto.getCdEregAlegaciones(), listadoDoc,
                EregAdmeService.ConstantesEregTipoAnexos.LISTADO_DOC_ANEXOS);

    }

    /**
     * Método que rellena el zip con los archivos de la alegación. Se utiliza
     * para generar el zip de alegación
     *
     * @param integer
     *
     * @param dto ExpedienteDoc
     * @param zos zip
     * @throws ServiceException
     */
    private void empaquetarZipTramite(Integer idTramite, String cdEreg, ExpedienteDTO dto,
            ZipOutputStream zos, Integer idExpedienteDoc) throws ServiceException {

        try {
            String cdExpediente = dto.getCdExpediente().replace("/", "_");
            String cdEregSubsana = dto.getCdEreg(); // para alegaciones coge el
                                                    // del expediente
            // MD-2190 Quitamos la configuración del cdEregSubsana del trámite de alegación exclusión provisional
//            if (idTramite != null
//                    && idTramite.equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES_EXCLUSION_PROVISIONAL))) {
//                ExpedienteDTO expSolicitante = this.expedienteService
//                        .buscarId(dto.getIdExpedientePadre());
//                cdEregSubsana = expSolicitante.getCdEreg();
//            }

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            if (idTramite != null && idTramite
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA))) {
                List<ExpedienteJustecDocDTO> expJstecDocs = expedienteJusTecDocService
                        .buscarPorExpedienteDoc(idExpedienteDoc);
                if (expJstecDocs != null && !expJstecDocs.isEmpty()) {
                    ExpedienteJusTecDTO expedienteJustec = expedienteJusTecService
                            .buscarPorIdExpedienteJustec(
                                    expJstecDocs.get(0).getIdExpedienteJusTec());
                    cdEregSubsana = expedienteJustec.getCdEregSubsana();
                }
            } else if (idTramite != null && idTramite
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_CONTRATO_INICIAL))) {
                ExpedienteContratoDTO expedienteContratoDTO = this.expedienteContratoService
                        .obtenerExpedienteContratoByExpediente(dto);
                cdEregSubsana = expedienteContratoDTO.getCdEregSubsana();
            }else if(idTramite != null && (idTramite
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA))
                    ||idTramite
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO )))) {
                
                ExpedienteDocDTO expedienteDocDTO = this.expedienteDocService.buscarId(idExpedienteDoc);
                if(expedienteDocDTO != null) {
                    cdEregSubsana =expedienteDocDTO.getCodigoEreg();
                }
               
                
            }else if (idTramite != null && idTramite
                    .equals(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ALEGACIONES))) {
                cdEregSubsana =dto.getCdEregAlegaciones();
                
            }

            // Obtenemos los listados de recibos de presentacion
            List<byte[]> anexos = this.descargarListadoDocumentos(idTramite, dto, cdEregSubsana)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Insertamos los recibos al zip
            for (Integer i = 0; i < anexos.size(); i++) {
                ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                        + cdExpediente + " " + i + ".pdf");
                zos.putNextEntry(anexoZIP);
                zos.write(anexos.get(i));
            }

            // Obtenemos la listado de documentos anexados
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    dto.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREGPADRE, cdEreg,
                    Operador.EQUALS));

            List<ExpedienteDoc> listaExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

//            for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
//                Integer cdFica = expedienteDoc.getCdFica();
//
//                // Recuperamos de FICA el nombre del fichero
//                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
//                
//                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
//                String unicodeEscapedFilename = "";
//                if (ficaFichero != null) {
//                    String rawFilename = ficaFichero.getNombreFichero();
//                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
//                }
//
//                // Insertamos los documentos de FICA
//                ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + cdFica + "_"
//                        + unicodeEscapedFilename);
//                zos.putNextEntry(ficaZIP);
//                zos.write(this.ficaService.recuperar(cdFica).getDatosFichero());
//
//            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Método que rellena el zip con los archivos del expediente. Se utiliza
     * para generar el zip de solicitud y el de expediente
     *
     * @param dto Expediente
     * @param zos zip
     * @throws ServiceException
     */                          
    private void empaquetarZip(ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {

        try {

            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpediente(dto).getContenidoFichero();
           if(impreso != null) {
               log.debug("impreso solicitud: " + Base64.getEncoder().encodeToString(impreso)); 
           }
           
            String cdEregEXP = dto.getCdEreg();
            // Obtenemos los listados de anexos de cada expediente
            // VMM 30/09/24 MD-610: pasar aqui por param el tramite 400 en vez
            // de un null aunque
            // el filtro que hace dentro con el recibo de presentacion (171) se
            // supone que es de este tramite
            // revisar que esto no falle para otros casos que no sean de
            // presentacion de solicitud
            List<DocumentoDTO> listaDoc = this.descargarListadoDocumentos(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION), dto, cdEregEXP);
            List<byte[]> anexos = listaDoc.stream().map(DocumentoDTO::getContenidoFichero)
                    .collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpediente(dto, Boolean.TRUE, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION), null);

            String cdExpediente = dto.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);

            // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
            // que no de error hasta que arreglemos lo de EREG
            if (impreso != null) {
                zos.write(impreso);
            } else {
                log.error("No se ha encontrado el impreso relleno del expediente");
            }

            // VMM 25/09/24: ponemos un control aqui si los anexos son vacios
            // para que no de error hasta que arreglemos lo de EREG
            // Insertamos los anexos
            if (!anexos.isEmpty()) {
                for (Integer i = 0; i < anexos.size(); i++) {
                    ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                            + cdExpediente + " " + i + ".pdf");
                    zos.putNextEntry(anexoZIP);
                    zos.write(anexos.get(i));
                }
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
    }
    
    /**
     * Método que rellena el zip con los archivos del expediente marcados como descargable
     * Se utiliza para generar el zip de solicitud y el de expediente
     * 
     * @param dto
     * @param zos
     * @throws ServiceException
     */
    private void empaquetarZipEval(ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {
        try {

            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpediente(dto).getContenidoFichero();
            if(impreso != null) {
                log.debug("impreso solicitud: " + Base64.getEncoder().encodeToString(impreso)); 
            }
            

            // Obtenemos los documentos subidos a FICA de cada expediente
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpedienteEval(dto, Boolean.TRUE, utilConstantes
                            .getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION),
                            null);

            String cdExpediente = dto.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);

            // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
            // que no de error hasta que arreglemos lo de EREG
            if (impreso != null) {
                zos.write(impreso);
            } else {
                log.error("No se ha encontrado el impreso relleno del expediente");
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarInformeCoordinador(Integer idExpediente) throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipInformeCoorEval(dto, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(dto.getCdExpediente().replace("/", "_") + ".zip");

        } catch (IOException ioex) {
            log.error("Error al crear el zip informe coordinador, ", ioex);
            throw new ServiceException("Error al crear el zip informe coordinador.");
        }

        return documentoDTO;
    }

    /**
     * Método que rellena el zip con los archivos del informe de los
     * coordinadores. Se utiliza para generar el zip de informes
     *
     * @param dto Expediente
     * @param zos zip
     * @throws ServiceException
     */
    private void empaquetarZipInformeCoorEval(ExpedienteDTO dto, ZipOutputStream zos)
            throws ServiceException {
        try {
            // Obtenemos los documentos subidos a FICA de cada expediente
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpedienteEval(dto.getIdExpediente(), Boolean.TRUE);

            if (!lstDocumentosFICA.isEmpty()) {
                String cdExpediente = dto.getCdExpediente().replace("/", "_");

                // Creamos la carpeta para el expediente
                ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
                zos.putNextEntry(carpetaZIP);

                if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                    for (DocumentoDTO fica : lstDocumentosFICA) {
                        // Insertamos los documentos de FICA
                        ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/" + fica.getCdFica() + "_"
                                + fica.getNombreFichero());
                        zos.putNextEntry(ficaZIP);
                        zos.write(fica.getContenidoFichero());
                    }
                }
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     *
     */
    @Override
    public DocumentoDTO descargarInformeCoorEval(Integer idExpedienteEval, String itCoorEva)
            throws ServiceException {
        // Obtenemos el expediente

        ExpedienteEvalDTO dto = this.expedienteEvalService.buscarId(idExpedienteEval);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipCoorEval(dto, itCoorEva, zos);

            zos.closeEntry();
            zos.close();
        } catch (IOException ioex) {
            log.error("Error al crear el zip informe coordinador evaluador, ", ioex);
            throw new ServiceException("Error al crear el zip informe coordinador evaluador.");
        }

        DocumentoDTO documentoDTO = new DocumentoDTO();
        documentoDTO.setContenidoFichero(baos.toByteArray());
        documentoDTO.setNombreFichero(
                (dto.getPersonaDTO().getDsNombre() + " " + dto.getPersonaDTO().getDsApellido1()
                        + " " + dto.getPersonaDTO().getDsApellido2()).replace("/", "_") + ".zip");
        return documentoDTO;
    }

    /**
     * Método que rellena el zip con los archivos del informe de los
     * coordinadores. Se utiliza para generar el zip de informes
     *
     * @param dto Expediente
     * @param zos zip
     * @throws ServiceException
     */
    private void empaquetarZipCoorEval(ExpedienteEvalDTO dto, String itCoorEva, ZipOutputStream zos)
            throws ServiceException {

        // Obtenemos los documentos subidos a FICA de cada expediente
        List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                .obtenerDocExpedienteEval(dto.getIdExpedienteEval(), itCoorEva, Boolean.TRUE);

        String dsNombreCapeta = (dto.getPersonaDTO().getDsNombre() + " "
                + dto.getPersonaDTO().getDsApellido1() + " " + dto.getPersonaDTO().getDsApellido2())
                        .replace("/", "_");

        try {
            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(dsNombreCapeta + "/");
            zos.putNextEntry(carpetaZIP);

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(dsNombreCapeta + "/" + fica.getCdFica() + "_"
                            + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }
            }
        } catch (IOException e) {
            throw new ServiceException(e.getMessage());
        }
    }
    
    /**
    *
    */
   @Override
   public byte[] crearFormularioEvaluacion(Integer idConvocatoria, String itCoorEval)
           throws ServiceException {

       byte[] bytesSalida = null;

       try {

           if (Objects.isNull(idConvocatoria)) {
               throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
           }

           // Recueperamos la convocatoria
           ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);

           // Lista de evaluaciones

           List<EvaluacionDTO> listaEvaluacion = this.evaluacionService
                   .obtenerPorConvocatoria(idConvocatoria, itCoorEval);

           if (Objects.isNull(listaEvaluacion) || listaEvaluacion.isEmpty()) {
               throw new ServiceException(Constantes.Error.ERROR_INFORME_NO_EXISTE_EVALUACION,
                       null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
           }

           Document document = new Document(PageSize.A4, 36, 36, 102, 36);
           ByteArrayOutputStream baos = new ByteArrayOutputStream();
           this.generarCabeceraPdf(convocatoriaDTO, listaEvaluacion.get(0).getImagenLogoDTO(),
                   document, baos, Boolean.FALSE, null);

           document.open();

           informeHelperPdfService.agregarTitulo(document, Constantes.Evaluacion.TEXTOTITULO,
                   convocatoriaDTO.getCdTitulo(),"","", null, null);

           informeHelperPdfService.agregarFilaVacio(document, 1);

           informeHelperPdfService.agregarTituloCabeceraExpediente(document,
                   Constantes.Evaluacion.TEXTO_EXPEDIENTE, null);
           if (Constantes.TipoCoorEval.COORDINADOR.equals(itCoorEval)) {
               informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                       Constantes.Evaluacion.TEXTO_INVES_COORDINADOR, null);
           } else {
               informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                       Constantes.Evaluacion.TEXTO_INVES_EVALUADOR, null);
           }

           informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Evaluacion.TEXTO_FECHA,
                   new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

           informeHelperPdfService.agregarFilaVacio(document, 1);

           for (EvaluacionDTO evaluacionDTO : listaEvaluacion) {
               informeHelperPdfService.agregarTituloEvaluacion(document,
                       evaluacionDTO.getNmOrden() + ") " + evaluacionDTO.getDsTitulo());

               informeHelperPdfService.agregarDescripcion(document, evaluacionDTO.getTlDescripcion());

               informeHelperPdfService.agregarListadoEvaluacionesApart(document,
                       evaluacionDTO.getListaApartadoDTO().stream()
                               .sorted(Comparator.comparing(ApartadoDTO::getNmOrden))
                               .collect(Collectors.toList()),
                       evaluacionDTO.getItCalificacion());

               if (Constantes.AfirmacionNegacion.S.equals(evaluacionDTO.getItCalificacion())) {

                   //IntSummaryStatistics valor = evaluacionDTO.getListaEvaluacionNotaDTO().stream()
                   //        .collect(Collectors.summarizingInt(EvaluacionNotaDTO::getNmA));

                   informeHelperPdfService.agregarValoracionGlobal(document,
                           "PUNTUACIÓN TOTAL DEL PROYECTO");
                   informeHelperPdfService.agregarListadoEvaluacionesNota(document,
                           evaluacionDTO.getListaEvaluacionNotaDTO(), null);
               }
               informeHelperPdfService.agregarFilaVacio(document, 1);
           }

           if (Constantes.TipoCoorEval.COORDINADOR.equals(itCoorEval)) {
               informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                       Constantes.Evaluacion.TEXTO_COMENTARIO, null);
               informeHelperPdfService.celdaCampoMultiTextoVacio(document);

               // Nota de formulario evaluación del informe del coordinador
               String nota = "0";
               if (Objects.nonNull(convocatoriaDTO.getNmCalificacionMax())) {

                   nota = String.valueOf(convocatoriaDTO.getNmCalificacionMax());

               }
               informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                       Constantes.Evaluacion.TEXTO_NOTA + "(0 - " + nota + ")", null);
               informeHelperPdfService.celdaCampoTextoVacio(document);
           }
           document.close();

           bytesSalida = baos.toByteArray();

       } catch (DocumentException e) {
           log.error(e.getMessage());

       }

       return bytesSalida;
   }

    /**
     *
     */
    @Override
    public byte[] crearFormularioEvaluacion(Integer idConvocatoria, Integer idTipoInforme)
            throws ServiceException {

        byte[] bytesSalida = null;
        List<EvaluacionDTO> listaEvaluacion=null;
        try {

            if (Objects.isNull(idConvocatoria)) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }

            // Recueperamos la convocatoria
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);

            // Lista de evaluaciones
            List<InformeDTO> listInformes= this.informeService.findByIdConvocatoriaIdTipoInforme(idConvocatoria, idTipoInforme);
            
            if (listInformes != null && !listInformes.isEmpty()) {
                listaEvaluacion = this.evaluacionService.obtenerPorInforme(listInformes.get(0));
            }
            

            if (Objects.isNull(listaEvaluacion) || listaEvaluacion.isEmpty()) {
                throw new ServiceException(Constantes.Error.ERROR_INFORME_NO_EXISTE_EVALUACION,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.generarCabeceraPdf(convocatoriaDTO, listInformes.get(0).getImagenLogoDTO(),
                    document, baos, Boolean.FALSE, null);

            document.open();
            
            informeHelperPdfService.agregarFilaVacio(document, 1);
            
            CatalogosValoresDTO tipoInformeDTO = this.catalogosValoresService.buscarId(idTipoInforme);

            informeHelperPdfService.agregarTitulo(document, tipoInformeDTO.getDsValor(),
                    convocatoriaDTO.getCdTitulo(), "",
                    Constantes.Evaluacion.TEXTO_SIN_CD_EXPEDIENTE, null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

//            informeHelperPdfService.agregarTituloCabeceraExpediente(document,
//                    Constantes.Evaluacion.TEXTO_EXPEDIENTE, null);

            informeHelperPdfService.agregarTituloInformeEvaluacion(document,
                    listaEvaluacion.get(0).getInformeDTO().getDsTitulo());

//            informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Evaluacion.TEXTO_FECHA,
//                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
            
            informeHelperPdfService.agregarTituloDatosProyecto(document);
            
            informeHelperPdfService.agregarDatosProyecto(document, null, null, null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            for (EvaluacionDTO evaluacionDTO : listaEvaluacion) {
                informeHelperPdfService.agregarTituloEvaluacion(document,
                        evaluacionDTO.getNmOrden() + ") " + evaluacionDTO.getDsTitulo());
                
                informeHelperPdfService.agregarFilaVacio(document, 1);

                if (evaluacionDTO.getTlDescripcion() != null
                        && !"".equals(evaluacionDTO.getTlDescripcion())) {

                    informeHelperPdfService.agregarDescripcion(document,
                            evaluacionDTO.getTlDescripcion());

                    informeHelperPdfService.agregarFilaVacio(document, 1);

                }

                informeHelperPdfService.agregarListadoEvaluacionesApart(document,
                        evaluacionDTO.getListaApartadoDTO().stream()
                                .sorted(Comparator.comparing(ApartadoDTO::getNmOrden))
                                .collect(Collectors.toList()),
                        evaluacionDTO.getItCalificacion());

                if (Constantes.AfirmacionNegacion.S.equals(evaluacionDTO.getItCalificacion())) {

                    IntSummaryStatistics valor = evaluacionDTO.getListaEvaluacionNotaDTO().stream()
                            .collect(Collectors.summarizingInt(EvaluacionNotaDTO::getNmA));

                    informeHelperPdfService.agregarValoracionGlobal(document,
                            "PUNTUACIÓN TOTAL DEL PROYECTO");
                    informeHelperPdfService.agregarListadoEvaluacionesNota(document,
                            evaluacionDTO.getListaEvaluacionNotaDTO(), null);
                }
                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error(e.getMessage());

        }

        return bytesSalida;
    }

    /**
     *
     * @param listaEvaluacion
     * @param document
     * @param expedienteEvalDTO
     * @throws ServiceException
     */
    private void crearFormularioEvaluacionDatosIdExpEvalEvaluaciones(
            List<EvaluacionDTO> listaEvaluacion, Document document,
            ExpedienteEvalDTO expedienteEvalDTO) throws ServiceException {

        try {

            for (EvaluacionDTO evaluacionDTO : listaEvaluacion) {

                informeHelperPdfService.agregarTituloEvaluacion(document,
                        evaluacionDTO.getNmOrden() + " ) " + evaluacionDTO.getDsTitulo());

                informeHelperPdfService.agregarDescripcion(document, evaluacionDTO.getTlDescripcion());

                informeHelperPdfService.agregarListadoEvaluacionesApartNotas(document,
                        this.expedienteEvalService.obtenerApartadosDatos(
                                expedienteEvalDTO.getIdExpedienteEval(),
                                evaluacionDTO.getIdEvaluacion()),
                        evaluacionDTO.getItCalificacion());

                if (StringUtils.isNotBlank(evaluacionDTO.getItCalificacion())) {

                    Integer nota = null;

                    if (Constantes.AfirmacionNegacion.S.equals(evaluacionDTO.getItCalificacion())) {

                        informeHelperPdfService.agregarValoracionGlobal(document,
                                "PUNTUACIÓN TOTAL DEL PROYECTO");

                        nota = expedienteEvalDTO.getNmNotaFinal();
                        informeHelperPdfService.agregarListadoEvaluacionesNota(document,
                                evaluacionDTO.getListaEvaluacionNotaDTO().stream()
                                        .filter(f -> f.getFcBajaFila() == null)
                                        .collect(Collectors.toList()),
                                nota);

                    } else if (Constantes.AfirmacionNegacion.N
                            .equals(evaluacionDTO.getItCalificacion())) {
/*					
//                        informeHelperPdfService.agregarValoracionGlobal(document,
//                                "VALORACIÓN GLOBAL DEL PROYECTO (suma de notas de los apartados)");
//
//                        List<EvaluacionNotaDatoDTO> lstEvaNotaDato = this.evaluacionNotaDatoService
//                                .obtenerEvaluacionNotaDatoPorIdEvalIdExpEval(
//                                        expedienteEvalDTO.getIdExpedienteEval(),
//                                        evaluacionDTO.getIdEvaluacion());
//
//                        if (!lstEvaNotaDato.isEmpty()) {
//                            nota = lstEvaNotaDato.get(0).getNmNota();
//                        }
*/
                    }

                informeHelperPdfService.agregarFilaVacio(document, 1);
                
                }
            }
            
            // agregar anexos.
           /* List<ExpedienteDocEvalDTO> listExpedienteDocEvalDTO = this.expedienteDocEvalService.obtenerDocumentoExpedienteEval(expedienteEvalDTO);
            if(listExpedienteDocEvalDTO != null && !listExpedienteDocEvalDTO.isEmpty()) {
                listExpedienteDocEvalDTO = listExpedienteDocEvalDTO.stream().filter(doc -> doc.getFcBajaFila()== null ).collect(Collectors.toList());
                
                if(!listExpedienteDocEvalDTO.isEmpty()) {
                    // añadimos los doc en el informe tipodoc y chec seleccionado
                    informeHelperPdfService.agregarDocumentosInforme(document,listExpedienteDocEvalDTO);
                    
                    informeHelperPdfService.agregarFilaVacio(document, 1);
                }
                
            }*/
            
        } catch (DocumentException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    /**
     *
     * @param document
     * @param convocatoriaDTO
     * @param expedienteEvalDTO
     * @throws ServiceException
     */
    private void crearFormularioEvaluacionDatosIdExpEvalCoordinador(Document document,
            ConvocatoriaDTO convocatoriaDTO, ExpedienteEvalDTO expedienteEvalDTO)
            throws ServiceException {

        try {
            if (Constantes.TipoCoorEval.COORDINADOR.equals(expedienteEvalDTO.getItCoorEval())) {

                informeHelperPdfService.agregarTituloExpedienteEval(document,
                        Constantes.Evaluacion.TEXTO_COMENTARIO);
                informeHelperPdfService.agregarFilaVacio(document, 1);
                informeHelperPdfService.celdaCampoMultiTextoExpEval(document,
                        expedienteEvalDTO.getDsComentario());

                // Nota de formulario evaluación del informe del coordinador
                String etiquetaNotaFinalMaxima = "0";
                if (Objects.nonNull(convocatoriaDTO.getNmCalificacionMax())) {
                    etiquetaNotaFinalMaxima = String
                            .valueOf(convocatoriaDTO.getNmCalificacionMax());
                }

                informeHelperPdfService.agregarFilaVacio(document, 1);
                informeHelperPdfService.agregarTituloExpedienteEval(document,
                        Constantes.Evaluacion.TEXTO_NOTA + "(0 - " + etiquetaNotaFinalMaxima + ")");
                informeHelperPdfService.agregarFilaVacio(document, 1);
                informeHelperPdfService.celdaCampoTextoExpEval(document,
                        expedienteEvalDTO.getNmNotaFinal().toString());
                
                // agregar anexos.
               /* List<ExpedienteDocEvalDTO> listExpedienteDocEvalDTO = this.expedienteDocEvalService.obtenerDocumentoExpedienteEval(expedienteEvalDTO);
                if(listExpedienteDocEvalDTO != null && !listExpedienteDocEvalDTO.isEmpty()) {
                    listExpedienteDocEvalDTO = listExpedienteDocEvalDTO.stream().filter(doc -> doc.getFcBajaFila()== null ).collect(Collectors.toList());
                    
                    if(!listExpedienteDocEvalDTO.isEmpty()) {
                        // añadimos los doc en el informe tipodoc y chec seleccionado
                        informeHelperPdfService.agregarDocumentosInforme(document,listExpedienteDocEvalDTO);
                        
                        informeHelperPdfService.agregarFilaVacio(document, 1);
                    }
                    
                }*/
            }

        } catch (DocumentException e) {
            throw new ServiceException(e.getMessage());
        }

    }
    
    private Document crearInformeEval( Document document, ConvocatoriaDTO convocatoriaDTO, ExpedienteEvalDTO expedienteEvalDTO,  List<InformeEvaluacionDTO> lstInfoEvaluacion,
            List<EvaluacionDTO> listaEvaluacion, Boolean docFirmado) throws DocumentException, ServiceException {
        
        informeHelperPdfService.agregarFilaVacio(document, 1);
        
        CatalogosValoresDTO tipoInformeDTO = this.catalogosValoresService.buscarId(expedienteEvalDTO.getInformeDTO().getTipoInformeDTO().getIdValor());

        informeHelperPdfService.agregarTitulo(document, tipoInformeDTO.getDsValor(),
                convocatoriaDTO.getCdTitulo(), "",
                Constantes.Evaluacion.TEXTO_SIN_CD_EXPEDIENTE, null, null);
		
		informeHelperPdfService.agregarFilaVacio(document, 1);
		
		informeHelperPdfService.agregarTituloInformeEvaluacion(document,
		        listaEvaluacion.get(0).getInformeDTO().getDsTitulo());
        
		informeHelperPdfService.agregarFilaVacio(document, 1);

        informeHelperPdfService.agregarTituloDatosProyecto(document);
        
        String datosCoordinador = StringUtils.EMPTY;
        String datosEvaluador = StringUtils.EMPTY;
        //if (Boolean.TRUE.equals(docFirmado)) {
            ExpedienteEvalDTO expedienteEvalActual = this.expedienteEvalService
                    .buscarId(expedienteEvalDTO.getIdExpedienteEval());

            if (Constantes.TipoCoorEval.COORDINADOR.equals(expedienteEvalDTO.getItCoorEval())) {
                datosCoordinador = this.obtenerNombreCompleto(expedienteEvalActual.getPersonaDTO());
            } else {
                datosEvaluador = this.obtenerNombreCompleto(expedienteEvalActual.getPersonaDTO());
                if (Objects.nonNull(expedienteEvalActual.getIdExpedienteEvalPadre())) {
                    ExpedienteEvalDTO expedienteEvalCoordinador = this.expedienteEvalService
                            .buscarId(expedienteEvalActual.getIdExpedienteEvalPadre());

                    if (Objects.nonNull(expedienteEvalCoordinador)) {
                        datosCoordinador = this
                                .obtenerNombreCompleto(expedienteEvalCoordinador.getPersonaDTO());
                    }
                }
            
            }
       // }
       
        String datosSolicitante="";
        InteresadoDTO solictante = lstInfoEvaluacion.get(0).getSolictante();
        
        if(solictante!= null) {
            datosSolicitante =solictante.getCdNumIden()+" "+ solictante.getDsNombre() + " "
                    + (Objects.isNull( solictante.getDsApellido1())? "": solictante.getDsApellido1())+ " "
                    + (Objects.isNull(solictante.getDsApellido2())
                            ? ""
                            : solictante.getDsApellido2());
        }
        
        
        informeHelperPdfService.agregarDatosProyecto(document, lstInfoEvaluacion.get(0).getExpCdExpediente(), datosSolicitante, datosEvaluador, datosCoordinador);

        //informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Evaluacion.TEXTO_FECHA,new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

        informeHelperPdfService.agregarFilaVacio(document, 1);
        
        
        return document;
    }

    /**
     * Obtiene el nombre completo de la persona asignada a la evaluación.
     *
     * @param interesado datos de la persona
     * @return nombre completo
     */
    private String obtenerNombreCompleto(InteresadoDTO interesado) {
        if (Objects.isNull(interesado)) {
            return StringUtils.EMPTY;
        }

        StringBuilder nombreCompleto = new StringBuilder();

        if (StringUtils.isNotBlank(interesado.getDsNombre())) {
            nombreCompleto.append(interesado.getDsNombre());
        }

        if (StringUtils.isNotBlank(interesado.getDsApellido1())) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(interesado.getDsApellido1());
        }

        if (StringUtils.isNotBlank(interesado.getDsApellido2())) {
            if (nombreCompleto.length() > 0) {
                nombreCompleto.append(" ");
            }
            nombreCompleto.append(interesado.getDsApellido2());
        }

        return nombreCompleto.toString();
    }
    
    /**
     *
     */
    @Override
    public Document crearFormularioEvaluacionDatosIdExpEval(ByteArrayOutputStream baos,Document document,ExpedienteEvalDTO expedienteEvalDTO,Boolean docFirmado)
            throws ServiceException {

       if (Objects.isNull(expedienteEvalDTO.getIdExpedienteEval())
                || Objects.isNull(expedienteEvalDTO.getInformeDTO())
                || Objects.isNull(expedienteEvalDTO.getInformeDTO().getIdInforme())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        try {
            
            // Lista de informe de evaluaciones
            List<InformeEvaluacionDTO> lstInfoEvaluacion = this.expedienteEvalService
                    .obtenerInformeEvaluacionByInforme(expedienteEvalDTO.getIdExpedienteEval(),
                            expedienteEvalDTO.getInformeDTO());

            if (lstInfoEvaluacion.isEmpty()) {
                throw new ServiceException(Constantes.Error.ERROR_INFORME_NO_EXISTE_EVALUACION,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            // Recueperamos la convocatoria
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                    .buscarId(expedienteEvalDTO.getInformeDTO().getIdConvocatoria());

            // Lista de evaluaciones
            List<EvaluacionDTO> listaEvaluacion = this.evaluacionService.obtenerPorInforme(expedienteEvalDTO.getInformeDTO());
            
            
            this.generarCabeceraPdf(convocatoriaDTO, expedienteEvalDTO.getInformeDTO().getImagenLogoDTO(),
                    document, baos, Boolean.FALSE, null);
            
            document.open();

           this.crearInformeEval( document,  convocatoriaDTO,  expedienteEvalDTO,  lstInfoEvaluacion,
                   listaEvaluacion, docFirmado) ;

            this.crearFormularioEvaluacionDatosIdExpEvalEvaluaciones(listaEvaluacion, document,
                   expedienteEvalDTO);

            if (Constantes.TipoCoorEval.COORDINADOR
                    .equals(lstInfoEvaluacion.get(0).getExpEvaItCoorEval())) {
                this.crearFormularioEvaluacionDatosIdExpEvalCoordinador(document, convocatoriaDTO,
                        expedienteEvalDTO);
                
                
            }
            
     
           
        } catch (DocumentException | ServiceException e) {
            log.error("Error al generar PDF", e);  // log completo con stack trace
            
            return null;
        }
       
        return document;
    }

    /**
     *
     */
    @Override
    public byte[] crearFormularioAlegacion(Integer idConvocatoria) throws ServiceException {

        byte[] bytesSalida = null;

        try {

            if (Objects.isNull(idConvocatoria)) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }

            // Recueperamos la convocatoria
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);

            // Lista de evaluaciones

            List<AlegacionDTO> listaAlegacion = this.alegacionService
                    .obtenerPorConvocatoria(idConvocatoria);

            if (Objects.isNull(listaAlegacion) || listaAlegacion.isEmpty()) {
                throw new ServiceException(
                        Constantes.Error.ERROR_INFORME_ALEGACIONES_NO_EXISTE_DATO, null,
                        Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.generarCabeceraPdf(convocatoriaDTO, listaAlegacion.get(0).getImagenLogoDTO(),
                    document, baos, Boolean.FALSE, null);

            document.open();

            informeHelperPdfService.agregarTitulo(document, Constantes.Alegacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),"","", null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarTituloCabeceraExpediente(document,
                    Constantes.Alegacion.TEXTO_EXPEDIENTE, null);
            informeHelperPdfService.agregarTituloCabeceraInvestigador(document,
                    Constantes.Alegacion.TEXTO_INVES, null);

            informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Alegacion.TEXTO_FECHA,
                    LocalDate.now().toString());

            informeHelperPdfService.agregarFilaVacio(document, 1);

            for (AlegacionDTO alegacionDTO : listaAlegacion) {
                informeHelperPdfService.agregarTituloAlegacion(document,
                        alegacionDTO.getNmOrden() + ") " + alegacionDTO.getDsTitulo(), 11);
                informeHelperPdfService.agregarTipo(document, alegacionDTO.getTipoDTO(), null);

                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error(e.getMessage());

        }

        return bytesSalida;
    }

    /**
     *
     */
    @Override
    public byte[] crearFormularioAlegacionDato(Integer idExpediente, String cdNumIden)
            throws ServiceException {

        byte[] bytesSalida = null;

        try {

            if (Objects.isNull(idExpediente)) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }

            // recuperamos datos del expediente
            ExpedienteDTO expedienteDTO = this.expedienteService.buscarId(idExpediente);

            // recuperamos datos de la convocatoria
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                    .buscarId(expedienteDTO.getConvocatoriaActDTO().getIdConvocatoria());

            Interesado evaluador = this.obtenerEvaluador(cdNumIden);

            // Lista de alegaciones dato
            List<AlegacionDatoDTO> listaAlegacionDatoDTO = this.alegacionDatoService
                    .obtenerPorExpediente(idExpediente);

            AlegacionDTO alegacionDTO = this.alegacionService
                    .buscarId(listaAlegacionDatoDTO.get(0).getAlegacionDTO().getIdAlegacion());

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            this.generarCabeceraPdf(convocatoriaDTO, alegacionDTO.getImagenLogoDTO(), document,
                    baos, Boolean.FALSE, null);

            document.open();
            
            String desImpreso="";
            
            if(expedienteDTO.getImpresoDTO() != null &&expedienteDTO.getImpresoDTO().getDsDescripcion()!= null ) {
                desImpreso=expedienteDTO.getImpresoDTO().getDsDescripcion();
            }

            informeHelperPdfService.agregarTitulo(document, Constantes.Alegacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),desImpreso,expedienteDTO.getCdExpediente(), null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarTituloCabeceraExpediente(document,
                    Constantes.Alegacion.TEXTO_EXPEDIENTE, expedienteDTO.getCdExpediente());
            String investigador = expedienteDTO.getInteresadoDTO().getCdNumIden() + " - "
                    + expedienteDTO.getInteresadoDTO().getDsNombre() + " "
                    + expedienteDTO.getInteresadoDTO().getDsApellido1() + " "
                    + expedienteDTO.getInteresadoDTO().getDsApellido2();

            String eval = evaluador.getCdNumIden() + " - " + evaluador.getDsNombre() + " "
                    + evaluador.getDsApellido1() + " " + evaluador.getDsApellido2();
            informeHelperPdfService.agregarTituloCabeceraInvestigador(document,
                    Constantes.Alegacion.TEXTO_INVES, investigador);
            informeHelperPdfService.agregarTituloCabeceraInvestigador(document,
                    Constantes.Alegacion.TEXTO_EVAL, eval);

            informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Alegacion.TEXTO_FECHA,
                    LocalDate.now().toString());

            informeHelperPdfService.agregarFilaVacio(document, 1);

            for (AlegacionDatoDTO dto : listaAlegacionDatoDTO) {
                informeHelperPdfService.agregarTituloAlegacion(document, dto.getAlegacionDTO().getNmOrden()
                        + ") " + dto.getAlegacionDTO().getDsTitulo(), 11);
                informeHelperPdfService.agregarTipo(document, dto.getAlegacionDTO().getTipoDTO(),
                        dto.getDsValor());

                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error(e.getMessage());

        }

        return bytesSalida;
    }

    /**
     *
     */
    @Override
    public byte[] crearFormularioPlantilla(ConvocatoriaPlaDTO convocatoriaPlaDTO)
            throws ServiceException {
        byte[] bytesSalida = null;

        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(convocatoriaPlaDTO.getIdConvocatoria());

        Tramite tramite = tramiteService
                .getTramite(convocatoriaPlaDTO.getTramiteDTO().getIdValor());

        if (Boolean.TRUE.equals(this.esPorNote(convocatoriaPlaDTO))
                && Objects.isNull(convocatoriaPlaDTO.getImagenDTO().getIdImagen())) {
            throw new ServiceException(Constantes.Error.ERROR_PLANTILLA_NO_CONFIGURADA);
        }

        tramite.setPdfService(this);
        tramite.setCatalogosValoresService(this.catalogosValoresService);
        tramite.setExpedienteService(this.expedienteService);
        
        //VMM 03/06/25 MD-1624: consultamos los documentos a subsanar si es la plantilla de 
        //requerimiento de subsanacion admin
        if (convocatoriaPlaDTO.getTramiteDTO().getNmConstante().equals(
                Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA)) {
            
            List<ConvocatoriaPlaDocDTO> lstConvocatoriaPlaDoc = this.convocatoriaPlaDocService.
                    consultarDocuSubsanaPlantilla(convocatoriaPlaDTO.getIdConvocatoriaPla());
            convocatoriaPlaDTO.setLstConvocatoriaPlaDocDTO(lstConvocatoriaPlaDoc);
        }

        // Obtenemos los datos de la consejeria
        String strConsejeria = this.unidadAdminService.obtenerConsejeria();

        bytesSalida = tramite.formularioPdf(convocatoriaDTO, convocatoriaPlaDTO, null,
                strConsejeria);

        return bytesSalida;
    }

    /**
     *
     */
    @Override
    public Boolean esPorNote(ConvocatoriaPlaDTO convocatoriaPlaDTO) {
        Boolean porNote = Boolean.FALSE;
        List<AtlasFilter> filtros = new ArrayList<>();
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_IDCONVOCATORIA,
                convocatoriaPlaDTO.getIdConvocatoria(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_TRAMITE,
                convocatoriaPlaDTO.getTramiteDTO().getIdValor(), Operador.EQUALS));
        filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));

        List<ConvocatoriaCom> lstConvocatoriaCom = new ArrayList<>(
                this.convocatoriaComDao.find(filtros, null));
        if (!lstConvocatoriaCom.isEmpty()) {
            ConvocatoriaCom convocatoriaCom = lstConvocatoriaCom.get(0);
            porNote = convocatoriaCom.getItNote().equals("S");
        }

        return porNote;
    }

    /**
     *
     */
    @Override
    public void generarCabeceraPdfTramite(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, PdfWriter writer, Boolean esRegistroEreg)
            throws ServiceException {

        Map<String, byte[]> lstLogo = new HashMap<>();

        if (Boolean.TRUE.equals(this.esPorNote(convocatoriaPlaDTO))
                && Objects.isNull(convocatoriaPlaDTO.getImagenDTO().getIdImagen())) {
            throw new ServiceException(Constantes.Error.ERROR_PLANTILLA_NO_CONFIGURADA);
        }

        Boolean esFondoEuropeo = Constantes.AfirmacionNegacion.S
                .equals(convocatoriaDTO.getItFondosEuropa());

//        if (Boolean.TRUE.equals(esFondoEuropeo)) {
//            this.generarCabeceraPdfLogo(convocatoriaDTO, lstLogo);
//        }

        // Obtenemos los datos de la consejeria
        String strConsejeria = this.unidadAdminService.obtenerConsejeria();

        String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();

        byte[] logoOrganismo = null;
//        if (Objects.nonNull(convocatoriaPlaDTO.getImagenDTO().getCdFica())) {
//            FicaFichero ficheroRecuperar = null;
//            try {
//                ficheroRecuperar = this.ficaService
//                        .recuperar(Integer.valueOf(convocatoriaPlaDTO.getImagenDTO().getCdFica()));
//                logoOrganismo = ficheroRecuperar.getDatosFichero();
//            } catch (Exception e) {
//                log.error(e.getMessage());
//                ficheroRecuperar = null;
//            }
//
//        }

        writer.createXmpMetadata();
        writer.setTagged();

        writer.setPageEvent(new HeaderFooterPdfEvent(strConsejeria, strDireccion, logoOrganismo,
                lstLogo, esFondoEuropeo, esRegistroEreg));

    }

    /**
     *
     * @param convocatoriaDTO
     * @param lstLogo
     * @throws ServiceException
     */
    private void generarCabeceraPdfLogo(ConvocatoriaDTO convocatoriaDTO,
            Map<String, byte[]> lstLogo) throws ServiceException {

        try {

            List<ImagenDTO> lstLogosEuropeos = this.imagenService
                    .buscarLogosEuropeos(convocatoriaDTO.getIdConvocatoria());

            if (Objects.nonNull(lstLogosEuropeos) && !lstLogosEuropeos.isEmpty()) {
                for (ImagenDTO imagenDTO : lstLogosEuropeos) {
                    if (StringUtils.isNotBlank(imagenDTO.getCdFica())) {
                        FicaFichero ficheroRecuperar = this.ficaService
                                .recuperar(Integer.valueOf(imagenDTO.getCdFica()));
                        if (Objects.nonNull(ficheroRecuperar)) {
                            lstLogo.put("Logo" + imagenDTO.getIdImagen(),
                                    ficheroRecuperar.getDatosFichero());
                        }
                    }
                }
            }

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }

    }

    /**
     * Metodo para generar la sección de cabecera de un PDF genérico
     *
     * @param convocatoriaDTO
     * @param imagenDTO
     * @param document
     * @param baos
     * @throws ServiceException
     */
    private void generarCabeceraPdf(ConvocatoriaDTO convocatoriaDTO, ImagenDTO imagenOrganismoDTO,
            Document document, ByteArrayOutputStream baos, Boolean mostrarRegistroEntrada,
            Map<String, String> mapaDatosEntrada) throws ServiceException {

        try {

            Map<String, byte[]> lstLogo = new HashMap<>();

            Boolean esFondoEuropeo = Constantes.AfirmacionNegacion.S
                    .equals(convocatoriaDTO.getItFondosEuropa());

//            if (Boolean.TRUE.equals(esFondoEuropeo)) {
//                this.generarCabeceraPdfLogo(convocatoriaDTO, lstLogo);
//            }

            // Obtenemos los datos de la consejeria
            String strConsejeria = this.unidadAdminService.obtenerConsejeria();

            String strDireccion = convocatoriaDTO.getIdDireccion().getDsUnidad();

            byte[] logoOrganismo = null;
//            if (Objects.nonNull(imagenOrganismoDTO)
//                    && Objects.nonNull(imagenOrganismoDTO.getCdFica())) {
//                FicaFichero ficheroRecuperar = this.ficaService
//                        .recuperar(Integer.valueOf(imagenOrganismoDTO.getCdFica()));
//                if (Objects.nonNull(ficheroRecuperar)) {
//                    logoOrganismo = ficheroRecuperar.getDatosFichero();
//                }
//            }

            PdfWriter writer = PdfWriter.getInstance(document, baos);
            writer.createXmpMetadata();
            writer.setTagged();

            writer.setPageEvent(new HeaderFooterPdfEvent(strConsejeria, strDireccion, logoOrganismo,
                    lstLogo, esFondoEuropeo, mostrarRegistroEntrada, mapaDatosEntrada));

        } catch (Exception e) {
            log.error("Error al crear el anexo de documentación en generarCabeceraPdf: " + e.getMessage());
            //throw new ServiceException(e.getMessage());
        }
    }

    /**
     *
     * @param cdNumIden
     * @return
     * @throws ServiceException
     */
    private Interesado obtenerEvaluador(String cdNumIden) throws ServiceException {
        List<AtlasFilter> filter = new ArrayList<>();
        filter.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDNUMIDEN, cdNumIden, Operador.EQUALS));

        List<Interesado> listaInteresados = this.interesadoService.find(filter, null);

        Interesado evaluador = new Interesado();

        if (Boolean.FALSE.equals(listaInteresados.isEmpty())) {

            evaluador = listaInteresados.get(0);

        }

        return evaluador;
    }

    /**
    *
    */
    @Override
    public byte[] crearFormularioEvaluacionJustec(Integer idConvocatoria, String itTipoEval)
            throws ServiceException {

        byte[] bytesSalida = null;

        try {

            if (Objects.isNull(idConvocatoria)) {
                throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
            }

            // Recueperamos la convocatoria
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService.buscarId(idConvocatoria);

            // Lista de evaluaciones

            List<EvaluacionJustecDTO> listaEvaluacion = this.evaluacionJustecService
                    .obtenerPorConvocatoria(idConvocatoria, itTipoEval);

            if (Objects.isNull(listaEvaluacion) || listaEvaluacion.isEmpty()) {
                throw new ServiceException(Constantes.Error.ERROR_INFORME_NO_EXISTE_EVALUACION,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.generarCabeceraPdf(convocatoriaDTO, listaEvaluacion.get(0).getImagenLogoDTO(),
                    document, baos, Boolean.FALSE, null);

            document.open();

            informeHelperPdfService.agregarTitulo(document, Constantes.Evaluacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),"","", null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarTituloCabeceraExpediente(document,
                    Constantes.Evaluacion.TEXTO_EXPEDIENTE, null);
            if (Constantes.TipoEvalJustec.INTERMEDIA.equals(itTipoEval)) {
                informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                        Constantes.Evaluacion.TEXTO_INVES_JUSTEC_INTERMEDIO, null);
            } else {
                informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                        Constantes.Evaluacion.TEXTO_INVES_JUSTEC_FINAL, null);
            }

            informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Evaluacion.TEXTO_FECHA,
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            informeHelperPdfService.agregarFilaVacio(document, 1);

            for (EvaluacionJustecDTO evaluacionDTO : listaEvaluacion) {
                informeHelperPdfService.agregarTituloEvaluacion(document,
                        evaluacionDTO.getNmOrden() + ") " + evaluacionDTO.getDsTitulo());

                informeHelperPdfService.agregarDescripcion(document, evaluacionDTO.getTlDescripcion());

                informeHelperPdfService.agregarListadoEvaluacionesApartJustec(document,
                        evaluacionDTO.getListaEvaluacionJustecApartaDTO().stream()
                                .sorted(Comparator.comparing(EvaluacionJustecApartaDTO::getNmOrden))
                                .collect(Collectors.toList()),
                        evaluacionDTO.getItCalificacion());

                if (Constantes.AfirmacionNegacion.S.equals(evaluacionDTO.getItCalificacion())) {

                    IntSummaryStatistics valor = evaluacionDTO.getListaEvaluacionJustecNotaDTO()
                            .stream()
                            .collect(Collectors.summarizingInt(EvaluacionJustecNotaDTO::getNmA));

                    informeHelperPdfService.agregarValoracionGlobal(document,
                            "VALORACIÓN GLOBAL DEL PROYECTO (indicar puntuación de 0 a "
                                    + valor.getMax() + " )");
                    informeHelperPdfService.agregarListadoEvaluacionesJustecNota(document,
                            evaluacionDTO.getListaEvaluacionJustecNotaDTO(), null);
                }
                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

            document.close();

            bytesSalida = baos.toByteArray();

        } catch (DocumentException e) {
            log.error(e.getMessage());

        }

        return bytesSalida;
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarExpedienteJustec(ExpedienteDTO expedienteDTO,
            ExpedienteJusTecDTO expedienteJustec) throws ServiceException {
        byte[] expedientePdf = null;
        ImpresoDTO impresoFormulario = null;
        List<AtlasFilter> filtros = new ArrayList<>();

        // VMM 25/10/24 MD-913: descargamos con el nuevo EREG de la JCT
        // Si hay código de EREG lo descargamos de EREG
        if (StringUtils.isNotBlank(expedienteJustec.getCdEregPresent())) {

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    expedienteDTO.getIdExpediente(), Operador.EQUALS));

            if (expedienteJustec.getTipoExpedienteJusTecDTO().getDsValor()
                    .equalsIgnoreCase("Intermedia")) {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_JUSTEC_INTERMEDIA_FIRMADO),
                        Operador.EQUALS));
            } else {
                filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_JUSTEC_FINAL_FIRMADO), Operador.EQUALS));

            }

            // Obtenemos el código de ereg del impreso
            List<ExpedienteDoc> listaExpedientesDoc = this.expedienteDocDAO.find(filtros, null);

            if (!listaExpedientesDoc.isEmpty()) {
                // Recuperamos el pdf
                expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(
                        expedienteJustec.getCdEregPresent(),
                        listaExpedientesDoc.get(0).getCdAnexoEreg());
            }

        } else {
            
            List<ExpedienteDocDTO> listaExpedienteDocDTO = this.expedienteService
                    .obtenerDocJusTec(expedienteJustec.getIdExpedienteJusTec()).stream()
                    .filter(x -> x.getFcBajaFila() == null )
                    .collect(Collectors.toList());

            if (expedienteJustec.getTipoExpedienteJusTecDTO().getDsValor()
                    .equalsIgnoreCase("Intermedia")) {
                impresoFormulario = this.getImpresoJustec(expedienteDTO.getImpresoJustecDTO(),
                        expedienteJustec.getIdExpedienteJusTec());
            } else {
                impresoFormulario = this.getImpresoJustec(expedienteDTO.getImpresoJustecFinalDTO(),
                        expedienteJustec.getIdExpedienteJusTec());
            }
            // Recuperamos los valores a insertar en el PDF
            HashMap<String, String> hamCampos = this.pdfCamposService
                    .rellenarCamposJustec(expedienteDTO, impresoFormulario,listaExpedienteDocDTO);

            // Construimos la plantilla PDF
            byte[] plantillaPdf = this.obtenerPlantillaJustec(expedienteDTO, hamCampos,
                    impresoFormulario);

            // VMM MD-913 28/10/24: llamamos al nuevo metodo para rellenarPDfJCT
            // con el EREG de la JCT
            // Inyectamos los valores a la plantilla PDF
            expedientePdf = this.rellenarPdfJCT(hamCampos, plantillaPdf, Boolean.FALSE,
                    expedienteDTO, expedienteJustec);
        }

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(
                    expedienteDTO.getCdExpediente() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    private ImpresoDTO getImpresoJustec(ImpresoDTO impresoJustecDTO, Integer idExpedienteJusTec) {
        // filtramos para quitar los registros de datos que no pertenecen al
        // periodo idExpedienteJusTec
        if (impresoJustecDTO != null && !impresoJustecDTO.getLstBloqueDTO().isEmpty()) {
            for (BloqueDTO bloque : impresoJustecDTO.getLstBloqueDTO()) {
                for (BloqueComponenteDTO bloqueComp : bloque.getLstBloqueComponenteDTO()) {
                    bloqueComp.setDatos(bloqueComp.getDatos().stream()
                            .filter(x -> x.getExpedienteJustecDTO() != null
                                    && x.getExpedienteJustecDTO().getIdExpedienteJusTec() != null
                                    && x.getExpedienteJustecDTO().getIdExpedienteJusTec()
                                            .equals(idExpedienteJusTec))
                            .collect(Collectors.toList()));

                }
            }

        }

        return impresoJustecDTO;
    }

    @Override
    public byte[] obtenerPlantillaJustec(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario) throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        return this.impresoService.crearInforme(impresoFormulario, mapaDatos, dto);
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarZipExpedienteJustec(Integer idExpediente,
            Integer idExpedienteJustec) throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);
            ExpedienteJusTecDTO expedienteJustec = expedienteJusTecService
                    .buscarPorIdExpedienteJustec(idExpedienteJustec);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipExpedienteJustec(dto, expedienteJustec, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(dto.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;
    }

    private void empaquetarZipExpedienteJustec(ExpedienteDTO dto,
            ExpedienteJusTecDTO expedienteJustec, ZipOutputStream zos) throws ServiceException {

        try {

            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpedienteJustec(dto, expedienteJustec)
                    .getContenidoFichero();
            log.info("impreso Justec: " + Base64.getEncoder().encodeToString(impreso));
            String cdEREGPresent = expedienteJustec.getCdEregPresent();

            // Obtenemos los listados de anexos de cada expediente
            List<byte[]> anexos = this
                    .descargarListadoDocumentos(utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA), dto,
                            cdEREGPresent)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente justec
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpedienteJustec(dto,
                            expedienteJustec.getIdExpedienteJusTec(), Boolean.TRUE, Boolean.FALSE);

            String cdExpediente = dto.getCdExpediente().replace("/", "_").replace(" ", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);
            if (impreso != null) {
                zos.write(impreso);
            }

            // Insertamos los anexos
            for (Integer i = 0; i < anexos.size(); i++) {
                ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                        + cdExpediente + " " + i + ".pdf");
                zos.putNextEntry(anexoZIP);
                zos.write(anexos.get(i));
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    @Transactional(readOnly = false)
    public String generarFirmarListadoJusTecAnexos(ExpedienteDTO dto,
            ExpedienteJusTecDTO expedienteJustec, String asunto,
            List<ExpedienteDocDTO> listaDocumentosDTO, Boolean subsanado) throws ServiceException {

        log.info(
                "Se va a generar el documento de anexos que acompañan a la presentacion o subsanacion de la JCT");

        // Generamos el documento, firmamos y subimos a ereg
        String cdListadoDocEreg = null;
        ExpedienteDoc expdteDocRecibo = null;
        List<ExpedienteDoc> listaDocumentos = new ArrayList<>();

        if (subsanado.equals(Boolean.TRUE)) {
            dto.setLstExpedienteDocDTO(listaDocumentosDTO);
        }
        // Se genera el listado de documentos y se anexa
        byte[] listadoDoc = this.generarAnexoDocumentacionJustec(dto, expedienteJustec, asunto,
                subsanado);
        listadoDoc = this.eregAdmeService.firmarDatos(listadoDoc);
        log.info("Se va a anexar el listado. Tamaño: " + listadoDoc.length);

        // VMM 25/10/24 MD-913: descargamos con el nuevo EREG de la justec ya
        // sea Presentacion o subsanacion
        if (subsanado.equals(Boolean.TRUE)) {
            cdListadoDocEreg = this.eregAdmeService.anexarFicheroAnexo(
                    expedienteJustec.getCdEregSubsana(), listadoDoc,
                    EregAdmeService.ConstantesEregTipoAnexos.LISTADO_DOC_ANEXOS);
        } else {
            cdListadoDocEreg = this.eregAdmeService.anexarFicheroAnexo(
                    expedienteJustec.getCdEregPresent(), listadoDoc,
                    EregAdmeService.ConstantesEregTipoAnexos.LISTADO_DOC_ANEXOS);
        }

        log.info("Código del listado de anexos: " + cdListadoDocEreg);

        if (StringUtils.isNotBlank(cdListadoDocEreg)) {
            // Creamos el documento de recibo de presentacion
            if (subsanado.equals(Boolean.TRUE)) {
                expdteDocRecibo = this.insertarDocumento(dto, cdListadoDocEreg,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA),
                        Constantes.AfirmacionNegacion.N, Boolean.TRUE);
            } else {
                expdteDocRecibo = this.insertarDocumento(dto, cdListadoDocEreg,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA),
                        Constantes.AfirmacionNegacion.N, Boolean.TRUE);
            }

            // VMM 07/11/24 MD-1012: insertamos tambien en la JustecDoc el
            // recibo de presentacion o subsanacion de la JCT
            this.insertarExpteDocFormularioYRecibo(expedienteJustec.getIdExpedienteJusTec(),
                    expdteDocRecibo);
        }

        for (ExpedienteDocDTO expDoc : listaDocumentosDTO) {
            listaDocumentos.add(expedienteDocService.find(expDoc.getIdExpedienteDoc()));
        }
        for (ExpedienteDoc expedienteDoc : listaDocumentos) {
            expedienteDoc.setCdAnexoEregPadre(cdListadoDocEreg);
            expedienteDoc.setCdUsuModif(Utils.valorAuditoria(dto.getInteresadoDTO().getCdNumIden()));
            expedienteDoc.setFcModifFila(new Date());
            CatalogosValores estado = new CatalogosValores();
            estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
            estado.setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION);
            if (subsanado.equals(Boolean.TRUE)) {
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                estado.setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);
            } else {
                CatalogosValores tramite = new CatalogosValores();
                tramite.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA));
                tramite.setNmConstante(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA);
                expedienteDoc.setTramite(tramite);
            }

            expedienteDoc.setEstado(estado);
            this.expedienteDocDAO.update(expedienteDoc);
            this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                    dto.getInteresadoDTO().getCdNumIden());
        }

        if (subsanado.equals(Boolean.TRUE)) {
            expedienteJustec.setIdEstadoDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM)));
        	expedienteJustec.getIdEstadoDTO().setNmConstante(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM);
            expedienteJustec.setIdEstadoRevAdminDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION)));
            expedienteJustec.getIdEstadoRevAdminDTO().setNmConstante(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_SUBSANACION);
        } else {
            expedienteJustec.setIdEstadoDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM)));
			expedienteJustec.getIdEstadoDTO().setNmConstante(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM);
            expedienteJustec.setIdEstadoRevAdminDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM)));
            expedienteJustec.getIdEstadoRevAdminDTO().setNmConstante(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION_ADM);
        }

        expedienteJusTecService.update(expedienteJustec);

        return cdListadoDocEreg;
    }

    /**
     * Método que genera el documento PDF de la presentacion JCT con el anexo,
     * lo firma y lo sube a EREG
     *
     * @param dto ExpedienteDTDo
     * @return String código del anexo
     * @throws ServiceException
     */

    @Override
    public void generaFirmaReciboPresentacionJustec(ExpedienteDTO dto, Integer idExpedienteJustec,
            Object object, List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException {

        ExpedienteJusTecDTO expedienteJustec = expedienteJusTecService
                .buscarPorIdExpedienteJustec(idExpedienteJustec);

        expedienteJustec.setFcFirmaPresent(new Date());

        // VMM 15/11/24 MD-1012: cambiamos el tipo de impreso que estaba de
        // solicitud por el de la Justec correspondiente
        List<Integer> tipoImpresoJCT = new ArrayList<>();
        ImpresoDTO impresoDTO = null;
        if (expedienteJustec.getTipoExpedienteJusTecDTO().getDsValor()
                .equalsIgnoreCase("Intermedia")) {
            impresoDTO = this.getImpresoJustec(dto.getImpresoJustecDTO(),
                    expedienteJustec.getIdExpedienteJusTec());
            tipoImpresoJCT.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_INTERMEDIA));
        } else {
            impresoDTO = this.getImpresoJustec(dto.getImpresoJustecFinalDTO(),
                    expedienteJustec.getIdExpedienteJusTec());
            tipoImpresoJCT.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_JUSTIFICACION_CIEN_TEC_FINAL));
        }

        // Se recupera el asunto del dto
        String cdAsunto = dto.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        // VMM 08/10/24 MD-611: se coge como unidad admin el nivel que tenga la
        // convocatoria definido ya sea Area Admin, subdireccion o direccion
        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG");
        // VMM MD-913 28/10/24: cambiamos funcion para crear el cdEreg del
        // ExpedienteJusTec y no del Expediente
        expedienteJustec = this.eregAdmeService.generarEntradaJCT(dto, cdAsunto, cdUnidad,
                expedienteJustec, Boolean.FALSE);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + expedienteJustec.getCdEregPresent());
        log.info("Destino: " + expedienteJustec.getDsDestinoEreg());

        // Actualizamos los estados del expediente
        Expediente expedienteFirmado = this.expedienteDAO.find(dto.getIdExpediente());

        // expedienteFirmado.setEstado(new
        // CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION_EVALUACION_GESTOR));

        // VMM esto hay que hacerlo por cada periodo!
        // Asignamos el estado interno de revision administrativa
        /// GIM 01/08/24 Hay que cambiar el estado de la justiifcacion, no el
        // estado del expediente
        expedienteFirmado.setEstadoRevJusTec(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)));
//    expedienteJustec.setIdEstadoDTO(new CatalogosValoresDTO(Constantes.EstadoExpedienteJustec.PENDIENTE_DE_REVISION));
//    expedienteJusTecService.update(expedienteJustec);

        // VMM 01/10/24 MD-714: comentamos esto para que no actualice el estado
        // del beneficiario como posible solucion
        // Asignamos el estado de beneficiario
//      expedienteFirmado.setEstadoBenef(ExpedienteToEntity.INSTANCE.calcularEstadoBenef(
//              Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA));

        this.expedienteDAO.update(expedienteFirmado);
        this.expedienteHistDAO.crearHistorico(expedienteFirmado,
                solicitud.getInteresado().getTipoIdentificacion() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));

        // VMM MD-913 28/10/24: guardamos la entidad ExpedienteJusTec firmado y
        // su historico
        ExpedienteJusTec expedienteJustecFirmado = this.expedienteJusTecDAO
                .find(expedienteJustec.getIdExpedienteJusTec());
        expedienteJustecFirmado.setCdEregPresent(expedienteJustec.getCdEregPresent());
        expedienteJustecFirmado.setDsDestinoEreg(expedienteJustec.getDsDestinoEreg());
        expedienteJustecFirmado.setFcFirmaPresent(expedienteJustec.getFcFirmaPresent());

        this.expedienteJusTecDAO.update(expedienteJustecFirmado);
        this.expedienteJusTecHistDAO.crearHistorico(expedienteJustecFirmado);

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposJustec(dto,
                impresoDTO,listaExpedienteDocDTO);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.obtenerPlantillaJustec(dto, hamCampos, impresoDTO);

        // ///////////////////////////////////////////
        // VMM MD-913 28/10/24: cambiamos para recuperar el cdEreg del
        // ExpedienteJusTec y no del Expediente
        byte[] t1 = this.crearFirmarArchivoPdfFirmadoJCT(Boolean.FALSE, dto, plantilla, hamCampos,
                expedienteJustec);
        byte[] t2 = this.crearFirmarArchivoPdfFirmadoJCT(Boolean.TRUE, dto, plantilla, hamCampos,
                expedienteJustec);
        byte[] t3 = this.crearFirmarArchivoFDFJCT(Boolean.FALSE, dto, plantilla, hamCampos,
                expedienteJustec);
        byte[] t4 = this.crearFirmarArchivoXFDFJCT(Boolean.FALSE, dto, plantilla, hamCampos,
                expedienteJustec);
        byte[] t5 = this.crearFirmarArchivoXFDFJCT(Boolean.TRUE, dto, plantilla, hamCampos,
                expedienteJustec);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(
                expedienteJustec.getCdEregPresent(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(
                expedienteJustec.getCdEregPresent(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(
                expedienteJustec.getCdEregPresent(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(
                expedienteJustec.getCdEregPresent(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(
                expedienteJustec.getCdEregPresent(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la presentacion de justec en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        ExpedienteDoc expdteDocFormularioJCT = null;
        if (expedienteJustec.getTipoExpedienteJusTecDTO().getDsValor()
                .equalsIgnoreCase("Intermedia")) {
            expdteDocFormularioJCT = this.insertarDocumento(dto, cdAnexoT2,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_JUSTEC_INTERMEDIA_FIRMADO),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA),
                    Constantes.AfirmacionNegacion.N, Boolean.FALSE);
        } else {
            expdteDocFormularioJCT = this.insertarDocumento(dto, cdAnexoT2,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_JUSTEC_FINAL_FIRMADO),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA),
                    Constantes.AfirmacionNegacion.N, Boolean.FALSE);
        }

        // VMM 12/11/24 MD-1012: insertamos tambien en la JustecDoc el pdf del
        // formulario de presentacion JCT
        this.insertarExpteDocFormularioYRecibo(idExpedienteJustec, expdteDocFormularioJCT);

        log.info("Código de ereg expedienteJusTec: " + expedienteJustec.getCdEregPresent());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info(
                "[PASO 8] - Generamos el listado de anexos que adjuntan a la Justificación científico técnica");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico
        this.generarFirmarListadoJusTecAnexos(dto, expedienteJustec, null, listaExpedienteDocDTO,
                Boolean.FALSE);

    }

    private void insertarExpteDocFormularioYRecibo(Integer idExpedienteJustec,
            ExpedienteDoc expdteDoc) throws ServiceException {
        ExpedienteJustecDoc expdteJustecDoc = new ExpedienteJustecDoc();
        expdteJustecDoc.setIdExpedienteDoc(expdteDoc.getIdExpedienteDoc());
        expdteJustecDoc.setIdExpedienteJusTec(idExpedienteJustec);
        expdteJustecDoc.setCdUsuAlta(expdteDoc.getCdUsuAlta());
        expdteJustecDoc.setFcAltaFila(new Date());

        this.expedienteJusTecDocService.insert(expdteJustecDoc);
    }

    /**
     * Método que genera el documento PDF de la subsanacion JCT con el anexo, lo
     * firma y lo sube a EREG
     *
     * @param dto ExpedienteDTDo
     * @return String código del anexo
     * @throws ServiceException
     */

    @Override
    public void generaFirmaReciboSubsanacionJustec(ExpedienteDTO dto,
            ExpedienteJusTecDTO expedienteJustec, Object object,
            List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException {

        expedienteJustec.setFcFirmaSubsana(new Date());

        // Se recupera el asunto del dto
        String cdAsunto = dto.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        // VMM 08/10/24 MD-611: se coge como unidad admin el nivel que tenga la
        // convocatoria definido ya sea Area Admin, subdireccion o direccion
        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG");
        // VMM MD-913 28/10/24: cambiamos funcion para crear el cdEreg del
        // ExpedienteJusTec y no del Expediente
        expedienteJustec = this.eregAdmeService.generarEntradaJCT(dto, cdAsunto, cdUnidad,
                expedienteJustec, Boolean.TRUE);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + expedienteJustec.getCdEregSubsana());
        log.info("Destino: " + expedienteJustec.getDsDestinoEreg());

        // VMM MD-913 28/10/24: guardamos la entidad ExpedienteJusTec firmado y
        // su historico
        ExpedienteJusTec expedienteJustecFirmado = this.expedienteJusTecDAO
                .find(expedienteJustec.getIdExpedienteJusTec());
        expedienteJustecFirmado.setCdEregSubsana(expedienteJustec.getCdEregSubsana());
        expedienteJustecFirmado.setDsDestinoEreg(expedienteJustec.getDsDestinoEreg());
        expedienteJustecFirmado.setFcFirmaSubsana(expedienteJustec.getFcFirmaSubsana());

        expedienteJustecFirmado
                .setTlObservacionDelCiudadano(expedienteJustec.getTlObservacionDelCiudadano());

        this.expedienteJusTecDAO.update(expedienteJustecFirmado);
        this.expedienteJusTecHistDAO.crearHistorico(expedienteJustecFirmado);

    }

    @Override
    public Integer calcularUnidadAdmin(Solicitud solicitud) throws ServiceException {
        Integer unidadAdmin = null;
        if (solicitud.getConvocatoria().getIdAreaAdministrativa() == null || solicitud
                .getConvocatoria().getIdAreaAdministrativa().getIdUnidadAdmin() == null) {
            // buscamos en el nivel superior (subdireccion)
            if (solicitud.getConvocatoria().getIdSubdireccion() == null
                    || solicitud.getConvocatoria().getIdSubdireccion().getIdUnidadAdmin() == null) {
                // establecemos el nivel superior que es obligatorio (direccion)
                unidadAdmin = solicitud.getConvocatoria().getIdDireccion().getIdUnidadAdmin();
            } else {
                unidadAdmin = solicitud.getConvocatoria().getIdSubdireccion().getIdUnidadAdmin();
            }
        } else {
            unidadAdmin = solicitud.getConvocatoria().getIdAreaAdministrativa().getIdUnidadAdmin();
        }

        return unidadAdmin;
    }

    private byte[] generarAnexoDocumentacionJustec(ExpedienteDTO expedienteDTO,
            ExpedienteJusTecDTO expedienteJustec, String asunto, Boolean subsanado)
            throws ServiceException {

        ImpresoDTO impresoDTO = null;
        // Recuperamos el listado de documentos adjuntos del expediente
        List<DocumentoDTO> lstDocumentosDTO = this.documentoService
                .obtenerDocumentosExpedienteJustec(expedienteDTO,
                        expedienteJustec.getIdExpedienteJusTec(), Boolean.TRUE, subsanado);

        log.info("Documentos recuperados: " + lstDocumentosDTO.size());
        log.info("Documentos pendientes presentar: " + lstDocumentosDTO.size());

        // Recuperamos la convocatoria
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // Recuperamos el interesado
        InteresadoDTO interesado = this.interesadoService
                .buscarId(expedienteDTO.getInteresadoDTO().getIdInteresado());

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        if (expedienteJustec.getTipoExpedienteJusTecDTO().getDsValor()
                .equalsIgnoreCase("Intermedia")) {
            impresoDTO = this.getImpresoJustec(expedienteDTO.getImpresoJustecDTO(),
                    expedienteJustec.getIdExpedienteJusTec());
        } else {
            impresoDTO = this.getImpresoJustec(expedienteDTO.getImpresoJustecFinalDTO(),
                    expedienteJustec.getIdExpedienteJusTec());
        }

        ConvocatoriaPlaDTO convocatoriaPlaDTO = new ConvocatoriaPlaDTO();
        if (Boolean.TRUE.equals(subsanado)) {
            convocatoriaPlaDTO.setTramiteDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA)));
            convocatoriaPlaDTO.getTramiteDTO().setNmConstante(Constantes.Tramite.PRESENTACION_SUBSANACION_JUSTIFICACION_TECNICA);
        } else {
            convocatoriaPlaDTO.setTramiteDTO(
                    new CatalogosValoresDTO(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA)));
            convocatoriaPlaDTO.getTramiteDTO().setNmConstante(Constantes.Tramite.PRESENTACION_JUSTIFICACION_CIENTIFICA_TECNICA);
        }

        // Creamos el anexo (hacemos esto por error del sonar)
        /*
         * if (convocatoriaDTO != null && convocatoriaPlaDTO != null && asunto
         * != null && expedienteDTO != null && interesado != null &&
         * lstDocumentosDTO != null && impresoDTO != null && expedienteJustec !=
         * null && subsanado != null) {
         */
        try {
            return this.generarReciboJCT(convocatoriaDTO, convocatoriaPlaDTO, asunto, expedienteDTO,
                    interesado, lstDocumentosDTO, impresoDTO, expedienteJustec, subsanado);
        } catch (Exception e) {
            log.error("Error al crear el anexo de documentación: " + e.getMessage());
            throw new ServiceException(e);
        }

        /*
         * }else { return new byte[10]; }
         */
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarZipContratos(Integer idExpediente) throws ServiceException {

        Integer idTramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL);
        List<ExpedienteDocDTO> listContratos = null;
//         listContratos = this.expedienteService.obtenerContratos(idTramite,  idExpediente);
        listContratos = this.expedienteDocDAO.listObtenerContratos(idExpediente, idTramite);

        List<ExpedienteDTO> lstExpedienteDTO = this.expedienteDAO
                .listExpedientePorExpediente(idExpediente);

        ExpedienteDTO expedienteDTO = new ExpedienteDTO();

        for (ExpedienteDTO expedienteDTOAux : lstExpedienteDTO) {
            expedienteDTOAux.setIdExpediente(lstExpedienteDTO.get(0).getIdExpediente());
            expedienteDTOAux.setCdExpediente(lstExpedienteDTO.get(0).getCdExpediente());
            expedienteDTO.setIdExpediente(expedienteDTOAux.getIdExpediente());
            expedienteDTO.setCdExpediente(expedienteDTOAux.getCdExpediente());
        }

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipContratos(listContratos, expedienteDTO, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO
                    .setNombreFichero(expedienteDTO.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip contratos, ", ioex);
            throw new ServiceException("Error al crear el zip contratos.");
        }

        return documentoDTO;
    }

    /**
     * Método que rellena el zip con los contratos del expediente. Se utiliza
     * para generar el zip de contratos
     *
     * @param listContratos
     * @param dto expedienteDTO
     * @param zos zip
     * @throws ServiceException
     */
    private void empaquetarZipContratos(List<ExpedienteDocDTO> listContratos,
            ExpedienteDTO expedienteDTO, ZipOutputStream zos) throws ServiceException {

        try {

            String cdExpediente = expedienteDTO.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            for (ExpedienteDocDTO expedienteDocDTO : listContratos) {
                Integer cdFica = expedienteDocDTO.getCdFica();

                // Recuperamos de FICA el nombre del fichero
                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                
                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                String unicodeEscapedFilename = "";
                if (ficaFichero != null) {
                    String rawFilename = ficaFichero.getNombreFichero();
                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                }

                // Insertamos los documentos de FICA
                ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + cdFica + "_"
                        + unicodeEscapedFilename);
                zos.putNextEntry(ficaZIP);
                zos.write(this.ficaService.recuperar(cdFica).getDatosFichero());

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Método que genera el documento PDF con el anexo, lo firma y lo sube a
     * EREG
     *
     * @param dto ExpedienteDTDo
     * @return String código del anexo
     * @throws ServiceException
     */

    @Override
    @Transactional(readOnly = false)
    public String generaFirmaReciboPresentacionContrato(ExpedienteDTO dto, Object object,
            List<ExpedienteDocDTO> lstExpedienteDocDTOAux,
            List<ExpedienteContrato> lstExpedienteContrato) throws ServiceException {

        dto.getExpedienteContrato().setFcFirmaPresent(new Date());
        ImpresoDTO impresoContratoCoordenadorDTO = null;
        // Si existe impreso contrato se recupera el asunto (se comenta esto por
        // estar repetido en rellenarDatosImpresoExpCont)
        if (dto.getImpresoContratoDTO() != null
                && StringUtils.isNotBlank(dto.getImpresoContratoDTO().getCdIdentificacion())) {
            impresoContratoCoordenadorDTO = dto.getImpresoContratoDTO();
            Integer indexContains = impresoContratoCoordenadorDTO.getCdIdentificacion()
                    .indexOf('_');
            if (indexContains < 0) {
                dto.setCdAsunto(impresoContratoCoordenadorDTO.getCdIdentificacion());
            } else {
                dto.setCdAsunto(impresoContratoCoordenadorDTO.getCdIdentificacion().substring(0,
                        indexContains));
            }
        }

        // Se recupera el asunto del dto
        String cdAsunto = dto.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        // VMM 08/10/24 MD-611: se coge como unidad admin el nivel que tenga la
        // convocatoria definido ya sea Area Admin, subdireccion o direccion
        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG de presentacion de contrato");
        dto = this.eregAdmeService.generarEntradaContrato(dto, cdAsunto, cdUnidad);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + dto.getExpedienteContrato().getCdEregPresent());
        log.info("Destino: " + dto.getExpedienteContrato().getDsDestinoEreg());

        // VMM 28/10/24 MD-913: buscamos el contrato del expdte por si no esta
        // relleno
//             if (Objects.isNull(dto.getExpedienteContrato())){
//                 ExpedienteDTO expdteAuxDTO = this.expedienteService.buscarIdSimple(dto.getIdExpediente(), Boolean.TRUE);             
//                 dto.setExpedienteContrato(expdteAuxDTO.getExpedienteContrato());
//                 
//                 Expediente expedienteFirmado = ExpedienteToEntity.INSTANCE.apply(dto);
//             }
        Expediente expedienteFirmado = this.expedienteDAO.find(dto.getIdExpediente());

        // Asignamos el estado interno de revision de contratos
        expedienteFirmado.setEstadoRevContrato(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION)));

        // POM 16/10/24 MD-922 No debe actualizar estado de expadiente
        // expedienteFirmado.setEstado(
        // new
        // CatalogosValores(Constantes.EstadoExpediente.PENDIENTE_REVISION));

        // VMM 01/10/24 MD-714: comentamos esto para que no actualice el estado
        // del beneficiario como posible solucion
        // Asignamos el estado de beneficiario
//             expedienteFirmado.setEstadoBenef(ExpedienteToEntity.INSTANCE.calcularEstadoBenef(
//                     Constantes.EstadoExpediente.EN_TRAMITACION_PRESENTADA));

        // VMM MD-913 28/10/24: comprobar que se esta actualizando el objeto
        // ExpedienteContrato con los campos del EREG
        this.expedienteDAO.update(expedienteFirmado);

        this.expedienteHistDAO.crearHistorico(expedienteFirmado,
                solicitud.getInteresado().getTipoIdentificacion() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        List<ExpedienteDTO> listExpedientesParticipantes = new ArrayList<>();
        for (ExpedienteContrato contrato : lstExpedienteContrato) {
            ExpedienteDTO expParticipanteDTO = this.expedienteService
                    .buscarId(contrato.getIdExpediente());
            expParticipanteDTO
                    .setExpedienteContrato(ExpedienteContratoToDTO.INSTANCE.apply(contrato));
            listExpedientesParticipantes.add(expParticipanteDTO);
        }

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposContrato(dto,
                impresoContratoCoordenadorDTO, listExpedientesParticipantes);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.obtenerInformePresentacionContrato(dto, hamCampos,
                impresoContratoCoordenadorDTO, listExpedientesParticipantes, convocatoriaDTO);
        if (plantilla == null) {
            log.info(
                    " La plantilla para generar el documento firmado de presentación de contrato no se ha generado correctamente");
        }
        // ///////////////////////////////////////////
        byte[] t1 = this.crearFirmarArchivoPdfFirmadoContrato(Boolean.FALSE, dto, plantilla,
                hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmadoContrato(Boolean.TRUE, dto, plantilla,
                hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDFContrato(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDFContrato(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDFContrato(Boolean.TRUE, dto, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteContrato().getCdEregPresent(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteContrato().getCdEregPresent(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteContrato().getCdEregPresent(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteContrato().getCdEregPresent(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteContrato().getCdEregPresent(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la solicitud en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        ExpedienteDoc expedienteDocPresentado = this.insertarDocumento(dto, cdAnexoT2,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_GESTION_CONTRATO_FIRMADO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.GESTION_CONTRATO),
                Constantes.AfirmacionNegacion.N, Boolean.TRUE);
        log.info("Código de ereg expediente: " + dto.getExpedienteContrato().getCdEregPresent());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info(
                "[PASO 8] - Generamos el listado de anexos que adjuntan a la presentacion de contratos");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico

        String cdEregAnexos = this.generarFirmarListadoContratoAnexos(dto, null,
                lstExpedienteDocDTOAux);

        for (ExpedienteContrato contrato : lstExpedienteContrato) {
            if (contrato.getIdEstado().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUBSANACION)) {
                contrato.setIdEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SUBSANACION_PRESENTADA)));
                contrato.getIdEstado().setNmConstante(Constantes.EstadoContrato.SUBSANACION_PRESENTADA);

                contrato.setIdEstadoRevAdmin(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION)));
                contrato.getIdEstadoRevAdmin().setNmConstante(Constantes.EstadoContrato.PENDIENTE_REVISION_SUBSANACION);

                contrato.setCdEregSubsana(dto.getExpedienteContrato().getCdEregPresent());
                contrato.setFcFirmaSubsana(new Date());
                this.expedienteContratoService.insertOrUpdate(contrato);
            } else if (contrato.getIdEstado().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA_CON_SUSTITUCION)) {
                contrato.setIdEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_CON_SUST_PRESENTADA_TIPO_C_INICIAL)));
                contrato.getIdEstado().setNmConstante(Constantes.EstadoContrato.RENUNCIA_CON_SUST_PRESENTADA_TIPO_C_INICIAL);

                contrato.setIdEstadoRevAdmin(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA)));
                contrato.getIdEstadoRevAdmin().setNmConstante(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA);

                contrato.setCdEregRenuncia(dto.getExpedienteContrato().getCdEregPresent());
                contrato.setFcFirmaRenuncia(new Date());
                contrato.setFcBajaFila(new Date());
                this.expedienteContratoService.insertOrUpdate(contrato);

                // FALTA CREAR NUEVO EXPEDIENTE CONTRATO Y CREAR REGISTRO DE SUS
                // DOCUMENTO CON TRAMITE 442
                ExpedienteContrato expedienteContrato = new ExpedienteContrato();
                expedienteContrato.setCdUsuAlta(dto.getCdUsuAlta());
                expedienteContrato.setFcAltaFila(new Date());
                expedienteContrato.setIdExpediente(contrato.getIdExpediente());
                CatalogosValores tipoContrato = new CatalogosValores();
                tipoContrato.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoContrato.CONTRATO_SUSTITUCION));
                tipoContrato.setNmConstante(Constantes.TipoContrato.CONTRATO_SUSTITUCION);
                expedienteContrato.setIdTipoContrato(tipoContrato);
                CatalogosValores estadoExpContrato = new CatalogosValores();
                estadoExpContrato.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SIN_SUBIR_SUSTITUCION));
                estadoExpContrato.setNmConstante(Constantes.EstadoContrato.SIN_SUBIR_SUSTITUCION);
                expedienteContrato.setIdEstado(estadoExpContrato);
                expedienteContrato.setItIndefinido(Constantes.AfirmacionNegacion.N);
                expedienteContrato
                        .setCdEregRenuncia(dto.getExpedienteContrato().getCdEregPresent());
                expedienteContrato.setFcFirmaRenuncia(new Date());
                expedienteContrato.setFcRenuncia(contrato.getFcRenuncia());

                this.expedienteContratoService.insertOrUpdate(expedienteContrato);

                this.expedienteService.registrarDocuemntosRenunciaContrato(
                        contrato.getIdExpediente(),
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_SUSTITUCION));

            } else if (contrato.getIdEstado().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_RENUNCIA_SIN_SUSTITUCION)) {
                contrato.setIdEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.RENUNCIA_SIN_SUSTITUCION_PRESENTADA)));
                contrato.getIdEstado().setNmConstante(Constantes.EstadoContrato.RENUNCIA_SIN_SUSTITUCION_PRESENTADA);

                contrato.setIdEstadoRevAdmin(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA)));
                contrato.getIdEstadoRevAdmin().setNmConstante(Constantes.EstadoContrato.PENDIENTE_REVISION_RENUNCIA);

                contrato.setCdEregRenuncia(dto.getExpedienteContrato().getCdEregPresent());
                contrato.setFcFirmaRenuncia(new Date());
                this.expedienteContratoService.insertOrUpdate(contrato);
            } else if (contrato.getIdEstado().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_SUSTITUCION)) {
                contrato.setIdEstado(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.SUSTITUCION_PRESENTADA)));
                contrato.getIdEstado().setNmConstante(Constantes.EstadoContrato.SUSTITUCION_PRESENTADA);

                contrato.setIdEstadoRevAdmin(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO)));
                contrato.getIdEstadoRevAdmin().setNmConstante(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO);

                contrato.setCdEregPresent(dto.getExpedienteContrato().getCdEregPresent());
                contrato.setFcFirmaPresent(new Date());
                this.expedienteContratoService.insertOrUpdate(contrato);
            } else if (contrato.getIdEstado().getNmConstante().equals(Constantes.EstadoContrato.PENDIENTE_PRESENTAR_CONTRATO)) {
                contrato.setIdEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.CONTRATO_PRESENTADO)));
                contrato.getIdEstado().setNmConstante(Constantes.EstadoContrato.CONTRATO_PRESENTADO);

                contrato.setIdEstadoRevAdmin(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO)));
                contrato.getIdEstadoRevAdmin().setNmConstante(Constantes.EstadoContrato.PENDIENTE_REVISION_CONTRATO);

                contrato.setCdEregPresent(dto.getExpedienteContrato().getCdEregPresent());
                contrato.setFcFirmaPresent(new Date());
                this.expedienteContratoService.insertOrUpdate(contrato);
            }

        }

        return cdEregAnexos;

    }

    private byte[] generarAnexoDocumentacionContrato(ExpedienteDTO expedienteDTO, String asunto,
            List<ExpedienteDocDTO> lstExpedienteDocDTOAux) throws ServiceException {

        ImpresoDTO impresoDTO = new ImpresoDTO();
        // Recuperamos el listado de documentos adjuntos del expediente
        List<DocumentoDTO> lstDocumentosDTO = this.documentoService
                .obtenerDocumentosExpedienteContrato(expedienteDTO, Boolean.TRUE,
                        lstExpedienteDocDTOAux);

        log.info("Documentos pendientes presentar contratos: " + lstDocumentosDTO.size());

        // Recuperamos la convocatoria
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // Recuperamos el interesado
        InteresadoDTO interesado = this.interesadoService
                .buscarId(expedienteDTO.getInteresadoDTO().getIdInteresado());

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        ConvocatoriaPlaDTO convocatoriaPlaDTO = new ConvocatoriaPlaDTO();
        convocatoriaPlaDTO.setTramiteDTO(catalogosValoresService.buscarNmConstante(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL));
		convocatoriaPlaDTO.getTramiteDTO().setNmConstante(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL);
        // Creamos el anexo
        return this.generarReciboContrato(convocatoriaDTO, convocatoriaPlaDTO, asunto,
                expedienteDTO, interesado, lstDocumentosDTO, impresoDTO);
    }

    @Override
    public byte[] obtenerPlantillaContrato(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,
            ConvocatoriaDTO convocatoriaDTOIn) throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        return this.impresoService.crearInformeContrato(dto, impresoFormulario, mapaDatos,
                listExpedientesParticipantes, convocatoriaDTOIn);
    }

    @Override
    public byte[] obtenerInformePresentacionContrato(ExpedienteDTO dto,
            Map<String, String> mapaDatos, ImpresoDTO impresoFormulario,
            List<ExpedienteDTO> listExpedientesParticipantes, ConvocatoriaDTO convocatoriaDTOIn)
            throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        return this.impresoService.crearInformePresentacionContrato(dto, impresoFormulario,
                mapaDatos, listExpedientesParticipantes, convocatoriaDTOIn);
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarZipExpedienteContrato(Integer idExpedienteDoc, String eregPresent)
            throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        List<ExpedienteDoc> lstExpedienteDocDTOAux = null;
        List<ExpedienteContrato> listaContratosParticipante = null;
        ExpedienteDTO dto = new ExpedienteDTO();

        try {
            if (idExpedienteDoc != null) {
                ExpedienteDoc docAnexo = this.expedienteDocDAO.find(idExpedienteDoc);
                if (docAnexo != null) {
                    List<AtlasFilter> filtros = new ArrayList<>();
                    filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREGPADRE,
                            docAnexo.getCdAnexoEreg(), Operador.EQUALS));
                    lstExpedienteDocDTOAux = this.expedienteDocDAO.find(filtros, null);
                }

            } else {
                lstExpedienteDocDTOAux = new ArrayList<>();
                List<ExpedienteContratoDoc> expedienteContratoDocs = this.expedienteContratoDocService
                        .getDao().findByCdEregeContrato(eregPresent);
                for (ExpedienteContratoDoc expContrDoc : expedienteContratoDocs) {
                    if (expContrDoc.getExpedienteDoc().getCdFica() != null
                            && expContrDoc.getExpedienteDoc().getCdAnexoEregPadre() != null) {
                        lstExpedienteDocDTOAux.add(expContrDoc.getExpedienteDoc());
                    }

                }
            }

            listaContratosParticipante = this.expedienteContratoService.getDao()
                    .findAllByEregPresent(eregPresent);
            if (listaContratosParticipante != null && !listaContratosParticipante.isEmpty()) {
                dto = this.expedienteService.buscarId(this.expedienteService
                        .buscarId(listaContratosParticipante.get(0).getIdExpediente())
                        .getIdExpedientePadre());
            }

            // Obtenemos el expediente

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipGestionContrato(dto, lstExpedienteDocDTOAux,
                    listaContratosParticipante, zos, eregPresent);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(dto.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;

    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarZipDocsExpedienteContrato(Integer idExpediente, Integer tramite)
            throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);
            List<ExpedienteDocDTO> lstExpedienteDocDTOAux = new ArrayList<>();

            lstExpedienteDocDTOAux = this.expedienteDocDAO
                    .listExpedienteDocContratosPorTramite(idExpediente, tramite);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipExpedienteContrato(dto, lstExpedienteDocDTOAux, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(dto.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;

    }

    private void empaquetarZipExpedienteContrato(ExpedienteDTO expDTO,
            List<ExpedienteDocDTO> lstExpedienteDocDTOAux, ZipOutputStream zos)
            throws ServiceException {

        try {

            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpedienteContrato(expDTO).getContenidoFichero();
            ExpedienteContratoDTO expedienteContratoDTO = this.expedienteContratoService
                    .obtenerExpedienteContratoByExpediente(expDTO);
            String cdEregEXP = expedienteContratoDTO.getCdEregPresent();
            // Obtenemos los listados de anexos de cada expediente
            List<byte[]> anexos = this
                    .descargarListadoDocumentos(
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_CONTRATO_INICIAL),
                            expDTO, cdEregEXP)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpedienteContrato(expDTO, Boolean.TRUE,
                            lstExpedienteDocDTOAux);

            String cdExpediente = expDTO.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);
            if (impreso != null) {
                zos.write(impreso);
            }

            // Insertamos los anexos
            for (Integer i = 0; i < anexos.size(); i++) {
                ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                        + cdExpediente + " " + i + ".pdf");
                zos.putNextEntry(anexoZIP);
                zos.write(anexos.get(i));
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarExpedienteContrato(ExpedienteDTO expedienteDTO)
            throws ServiceException {
        byte[] expedientePdf = null;
        ImpresoDTO impresoFormulario = null;
        List<AtlasFilter> filtros = new ArrayList<>();
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        List<Integer> lstTipoImpresoContrato = new ArrayList<>();
        lstTipoImpresoContrato.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_CONTRATO_INICIAL));
        List<Impreso> impreso = this.impresoService
                .buscarPorExpedienteTipo(expedienteDTO.getIdExpediente(), lstTipoImpresoContrato);
        if (impreso != null && !impreso.isEmpty()) {
            impresoFormulario = ImpresoToDTO.INSTANCE.apply(impreso.get(0));
        }

        // VMM 28/10/24 MD-913: buscamos el contrato del expdte por si no llega
        // relleno
        if (Objects.isNull(expedienteDTO.getExpedienteContrato())) {
            ExpedienteDTO expdteAuxDTO = this.expedienteService
                    .buscarIdSimple(expedienteDTO.getIdExpediente(), Boolean.TRUE);
            expedienteDTO.setExpedienteContrato(expdteAuxDTO.getExpedienteContrato());
        }
        if (StringUtils.isNotBlank(expedienteDTO.getExpedienteContrato().getCdEregPresent())) {

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    expedienteDTO.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_GESTION_CONTRATO_FIRMADO), Operador.EQUALS));

            // Obtenemos el código de ereg del impreso
            List<ExpedienteDoc> listaExpedientes = this.expedienteDocDAO.find(filtros, null);

            // Si hay código de EREG lo descargamos de EREG
            if (!listaExpedientes.isEmpty()) {
                // Recuperamos el pdf
                expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(
                        expedienteDTO.getExpedienteContrato().getCdEregPresent(),
                        listaExpedientes.get(0).getCdAnexoEreg());
            }

        } else {

            // Recuperamos los valores a insertar en el PDF
            List<ExpedienteDTO> listExpedientesParticipantes = this.expedienteService
                    .buscarParticipantesPorExpedienteAgrupador(expedienteDTO.getIdExpediente(),
                            expedienteDTO.getConvocatoriaActDTO().getIdActor());

            HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposContrato(
                    expedienteDTO, impresoFormulario, listExpedientesParticipantes);

            // Construimos la plantilla PDF
            byte[] plantillaPdf = this.obtenerPlantillaContrato(expedienteDTO, hamCampos,
                    impresoFormulario, listExpedientesParticipantes, convocatoriaDTO);

            // Inyectamos los valores a la plantilla PDF
            expedientePdf = this.rellenarPdfContrato(hamCampos, plantillaPdf, Boolean.FALSE,
                    expedienteDTO);
        }

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(
                    expedienteDTO.getCdExpediente() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    @Override
    @Transactional(readOnly = false)
    public String generarFirmarListadoContratoAnexos(ExpedienteDTO dto, String asunto,
            List<ExpedienteDocDTO> listaDocumentosDTO) throws ServiceException {
        log.info(
                "Se va a generar el documento de anexos que acompañan a la presentacion Contrato o subsanacion del mismo");
        // Generamos el documento, firmamos y subimos a ereg
        String cdListadoDocEreg = null;
        List<ExpedienteDoc> listaDocumentos = new ArrayList<>();
        Integer idTramite = utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.GESTION_CONTRATO);

        // VMM MD-913 28/10/24: cambiamos para recuperar el EREG del
        // expedienteContrato en vez del Expediente
        // Se genera el listado de documentos y se anexa
        byte[] listadoDoc = this.generarAnexoDocumentacionContrato(dto, null, listaDocumentosDTO);
        listadoDoc = this.eregAdmeService.firmarDatos(listadoDoc);
        log.info("Se va a anexar el listado. Tamaño: " + listadoDoc.length);

        cdListadoDocEreg = this.eregAdmeService.anexarFicheroAnexo(
                dto.getExpedienteContrato().getCdEregPresent(), listadoDoc,
                EregAdmeService.ConstantesEregTipoAnexos.LISTADO_DOC_ANEXOS);

        log.info("Código del listado de anexos contrato: " + cdListadoDocEreg);

        if (StringUtils.isNotBlank(cdListadoDocEreg)) {
            // Creamos el documento de recibo de presentacion
            this.insertarDocumento(dto, cdListadoDocEreg,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.RECIBO_PRESENTACION),
                    idTramite, Constantes.AfirmacionNegacion.N, Boolean.FALSE);
        }

        for (ExpedienteDocDTO expDoc : listaDocumentosDTO) {
            listaDocumentos.add(expedienteDocService.find(expDoc.getIdExpedienteDoc()));
        }

        for (ExpedienteDoc expedienteDoc : listaDocumentos) {
            expedienteDoc.setCdAnexoEregPadre(cdListadoDocEreg);
            expedienteDoc.setCdUsuModif(Utils.valorAuditoria(dto.getInteresadoDTO().getCdNumIden()));
            expedienteDoc.setFcModifFila(new Date());
            if (expedienteDoc.getEstado().getNmConstante().equals(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO)) {
                CatalogosValores estado = new CatalogosValores();
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION));
                estado.setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION_SUBSANACION);
                expedienteDoc.setEstado(estado);
            } else {
                CatalogosValores estado = new CatalogosValores();
                estado.setIdValor(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.PENDIENTE_REVISION));
                estado.setNmConstante(Constantes.EstadoDocumento.PENDIENTE_REVISION);
                expedienteDoc.setEstado(estado);
            }

            this.expedienteDocDAO.update(expedienteDoc);
            this.expedienteDocHistDAO.crearHistorico(expedienteDoc,
                    dto.getInteresadoDTO().getCdNumIden());
        }

        return cdListadoDocEreg;
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarZipExpedienteEvaluacionJustecCT(Integer idExpedienteEvalJusTec)
            throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            ExpedienteEvalJusTecDTO dto = this.expedienteEvalJusTecService
                    .buscarByIdExpedienteJustec(idExpedienteEvalJusTec);
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarId(dto.getExpedienteJustecDTO().getIdExpediente());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipExpedienteEvaluadorJCT(dto, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO
                    .setNombreFichero(expedienteDTO.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException(
                    "Error al crear el zip expediente evaluador de la Justec. CT");
        }

        return documentoDTO;
    }

    /**
     * Método que rellena el zip con los contratos del expediente. Se utiliza
     * para generar el zip de contratos
     *
     * @param listContratos
     * @param dto expedienteDTO
     * @param zos zip
     * @throws ServiceException
     */
    private void empaquetarZipExpedienteEvaluadorJCT(
            ExpedienteEvalJusTecDTO expedienteEvalJusTecDTO, ZipOutputStream zos)
            throws ServiceException {

        try {

            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarId(expedienteEvalJusTecDTO.getExpedienteJustecDTO().getIdExpediente());

            String cdExpediente = expedienteDTO.getCdExpediente().replace("/", "_");
            Integer cdFica = Integer.valueOf(expedienteEvalJusTecDTO.getCdFica());

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Recuperamos de FICA el nombre del fichero
            FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
            
            //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            }

            // Insertamos los documentos de FICA
            ZipEntry ficaZIP = new ZipEntry(
                    cdExpediente + "/documentos/" + cdFica + "_" + unicodeEscapedFilename);
            zos.putNextEntry(ficaZIP);
            zos.write(this.ficaService.recuperar(cdFica).getDatosFichero());

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
    *
    */
    @Override
    public byte[] crearFormularioEvaluacionJustecDatosIdExpEval(
            ExpedienteEvalJusTecDTO expedienteEvalJustecDTO) throws ServiceException {

        byte[] byteSalida = null;

        if (Objects.isNull(expedienteEvalJustecDTO.getIdExpedienteEvalJusTec())) {
            throw new ServiceException(Constantes.Error.ERROR_VALORES_ENT);
        }

        try {

            // Lista de informe de evaluaciones
            List<InformeEvaluacionDTO> lstInfoEvaluacion = this.expedienteEvalJusTecService
                    .obtenerInformeEvaluacionJustec(
                            expedienteEvalJustecDTO.getIdExpedienteEvalJusTec());

            if (lstInfoEvaluacion.isEmpty()) {
                throw new ServiceException(Constantes.Error.ERROR_INFORME_NO_EXISTE_EVALUACION,
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            // Recueperamos la convocatoria
            ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                    .buscarId(lstInfoEvaluacion.get(0).getConvIdConvocatoria());

            // Lista de evaluaciones
            List<EvaluacionJustecDTO> listaEvaluacion = this.evaluacionJustecService
                    .obtenerPorConvocatoria(lstInfoEvaluacion.get(0).getConvIdConvocatoria(),
                            lstInfoEvaluacion.get(0).getExpEvaItCoorEval());
            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            this.generarCabeceraPdf(convocatoriaDTO, listaEvaluacion.get(0).getImagenLogoDTO(),
                    document, baos, Boolean.FALSE, null);

            document.open();

            informeHelperPdfService.agregarTitulo(document, Constantes.Evaluacion.TEXTOTITULO,
                    convocatoriaDTO.getCdTitulo(),"","", null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);

            informeHelperPdfService.agregarTituloCabeceraExpediente(document,
                    Constantes.Evaluacion.TEXTO_EXPEDIENTE,
                    lstInfoEvaluacion.get(0).getExpCdExpediente());
            if (StringUtils.isNotBlank(lstInfoEvaluacion.get(0).getInteDsApellido2())) {
                informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                        Constantes.Evaluacion.TEXTO_INVES_EVALUADOR,
                        lstInfoEvaluacion.get(0).getInteDsNombre() + " "
                                + lstInfoEvaluacion.get(0).getInteDsApellido1() + " "
                                + (Objects.isNull(lstInfoEvaluacion.get(0).getInteDsApellido2())
                                        ? ""
                                        : lstInfoEvaluacion.get(0).getInteDsApellido2()));
            } else {
                informeHelperPdfService.agregarTituloCabeceraCoorEval(document,
                        Constantes.Evaluacion.TEXTO_INVES_EVALUADOR,
                        lstInfoEvaluacion.get(0).getInteDsNombre() + " "
                                + lstInfoEvaluacion.get(0).getInteDsApellido1());
            }

            informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Evaluacion.TEXTO_FECHA,
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            informeHelperPdfService.agregarFilaVacio(document, 1);

            this.crearFormularioEvaluacionJustecDatosIdExpEvalEvaluaciones(listaEvaluacion,
                    document, expedienteEvalJustecDTO);

            document.close();

            byteSalida = baos.toByteArray();

        } catch (DocumentException | ServiceException e) {
            log.error(e.getMessage());

        }

        return byteSalida;
    }

    private void crearFormularioEvaluacionJustecDatosIdExpEvalEvaluaciones(
            List<EvaluacionJustecDTO> listaEvaluacion, Document document,
            ExpedienteEvalJusTecDTO expedienteEvalDTO) throws ServiceException {

        try {

            for (EvaluacionJustecDTO evaluacionDTO : listaEvaluacion) {

                informeHelperPdfService.agregarTituloEvaluacion(document,
                        evaluacionDTO.getNmOrden() + " ) " + evaluacionDTO.getDsTitulo());

                informeHelperPdfService.agregarDescripcion(document, evaluacionDTO.getTlDescripcion());

                informeHelperPdfService.agregarListadoEvaluacionesApartNotas(document,
                        this.expedienteEvalJusTecService.obtenerApartadosDatos(
                                expedienteEvalDTO.getIdExpedienteEvalJusTec(),
                                evaluacionDTO.getIdEvaluacionJustec()),
                        evaluacionDTO.getItCalificacion());

                IntSummaryStatistics valMin = evaluacionDTO.getListaEvaluacionJustecNotaDTO()
                        .stream()
                        .collect(Collectors.summarizingInt(EvaluacionJustecNotaDTO::getNmDe));

                IntSummaryStatistics valMax = evaluacionDTO.getListaEvaluacionJustecNotaDTO()
                        .stream()
                        .collect(Collectors.summarizingInt(EvaluacionJustecNotaDTO::getNmA));

                if (Constantes.AfirmacionNegacion.S.equals(evaluacionDTO.getItCalificacion())) {
                    informeHelperPdfService.agregarValoracionGlobal(document,
                            "VALORACIÓN GLOBAL DEL PROYECTO (indicar puntuación de "
                                    + valMin.getMin() + " a " + valMax.getMax() + " )");
                } else if (Constantes.AfirmacionNegacion.N
                        .equals(evaluacionDTO.getItCalificacion())) {
                    informeHelperPdfService.agregarValoracionGlobal(document,
                            "VALORACIÓN GLOBAL DEL PROYECTO (suma de notas de los apartados)");
                }

                List<EvaluacionJustecNotaDatoDTO> lstEvaNotaDato = this.evaluacionJustecNotaDatoService
                        .obtenerEvaluacionJustecNotaDatoPorIdEvalIdExpEvalJustec(
                                expedienteEvalDTO.getIdExpedienteEvalJusTec(),
                                evaluacionDTO.getIdEvaluacionJustec());

                Integer nota = 0;
                if (!lstEvaNotaDato.isEmpty()) {
                    nota = lstEvaNotaDato.get(0).getNmNota();
                }

                informeHelperPdfService.agregarListadoEvaluacionesJustecNota(document,
                        evaluacionDTO.getListaEvaluacionJustecNotaDTO().stream()
                                .filter(f -> f.getFcBajaFila() == null)
                                .collect(Collectors.toList()),
                        nota);

                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

        } catch (DocumentException | ServiceException e) {
            throw new ServiceException(e.getMessage());
        }

    }

    private void empaquetarZipGestionContrato(ExpedienteDTO expDTO,
            List<ExpedienteDoc> lstExpedienteDoc, List<ExpedienteContrato> contratos,
            ZipOutputStream zos, String eregPresent) throws ServiceException {
        List<ExpedienteDocDTO> lstExpedienteDocDTOAux = new ArrayList<>();
        try {
            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarPdfExpedienteContrato(expDTO, contratos, eregPresent)
                    .getContenidoFichero();

            // Obtenemos los listados de anexos de cada expediente
            List<byte[]> anexos = this
                    .descargarListadoDocumentos(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.GESTION_CONTRATO), 
                            expDTO, eregPresent)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente
            if (lstExpedienteDoc != null && !lstExpedienteDoc.isEmpty()) {
                for (ExpedienteDoc doc : lstExpedienteDoc) {
                    lstExpedienteDocDTOAux.add(ExpedienteDocToDTO.INSTANCE.apply(doc));
                }
            }
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpedienteContrato(expDTO, Boolean.TRUE,
                            lstExpedienteDocDTOAux);

            String cdExpediente = expDTO.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);
            if (impreso != null) {
                zos.write(impreso);
            }

            // Insertamos los anexos
            for (Integer i = 0; i < anexos.size(); i++) {
                ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                        + cdExpediente + " " + i + ".pdf");
                zos.putNextEntry(anexoZIP);
                zos.write(anexos.get(i));
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
    *
    */
    @Override
    public DocumentoDTO descargarPdfExpedienteContrato(ExpedienteDTO expedienteDTO,
            List<ExpedienteContrato> contratos, String eregPresent) throws ServiceException {
        byte[] expedientePdf = null;
        ImpresoDTO impresoFormulario = null;
        List<AtlasFilter> filtros = new ArrayList<>();
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        impresoFormulario = expedienteDTO.getImpresoContratoDTO();

        if (contratos != null && !contratos.isEmpty()) {

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    expedienteDTO.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_GESTION_CONTRATO_FIRMADO), Operador.EQUALS));

            // Obtenemos el código de ereg del impreso
            List<ExpedienteDoc> listaExpedientes = this.expedienteDocDAO.find(filtros, null);

            // Si hay código de EREG lo descargamos de EREG
            if (!listaExpedientes.isEmpty()) {
                // Recuperamos el pdf
                for (ExpedienteDoc doc : listaExpedientes) {
                    if (expedientePdf == null) {
                        expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(eregPresent,
                                doc.getCdAnexoEreg());
                    }

                }

            }

        } else {

            List<ExpedienteDTO> listExpedientesParticipantes = new ArrayList<>();
            for (ExpedienteContrato contrato : contratos) {
                ExpedienteDTO expParticipanteDTO = this.expedienteService
                        .buscarId(contrato.getIdExpediente());
                expParticipanteDTO
                        .setExpedienteContrato(ExpedienteContratoToDTO.INSTANCE.apply(contrato));
                listExpedientesParticipantes.add(expParticipanteDTO);
            }

            // Obtenemos todos los campos a fusionar
            HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposContrato(
                    expedienteDTO, impresoFormulario, listExpedientesParticipantes);

            // Recuperamos la plantilla del impreso
            byte[] plantillaPdf = this.obtenerInformePresentacionContrato(expedienteDTO, hamCampos,
                    impresoFormulario, listExpedientesParticipantes, convocatoriaDTO);
            // Construimos la plantilla PDF
            // byte[] plantillaPdf =
            // this.obtenerPlantillaContrato(expedienteDTO, hamCampos,
            // impresoFormulario, listExpedientesParticipantes,
            // convocatoriaDTO);

            // Inyectamos los valores a la plantilla PDF
            expedientePdf = this.rellenarPdfContrato(hamCampos, plantillaPdf, Boolean.FALSE,
                    expedienteDTO);
        }

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(
                    expedienteDTO.getCdExpediente() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    @Override
    @Transactional(readOnly = false)
    public void generaFirmaReciboPresentacionPresupPlazosOtros(ExpedienteDTO dto)
            throws ServiceException {

        dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                .setFcFirmaPresent(new Date());
        ImpresoDTO impresoPresupPlazosOtros = dto.getImpresoModificacionesPresupPlazosOtrosDTO();

        // Si existe impreso contrato se recupera el asunto (se comenta esto por
        // estar repetido en rellenarDatosImpresoExpCont)
        if (dto.getImpresoModificacionesPresupPlazosOtrosDTO() != null && StringUtils.isNotBlank(
                dto.getImpresoModificacionesPresupPlazosOtrosDTO().getCdIdentificacion())) {

            Integer idModificacion = dto.getExpedienteModifOtraDTO()
                    .getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra();
            for (BloqueDTO bloque : impresoPresupPlazosOtros.getLstBloqueDTO()) {
                for (BloqueComponenteDTO bloqComp : bloque.getLstBloqueComponenteDTO()) {
                    List<DatoDTO> datos = bloqComp.getDatos().stream()
                            .filter(d -> d.getExpedienteModifPresPlazoOtraDTO() != null
                                    && d.getExpedienteModifPresPlazoOtraDTO()
                                            .getIdExpedienteModifPresPlazoOtra() != null
                                    && d.getExpedienteModifPresPlazoOtraDTO()
                                            .getIdExpedienteModifPresPlazoOtra()
                                            .equals(idModificacion))
                            .collect(Collectors.toList());

                    bloqComp.setDatos(datos);
                }
            }
            dto.setImpresoModificacionesPresupPlazosOtrosDTO(impresoPresupPlazosOtros);

            Integer indexContains = impresoPresupPlazosOtros.getCdIdentificacion().indexOf('_');
            if (indexContains < 0) {
                dto.setCdAsunto(impresoPresupPlazosOtros.getCdIdentificacion());
            } else {
                dto.setCdAsunto(
                        impresoPresupPlazosOtros.getCdIdentificacion().substring(0, indexContains));
            }
        }

        // Se recupera el asunto del dto
        String cdAsunto = dto.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        Integer unidadAdmin = calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG impreso Presup plazos otros");
        log.info("Se va a generar la entrada en EREG impreso Presup plazos otros");
        dto = this.eregAdmeService.generarEntradaPresupPlazosOtros(dto, cdAsunto, cdUnidad,
                Boolean.FALSE);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                .getCdEregPresent());
        log.info("Destino: " + dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                .getDsDestinoEreg());

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService
                .rellenarCamposModPresupPlazosOtra(dto);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.obtenerPlantillaModPresupPlazosOtros(dto, hamCampos);

        // ///////////////////////////////////////////
        byte[] t1 = this.crearFirmarArchivoPdfFirmado(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmado(Boolean.TRUE, dto, plantilla, hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDF(Boolean.TRUE, dto, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent(),
                t1, EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent(),
                t2, EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent(),
                t3, EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent(),
                t4, EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(
                dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO()
                        .getCdEregPresent(),
                t5, EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info(
                "[PASO 7] - Insertamos la modificacion de presup plazos otros firmado en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        ExpedienteDoc docFormPDF = this.insertarDocumento(dto, cdAnexoT2,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_MODIFICACIONES_PRESUP_PLAZOS_OTROS_FIRMADO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS),
                Constantes.AfirmacionNegacion.N, Boolean.FALSE);

        ExpedienteModfPresupPlazosOtrosDoc expedienteModfPresupPlazosOtrosDoc = new ExpedienteModfPresupPlazosOtrosDoc();
        expedienteModfPresupPlazosOtrosDoc.setExpedienteDoc(docFormPDF);
        ExpedienteModifPresPlazoOtra expModifOtra = new ExpedienteModifPresPlazoOtra();

        expModifOtra.setIdExpedienteModifPresPlazoOtra(dto.getExpedienteModifOtraDTO()
                .getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra());
        expedienteModfPresupPlazosOtrosDoc.setExpedienteModifPresPlazoOtra(expModifOtra);
        expedienteModfPresupPlazosOtrosDoc.setFcAltaFila(new Date());
        expedienteModfPresupPlazosOtrosDoc.setCdUsuAlta(dto.getCdUsuAlta());
        this.expedienteModfPresupPlazosOtrosDocService.getDao()
                .insert(expedienteModfPresupPlazosOtrosDoc);

        log.info("Código de ereg expediente: " + dto.getExpedienteModifOtraDTO()
                .getExpedienteModifPresPlazoOtraDTO().getCdEregPresent());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info("[PASO 8] - Generamos el listado de anexos que adjuntan a la solicitud");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico

        this.generarFirmarListadoAnexos(dto,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.PRESENTACION_MODIFICACION_PRESP_PLAZOS_OTROS),
                null, dto.getListaDocumentosPresup());

    }

    public byte[] obtenerPlantillaModPresupPlazosOtros(ExpedienteDTO dto,
            Map<String, String> mapaDatos) throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        
        if(dto.getImpresoModificacionesPresupPlazosOtrosDTO() != null && !dto.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO().isEmpty()) {
            Integer idModificacion=dto.getExpedienteModifOtraDTO()
                    .getExpedienteModifPresPlazoOtraDTO()
                    .getIdExpedienteModifPresPlazoOtra();
            for(BloqueDTO bloque:dto.getImpresoModificacionesPresupPlazosOtrosDTO().getLstBloqueDTO()) {
                List<BloqueComponenteDTO> listaBloqueComponente = bloque
                        .getLstBloqueComponenteDTO().stream()
                        .filter(x -> x.getFcBajaFila() == null)
                        .sorted(Comparator.comparing(BloqueComponenteDTO::getNmOrden))
                        .collect(Collectors.toList());

                // Se filtra en todos los bloqueComponentes del bloque (no solo los de tipo
                // fila) para que solo queden los datos de la modificacion actual y no se
                // cuelen datos de otras modificaciones del mismo expediente
                for(BloqueComponenteDTO bloqCom:listaBloqueComponente) {
                    List<DatoDTO> datosBloq= bloqCom.getDatos().stream().filter(x ->
                    x.getExpedienteModifPresPlazoOtraDTO() != null
                    && dto.getExpedienteModifOtraDTO()!= null
                    && dto.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO() != null

                    && x.getExpedienteModifPresPlazoOtraDTO().getIdExpedienteModifPresPlazoOtra()
                    .equals(idModificacion)).collect(Collectors.toList());
                    bloqCom.setDatos(datosBloq);
                }
            }
        }
       
        
        
        return this.impresoService.crearInforme(dto.getImpresoModificacionesPresupPlazosOtrosDTO(),
                mapaDatos, dto);
    }

    @Override
    public DocumentoDTO descargarModificacionesPresupPlazosOtros(Integer idModificacion)
            throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            // Obtenemos los expedientes de la solicitud
            ExpedienteModifPresPlazoOtra expedienteModifPresPlazoOtra = this.expedienteModifPresPlazoOtraService
                    .getDao().find(idModificacion);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            if (expedienteModifPresPlazoOtra != null) {
                this.empaquetarExpedienteModifPresPlazoOtraZip(expedienteModifPresPlazoOtra, zos);
            }

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(idModificacion + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip modificaciones presup plazos otros, ", ioex);
            throw new ServiceException(
                    "Error al crear el zip  modificaciones presup plazos otros.");
        }

        return documentoDTO;
    }

    private void empaquetarExpedienteModifPresPlazoOtraZip(ExpedienteModifPresPlazoOtra dto,
            ZipOutputStream zos) throws ServiceException {

        try {
            ExpedienteDTO expedienteDTO = this.expedienteService
                    .buscarId(dto.getExpediente().getIdExpediente());
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(
                    new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_MODIF_ID_EXPEDIENTE_MODIF,
                            dto.getIdExpedienteModifPresPlazoOtra(), Operador.EQUALS));

            List<ExpedienteModfPresupPlazosOtrosDocDTO> listaExpedientesModif = this.expedienteModfPresupPlazosOtrosDocService
                    .buscar(0, -1, filtros, null);
            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpedienteModifPresPlazoOtra(dto, expedienteDTO,
                    listaExpedientesModif).getContenidoFichero();
            log.debug("impreso solicitud: " + Base64.getEncoder().encodeToString(impreso));
            String cdEregEXP = dto.getCdEregPresent();
            // Obtenemos los listados de anexos de cada expediente
            // VMM 30/09/24 MD-610: pasar aqui por param el tramite 400 en vez
            // de un null aunque
            // el filtro que hace dentro con el recibo de presentacion (171) se
            // supone que es de este tramite
            // revisar que esto no falle para otros casos que no sean de
            // presentacion de solicitud
            List<byte[]> anexos = this
                    .descargarListadoDocumentosModifPresup(dto, cdEregEXP, listaExpedientesModif)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente
            List<ExpedienteDocDTO> lstExpDocDTO = new ArrayList<>();

            for (ExpedienteModfPresupPlazosOtrosDocDTO docModDTO : listaExpedientesModif) {
                if (docModDTO.getExpedienteDocDTO() != null


                        && ((docModDTO.getExpedienteDocDTO().getTipoDocumentoDTO().getNmConstante() == null)
                        ||(docModDTO.getExpedienteDocDTO().getTipoDocumentoDTO().getNmConstante() != null
                        && !docModDTO.getExpedienteDocDTO().getTipoDocumentoDTO().getNmConstante().equals(Constantes.TipoDocumento.PDF_FORM_MODIFICACIONES_PRESUP_PLAZOS_OTROS_FIRMADO)
                        && !docModDTO.getExpedienteDocDTO().getTipoDocumentoDTO().getNmConstante().equals(Constantes.TipoDocumento.RECIBO_PRESENTACION)))) {
                    lstExpDocDTO.add(docModDTO.getExpedienteDocDTO());
                }
            }

            // Añadimos los documentos subidos en los conceptos del presupuesto
            // (detalle concepto), que no están ligados a un bloque de tipo
            // documento del impreso y por tanto no vienen en listaExpedientesModif
            lstExpDocDTO.addAll(this.expedienteDocService
                    .obtenerDocumentosConceptoPresupuesto(dto.getIdExpedienteModifPresPlazoOtra()));

            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpediente(expedienteDTO, Boolean.TRUE,
                            utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.MODIFICACION_PRESP_PLAZOS_OTROS),
                            lstExpDocDTO);

            String cdExpediente = dto.getExpediente().getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);

            // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
            // que no de error hasta que arreglemos lo de EREG
            if (impreso != null) {
                zos.write(impreso);
            } else {
                log.error("No se ha encontrado el impreso relleno del expediente");
            }

            // VMM 25/09/24: ponemos un control aqui si los anexos son vacios
            // para que no de error hasta que arreglemos lo de EREG
            // Insertamos los anexos
            if (!anexos.isEmpty()) {
                for (Integer i = 0; i < anexos.size(); i++) {
                    ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                            + cdExpediente + " " + i + ".pdf");
                    zos.putNextEntry(anexoZIP);
                    zos.write(anexos.get(i));
                }
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    if(fica.getCdFica() != null) {
                        ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                        + "_" + fica.getNombreFichero());
                        zos.putNextEntry(ficaZIP);
                        zos.write(fica.getContenidoFichero());
                    }
                    
                }

            }
        } catch (Exception e) {
            log.error("Error al empaquetar el zip de modificaciones presup plazos otros", e);
            throw new ServiceException(e);
        }
    }

    private DocumentoDTO descargarExpedienteModifPresPlazoOtra(
            ExpedienteModifPresPlazoOtra modificacionDTO, ExpedienteDTO expedienteDTO,
            List<ExpedienteModfPresupPlazosOtrosDocDTO> listaExpedientesModif)
            throws ServiceException {
        byte[] expedientePdf = null;

        // Si hay código de EREG lo descargamos de EREG
        if (StringUtils.isNotBlank(modificacionDTO.getCdEregPresent())) {

            // Obtenemos el código de ereg del impreso

            if (!listaExpedientesModif.isEmpty()) {
                for (ExpedienteModfPresupPlazosOtrosDocDTO docModDTO : listaExpedientesModif) {
                    if (docModDTO.getExpedienteDocDTO() != null &&
                            docModDTO.getExpedienteDocDTO()
                            .getTipoDocumentoDTO().getNmConstante() != null &&
                            docModDTO.getExpedienteDocDTO()
                            .getTipoDocumentoDTO().getNmConstante().equals(Constantes.TipoDocumento.PDF_FORM_MODIFICACIONES_PRESUP_PLAZOS_OTROS_FIRMADO)) {
                        expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(
                                modificacionDTO.getCdEregPresent(),
                                docModDTO.getExpedienteDocDTO().getCdAnexoEreg());
                    }
                }
                // Recuperamos el pdf

            }

        } else {

            // Recuperamos los valores a insertar en el PDF.
            // expedienteDTO.getExpedienteModifOtraDTO() solo viene relleno desde
            // buscarId() cuando existe una modificación en estado INICIADA para el
            // expediente; si la modificación a descargar no está en ese estado (o
            // ya se ha presentado otra en paralelo) viene a null, por lo que hay
            // que asegurar el contenedor antes de asignarle la modificación actual.
            if (expedienteDTO.getExpedienteModifOtraDTO() == null) {
                expedienteDTO.setExpedienteModifOtraDTO(new ExpedienteModifOtraDTO());
            }
            expedienteDTO.getExpedienteModifOtraDTO().setExpedienteModifPresPlazoOtraDTO(
                    ExpedienteModifPresPlazoOtraToDTO.INSTANCE.apply(modificacionDTO));
            HashMap<String, String> hamCampos = this.pdfCamposService
                    .rellenarCamposModPresupPlazosOtra(expedienteDTO);

            // Construimos la plantilla PDF
            byte[] plantillaPdf = this.obtenerPlantillaModPresupPlazosOtros(expedienteDTO,
                    hamCampos);

            // Inyectamos los valores a la plantilla PDF
            expedientePdf = this.rellenarPdf(hamCampos, plantillaPdf, Boolean.FALSE, expedienteDTO);
        }

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(expedienteDTO.getCdExpediente() + "modificacion"
                    + modificacionDTO.getIdModificacion() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    public List<DocumentoDTO> descargarListadoDocumentosModifPresup(
            ExpedienteModifPresPlazoOtra modificacionDTO, String cdEreg,
            List<ExpedienteModfPresupPlazosOtrosDocDTO> listaExpedientesModif)
            throws ServiceException {

        List<byte[]> listaDocAnexados = new ArrayList<>();
        if (!listaExpedientesModif.isEmpty()) {
            for (ExpedienteModfPresupPlazosOtrosDocDTO docModDTO : listaExpedientesModif) {
                if (docModDTO.getExpedienteDocDTO() != null
                        && docModDTO.getExpedienteDocDTO().getTipoDocumentoDTO().getNmConstante() != null
                        && docModDTO.getExpedienteDocDTO().getTipoDocumentoDTO().getNmConstante().equals(Constantes.TipoDocumento.RECIBO_PRESENTACION)) {

                    // Recuperamos el pdf de cada uno
                    if (StringUtils.isNotBlank(cdEreg)) {

                        byte[] result = this.eregAdmeService.descargarAnexoPorCodigo(cdEreg,
                                docModDTO.getExpedienteDocDTO().getCdAnexoEreg());
                        if (result != null && result.length > 0) {
                            listaDocAnexados.add(result);
                        }

                    }
                }
            }

        }

        List<DocumentoDTO> listaDocumentoDTO = new ArrayList<>();
        log.info(" El total de listaDocAnexados:" + listaDocAnexados.size());
        for (byte[] anexo : listaDocAnexados) {
            DocumentoDTO documento = new DocumentoDTO();
            documento.setContenidoFichero(anexo);
            documento.setNombreFichero(modificacionDTO.getExpediente().getCdExpediente()
                    + "_recibo_presentacion" + "." + Constantes.Extensiones.PDF);
            listaDocumentoDTO.add(documento);
        }

        return listaDocumentoDTO;

    }

    @Override
    public void generaFirmaReciboPresentacionReq(ExpedienteDTO dto, Object object,
            List<ExpedienteDocDTO> lstExpedienteDocDTO, List<ExpedienteDTO> listExpParticipantes)
            throws ServiceException {
        dto.setFcFirmaSubsanaReq(new Date());
        ImpresoDTO impresoCoordinadorDTO = null;
        // Si existe impreso se recupera el asunto
        if (dto.getImpresoDTO() != null
                && StringUtils.isNotBlank(dto.getImpresoDTO().getCdIdentificacion())) {
            impresoCoordinadorDTO = dto.getImpresoDTO();
            Integer indexContains = impresoCoordinadorDTO.getCdIdentificacion().indexOf('_');
            if (indexContains < 0) {
                dto.setCdAsunto(impresoCoordinadorDTO.getCdIdentificacion());
            } else {
                dto.setCdAsunto(
                        impresoCoordinadorDTO.getCdIdentificacion().substring(0, indexContains));
            }
        }

        // Se recupera el asunto del dto
        String cdAsunto = dto.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG de presentacion de subsanacion de requisitos");
        dto = this.eregAdmeService.generarEntradaSubsanarReq(dto, cdAsunto, cdUnidad, Boolean.TRUE);
        dto.setCdEregSubsanaReq(dto.getCdEregSubsanaReq());
        dto.setDsDestinoEreg(dto.getDsDestinoEreg());
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + dto.getCdEregSubsanaReq());
        log.info("Destino: " + dto.getDsDestinoEreg());

        for (ExpedienteDTO expPartiDTO : listExpParticipantes) {
            Expediente expPar = this.expedienteDAO.find(expPartiDTO.getIdExpediente());

            // Asignamos el estado interno de revision de requisitos y cdereg
            expPar.setCdEregSubsanaReq(dto.getCdEregSubsanaReq());
            expPar.setEstadoRevRequi(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)));
            expPar.getEstadoRevRequi().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS);
            expPar.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)));
            expPar.getEstado().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS);
            this.expedienteDAO.update(expPar);

            this.expedienteHistDAO.crearHistorico(expPar,
                    solicitud.getInteresado().getTipoIdentificacion() != null
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                    .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                            ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                            : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
        }

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        List<ExpedienteDTO> listExpedientesParticipantes = new ArrayList<>();
        for (ExpedienteDTO expParti : listExpParticipantes) {
            ExpedienteDTO expParticipanteDTO = this.expedienteService
                    .buscarId(expParti.getIdExpediente());
            listExpedientesParticipantes.add(expParticipanteDTO);
        }

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposSubsanacionReq(dto,
                impresoCoordinadorDTO, listExpParticipantes);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.obtenerPlantillaSubsanacionReq(dto, hamCampos,
                impresoCoordinadorDTO, listExpedientesParticipantes, convocatoriaDTO);
        if (plantilla == null) {
            log.info(
                    " La plantilla para generar el documento firmado de presentación de subsanacion no se ha generado correctamente");
        }
        // ///////////////////////////////////////////
        byte[] t1 = this.crearFirmarArchivoPdfFirmado(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmado(Boolean.TRUE, dto, plantilla, hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDF(Boolean.TRUE, dto, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaReq(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaReq(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaReq(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaReq(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaReq(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la solicitud en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        this.insertarDocumento(dto, cdAnexoT2,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_EXPEDIENTE_FIRMADO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                Constantes.AfirmacionNegacion.N, Boolean.TRUE);
        log.info("Código de ereg subsanacion expediente: " + dto.getCdEregSubsanaReq());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info(
                "[PASO 8] - Generamos el listado de anexos que adjuntan a la presentacion de subsanacion requisitos");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico

        if (lstExpedienteDocDTO != null && !lstExpedienteDocDTO.isEmpty()) {
            this.generarFirmarListadoAnexos(dto,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO),
                    cdAsunto, lstExpedienteDocDTO);
        } else {
            log.info(
                    "No hay documentos para anexar a la presentacion de subsanacion requisitos de participantes y/o coordinadores");
        }
        Expediente expedienteFirmado = this.expedienteDAO.find(dto.getIdExpediente());

        // Asignamos el estado interno de revision de requisitos
        expedienteFirmado.setCdEregSubsanaReq(dto.getCdEregSubsanaReq());
        expedienteFirmado.setFcFirmaSubsanaReq(new Date());
        this.expedienteDAO.update(expedienteFirmado);
        this.expedienteHistDAO.crearHistorico(expedienteFirmado,
                solicitud.getInteresado().getTipoIdentificacion() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));

        for (ExpedienteDTO expHijo : listExpParticipantes) {
            if (expHijo.getEstadoRevRequiDTO().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_PRESENTACION_SUBSANACION)) {
                Expediente exp = this.expedienteService.find(expHijo.getIdExpediente());
                exp.setEstadoRevRequi(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)));
                exp.getEstadoRevRequi().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS);
                exp.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS)));
                exp.getEstado().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION_REQUISITOS);
                exp.setCdEregSubsanaReq(dto.getCdEregSubsanaReq());
                exp.setFcFirmaSubsanaReq(new Date());
                this.expedienteDAO.update(exp);
                this.expedienteHistDAO.crearHistorico(exp,
                        solicitud.getInteresado().getTipoIdentificacion() != null
                                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                        .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                                ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                                : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
            }
        }

    }

    @Override
    public byte[] obtenerPlantillaSubsanacionReq(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,
            ConvocatoriaDTO convocatoriaDTOIn) throws ServiceException {
        // Utilizaremos el crear pdf del impreso
        return this.impresoService.crearInformeSubsanacionReq(dto, impresoFormulario, mapaDatos,
                listExpedientesParticipantes, convocatoriaDTOIn);
    }

    @Override
    public DocumentoDTO descargarZipTramiteSubsanacionReq(Integer idExpedienteDoc)
            throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();
        ExpedienteDoc expedienteDoc = this.expedienteDocDAO.find(idExpedienteDoc);
        Expediente expediente = this.expedienteService
                .find(expedienteDoc.getExpediente().getIdExpediente());
        String cdEreg = expedienteDoc.getCdAnexoEreg();
        try {
            ExpedienteDTO expedienteDTO = ExpedienteToDTO.INSTANCE.apply(expediente);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipTramiteSubsanacionReq(expedienteDoc.getTramite().getIdValor(), cdEreg,
                    expedienteDTO, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO
                    .setNombreFichero(expedienteDTO.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip alegación, ", ioex);
            throw new ServiceException("Error al crear el zip subsanación requisitos.");
        }

        return documentoDTO;
    }

    /**
     * Método que rellena el zip con los archivos de la gestion de subsanacion.
     *
     * @param integer
     *
     * @param dto ExpedienteDoc
     * @param zos zip
     * @throws ServiceException
     */
    private void empaquetarZipTramiteSubsanacionReq(Integer idTramite, String cdEreg,
            ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {

        try {
            String cdExpediente = dto.getCdExpediente().replace("/", "_");
            String cdEregSubsana = dto.getCdEreg(); // para alegaciones coge el
                                                    // del expediente

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            if (StringUtils.isNotBlank(dto.getCdEregSubsanaReq())) {
                cdEregSubsana = dto.getCdEregSubsanaReq();
            }

            // Obtenemos los listados de recibos de presentacion
            List<byte[]> anexos = this.descargarListadoDocumentos(idTramite, dto, cdEregSubsana)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Insertamos los recibos al zip
            for (Integer i = 0; i < anexos.size(); i++) {
                ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                        + cdExpediente + " " + i + ".pdf");
                zos.putNextEntry(anexoZIP);
                zos.write(anexos.get(i));
            }

            // Combinar los filtros con OR
            List<AtlasFilter> filtroCompuesto = new ArrayList<>();
            filtroCompuesto.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    dto.getIdExpediente(), Operador.EQUALS));
            filtroCompuesto.add(new AtlasFilter("expediente.idExpedientePadre",
                    dto.getIdExpediente(), Operador.EQUALS));

            // Obtenemos la listado de documentos anexados
            List<AtlasFilter> filtros = new ArrayList<>();
            // Agregar el filtro compuesto a la lista
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    filtroCompuesto, Operador.OR));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_FCBAJA, Operador.NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDFICA, Operador.NOT_NULL));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CDANEXOEREGPADRE, cdEreg,
                    Operador.EQUALS));

            List<ExpedienteDoc> listaExpedienteDoc = this.expedienteDocDAO.find(filtros, null);

            for (ExpedienteDoc expedienteDoc : listaExpedienteDoc) {
                Integer cdFica = expedienteDoc.getCdFica();

                // Recuperamos de FICA el nombre del fichero
                FicaFichero ficaFichero = this.ficaService.recuperarMetadatos(cdFica);

                //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                String unicodeEscapedFilename = "";
                if (ficaFichero != null) {
                    String rawFilename = ficaFichero.getNombreFichero();
                    unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                }
                
                // Insertamos los documentos de FICA
                ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + cdFica + "_"
                        + unicodeEscapedFilename);
                zos.putNextEntry(ficaZIP);
                zos.write(this.ficaService.recuperar(cdFica).getDatosFichero());

            }

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Método que genera el documento PDF con el anexo, lo firma y lo sube a
     * EREG
     *
     * @param dto ExpedienteDTDo
     * @return String código del anexo
     * @throws ServiceException
     */
    private String generaFirmaReciboPresentacionReq(ExpedienteDTO dto, Integer idTramite,
            List<ExpedienteDocDTO> lstDoc) throws ServiceException {
        String cdEreg = dto.getCdEregSubsanaReq();
        if( utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA).equals(idTramite)) {
            cdEreg = dto.getCdEregSubsanaAdm();
        }

        // Se genera el listado de documentos y se anexa
        byte[] listadoDoc = this.generarAnexoDocumentacionSubsanacionReq(dto, idTramite, lstDoc);
        listadoDoc = this.eregAdmeService.firmarDatos(listadoDoc);
        log.info("Se va a anexar el listado. Tamaño: " + listadoDoc.length);

        return this.eregAdmeService.anexarFicheroAnexo(cdEreg, listadoDoc,
                EregAdmeService.ConstantesEregTipoAnexos.LISTADO_DOC_ANEXOS);

    }

    /**
     * Método para recuperar los documentos anexos al expediente
     *
     * @param expedienteDTO
     * @return byte[]
     * @throws ServiceException
     */
    private byte[] generarAnexoDocumentacionSubsanacionReq(ExpedienteDTO expedienteDTO,
            Integer idTramite, List<ExpedienteDocDTO> lstDoc) throws ServiceException {

        ImpresoDTO impresoDTO = null;
        // Recuperamos la convocatoria
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // Recuperamos el interesado
        InteresadoDTO interesado = this.interesadoService
                .buscarId(expedienteDTO.getInteresadoDTO().getIdInteresado());

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        // Recuperamos la plantilla por convocatoria y tramite
        ConvocatoriaPlaDTO convocatoriaPlaDTO = this.convocatoriaComService
                .obtenerConvocatoriaPla(convocatoriaDTO.getIdConvocatoria(), idTramite);
        
        if(convocatoriaPlaDTO== null || convocatoriaPlaDTO.getTramiteDTO()== null || convocatoriaPlaDTO.getTramiteDTO().getIdValor()==null) {
            CatalogosValoresDTO tramite = new CatalogosValoresDTO();
            tramite.setIdValor(idTramite);
            convocatoriaPlaDTO = new ConvocatoriaPlaDTO();
            convocatoriaPlaDTO.setTramiteDTO(tramite);
        }

        // Recuperamos el listado de documentos adjuntos del expediente
        List<DocumentoDTO> lstDocumentosDTO = null;

        lstDocumentosDTO = this.documentoService.obtenerDocumentosExpediente(expedienteDTO,
                Boolean.FALSE, idTramite, lstDoc);
        log.info("Documentos recuperados: " + lstDocumentosDTO.size());

        List<Integer> lstTipoImpreso = new ArrayList<>();
        lstTipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_SOLICITUD));
        impresoDTO = this.impresoService.buscarPorConvActor(convocatoriaDTO.getIdConvocatoria(),
                expedienteDTO.getConvocatoriaActDTO().getIdActor(), lstTipoImpreso);

        impresoDTO = impresoDTO == null ? new ImpresoDTO() : impresoDTO;

        log.info("Documentos pendientes presentar: " + lstDocumentosDTO.size());

        // Creamos el anexo
        return this.generarReciboSubsanacionReq(convocatoriaDTO, convocatoriaPlaDTO, null,
                expedienteDTO, interesado, lstDocumentosDTO, impresoDTO);
    }

    /**
     * Genera firma recibo presentacion administrativa
     * 
     * @param expedienteDTO
     * @param object
     * @param lstExpedienteDocDTOAux
     * @param lstExpedienteContrato
     * @return
     * @throws ServiceException
     */
    @Override
    @Transactional(readOnly = false)
    public String generaFirmaReciboPresentacionAdm(ExpedienteDTO dto, Object object,
            List<ExpedienteDocDTO> lstExpedienteDocDTO, List<ExpedienteDTO> lstExpParticipantes)
            throws ServiceException {
        String cdListadoDocEreg = null;
        dto.setFcFirmaSubsanaAdm(new Date());
        ImpresoDTO impresoCoordinadorDTO = null;
        if (dto.getImpresoDTO() != null
                && StringUtils.isNotBlank(dto.getImpresoDTO().getCdIdentificacion())) {
            impresoCoordinadorDTO = dto.getImpresoDTO();
            Integer indexContains = impresoCoordinadorDTO.getCdIdentificacion().indexOf('_');
            if (indexContains < 0) {
                dto.setCdAsunto(impresoCoordinadorDTO.getCdIdentificacion());
            } else {
                dto.setCdAsunto(
                        impresoCoordinadorDTO.getCdIdentificacion().substring(0, indexContains));
            }
        }

        String cdAsunto = dto.getCdAsunto();

        Solicitud solicitud = this.solicitudDAO.find(dto.getIdSolicitud());

        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info(
                "Se va a generar la entrada en EREG de presentacion de subsanacion administrativa");
        dto = this.eregAdmeService.generarEntradaSubsanarAdm(dto, cdAsunto, cdUnidad, Boolean.TRUE);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + dto.getCdEregSubsanaAdm());
        log.info("Destino: " + dto.getDsDestinoEreg());

        for (ExpedienteDTO expPartiDTO : lstExpParticipantes) {
            Expediente expPar = this.expedienteDAO.find(expPartiDTO.getIdExpediente());
            expPar.setCdEregSubsanaAdm(dto.getCdEregSubsanaAdm());
            expPar.setEstadoRevAdmin(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
            expPar.getEstadoRevAdmin().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
            expPar.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
            expPar.getEstado().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
            this.expedienteDAO.update(expPar);
            this.expedienteHistDAO.crearHistorico(expPar,
                    solicitud.getInteresado().getTipoIdentificacion() != null
                            && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                    .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                            ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                            : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
        }

        // Recuperamos la convocatoria
        ConvocatoriaDTO convocatoriaDTO = this.convocatoriaService
                .buscarId(solicitud.getConvocatoria().getIdConvocatoria());

        List<ExpedienteDTO> listExpedientesParticipantes = new ArrayList<>();
        for (ExpedienteDTO expParti : lstExpParticipantes) {
            ExpedienteDTO expParticipanteDTO = this.expedienteService
                    .buscarId(expParti.getIdExpediente());
            listExpedientesParticipantes.add(expParticipanteDTO);
        }

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposSubsanacionAdm(dto,
                impresoCoordinadorDTO, listExpedientesParticipantes);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.obtenerPlantillaSubsanacionAdm(dto, hamCampos,
                impresoCoordinadorDTO, listExpedientesParticipantes, convocatoriaDTO);
        if (plantilla == null) {
            log.info(
                    " La plantilla para generar el documento firmado de presentación de subsanacion no se ha generado correctamente");
        }
        // ///////////////////////////////////////////
        byte[] t1 = this.crearFirmarArchivoPdfFirmado(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmado(Boolean.TRUE, dto, plantilla, hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDF(Boolean.FALSE, dto, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDF(Boolean.TRUE, dto, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaAdm(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaAdm(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaAdm(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaAdm(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(dto.getCdEregSubsanaAdm(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la solicitud en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        this.insertarDocumento(dto, cdAnexoT2,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_EXPEDIENTE_FIRMADO),
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                Constantes.AfirmacionNegacion.N, Boolean.TRUE);
        log.info("Código de ereg expediente: " + dto.getCdEregSubsanaAdm());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info(
                "[PASO 8] - Generamos el listado de anexos que adjuntan a la presentacion de subsanación administrativa");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico

        if (lstExpedienteDocDTO != null && !lstExpedienteDocDTO.isEmpty()) {
            cdListadoDocEreg = this.generarFirmarListadoAnexos(dto,
                    utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.REQUERIMIENTO_SUBSANACION_ADMINISTRATIVA),
                    cdAsunto, lstExpedienteDocDTO);
        } else {
            log.info(
                    "No hay documentos para anexar a la presentacion de subsanacion administrativa de participantes y/o coordinadores");
        }

        Expediente expedienteFirmado = this.expedienteDAO.find(dto.getIdExpediente());
        expedienteFirmado.setCdEregSubsanaAdm(dto.getCdEregSubsanaAdm());
        expedienteFirmado.setFcFirmaSubsanaAdm(new Date());
        if(expedienteFirmado.getEstadoRevRequi().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_CONJUNTA_REQ_DOC)) {
            expedienteFirmado.setEstadoRevRequi(
                    new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
            expedienteFirmado.getEstadoRevRequi().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
           
        }
        this.expedienteDAO.update(expedienteFirmado);
        this.expedienteHistDAO.crearHistorico(expedienteFirmado,
                solicitud.getInteresado().getTipoIdentificacion() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
        for (ExpedienteDTO expHijo : listExpedientesParticipantes) {
            if (expHijo.getEstadoRevAdminDTO().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_PRESENTACION_SUBSANACION)) {
                Expediente exp = this.expedienteService.find(expHijo.getIdExpediente());
                exp.setEstadoRevAdmin(
                        new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
                exp.getEstadoRevAdmin().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
                exp.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
                exp.getEstado().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
                exp.setCdEregSubsanaAdm(dto.getCdEregSubsanaAdm());
                exp.setFcFirmaSubsanaAdm(new Date());
                
                if(exp.getEstadoRevRequi().getNmConstante().equals(Constantes.EstadoExpediente.PENDIENTE_SUBSANACION_CONJUNTA_REQ_DOC)) {
                    exp.setEstadoRevRequi(
                            new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION)));
                    exp.getEstadoRevRequi().setNmConstante(Constantes.EstadoExpediente.PENDIENTE_REVISION_SUBSANACION);
                   
                }
                

                this.expedienteDAO.update(exp);
                this.expedienteHistDAO.crearHistorico(exp,
                        solicitud.getInteresado().getTipoIdentificacion() != null
                                && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                        .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                                ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                                : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));
            }
        }
        return cdListadoDocEreg;
    }

    @Override
    public byte[] obtenerPlantillaSubsanacionAdm(ExpedienteDTO dto, Map<String, String> mapaDatos,
            ImpresoDTO impresoFormulario, List<ExpedienteDTO> listExpedientesParticipantes,
            ConvocatoriaDTO convocatoriaDTOIn) throws ServiceException {
        return this.impresoService.crearInformeSubsanacionReq(dto, impresoFormulario, mapaDatos,
                listExpedientesParticipantes, convocatoriaDTOIn);
    }

    @Override
    public void generaFirmaReciboPresentacionAceptacion(ExpedienteDTO expedienteDTO,
            List<ExpedienteDocDTO> listaExpedienteDocDTO) throws ServiceException {
       
        Date fechaFirmaEntrada=new Date();
        List<Integer> tipoImpreso = new ArrayList<>();
        ImpresoDTO impresoDTO = null;
       
        impresoDTO = expedienteDTO.getImpresoAceptacionDTO();
        tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));

        // Se recupera el asunto del dto
        String cdAsunto = expedienteDTO.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // VMM 08/10/24 MD-611: se coge como unidad admin el nivel que tenga la
        // convocatoria definido ya sea Area Admin, subdireccion o direccion
        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG de aceptación desistimiento");
        
        expedienteDTO = this.eregAdmeService.generarEntrada(expedienteDTO, cdAsunto, cdUnidad);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + expedienteDTO.getCdEreg());
        log.info("Destino: " + expedienteDTO.getDsDestinoEreg());
        expedienteDTO.setFcFirma(fechaFirmaEntrada);

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposAceptacion(expedienteDTO);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.impresoService.crearInforme(impresoDTO, hamCampos, expedienteDTO); 

        byte[] t1 = this.crearFirmarArchivoPdfFirmado(Boolean.FALSE, expedienteDTO, plantilla, hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmado(Boolean.TRUE, expedienteDTO, plantilla, hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDF(Boolean.FALSE, expedienteDTO, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDF(Boolean.FALSE, expedienteDTO, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDF(Boolean.TRUE, expedienteDTO, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la presentacion de aceptación en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        ExpedienteDoc expdteDocFormularioAceptacion = this.insertarDocumento(expedienteDTO, cdAnexoT2,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_ACEPTACION_DESIS_FIRMADO),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA),
                    Constantes.AfirmacionNegacion.N, Boolean.FALSE);
       
        expdteDocFormularioAceptacion.setCodigoEreg(expedienteDTO.getCdEreg());
        expdteDocFormularioAceptacion.setFcRegistro(fechaFirmaEntrada);
        
        this.expedienteDocDAO.insertOrUpdate(expdteDocFormularioAceptacion);

        log.info("Código de ereg expdteDocFormularioAceptacion: " + expedienteDTO.getCdEreg());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info(
                "[PASO 8] - Generamos el listado de anexos que adjuntan a aceptación o desistimiento de la ayuda concedida");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico
        
        this.generarFirmarListadoAnexos(expedienteDTO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA),
                null, listaExpedienteDocDTO);
        
     // Actualizamos los estados del expediente
        Expediente expedienteFirmado = this.expedienteDAO.find(expedienteDTO.getIdExpediente());

         
        expedienteFirmado.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpediente.CONCEDIDO_DEFINITIVO_PENDIENTE_REVISION_ACEPTACION_DESIST)));

        this.expedienteDAO.update(expedienteFirmado);
        this.expedienteHistDAO.crearHistorico(expedienteFirmado,
                solicitud.getInteresado().getTipoIdentificacion() != null
                        && utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoIdentificacion.EXTRA_COMUNITARIO)
                                .equals(solicitud.getInteresado().getTipoIdentificacion().getIdValor())
                        ? Constantes.Identificacion.CD_USU_EXTRACOMUNITARIO
                        : Utils.valorAuditoria(solicitud.getInteresado().getCdNumIden()));

        
    }
    
    
    private void empaquetarZipAceptacion(ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {

        try {

            // Obtenemos el impreso relleno de aceptación
            byte[] impreso = this.descargarFormularioAceptacion(dto).getContenidoFichero();
            log.info("impreso aceptacion: " + Base64.getEncoder().encodeToString(impreso));
            String cdEregEXP = dto.getCdEreg();
            
            List<DocumentoDTO> listaDoc = this.descargarListadoDocumentos(utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA), dto, cdEregEXP);
            List<byte[]> anexos = listaDoc.stream().map(DocumentoDTO::getContenidoFichero)
                    .collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService
                    .obtenerDocumentosExpediente(dto, Boolean.TRUE, utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.ACEPTACION_DESISTIMIENTO_AYUDA), null);

            String cdExpediente = dto.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);

            // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
            // que no de error hasta que arreglemos lo de EREG
            if (impreso != null) {
                zos.write(impreso);
            } else {
                log.error("No se ha encontrado el impreso relleno del expediente");
            }

            // VMM 25/09/24: ponemos un control aqui si los anexos son vacios
            // para que no de error hasta que arreglemos lo de EREG
            // Insertamos los anexos
            if (!anexos.isEmpty()) {
                for (Integer i = 0; i < anexos.size(); i++) {
                    ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                            + cdExpediente + " " + i + ".pdf");
                    zos.putNextEntry(anexoZIP);
                    zos.write(anexos.get(i));
                }
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
    }

    
    private DocumentoDTO descargarFormularioAceptacion(ExpedienteDTO expedienteDTO) throws ServiceException {
        byte[] expedientePdf = null;

        // Si hay código de EREG lo descargamos de EREG
        if (StringUtils.isNotBlank(expedienteDTO.getCdEreg())) {
            // Obtenemos el código de ereg del impreso
            List<AtlasFilter> filtros = new ArrayList<>();
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    expedienteDTO.getIdExpediente(), Operador.EQUALS));
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_ACEPTACION_DESIS_FIRMADO), Operador.EQUALS));

            List<ExpedienteDoc> listaExpedientes = this.expedienteDocDAO.find(filtros, null);

            if (!listaExpedientes.isEmpty()) {
                // Recuperamos el pdf
                expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(
                        listaExpedientes.get(0).getCodigoEreg(), listaExpedientes.get(0).getCdAnexoEreg());
            }

        } 

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(
                    expedienteDTO.getCdExpediente() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    @Override
    public DocumentoDTO descargarAceptacion(Integer idExpediente) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);
           
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipAceptacion(dto, zos);
            

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(idExpediente + "_aceptacion.zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip de aceptacion o desistimiento de la ayuda concedida, ", ioex);
            throw new ServiceException("Error al crear el zip aceptacion o desistimiento de la ayuda concedida.");
        }

        return documentoDTO;
    }

    @Override
    public void generaFirmaReciboPresentacionJusEco(ExpedienteDTO expedienteDTO,
            List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO) throws ServiceException {
        
        Date fechaFirmaEntrada=new Date();
       // List<Integer> tipoImpreso = new ArrayList<>();
        ImpresoDTO impresoDTO = expedienteDTO.getImpresoDTO();
       // tipoImpreso.add(utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoImpreso.FORMULARIO_ACEPTACION_AYUDA));

        // Se recupera el asunto del dto
        String cdAsunto = expedienteDTO.getCdAsunto();

        // Se recupera el código de la unidad administrativa
        Solicitud solicitud = this.solicitudDAO.find(expedienteDTO.getIdSolicitud());

        // VMM 08/10/24 MD-611: se coge como unidad admin el nivel que tenga la
        // convocatoria definido ya sea Area Admin, subdireccion o direccion
        Integer unidadAdmin = this.calcularUnidadAdmin(solicitud);

        UnidadAdminDTO unidadAdminDTO = this.unidadAdminService.buscarId(unidadAdmin);

        String cdUnidad = unidadAdminDTO.getCdUnidad();

        // **********************************************************//
        // 1.- Registro de Entrada a EREG.
        // **********************************************************/
        log.info("[PASO 1] - Registro de entrada en EREG");
        log.info("Se va a generar la entrada en EREG de justificación económica");
        
        expedienteDTO = this.eregAdmeService.generarEntrada(expedienteDTO, cdAsunto, cdUnidad);
        log.info("Se ha generado la entrada: ");
        log.info("Código: " + expedienteDTO.getCdEreg());
        log.info("Destino: " + expedienteDTO.getDsDestinoEreg());
        expedienteDTO.setFcFirma(fechaFirmaEntrada);

        // Obtenemos todos los campos a fusionar
        HashMap<String, String> hamCampos = this.pdfCamposService.rellenarCamposJusEco(expedienteDTO, listExpedienteJusEcoDTO);

        // Recuperamos la plantilla del impreso
        byte[] plantilla = this.impresoService.crearInformeJusEco(impresoDTO, hamCampos, expedienteDTO,listExpedienteJusEcoDTO); 

        byte[] t1 = this.crearFirmarArchivoPdfFirmado(Boolean.FALSE, expedienteDTO, plantilla, hamCampos);
        byte[] t2 = this.crearFirmarArchivoPdfFirmado(Boolean.TRUE, expedienteDTO, plantilla, hamCampos);
        byte[] t3 = this.crearFirmarArchivoFDF(Boolean.FALSE, expedienteDTO, plantilla, hamCampos);
        byte[] t4 = this.crearFirmarArchivoXFDF(Boolean.FALSE, expedienteDTO, plantilla, hamCampos);
        byte[] t5 = this.crearFirmarArchivoXFDF(Boolean.TRUE, expedienteDTO, plantilla, hamCampos);
        // ///////////////////////////////////////////

        // **********************************************************//
        // 2.- T1 - PDF firmado sin la información de EREG
        // **********************************************************//
        log.info("[PASO 2] - Anexo del T1 - PDF firmado sin la información de EREG");
        String cdAnexoT1 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t1,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T1: " + cdAnexoT1);

        // **********************************************************//
        // 3.- T2 - PDF firmado con la información de EREG
        // **********************************************************//
        log.info("[PASO 3] - Anexo del T2 - PDF firmado con la información de EREG");
        String cdAnexoT2 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t2,
                EregAdmeService.ConstantesEregTipoDoc.PDF_SOLICITUD_EREG_FIRMADO,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T2: " + cdAnexoT2);

        // **********************************************************//
        // 4.- T3 - FDF de la solicitud
        // **********************************************************//
        log.info("[PASO 4] - Anexo del T3 - FDF de la solicitud");
        String cdAnexoT3 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t3,
                EregAdmeService.ConstantesEregTipoDoc.FDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T3: " + cdAnexoT3);

        // **********************************************************//
        // 5.- T4 - XFDF de la solicitud sin EREG
        // **********************************************************//
        log.info("[PASO 5] - Anexo del T4 - XFDF de la solicitud sin EREG");
        String cdAnexoT4 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t4,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T4: " + cdAnexoT4);

        // **********************************************************//
        // 6.- T5 - XFDF de la solicitud con EREG
        // **********************************************************//
        log.info("[PASO 6] - Anexo del T5 - XFDF de la solicitud con EREG");
        String cdAnexoT5 = this.eregAdmeService.anexarFicherosDocum(
                expedienteDTO.getCdEreg(), t5,
                EregAdmeService.ConstantesEregTipoDoc.XFDF_SOLICITUD_EREG,
                Constantes.AplicativoSaii.SAII_REST_SOLICITUD);
        log.info("Código de anexo del T5: " + cdAnexoT5);

        // **********************************************************//
        // 7.- Insertamos la solicitud en la tabla de documentos
        // **********************************************************//
        log.info("[PASO 7] - Insertamos la presentacion de Justificación económica en la tabla de documentos");

        // Creamos el documento del impreso usando el documento con datos
        // ereg
        ExpedienteDoc expdteDocFormularioJusEco = this.insertarDocumento(expedienteDTO, cdAnexoT2,
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_JUSECO_FIRMADO),
                    utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA),
                    Constantes.AfirmacionNegacion.N, Boolean.FALSE);
       
        expdteDocFormularioJusEco.setCodigoEreg(expedienteDTO.getCdEreg());
        expdteDocFormularioJusEco.setFcRegistro(fechaFirmaEntrada);
        
        this.expedienteDocDAO.insertOrUpdate(expdteDocFormularioJusEco);

        log.info("Código de ereg expdteDocFormularioJusEco: " + expedienteDTO.getCdEreg());
        log.info("Código de anexo documento: " + cdAnexoT2);

        // **********************************************************//
        // 8.- Generamos el listado de anexos que adjuntan a la solicitud
        // **********************************************************//
        log.info(
                "[PASO 8] - Generamos el listado de anexos que adjuntan a la justificación económica");
        // Generamos el listado de anexos, lo firmamos, lo subimos a EREG y
        // actualizamos las tabla de expedienteDoc con histórico
        
        List<ExpedienteDocDTO> listaExpedienteDocDTO= new ArrayList<>();
        for(ExpedienteJusEcoDocDTO item:listExpedienteJusEcoDTO) {
            listaExpedienteDocDTO.add(item.getExpedienteDocDTO());
        }
        
        listaExpedienteDocDTO.removeIf(x -> x.getCdFica() == null || x.getFcBajaFila() != null);
        this.generarFirmarListadoAnexos(expedienteDTO,
                utilConstantes.getGlobalCatalogosValores().get(Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA),
                null, listaExpedienteDocDTO);
        
     // Actualizamos los estados del periodo de JUSECO 
        
        if(!listExpedienteJusEcoDTO.isEmpty()) {
            ExpedienteJusEco  expedienteJusEco= expedienteJusEcoService.find(listExpedienteJusEcoDTO.get(0).getExpedienteJusEcoDTO().getIdExpedienteJusEco());
            expedienteJusEco.setIdEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)));
            expedienteJusEco.setIdEstadoRevAdmin(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)));
            expedienteJusEco.setCdEregPresent(expedienteDTO.getCdEreg());
            expedienteJusEco.setFcFirmaPresent(fechaFirmaEntrada);
            expedienteJusEcoService.update(expedienteJusEco);
            
        for(ExpedienteJusEcoDocDTO item: listExpedienteJusEcoDTO){
            
            ExpedientePresupMonto  expedientePresupMonto =this.expedientePresupMontoService.find(item.getExpedientePresupMontoDTO().getIdExpedientePresupMonto());
           
            expedientePresupMonto.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)));
            expedientePresupMonto.setNmImporteJustificado(item.getExpedientePresupMontoDTO().getNmImporteJustificado());
            expedientePresupMontoService.getDao().update(expedientePresupMonto);
            ExpedientePresup expedientePresup= this.expedientePresupService.find(item.getExpedientePresupMontoDTO().getIdExpedientePresup());
            expedientePresup.setEstado(new CatalogosValores(utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoExpedienteJusEco.PENDIENTE_DE_REVISION_ADM)));
            this.expedientePresupService.getDao().update(expedientePresup);
            expedienteJusEcoDocService.insertOrUpdate(ExpedienteJusEcoDocToEntity.INSTANCE.apply(item));
            
        }
        
        }
        
    }


    @Override
    public DocumentoDTO descargarZipExpedienteJusEco(Integer idExpedienteJusEco)
            throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            ExpedienteJusEco expedienteJusEco =expedienteJusEcoService.find(idExpedienteJusEco);
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(expedienteJusEco.getIdExpediente());
           

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            this.empaquetarZipExpedienteJusEco(dto, expedienteJusEco, zos);

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(dto.getCdExpediente().replace("/", "_") + ".zip");

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;
    }

    private void empaquetarZipExpedienteJusEco(ExpedienteDTO dto, ExpedienteJusEco expedienteJusEco,
            ZipOutputStream zos) throws ServiceException {
        
        try {

            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpedienteJusEco(dto, expedienteJusEco)
                    .getContenidoFichero();
            log.info("impreso JusEco: " + Base64.getEncoder().encodeToString(impreso));
            String cdEREGPresent = expedienteJusEco.getCdEregPresent();

            // Obtenemos los listados de anexos de cada expediente
            List<byte[]> anexos = this
                    .descargarListadoDocumentos(utilConstantes.getGlobalCatalogosValores().get(
                            Constantes.Tramite.TRAMITE_PRESENTACION_JUSTIFICACION_ECONOMICA), dto,
                            cdEREGPresent)
                    .stream().map(DocumentoDTO::getContenidoFichero).collect(Collectors.toList());

            // Obtenemos los documentos subidos a FICA de cada expediente justec
           List<ExpedienteJusEcoDocDTO> listaExpJusEcoDoc= this.expedienteJusEcoDocService.buscarPorExpedienteJuseco(expedienteJusEco.getIdExpedienteJusEco());
           List<ExpedienteDocDTO> listaDocs= new ArrayList<>();
           if(!listaExpJusEcoDoc.isEmpty()) {
               for(ExpedienteJusEcoDocDTO item:listaExpJusEcoDoc) {
                   listaDocs.add(item.getExpedienteDocDTO());
               }
           }
           
            List<DocumentoDTO> lstDocumentosFICA = this.documentoService.obtenerDocumentosExpedienteJusEco(listaDocs);

            String cdExpediente = dto.getCdExpediente().replace("/", "_").replace(" ", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);
            if (impreso != null) {
                zos.write(impreso);
            }

            // Insertamos los anexos
            for (Integer i = 0; i < anexos.size(); i++) {
                ZipEntry anexoZIP = new ZipEntry(cdExpediente + "/" + "Recibo de presentacion "
                        + cdExpediente + " " + i + ".pdf");
                zos.putNextEntry(anexoZIP);
                zos.write(anexos.get(i));
            }

            if (Boolean.FALSE.equals(lstDocumentosFICA.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosFICA) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(cdExpediente + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(e.getMessage());
        }
        
    }
    
    private DocumentoDTO descargarExpedienteJusEco(ExpedienteDTO dto, ExpedienteJusEco expedienteJusEco)
            throws ServiceException {
        byte[] expedientePdf = null;
        List<AtlasFilter> filtros = new ArrayList<>();
        
       

        // VMM 25/10/24 MD-913: descargamos con el nuevo EREG de la JCT
        // Si hay código de EREG lo descargamos de EREG
        if (StringUtils.isNotBlank(expedienteJusEco.getCdEregPresent())) {

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_EXPEDIENTE_IDEXPEDIENTE,
                    dto.getIdExpediente(), Operador.EQUALS));

            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_COMPONENTE_TIPO_DOCUMENTO,
                        utilConstantes.getGlobalCatalogosValores().get(Constantes.TipoDocumento.PDF_FORM_JUSECO_FIRMADO),
                        Operador.EQUALS));
            
            filtros.add(new AtlasFilter(Constantes.Filtro.FILTRO_CODIGO_EREG,
                    expedienteJusEco.getCdEregPresent(),
                    Operador.EQUALS));
           

            // Obtenemos el código de ereg del impreso
            List<ExpedienteDoc> listaExpedientesDoc = this.expedienteDocDAO.find(filtros, null);

            if (!listaExpedientesDoc.isEmpty()) {
                // Recuperamos el pdf
                expedientePdf = this.eregAdmeService.descargarAnexoPorCodigo(
                        expedienteJusEco.getCdEregPresent(),
                        listaExpedientesDoc.get(0).getCdAnexoEreg());
            }

        } 

        DocumentoDTO documento = new DocumentoDTO();
        if (Objects.nonNull(expedientePdf)) {
            documento.setContenidoFichero(expedientePdf);
            documento.setNombreFichero(
                    dto.getCdExpediente() + "." + Constantes.Extensiones.PDF);
        }

        return documento;
    }

    @Override
    public DocumentoDTO descargarFormulariosSolicitudExpediente(Integer idExpediente)
            throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {
            // Obtenemos el expediente
            ExpedienteDTO dto = this.expedienteService.buscarId(idExpediente);

            if (dto != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                ZipOutputStream zos = new ZipOutputStream(baos);

                this.empaquetarFormularioSolictudZip(dto, zos);

                // Agregamos expedientes hijos
                List<ExpedienteDTO> hijosDto = new ArrayList<>();
                if (dto.getIdSolicitud() != null) {
                    hijosDto = this.expedienteService
                            .buscarExpedientePorExpedientePadre(dto.getIdSolicitud(), idExpediente);
                }
                for (ExpedienteDTO hijoDto : hijosDto) {
                    // VMM 05/12/24 MD-1066: obtenemos los datos de los impresos
                    // de los expdtes hijos
                    ExpedienteDTO expdteHijoDTO = this.expedienteService
                            .buscarId(hijoDto.getIdExpediente());
                    this.empaquetarFormularioSolictudZip(expdteHijoDTO, zos);
                }

                zos.closeEntry();
                zos.close();
                String nombreDocu = dto.getCdExpediente();
                if (nombreDocu != null && nombreDocu.indexOf("/") != -1) {
                    nombreDocu = nombreDocu.replace("/", "_") + ".zip";
                }
                documentoDTO.setNombreFichero(nombreDocu);
                documentoDTO.setContenidoFichero(baos.toByteArray());

            }

        } catch (Exception ioex) {
            log.error(ioex.getMessage());
            throw new ServiceException("Error al crear el zip expediente.");
        }

        return documentoDTO;
    }

    private void empaquetarFormularioSolictudZip(ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {

        try {

            // Obtenemos el impreso relleno de cada expediente
            byte[] impreso = this.descargarExpediente(dto).getContenidoFichero();
            log.info("impreso solicitud: " + Base64.getEncoder().encodeToString(impreso));
            
            String cdExpediente = dto.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);

            // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
            // que no de error hasta que arreglemos lo de EREG
            if (impreso != null) {
                zos.write(impreso);
            } else {
                log.error("No se ha encontrado el impreso relleno del expediente");
            }

           
        } catch (IOException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public Document datosFirmanteEvalCoor(Document documentInforme, String tituloFirma, String datoFirma) throws ServiceException {
        try {
            this.informeHelperPdfService.generarFirmaAutor(documentInforme,tituloFirma,datoFirma);
         }catch ( DocumentException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
        return documentInforme;
       
    }

    @Override
    public byte[] generarFirmaInformeEvalCoor(byte[] byteArray) throws ServiceException {
       
        return this.eregAdmeService.firmarDatos(byteArray);
       
        
    }

    @Override
    public DocumentoDTO descargarInforme(Integer cdFica) throws ServiceException {
        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            // Obtenemos los documentos de evaluación relacionados con el InformeEvalDoc
            List<ExpedienteDocEvalDTO> lstExpedientesDocEvalDTO = this.expedienteEvalService.obtenerListaDocEvalPorCdFicaInformeEvalDoc(cdFica != null ? String.valueOf(cdFica) : null);
                    
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);
            
            if(lstExpedientesDocEvalDTO != null && !lstExpedientesDocEvalDTO.isEmpty()) {
                this.empaquetarZipInformeEvaluacion(cdFica,lstExpedientesDocEvalDTO, zos);
            }else {
                // cuando el tipo informe no tiene documentos.
                lstExpedientesDocEvalDTO = new ArrayList<>();
                this.empaquetarZipInformeEvaluacion(cdFica,lstExpedientesDocEvalDTO, zos);
            }

           
            

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero("informe_"+cdFica + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip descargarInforme, ", ioex);
            throw new ServiceException("Error al crear el zip informe de evaluación.");
        }

        return documentoDTO;
    }

    private void empaquetarZipInformeEvaluacion(Integer cdFicaInforme,List<ExpedienteDocEvalDTO> listaDocExpEval, ZipOutputStream zos) throws ServiceException {
     // Obtenemos los documentos subidos a FICA de cada expediente
       
        try {
        
            List<DocumentoDTO> lstDocumentosExpediente = new ArrayList<>();
            //Recuperamos documentos de la lista facilitada
            if(!listaDocExpEval.isEmpty()) {
                for (ExpedienteDocEvalDTO expedienteDocEvalDTO : listaDocExpEval) {
                    Integer cdFica = expedienteDocEvalDTO.getCdFica();

                    // Recuperamos de FICA el nombre del fichero
                    FicaFichero ficaFichero=null;
                    if(cdFica != null) {
                        ficaFichero = this.ficaService.recuperarMetadatos(cdFica);
                    }
                    
                    
                    //VMM 19/05/25 MD-1531: corregimos los nombres del fichero que llegan de FICA con caracteres especiales o tildes...
                    String unicodeEscapedFilename = "";
                    if (ficaFichero != null) {
                        String rawFilename = ficaFichero.getNombreFichero();
                        unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
                    }

                    // Guardamos el id (cdFica) y nombre del fichero en la lista
                    DocumentoDTO documentoDTO = new DocumentoDTO();

                    documentoDTO.setCdFica(cdFica);
                    documentoDTO.setNombreFichero(unicodeEscapedFilename);
                    documentoDTO.setTipoDoc(expedienteDocEvalDTO.getIdTipoDocumento().getDsValor());

                    // Guardamos el contenido del fichero
                    if (cdFica != null) {
                        documentoDTO
                                .setContenidoFichero(this.ficaService.recuperar(cdFica).getDatosFichero());
                    }

                    lstDocumentosExpediente.add(documentoDTO);

                }
            }
           
            
            // empezar a empaquetar
            String nombreZip="informe_evaluacion";
            ZipEntry carpetaZIP = new ZipEntry(nombreZip+ "/");
            zos.putNextEntry(carpetaZIP);
           
            FicaFichero ficaFichero=null;
            if(cdFicaInforme != null) {
                ficaFichero = this.ficaService.recuperarMetadatos(cdFicaInforme);
            }
            
            String unicodeEscapedFilename = "";
            if (ficaFichero != null) {
                String rawFilename = ficaFichero.getNombreFichero();
                unicodeEscapedFilename = this.expedienteService.toUnicodeEscaped(rawFilename);
            }

            // Guardamos el id (cdFica) y nombre del fichero en la lista
            DocumentoDTO documentoDTO = new DocumentoDTO();

            documentoDTO.setCdFica(cdFicaInforme);
            documentoDTO.setNombreFichero(unicodeEscapedFilename);
            documentoDTO.setTipoDoc(nombreZip);

            // Guardamos el contenido del fichero
            if (cdFicaInforme != null) {
                documentoDTO
                        .setContenidoFichero(this.ficaService.recuperar(cdFicaInforme).getDatosFichero());
            }
            
            if (documentoDTO != null && documentoDTO.getContenidoFichero() != null) {
             // Insertamos el impreso
                ZipEntry impresoZIP = new ZipEntry(nombreZip+ "/" + documentoDTO.getNombreFichero());
                zos.putNextEntry(impresoZIP);
                zos.write(documentoDTO.getContenidoFichero());
            } else {
                log.error("No se ha encontrado el informe relleno de evaluación");
            }

            
            if (Boolean.FALSE.equals(lstDocumentosExpediente.isEmpty())) {

                for (DocumentoDTO fica : lstDocumentosExpediente) {
                    // Insertamos los documentos de FICA
                    ZipEntry ficaZIP = new ZipEntry(nombreZip + "/documentos/" + fica.getCdFica()
                            + "_" + fica.getNombreFichero());
                    zos.putNextEntry(ficaZIP);
                    zos.write(fica.getContenidoFichero());
                }

            }
        } catch (IOException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
        
    }


  @Override
    public DocumentoDTO descargarZipBorradorSolicitud(Integer idSolicitud) throws ServiceException {

        DocumentoDTO documentoDTO = new DocumentoDTO();

        try {

            // Obtenemos los expedientes de la solicitud
            List<ExpedienteDTO> lstExpedientesDTO = this.expedienteService
                    .obtenerExpedientesPorSolicitud(idSolicitud);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ZipOutputStream zos = new ZipOutputStream(baos);

            for (ExpedienteDTO dto : lstExpedientesDTO) {
                this.empaquetarBorradorZip(dto, zos);
            }

            zos.closeEntry();
            zos.close();

            documentoDTO.setContenidoFichero(baos.toByteArray());
            documentoDTO.setNombreFichero(idSolicitud + ".zip");

        } catch (Exception ioex) {
            log.error("Error al crear el zip solicitud, ", ioex);
            throw new ServiceException("Error al crear el zip solicitud.");
        }

        return documentoDTO;
    }
    
    private void empaquetarBorradorZip(ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {

        try {

            // Obtenemos el impreso relleno de cada expediente
            dto.setCdEreg(null);
            byte[] impreso = this.descargarExpediente(dto).getContenidoFichero();
            log.debug("impreso solicitud: " + Base64.getEncoder().encodeToString(impreso));
           
            String cdExpediente = dto.getCdExpediente().replace("/", "_");

            // Creamos la carpeta para el expediente
            ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
            zos.putNextEntry(carpetaZIP);

            // Insertamos el impreso
            ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
            zos.putNextEntry(impresoZIP);

            // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
            // que no de error hasta que arreglemos lo de EREG
            if (impreso != null) {
                zos.write(impreso);
            } else {
                log.error("No se ha encontrado el impreso relleno del expediente");
            }

           
        } catch (IOException e) {
            log.error(e.getMessage());
            // throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public DocumentoDTO descargarReciboFirmadoSolicitud(Integer idSolicitud) throws ServiceException {
    DocumentoDTO documentoDTO = new DocumentoDTO();

    try {

        // Obtenemos los expedientes de la solicitud
        List<ExpedienteDTO> lstExpedientesDTO = this.expedienteService
                .obtenerExpedientesPorSolicitud(idSolicitud);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        ZipOutputStream zos = new ZipOutputStream(baos);

        for (ExpedienteDTO dto : lstExpedientesDTO) {
            this.empaquetarReciboFirmadoZip(dto, zos);
        }

        zos.closeEntry();
        zos.close();

        documentoDTO.setContenidoFichero(baos.toByteArray());
        documentoDTO.setNombreFichero(idSolicitud + ".zip");

    } catch (Exception ioex) {
        log.error("Error al crear el zip solicitud, ", ioex);
        throw new ServiceException("Error al crear el zip solicitud.");
    }

    return documentoDTO;
    }


private void empaquetarReciboFirmadoZip(ExpedienteDTO dto, ZipOutputStream zos) throws ServiceException {

    try {

        // Obtenemos el impreso relleno de cada expediente
        byte[] impreso = this.descargarExpediente(dto).getContenidoFichero();
        log.debug("impreso solicitud: " + Base64.getEncoder().encodeToString(impreso));
       
        String cdExpediente = dto.getCdExpediente().replace("/", "_");

        // Creamos la carpeta para el expediente
        ZipEntry carpetaZIP = new ZipEntry(cdExpediente + "/");
        zos.putNextEntry(carpetaZIP);

        // Insertamos el impreso
        ZipEntry impresoZIP = new ZipEntry(cdExpediente + "/" + cdExpediente + ".pdf");
        zos.putNextEntry(impresoZIP);

        // VMM 25/09/24: ponemos un control aqui si el impreso es nulo para
        // que no de error hasta que arreglemos lo de EREG
        if (impreso != null) {
            zos.write(impreso);
        } else {
            log.error("No se ha encontrado el impreso relleno del expediente");
        }

       
    } catch (IOException e) {
        log.error(e.getMessage());
        // throw new ServiceException(e.getMessage());
    }
}


}
