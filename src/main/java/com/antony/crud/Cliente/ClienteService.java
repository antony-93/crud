package com.antony.crud.Cliente;

import com.antony.crud.Pessoa.Pessoa;
import com.antony.crud.Pessoa.PessoaRepository;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private PessoaRepository pessoaRepository;

    //FUNÇÕES DE CONSULTA

    public Cliente findById(Integer id) {
        Optional<Cliente> obj = clienteRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id, id));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    //FUNÇÕES DE CADASTRO

    public Cliente create(ClienteDTO objDTO) {
        objDTO.setId(null);

        validaPorCpfEEmail(objDTO);

        Cliente newObj = new Cliente(objDTO);

        return clienteRepository.save(newObj);
    }

    //FUNÇÕES DE ATUALIZAÇÃO

    public Cliente update(Integer id, @Valid ClienteDTO objDTO) {
        objDTO.setId(id);

        Cliente oldObj = findById(id);

        if (!objDTO.getSenha().equals(oldObj.getSenha())) {
            objDTO.setSenha(objDTO.getSenha());
        }

        validaPorCpfEEmail(objDTO);

        oldObj = new Cliente(objDTO);
        return clienteRepository.save(oldObj);
    }

    //FUNÇÕES DE EXCLUSÃO

    public void delete(Integer id) {
        Cliente obj = findById(id);

        if (obj.getChamados().size() > 0) {
            throw new DataIntegrityViolationException("Cliente possui ordens de serviço e não pode ser deletado!");
        }

        clienteRepository.deleteById(id);
    }

    //FUNÇÕES DE VALIDAÇÃO

    private void validaPorCpfEEmail(ClienteDTO objDTO) {
        Optional<Pessoa> objCpf = pessoaRepository.findByCpf(objDTO.getCpf());

        if (objCpf.isPresent() && objCpf.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("CPF ja existe");
        }

        Optional<Pessoa> objEmail = pessoaRepository.findByEmail(objDTO.getEmail());

        if (objEmail.isPresent() && objEmail.get().getId() != objDTO.getId()) {
            throw new DataIntegrityViolationException("Email ja existe");
        }
    }
}
