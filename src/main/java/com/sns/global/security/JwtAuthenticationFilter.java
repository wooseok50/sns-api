package com.sns.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.global.dto.CommonResponseDto;
import com.sns.global.jwt.JwtProvider;
import com.sns.global.jwt.repository.TokenRepository;
import com.sns.user.dto.LoginRequestDto;
import com.sns.user.entity.User;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtProvider jwtProvider, TokenRepository tokenRepository) {
        this.jwtProvider = jwtProvider;
        this.tokenRepository = tokenRepository;
        setFilterProcessesUrl("/api/users/login");
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(),
                LoginRequestDto.class);

            return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(),
                    requestDto.getPassword(),
                    null
                )
            );
        } catch (IOException | java.io.IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain, Authentication authResult) throws IOException, java.io.IOException {
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();

        String token = jwtProvider.generateAccessToken(user.getEmail(), user.getRole());
        String refreshToken = jwtProvider.generateRefreshToken(user.getEmail(), user.getRole());
        refreshToken = jwtProvider.substringToken(refreshToken);
        tokenRepository.register(user.getId(), refreshToken);
        response.addHeader(JwtProvider.AUTHORIZATION_ACCESS_TOKEN_HEADER_KEY, token);
        response.setStatus(HttpServletResponse.SC_OK);

        String jsonResponse = objectMapper.writeValueAsString(
            CommonResponseDto.<Void>builder().message("로그인에 성공하였습니다.").build());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
        HttpServletResponse response, AuthenticationException failed)
        throws IOException, java.io.IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        String jsonResponse = objectMapper.writeValueAsString(
            CommonResponseDto.<Void>builder().message("로그인에 실패하였습니다.").build());

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonResponse);
    }
}
