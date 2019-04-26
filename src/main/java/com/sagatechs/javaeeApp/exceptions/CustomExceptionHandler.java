package com.sagatechs.javaeeApp.exceptions;


import static com.sagatechs.adminfaces.starter.util.Assert.has;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION;
import static javax.servlet.RequestDispatcher.ERROR_EXCEPTION_TYPE;
import static javax.servlet.RequestDispatcher.ERROR_MESSAGE;
import static javax.servlet.RequestDispatcher.ERROR_REQUEST_URI;
import static javax.servlet.RequestDispatcher.ERROR_STATUS_CODE;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExceptionHandler;
import javax.faces.context.ExceptionHandlerWrapper;
import javax.faces.context.FacesContext;
import javax.faces.event.ExceptionQueuedEvent;
import javax.faces.event.PhaseId;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omnifaces.config.WebXml;
import org.omnifaces.util.Exceptions;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import com.sagatechs.adminfaces.starter.util.Constants;


/**
 * Based on: https://github.com/conventions/core/blob/master/src/main/java/org/conventionsframework/exception/ConventionsExceptionHandler.java
 * This handler adds FacesMessages when BusinessExceptions are thrown
 * OR sends user to error page when unexpected exception are raised.
 *
 * @author rafael-pestano
 */
public class CustomExceptionHandler extends ExceptionHandlerWrapper {

    private static final Logger LOGGER = Logger.getLogger(CustomExceptionHandler.class.getName());
    private ExceptionHandler wrapped;

    @SuppressWarnings("deprecation")
	public CustomExceptionHandler(ExceptionHandler exceptionHandler) {
        this.wrapped = exceptionHandler;
    }

    @Override
    public ExceptionHandler getWrapped() {
        return wrapped;
    }

    @Override
    public void handle() throws FacesException {
        FacesContext context = FacesContext.getCurrentInstance();
        findErrorMessages(context);
        handleException(context);
        wrapped.handle();
    }

    /**
     * @param context
     * @throws Throwable
     */
    private void handleException(FacesContext context) {
        Iterator<ExceptionQueuedEvent> unhandledExceptionQueuedEvents = getUnhandledExceptionQueuedEvents().iterator();

        if (unhandledExceptionQueuedEvents.hasNext()) {
            Throwable exception = unhandledExceptionQueuedEvents.next().getContext().getException();
            unhandledExceptionQueuedEvents.remove();

            Throwable rootCause = Exceptions.unwrap(exception);

            if (rootCause instanceof BusinessException) {
                handleBusinessException(context, (BusinessException) rootCause);
                return;
            }

            //send user to error page when unexpected exceptions are raised
            goToErrorPage(context, rootCause);
        }

    }


    /**
     * @param context
     * @param e
     * @throws Throwable
     */
    private void goToErrorPage(FacesContext context, Throwable e) {
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        request.setAttribute(ERROR_EXCEPTION + "_stacktrace", e);

        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(e);
        }

        if (e instanceof FileNotFoundException) {
            LOGGER.log(Level.WARNING,"File not found", e);
            throw new FacesException(e);
        }


        request.setAttribute(ERROR_EXCEPTION_TYPE, e.getClass().getName());
        request.setAttribute(ERROR_MESSAGE, e.getMessage());
        request.setAttribute(ERROR_REQUEST_URI, request.getHeader("Referer"));
        request.setAttribute(ERROR_STATUS_CODE, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

        String errorPage = findErrorPage(e);
        if (!has(errorPage)) {
            String errorPageParam = context.getExternalContext().getInitParameter(Constants.InitialParams.ERROR_PAGE);
            if (!has(errorPageParam)) {
                errorPage = Constants.DEFAULT_ERROR_PAGE;
            }
        }
        context.getApplication().getNavigationHandler().handleNavigation(context, null, errorPage);
        context.renderResponse();
    }

    /**
     * Find error page in web.xml
     *
     * @param exception
     * @return
     */
    private String findErrorPage(Throwable exception) {
        if (exception instanceof EJBException && exception.getCause() != null) {
            exception = exception.getCause();
        }
        @SuppressWarnings("deprecation")
		String errorPage = WebXml.INSTANCE.findErrorPageLocation(exception);

        return errorPage;
    }

    /**
     * @param context
     * @param e       application business exception
     */
    private void handleBusinessException(FacesContext context, BusinessException e) {
        if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
            throw new FacesException(e);
        }
        if (has(e.getExceptionList())) {
            for (BusinessException be : e.getExceptionList()) {
                addFacesMessage(be);
            }
        } else { //Single exception
            addFacesMessage(e);
        }
        validationFailed();
        context.renderResponse();
    }

    private void addFacesMessage(BusinessException be) {
        FacesMessage facesMessage = new FacesMessage();
        if (has(be.getSummary())) {
            facesMessage.setSummary(be.getSummary());
        }
        if (has(be.getDetail())) {
            facesMessage.setDetail(be.getDetail());
        }
        if (has(be.getSeverity())) {
            facesMessage.setSeverity(be.getSeverity());
        } else {
            facesMessage.setSeverity(FacesMessage.SEVERITY_ERROR);
        }
        Messages.add(be.getFieldId(), facesMessage);
    }

    /**
     * Set primefaces validationFailled callback param
     */
    private void validationFailed() {
        PrimeFaces context = PrimeFaces.current();
        if (context != null) {
            context.ajax().addCallbackParam("validationFailed", true);
        }
    }


    /**
     * If there is any faces message queued add PrimeFaces validation failed
     *
     * @param context
     */
    private void findErrorMessages(FacesContext context) {
        if (context.getMessageList().isEmpty() || context.isValidationFailed()) {
            return;
        }
        for (FacesMessage msg : context.getMessageList()) {
            if (msg.getSeverity().equals(FacesMessage.SEVERITY_ERROR) || msg.getSeverity().equals(FacesMessage.SEVERITY_FATAL)) {
                validationFailed();
                break;
            }
        }
    }

}
