package com.example.jwt.demo_implement_jwt.configs;

import com.example.jwt.demo_implement_jwt.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;


/*
* Intercepta todas las solicitudes HTTP y comprueba si tienen un encabezado Authorization válido con un token JWT.
* Si el encabezado es válido, extrae y valida el token JWT
* */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    // Se utiliza para manejar excepciones dentro del filtro y redirigirlas
    // al controlador global de excepciones.
    private final HandlerExceptionResolver handlerExceptionResolver;

    private final JwtService jwtService;

    // Se usa para cargar los detalles del usuario desde la base de datos utilizando
    // su correo electrónico
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(
            JwtService jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);//se obtiene el token puro , se elimina el prefijo bearer
            final String userEmail = jwtService.extractUsername(jwt);

            //Comprueba si ya hay una autenticación establecida para la solicitud actual.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValid(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            // Envia el error al controlador de erroes globales
            //Esto permite manejar errores de autenticación de manera centralizada en un controlador global de excepciones.
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
