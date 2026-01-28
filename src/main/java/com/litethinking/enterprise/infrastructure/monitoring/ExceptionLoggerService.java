package com.litethinking.enterprise.infrastructure.monitoring;

import com.litethinking.enterprise.infrastructure.persistence.entity.ExcepcionSistemaEntity;
import com.litethinking.enterprise.infrastructure.persistence.repository.jpa.ExcepcionSistemaJpaRepository;
import com.litethinking.enterprise.infrastructure.security.UserContextService;
import com.litethinking.enterprise.interfaces.exception.GlobalExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class ExceptionLoggerService implements GlobalExceptionHandler.ExceptionLogger {

    private static final Logger logger = LoggerFactory.getLogger(ExceptionLoggerService.class);

    private final ExcepcionSistemaJpaRepository excepcionRepository;
    private final UserContextService userContextService;

    public ExceptionLoggerService(
            ExcepcionSistemaJpaRepository excepcionRepository,
            UserContextService userContextService
    ) {
        this.excepcionRepository = excepcionRepository;
        this.userContextService = userContextService;
    }

    @Override
    @Transactional
    public void logException(Exception ex, HttpServletRequest request) {
        try {
            ExcepcionSistemaEntity entity = new ExcepcionSistemaEntity();

            try {
                entity.setUsuarioId(userContextService.getCurrentUserId());
            } catch (Exception e) {
                entity.setUsuarioId(null);
            }

            entity.setClaseError(ex.getClass().getName());
            entity.setMensajeError(ex.getMessage());
            entity.setStackTrace(getStackTrace(ex));
            entity.setEndpointUrl(request.getRequestURI());
            entity.setMetodoHttp(request.getMethod());
            entity.setParametrosEntrada(extractRequestParameters(request));
            entity.setIpOrigen(extractClientIp(request));
            entity.setEstadoResolucion("PENDIENTE");

            excepcionRepository.save(entity);

        } catch (Exception e) {
            logger.error("Failed to log exception to database", e);
        }
    }

    private String getStackTrace(Exception ex) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        return sw.toString();
    }

    private Map<String, Object> extractRequestParameters(HttpServletRequest request) {
        Map<String, Object> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if (values.length == 1) {
                params.put(key, values[0]);
            } else {
                params.put(key, values);
            }
        });
        return params;
    }

    private String extractClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty()) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
