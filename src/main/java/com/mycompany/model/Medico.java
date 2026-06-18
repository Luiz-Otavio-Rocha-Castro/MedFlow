
package com.mycompany.model;

public class Medico extends ProfissionalSaude{
    
    private String especialidade;
    
    public Medico(Integer id, String nome, String registroProfissional, String especialidade) {
        super(id, nome, registroProfissional);
        this.especialidade = especialidade;
    }
    @Override
    public void ExibirDados(){
        System.out.println("=== MÉDICO ===");
        System.out.println("ID: " + getId());
        System.out.println("Nome: " + getNome());
        System.out.println("CRM: " + getRegistroProfissional());
        System.out.println("Especialidade: " + especialidade);
    }
    
}
