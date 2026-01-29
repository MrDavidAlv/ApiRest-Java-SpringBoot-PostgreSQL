package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.EmpresaFilters;
import com.litethinking.enterprise.application.dto.request.ActualizarEmpresaRequest;
import com.litethinking.enterprise.application.dto.request.CrearEmpresaRequest;
import com.litethinking.enterprise.application.dto.response.EmpresaResponse;
import com.litethinking.enterprise.application.mapper.EmpresaMapper;
import com.litethinking.enterprise.domain.exception.DuplicateResourceException;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Empresa;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.domain.port.EmpresaRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class EmpresaUseCase {

    private final EmpresaRepositoryPort empresaRepository;
    private final EmpresaMapper empresaMapper;
    private final UserContextProvider userContextProvider;

    public EmpresaUseCase(
            EmpresaRepositoryPort empresaRepository,
            EmpresaMapper empresaMapper,
            UserContextProvider userContextProvider
    ) {
        this.empresaRepository = empresaRepository;
        this.empresaMapper = empresaMapper;
        this.userContextProvider = userContextProvider;
    }

    @Transactional
    public EmpresaResponse crear(CrearEmpresaRequest request) {
        Nit nit = Nit.of(request.nit());

        if (empresaRepository.existePorNit(nit)) {
            throw DuplicateResourceException.forResource("Empresa", nit.getValue());
        }

        Long usuarioId = userContextProvider.getCurrentUserId();

        Empresa empresa = Empresa.crear(
                nit,
                request.nombre(),
                request.direccion(),
                request.telefono(),
                usuarioId
        );

        Empresa empresaGuardada = empresaRepository.guardar(empresa);

        return empresaMapper.toResponse(empresaGuardada);
    }

    public EmpresaResponse buscarPorNit(String nitValue) {
        Nit nit = Nit.of(nitValue);

        Empresa empresa = empresaRepository.buscarPorNit(nit)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Empresa", nit.getValue()));

        return empresaMapper.toResponse(empresa);
    }

    public List<EmpresaResponse> buscarTodas() {
        List<Empresa> empresas = empresaRepository.buscarActivas();
        return empresaMapper.toResponseList(empresas);
    }

    public List<EmpresaResponse> buscarConFiltros(EmpresaFilters filters) {
        List<Empresa> empresas = empresaRepository.buscar(filters);
        return empresaMapper.toResponseList(empresas);
    }

    @Transactional
    public EmpresaResponse actualizar(String nitValue, ActualizarEmpresaRequest request) {
        Nit nit = Nit.of(nitValue);

        Empresa empresa = empresaRepository.buscarPorNit(nit)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Empresa", nit.getValue()));

        Long usuarioId = userContextProvider.getCurrentUserId();

        empresa.actualizar(
                request.nombre(),
                request.direccion(),
                request.telefono(),
                usuarioId
        );

        Empresa empresaActualizada = empresaRepository.guardar(empresa);

        return empresaMapper.toResponse(empresaActualizada);
    }

    @Transactional
    public void eliminar(String nitValue) {
        Nit nit = Nit.of(nitValue);

        Empresa empresa = empresaRepository.buscarPorNit(nit)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Empresa", nit.getValue()));

        empresa.desactivar();
        empresaRepository.guardar(empresa);
    }

    public interface UserContextProvider {
        Long getCurrentUserId();
        String getCurrentUserRole();
    }
}
