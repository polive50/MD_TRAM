package org.madrid.saii.lib.services.adme;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.madrid.saii.lib.dto.PtfrAltaSolicitudDatosDTO;
import org.madrid.saii.lib.dto.PtfrAltaSolicitudResultadoDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesDTO;
import org.madrid.saii.lib.dto.PtfrConsultaFirmantesTipoDocDTO;
import org.madrid.saii.lib.dto.PtfrConsultaTiposDocumentoDTO;
import org.madrid.saii.lib.dto.PtfrConsultaVerificadoresTipoDocDTO;
import org.madrid.saii.lib.dto.PtfrRecuperaDocFirmadoDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.ptfr.domain.PtfrAltaSolicitudIN;
import atlas.eadm.ptfr.domain.PtfrAltaSolicitudOut;
import atlas.eadm.ptfr.domain.PtfrConsultaEtapasOut;
import atlas.eadm.ptfr.domain.PtfrConsultaFirmantesOut;
import atlas.eadm.ptfr.domain.PtfrConsultaFirmantesTipoDocOut;
import atlas.eadm.ptfr.domain.PtfrConsultaTiposDocumentoOut;
import atlas.eadm.ptfr.domain.PtfrEstadoDocOut;
import atlas.eadm.ptfr.domain.PtfrFirmante;
import atlas.eadm.ptfr.domain.PtfrFirmanteTipoDoc;
import atlas.eadm.ptfr.domain.PtfrRecuperaDocFirmadoOut;
import atlas.eadm.ptfr.domain.PtfrTipoDocumento;
import atlas.eadm.services.PtfrService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio PTFR Administracion
 *
 * @author everis
 */
@Service("ptfrAdmeService")
@Slf4j
public class PtfrAdmeServiceImpl implements PtfrAdmeService {

    /**
     * Servicio PtfrService
     */
    @Autowired
    @Qualifier("ptfrService")
    public PtfrService ptfrService;

    /**
     *
     */
    @Override
    public PtfrAltaSolicitudResultadoDTO altaSolicitud(PtfrAltaSolicitudDatosDTO datosEntrada)
            throws ServiceException {
        log.info("Método altaSolicitud");

        PtfrAltaSolicitudResultadoDTO resultado = null;

        try {

            PtfrAltaSolicitudIN input = new PtfrAltaSolicitudIN();
            input.setCodigoAplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
            input.setCodigoTipoDocumento(datosEntrada.getCodigoTipoDocumento());

            InputStream targetStream = new ByteArrayInputStream(datosEntrada.getDocumento());
            input.setDocumento(targetStream);

            input.setModoRespuesta(datosEntrada.getModoRespuesta());

            // Si tiene verificador, lo añadimos primero
            Boolean tieneVerificador = Boolean.FALSE
                    .equals(datosEntrada.getListaCodigoVerificadores().isEmpty());

            if (Boolean.TRUE.equals(tieneVerificador)) {
                input.incluirFirmante(datosEntrada.getListaCodigoVerificadores().get(0));
            }

            // Verificamos si hay firmantes antes de incluirlos
            if (datosEntrada.getListaCodigoFirmantes() == null
                    || datosEntrada.getListaCodigoFirmantes().isEmpty()) {
                throw new ServiceException("No hay firmantes", null,
                        Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }
            // Incluimos el firmante
            input.incluirFirmante(datosEntrada.getListaCodigoFirmantes().get(0));

            input.setReferenciaSolicitante(datosEntrada.getReferenciaSolicitante());
            input.setTitulo(datosEntrada.getTitulo());
            input.setCove(datosEntrada.getCove());

            log.info("-------------------------");
            log.info("Código aplicación: " + input.getCodigoAplicacion());
            log.info("Código tipo docum: " + input.getCodigoTipoDocumento());
            log.info("Código respuesta : " + input.getModoRespuesta());
            log.info("Nº de firmantes  : " + input.getListaEtapas().size());

            if (Boolean.TRUE.equals(tieneVerificador)) {
                log.info("Verificador: " + input.getListaEtapas().get(0).getCodigoFirmante());
                log.info("Firmante: " + input.getListaEtapas().get(1).getCodigoFirmante());
            } else {
                log.info("Firmante: " + input.getListaEtapas().get(0).getCodigoFirmante());
            }
            log.info("-------------------------");

            PtfrAltaSolicitudOut ptfrAltaSolicitudOut = this.ptfrService.altaSolicitud(input);

            if (Objects.isNull(ptfrAltaSolicitudOut)) {
                throw new ServiceException(
                        "Servicio Portafirmas. Alta de solicitud no disponible. Por favor intente en unos minutos.",
                        null, Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

            if (Constantes.Portafirmas.RESPUESTA_OK
                    .equals(ptfrAltaSolicitudOut.getCodigoRespuesta())) {
                resultado = new PtfrAltaSolicitudResultadoDTO();
                resultado.setCodigoArchivo(
                        ptfrAltaSolicitudOut.getDocumento().getCodArchivoOriginal());
                resultado.setReferencia(ptfrAltaSolicitudOut.getDocumento().getReferencia());
            } else {
                throw new ServiceException("Servicio Portafirmas. "
                        + ptfrAltaSolicitudOut.getDescripcionRespuesta() + ".", null,
                        Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }

        } catch (Exception e) {
            // VMM 09/09/26 MD-2725: e.getMessage() puede ser null (excepciones sin
            // mensaje), lo que provocaba una NPE aquí y enmascaraba el error real
            log.error("Error en alta de solicitud Portafirmas", e);
            if (e.getMessage() != null && e.getMessage().contains("firmantes")) {
                throw new ServiceException(e.getMessage(), null,
                        Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
            }
            throw new ServiceException(
                    "Servicio Portafirmas. Ocurrió un error al registrar el alta de solicitud.", e,
                    Constantes.Error.CODIGO_EXCEPTION_PERSONALIZADA);
        }

        return resultado;
    }

    /**
     *
     */
    @Override
    public PtfrEstadoDocOut consultaEstadoDoc(String referencia, String codigoArchivo)
            throws ServiceException {
        return this.ptfrService.consultaEstadoDoc(Constantes.AplicativoSaii.CODIGO_APLICATIVO,
                referencia, codigoArchivo);
    }

    @Override
    public PtfrConsultaEtapasOut consultaEtapasDoc(String referencia, String codigoArchivo)
            throws ServiceException {
        return this.ptfrService.consultaEtapasDoc(Constantes.AplicativoSaii.CODIGO_APLICATIVO,
                referencia, codigoArchivo);
    }

    /**
     *
     */
    @Override
    public PtfrRecuperaDocFirmadoDTO recuperarDocumentoFirmado(String referencia,
            String codigoArchivo) throws ServiceException {
        log.info("Método recuperarDocumentoFirmado");

        PtfrRecuperaDocFirmadoDTO resultado = null;

        PtfrRecuperaDocFirmadoOut output = this.ptfrService.recuperarDocumentoFirmado(
                Constantes.AplicativoSaii.CODIGO_APLICATIVO, referencia, codigoArchivo);

        if (Constantes.Portafirmas.RESPUESTA_OK.equals(output.getCodigoRespuesta())
                && output.getDocumento() != null) {
            resultado = new PtfrRecuperaDocFirmadoDTO();
            resultado.setEstado(output.getDocumento().getEstado());
            resultado.setCodigoArchivo(output.getDocumento().getCodArchivo());
            resultado.setCodigoArchivoOriginal(output.getDocumento().getCodArchivoOriginal());
            resultado.setReferencia(output.getDocumento().getReferencia());
            resultado.setReferenciaSolicitante(output.getDocumento().getReferenciaSolicitante());
            resultado.setPdfB64(output.getDocumento().getPdfB64());
        } else {
            log.error(output.getDescripcionRespuesta());
        }

        return resultado;
    }

    /**
     *
     */
    @Override
    public List<PtfrConsultaTiposDocumentoDTO> consultaTiposDocumento() throws ServiceException {
        log.info("Método consultaTiposDocumento");

        List<PtfrConsultaTiposDocumentoDTO> lista = new ArrayList<>();

        PtfrConsultaTiposDocumentoOut output = this.obtenerTiposDocumento();

        if (output != null && output.getTiposDocumentos() != null) {
            for (PtfrTipoDocumento tipoDoc : output.getTiposDocumentos()) {
                PtfrConsultaTiposDocumentoDTO dto = new PtfrConsultaTiposDocumentoDTO();
                log.info("Tipo documento: " + tipoDoc.getCodigo() + " " + tipoDoc.getNombre());
                dto.setCodigo(tipoDoc.getCodigo());
                dto.setNombre(tipoDoc.getNombre());
                dto.setDescripcion(tipoDoc.getDescripcion());
                dto.setModulo(tipoDoc.getModulo());
                lista.add(dto);
            }
        }

        return lista;
    }

    /**
     *
     */
    @Override
    public PtfrConsultaTiposDocumentoDTO consultaTiposDocumentoPorCodigoAlias(
            String codigoAliasTipoDocumento) throws ServiceException {
        log.info("Método consultaTiposDocumentoPorCodigoAlias");

        PtfrConsultaTiposDocumentoDTO plantilla = new PtfrConsultaTiposDocumentoDTO();

        PtfrConsultaTiposDocumentoOut output = this.obtenerTiposDocumento();

        if (output != null && output.getTiposDocumentos() != null) {

            for (PtfrTipoDocumento tipoDoc : output.getTiposDocumentos()) {

                if (codigoAliasTipoDocumento.equals(tipoDoc.getNombre())) {
                    plantilla = new PtfrConsultaTiposDocumentoDTO();
                    log.info("Tipo documento: " + tipoDoc.getCodigo() + " " + tipoDoc.getNombre());
                    plantilla.setCodigo(tipoDoc.getCodigo());
                    plantilla.setNombre(tipoDoc.getNombre());
                    plantilla.setDescripcion(tipoDoc.getDescripcion());
                    plantilla.setModulo(tipoDoc.getModulo());

                    break;
                }
            }
        }

        return plantilla;
    }

    /**
     *
     */
    @Override
    public List<PtfrConsultaFirmantesDTO> consultaFirmantes() throws ServiceException {
        log.info("Método consultaFirmantes");

        List<PtfrConsultaFirmantesDTO> lista = new ArrayList<>();

        PtfrConsultaFirmantesOut output = this.ptfrService.consultaFirmantes(
                Constantes.AplicativoSaii.CODIGO_APLICATIVO,
                Constantes.AplicativoSaii.CODIGO_APLICATIVO);

        if (output != null && output.getFirmantes() != null) {
            for (PtfrFirmante firmante : output.getFirmantes()) {
                PtfrConsultaFirmantesDTO dto = new PtfrConsultaFirmantesDTO();
                dto.setCargo(firmante.getCargo());
                dto.setCodUsu(firmante.getCodUsu());
                dto.setNombre(firmante.getNombre());
                dto.setTipo(firmante.getTipo());
                dto.setEtapa(firmante.getEtapa());
                log.info("Firmante: " + dto.toString());
                lista.add(dto);
            }
        }

        return lista;
    }

    /**
     *
     */
    @Override
    public List<PtfrConsultaFirmantesTipoDocDTO> consultaFirmantesTipoDocumento(
            Integer codigoDocumento) throws ServiceException {
        log.info("Método consultaFirmantesTipoDocumento");
        List<PtfrConsultaFirmantesTipoDocDTO> lista = new ArrayList<>();

        PtfrConsultaFirmantesTipoDocOut output = this.obtenerFirmantesTipoDocOut(codigoDocumento);

        if (output != null && output.getFirmantes() != null) {
            for (PtfrFirmanteTipoDoc firTipoDoc : output.getFirmantes()) {

                if (Constantes.Portafirmas.FIRMANTE.equals(firTipoDoc.getRol())) {
                    PtfrConsultaFirmantesTipoDocDTO dto = new PtfrConsultaFirmantesTipoDocDTO();

                    dto.setTipo(firTipoDoc.getTipo());
                    dto.setCargo(firTipoDoc.getCargo());
                    dto.setCodUsu(firTipoDoc.getCodUsu());
                    dto.setEtapa(firTipoDoc.getEtapa());
                    dto.setRol(firTipoDoc.getRol());
                    log.info("FirmanteTipoDoc: " + dto.toString());
                    lista.add(dto);
                }
            }
        }

        return lista;
    }

    /**
     *
     */
    @Override
    public List<PtfrConsultaVerificadoresTipoDocDTO> consultaVerificadoresTipoDocumento(
            Integer codigoDocumento) throws ServiceException {
        log.info("Método consultaVerificadoresTipoDocumento");
        List<PtfrConsultaVerificadoresTipoDocDTO> lista = new ArrayList<>();

        PtfrConsultaFirmantesTipoDocOut output = this.obtenerFirmantesTipoDocOut(codigoDocumento);

        if (output != null && output.getFirmantes() != null) {
            for (PtfrFirmanteTipoDoc firTipoDoc : output.getFirmantes()) {

                if (Constantes.Portafirmas.VERIFICADOR.equals(firTipoDoc.getRol())) {
                    PtfrConsultaVerificadoresTipoDocDTO dto = new PtfrConsultaVerificadoresTipoDocDTO();

                    dto.setTipo(firTipoDoc.getTipo());
                    dto.setCargo(firTipoDoc.getCargo());
                    dto.setCodUsu(firTipoDoc.getCodUsu());
                    dto.setEtapa(firTipoDoc.getEtapa());
                    dto.setRol(firTipoDoc.getRol());
                    log.info("VerificadorTipoDoc: " + dto.toString());
                    lista.add(dto);
                }
            }
        }

        return lista;
    }

    /**
     * Devuelve el objeto PtfrConsultaTiposDocumentoOut
     *
     * @return PtfrConsultaTiposDocumentoOut
     * @throws ServiceException
     */
    private PtfrConsultaTiposDocumentoOut obtenerTiposDocumento() throws ServiceException {
        return this.ptfrService.consultaTiposDocumento(Constantes.AplicativoSaii.CODIGO_APLICATIVO,
                Constantes.AplicativoSaii.CODIGO_APLICATIVO);
    }

    /**
     * Devuelve el objeto obtenerFirmantesTipoDocOut
     *
     * @param codigoDocumento a consultar
     * @return obtenerFirmantesTipoDocOut
     * @throws ServiceException
     */
    private PtfrConsultaFirmantesTipoDocOut obtenerFirmantesTipoDocOut(Integer codigoDocumento)
            throws ServiceException {
        return this.ptfrService.consultaFirmantesTipoDocumento(
                Constantes.AplicativoSaii.CODIGO_APLICATIVO, codigoDocumento);
    }

}
