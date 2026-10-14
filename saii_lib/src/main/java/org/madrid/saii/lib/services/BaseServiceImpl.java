package org.madrid.saii.lib.services;

import java.io.Serializable;

import org.madrid.saii.lib.dao.BaseDAO;

import atlas.core.service.AtlasBaseServiceImpl;

/**
 * Esta clase sirve como base de desarrollo para todos los demás Servicios. Se
 * proporcionan implementaciones de referencia para la funcionalidad CRUD. Solo
 * es necesario extender esta clase cuando se requiere lógica específica.
 * <p/>
 *
 * @author everis
 *
 * @since 01-oct-2020 15:03:20
 *
 * @param <T> el tipo de objeto que manejará este servicio.
 * @param <PK> la clave primaria del objeto de dominio.
 * @param <D> el dao que maneja el objeto.
 */
public class BaseServiceImpl<T, K extends Serializable, D extends BaseDAO<T, K>>
        extends AtlasBaseServiceImpl<T, K, D> implements BaseService<T, K, D> {

}
