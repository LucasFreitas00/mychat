package com.mychat.v2.chatbot;

import com.mychat.v2.chatbot.domain.MeuChat;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebListener;
import lucas.tarefas.TarefasChat;

import java.io.InputStream;
import java.util.Properties;

@WebListener
public class MeuChatContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            // Carrega as propriedades do arquivo application.properties
            Properties properties = new Properties();
            try (InputStream input = getClass().getClassLoader().getResourceAsStream("application.properties")) {
                if (input == null) {
                    System.out.println("Desculpe, não foi possível encontrar o arquivo application.properties");
                    return;
                }
                properties.load(input);
            }

            // Obt´m o nome da classe a partir das propriedades
            String nomeChat = properties.getProperty("meuchat");

            // Verifica se a propriedade foi encontrada
            if (nomeChat == null || nomeChat.isEmpty()) {
                System.out.println("A propriedade 'meuchat' não foi encontrada ou está vazia");
                return;
            }

            // Obtém a classe utilizando o nome
            Class<?> clazz = Class.forName(nomeChat);

            // Instancia a classe
            MeuChat meuChat = (MeuChat) clazz.getDeclaredConstructor().newInstance();

            // Verifica a instância
            System.out.println("Instância criada: " + meuChat.getClass().getName());

            // Opcional: adicionar a instância no contexto da aplicação para uso posterior
            sce.getServletContext().setAttribute("meuChat", meuChat);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Código para limpar recursos se necessário
    }

}
