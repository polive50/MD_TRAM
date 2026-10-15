package org.madrid.saii.tramitacion.catalogos.jsf;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.madrid.core.fica.FicaFichero;
import org.springframework.web.context.support.WebApplicationContextUtils; // ¡NUEVO IMPORT!

import atlas.fica.services.FicaService;

@WebServlet("/DescargaFicaServlet")
public class DescargaFicaServletBean extends HttpServlet {
    
    private static final long serialVersionUID = 1L;

    // Quitamos las anotaciones de aquí arriba porque no funcionan de forma nativa en Servlets
    private FicaService ficaService;

    /**
     * Este método se ejecuta UNA SOLA VEZ cuando el servidor levanta el Servlet.
     * Aquí vinculamos Spring de forma manual e infalible.
     */
    @Override
    public void init() throws ServletException {
        super.init();
        try {
            this.ficaService = WebApplicationContextUtils
                    .getRequiredWebApplicationContext(getServletContext())
                    .getBean("ficaService", FicaService.class);
        } catch (Exception e) {
            throw new ServletException("Error al inicializar FicaService desde el contexto de Spring", e);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        String cdFicaStr = request.getParameter("cdFica");
        
        if (cdFicaStr != null && !cdFicaStr.isEmpty()) {
            try {
                Integer codigoFica = Integer.parseInt(cdFicaStr);
                
                // Ahora this.ficaService ya NO será nulo 👍
                FicaFichero ficaFichero = this.ficaService.recuperar(codigoFica);

                if (ficaFichero != null && ficaFichero.getDatosFichero() != null) {
                    byte[] bytesFichero = ficaFichero.getDatosFichero();
                    String extension = ficaFichero.getExtension();
                    String nombreDocumento = FilenameUtils.removeExtension(ficaFichero.getNombreFichero());

                    // Configurar cabeceras de respuesta limpia tipo GET
                    // Detectamos el content type real por si acaso (ej. application/pdf)
                    String contentType = getServletContext().getMimeType(ficaFichero.getNombreFichero());
                    if (contentType == null) {
                        contentType = "application/pdf"; 
                    }
                    response.setContentType(contentType);
                    response.setContentLength(bytesFichero.length);
                    
                    String fileNameWithExt = nombreDocumento + "." + extension;
                    response.setHeader("Content-Disposition", "inline; filename=\"" + fileNameWithExt + "\"");
                    
                    // Evitar cachés que rompan el visor
                    response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
                    response.setHeader("Pragma", "no-cache");
                    response.setDateHeader("Expires", 0);

                    // Escribir los bytes directamente al navegador
                    try (ServletOutputStream out = response.getOutputStream()) {
                        out.write(bytesFichero);
                        out.flush();
                    }
                    return; 
                }
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al procesar el documento");
                return;
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Parámetro incorrecto");
    }
}