package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.BloqueComponente;
import org.madrid.saii.lib.dto.BloqueComponenteDTO;

/**
 * Interfaz para el DAO de la clase de dominio Componente
 *
 * @author everis
 */
public interface BloqueComponenteDAO extends BaseDAO<BloqueComponente, Integer> {

    /**
     *
     * @param idBloque
     * @param idFinanciacion
     * @return
     */
    public List<BloqueComponente> generarFilasBloqueComponenteFinanciacion(Integer idBloque,
            Integer idFinanciacion);

    /**
     * Metodo para obtener bloques componente por expediente y/o bloque
     *
     * @param idExpediente
     * @param idFormatoBloque
     * @return
     */
    public List<BloqueComponenteDTO> obtenerPorExpediente(Integer idExpediente,
            Integer idFormatoBloque);

    
    public List<BloqueComponente> listaBloqueComponentesPorConvYTipoDoc(Integer idConvocatoria, Integer idTipoDoc);

}
