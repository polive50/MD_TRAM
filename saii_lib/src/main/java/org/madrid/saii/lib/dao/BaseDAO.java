package org.madrid.saii.lib.dao;

import java.io.Serializable;

import atlas.core.dao.AtlasBaseDAO;

/**
 * DAO genérico con funcionalidad CRUD <br/>
 * <br/>
 *
 * Extiende esta interfaz para los objetos de modelo sin necesidad de realizar
 * cast de conversión de datos.
 *
 * @author everis
 *
 * @since 01-oct-2020 15:03:20
 *
 * @param <T> el tipo de objeto que manejará este DAO.
 * @param <K> la clave primaria del objeto de dominio.
 */
public interface BaseDAO<T, K extends Serializable> extends AtlasBaseDAO<T, K> {

}
