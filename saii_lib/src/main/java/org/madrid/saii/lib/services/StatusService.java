package org.madrid.saii.lib.services;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el Servicio de la clase de dominio Medico
 *
 * @see atlas.rest.negocio.Medico
 * @author Madrid Digital
 * @since 20-feb-2012 15:43:54
 */
public interface StatusService {

    Integer obtieneData() throws ServiceException;

}
