package org.madrid.saii.tramitacion.jsf.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import atlas.core.general.IoUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * @author everis
 *
 */
@Slf4j
public final class UtilesWeb {

    /**
     * Constructor privado
     */
    private UtilesWeb() {
    }

    /**
     * Metodo que en base a flujo de entrada en capaz de descargar el fichero y
     * abrirlo por consola
     *
     * @param salidaReport
     * @param log
     * @param tipoDeDocumento
     * @param nombreDocumento
     */
    public static void openFile(final byte[] bytesFichero, final String strTipoDocumento,
            final String nombreDocumento) {
        try {

            // Declaro un stream de escritura donde volcar el array de byte
            ByteArrayOutputStream streamEscritura = new ByteArrayOutputStream();

            // Vuelco el array de byte al stream
            IoUtils.stream(new ByteArrayInputStream(bytesFichero), streamEscritura);

            // Recupero el contexto para copiar en el output de la llamada

            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            // Init servlet response.
            response.reset();
            response.setHeader("Content-type", "application/" + strTipoDocumento);
            response.setContentLength(streamEscritura.size());

            response.setHeader("Content-disposition",
                    "attachment; filename=" + nombreDocumento + "." + strTipoDocumento);
            response.setHeader("pragma", "public");

            ServletOutputStream out = response.getOutputStream();
            streamEscritura.writeTo(out);
            out.flush();
            out.close();
            facesContext.responseComplete();
        } catch (IOException e) {
            log.error("Se produjo un error al descargar el fichero. ", e);
        }

    }
    
    
   
    public static void verDocumentoFica(byte[] bytesFichero, String extension, String nombreDocumento) {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();
            HttpServletResponse response = (HttpServletResponse) externalContext.getResponse();

            // 1. LIMPIEZA TOTAL: Evita que se acumulen cabeceras previas
            response.reset(); 

            // 2. TIPO DE CONTENIDO
            response.setContentType("application/" + extension);
            response.setContentLength(bytesFichero.length);

            // 3. NOMBRE SEGURO: Ponemos el nombre entre comillas dobles para evitar el error de cabeceras múltiples
            // si el nombre contiene espacios o caracteres especiales.
            String fileNameWithExt = nombreDocumento + "." + extension;
            response.setHeader("Content-Disposition", "inline; filename=\"" + fileNameWithExt + "\"");

            // 4. EVITAR CACHE
            response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
            response.setHeader("Pragma", "no-cache");

            // 5. ESCRITURA
            try (ServletOutputStream out = response.getOutputStream()) {
                out.write(bytesFichero);
                out.flush();
            }
            
            facesContext.responseComplete();
            
        } catch (IOException e) {
            log.error("Error al visualizar el documento", e);
        }
    }

}
