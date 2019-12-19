package com.xebia.fs101.config;

import com.xebia.fs101.service.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtTokenSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;
    private String tokenCookie = "AUTH_TOK";
    private int expiresIn = Integer.MAX_VALUE;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException,
            ServletException {
        System.out.println("onAuthenticationSuccess()");
        clearAuthenticationAttributes(request);
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String jws = this.jwtTokenHelper.generateToken(userDetails.getUsername());

        Cookie authCookie = new Cookie(this.tokenCookie, (jws));
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(this.expiresIn);
        authCookie.setPath("/");
        response.addCookie(authCookie);
        response.sendRedirect("/");
    }
}
