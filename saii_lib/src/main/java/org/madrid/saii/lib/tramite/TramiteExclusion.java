package org.madrid.saii.lib.tramite;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.madrid.saii.lib.dto.CatalogosValoresDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteReqDTO;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de tramite exclusión (requisitos)
 *
 * @author nttdata
 *
 */
@Slf4j
public class TramiteExclusion extends Tramite {

    @Override
    public byte[] formularioPdf(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria) {
        byte[] byteSalida = null;
        try {

            // VMM 14/10/26 MD-3142: mismo modelo de PDF que subsanacion administrativa (MD-2950): margen
            // derecho de 85f para la marca de agua lateral y margen inferior de 110f para la firma/pie
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
            // VMM 14/10/26 MD-3142: cabecera igual que subsanacion administrativa (MD-2950): el titulo
            // de la plantilla (barra roja) es el nombre de la convocatoria (acronimo + titulo) y se
            // separa del logo con agregarEspacioLogos=true; sin "Informe de tramite" ni impreso
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
            // VMM 14/10/26 MD-3142: la fecha ya no va en la cabecera, sale junto a "Firmado
            // electronicamente" al pie (agregarLugarFechaFirmaElectronicaADM), como en subsanacion administrativa
            informeHelperPdfService.agregarFilaVacio(document, 1);

            // VMM 14/10/26 MD-3142: cuerpo del PDF: texto superior + tabla con los requisitos que no
            // cumple (itCumple = 'N': requisito y motivo/observaciones) + texto inferior
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

            PdfPTable tablaRequisitos = new PdfPTable(2);
            tablaRequisitos.setWidthPercentage(100);
            tablaRequisitos.setWidths(new float[] { 45f, 55f });
            tablaRequisitos.addCell(informeHelperPdfService.celdaEtiquetaCabecera("Requisito", true));
            tablaRequisitos.addCell(informeHelperPdfService.celdaEtiquetaCabecera("Motivo", true));
            tablaRequisitos.setHeaderRows(1);

            if (Objects.nonNull(expedienteDTO)) {
                List<ExpedienteReqDTO> listExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                        .filter(x -> Objects.isNull(x.getFcBajaFila())
                                && Constantes.AfirmacionNegacion.N.equals(x.getItCumple()))
                        .collect(Collectors.toList());

                for (ExpedienteReqDTO expedienteReqDTO : listExpReq) {
                    String requisito = Objects.nonNull(expedienteReqDTO.getRequisitoDTO())
                            && Objects.nonNull(expedienteReqDTO.getRequisitoDTO().getDsValor())
                                    ? expedienteReqDTO.getRequisitoDTO().getDsValor()
                                    : "";
                    String motivo = Objects.nonNull(expedienteReqDTO.getTlObservaciones())
                            ? expedienteReqDTO.getTlObservaciones()
                            : "";
                    tablaRequisitos.addCell(informeHelperPdfService.celdaEtiqueta(requisito, true));
                    tablaRequisitos.addCell(informeHelperPdfService.celdaEtiqueta(motivo, true));
                }
            }
            document.add(tablaRequisitos);

            if (StringUtils.isNotBlank(descripcionInf)) {
                informeHelperPdfService.agregarFilaVacio(document, 1);
                informeHelperPdfService.agregarTextoEnriquecidoADM(document, "", descripcionInf, 0, true);
            }

            // VMM 14/10/26 MD-3142: firma y pie como subsanacion administrativa. Si no cabe la firma
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

        if (Objects.nonNull(expedienteDTO)) {
            strCuerpoHtml.append("<table aria-label=\"Exclusión\">");
            strCuerpoHtml.append("<tr>");
            strCuerpoHtml.append("<th>").append("N°").append("</th>");
            strCuerpoHtml.append("<th>").append("Requisito").append("</th>");
            strCuerpoHtml.append("<th>").append("Observacion").append("</th>");
            strCuerpoHtml.append("</tr>");

            int numOrden = 1;

            List<ExpedienteReqDTO> listExpReq = expedienteDTO.getLstExpedienteReqDTO().stream()
                    .filter(x -> Objects.isNull(x.getFcBajaFila())
                            && Constantes.AfirmacionNegacion.N.equals(x.getItCumple()))
                    .collect(Collectors.toList());

            for (ExpedienteReqDTO expedienteReqDTO : listExpReq) {

                strCuerpoHtml.append("<tr>");
                strCuerpoHtml.append("<td>").append(numOrden).append("</td>");
                strCuerpoHtml.append("<td>").append(expedienteReqDTO.getRequisitoDTO().getDsValor())
                        .append("</td>");
                if (StringUtils.isNotBlank(expedienteReqDTO.getTlObservaciones())) {
                    strCuerpoHtml.append("<td>").append(expedienteReqDTO.getTlObservaciones())
                            .append("</td>");
                } else {
                    strCuerpoHtml.append("<td>").append("</td>");
                }
                strCuerpoHtml.append("</tr>");

            }

            strCuerpoHtml.append("</table> <br><br><br>");
        } else {
            strCuerpoHtml.append("<p>").append(Constantes.Plantilla.TEXTO_TIPO_EXCLUSION)
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
