package demo.etl.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Slf4j
@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             @NonNull HttpServletResponse response,
                             @NonNull Object handler) throws IOException {
        String httpMethod = request.getMethod();
        String path = request.getRequestURI().substring(request.getContextPath().length());
        String fullUrl = request.getRequestURL().toString();
        String clientIp = request.getRemoteAddr();

        // ignore swagger access
        if (path.contains("/error")
                || path.contains("/swagger")
                || path.contains("/api-docs")
                || path.startsWith("/favicon")) {
            return true;
        }

        // simple authentication to validate if the request contains the X-User-ID header
        String userID = request.getHeader("X-User-ID");
        if (userID == null || userID.isEmpty()) {
            log.info("User ID is missing in the {} request {} and IP {}", httpMethod, fullUrl, clientIp);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("{\"code\":" + HttpStatus.UNAUTHORIZED.value() + ",\"msg\": \"unauthorized access\"}");
            response.getWriter().flush();
            return false;
        }
        return true;
    }
}
