package com.antony.crud.Chamado;

import com.antony.crud.Cliente.Cliente;
import com.antony.crud.Cliente.ClienteService;
import com.antony.crud.Enums.Prioridade;
import com.antony.crud.Enums.Status;
import com.antony.crud.Tecnico.Tecnico;
import com.antony.crud.Tecnico.TecnicoService;
import jakarta.validation.Valid;
import org.hibernate.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ChamadoService {

    @Autowired
    private ChamadoRepository chamadoRepository;

    @Autowired
    private TecnicoService tecnicoService;

    @Autowired
    private ClienteService clienteService;

    //FUNÇÕES DE CONSULTA

    public Chamado findById(Integer id) {
        Optional<Chamado> obj = chamadoRepository.findById(id);

        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id, id));
    }

    public List<Chamado> findAll() {
        return chamadoRepository.findAll();
    }

    //FUNÇÕES DE CADASTRO

    public Chamado create(ChamadoDTO objDTO) {
        objDTO.setId(null);

        Chamado newObj = new Chamado(objDTO);

        return chamadoRepository.save(newObj);
    }

    public Chamado newChamado(ChamadoDTO objDTO) {
        Tecnico tecnico = tecnicoService.findById(objDTO.getTecnico());
        Cliente cliente = clienteService.findById(objDTO.getCliente());

        Chamado newObj = new Chamado(objDTO);

        if (objDTO.getId() != null) {
            newObj.setId(objDTO.getId());
        }

        if (objDTO.getStatus().equals(2)) {
            newObj.setDataFechamento(LocalDate.now());
        }

        newObj.setTecnico(tecnico);
        newObj.setCliente(cliente);
        newObj.setPrioridade(Prioridade.toEnum(objDTO.getPrioridade()));
        newObj.setStatus(Status.toEnum(objDTO.getStatus()));
        newObj.setTitulo(objDTO.getTitulo());
        newObj.setObservacoes(objDTO.getObservacoes());

        return chamadoRepository.save(newObj);
    }

    //FUNÇÕES DE ATUALIZAÇÃO

    public Chamado update(Integer id, @Valid ChamadoDTO objDTO) {
        objDTO.setId(id);

        Chamado oldObj = findById(id);

        oldObj = new Chamado(objDTO);

        return chamadoRepository.save(oldObj);
    }

}
