package com.antony.crud.Tecnico;

import com.antony.crud.Chamado.Chamado;
import com.antony.crud.Enums.Perfil;
import com.antony.crud.Pessoa.Pessoa;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Tecnico extends Pessoa {
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @OneToMany(mappedBy = "tecnico")
    List<Chamado> chamados = new ArrayList<>();

    public Tecnico() {
        super();
        addPerfil(Perfil.TECNICO);
    }

    public Tecnico(TecnicoDTO tecnico) {
        super(tecnico.getId(), tecnico.getNome(), tecnico.getCpf(), tecnico.getEmail(), tecnico.getSenha());
        addPerfil(Perfil.TECNICO);
    }
}
