package org.madrid.saii.tramitacion.init;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import atlas.test.base.config.HabilitarWebTest;

/**
 * Clase de configuración que carga el contexto de Spring necesario para la
 * ejecución de test unitarios. Se sustituye la carga de ficheros XML de
 * configuración de versiones ATLAS 1.3.x y anteriores por anotaciones.
 *
 * En este @configuration incluimos los imports y resources necesarios para que
 * se levante nuestra aplicación, también añadiremos @habilitarWebTest para que
 * se levante el contexto WEB, también
 * exiten @HabilitarRestTest, @HabilitarWebServicesTest
 * y @HabilitarStandAloneTest los cuales levantan contextos diferentes, si se
 * desean utilizar estos profiles para los test, se debe incluir la dependencia
 * del starter en scope test en el proyecto ya que sino falla en ejecución ya
 * que no encuentra las clases de contexto.
 *
 * @author Madrid Digital
 *
 */
@Configuration
@HabilitarWebTest
@Import({ApplicationContextSaiiTramitacion.class })
public class ApplicationContextPruebasSaiiTramitacion {

}
