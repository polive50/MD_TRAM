package org.madrid.saii.lib.dao;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.query.Query;
import org.madrid.saii.lib.domain.Convocatoria;
import org.madrid.saii.lib.domain.ConvocatoriaArea;
import org.madrid.saii.lib.domain.EnvioNotificacionesPortaFirma;
import org.madrid.saii.lib.domain.Expediente;
import org.madrid.saii.lib.dto.ConvocatoriaAreaDTO;
import org.madrid.saii.lib.dto.ConvocatoriaDTO;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteFiltrosDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaAreaToDTO;
import org.madrid.saii.lib.dto.converters.ConvocatoriaToDTO;
import org.madrid.saii.lib.dto.converters.ExpedienteToDTO;
import org.madrid.saii.lib.util.Constantes;
import org.madrid.saii.lib.util.Utils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.busquedas.domain.AtlasFilter;
import atlas.busquedas.domain.AtlasFilter.Operador;

/**
 * Implementacion para el DAO de la clase de dominio Expediente
 *
 * @author everis
 */
@Repository("envioNotificacionesPortafirmasDAO")
@Transactional(readOnly = true, propagation = Propagation.SUPPORTS)
public class EnvioNotificacionesPortafirmasDAOImpl extends BaseDAOImpl<EnvioNotificacionesPortaFirma, Integer> implements EnvioNotificacionesPortafirmasDAO {


    
  
}
