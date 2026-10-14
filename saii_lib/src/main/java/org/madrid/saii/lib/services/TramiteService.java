package org.madrid.saii.lib.services;

import org.madrid.saii.lib.tramite.Tramite;

import atlas.core.exceptions.ServiceException;

public interface TramiteService {
    
    /**
     * Metodo encargado de determinar el tipo de tramite
     * 
     * @param idTipoTramite
     * @return
     * @throws ServiceException
     */
    public Tramite getTramite(Integer idTipoTramite) throws ServiceException;
    
}
