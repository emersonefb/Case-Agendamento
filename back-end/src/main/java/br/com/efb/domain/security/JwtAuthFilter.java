package br.com.efb.domain.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private MyAutenticationUser myAutenticationUser;

    public JwtAuthFilter(JwtService jwtService, MyAutenticationUser myAutenticationUser) {
        this.jwtService = jwtService;
        this.myAutenticationUser = myAutenticationUser;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse,
            FilterChain filterChain) throws ServletException, IOException {

        httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, PATCH, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Content-Type", "application/json, charset=utf-8");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "authorization, content-type, X-Auth-Token,  xsrf-token");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "xsrf-token");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "Authorization");
        httpServletResponse.addHeader("Access-Control-Expose-Headers", "tokenJWT");
        httpServletResponse.addHeader("Access-Control-Expose-Headers"," X-Custom-header");
        httpServletResponse.addHeader("Access-Control-Allow-Headers", "Authorization, X-PINGOTHER, Origin, X-Requested-With, Content-Type, Accept, X-Custom-header");

        String authorization = httpServletRequest.getHeader("Authorization");
        String tokenJWT = httpServletRequest.getHeader("tokenJWT");

        // gerando token fixo para usuario.
        tokenJWT = jwtService.gerarToken("Emerson");

        String ipAddress = httpServletRequest.getHeader("x-forwarded-for");
        if (ipAddress == null) {
            ipAddress = httpServletRequest.getHeader("X_FORWARDED_FOR");
            if (ipAddress == null) {
                ipAddress = httpServletRequest.getRemoteAddr();
            }
        }

        String geraredToken = null;
        if ((authorization != null && authorization.startsWith("Bearer")) || tokenJWT != null && !tokenJWT.equalsIgnoreCase("null")) {
            String token = tokenJWT;
            if (tokenJWT == null) {
                token = authorization.replace("Bearer ", "");
            }

            boolean isValid = jwtService.tokenValido(token);


            if (isValid) {
                String loginUsuario = jwtService.obterUsuario(token);
                UserDetails userDetails = myAutenticationUser.loadUserByUsername(loginUsuario);
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                user.setDetails(new WebAuthenticationDetailsSource()
                        .buildDetails(httpServletRequest));

                SecurityContextHolder.getContext().setAuthentication(user);

                geraredToken = jwtService.gerarToken(user.getName());

            }
        }

        httpServletResponse.addHeader("tokenJWT", geraredToken);

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
