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
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.UtilConstantes;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import atlas.core.exceptions.ServiceException;
import lombok.extern.slf4j.Slf4j;

/**
 * Clase de tramite subsanación administrativa
 *
 * @author nttdata
 *
 */
@Slf4j
public class TramiteSubsanacionAdministrativa extends Tramite {
 
    @Override
    public byte[] formularioPdf(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria) {
        byte[] byteSalida = null;
        try {

           // Document document = new Document(PageSize.A4, 36, 36, 102, 36);
            
            log.info("FIRMANTE ENTRANDO AL PDF CONSTRUCTOR : "+convocatoriaPlaDTO.getDsFirmante());
         // Cambiamos el margen derecho de 36f a 85f para reservar el espacio de la marca de agua lateral
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

            InformeHelperPdfService informeHelperPdfService = ApplicationContextProvider.getBean(InformeHelperPdfService.class);
            /*String desImpreso=  Objects.nonNull(expedienteDTO) && Objects.nonNull(expedienteDTO.getImpresoDTO())
                    ? expedienteDTO.getImpresoDTO().getDsDescripcion()
                    : "";*/
            String cdExpediente=  Objects.nonNull(expedienteDTO)
                     ? expedienteDTO.getCdExpediente()
                                     : "";

            // VMM MD-2950: el titulo de la plantilla (barra roja) muestra ahora el nombre completo de la
            // convocatoria (acronimo + titulo + modalidad) en vez del nombre del tramite/plantilla,
            // y se separa del logo de cabecera con el espacio de agregarEspacioLogos=true
            StringBuilder dsConvocatoria = new StringBuilder();
            dsConvocatoria.append(convocatoriaDTO.getCdAcronimo()).append(". ");
            dsConvocatoria.append(convocatoriaDTO.getCdTitulo()).append(". ");
            /*if (StringUtils.isNotBlank(convocatoriaDTO.getIdModalidadConvocatoria().getDsValor())) {
                dsConvocatoria.append(convocatoriaDTO.getIdModalidadConvocatoria().getDsValor());
            }*/

            // VMM MD-2950: quitamos el "Informe de tramite" (tituloSup) que salia arriba a la derecha del expediente,
            // y el "FORMULARIO DE SOLICITUD" (desImpreso, la descripcion del impreso de la solicitud)
            informeHelperPdfService.agregarTitulo(document, null,
                    dsConvocatoria.toString(), null, cdExpediente, Boolean.TRUE, null);

            UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);

            informeHelperPdfService.agregarFilaVacio(document, 1);
            String dsCatalogo = Objects.nonNull(catalogoValor) ? catalogoValor.getDsValor() : "";

            String nifSolicitante = Objects.nonNull(expedienteDTO)
                    ? expedienteDTO.getInteresadoDTO().getCdNumIden()
                    : "";
            // dsInteresado ya trae nombre+apellidos o razon social segun sea persona fisica o juridica
            String nombreORazonSocial = Objects.nonNull(expedienteDTO)
                    ? expedienteDTO.getInteresadoDTO().getDsInteresado()
                    : "";

            // VMM MD-2950: "Convocatoria" ya no se repite aqui porque ahora es el titulo de la plantilla.
            // Se sustituye "Procedimiento" por "Asunto" y "Expediente" pasa a mostrar solo el
            // codigo (antes traia tambien nif/nombre, que ahora van en sus propios campos), y se
            // anaden NIF Solicitante y Nombre y Apellidos/Razon Social. El ancho de la etiqueta en
            // negrita se amplia para que el texto entre de corrido sin cortarse, y el espacio entre
            // estos 4 campos usa agregarEspacioPequeno (menor que una linea en blanco completa).
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
            
            
            if(dsCatalogo.equals("Persona jurídica")) {
                informeHelperPdfService.agregarSubTituloPlantilla(document,
                        Constantes.Plantilla.TEXTO_RAZON_SOCIAL + ": ",
                        nombreORazonSocial, 14, null);
                informeHelperPdfService.agregarEspacioPequeno(document);
            }else {
                informeHelperPdfService.agregarSubTituloPlantilla(document,
                        Constantes.Plantilla.TEXTO_NOMBRE + ": ",
                        nombreORazonSocial, 14, null);
                informeHelperPdfService.agregarEspacioPequeno(document);
            }
            

            // VMM MD-2962:: la fecha vuelve a la cabecera, debajo de nombre/razon social, como estaba
            // antes de MD-2950 (que la habia movido al pie junto a "Firmado electronicamente")
//            LocalDate localDate = LocalDate.now();
//            String fechaTexto = localDate.getDayOfMonth() + " de "
//                    + localDate.format(DateTimeFormatter.ofPattern("MMMM", new Locale("es", "ES")))
//                    + " de " + localDate.getYear();
//            informeHelperPdfService.agregarSubTituloPlantilla(document, Constantes.Plantilla.TEXTO_FECHA, fechaTexto, 0, null); 

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
            informeHelperPdfService.agregarFilaVacio(document, 1);

//            String descripcion = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcion())
//                    ? convocatoriaPlaDTO.getDsDescripcion()
//                    : "";
//            informeHelperPdfService.agregarTextoEnriquecido(document, "", descripcion, 0, true);

            //MD-2962: quitamos espacio
            //informeHelperPdfService.agregarFilaVacio(document, 1);
            
            //VMM 03/06/25 MD-1624: añadimos la documentación a subsanar
//            PdfPTable table = new PdfPTable(2);
//            table.setWidthPercentage(100);
//            table.addCell(informeHelperPdfService.celdaEtiquetaCabecera("Documentación a subsanar", true));
//            table.addCell(informeHelperPdfService.celdaEtiquetaCabecera("Observaciones", true));
//            
//            if (Objects.nonNull(convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO()) 
//                    && !convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO().isEmpty()) {
//                
//                for (ConvocatoriaPlaDocDTO documentacion: convocatoriaPlaDTO.getLstConvocatoriaPlaDocDTO()) {
//                                        
//                    String documentoSubsana = Objects.nonNull(documentacion.getDsDocumentacion())
//                                ? documentacion.getDsDocumentacion()
//                                : "";
//                    
//                    table.addCell(informeHelperPdfService.celdaEtiqueta(documentoSubsana, true));
//                    
//                    String observacSubsana = Objects.nonNull(documentacion.getDsObservaciones())
//                            ? documentacion.getDsObservaciones()
//                            : "";
//                                             
//                    table.addCell(informeHelperPdfService.celdaEtiqueta(observacSubsana, true));
//                }
//            }
//            document.add(table);
            
            //VMM 10/08/26 MD-2950: quitamos esta documentacion de aqui que ya no debe salir se supone...
            //VMM 03/06/25 MD-1624: añadimos la descripcion inferior            
            /*if (Objects.nonNull(expedienteDTO)) {
                int numOrden = 1;
                // Integer idReqSub = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
                Integer idReqSub = convocatoriaPlaDTO.getIdEstaDocuSubaAdmi() != null
                        ? utilConstantes.getGlobalCatalogosValores()
                                .get(convocatoriaPlaDTO.getIdEstaDocuSubaAdmi())
                        : utilConstantes.getGlobalCatalogosValores()
                                .get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION);
                List<ExpedienteDocDTO> listExpDoc = expedienteDTO.getLstExpedienteDocDTO().stream()
                        .filter(expeDoc -> idReqSub.equals(expeDoc.getEstadoDTO().getIdValor())
                        && Objects.isNull(expeDoc.getFcBajaFila()) 
                        // MD-2077. Quitamos la validación de que el FICA sea
                        // nulo para los documentos de requisitos
                        ).sorted((o1, o2) -> o1.getIdExpedienteDoc()
                                .compareTo(o2.getIdExpedienteDoc()))
                        .collect(Collectors.toList());
                for (ExpedienteDocDTO expedienteDocDTO : listExpDoc) {
                    
                    // 2077. Configuramos la causa según sea el tipo de trámite subsanación admin o subsanacion req
                    String causa = "";
                    if (expedienteDocDTO.getTramiteDTO().getIdValor()
                            .equals(utilConstantes.getGlobalCatalogosValores()
                                    .get(Constantes.Tramite.REQUERIMIENTO_SUBSANACION_REQUISITO))) {
                        for (ExpedienteReqDTO expedienteReqDTO : expedienteDTO
                                .getLstExpedienteReqDTO()) {
                            if (expedienteReqDTO.getIdExpedienteDoc() != null
                                    && expedienteReqDTO.getIdExpedienteDoc()
                                            .equals(expedienteDocDTO.getIdExpedienteDoc())) {
                                causa = expedienteReqDTO.getTlObservaciones();
                            }
                        }
                    } else {
                        causa = Constantes.CausaDenegacion.CODIGO_DS_OTROS.equals(
                                expedienteDocDTO.getConvocatoriaCauDTO().getCausaDTO().getDsValor())
                                        ? expedienteDocDTO.getTlCausaDescripcion()
                                        : expedienteDocDTO.getConvocatoriaCauDTO().getCausaDTO()
                                                .getDsValor();
                    }
                    
                    String nombreTipoDoc = Objects.isNull(expedienteDocDTO.getTipoDocumentoDTO())
                            || StringUtils
                                    .isEmpty(expedienteDocDTO.getTipoDocumentoDTO().getDsValor())
                                            ? Constantes.CausaDenegacion.CODIGO_DS_OTROS
                                            : expedienteDocDTO.getTipoDocumentoDTO().getDsValor();
                    
                    String srtNumOrden = numOrden + ") ";
                    
                    StringBuilder strDescripcion = new StringBuilder();
                    // strDescripcion.append(numOrden).append(") ");
                    strDescripcion.append(nombreTipoDoc);
                    strDescripcion.append(" - ");
                    strDescripcion.append(expedienteDocDTO.getNombreFicheroFica());
                    strDescripcion.append(" - ");
                    strDescripcion.append(causa);

                    informeHelperPdfService.agregarSubTituloPlantilla(document, srtNumOrden,
                            strDescripcion.toString(), srtNumOrden.length()-1, null);
                    numOrden++;
                }
            }
            
            informeHelperPdfService.agregarFilaVacio(document, 1);*/


            
         // 1. Obtenemos ambas descripciones
            String descripcion = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcion())
                    ? convocatoriaPlaDTO.getDsDescripcion()
                    : "";

            String descripcionInf = Objects.nonNull(convocatoriaPlaDTO.getDsDescripcionInf())
                    ? convocatoriaPlaDTO.getDsDescripcionInf()
                    : "";

            // 2. Unimos los textos con un salto de línea en HTML
            StringBuilder textoUnificado = new StringBuilder();
            textoUnificado.append(descripcion);

            if (StringUtils.isNotBlank(descripcion) && StringUtils.isNotBlank(descripcionInf)) {
                textoUnificado.append("<br/>"); // Salto de línea único sin huecos gigantes
            }

            textoUnificado.append(descripcionInf);

            // 3. Imprimimos una sola vez
            informeHelperPdfService.agregarTextoEnriquecidoADM(document, "", textoUnificado.toString(), 0, true);
            
            
            // 1. Obtenemos el firmante
         // 1. Obtenemos el firmante validando también la lista
            
            log.info("FIRMANTE ANTES DE ASGINAR: "+convocatoriaPlaDTO.getDsFirmante());
            
            String dsFirmante = "";
            if (Objects.nonNull(convocatoriaPlaDTO) 
                    && Objects.nonNull(convocatoriaPlaDTO.getLstConvocatoriaPlaFirDTO()) 
                    && StringUtils.isNotBlank(convocatoriaPlaDTO.getDsFirmante())) {
                
                dsFirmante = convocatoriaPlaDTO.getDsFirmante();
            }

            log.info("FIRMANTE despùes DE ASGINAR: "+convocatoriaPlaDTO.getDsFirmante()+" y el valor asginasdo: "+dsFirmante);
            
            
                      
         // 2. COMPROBACIÓN: ¿Cabe la firma holgadamente en el espacio que queda?
            float yActual = writer.getVerticalPosition(false);

            if (yActual < 140f) { // Si queda menos de 140pt, la firma NO cabe bien
                document.newPage(); // Saltamos a la Página 2 antes de pintar la firma
            }
            
            informeHelperPdfService.agregarFilaVacio(document, 1);
            informeHelperPdfService.agregarFilaVacio(document, 1);
            // 2. Se lo pasamos al método de la firma
            informeHelperPdfService.agregarLugarFechaFirmaElectronicaADM(document, writer, dsFirmante);

            // 3. El contenido final (organigrama/pie de página fijo)
            informeHelperPdfService.agregarContenidoFinalAdministrativo(document, writer, convocatoriaPlaDTO, convocatoriaDTO);

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
            String strConsejeria) throws ServiceException {
        
        UtilConstantes utilConstantes = ApplicationContextProvider.getBean(UtilConstantes.class);
        
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

        // Etiquetas y css básicos
        strCuerpoHtml.append("<!DOCTYPE html>");
        strCuerpoHtml.append("<html lang=\"es\">");
        strCuerpoHtml.append("<head>");
        strCuerpoHtml.append("<title>Documentos de subsanacion Administrativa</title>");
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
            strCuerpoHtml.append("<table aria-label=\"Documentos a subsanar\">");
            strCuerpoHtml.append("<tr>");
            strCuerpoHtml.append("<th>").append("N°").append("</th>");
            strCuerpoHtml.append("<th>").append("Tipo").append("</th>");
            strCuerpoHtml.append("<th>").append("Documento").append("</th>");
            strCuerpoHtml.append("<th>").append("Causa").append("</th>");
            strCuerpoHtml.append("</tr>");

            int numOrden = 1;
            Integer idReqSubNot = utilConstantes.getGlobalCatalogosValores().get(Constantes.EstadoDocumento.REQUIERE_SUBSANACION_NOTIFICADO);
            List<ExpedienteDocDTO> listExpDoc = expedienteDTO.getLstExpedienteDocDTO().stream()
                    .filter(expeDoc -> idReqSubNot.equals(expeDoc.getEstadoDTO().getIdValor())
                            && Objects.isNull(expeDoc.getFcBajaFila())
                            && Objects.nonNull(expeDoc.getCdFica()))
                    .collect(Collectors.toList());
            for (ExpedienteDocDTO expedienteDocDTO : listExpDoc) {
                String causa = Constantes.CausaDenegacion.CODIGO_DS_OTROS
                        .equals(expedienteDocDTO.getConvocatoriaCauDTO().getCausaDTO().getDsValor())
                                ? expedienteDocDTO.getTlCausaDescripcion()
                                : expedienteDocDTO.getConvocatoriaCauDTO().getCausaDTO()
                                        .getDsValor();

                String nombreTipoDoc = Objects.isNull(expedienteDocDTO.getTipoDocumentoDTO())
                        || StringUtils.isEmpty(expedienteDocDTO.getTipoDocumentoDTO().getDsValor())
                                ? Constantes.CausaDenegacion.CODIGO_DS_OTROS
                                : expedienteDocDTO.getTipoDocumentoDTO().getDsValor();

                strCuerpoHtml.append("<tr>");
                strCuerpoHtml.append("<td>").append(numOrden).append("</td>");
                strCuerpoHtml.append("<td>").append(nombreTipoDoc).append("</td>");
                strCuerpoHtml.append("<td>").append(expedienteDocDTO.getNombreFicheroFica())
                        .append("</td>");
                strCuerpoHtml.append("<td>").append(causa).append("</td>");
                strCuerpoHtml.append("</tr>");
                numOrden++;
            }
            strCuerpoHtml.append("</table> <br><br><br>");
        } else {
            strCuerpoHtml.append("<p>")
                    .append(Constantes.Plantilla.TEXTO_TIPO_SUBSANACION_ADMINISTRATIVA)
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
