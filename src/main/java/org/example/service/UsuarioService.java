package org.example.service;

import org.example.dto.UsuarioDTOInput;
import org.example.dto.UsuarioDTOOutput;
import org.example.model.Usuario;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class UsuarioService {
    private List<Usuario> listaUsuarios;

    private final ModelMapper modelMapper = new ModelMapper();

    public UsuarioService() {
        this.listaUsuarios = new ArrayList<Usuario>();
    }

    public Collection<UsuarioDTOOutput> listar() {
        return listaUsuarios.stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioDTOOutput.class))
                .collect(Collectors.toList());
    }

    public void setListaUsuarios(List<Usuario> listaUsuarios) {
        this.listaUsuarios = listaUsuarios;
    }

    public void inserir(UsuarioDTOInput usuarioDTOInput) {
        Usuario usuario = modelMapper.map(usuarioDTOInput, Usuario.class);
        listaUsuarios.add(usuario);
    }

    public UsuarioDTOOutput buscar(int id) {
        for (Usuario usuario : listaUsuarios) {
            if (usuario.getId() == id) {
                return modelMapper.map(usuario, UsuarioDTOOutput.class);
            }
        }
        return null;
    }

    public void alterar(UsuarioDTOInput usuarioDTOInput) {
        for (int i = 0; i < listaUsuarios.size(); i++) {
            Usuario usuario = listaUsuarios.get(i);
            if (usuario.getId() == usuarioDTOInput.getId()) {
                Usuario usuarioAtualizado = modelMapper.map(usuarioDTOInput, Usuario.class);
                listaUsuarios.set(i, usuarioAtualizado);
                break;
            }
        }
    }

    public void excluir(int id) {
        listaUsuarios.removeIf(usuario -> usuario.getId() == id);
    }
}
