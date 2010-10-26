package com.pch.bm.amazon;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.BooleanUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.namespace.QName;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class AmazonSecurityHandler implements SOAPHandler<SOAPMessageContext> {

    private final byte[] secretBytes;

    public AmazonSecurityHandler(final String awsSecretKey) {
        secretBytes = stringToUtf8(awsSecretKey);
    }

    public boolean handleMessage(final SOAPMessageContext messagecontext) {
        final Boolean outbound = (Boolean) messagecontext.get(MessageContext.MESSAGE_OUTBOUND_PROPERTY);
        if (BooleanUtils.isTrue(outbound)) {
            try {
                final SOAPMessage soapMessage = messagecontext.getMessage();
                final SOAPBody soapBody = soapMessage.getSOAPBody();
                final Node firstChild = soapBody.getFirstChild();

                final String timeStamp = getTimestamp();
                final String signature = getSignature(firstChild.getLocalName(), timeStamp, secretBytes);

                appendTextElement(firstChild, "Signature", signature);
                appendTextElement(firstChild, "Timestamp", timeStamp);
            } catch (SOAPException se) {
                throw new RuntimeException("SOAPException was thrown.", se);
            }
        }
        return true;
    }

    private String getSignature(final String operation, final String timeStamp, final byte[] secretBytes) {
        try {
            final String toSign = operation + timeStamp;
            final byte[] toSignBytes = stringToUtf8(toSign);

            final Mac signer = Mac.getInstance("HmacSHA256");
            final SecretKeySpec keySpec = new SecretKeySpec(secretBytes, "HmacSHA256");

            signer.init(keySpec);
            signer.update(toSignBytes);
            final byte[] signBytes = signer.doFinal();

            return new String(Base64.encodeBase64(signBytes));

        } catch (NoSuchAlgorithmException nsae) {
            throw new RuntimeException("NoSuchAlgorithmException was thrown.", nsae);
        } catch (InvalidKeyException ike) {
            throw new RuntimeException("InvalidKeyException was thrown.", ike);
        }
    }

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    private static String getTimestamp() {
        return DATE_TIME_FORMATTER.print(new DateTime().withZone(DateTimeZone.UTC));
    }

    protected static byte[] stringToUtf8(final String source) {
        try {
            return source.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            // This shall will never happen. UTF-8 shall be always available.
            throw new RuntimeException("getBytes threw an UnsupportedEncodingException", e);
        }
    }

    protected void appendTextElement(final Node node, final String elementName, final String elementText) {
        final Element element = node.getOwnerDocument().createElement(elementName);
        element.setTextContent(elementText);
        node.appendChild(element);
    }

    public void close(final MessageContext messagecontext) {
    }

    public Set<QName> getHeaders() {
        return new HashSet<QName>();
    }

    public boolean handleFault(final SOAPMessageContext messagecontext) {
        return true;
    }

}
