package org.madrid.saii.lib.dao;

import java.util.Date;

import atlas.core.exceptions.DataAccessException;

/**
 * Interfaz para el DAO de la clase de dominio Cata
 *
 * @author nttdata
 */
public interface CataDAO {
    /**
     * Ejecuta la funcion
     *
     * @param funcion a ejecutar
     * @return resultado
     * @throws DataAccessException Excepción.
     */
    Date calcularDiasHabiles(Date fcDesde, Integer iDias, String cdMuniAdministracion,
            String cdMuniInteresado) throws DataAccessException;

    Date calcularDiasHabilesInicio(Date fcDesde, String cdMuniAdministracion,
            String cdMuniInteresado);
}
