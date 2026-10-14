package org.madrid.saii.lib.services;

import java.util.Date;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz del servicio
 *
 * @author nttdata
 */
public interface CataService {

    /**
     * Metodo para calcular dias habiles
     *
     * @param fcDesde
     * @param iDias
     * @param cdMuniAdministracion
     * @param cdMuniInteresado
     * @return Date
     * @throws ServiceException
     */
    Date calcularDiasHabiles(Date fcDesde, Integer iDias, String cdMuniAdministracion,
            String cdMuniInteresado) throws ServiceException;

    Date calcularDiasHabilesInicio(Date fcDesde, String cdMuniAdministracion,
            String cdMuniInteresado) throws ServiceException;
}
