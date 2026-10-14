package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Usuario;

/**
 * Interfaz para el DAO de la clase de dominio UsuarioOrga
 *
 * @author nttdata
 */
public interface UsuarioDAO extends BaseDAO<Usuario, Integer> {

    /**
     * Metodo de obtener la cantidad de rol de usuario
     *
     * @param cdUsuario
     * @return obtenerTotalRolesUsuario
     */
    Boolean consultarTieneRolesUsuarioSaii(String cdUsuario);

    /**
     * Metodo para consultar si es usuario gestor
     *
     * @param cdUsuario
     * @return
     */
    Boolean consultarEsUsuarioGestor(String cdUsuario);

}
