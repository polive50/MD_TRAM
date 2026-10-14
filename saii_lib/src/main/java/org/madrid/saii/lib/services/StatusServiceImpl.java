package org.madrid.saii.lib.services;

import org.madrid.saii.lib.dao.StatusDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.DataAccessException;
import atlas.core.exceptions.ServiceException;

/**
 * I
 *
 * @author Arquetipo web 2.1.X
 * @since 28-ene-2020 12:47:35
 *
 *
 */
@Service("statusService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
public class StatusServiceImpl implements StatusService {

    @Autowired
    @Qualifier("statusDAO")
    private StatusDAO statusDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer obtieneData() throws ServiceException {
        try {
            return this.statusDAO.obtieneData();
        } catch (DataAccessException dae) {
            throw new ServiceException(dae);
        }
    }
}
