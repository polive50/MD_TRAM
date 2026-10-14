package org.madrid.saii.lib.services.adme;

import java.io.ByteArrayInputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.madrid.crypt.services.AfcOpServidorService;
import org.madrid.saii.lib.dto.ExpedienteDTO;
import org.madrid.saii.lib.dto.ExpedienteJusTecDTO;
import org.madrid.saii.lib.util.Constantes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import atlas.core.exceptions.ServiceException;
import atlas.eadm.eregws2.domain.EregAnexo;
import atlas.eadm.eregws2.domain.EregAnexoDocumIN;
import atlas.eadm.eregws2.domain.EregFichSolicitudDocumIN;
import atlas.eadm.eregws2.domain.EregRegistrarEntradaIN;
import atlas.eadm.eregws2.domain.EregRegistrarEntradaOUT;
import atlas.eadm.eregws2.domain.EregRelAnexosUnificadoOUT;
import atlas.eadm.services.EregAltaService;
import atlas.eadm.services.EregConsultaService;
import lombok.extern.slf4j.Slf4j;

/**
 * The Class EregAdmeService.
 *
 * @author Everis
 */
@Service("eregAdmeService")
@Transactional(readOnly = true, propagation = Propagation.REQUIRED, rollbackFor = {
        ServiceException.class, RuntimeException.class })
@Slf4j
public class EregAdmeServiceImpl implements EregAdmeService {

    // ////////////////////////////////////////////////////////
    // /////////////////////////////// SERVICIOS
    // ////////////////////////////////////////////////////////

    /** The ereg alta service. */
    @Autowired
    @Qualifier("eregAltaService")
    private EregAltaService eregAltaService;

    /** The ereg consulta service. */
    @Autowired
    @Qualifier("eregConsultaService")
    private EregConsultaService eregConsultaService;

    /** The afc op servidor service. */
    @Autowired
    @Qualifier("afcOpServidorService")
    private AfcOpServidorService afcOpServidorService;

    /**
     *
     */
    @Override
    public byte[] obtenerPdfPlantilla(String plantilla) throws ServiceException {

        log.info("Método obtenerPdfPlantilla: " + plantilla);

        byte[] pdfPlantilla = null;

        try {
            pdfPlantilla = this.eregAltaService.getFormulario(plantilla);
        } catch (ServiceException e) {
            log.error("obtenerPdfPlantilla: getFormulario: " + e.getMessage());
            throw new ServiceException(
                    "EregService.obtenerPdfPlantilla: eregAltaService.getFormulario : "
                            + e.getMessage());
        }
        return pdfPlantilla;
    }

    /**
     *
     */
    @Override
    @Transactional(readOnly = false)
    public byte[] firmarDatos(byte[] firmado) throws ServiceException {
        try {
            // TODO - cambio temporal
            return this.afcOpServidorService.firmarDatos(firmado, "SAII_FIRMA_SERV");
        } catch (Exception e) {
            log.error("ERROR firmar fichero tamaño: " + firmado.length);
            log.error("ERROR firmar fichero: " + e.getMessage());
            throw new ServiceException(
                    "No es posible firmar la solicitud en estos momentos: " + e.getMessage());
        }
    }

    /**
     *
     */
    @Override
    public ExpedienteDTO generarEntrada(ExpedienteDTO dto, String cdasunto, String cdunidad)
            throws ServiceException {

        ExpedienteDTO dtoSalida = dto;

        try {
            EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
            entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
            entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
            entrada.setCod_asunto(cdasunto);
            entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());

            entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());

            entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());

            if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
                entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setInterApellido1("-");
                entrada.setInterApellido2("-");
                entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreApellido1("-");
                entrada.setPreApellido2("-");
            }

            entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
            entrada.setCdunidaddest("09" + cdunidad);

            EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);

            if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                    .equals(salida.getCdstatus())) {
                dtoSalida.setCdEreg(salida.getCddocum());
                dtoSalida.setDsDestinoEreg(salida.getDsunidaddest());
            } else {
                log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                        + " del expediente: " + dto.toString());
                throw new ServiceException(salida.getDsstatus());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
        }

        return dtoSalida;
    }
    

   @Override
   public ExpedienteJusTecDTO generarEntradaJCT(ExpedienteDTO dto, String cdasunto, String cdunidad, ExpedienteJusTecDTO expedienteJustec, Boolean subsanar)
           throws ServiceException {

       ExpedienteJusTecDTO dtoSalida = expedienteJustec;

       try {
           EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
           entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
           entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
           entrada.setCod_asunto(cdasunto);
           entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());

           entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
           entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
           entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());

           entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
           entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
           entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
           entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());

           if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
               entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
               entrada.setInterApellido1("-");
               entrada.setInterApellido2("-");
               entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
               entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
               entrada.setPreApellido1("-");
               entrada.setPreApellido2("-");
           }

           entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
           entrada.setCdunidaddest("09" + cdunidad);

           EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);

           if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                   .equals(salida.getCdstatus())) {
               if (Boolean.TRUE.equals(subsanar)) {
                   dtoSalida.setCdEregSubsana(salida.getCddocum());
               }else {
                   dtoSalida.setCdEregPresent(salida.getCddocum());
               }
               
               dtoSalida.setDsDestinoEreg(salida.getDsunidaddest());
           } else {
               log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                       + " del expediente: " + dto.toString());
               throw new ServiceException(salida.getDsstatus());
           }
       } catch (Exception e) {
           log.error(e.getMessage());
           throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
       }

       return dtoSalida;
   }
   
   
   @Override
   public ExpedienteDTO generarEntradaContrato(ExpedienteDTO dto, String cdasunto, String cdunidad)
           throws ServiceException {

       ExpedienteDTO dtoSalida = dto;

       try {
           EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
           entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
           entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
           entrada.setCod_asunto(cdasunto);
           entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());

           entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
           entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
           entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());

           entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
           entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
           entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
           entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());

           if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
               entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
               entrada.setInterApellido1("-");
               entrada.setInterApellido2("-");
               entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
               entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
               entrada.setPreApellido1("-");
               entrada.setPreApellido2("-");
           }

           entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
           entrada.setCdunidaddest("09" + cdunidad);

           EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);

           if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                   .equals(salida.getCdstatus()) && dtoSalida.getExpedienteContrato() != null) {
               dtoSalida.getExpedienteContrato().setCdEregPresent(salida.getCddocum());
               dtoSalida.getExpedienteContrato().setDsDestinoEreg(salida.getDsunidaddest());
           } else {
               log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                       + " del expediente: " + dto.toString());
               throw new ServiceException(salida.getDsstatus());
           }
       } catch (Exception e) {
           log.error(e.getMessage());
           throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
       }

       return dtoSalida;
   }

    /**
     *
     */
    @Override
    public String anexarFicherosDocum(String cdEreg, byte[] fichero, String tipo,
            String cdModuloTecnico) throws ServiceException {

        String cdAnexo = null;

        try {

            EregFichSolicitudDocumIN entrada = new EregFichSolicitudDocumIN();
            entrada.setCdDocum(cdEreg);
            entrada.setCdModuloTec(cdModuloTecnico);
            entrada.setCdTipoFichero(tipo);
            entrada.setFichero(new ByteArrayInputStream(fichero));

            log.info("Llamada al servicio eregAltaService.AnexarFichSolicitudDocum");
            log.info("Parámetro CdDocum: " + cdEreg);
            log.info("Parámetro CdModuloTec: " + cdModuloTecnico);
            log.info("Parámetro entrada: " + entrada.toString());
            log.info("Parámetro CdTipoFichero: " + tipo);
            /** 12/12/2024 Quitamos Log innecesario
            log.info("Parámetro Fichero: " +  Base64.getEncoder().encodeToString(fichero));
            */
            

            cdAnexo = this.eregAltaService.AnexarFichSolicitudDocum(entrada);

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(
                    "Ocurrió un error al anexar fichero documento en el servicio EREG");
        }

        return cdAnexo;
    }

    /**
     *
     */
    @Override
    public String anexarFicheroAnexo(String cdEreg, byte[] fichero, String nombreAnexo)
            throws ServiceException {
        String cdAnexo = null;
        try {
            EregAnexoDocumIN entrada = new EregAnexoDocumIN();
            entrada.setAnexoInputStream(new ByteArrayInputStream(fichero));
            entrada.setDescripcion(nombreAnexo);
            entrada.setCdmimetype("pdf");
            entrada.setCddocum(cdEreg);

            log.info("Llamada al servicio eregAltaService.loadAnexoDocum");
            log.info("Parametro AnexoInputStream: " + fichero);
            log.info("Parametro Descripcion: " + nombreAnexo);
            log.info("Parametro Cdmimetype: " + "pdf");
            log.info("Parametro Cddocum: " + cdEreg);

            log.info("Parametro EregAnexoDocumIN: " + entrada.toString());

            cdAnexo = this.eregAltaService.loadAnexoDocum(entrada);
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException(
                    "Ocurrió un error al anexar fichero anexo en el servicio EREG");
        }
        return cdAnexo;
    }

    /**
     *
     */
    @Override
    public byte[] descargarAnexoPorCodigo(String cdEreg, String cdAnexo) throws ServiceException {
        try {
            log.info("Se va a descargar anexo => cdEreg=" + cdEreg + ", cdAnexo=" + cdAnexo);
            byte[] result = this.eregConsultaService.downloadAnexoDocum(cdEreg, cdAnexo);
            if(result != null) {
                log.info("Resultado de descargar anexo: " + result.length);
            }
            return result;
        } catch (Exception e) {
            log.error("Error al descargar anexo por código: " + e.getMessage());
            log.error("Error al descargar anexo => cdEreg=" + cdEreg + ", cdAnexo=" + cdAnexo);
            throw new ServiceException(
                    "No es posible descargar el Anexo en estos momentos." + e.getMessage());
        }
    }

    /**
     *
     */
    @Override
    public byte[] descargarAnexoPorTipo(String cdEreg, String strTipoAnexo)
            throws ServiceException {
        try {

            String cdAnexo = null;

            // Obtenemos los anexos del cdEreg
            EregRelAnexosUnificadoOUT obtenerRelAnexosEreg = this.obtenerRelAnexosEreg(cdEreg);
            List<EregAnexo> anexos = obtenerRelAnexosEreg.getAnexos();

            // Recorremos los anexos para encontrar el cdAnexo que corresponde
            // al tipo
            Iterator<EregAnexo> iterator = anexos.iterator();
            while (iterator.hasNext()) {
                EregAnexo eregAnexo = iterator.next();
                if (strTipoAnexo.equals(eregAnexo.getDescripcion())) {
                    cdAnexo = eregAnexo.getCod_archivo();
                }
            }

            return this.eregConsultaService.downloadAnexoDocum(cdEreg, cdAnexo);

        } catch (Exception e) {
            log.error("Error al descargar anexo por tipo: " + e.getMessage());
            throw new ServiceException(
                    "No es posible descargar el PDF en estos momentos." + e.getMessage());
        }
    }

    /**
     *
     */
    @Override
    public EregRelAnexosUnificadoOUT obtenerRelAnexosEreg(String cdEreg) throws ServiceException {
        try {
            return this.eregAltaService.getRelAnexosUnificado(cdEreg);
        } catch (Exception e) {
            log.error("Error al obtener la relación de anexos: " + e.getMessage());
            throw new ServiceException(
                    "No es posible obtener la relación de anexos en estos momentos."
                            + e.getMessage());
        }
    }
    
    @Override
    public ExpedienteDTO generarEntradaPresupPlazosOtros(ExpedienteDTO dto, String cdasunto, String cdunidad, Boolean subsanar)
            throws ServiceException {

        ExpedienteDTO dtoSalida = dto;

        try {
            EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
            entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
            entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
            entrada.setCod_asunto(cdasunto);
            entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());

            entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());

            entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());

            if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
                entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setInterApellido1("-");
                entrada.setInterApellido2("-");
                entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreApellido1("-");
                entrada.setPreApellido2("-");
            }

            entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
            entrada.setCdunidaddest("09" + cdunidad);

            EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);

            if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                    .equals(salida.getCdstatus())) {
                if (Boolean.TRUE.equals(subsanar)) {
                    dtoSalida.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO().setCdEregSubsana(salida.getCddocum());
                }else {
                    dtoSalida.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO().setCdEregPresent(salida.getCddocum());
                }
                
                dtoSalida.getExpedienteModifOtraDTO().getExpedienteModifPresPlazoOtraDTO().setDsDestinoEreg(salida.getDsunidaddest());
            } else {
                log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                        + " del expediente: " + dto.toString());
                throw new ServiceException(salida.getDsstatus());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
        }

        return dtoSalida;
    }
    
    @Override
    public ExpedienteDTO generarEntradaSubsanarReq(ExpedienteDTO dto, String cdasunto, String cdunidad,
            Boolean subsanar)
            throws ServiceException {
        
        ExpedienteDTO dtoSalida = dto;
        
        try {
            EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
            entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
            entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
            entrada.setCod_asunto(cdasunto);
            entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());
            
            entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());
            
            entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());
            
            if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
                entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setInterApellido1("-");
                entrada.setInterApellido2("-");
                entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreApellido1("-");
                entrada.setPreApellido2("-");
            }
            
            entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
            entrada.setCdunidaddest("09" + cdunidad);
            
            EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);
            
            if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                    .equals(salida.getCdstatus())) {

                dtoSalida.setCdEregSubsanaReq(salida.getCddocum());
                dtoSalida.setDsDestinoEreg(salida.getDsunidaddest());
            } else {
                log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                + " del expediente: " + dto.toString());
                throw new ServiceException(salida.getDsstatus());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
        }
        
        return dtoSalida;
    }

    @Override
    public ExpedienteDTO generarEntradaSubsanarAdm(ExpedienteDTO dto, String cdAsunto,
            String cdUnidad, Boolean subsanar) throws ServiceException {
        ExpedienteDTO dtoSalida = dto;
        try {
            EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
            entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
            entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
            entrada.setCod_asunto(cdAsunto);
            entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());            
            entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());
            if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
                entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setInterApellido1("-");
                entrada.setInterApellido2("-");
                entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreApellido1("-");
                entrada.setPreApellido2("-");
            }
            entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
            entrada.setCdunidaddest("09" + cdUnidad);
            EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);
            if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                    .equals(salida.getCdstatus())) {

                dtoSalida.setCdEregSubsanaAdm(salida.getCddocum());
                dtoSalida.setDsDestinoEreg(salida.getDsunidaddest());
            } else {
                log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                + " del expediente: " + dto.toString());
                throw new ServiceException(salida.getDsstatus());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
        }
        return dtoSalida;
    }

    @Override
    public ExpedienteDTO generarEntradaAlegaciones(ExpedienteDTO dto, String cdAsunto,
            String cdUnidad) throws ServiceException {
        ExpedienteDTO dtoSalida = dto;
        try {
            EregRegistrarEntradaIN entrada = new EregRegistrarEntradaIN();
            entrada.setCdaplicacion(Constantes.AplicativoSaii.CODIGO_APLICATIVO);
            entrada.setCdorigenanoreg(EregAdmeService.ConstantesEreg.ENTRADA_EREG_INTERNET);
            entrada.setCod_asunto(cdAsunto);
            entrada.setInterNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setInterApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setInterApellido2(dto.getInteresadoDTO().getDsApellido2());            
            entrada.setPreNifcif(dto.getInteresadoDTO().getCdNumIden());
            entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
            entrada.setPreApellido1(dto.getInteresadoDTO().getDsApellido1());
            entrada.setPreApellido2(dto.getInteresadoDTO().getDsApellido2());
            if (StringUtils.isBlank(dto.getInteresadoDTO().getDsApellido1())) {
                entrada.setInterNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setInterApellido1("-");
                entrada.setInterApellido2("-");
                entrada.setInterRazonsocial(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreNombre(dto.getInteresadoDTO().getDsNombre());
                entrada.setPreApellido1("-");
                entrada.setPreApellido2("-");
            }
            entrada.setEstadodocumento(EregAdmeService.ConstantesEregEstado.ESTADO_DOCUMENTO_EREG);
            entrada.setCdunidaddest("09" + cdUnidad);
            EregRegistrarEntradaOUT salida = this.eregAltaService.registrarEntrada(entrada);
            if (EregAdmeService.ConstantesEregEstado.ESTADO_SIN_ERRORES
                    .equals(salida.getCdstatus())) {

                dtoSalida.setCdEregAlegaciones(salida.getCddocum());
                dtoSalida.setDsDestinoEreg(salida.getDsunidaddest());
            } else {
                log.error("Error al registrar la entrada en EREG con código " + salida.getCdstatus()
                + " del expediente: " + dto.toString());
                throw new ServiceException(salida.getDsstatus());
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new ServiceException("Ocurrió un error al registrar entrada en el servicio EREG");
        }
        return dtoSalida;
    }
    
    
    
}
