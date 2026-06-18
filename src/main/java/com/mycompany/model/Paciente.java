/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

public class Paciente {
    private Integer id;
    private String nome;
    private int idade;
    private String sintoma;
    private StatusPaciente status;
    private CorPrioridade prioridade;


    public Paciente(Integer id, String nome, int idade, String sintoma, StatusPaciente status, CorPrioridade prioridade) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.sintoma = sintoma;
        this.status = status;
        this.prioridade = prioridade;
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

    public int getIdade() {
        return idade;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    public String getSintoma() {
        return sintoma;
    }

    public void setSintoma(String sintoma) {
        this.sintoma = sintoma;
    }

    public StatusPaciente getStatus() {
        return status;
    }

    public void setStatus(StatusPaciente status) {
        this.status = status;
    }

    public CorPrioridade getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(CorPrioridade prioridade) {
        this.prioridade = prioridade;
    }
}
