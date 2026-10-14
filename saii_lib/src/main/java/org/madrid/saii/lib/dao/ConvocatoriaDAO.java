
package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;

import atlas.core.exceptions.ServiceException;

/**
 * Interfaz para el DAO de la clase de dominio Convocatoria
 *
 * @author everis
 */
public interface ConvocatoriaDAO extends BaseDAO<Convocatoria, Integer> {

    /**
     * Busca las convocatorias activas
     *
     * @return List<Convocatoria>
     * @throws ServiceException 
     */
    List<Convocatoria> buscarConvocatoriasActivas();

    /**
     * comp´rueba que todos los expedientes que no están dado de baja tengan su
     * estado tramite evaluación (ID_ESTADO_REV_EVA) en “Evaluado KO o Evaluado
     * OK” y su estado interno no sea “Desistido” (ID_ESTADO_REV_ADMI),
     * “Excluido” (ID_ESTADO_REV_REQUI) o “Renuncia expresa” (ID_ESTADO)
     *
     * @param idConvocatoria
     * @return
     * @throws ServiceException 
     */
    Boolean esPuedeSubirPropuestaResProvisional(Integer idConvocatoria);
    
    /**
     * Busca las convocatoria
     *
     * @return Convocatoria
     */
    ConvocatoriaDTO buscarConvocatoriaByIdConvocatoria(Integer idConvocatoria);

}
