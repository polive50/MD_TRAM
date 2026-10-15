package org.madrid.saii.lib.tramite;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de tramite Requerimiento de Subsanación - Borrador Acta del Comité de
 * Gestión
 *
 * @author nttdata
 *
 */
@Slf4j
public class TramiteRequerimientoSubsanacionBorradorActaComiteGestion extends Tramite {
    @Override
    public byte[] formularioPdf(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria) {
        byte[] byteSalida = null;
        try {

            Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PdfWriter writer = PdfWriter.getInstance(document, baos);

            CatalogosValoresDTO catalogoValor = null;

            this.getPdfService().generarCabeceraPdfTramite(convocatoriaDTO, convocatoriaPlaDTO,
                    writer, Boolean.FALSE);

            catalogoValor = Objects.nonNull(expedienteDTO)
                    ? this.catalogosValoresService
                            .buscarNmConstante(expedienteDTO.getInteresadoDTO().getTipoActorDTO().getNmConstante())
                    : null;

            document.open();
            String desImpreso=  Objects.nonNull(expedienteDTO) && Objects.nonNull(expedienteDTO.getImpresoDTO())
                    ? expedienteDTO.getImpresoDTO().getDsDescripcion()
                    : "";
            String cdExpediente=  Objects.nonNull(expedienteDTO)
                     ? expedienteDTO.getCdExpediente()
                                     : "";
            InformeHelperPdfService informeHelperPdfService = ApplicationContextProvider.getBean(InformeHelperPdfService.class);
            informeHelperPdfService.agregarTitulo(document, Constantes.Plantilla.TEXTO_TITULO,
                    Objects.isNull(convocatoriaPlaDTO.getDsPlantilla())
                            || convocatoriaPlaDTO.getDsPlantilla().isEmpty()
                                    ? convocatoriaPlaDTO.getTramiteDTO().getDsValor()
                                    : convocatoriaPlaDTO.getDsPlantilla(),desImpreso,cdExpediente,
                    null, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);
            String dsCatalogo = Objects.nonNull(catalogoValor) ? catalogoValor.getDsValor() : "";

            StringBuilder dsConvocatoria = new StringBuilder();
            dsConvocatoria.append(convocatoriaDTO.getCdAcronimo()).append(". ");
            dsConvocatoria.append(convocatoriaDTO.getCdTitulo()).append(". ");
            if (StringUtils.isNotBlank(convocatoriaDTO.getIdModalidadConvocatoria().getDsValor())) {
                dsConvocatoria.append(convocatoriaDTO.getIdModalidadConvocatoria().getDsValor());
            }

            String dsExpediente = Objects.nonNull(expedienteDTO)
                    ? expedienteDTO.getCdExpediente() + ". "
                            + expedienteDTO.getInteresadoDTO().getCdNumIden() + " - "
                            + expedienteDTO.getInteresadoDTO().getDsNombre() + " "
                            + expedienteDTO.getInteresadoDTO().getDsApellido1() + " "
                            + expedienteDTO.getInteresadoDTO().getDsApellido2() + ". " + dsCatalogo
                    : "";

            informeHelperPdfService.agregarSubTituloPlantilla(document,
                    Constantes.Plantilla.TEXTO_CONVOCATORIA, dsConvocatoria.toString(), 11, null);
            informeHelperPdfService.agregarSubTituloPlantilla(document,
                    Constantes.Plantilla.TEXTO_PROCEDIMIENTO,
                    convocatoriaPlaDTO.getTramiteDTO().getDsValor(), 12, null);
            informeHelperPdfService.agregarSubTituloPlantilla(document,
                    Constantes.Plantilla.TEXTO_EXPEDIENTE, dsExpediente, 9, null);

            if (Objects.nonNull(expedienteDTO)
                    && Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())) {
                List<ExpedienteDTO> lstExpediente = this.expedienteService
                        .buscarParticipantesPorSolicitud(expedienteDTO.getIdSolicitud(), null);
                if (!lstExpediente.isEmpty()) {
					informeHelperPdfService.agregarSubTituloPlantilla(document,
                        Constantes.Plantilla.TEXTO_SUB_EXPEDIENTE, null, 15, null);
                    for (ExpedienteDTO expeDTO : lstExpediente) {
                        CatalogosValoresDTO catalogoValorDTO = this.catalogosValoresService
                                .buscarNmConstante(expeDTO.getInteresadoDTO().getTipoActorDTO().getNmConstante());
                        String dsExpe = expeDTO.getCdExpediente() + ". "
                                + expeDTO.getInteresadoDTO().getCdNumIden() + ". "
                                + expeDTO.getInteresadoDTO().getDsNombre() + " "
                                + expeDTO.getInteresadoDTO().getDsApellido1() + " "
                                + expeDTO.getInteresadoDTO().getDsApellido2() + ". "
                                + catalogoValorDTO.getDsValor();
                        informeHelperPdfService.agregarSubTituloPlantilla(document, "", dsExpe,
                                Constantes.Plantilla.TEXTO_SUB_EXPEDIENTE.length() - 3, null);
                    }
                }

            }
            informeHelperPdfService.agregarTituloCabeceraFecha(document, Constantes.Evaluacion.TEXTO_FECHA,
                    new SimpleDateFormat("dd/MM/yyyy").format(new Date()));

            informeHelperPdfService.agregarFilaVacio(document, 1);

            // VMM 14/10/26 MD-3142: texto superior + inferior (el inferior es opcional)
            String descripcion = this.textoDescripcionUnificado(convocatoriaPlaDTO);
            informeHelperPdfService.agregarTextoEnriquecido(document, "", descripcion, 0, true);

            informeHelperPdfService.agregarFilaVacio(document, 1);
            informeHelperPdfService.agregarContenidoFinal(document, writer, convocatoriaPlaDTO,
                    convocatoriaDTO);

            document.close();

            byteSalida = baos.toByteArray();

        } catch (ServiceException | DocumentException e) {
            log.error(e.getMessage());
        }

        return byteSalida;
    }

    @Override
    public String formularioHtml(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria) {
        StringBuilder strCuerpoHtml = new StringBuilder();

        CatalogosValoresDTO catalogoValor = null;
        try {
            catalogoValor = Objects.nonNull(expedienteDTO)
                    ? this.catalogosValoresService
                            .buscarNmConstante(expedienteDTO.getInteresadoDTO().getTipoActorDTO().getNmConstante())
                    : null;
        } catch (ServiceException e) {
            log.error(e.getMessage());
        }

        // Usar etiquetas y css básicos
        strCuerpoHtml.append("<!DOCTYPE html>");
        strCuerpoHtml.append("<html lang=\"es\">");
        strCuerpoHtml.append("<head>");
        strCuerpoHtml.append("<title>Exclusion de requisitos</title>");
        strCuerpoHtml.append("<style>");
        strCuerpoHtml.append("body { font-family: sans-serif; }");
        strCuerpoHtml.append("table { border-collapse: collapse; width: 100%; }");
        strCuerpoHtml.append(
                "th, td { padding: 8px; text-align: left; border-bottom: 1px solid #DDD; }");
        strCuerpoHtml.append("tr:hover { background-color: #D6EEEE; }");
        strCuerpoHtml.append("</style>");
        strCuerpoHtml.append("</head>");
        strCuerpoHtml.append("<body>");

        // Contenido de datos no editables
        String dsCatalogo = Objects.nonNull(catalogoValor) ? catalogoValor.getDsValor() : "";
        String dsConvocatoria = convocatoriaDTO.getCdAcronimo();
        String dsConvocatoria2 = ". " + convocatoriaDTO.getCdTitulo();
        if (Objects.nonNull(convocatoriaDTO.getIdTipoModalidad())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdTipoModalidad().getDsValor())) {
            dsConvocatoria2 = dsConvocatoria2
                    .concat(". " + convocatoriaDTO.getIdModalidadConvocatoria().getDsValor());
        }

        String dsExpediente = Objects.nonNull(expedienteDTO)
                ? expedienteDTO.getCdExpediente() + ". "
                        + expedienteDTO.getInteresadoDTO().getCdNumIden() + ". "
                        + expedienteDTO.getInteresadoDTO().getDsNombre() + " "
                        + expedienteDTO.getInteresadoDTO().getDsApellido1() + " "
                        + expedienteDTO.getInteresadoDTO().getDsApellido2() + ". " + dsCatalogo
                : "";

        strCuerpoHtml.append("<p>").append("<strong>")
                .append(Constantes.Plantilla.TEXTO_CONVOCATORIA).append(dsConvocatoria)
                .append("</strong>").append(dsConvocatoria2).append("</p>");
        strCuerpoHtml.append("<p>").append("<strong>")
                .append(Constantes.Plantilla.TEXTO_PROCEDIMIENTO).append("</strong>")
                .append(convocatoriaPlaDTO.getTramiteDTO().getDsValor()).append("</p>");

        strCuerpoHtml.append("<p>").append("<strong>").append(Constantes.Plantilla.TEXTO_EXPEDIENTE)
                .append("</strong>").append(dsExpediente).append("</p>");

        if (Objects.nonNull(expedienteDTO)
                && Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())) {
            try {
                List<ExpedienteDTO> lstExpediente = this.expedienteService
                        .buscarParticipantesPorSolicitud(expedienteDTO.getIdSolicitud(), null);
                if (!lstExpediente.isEmpty()) {
					strCuerpoHtml.append("<h4>").append(Constantes.Plantilla.TEXTO_SUB_EXPEDIENTE)
                        .append("</h4>");
                    strCuerpoHtml.append("<ol style=\"line-height:180%\">");
                    for (ExpedienteDTO expeDTO : lstExpediente) {
                        CatalogosValoresDTO catalogoValorDTO = this.catalogosValoresService
                                .buscarNmConstante(expeDTO.getInteresadoDTO().getTipoActorDTO().getNmConstante());
                        String dsExpe = expeDTO.getCdExpediente() + ". "
                                + expeDTO.getInteresadoDTO().getCdNumIden() + ". "
                                + expeDTO.getInteresadoDTO().getDsNombre() + " "
                                + expeDTO.getInteresadoDTO().getDsApellido1() + " "
                                + expeDTO.getInteresadoDTO().getDsApellido2() + ". "
                                + catalogoValorDTO.getDsValor();
                        strCuerpoHtml.append("<li>").append(dsExpe).append("</li>");
                    }
                    strCuerpoHtml.append("</ol>");
                }
            } catch (ServiceException e) {
                log.error(e.getMessage());
            }
        }

        strCuerpoHtml.append("<p>").append("<strong>").append(Constantes.Plantilla.TEXTO_FECHA)
                .append("</strong>").append(new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .append("</p> <br>");

        // Contenido de datos editables

        // VMM 14/10/26 MD-3142: texto superior + inferior (el inferior es opcional)
        String textoDescripcionHtml = this.textoDescripcionUnificado(convocatoriaPlaDTO);
        if (StringUtils.isNotBlank(textoDescripcionHtml)) {
            strCuerpoHtml.append("<p>")
                    .append(textoDescripcionHtml.replace("&nbsp;", "<br>"))
                    .append("</p> <br><br>");
        }

        // Contenido de firma
        if (StringUtils.isNotBlank(convocatoriaPlaDTO.getDsFirmante())) {
            strCuerpoHtml.append("<h4>").append(convocatoriaPlaDTO.getDsFirmante()).append("</h4>");
        }

        if (Objects.nonNull(convocatoriaDTO.getIdAreaAdministrativa()) && StringUtils
                .isNotBlank(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad())) {
            strCuerpoHtml.append("<h4>")
                    .append(convocatoriaDTO.getIdAreaAdministrativa().getDsUnidad())
                    .append("</h4>");
        }

        if (Objects.nonNull(convocatoriaDTO.getIdSubdireccion())
                && StringUtils.isNotBlank(convocatoriaDTO.getIdSubdireccion().getDsUnidad())) {
            strCuerpoHtml.append("<h4>").append(convocatoriaDTO.getIdSubdireccion().getDsUnidad())
                    .append("</h4>");
        }

        if (Objects.nonNull(convocatoriaDTO.getIdDireccion())) {
            strCuerpoHtml.append("<h4>").append(convocatoriaDTO.getIdDireccion().getDsUnidad())
                    .append("</h4>");
        }

        strCuerpoHtml.append("<h4>").append(Constantes.Plantilla.TEXTO_CONTENIDO_CONSEJERIA)
                .append("</h4>");

        strCuerpoHtml.append("</body>");
        strCuerpoHtml.append("</html>");

        return strCuerpoHtml.toString();
    }
}
