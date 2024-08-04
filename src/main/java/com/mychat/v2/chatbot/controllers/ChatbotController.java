package com.mychat.v2.chatbot.controllers;

import com.mychat.v2.chatbot.domain.MeuChat;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.context.WebContext;
import org.thymeleaf.web.IWebExchange;

import java.io.IOException;
import java.io.Writer;

public class ChatbotController implements ChatController {

    private final MeuChat meuChat;

    public ChatbotController(MeuChat meuChat) {
        this.meuChat = meuChat;
    }

    @Override
    public void process(final IWebExchange webExchange, final ITemplateEngine templateEngine, final Writer writer) throws IOException {
        String entrada = webExchange.getRequest().getParameterValue("userInput");

        meuChat.receberMensagem(entrada);

        WebContext context = new WebContext(webExchange, webExchange.getLocale());
        context.setVariable("mensagens", meuChat.getMensagens());

        templateEngine.process("chatbot", context, writer);
    }
}
