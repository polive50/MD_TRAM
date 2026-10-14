package org.madrid.saii.lib.services.adme;

import java.util.Date;

import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.services.SiexService;
import atlas.eadm.siex.domain.SiexGrabarExpteApliIN;
import atlas.eadm.siex.domain.SiexGrabarExpteApliOut;
import atlas.eadm.siex.domain.SiexGrabarHitoApliIN;
import atlas.eadm.siex.domain.SiexGrabarHitoApliOut;
import atlas.eadm.siex.domain.SiexGrabarInteApliIN;
import atlas.eadm.siex.domain.SiexGrabarInteApliOut;
import atlas.eadm.siex.domain.SiexObtenerDatosExpteApliIN;
import atlas.eadm.siex.domain.SiexObtenerDatosExpteApliOut;
import atlas.eadm.siex.domain.SiexObtenerDatosInteApliIN;
import atlas.eadm.siex.domain.SiexObtenerDatosInteApliOut;
import lombok.extern.slf4j.Slf4j;

/**
 * Implementación del servicio SIEX Administracion
 *
 * @author everis
 */
@Service("siexAdmeService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class SiexAdmeServiceImpl implements SiexAdmeService {

    // /**
    // * Codigo de aplicacion
    // */
    @Value("${saii.codigo}")
    private String codigoAplicacion;

    /**
     */
    private static final String SIEX_ESTADO_VIVO = "VI";

    /**
     * Servicio SIEX
     */
    @Autowired
    @Qualifier("siexService")
    private SiexService siexService;

    @Override
    public String grabarExpteAPLI(ExpedienteDTO dto) throws ServiceException {

        String cdExpedienteSiex;

        SiexGrabarExpteApliIN in = new SiexGrabarExpteApliIN();

        // <ser:cdaplicacion>SAII</ser:cdaplicacion>
        // <ser:cdproc>SAII</ser:cdproc>
        // <ser:cdclave1>01</ser:cdclave1>
        // <ser:cdclave2>SAII</ser:cdclave2>
        // <ser:cdclave3>0001</ser:cdclave3>
        // <ser:cdclave4>2021</ser:cdclave4>
        // <ser:cdclave5></ser:cdclave5>
        // <ser:dsrefexped>01-SAII-0001.1/2021</ser:dsrefexped>
        // <ser:dsexped>SISTEMA DE AYUDAS DE INVESTIGACIÓN E
        // INNOVACIÓN</ser:dsexped>
        // <ser:fcinicio>18/06/2021</ser:fcinicio>
        // <ser:cddocum></ser:cddocum>
        // <ser:cdestado>VI</ser:cdestado>
        // <ser:datossingu></ser:datossingu>
        // <ser:cdusrmod>AE</ser:cdusrmod>
        // <ser:cdunidalta></ser:cdunidalta>
        // <ser:cdunidprop></ser:cdunidprop>
        // <ser:campoaux1></ser:campoaux1>
        // <ser:campoaux2></ser:campoaux2>
        // <ser:campoaux3></ser:campoaux3>

        // Parametros entrada
        // Cod. Aplicación invocación
        in.setCdAplicacion(this.codigoAplicacion);
        // Col. Procedimiento
        in.setCdProc(this.codigoAplicacion);
        // Clave interna de la aplicación
        in.setCdClave1(dto.getCdExpediente());
        // referencia del expediente
        in.setDsRefExped(dto.getCdExpediente());
        // descripción del expediente
        in.setDsExped("Sistema de ayudas de investigación e innovación");
        // fecha de inicio
        in.setFcInicio(Utils.dateToString(new Date()));
        // cód. estado del expediente
        in.setCdEstado(SIEX_ESTADO_VIVO);
        // observaciones del expediente
        in.setDatosSingu("");
        // Código de usuario
        in.setCdUsrmod(dto.getCdUsuAlta());
        // Código unidad que da de alta
        in.setCdUnidAlta(""); // TODO
        // código unidad propietaria del expediente
        in.setCdUnidProp(""); // TODO

        SiexGrabarExpteApliOut out = this.siexService.grabarExpteAPLI(in);

        if (!"0".equals(out.getCodigoRespuesta())) {
            log.error("Siex:" + out.getCodigoRespuesta() + " " + out.getDescripcionRespuesta());
            throw new ServiceException(out.getDescripcionRespuesta());
        }

        cdExpedienteSiex = out.getIdExp();

        return cdExpedienteSiex;
    }

    @Override
    public void grabarHitoAPLI(ExpedienteDTO dto) throws ServiceException {
        SiexGrabarHitoApliIN in = new SiexGrabarHitoApliIN();

        // Parametros entrada
        // Cod. Aplicación invocación
        in.setCdAplicacion(this.codigoAplicacion);
        // Cod. Hito
        // TODO: Devuelve este error: Siex:1 El Hito no está catalogado en el
        // S.I. de en SITUEXPE. Averiguar el cdHito
        in.setCdHito("RESO");
        // Col. Procedimiento
        in.setCdProc(this.codigoAplicacion);
        in.setCdUsrmod(dto.getCdUsuAlta());
        // observaciones del expediente
        in.setDatosSingu("");
        in.setFcHito(Utils.dateToString(new Date()));
        // Clave interna de la aplicación
        in.setCdClave1(dto.getIdExpediente().toString());

        SiexGrabarHitoApliOut out = this.siexService.grabarHitoAPLI(in);

        if (!"0".equals(out.getCodigoRespuesta())) {
            log.error("Siex:" + out.getCodigoRespuesta() + " " + out.getDescripcionRespuesta());
            throw new ServiceException(out.getDescripcionRespuesta());
        }
    }

    @Override
    public void grabarInteAPLI(ExpedienteDTO dto) throws ServiceException {
        SiexGrabarInteApliIN in = new SiexGrabarInteApliIN();

        // Parametros entrada
        // Cod. Aplicación invocación
        in.setCdAplicacion(this.codigoAplicacion);
        // Clave interna de la aplicación
        in.setCdClave1(dto.getCdExpediente());
        // Col. Procedimiento
        in.setCdProc(this.codigoAplicacion);
        // Código de usuario
        in.setCdUsrmod(dto.getCdUsuAlta());
        // Cod. Relacion
        in.setCdRelacion("IN");
        // Parametros interesado
        in.setCdDocIdent(dto.getInteresadoDTO().getCdNumIden());
        // TODO: Averiguar el código de duplicado
        in.setCdDuplicado("0");
        in.setDsApe1(dto.getInteresadoDTO().getDsApellido1());
        in.setDsApe2(dto.getInteresadoDTO().getDsApellido2());
        in.setDsEmail(dto.getInteresadoDTO().getDsEmail());
        in.setDsNombre(dto.getInteresadoDTO().getDsNombre());
        in.setDsRazon("");
        // Parametros representante
        in.setCdDocRepre("");
        in.setCdDuplicadoRepre("");
        in.setDsApe1Repr("");
        in.setDsApe2Repre("");
        in.setDsEmailRepre("");
        in.setDsNombreRepre("");
        in.setDsRazonRepre("");

        SiexGrabarInteApliOut out = this.siexService.grabarInteAPLI(in);

        if (!"0".equals(out.getCodigoRespuesta())) {
            log.error("Siex:" + out.getCodigoRespuesta() + " " + out.getDescripcionRespuesta());
            throw new ServiceException(out.getDescripcionRespuesta());

        }

    }

    /**
     *
     */
    @Override
    public String obtenerDatosExpteAPLI(ExpedienteDTO dto) throws ServiceException {

        SiexObtenerDatosExpteApliIN in = new SiexObtenerDatosExpteApliIN();

        // Cod. Aplicación invocación
        in.setCdAplicacion(this.codigoAplicacion);
        // Clave interna de la aplicación
        in.setCdClave1(dto.getCdExpediente());
        // Col. Procedimiento
        in.setCdProc(this.codigoAplicacion);

        SiexObtenerDatosExpteApliOut out = this.siexService.obtenerDatosExpteAPLI(in);

        if (!"0".equals(out.getCodigoRespuesta())) {
            log.error("Siex:" + out.getCodigoRespuesta() + " " + out.getDescripcionRespuesta());
            throw new ServiceException(out.getDescripcionRespuesta());

        }

        return out.getXmlOriginal();
    }

    @Override
    public void obtenerDatosHitoAPLI(ExpedienteDTO dto) throws ServiceException {
        // TODO Auto-generated method stub

    }

    /**
     *
     */
    @Override
    public void obtenerdatosInteAPLI(ExpedienteDTO dto) throws ServiceException {

        SiexObtenerDatosInteApliIN in = new SiexObtenerDatosInteApliIN();

        // Cod. Aplicación invocación
        in.setCdAplicacion(this.codigoAplicacion);
        // Clave interna de la aplicación
        in.setCdClave1(dto.getCdExpediente());
        in.setCdDocIdent(dto.getInteresadoDTO().getCdNumIden());
        // Col. Procedimiento
        in.setCdProc(this.codigoAplicacion);

        SiexObtenerDatosInteApliOut out = this.siexService.obtenerDatosInteAPLI(in);

        if (!"0".equals(out.getCodigoRespuesta())) {
            log.error("Siex:" + out.getCodigoRespuesta() + " " + out.getDescripcionRespuesta());
            throw new ServiceException(out.getDescripcionRespuesta());

        }

    }






}
