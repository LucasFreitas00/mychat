package com.mychat.v2.chatbot.domain;

public abstract class MeuChat {

    private String resposta;

    public MeuChat() {
        this.resposta = "";
    }

    public String getResposta() {
        return resposta;
    }

    protected void setResposta(String resposta) {
        this.resposta = resposta;
    }

    public abstract void receberMensagem(String msg);

    protected abstract void processarMensagem(String msg);
}
