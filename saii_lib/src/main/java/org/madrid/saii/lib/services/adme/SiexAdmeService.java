package org.madrid.saii.lib.services.adme;

import org.madrid.saii.lib.dto.ExpedienteDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio SIEX Administracion
 *
 * @author everis
 */
public interface SiexAdmeService {

    /**
     * Servicio que permite grabar un expediente
     */
    String grabarExpteAPLI(ExpedienteDTO dto) throws ServiceException;

    /**
     * Servicio que permite grabar un Hito de expediente.
     *
     */
    void grabarHitoAPLI(ExpedienteDTO dto) throws ServiceException;

    /**
     * Servicio que permite grabar un interesado.
     */
    void grabarInteAPLI(ExpedienteDTO dto) throws ServiceException;

    /**
     * Servicio que permite obtener los datos de un expediente
     * @param dto Expediente.
     * @return Xml de salida
     * @throws ServiceException
     */
    String obtenerDatosExpteAPLI(ExpedienteDTO dto) throws ServiceException;

    /**
     *  Servicio que permite obtener los datos del hito de un expediente.
     */
    void obtenerDatosHitoAPLI(ExpedienteDTO dto) throws ServiceException;

    /**
     * Servicio que permite obtener los datos de interesados.
     */
    void obtenerdatosInteAPLI(ExpedienteDTO dto) throws ServiceException;
}
