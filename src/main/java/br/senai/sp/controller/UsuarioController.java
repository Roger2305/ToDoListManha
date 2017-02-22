package br.senai.sp.controller;

import java.net.URI;
import java.util.HashMap;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.auth0.jwt.JWTSigner;

import br.senai.sp.dao.UsuarioDAO;
import br.senai.sp.model.TokenJWT;
import br.senai.sp.model.Usuario;

@RestController
public class UsuarioController {
	public static final String EMISSOR = "senai";
	public static final String SECRET = "ToDoListSENAIInformatica";

	@Autowired
	private UsuarioDAO dao;

	@RequestMapping(value = "/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> cadastroUsuario(@RequestBody Usuario usuario) {
		try {
			dao.cadastroUsuario(usuario);
			return ResponseEntity.created(URI.create("/usuario/" + usuario.getId())).body(usuario);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<Usuario>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Usuario>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<TokenJWT> logar(@RequestBody Usuario usuario) {
		try {
			Usuario user = dao.logar(usuario);
			if (user != null) {
				HashMap<String, Object> claims = new HashMap<String, Object>();
				claims.put("iss", EMISSOR);
				claims.put("id_user", user.getId());
				claims.put("nome_user", user.getNome());
				// hora atual em segundos
				long horaAtual = System.currentTimeMillis() / 1000;
				// hora_expiração (horaAtual + 1 hora(3600 segs))
				long horaExpiracao = horaAtual + 3600;
				claims.put("iat", horaAtual);
				claims.put("exp", horaExpiracao);
				JWTSigner signer = new JWTSigner(SECRET);
				TokenJWT tokenJwt = new TokenJWT();
				tokenJwt.setToken(signer.sign(claims));
				return ResponseEntity.ok(tokenJwt);
			} else {
				return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<TokenJWT>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
