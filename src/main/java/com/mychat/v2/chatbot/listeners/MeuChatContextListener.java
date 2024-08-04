package com.mychat.v2.chatbot.listeners;

import com.mychat.v2.chatbot.annotations.Chatbot;
import com.mychat.v2.chatbot.domain.MeuChat;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@WebListener
public class MeuChatContextListener implements ServletContextListener {

    private static final Logger logger = LoggerFactory.getLogger(MeuChatContextListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            logger.info("Inicializando contexto");
            // Instancia a classe
            MeuChat meuChat = buscaClasseChatbot();

            // Verifica se a classe foi encontrada
            if (meuChat == null) {
                logger.error("Nenhuma classe extendendo de MeuChat e anotada com @ChatBot foi encontrada.");
                return;
            }

            // Verifica a instância
            logger.info("Instância de " + meuChat.getClass().getSimpleName() + " criada");

            // Opcional: adicionar a instância no contexto da aplicação para uso posterior
            sce.getServletContext().setAttribute("meuChat", meuChat);
            logger.info("Contexto inicializado com sucesso!");
        } catch (Exception e) {
            logger.error("Erro durante a inicialização do contexto!", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        logger.info("Contexto destruído.");
    }

    private MeuChat buscaClasseChatbot() throws Exception {
        try (ScanResult scanResult = new ClassGraph().enableAnnotationInfo().scan()) {
            for (Class<?> clazz : scanResult.getClassesWithAnnotation(Chatbot.class.getName()).loadClasses()) {
                if (MeuChat.class.isAssignableFrom(clazz)) {
                    return (MeuChat) clazz.getDeclaredConstructor().newInstance();
                }
            }
        }
        return null;
    }
}
