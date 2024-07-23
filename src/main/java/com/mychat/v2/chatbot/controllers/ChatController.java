package com.mychat.v2.chatbot.controllers;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;

public interface ChatController {

    void process(final IWebExchange webExchange, final ITemplateEngine templateEngine, final Writer writer) throws Exception;
}
