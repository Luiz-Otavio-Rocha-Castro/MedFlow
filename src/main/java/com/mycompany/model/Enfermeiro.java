/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.model;

public class Enfermeiro extends ProfissionalSaude {

    public Enfermeiro(Integer id, String nome, String registroProfissional) {
        super(id, nome, registroProfissional);
    }

    @Override
    public void ExibirDados(){
        System.out.println("=== Enfermeiro ===");
        System.out.println("ID: " + getId());
        System.out.println("Nome: " + getNome());
        System.out.println("CRM: " + getRegistroProfissional());
    }
    
}
