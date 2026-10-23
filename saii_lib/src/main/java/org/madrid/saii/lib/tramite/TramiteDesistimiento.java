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
import org.springframework.stereotype.Component;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de tramite exclusión (administrativo)
 *
 * @author nttdata
 *
 */
@Slf4j
@Component
public class TramiteDesistimiento extends Tramite {

    @Override
    public byte[] formularioPdf(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria) {
        byte[] byteSalida = null;
        try {

            // VMM 22/10/26 MD-3169: mismo modelo de PDF que exclusion (MD-3142) y subsanacion administrativa
            // (MD-2950), tanto para desistimiento definitivo (411) como provisional (418): margen derecho de 85f
            // para la marca de agua lateral y margen inferior de 110f para la firma/pie
            Document document = new Document(PageSize.A4, 36f, 85f, 102f, 110f);
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

            String cdExpediente=  Objects.nonNull(expedienteDTO)
                     ? expedienteDTO.getCdExpediente()
                     : "";
            InformeHelperPdfService informeHelperPdfService = ApplicationContextProvider.getBean(InformeHelperPdfService.class);

            // VMM 22/10/26 MD-3169: cabecera igual que exclusion/subsanacion administrativa: el titulo de la
            // plantilla (barra roja) es el nombre de la convocatoria (acronimo + titulo) y se separa del logo
            // con agregarEspacioLogos=true; sin "Informe de tramite" ni impreso
            StringBuilder dsConvocatoria = new StringBuilder();
            dsConvocatoria.append(convocatoriaDTO.getCdAcronimo()).append(". ");
            dsConvocatoria.append(convocatoriaDTO.getCdTitulo()).append(". ");

            informeHelperPdfService.agregarTitulo(document, null, dsConvocatoria.toString(), null,
                    cdExpediente, Boolean.TRUE, null);

            informeHelperPdfService.agregarFilaVacio(document, 1);
            String dsCatalogo = Objects.nonNull(catalogoValor) ? catalogoValor.getDsValor() : "";

            String nifSolicitante = Objects.nonNull(expedienteDTO)
                    ? expedienteDTO.getInteresadoDTO().getCdNumIden()
                    : "";
            // dsInteresado ya trae nombre+apellidos o razon social segun sea persona fisica o juridica
            String nombreORazonSocial = Objects.nonNull(expedienteDTO)
                    ? expedienteDTO.getInteresadoDTO().getDsInteresado()
                    : "";

            informeHelperPdfService.agregarSubTituloPlantilla(document,
                    Constantes.Plantilla.TEXTO_ASUNTO,
                    convocatoriaPlaDTO.getTramiteDTO().getDsValor(), 14, null);
            informeHelperPdfService.agregarEspacioPequeno(document);
            informeHelperPdfService.agregarSubTituloPlantilla(document,
                    Constantes.Plantilla.TEXTO_EXPEDIENTE, cdExpediente, 14, null);
            informeHelperPdfService.agregarEspacioPequeno(document);
            informeHelperPdfService.agregarSubTituloPlantilla(document,
                    Constantes.Plantilla.TEXTO_NIF_SOLICITANTE, nifSolicitante, 14, null);
            informeHelperPdfService.agregarEspacioPequeno(document);

            if ("Persona jurídica".equals(dsCatalogo)) {
                informeHelperPdfService.agregarSubTituloPlantilla(document,
                        Constantes.Plantilla.TEXTO_RAZON_SOCIAL + ": ", nombreORazonSocial, 14, null);
            } else {
                informeHelperPdfService.agregarSubTituloPlantilla(document,
                        Constantes.Plantilla.TEXTO_NOMBRE + ": ", nombreORazonSocial, 14, null);
            }
            informeHelperPdfService.agregarEspacioPequeno(document);

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
            // VMM 22/10/26 MD-3169: la fecha ya no va en la cabecera, sale junto a "Firmado
            // electronicamente" al pie (agregarLugarFechaFirmaElectronicaADM), como en exclusion
            informeHelperPdfService.agregarFilaVacio(document, 1);

            // VMM 22/10/26 MD-3169: cuerpo del PDF: texto superior + (causa del desistimiento del contrato y
            // observaciones de la plantilla, si las hay) + texto inferior
            String descripcionSup = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcion())
                    ? convocatoriaPlaDTO.getDsDescripcion()
                    : "";
            String descripcionInf = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcionInf())
                    ? convocatoriaPlaDTO.getDsDescripcionInf()
                    : "";

            if (StringUtils.isNotBlank(descripcionSup)) {
                informeHelperPdfService.agregarTextoEnriquecidoADM(document, "", descripcionSup, 0, true);
                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

            if (Objects.nonNull(expedienteDTO)
                    && Boolean.TRUE.equals(expedienteDTO.getEsExpedienteContrato())) {
                String observaciones = "";
                if(expedienteDTO.getExpedienteContrato() != null &&
                        StringUtils.isNotBlank(expedienteDTO.getExpedienteContrato().getTlObservaciParaCiudadano())) {
                    observaciones = expedienteDTO.getExpedienteContrato().getTlObservaciParaCiudadano();

                }else if(expedienteDTO.getExpedienteContratoSustitucion() != null &&
                        StringUtils.isNotBlank(expedienteDTO.getExpedienteContratoSustitucion().getTlObservaciParaCiudadano())) {
                    observaciones = expedienteDTO.getExpedienteContratoSustitucion().getTlObservaciParaCiudadano();
                }
                if(StringUtils.isNotBlank(observaciones)) {
                    informeHelperPdfService.agregarSubTituloPlantilla(document,
                            Constantes.Plantilla.TEXTO_CAUSA_DESISTIMIENTO, observaciones, 12, null);
                    informeHelperPdfService.agregarFilaVacio(document, 1);
                }
            }

            if(convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO() != null
                    && !convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO().isEmpty()
                    && convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO().get(0) != null
                    && StringUtils.isNotBlank(convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO().get(0).getDsObservaciones())) {
                informeHelperPdfService.agregarSubTituloPlantilla(document, "",
                        convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO().get(0).getDsObservaciones(), 0, true);
                informeHelperPdfService.agregarFilaVacio(document, 1);
            }

            if (StringUtils.isNotBlank(descripcionInf)) {
                informeHelperPdfService.agregarTextoEnriquecidoADM(document, "", descripcionInf, 0, true);
            }

            // VMM 22/10/26 MD-3169: firma y pie como exclusion/subsanacion administrativa. Si no cabe la firma
            // (quedan menos de 140pt) se salta de pagina antes de pintarla
            String dsFirmante = "";
            if (Objects.nonNull(convocatoriaPlaDTO.getLstConvocatoriaPlaFirDTO())
                    && StringUtils.isNotBlank(convocatoriaPlaDTO.getDsFirmante())) {
                dsFirmante = convocatoriaPlaDTO.getDsFirmante();
            }

            if (writer.getVerticalPosition(false) < 140f) {
                document.newPage();
            }

            informeHelperPdfService.agregarFilaVacio(document, 1);
            informeHelperPdfService.agregarFilaVacio(document, 1);
            informeHelperPdfService.agregarLugarFechaFirmaElectronicaADM(document, writer, dsFirmante);
            informeHelperPdfService.agregarContenidoFinalAdministrativo(document, writer,
                    convocatoriaPlaDTO, convocatoriaDTO);

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
        strCuerpoHtml.append("<html>");
        strCuerpoHtml.append("<head>");
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

        strCuerpoHtml.append("<p>").append("<b>").append(Constantes.Plantilla.TEXTO_CONVOCATORIA)
                .append(dsConvocatoria).append("</b>").append(dsConvocatoria2).append("</p>");
        strCuerpoHtml.append("<p>").append("<b>").append(Constantes.Plantilla.TEXTO_PROCEDIMIENTO)
                .append("</b>").append(convocatoriaPlaDTO.getTramiteDTO().getDsValor())
                .append("</p>");

        strCuerpoHtml.append("<p>").append("<b>").append(Constantes.Plantilla.TEXTO_EXPEDIENTE)
                .append("</b>").append(dsExpediente).append("</p>");

        if (Objects.nonNull(expedienteDTO)
                && Boolean.TRUE.equals(expedienteDTO.getEsExpedienteSolicitante())) {
            try {

                List<ExpedienteDTO> lstExpediente = this.expedienteService
                        .buscarParticipantesPorSolicitud(expedienteDTO.getIdSolicitud(), null);
                if (!lstExpediente.isEmpty()) {
                    strCuerpoHtml.append("<h4>").append(Constantes.Plantilla.TEXTO_SUB_EXPEDIENTE)
                            .append("</h4>");
                    strCuerpoHtml.append("<ol>");
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

        strCuerpoHtml.append("<p>").append("<b>").append(Constantes.Plantilla.TEXTO_FECHA)
                .append("</b>").append(new SimpleDateFormat("dd/MM/yyyy").format(new Date()))
                .append("</p>");

        // Contenido de datos editables
        // VMM 14/10/26 MD-3142: texto superior + inferior (el inferior es opcional)
        String textoDescripcionHtml = this.textoDescripcionUnificado(convocatoriaPlaDTO);
        if (StringUtils.isNotBlank(textoDescripcionHtml)) {
            strCuerpoHtml.append("<p>")
                    .append(textoDescripcionHtml.replace("&nbsp;", "<br>"))
                    .append("</p> <br><br><br>");
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
