package org.madrid.saii.lib.tramite;

import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaPlaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.services.CatalogosValoresService;
import org.madrid.saii.lib.services.ExpedienteService;
import org.madrid.saii.lib.services.InformeHelperPdfService;
import org.madrid.saii.lib.services.PdfService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import atlas.core.exceptions.ServiceException;
import lombok.Data;

/**
 * Clase abstracta para los trámites
 *
 * @author nttdata
 *
 */
@Data
public abstract class Tramite {

    /**
     * Mensaje en archivo log
     */
    Logger log = LoggerFactory.getLogger(Tramite.class);

    /**
     * Servicio impresoService
     */
    @Autowired
    @Qualifier("pdfService")
    private PdfService pdfService;

    /**
     * Servicio catalogos valores
     */
    CatalogosValoresService catalogosValoresService;

    /**
     * Servicio expediente
     */
    ExpedienteService expedienteService;
    
    /**
     * Servicio informe Helper PDF
     */
    //InformeHelperPdfService informeHelperPdfService;
    
    

    /**
     * Método para generar formulario PDF personalizado
     *
     * @param convocatoriaDTO
     * @param convocatoriaPlaDTO
     * @param lstImagenesLogo
     * @param expedienteDTO
     * @return
     */
    public abstract byte[] formularioPdf(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria);

    /**
     * Método para generar formulario HTML personalizado
     *
     * @param convocatoriaDTO
     * @param convocatoriaPlaDTO
     * @param expedienteDTO
     * @param strConsejeria
     * @return
     * @throws ServiceException 
     */
    public abstract String formularioHtml(ConvocatoriaDTO convocatoriaDTO,
            ConvocatoriaPlaDTO convocatoriaPlaDTO, ExpedienteDTO expedienteDTO,
            String strConsejeria) throws ServiceException;

}
