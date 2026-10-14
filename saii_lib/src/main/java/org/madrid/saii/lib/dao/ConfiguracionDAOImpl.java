package org.madrid.saii.lib.dao;

import org.madrid.saii.lib.domain.Configuracion;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Repository("configuracionDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class ConfiguracionDAOImpl extends BaseDAOImpl<Configuracion, Integer> implements ConfiguracionDAO {

}
