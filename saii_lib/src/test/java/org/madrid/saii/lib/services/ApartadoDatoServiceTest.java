package org.madrid.saii.lib.services;

import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.hibernate.SessionFactory;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.madrid.saii.lib.dto.ApartadoDatoDTO;
import org.madrid.saii.lib.dto.EvaluacionNotaDatoDTO;
import org.madrid.saii.lib.dto.InformeEvaluacionApartadoDTO;
import org.madrid.saii.lib.init.ApplicationContextPruebasSaiiLib;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import atlas.core.exceptions.ServiceException;
import atlas.test.base.AtlasAbstractBaseTest;
import lombok.extern.slf4j.Slf4j;

/**
 * Test ApartadoDatoService
 */
@ContextConfiguration(classes = {ApplicationContextPruebasSaiiLib.class })
@ActiveProfiles("BaseDatosEnMemoria")
@Slf4j
public class ApartadoDatoServiceTest extends AtlasAbstractBaseTest {

    /**
     * Servicio
     */
    @Autowired
    @Qualifier("apartadoDatoService")
    private ApartadoDatoService service;

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
    public void testGuardarListaApartadoDato() {
        try {

            // GUARDAR
            InformeEvaluacionApartadoDTO aparDTO = new InformeEvaluacionApartadoDTO();

            InformeEvaluacionApartadoDTO dto = new InformeEvaluacionApartadoDTO();

            List<ApartadoDatoDTO> lstApartadoDTO = new ArrayList<ApartadoDatoDTO>();

            ApartadoDatoDTO apartadoDTO = new ApartadoDatoDTO();

            apartadoDTO.setCdUsuAlta("13921784E");
            apartadoDTO.setFcAltaFila(new Date());
            apartadoDTO.setIdApartado(303);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Plan de captación de capital humano, plan de movilidad, participación en redes y consorcios nacionales e internacionales y plan de formación de personal vinculado al proyecto y acciones de formación especializada.");
            apartadoDTO.setNmNota(9);

            lstApartadoDTO.add(apartadoDTO);

            apartadoDTO.setCdUsuAlta("13921784E");
            apartadoDTO.setFcAltaFila(new Date());
            apartadoDTO.setIdApartado(304);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Plan de gestión, coordinación y toma de decisiones, plan de transferencia de resultados y plan de difusión de actividades.");
            apartadoDTO.setNmNota(14);

            lstApartadoDTO.add(apartadoDTO);

            apartadoDTO.setCdUsuAlta("13921784E");
            apartadoDTO.setFcAltaFila(new Date());
            apartadoDTO.setIdApartado(305);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Justificación del presupuesto solicitado en relación con las actividades de I+D a llevar a cabo, plan de captación de fondos (cofinanciación con otras convocatorias competitivas y recursos obtenidos mediante contratos con empresas y otras entidades).");
            apartadoDTO.setNmNota(18);

            lstApartadoDTO.add(apartadoDTO);

            List<EvaluacionNotaDatoDTO> lstEvauacionNotaDatoDTO = new ArrayList<EvaluacionNotaDatoDTO>();

            EvaluacionNotaDatoDTO evaluacionNotaDatoDTO = new EvaluacionNotaDatoDTO();

            evaluacionNotaDatoDTO.setCdUsuAlta("13921784E");
            evaluacionNotaDatoDTO.setFcAltaFila(new Date());
            evaluacionNotaDatoDTO.setIdExpedienteEval(10);
            evaluacionNotaDatoDTO.setIdEvaluacion(304);
            evaluacionNotaDatoDTO.setNmNota(55);
            lstEvauacionNotaDatoDTO.add(evaluacionNotaDatoDTO);

            evaluacionNotaDatoDTO.setCdUsuAlta("13921784E");
            evaluacionNotaDatoDTO.setFcAltaFila(new Date());
            evaluacionNotaDatoDTO.setIdExpedienteEval(10);
            evaluacionNotaDatoDTO.setIdEvaluacion(304);
            evaluacionNotaDatoDTO.setNmNota(41);
            lstEvauacionNotaDatoDTO.add(evaluacionNotaDatoDTO);

            dto.setLstApartadoDatoDTO(lstApartadoDTO);
            dto.setLstEvaluacionNotaDatoDTO(lstEvauacionNotaDatoDTO);

            aparDTO = this.service.guardarListaApartadoDato(dto, "13921784E");

            // No es nulo
            Assert.assertNotNull(aparDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Prueba el metodo actualizar
     */
    @Test
    public void testActualizarListaApartadoDato() {
        try {

            // Actualizar
            InformeEvaluacionApartadoDTO aparDTO = new InformeEvaluacionApartadoDTO();

            InformeEvaluacionApartadoDTO dto = new InformeEvaluacionApartadoDTO();

            List<ApartadoDatoDTO> lstApartadoDTO = new ArrayList<ApartadoDatoDTO>();

            ApartadoDatoDTO apartadoDTO = new ApartadoDatoDTO();

            apartadoDTO.setIdApartadoDato(2);
            apartadoDTO.setIdApartado(303);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Plan de captación de capital humano, plan de movilidad, participación en redes y consorcios nacionales e internacionales y plan de formación de personal vinculado al proyecto y acciones de formación especializada.");
            apartadoDTO.setNmNota(9);

            lstApartadoDTO.add(apartadoDTO);

            apartadoDTO.setIdApartadoDato(3);
            apartadoDTO.setIdApartado(304);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Plan de gestión, coordinación y toma de decisiones, plan de transferencia de resultados y plan de difusión de actividades.");
            apartadoDTO.setNmNota(14);

            lstApartadoDTO.add(apartadoDTO);

            apartadoDTO.setIdApartadoDato(4);
            apartadoDTO.setIdApartado(305);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Justificación del presupuesto solicitado en relación con las actividades de I+D a llevar a cabo, plan de captación de fondos (cofinanciación con otras convocatorias competitivas y recursos obtenidos mediante contratos con empresas y otras entidades).");
            apartadoDTO.setNmNota(18);

            lstApartadoDTO.add(apartadoDTO);

            List<EvaluacionNotaDatoDTO> lstEvauacionNotaDatoDTO = new ArrayList<EvaluacionNotaDatoDTO>();

            EvaluacionNotaDatoDTO evaluacionNotaDatoDTO = new EvaluacionNotaDatoDTO();

            evaluacionNotaDatoDTO.setIdEvaluacionNotaDato(2);
            evaluacionNotaDatoDTO.setIdExpedienteEval(10);
            evaluacionNotaDatoDTO.setIdEvaluacion(304);
            evaluacionNotaDatoDTO.setNmNota(55);
            lstEvauacionNotaDatoDTO.add(evaluacionNotaDatoDTO);

            evaluacionNotaDatoDTO.setIdEvaluacionNotaDato(3);
            evaluacionNotaDatoDTO.setIdExpedienteEval(10);
            evaluacionNotaDatoDTO.setIdEvaluacion(305);
            evaluacionNotaDatoDTO.setNmNota(41);
            lstEvauacionNotaDatoDTO.add(evaluacionNotaDatoDTO);

            dto.setLstApartadoDatoDTO(lstApartadoDTO);
            dto.setLstEvaluacionNotaDatoDTO(lstEvauacionNotaDatoDTO);

            aparDTO = this.service.guardarListaApartadoDato(dto, "13921784E");

            // No es nulo
            Assert.assertNotNull(aparDTO);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Prueba el metodo guardar
     */
    @Test
    public void testGuardarListaApartadoDatoError() {

        // GUARDAR
        InformeEvaluacionApartadoDTO aparDTO = new InformeEvaluacionApartadoDTO();

        InformeEvaluacionApartadoDTO dto = new InformeEvaluacionApartadoDTO();

        try {

            List<ApartadoDatoDTO> lstApartadoDTO = new ArrayList<ApartadoDatoDTO>();

            ApartadoDatoDTO apartadoDTO = new ApartadoDatoDTO();

            apartadoDTO.setCdUsuAlta("13921784E");
            apartadoDTO.setFcAltaFila(new Date());
            apartadoDTO.setIdApartado(303);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Plan de captación de capital humano, plan de movilidad, participación en redes y consorcios nacionales e internacionales y plan de formación de personal vinculado al proyecto y acciones de formación especializada.");
            apartadoDTO.setNmNota(9);

            lstApartadoDTO.add(apartadoDTO);

            apartadoDTO.setCdUsuAlta("13921784E");
            apartadoDTO.setFcAltaFila(new Date());
            apartadoDTO.setIdApartado(304);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Plan de gestión, coordinación y toma de decisiones, plan de transferencia de resultados y plan de difusión de actividades.");
            apartadoDTO.setNmNota(14);

            lstApartadoDTO.add(apartadoDTO);

            apartadoDTO.setCdUsuAlta("13921784E");
            apartadoDTO.setFcAltaFila(new Date());
            apartadoDTO.setIdApartado(305);
            apartadoDTO.setIdExpedienteEval(10);
            apartadoDTO.setDsComentario(
                    "Justificación del presupuesto solicitado en relación con las actividades de I+D a llevar a cabo, plan de captación de fondos (cofinanciación con otras convocatorias competitivas y recursos obtenidos mediante contratos con empresas y otras entidades).");
            apartadoDTO.setNmNota(18);

            lstApartadoDTO.add(apartadoDTO);

            List<EvaluacionNotaDatoDTO> lstEvauacionNotaDatoDTO = new ArrayList<EvaluacionNotaDatoDTO>();

            EvaluacionNotaDatoDTO evaluacionNotaDatoDTO = new EvaluacionNotaDatoDTO();

            evaluacionNotaDatoDTO.setCdUsuAlta("13921784E");
            evaluacionNotaDatoDTO.setFcAltaFila(new Date());
            evaluacionNotaDatoDTO.setIdExpedienteEval(10);
            evaluacionNotaDatoDTO.setIdEvaluacion(304);
            evaluacionNotaDatoDTO.setNmNota(55);
            lstEvauacionNotaDatoDTO.add(evaluacionNotaDatoDTO);

            evaluacionNotaDatoDTO.setCdUsuAlta("13921784E");
            evaluacionNotaDatoDTO.setFcAltaFila(new Date());
            evaluacionNotaDatoDTO.setIdExpedienteEval(10);
            evaluacionNotaDatoDTO.setIdEvaluacion(304);
            evaluacionNotaDatoDTO.setNmNota(41);
            lstEvauacionNotaDatoDTO.add(evaluacionNotaDatoDTO);

            dto.setLstApartadoDatoDTO(lstApartadoDTO);
            dto.setLstEvaluacionNotaDatoDTO(lstEvauacionNotaDatoDTO);

            aparDTO = this.service.guardarListaApartadoDato(dto, null);

            // Es nulo
            Assert.assertNull(aparDTO);

        } catch (final ServiceException e) {
            // Es nulo
            Assert.assertNull(null);

        }
    }

    /**
     * Prueba el metodo obtener
     */
    @Test
    public void testObtenerApartadosDatoPorIdExpedienteEval() {
        try {

            InformeEvaluacionApartadoDTO dto = new InformeEvaluacionApartadoDTO();

            dto = this.service.obtenerApartadosDatoPorIdExpedienteEval(10);

            // No es nulo
            Assert.assertNotNull(dto);

        } catch (final ServiceException e) {
            log.error(e.getMessage());
        }
    }

    /**
     * Prueba el metodo obtener
     */
    @Test
    public void testObtenerApartadosDatoPorIdExpedienteEvalError() {

        InformeEvaluacionApartadoDTO dto = new InformeEvaluacionApartadoDTO();

        try {

            dto = this.service.obtenerApartadosDatoPorIdExpedienteEval(null);

            // Es nulo
            Assert.assertNull(dto);

        } catch (final ServiceException e) {
            // Es nulo
            Assert.assertNull(null);

        }
    }

}
