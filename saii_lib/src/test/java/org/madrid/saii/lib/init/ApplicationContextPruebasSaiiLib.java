package org.madrid.saii.lib.init;

import org.madrid.saii.lib.domain.ConvocatoriaDoc;
import org.madrid.saii.lib.util.ApplicationContextProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

import atlas.starter.crypt.AtlasHabilitaConfiguracionCrypt;
import atlas.starter.eadm.AtlasHabilitaConfiguracionEadm;
import atlas.starter.enviocorreo.conf.AtlasHabilitaConfiguracionEnvioCorreo;
import atlas.starter.fica.AtlasHabilitaConfiguracionFica;
import atlas.test.base.config.HabilitarStandAloneTest;
import org.springframework.context.annotation.Import;

/**
 * Clase de configuración que carga el contexto de Spring necesario para la
 * ejecución de test unitarios. Se sustituye la carga de ficheros XML de
 * configuración de versiones ATLAS 1.3.x y anteriores por anotaciones.
 *
 * En este @configuration incluimos los imports y resources necesarios para que
 * se levante nuestra aplicación, también añadiremos @HabilitarStandAloneTest
 * para que se levante el contexto BTACH, DESKTOP, JAR, también
 * exiten @HabilitarRestTest, @HabilitarWebServicesTest y @HabilitarWebTest los
 * cuales levantan contextos diferentes, si se desean utilizar estos profiles
 * para los test, se debe incluir la dependencia del starter en scope test en el
 * proyecto ya que sino falla en ejecución ya que no encuentra las clases de
 * contexto.
 *
 * @author everis
 *
 */
@Configuration
@HabilitarStandAloneTest
@AtlasHabilitaConfiguracionFica
@AtlasHabilitaConfiguracionEadm
@AtlasHabilitaConfiguracionCrypt
@AtlasHabilitaConfiguracionEnvioCorreo
@ComponentScan(basePackages = {"org.madrid.saii.lib" }, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, value = ConvocatoriaDoc.class) })
public class ApplicationContextPruebasSaiiLib {

}
