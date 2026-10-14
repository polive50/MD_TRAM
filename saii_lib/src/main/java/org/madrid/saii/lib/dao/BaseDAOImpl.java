package org.madrid.saii.lib.dao;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import atlas.core.dao.AtlasBaseDAOImpl;

/**
 * Esta clase sirve como base de desarrollo para todos los demás DAOs. Se
 * proporcionan implementaciones de referencia para la funcionalidad CRUD.
 *
 * @author everis
 *
 * @since 01-oct-2020 15:03:20
 *
 * @param <T> el tipo de objeto que manejará este DAO.
 * @param <K> la clave primaria del objeto de dominio.
 */
public class BaseDAOImpl<T, K extends Serializable> extends AtlasBaseDAOImpl<T, K> {

    /**
     * Inicialización.
     *
     * @param factory sessionFactory
     */
    @Override
    @Autowired
    @Qualifier("sessionFactory")
    public void init(final SessionFactory factory) {
        super.init(factory);
    }

}
