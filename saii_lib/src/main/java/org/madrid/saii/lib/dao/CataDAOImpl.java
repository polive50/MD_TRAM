package org.madrid.saii.lib.dao;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Date;

import org.hibernate.SessionFactory;
import org.hibernate.jdbc.Work;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.DataAccessException;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementacion para el DAO
 *
 * @author nttdata
 */

@Repository("cataDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
@Slf4j
public class CataDAOImpl extends HibernateDaoSupport implements CataDAO {

    /**
     * Constructor.
     *
     * @param sessionfactory the sessionfactory
     */
    public CataDAOImpl(SessionFactory sessionfactory) {
        this.setSessionFactory(sessionfactory);
    }

    /**
     *
     */
    @Override
    public Date calcularDiasHabiles(Date fcDesde, Integer iDias, String cdMuniAdministracion,
            String cdMuniInteresado) throws DataAccessException {

        /**
         * The Class WorkImpl.
         */
        class WorkFuncionTareaImpl implements Work {

            /** Resultado **/
            private Date resultado;

            private Date fcDesde;

            private Integer iDias;

            private String cdMuniAdministracion;

            private String cdMuniInteresado;

            /**
             * Constructor
             *
             * @param procedimiento a ejecutar
             */
            public WorkFuncionTareaImpl(Date fcDesde, int iDias, String cdMuniAdministracion,
                    String cdMuniInteresado) {
                this.fcDesde = fcDesde;
                this.iDias = iDias;
                this.cdMuniAdministracion = cdMuniAdministracion;
                this.cdMuniInteresado = cdMuniInteresado;
            }

            /**
             * Execute.
             *
             * @param connection the connection
             * @throws SQLException the SQL exception
             */
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement statement = null;
                try {
                    StringBuilder sql = new StringBuilder();

                    sql.append("{ ? = call ");
                    sql.append("CATA_FUNC_CALC_PLAZO_L39(?,?,?,?,?,?) ");
                    sql.append(" }");

                    statement = connection.prepareCall(sql.toString());

                    statement.registerOutParameter(1, Types.DATE);

                    statement.setString(2, Utils.dateToString(fcDesde));
                    statement.setInt(3, iDias);
                    statement.setString(4, cdMuniAdministracion);
                    statement.setString(5, null);
                    statement.setString(6, cdMuniInteresado);
                    statement.setString(7, null);

                    statement.execute();

                    resultado = statement.getDate(1);

                } catch (SQLException e) {
                    log.error(e.getMessage());
                    throw new DataAccessException(e.getMessage(), e);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            }
        }

        Date respuesta = null;

        try {
            WorkFuncionTareaImpl work = new WorkFuncionTareaImpl(fcDesde, iDias,
                    cdMuniAdministracion, cdMuniInteresado);
            this.getHibernateTemplate().getSessionFactory().getCurrentSession().doWork(work);

            respuesta = work.resultado;

        } catch (Exception e) {
            log.error(Constantes.Error.ERROR_EJECUCION_FUNCION, e);
            throw new DataAccessException(e.getCause().getMessage());
        }

        return respuesta;
    }
    
    @Override
    public Date calcularDiasHabilesInicio(Date fcDesde, String cdMuniAdministracion,
            String cdMuniInteresado) throws DataAccessException {

        /**
         * The Class WorkImpl.
         */
        class WorkFuncionTareaImpl implements Work {

            /** Resultado **/
            private Date resultado;

            private Date fcDesde;

            private String cdMuniAdministracion;

            private String cdMuniInteresado;

            /**
             * Constructor
             *
             * @param procedimiento a ejecutar
             */
            public WorkFuncionTareaImpl(Date fcDesde, String cdMuniAdministracion,
                    String cdMuniInteresado) {
                this.fcDesde = fcDesde;
                this.cdMuniAdministracion = cdMuniAdministracion;
                this.cdMuniInteresado = cdMuniInteresado;
            }

            /**
             * Execute.
             *
             * @param connection the connection
             * @throws SQLException the SQL exception
             */
            @Override
            public void execute(Connection connection) throws SQLException {

                CallableStatement statement = null;
                try {
                    StringBuilder sql = new StringBuilder();

                    sql.append("{ ? = call ");
                    sql.append("SAII_PACK_BATCH.SAII_FUNC_CALC_DIAHABIL (?,?,?,?,?) ");
                    sql.append(" }");

                    statement = connection.prepareCall(sql.toString());

                    statement.registerOutParameter(1, Types.DATE);
                    statement.setString(2, Utils.dateToString(fcDesde));
                    statement.setString(3, cdMuniAdministracion);
                    statement.setString(4, null);
                    statement.setString(5, cdMuniInteresado);
                    statement.setString(6, null);

                    statement.execute();

                    resultado = statement.getDate(1);

                } catch (SQLException e) {
                    log.error(e.getMessage());
                    throw new DataAccessException(e.getMessage(), e);
                } finally {
                    if (statement != null) {
                        statement.close();
                    }
                }
            }
        }

        Date respuesta = null;

        try {
            WorkFuncionTareaImpl work = new WorkFuncionTareaImpl(fcDesde,cdMuniAdministracion, cdMuniInteresado);
            this.getHibernateTemplate().getSessionFactory().getCurrentSession().doWork(work);

            respuesta = work.resultado;

        } catch (Exception e) {
            log.error(Constantes.Error.ERROR_EJECUCION_FUNCION, e);
            throw new DataAccessException(e.getCause().getMessage());
        }

        return respuesta;
    }
    
    
    
}
