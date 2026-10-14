package org.madrid.saii.tramitacion.jsf.utils;

import java.util.Iterator;

import javax.faces.FacesException;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.ExceptionQueuedEventContext;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomExceptionHandler extends ExceptionHandlerWrapper {
    private ExceptionHandler wrapped;

    public CustomExceptionHandler(ExceptionHandler wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return this.wrapped;
    }

    @Override
    public void handle() throws FacesException {
        Iterator iterator = this.getUnhandledExceptionQueuedEvents().iterator();

        while (iterator.hasNext()) {
            ExceptionQueuedEvent event = (ExceptionQueuedEvent) iterator.next();
            ExceptionQueuedEventContext context = (ExceptionQueuedEventContext) event.getSource();

            Throwable throwable = context.getException();

            FacesContext fc = FacesContext.getCurrentInstance();

            try {
                // Flash flash = fc.getExternalContext().getFlash();

                // Put the exception in the flash scope to be displayed in the
                // error
                // page if necessary ...
                // flash.put("errorDetails", throwable.getMessage());

                log.error("Mensaje de error: " + throwable.getMessage());

                log.error("Causa de error: " + throwable.getCause().getMessage());

                // NavigationHandler navigationHandler =
                // fc.getApplication().getNavigationHandler();

                // navigationHandler.handleNavigation(fc, null,
                // "error?faces-redirect=true");

                fc.renderResponse();
            } finally {
                iterator.remove();
            }
        }

        // Let the parent handle the rest
        this.getWrapped().handle();
    }
}
