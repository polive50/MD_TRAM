package org.madrid.saii.lib.services;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusEcoDocDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.dto.ImpresoDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el Servicio de llenado de PDF
 */
public interface PdfCamposService {

    /**
     * Rellena los campos del pdf.
     *
     * @param dto the dto
     * @return HashMap<String, String>
     * @throws ServiceException the service exception
     */
    HashMap<String, String> rellenarCampos(ExpedienteDTO dto) throws ServiceException;

    HashMap<String, String> rellenarCamposJustec(ExpedienteDTO dto, ImpresoDTO impresoFormulario,List<ExpedienteDocDTO> listaExpedienteDocDTO)
            throws ServiceException;
    
    HashMap<String, String> rellenarCamposContrato(ExpedienteDTO dto, ImpresoDTO impresoFormulario,  List<ExpedienteDTO> listExpedientesParticipantes)
            throws ServiceException;

    HashMap<String, String> rellenarCamposModPresupPlazosOtra(ExpedienteDTO dto)
            throws ServiceException;
    
    HashMap<String, String> rellenarCamposSubsanacionReq(ExpedienteDTO dto, ImpresoDTO impresoFormulario,  List<ExpedienteDTO> listExpedientesParticipantes)
            throws ServiceException;

    HashMap<String, String> rellenarCamposSubsanacionAdm(ExpedienteDTO dto, ImpresoDTO impresoFormulario,  List<ExpedienteDTO> listExpedientesParticipantes)
            throws ServiceException;

    HashMap<String, String> rellenarCamposAceptacion(ExpedienteDTO dto) throws ServiceException;

    HashMap<String, String> rellenarCamposJusEco(ExpedienteDTO expedienteDTO,
            List<ExpedienteJusEcoDocDTO> listExpedienteJusEcoDTO) throws ServiceException;

    /**
     * Calcula el importe total del presupuesto presentado de un expediente.
     *
     * @param dto expediente sobre el que calcular el importe
     * @return importe total del presupuesto presentado
     * @throws ServiceException the service exception
     */
    BigDecimal calcularImporteTotalPresupuesto(ExpedienteDTO dto) throws ServiceException;

}
