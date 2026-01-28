package com.litethinking.enterprise.application.usecase;

import com.litethinking.enterprise.application.dto.request.CrearProductoRequest;
import com.litethinking.enterprise.application.dto.request.PrecioRequest;
import com.litethinking.enterprise.application.dto.response.PrecioResponse;
import com.litethinking.enterprise.application.dto.response.ProductoResponse;
import com.litethinking.enterprise.application.mapper.ProductoMapper;
import com.litethinking.enterprise.domain.exception.DuplicateResourceException;
import com.litethinking.enterprise.domain.exception.ResourceNotFoundException;
import com.litethinking.enterprise.domain.model.Producto;
import com.litethinking.enterprise.domain.model.valueobject.CodigoProducto;
import com.litethinking.enterprise.domain.model.valueobject.Money;
import com.litethinking.enterprise.domain.model.valueobject.Nit;
import com.litethinking.enterprise.domain.port.EmpresaRepositoryPort;
import com.litethinking.enterprise.domain.port.ProductoRepositoryPort;
import com.litethinking.enterprise.application.usecase.EmpresaUseCase.UserContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductoUseCase {

    private final ProductoRepositoryPort productoRepository;
    private final EmpresaRepositoryPort empresaRepository;
    private final ProductoMapper productoMapper;
    private final UserContextProvider userContextProvider;
    private final MonedaService monedaService;

    public ProductoUseCase(
            ProductoRepositoryPort productoRepository,
            EmpresaRepositoryPort empresaRepository,
            ProductoMapper productoMapper,
            UserContextProvider userContextProvider,
            MonedaService monedaService
    ) {
        this.productoRepository = productoRepository;
        this.empresaRepository = empresaRepository;
        this.productoMapper = productoMapper;
        this.userContextProvider = userContextProvider;
        this.monedaService = monedaService;
    }

    @Transactional
    public ProductoResponse crear(CrearProductoRequest request) {
        CodigoProducto codigo = CodigoProducto.of(request.codigo());
        Nit empresaNit = Nit.of(request.empresaNit());

        if (productoRepository.existePorCodigo(codigo)) {
            throw DuplicateResourceException.forResource("Producto", codigo.getValue());
        }

        if (!empresaRepository.existePorNit(empresaNit)) {
            throw ResourceNotFoundException.forResource("Empresa", empresaNit.getValue());
        }

        Long usuarioId = userContextProvider.getCurrentUserId();

        Producto producto = Producto.crear(
                codigo,
                request.nombre(),
                request.caracteristicas(),
                empresaNit,
                usuarioId
        );

        for (PrecioRequest precioReq : request.precios()) {
            String codigoMoneda = monedaService.obtenerCodigoMoneda(precioReq.monedaId());
            Money precio = Money.of(precioReq.precio(), codigoMoneda);
            producto.agregarPrecio(precioReq.monedaId(), precio);
        }

        if (request.categoriaIds() != null) {
            request.categoriaIds().forEach(producto::agregarCategoria);
        }

        Producto productoGuardado = productoRepository.guardar(producto);

        return buildProductoResponse(productoGuardado);
    }

    public ProductoResponse buscarPorCodigo(String codigoValue) {
        CodigoProducto codigo = CodigoProducto.of(codigoValue);

        Producto producto = productoRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Producto", codigo.getValue()));

        return buildProductoResponse(producto);
    }

    public List<ProductoResponse> buscarTodos() {
        List<Producto> productos = productoRepository.buscarActivos();
        return productos.stream()
                .map(this::buildProductoResponse)
                .toList();
    }

    public List<ProductoResponse> buscarPorEmpresa(String nitValue) {
        Nit nit = Nit.of(nitValue);
        List<Producto> productos = productoRepository.buscarPorEmpresa(nit);
        return productos.stream()
                .map(this::buildProductoResponse)
                .toList();
    }

    @Transactional
    public void eliminar(String codigoValue) {
        CodigoProducto codigo = CodigoProducto.of(codigoValue);

        Producto producto = productoRepository.buscarPorCodigo(codigo)
                .orElseThrow(() -> ResourceNotFoundException.forResource("Producto", codigo.getValue()));

        producto.desactivar();
        productoRepository.guardar(producto);
    }

    private ProductoResponse buildProductoResponse(Producto producto) {
        List<PrecioResponse> precios = new ArrayList<>();

        producto.getPrecios().forEach((monedaId, money) -> {
            String codigoMoneda = monedaService.obtenerCodigoMoneda(monedaId);
            precios.add(new PrecioResponse(monedaId, codigoMoneda, money.getAmount()));
        });

        return new ProductoResponse(
                producto.getCodigo().getValue(),
                producto.getNombre(),
                producto.getCaracteristicas(),
                producto.getEmpresaNit().getValue(),
                producto.isActivo(),
                precios,
                producto.getFechaCreacion()
        );
    }

    public interface MonedaService {
        String obtenerCodigoMoneda(Integer monedaId);
    }
}
