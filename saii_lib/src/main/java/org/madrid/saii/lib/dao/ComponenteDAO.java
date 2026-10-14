package org.madrid.saii.lib.dao;

import java.util.List;

import org.madrid.saii.lib.domain.Componente;

/**
 * Interfaz para el DAO de la clase de dominio Componente
 *
 * @author everis
 */
public interface ComponenteDAO extends BaseDAO<Componente, Integer> {

    List<Componente> cargaNmConstante();

}
