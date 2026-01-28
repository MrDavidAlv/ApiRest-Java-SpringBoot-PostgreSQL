package com.litethinking.enterprise.infrastructure.service;

import com.litethinking.enterprise.application.usecase.ProductoUseCase;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.MonedaJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class MonedaServiceImpl implements ProductoUseCase.MonedaService {

    private final MonedaJpaRepository monedaRepository;

    public MonedaServiceImpl(MonedaJpaRepository monedaRepository) {
        this.monedaRepository = monedaRepository;
    }

    @Override
    public String obtenerCodigoMoneda(Integer monedaId) {
        return monedaRepository.findById(monedaId)
                .map(moneda -> moneda.getCodigoIso())
                .orElseThrow(() -> ResourceNotFoundException.forResource("Moneda", monedaId));
    }
}
