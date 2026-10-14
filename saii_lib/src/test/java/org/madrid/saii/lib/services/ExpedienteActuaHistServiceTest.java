package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.domain.ExpedienteActuaHist;
import org.madrid.saii.lib.dto.ExpedienteActuaHistDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.busquedas.domain.AtlasFilter;
import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.log4j.Log4j2;

/**
 * Test ExpedienteActuaHistService
 */
@ContextConfiguration(classes = { ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Log4j2
public class ExpedienteActuaHistServiceTest extends AtlasAbstractBaseTest {

	/**
	 * Servicio
	 */
	@Autowired
	@Qualifier("expedienteActuaHistService")
	private ExpedienteActuaHistService service;

	/**
	 * Session factory de hibernate
	 */
	@Resource
	private SessionFactory sessionFactory;

	/**
	 * Asocia la sessionFactory de Hibernate a la variable interna
	 *
	 * @param sessionFactory la sessionFactory
	 */
	public void setSessionFactory(final SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	/**
	 * Inicialización antes de empezar los test
	 */
	@Before
	@Override
	public void setUp() {
		assertNotNull(this.service);
	}

	/**
	 * Prueba el metodo guardar
	 */
	@Test
	public void testGuardar() {
		try {

			// GUARDAR
			ExpedienteActuaHistDTO dto = new ExpedienteActuaHistDTO();
			// dto.setDsConvocatoria("PRUEBA");
			// dto.setCdTipoProc("01");
			// dto.setDsTipoProc("Des Tipo Proc");
			// dto.setFcConvocatoria(new Date());
			// dto.setFcInicioPres(new Date());
			// dto.setFcFinPres(new Date());
			// dto.setFcInicioTrib(new Date());
			// dto.setFcFinTrib(new Date());

			dto = this.service.guardar(dto);

			// No es nulo
			Assert.assertNotNull(dto);

			// Está relleno
			this.testAtributos(dto, Boolean.TRUE);

			ExpedienteActuaHist ent = this.sessionFactory.getCurrentSession().get(ExpedienteActuaHist.class,
					dto.getIdExpedienteActuaHist());
			this.sessionFactory.getCurrentSession().evict(ent);

			// Guardamos
			// dto.setDsConvocatoria("Prueba Test Mod");
			dto = this.service.guardar(dto);

			// La modificación es correcta
			// Assert.assertEquals("Prueba Test Mod", dto.getDsConvocatoria());

		} catch (final ServiceException e) {
			log.error(e.getMessage());
			fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
		}
	}

	/**
	 * Prueba el método obtener
	 */
	@Test
	public void testObtener() {
		try {
			List<ExpedienteActuaHistDTO> lstDTO = this.service.obtener();

			// No es nulo
			Assert.assertNotNull(lstDTO);

			// Está relleno
			Assert.assertFalse(lstDTO.isEmpty());

			// Comprobamos los atributos obligatorios
			// El DTO tiene que ser null safe.
			Iterator<ExpedienteActuaHistDTO> it = lstDTO.iterator();
			while (it.hasNext()) {
				ExpedienteActuaHistDTO dto = it.next();

				this.testAtributos(dto, Boolean.FALSE);
			}

		} catch (final ServiceException e) {
			log.error(e.getMessage());
			fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
		}
	}

	@Test
	public void testBuscarPorId() {
		try {
			ExpedienteActuaHistDTO dto = this.service.buscarId(1);

			// No es nulo
			Assert.assertNotNull(dto);

			// Está relleno el id
			Assert.assertNotNull(dto.getIdExpedienteActuaHist());

			this.testAtributos(dto, Boolean.FALSE);
		} catch (final ServiceException e) {
			log.error(e.getMessage());
			fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
		}
	}

	@Test
	public void testBuscar() {
		try {
			List<ExpedienteActuaHistDTO> lstDTO = this.service.buscar(0, -1, null, null);

			// No es nulo
			Assert.assertNotNull(lstDTO);

			// Está relleno
			Assert.assertFalse(lstDTO.isEmpty());

			// Comprobamos los atributos obligatorios
			// El DTO tiene que ser null safe.
			Iterator<ExpedienteActuaHistDTO> it = lstDTO.iterator();
			while (it.hasNext()) {
				ExpedienteActuaHistDTO dto = it.next();

				this.testAtributos(dto, Boolean.FALSE);
			}

			// Búsqueda vacia
			List<AtlasFilter> filtros = new ArrayList<>();
			filtros.add(new AtlasFilter("id", AtlasFilter.Operador.EMPTY));

			lstDTO = this.service.buscar(0, -1, filtros, null);

			// No es nulo
			Assert.assertNotNull(lstDTO);

			// Está vacio
			Assert.assertTrue(lstDTO.isEmpty());

		} catch (final ServiceException e) {
			log.error(e.getMessage());
			fail(Constantes.Test.TEST_ERROR_PRUEBA + e.getMessage());
		}
	}

	/**
	 * Comprueba los atributos
	 *
	 * @param dto
	 * @parama relleno
	 */
	private void testAtributos(ExpedienteActuaHistDTO dto, Boolean bRelleno) {

		log.debug(dto.toString());

		// Obligatorios
		Assert.assertNotNull(dto.getIdExpedienteActuaHist());

		Assert.assertNotNull(dto.getIdExpedienteActua());

		Assert.assertNotNull(dto.getExpedienteDTO());

		Assert.assertNotNull(dto.getTipoFinanciacionDTO());

		Assert.assertTrue(StringUtils.isNotBlank(dto.getDsProveedor()));

		Assert.assertNotNull(dto.getNmImporte());

		Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuAlta()));

		Assert.assertNotNull(dto.getFcAltaFila());

		if (Boolean.TRUE.equals(bRelleno)) {
			Assert.assertTrue(StringUtils.isNotBlank(dto.getTlObservacion()));

			Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuModif()));

			Assert.assertNotNull(dto.getFcModifFila());

			Assert.assertTrue(StringUtils.isNotBlank(dto.getCdUsuBaja()));

			Assert.assertNotNull(dto.getFcBajaFila());
		}
		Assert.assertNotNull(dto.getTlObservacion());

		Assert.assertNotNull(dto.getCdUsuModif());

		Assert.assertNotNull(dto.getCdUsuBaja());

	}

}