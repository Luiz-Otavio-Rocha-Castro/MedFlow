package com.mycompany.repository;

import com.mycompany.model.Paciente;

import java.util.ArrayList;
import java.util.List;

public class PacienteRepository {

    List<Paciente> pacientes = new ArrayList<>();
    static Integer codigo = 1;

    public List<Paciente> buscartodos(){
        return pacientes;
    }

    public Paciente buscarPorId(Integer id){
        return pacientes.stream()
                .filter(paciente -> paciente.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Paciente adicionar(Paciente paciente) {
        paciente.setId(codigo);
        codigo += 1;
        pacientes.add(paciente);
        return paciente;
    }

    public void deletar(Integer id){
        pacientes.removeIf(paciente -> paciente.getId().equals(id));
    }

    public Paciente atualizar(Integer id, Paciente paciente){
        paciente.setId(id);
        Paciente dadosVelhos = buscarPorId(id);
        pacientes.set(pacientes.indexOf(dadosVelhos), paciente);
        return paciente;
    }
}
