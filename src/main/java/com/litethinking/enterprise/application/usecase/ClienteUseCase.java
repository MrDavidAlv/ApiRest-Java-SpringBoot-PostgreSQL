package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.CrearClienteRequest;
import com.litethinking.enterprise.application.dto.response.ClienteResponse;
import com.litethinking.enterprise.domain.exception.BusinessRuleViolationException;
import com.litethinking.enterprise.domain.model.Cliente;
import com.litethinking.enterprise.domain.model.valueobject.Email;
import com.litethinking.enterprise.domain.port.ClienteRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ClienteUseCase {

    private final ClienteRepositoryPort clienteRepository;

    public ClienteUseCase(ClienteRepositoryPort clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Transactional
    public ClienteResponse crear(CrearClienteRequest request) {
        if (clienteRepository.existePorDocumento(request.documento())) {
            throw new BusinessRuleViolationException("Ya existe un cliente con el documento: " + request.documento());
        }

        Email correo = request.correo() != null && !request.correo().isBlank() 
            ? Email.of(request.correo()) 
            : null;

        Cliente cliente = Cliente.crear(
            request.documento(),
            request.nombre(),
            correo
        );

        Cliente clienteGuardado = clienteRepository.guardar(cliente);
        return mapToResponse(clienteGuardado);
    }

    public Optional<ClienteResponse> buscarPorId(Long id) {
        return clienteRepository.buscarPorId(id)
            .map(this::mapToResponse);
    }

    public Optional<ClienteResponse> buscarPorDocumento(String documento) {
        return clienteRepository.buscarPorDocumento(documento)
            .map(this::mapToResponse);
    }

    public List<ClienteResponse> buscarTodos() {
        return clienteRepository.buscarTodos().stream()
            .map(this::mapToResponse)
            .toList();
    }

    @Transactional
    public void eliminar(Long id) {
        clienteRepository.eliminar(id);
    }

    private ClienteResponse mapToResponse(Cliente cliente) {
        return new ClienteResponse(
            cliente.getId(),
            cliente.getDocumento(),
            cliente.getNombre(),
            cliente.getCorreo() != null ? cliente.getCorreo().getValue() : null
        );
    }
}
