package com.nutritionist.config;

import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

  @Autowired private JwtService jwtService;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String path = request.getRequestURI();
    // 公开接口放行
    boolean isPublic = 
        path.startsWith("/api/app/auth") || 
        path.startsWith("/api/admin/auth") ||
        path.startsWith("/api/uploads/") ||
        path.startsWith("/api/app/banners") ||
        path.startsWith("/api/app/courses") ||
        path.startsWith("/api/app/questions") ||
        path.startsWith("/api/app/forum") ||
        path.startsWith("/api/app/materials") ||
        path.startsWith("/api/app/trainings");
    
    if (isPublic) {
      filterChain.doFilter(request, response);
      return;
    }

    boolean needUser = path.startsWith("/api/app/");
    boolean needAdmin = path.startsWith("/api/admin/");
    if (!needUser && !needAdmin) {
      filterChain.doFilter(request, response);
      return;
    }

    String header = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (header == null || !header.startsWith("Bearer ")) {
      writeUnauthorized(response);
      return;
    }
    String token = header.substring(7);
    try {
      var claims = jwtService.parse(token);
      String subject = claims.getSubject();
      String role = claims.get("role", String.class);
      if (needAdmin && !"ADMIN".equals(role)) {
        writeUnauthorized(response);
        return;
      }
      if (needUser && !"USER".equals(role)) {
        writeUnauthorized(response);
        return;
      }
      var auth =
          new UsernamePasswordAuthenticationToken(
              subject,
              null,
              List.of(new SimpleGrantedAuthority("ROLE_" + role)));
      SecurityContextHolder.getContext().setAuthentication(auth);
      filterChain.doFilter(request, response);
    } catch (Exception e) {
      writeUnauthorized(response);
    } finally {
      SecurityContextHolder.clearContext();
    }
  }

  private void writeUnauthorized(HttpServletResponse response) throws IOException {
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("{\"code\":401,\"message\":\"未登录或令牌无效\",\"data\":null}");
  }
}
