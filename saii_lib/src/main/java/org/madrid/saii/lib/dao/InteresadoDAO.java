package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Interesado;
import org.madrid.saii.lib.dto.InteresadoDTO;

/**
 * Interfaz para el DAO de la clase de dominio Interesado
 *
 * @author nttdata
 */
public interface InteresadoDAO extends BaseDAO<Interesado, Integer> {
    /**
     * Metodo para obtener evaluador para su asignacion
     *
     * @param identificadorCoordinador
     * @param cdUsuario
     * @return List<InteresadoDTO>
     */
    List<InteresadoDTO> obtenerEvaluador(String identificadorCoordinador, String cdUsuario);

    /**
     * Metodo para obtener evaluador por coordinador
     *
     * @param identificadorCoordinador
     * @param cdCodigo
     * @param idConvocatoria
     * @return List<InteresadoDTO>
     */
    List<InteresadoDTO> obtenerEvaluadoresxCoordinador(String identificadorCoordinador,
            Integer idConvocatoria, String cdCodigo);
    
    
    List<Interesado> obtenerInteresadosByCdNumIdem(String cdNumIden);
        
    List<Interesado> buscarPersonaNormalizado(String nombre, String apellido1, String apellido2,
            String cdNumIden, String cdUsuario, Integer idArea);

}
