package com.mtgleague.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mtgleague.model.Player;
import com.mtgleague.service.PlayersService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final PlayersService playersService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");
        final String jwtToken;
        final String userEmail;
        Player player = null;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        jwtToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(jwtToken);

        // Wrap the request to buffer the input stream
        HttpServletRequestWrapper requestWrapper = new BufferedRequestWrapper(request);

        // Parse the JSON request body
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(requestWrapper.getInputStream());

        // Check if the JSON object contains the "playerId" property
        if (jsonNode.has("playerId")) {
            String playerId = jsonNode.get("playerId").asText();
            player = this.playersService.findByIdString(playerId).orElse(null);
        }

        if ((player != null && player.getEmail().equals(userEmail)) || player == null) {
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (jwtService.isTokenValid(jwtToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }
        filterChain.doFilter(requestWrapper, response);
    }

    private static class BufferedRequestWrapper extends HttpServletRequestWrapper {
        private byte[] requestBody;

        public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
            super(request);
            requestBody = request.getInputStream().readAllBytes();
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return new BufferedReader(new InputStreamReader(getInputStream()));
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return new BufferedServletInputStream(requestBody);
        }
    }

    private static class BufferedServletInputStream extends ServletInputStream {
        private final byte[] buffer;
        private int index = 0;

        public BufferedServletInputStream(byte[] buffer) {
            this.buffer = buffer;
        }
        @Override
        public int read() throws IOException {
            if (index >= buffer.length) {
                return -1;
            }
            return buffer[index++];
        }
        @Override
        public boolean isFinished() {
            return index >= buffer.length;
        }
        @Override
        public boolean isReady() {
            return true;
        }
        @Override
        public void setReadListener(ReadListener readListener) {
        }
    }
}