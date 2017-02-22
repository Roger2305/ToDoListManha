package br.senai.sp.filtro;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import com.auth0.jwt.JWTVerifier;

import br.senai.sp.controller.UsuarioController;

//@WebFilter("/*")
public class FiltroToken implements Filter {

	public void destroy() {
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;

		if (request.getRequestURI().contains("login") || request.getRequestURI().contains("usuario")) {
			chain.doFilter(request, response);
		} else {
			String token = null;
			try {
				token = request.getHeader("Authorization");
				JWTVerifier verifier = new JWTVerifier(UsuarioController.SECRET);
				Map<String, Object> claims = verifier.verify(token);
				System.out.println("Nome do usuário: " + claims.get("nome_user"));
				chain.doFilter(request, response);
			} catch (Exception e) {
				e.printStackTrace();
				if (token == null) {
					response.sendError(HttpStatus.UNAUTHORIZED.value());
				} else {
					response.sendError(HttpStatus.FORBIDDEN.value());
				}

			}
		}

	}

	public void init(FilterConfig arg0) throws ServletException {
	}

}
