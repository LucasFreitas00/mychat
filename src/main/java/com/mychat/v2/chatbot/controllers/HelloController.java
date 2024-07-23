package com.mychat.v2.chatbot.controllers;

import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;

public class HelloController implements ChatController {

    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {
        WebContext context = new WebContext(webExchange, webExchange.getLocale());
        /*writer.write("<html><body>");
        writer.write("<h1>Hello Worldddd!</h1>");
        writer.write("</body></html>");*/

        templateEngine.process("index", context, writer);
    }
}