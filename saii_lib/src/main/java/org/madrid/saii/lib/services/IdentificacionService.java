package org.madrid.saii.lib.services;

import atlas.core.exceptions.ServiceException;

public interface IdentificacionService {
    
    /**
     * Metodo que detecta si un número de documento pasado es NIF o NIE
     *
     * @param numDoc
     * @return TipoDoc. 0 si no es NIF o NIE
     */
    public Integer calcularTipoIdentificacion(String numDoc) throws ServiceException;
    
}
