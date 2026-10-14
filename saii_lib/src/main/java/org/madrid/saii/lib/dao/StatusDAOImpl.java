package org.madrid.saii.lib.dao;

import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.dao.AbstractAtlasSimpleBaseDAO;
import atlas.core.exceptions.DataAccessException;

/**
 * <p>
 * Interfaz para el DAO de la clase de dominio Medico
 * </p>
 *
 * @see atlas.rest.negocio.Medico
 * @author Madrid Digital
 * @since 20-feb-2012 15:43:54
 */
@Repository("statusDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class StatusDAOImpl extends AbstractAtlasSimpleBaseDAO implements StatusDAO {
    /**
     * {@inheritDoc}
     */
    @Override
    public Integer obtieneData() {
        String msg = "";
        try {
            final String cadena;
            cadena = "select 1 as cuenta from dual";
            Map<String, Object> queryParams = new HashMap<>();
            return (Integer) this.executeUniqueSQLQuery(cadena, queryParams);
        } catch (HibernateException e) {
            msg = "obtieneData: Error Criteria:" + e.getMessage();
            throw new DataAccessException(msg, e); // HibernateException
        } catch (Exception e) {
            msg = "obtieneData: Error Criteria:" + e.fillInStackTrace();
            throw new DataAccessException(msg, e); // Exception
        }

    }
}
