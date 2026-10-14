package org.madrid.saii.lib.services.adme;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.cove.domain.CoveInfo;
import atlas.eadm.services.CoveService;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio COVE Administracion
 *
 * @author nttdata
 */
@Service("coveAdmeService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class CoveAdmeServiceImpl implements CoveAdmeService {

    // /**
    // * Codigo de aplicacion
    // */
    @Value("${saii.codigo}")
    private String codigoAplicacion;

    /**
     * Servicio COVE
     */
    @Autowired
    @Qualifier("coveService")
    private CoveService coveService;

    @Override
    public void insertarCove() {

        CoveInfo coveInfo = new CoveInfo();

        // this.coveService.insertarCove(arg0, arg1, arg2, arg3, arg4, arg5)

    }

}
