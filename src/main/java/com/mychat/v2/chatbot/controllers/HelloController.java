package com.mychat.v2.chatbot.controllers;

import com.mychat.v2.chatbot.domain.MeuChat;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.Writer;

public class HelloController implements ChatController {

    @Override
    public void process(IWebExchange webExchange, ITemplateEngine templateEngine, Writer writer) throws Exception {
        WebContext context = new WebContext(webExchange, webExchange.getLocale());

        // Pega a instância de MeuChat do contexto da aplicação
        MeuChat meuChat = (MeuChat) webExchange.getApplication().getAttributeValue("meuChat");

        // Pega o nome da classe do chat implementado
        String nomeChat = meuChat.getClass().getSimpleName();

        // Adiciona o nome do chat ao contexto do Thymeleaf
        context.setVariable("nomeChat", nomeChat);

        templateEngine.process("index", context, writer);
    }
}