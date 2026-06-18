package com.mycompany.model;

public abstract class ProfissionalSaude {
    private Integer id;
    private String nome;
    private String registroProfissional;

    public ProfissionalSaude(Integer id, String nome, String registroProfissional) {
        this.id = id;
        this.nome = nome;
        this.registroProfissional = registroProfissional;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegistroProfissional() {
        return registroProfissional;
    }

    public void setRegistroProfissional(String registroProfissional) {
        this.registroProfissional = registroProfissional;
    }
    
    public abstract void ExibirDados();
    
}
