package com.sns.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sns.global.dto.CommonResponseDto;
import com.sns.global.jwt.JwtProvider;
import com.sns.global.jwt.TokenState;
import com.sns.global.jwt.entity.RefreshTokenEntity;
import com.sns.global.jwt.repository.TokenRepository;
import com.sns.user.entity.UserRole;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JWT 검증 및 인가")
@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;
    private final UserDetailsServiceImpl userDetailsService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(
        HttpServletRequest req, HttpServletResponse res, FilterChain filterChain)
        throws ServletException, IOException {

        String tokenValue = jwtProvider.getAccessTokenFromRequest(req);

        if (StringUtils.hasText(tokenValue)) {
            TokenState state = jwtProvider.validateToken(tokenValue);

            if (state.equals(TokenState.EXPIRED)) {
                try {
                    Claims info = jwtProvider.getMemberInfoFromExpiredToken(tokenValue);
                    UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(
                        info.getSubject());
                    RefreshTokenEntity refreshToken = tokenRepository.findByUserId(
                        userDetails.getUser().getId());

                    TokenState refreshState = jwtProvider.validateToken(refreshToken.getToken());

                    if(refreshState.equals(TokenState.VALID)) {
                        String newAccessToken = jwtProvider.generateRefreshToken(userDetails.getUsername(), UserRole.USER);
                        res.addHeader(JwtProvider.AUTHORIZATION_ACCESS_TOKEN_HEADER_KEY, newAccessToken);
                        res.setStatus(HttpServletResponse.SC_OK);

                        String jsonResponse = objectMapper.writeValueAsString(
                            CommonResponseDto.<Void>builder().message("새로운 Acces Token이 발급되었습니다.")
                                .build());

                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        res.getWriter().write(jsonResponse);
                        return;
                    } else {
                        res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

                        String jsonResponse = objectMapper.writeValueAsString(
                            CommonResponseDto.<Void>builder()
                                .message("Access Token과 Refresh Token이 모두 만료되었습니다.")
                                .build());

                        tokenRepository.deleteToken(refreshToken);

                        res.setContentType("application/json");
                        res.setCharacterEncoding("UTF-8");
                        res.getWriter().write(jsonResponse);
                        return;
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            } else if(state.equals(TokenState.INVALID)) {
                log.error("Token Error");
                return;
            }

            Claims info = jwtProvider.getMemberInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e) {
                log.error(e.getMessage());
                return;
            }
        }

        filterChain.doFilter(req, res);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
