package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.UsuarioJur;
import org.madrid.saii.lib.dto.UsuarioJurFiltrosDTO;

/**
 * Interfaz para el DAO de la clase de dominio UsuarioJur
 *
 * @author everis
 */
public interface UsuarioJurDAO extends BaseDAO<UsuarioJur, Integer> {

    /**
     * Realiza un filtrado de usuario Juridico
     *
     * @param usuarioJuridicoFiltros
     * @return List<UsuarioJuridico>
     */
    List<UsuarioJur> filtrar(UsuarioJurFiltrosDTO usuarioJurFiltrosDTO);
}
