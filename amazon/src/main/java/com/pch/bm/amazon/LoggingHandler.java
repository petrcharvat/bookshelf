package com.pch.bm.amazon;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.ByteArrayOutputStream;
import java.util.HashSet;
import java.util.Set;

public class LoggingHandler implements SOAPHandler<SOAPMessageContext> {

    private final Log log = LogFactory.getLog(getClass());

    private static final boolean FORMAT = true;

    @Override
    public Set<QName> getHeaders() {
        return new HashSet<QName>();
    }

    @Override
    public boolean handleMessage(final SOAPMessageContext context) {
        final Boolean outbound = (Boolean) context.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);

        final String message = getMessage(context.getMessage());
        if (BooleanUtils.isFalse(outbound)) {
            // incoming message

            if (log.isDebugEnabled()) {
                log.debug("In message = " + message);
            }
        } else {
            // outgoing message
            if (log.isDebugEnabled()) {
                log.debug("Out message = " + message);
            }
        }
        return true;
    }

    @Override
    public boolean handleFault(final SOAPMessageContext context) {
        final String message = getMessage(context.getMessage());

        if (log.isDebugEnabled()) {
            log.debug("Fail message = " + message);
        }
        return true;
    }

    @Override
    public void close(final MessageContext context) {
    }

    private String getMessage(final SOAPMessage message) {
        if (message != null && message.getSOAPPart() != null) {
            try {
                // TOTO na produkcnim WLS nefunguje
                //message.writeTo(outputStream);

                final TransformerFactory transformerFactory = TransformerFactory.newInstance();
                final Transformer transformer = transformerFactory.newTransformer();

                if (FORMAT) {
                    transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
                }

                final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                final Result output = new StreamResult(outputStream);

                transformer.transform(message.getSOAPPart().getContent(), output);
                return new String(outputStream.toByteArray(), "UTF-8");
            } catch (Exception ex) {
                log.error(ex, ex);
            }
        }
        return null;
    }




}