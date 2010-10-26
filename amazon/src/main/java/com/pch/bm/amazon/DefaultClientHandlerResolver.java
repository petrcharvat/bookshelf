package com.pch.bm.amazon;

import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.HandlerResolver;
import javax.xml.ws.handler.PortInfo;
import java.util.List;

public class DefaultClientHandlerResolver implements HandlerResolver {

    private List<Handler> handlers;

    public void setHandlers(final List<Handler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public List<Handler> getHandlerChain(final PortInfo portInfo) {
        return handlers;
    }

}
