package org.madrid.saii.lib.services;

import java.util.Date;

import org.madrid.saii.lib.dao.CataDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;

/**
 * Implementación del servicio
 *
 * @author nttdata
 */
@Service("cataService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class CataServiceImpl implements CataService {

    /**
     * cataDAO
     */
    @Autowired
    private CataDAO cataDAO;

    /**
     *
     */
    @Override
    public Date calcularDiasHabiles(Date fcDesde, Integer iDias, String cdMuniAdministracion,
            String cdMuniInteresado) throws ServiceException {

        Date calcularDiasHabiles = null;
        try {
            calcularDiasHabiles = this.cataDAO.calcularDiasHabiles(fcDesde, iDias, cdMuniAdministracion,
                    cdMuniInteresado);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }

        return calcularDiasHabiles;
    }
    
    
    
    
    @Override
    public Date calcularDiasHabilesInicio(Date fcDesde, String cdMuniAdministracion,
            String cdMuniInteresado) throws ServiceException {

        Date calcularDiasHabiles = null;
        try {
            calcularDiasHabiles = this.cataDAO.calcularDiasHabilesInicio(fcDesde, cdMuniAdministracion,
                    cdMuniInteresado);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }

        return calcularDiasHabiles;
    }

}
