package br.senai.sp.controller;

import java.net.URI;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.dao.UsuarioDAO;
import br.senai.sp.model.TokenJWT;
import br.senai.sp.model.Usuario;

@RestController
public class UsuarioController {

	@Autowired
	private UsuarioDAO dao;

	@RequestMapping(value = "/usuario", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Usuario> cadastroUsuario(@RequestBody Usuario usuario) {
		String senha = usuario.getSenha();
		try {
			Md5PasswordEncoder encoder = new Md5PasswordEncoder();
			String md5 = encoder.encodePassword(senha, null);
			usuario.setSenha(md5);
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
	public ResponseEntity<TokenJWT> logar(Usuario usuario) {
		try {
			Usuario user = dao.logar(usuario);
			if (user != null) {
				return null;
			} else {
				return new ResponseEntity<TokenJWT>(HttpStatus.UNAUTHORIZED);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<TokenJWT>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
