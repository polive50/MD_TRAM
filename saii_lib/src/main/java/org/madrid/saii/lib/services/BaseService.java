package org.madrid.saii.lib.services;

import java.io.Serializable;

import org.madrid.saii.lib.dao.BaseDAO;

import atlas.core.service.AtlasBaseService;

/**
 * Interfaz de servicio genérico con funcionalidad CRUD
 *
 * <p>
 * Extiende esta interfaz si se requiere servicios para los objetos de modelo
 * sin necesidad de realizar cast de conversión de datos.
 *
 * @author everis
 *
 * @since 01-oct-2020 15:03:20
 *
 * @param <T> el tipo de objeto que manejará este servicio.
 * @param <K> la clave primaria del objeto de dominio.
 * @param <D> el dao que maneja el objeto.
 */
public interface BaseService<T, K extends Serializable, D extends BaseDAO<T, K>>
        extends AtlasBaseService<T, K, D> {

}
