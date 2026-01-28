package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.CrearOrdenRequest;
import com.litethinking.enterprise.application.dto.request.OrdenDetalleRequest;
import com.litethinking.enterprise.application.dto.response.OrdenDetalleResponse;
import com.litethinking.enterprise.application.dto.response.OrdenResponse;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.*;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;
import com.litethinking.enterprise.domain.port.ClienteRepositoryPort;
import com.litethinking.enterprise.domain.port.OrdenRepositoryPort;
import com.litethinking.enterprise.domain.port.ProductoRepositoryPort;
import com.litethinking.enterprise.application.usecase.EmpresaUseCase.UserContextProvider;
import com.litethinking.enterprise.application.usecase.ProductoUseCase.MonedaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrdenUseCase {

    private final OrdenRepositoryPort ordenRepository;
    private final ClienteRepositoryPort clienteRepository;
    private final ProductoRepositoryPort productoRepository;
    private final UserContextProvider userContextProvider;
    private final MonedaService monedaService;

    public OrdenUseCase(
            OrdenRepositoryPort ordenRepository,
            ClienteRepositoryPort clienteRepository,
            ProductoRepositoryPort productoRepository,
            UserContextProvider userContextProvider,
            MonedaService monedaService
    ) {
        this.ordenRepository = ordenRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.userContextProvider = userContextProvider;
        this.monedaService = monedaService;
    }

    @Transactional
    public OrdenResponse crear(CrearOrdenRequest request) {
        Cliente cliente = clienteRepository.buscarPorId(request.clienteId())
                .orElseThrow(() -> ResourceNotFoundException.forResource("Cliente", request.clienteId()));

        Long usuarioId = userContextProvider.getCurrentUserId();

        Orden orden = Orden.crear(
                cliente.getId(),
                EstadoOrden.pendiente(),
                usuarioId
        );

        for (OrdenDetalleRequest detalleReq : request.detalles()) {
            CodigoProducto codigoProducto = CodigoProducto.of(detalleReq.productoCodigo());

            Producto producto = productoRepository.buscarPorCodigo(codigoProducto)
                    .orElseThrow(() -> ResourceNotFoundException.forResource("Producto", codigoProducto.getValue()));

            Money precioUnitario = producto.obtenerPrecio(detalleReq.monedaId());

            orden.agregarDetalle(codigoProducto, detalleReq.cantidad(), precioUnitario);
        }

        Orden ordenGuardada = ordenRepository.guardar(orden);

        return buildOrdenResponse(ordenGuardada, cliente);
    }

    public OrdenResponse buscarPorId(Long id) {
        Orden orden = ordenRepository.buscarPorId(id)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Orden", id));

        Cliente cliente = clienteRepository.buscarPorId(orden.getClienteId())
                .orElseThrow(() -> ResourceNotFoundException.forResource("Cliente", orden.getClienteId()));

        return buildOrdenResponse(orden, cliente);
    }

    public List<OrdenResponse> buscarTodas() {
        List<Orden> ordenes = ordenRepository.buscarTodas();
        return ordenes.stream()
                .map(orden -> {
                    Cliente cliente = clienteRepository.buscarPorId(orden.getClienteId()).orElse(null);
                    return buildOrdenResponse(orden, cliente);
                })
                .toList();
    }

    @Transactional
    public OrdenResponse cambiarEstado(Long ordenId, Integer nuevoEstadoId) {
        Orden orden = ordenRepository.buscarPorId(ordenId)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Orden", ordenId));

        EstadoOrden nuevoEstado = mapearEstadoOrden(nuevoEstadoId);

        orden.cambiarEstado(nuevoEstado);

        Orden ordenActualizada = ordenRepository.guardar(orden);

        Cliente cliente = clienteRepository.buscarPorId(orden.getClienteId()).orElse(null);

        return buildOrdenResponse(ordenActualizada, cliente);
    }

    private OrdenResponse buildOrdenResponse(Orden orden, Cliente cliente) {
        List<OrdenDetalleResponse> detallesResponse = orden.getDetalles().stream()
                .map(detalle -> {
                    Producto producto = productoRepository.buscarPorCodigo(detalle.getProductoCodigo()).orElse(null);
                    String nombreProducto = producto != null ? producto.getNombre() : "N/A";

                    return new OrdenDetalleResponse(
                            detalle.getProductoCodigo().getValue(),
                            nombreProducto,
                            detalle.getCantidad(),
                            detalle.getPrecioUnitarioHistorico().getAmount(),
                            detalle.calcularSubtotal().getAmount()
                    );
                })
                .toList();

        Money total = orden.calcularTotal();

        return new OrdenResponse(
                orden.getId(),
                orden.getClienteId(),
                cliente != null ? cliente.getNombre() : "N/A",
                orden.getEstado().getNombre(),
                orden.getFechaOrden(),
                detallesResponse,
                total.getAmount(),
                total.getCurrencyCode()
        );
    }

    private EstadoOrden mapearEstadoOrden(Integer estadoId) {
        return switch (estadoId) {
            case 1 -> EstadoOrden.pendiente();
            case 2 -> EstadoOrden.pagada();
            case 3 -> EstadoOrden.anulada();
            default -> throw new ResourceNotFoundException("Invalid order state ID: " + estadoId);
        };
    }
}
