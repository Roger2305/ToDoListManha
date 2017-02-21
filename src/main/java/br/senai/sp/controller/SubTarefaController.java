package br.senai.sp.controller;

import java.net.URI;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.senai.sp.dao.SubTarefaDAO;
import br.senai.sp.model.SubTarefa;

@RestController
public class SubTarefaController {

	@Autowired
	private SubTarefaDAO dao;

	@RequestMapping(value = "/tarefa/{id}/subtarefa", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<SubTarefa> addSubtarefa(@PathVariable("id") Long idTarefa, @RequestBody SubTarefa subTarefa) {
		try {
			dao.criarSubTarefa(idTarefa, subTarefa);
			return ResponseEntity.created(URI.create("/subtarefa/" + subTarefa.getId())).body(subTarefa);
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<SubTarefa>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<SubTarefa>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/subtarefa/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.GET)
	public SubTarefa buscarSubTarefa(@PathVariable Long id) {
		return dao.buscar(id);
	}

	@RequestMapping(value = "/subtarefa/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, method = RequestMethod.PUT)
	public ResponseEntity<Void> marcarFeito(@PathVariable Long id, @RequestBody SubTarefa subTarefa) {
		try {
			boolean feito = subTarefa.isFeita();
			dao.marcarFeito(id, feito);
			HttpHeaders header = new HttpHeaders();
			header.setLocation(URI.create("/subtarefa/" + id));
			ResponseEntity<Void> response = new ResponseEntity<Void>(header, HttpStatus.OK);
			return response;
		} catch (ConstraintViolationException e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
		}
	}

	@RequestMapping(value = "/subtarefa/{idSubtarefa}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> excluir(@PathVariable Long idSubtarefa) {
		dao.excluir(idSubtarefa);
		return ResponseEntity.noContent().build();
	}
}
