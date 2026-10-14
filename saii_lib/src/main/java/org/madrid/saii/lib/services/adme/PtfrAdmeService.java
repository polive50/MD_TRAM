package org.madrid.saii.lib.services.adme;

import java.util.List;

import org.madrid.saii.lib.dto.PtfrAltaSolicitudDatosDTO;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudResultadoDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesTipoDocDTO;
import org.madrid.saii.lib.dto.PtfrConsultaTiposDocumentoDTO;
import org.madrid.saii.lib.dto.PtfrConsultaVerificadoresTipoDocDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.ptfr.domain.PtfrConsultaEtapasOut;
import atlas.eadm.ptfr.domain.PtfrEstadoDocOut;

/**
 * Implementación del servicio PTFR Administracion
 *
 * @author everis
 */
public interface PtfrAdmeService {

    /**
     * Servicio que permite dar de alta
     */
    PtfrAltaSolicitudResultadoDTO altaSolicitud(PtfrAltaSolicitudDatosDTO datosEntrada)
            throws ServiceException;

    PtfrEstadoDocOut consultaEstadoDoc(String referencia, String codigoArchivo) throws ServiceException;

    PtfrRecuperaDocFirmadoDTO recuperarDocumentoFirmado(String referencia, String codigoArchivo)
            throws ServiceException;

    List<PtfrConsultaTiposDocumentoDTO> consultaTiposDocumento() throws ServiceException;

    PtfrConsultaTiposDocumentoDTO consultaTiposDocumentoPorCodigoAlias(
            String codigoAliasTipoDocumento) throws ServiceException;

    List<PtfrConsultaFirmantesDTO> consultaFirmantes() throws ServiceException;

    List<PtfrConsultaFirmantesTipoDocDTO> consultaFirmantesTipoDocumento(Integer codigoDocumento)
            throws ServiceException;

    List<PtfrConsultaVerificadoresTipoDocDTO> consultaVerificadoresTipoDocumento(
            Integer codigoDocumento) throws ServiceException;

    PtfrConsultaEtapasOut consultaEtapasDoc(String referencia, String codigoArchivo)
            throws ServiceException;

}
