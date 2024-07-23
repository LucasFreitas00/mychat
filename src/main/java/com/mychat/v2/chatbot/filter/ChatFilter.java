package com.mychat.v2.chatbot.filter;

import com.mychat.v2.chatbot.controllers.ChatController;
import com.mychat.v2.chatbot.controllers.ChatbotController;
import com.mychat.v2.chatbot.controllers.HelloController;
import com.mychat.v2.chatbot.domain.MeuChat;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.ITemplateEngine;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.WebApplicationTemplateResolver;
import org.thymeleaf.web.IWebApplication;
import org.thymeleaf.web.IWebExchange;
import org.thymeleaf.web.IWebRequest;
import org.thymeleaf.web.servlet.JakartaServletWebApplication;

import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

public class ChatFilter implements Filter {

    private ITemplateEngine templateEngine;
    private JakartaServletWebApplication application;

    public ChatFilter() {
        super();
    }

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        this.application = JakartaServletWebApplication.buildApplication(filterConfig.getServletContext());
        this.templateEngine = buildTemplateEngine(this.application);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {
        if (!process((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse)) {
            chain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean process(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        try {
            MeuChat meuChat = (MeuChat) request.getServletContext().getAttribute("meuChat");

            final Map<String, ChatController> controllersByURL= new HashMap<>();
            controllersByURL.put("/", new HelloController());
            controllersByURL.put("/chatbot", new ChatbotController(meuChat));

            final IWebExchange webExchange = this.application.buildExchange(request, response);
            final IWebRequest webRequest = webExchange.getRequest();

            if (webRequest.getPathWithinApplication().startsWith("/css") ||
                    webRequest.getPathWithinApplication().startsWith("/images") ||
                    webRequest.getPathWithinApplication().startsWith("/favicon")) {
                return false;
            }

            final String path = webRequest.getPathWithinApplication();

            final ChatController controller = controllersByURL.get(path);
            if (controller == null) {
                return false;
            }

            response.setContentType("text/html;charset=UTF-8");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            final Writer writer = response.getWriter();

            controller.process(webExchange, this.templateEngine, writer);

            return true;

        } catch (Exception e) {
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (final IOException ignored) {
                // Just ignore this
            }
            throw new ServletException(e);
        }
    }

    private static ITemplateEngine buildTemplateEngine(final IWebApplication application) {
        final WebApplicationTemplateResolver templateResolver = new WebApplicationTemplateResolver(application);

        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setPrefix("/WEB-INF/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        templateResolver.setCacheable(true);

        final TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        return templateEngine;
    }
}
