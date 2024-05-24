package demo.etl.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

/**
 * Security filter
 *
 * check all requests to ensure they contain a valid header
 */
@Slf4j
//@Component
public class UserFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // check for the presence of X-User=ID header
        String userId = request.getHeader("X-User-ID");
        if (userId == null || userId.isEmpty()) {
            log.info("User ID is missing in the request {} and IP {}", request.getRequestURI(), request.getRemoteAddr());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "User ID is missing")));
            return;
        }
        // proceed the next filter
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
