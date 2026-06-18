package com.mycompany.repository;

import com.mycompany.model.Paciente;
import com.mycompany.model.ProfissionalSaude;

import java.util.ArrayList;
import java.util.List;

public class ProfissionalSaudeRepository {
    List<ProfissionalSaude> profissionais = new ArrayList<>();
    static Integer codigo = 1;

    public List<ProfissionalSaude> buscartodos(){
        return profissionais;
    }

    public ProfissionalSaude buscarPorId(Integer id){
        return profissionais.stream()
                .filter(profissional -> profissional.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public ProfissionalSaude adicionar(ProfissionalSaude profissionalSaude) {
        profissionalSaude.setId(codigo);
        codigo += 1;
        profissionais.add(profissionalSaude);
        return profissionalSaude;
    }

    public void deletar(Integer id){
        profissionais.removeIf(profissionalSaude -> profissionalSaude.getId().equals(id));
    }

    public ProfissionalSaude atualizar(Integer id, ProfissionalSaude profissionalSaude){
        profissionalSaude.setId(id);
        ProfissionalSaude dadosVelhos = buscarPorId(id);
        profissionais.set(profissionais.indexOf(dadosVelhos), profissionalSaude);
        return profissionalSaude;
    }
}
