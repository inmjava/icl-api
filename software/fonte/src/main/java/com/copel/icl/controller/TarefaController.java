package com.copel.icl.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springdoc.core.converters.models.PageableAsQueryParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.copel.icl.doc.OpenApi30Config;
import com.copel.icl.entidade.Tarefa;
import com.copel.icl.excecao.NotFoundException;
import com.copel.icl.service.TarefaService;

import datadog.trace.api.Trace;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;


@CrossOrigin
@RestController
@Validated

@Tag(name = "Controlador de tarefas", description = "Exemplo de CRUD Rest API + validation + OpenAPI via SpringDoc")
/*
 URI paths iguais sao agrupadas na mesma documentacao, portanto, nao eh possivel documentar separadamente /tarefas  e  /tarefas?nome=xxx  por exemplo.
 Ha uma incongruencia entre a OpenAPI e o mecanismo spring de mapeamento para metodos do controller quando utiliza-se como diferenciais apenas
 query params, headers, tipo de dado consumidor ou tipo de dado produzido, mantendo-se a uri.  (Semelhante a method overloading na O.O.).
 Trick: OpenAPI diferenciou /tarefas?xx=yy de /tarefas/?xx=yy.
*/
public class TarefaController {

	private static final Logger logger = LoggerFactory.getLogger(TarefaController.class);
	
	@Autowired
	private TarefaService tarefaService;
	
	@Trace
	@PageableAsQueryParam
	@GetMapping(value = "/tarefasPaginadas")
	public Page<Tarefa> allPaginada( @Parameter(hidden = true) @PageableDefault(size = 5) Pageable paginacao) {
		logger.info("Paginacao utilizada: " + paginacao);
		Page<Tarefa> tarefas = tarefaService.allPaginada(paginacao);
		return tarefas;
	}
	
	
	@Trace
	@Operation(operationId = "all", summary = "Lista tarefas", parameters = {})
	@ApiResponses( value = {
			@ApiResponse(responseCode = "200", description = "Tarefas encontradas", content = {
					@Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = Tarefa.class))) }),
			@ApiResponse(responseCode = "204", description = "Não há tarefas para serem listadas", content = @Content)})
	@GetMapping(value = "/tarefas", params = {})
	public ResponseEntity<List<Tarefa>> all() {
		List<Tarefa> tarefas = tarefaService.all();

		if (tarefas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(tarefas, HttpStatus.OK);
	}
	
	
	
	@Trace
	@Operation(summary = "Cria nova tarefa. Obs.: autenticação basic é deixada aqui apenas como exemplo", security = {@SecurityRequirement(name = OpenApi30Config.BASIC_NAME)})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Tarefa criada com sucesso", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Tarefa.class)) }),
			@ApiResponse(responseCode = "400", description = "Tarefa inválida", content = @Content)

	})
	@PostMapping(value = "/tarefas")
	public ResponseEntity<Tarefa> novaTarefa(
			@Parameter(description = "Nova tarefa") @Valid @RequestBody Tarefa novaTarefa) {
		Tarefa tarefaSalva = tarefaService.novaTarefa(novaTarefa);
		return new ResponseEntity<>(tarefaSalva, HttpStatus.CREATED);
	}

	@Operation(summary = "Busca por identificador de tarefa")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tarefa localizada", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Tarefa.class)) }),
			@ApiResponse(responseCode = "404", description = "Tarefa não localizada", content = @Content) })
	@GetMapping(value = "/tarefas/{id}")
	public ResponseEntity<Tarefa> findById(
			@Parameter(description = "Identificador da tarefa") @PathVariable(value = "id") Long id) {
		Optional<Tarefa> tarefaOpt = tarefaService.findById(id);
		// return tarefaOpt.map(t ->
		// ResponseEntity.ok(tarefaOpt.get())).orElse(ResponseEntity.notFound().build());

		if (tarefaOpt.isPresent()) {
			return new ResponseEntity<>(tarefaOpt.get(), HttpStatus.OK);
		} else {
			// return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			throw new NotFoundException("Tarefa não localizada com id=" + id);
		}

	}

	@Operation(summary = "Altera uma tarefa. Obs.: autenticação Oauth2 (fluxo code) é deixada aqui apenas como exemplo", security = {@SecurityRequirement(name = OpenApi30Config.OAUTH2_CODE_NAME)})
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Tarefa alterada", content = {
					@Content(mediaType = "application/json", schema = @Schema(implementation = Tarefa.class)) }),
			@ApiResponse(responseCode = "404", description = "Tarefa não localizada", content = @Content) })
	@PutMapping(value = "/tarefas/{id}")
	public ResponseEntity<Tarefa> alterar(
			@Parameter(description = "Alterações a serem aplicadas") @Valid @RequestBody Tarefa tarefa,
			@Parameter(description = "Identificador da tarefa") @PathVariable(value = "id") Long id) {
		Tarefa tarefaAlterada = tarefaService.alterar(tarefa, id);
		return new ResponseEntity<>(tarefaAlterada, HttpStatus.OK);

	}

	@Operation(summary = "Exclui uma tarefa")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Tarefa excluída com sucesso", content = {
			@Content(mediaType = "application/json", schema = @Schema(implementation = Tarefa.class)) }) })
	@DeleteMapping(value = "/tarefas/{id}")
	public ResponseEntity<HttpStatus> excluir(
			@Parameter(description = "Identificador da tarefa") @PathVariable(value = "id") Long id) {
		tarefaService.excluir(id);
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	
	@Hidden
	@GetMapping(value = "/tarefas", params = { "nome" })
	public ResponseEntity<List<Tarefa>> findByNome(

			@RequestParam(value = "nome") @Valid @NotBlank @Size(min = 1, max = 10) String nome) {
		List<Tarefa> tarefas = tarefaService.findByNome(nome);

		if (tarefas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(tarefas, HttpStatus.OK);
	}
	
	
	@Operation(operationId = "findByNomeEStatus", summary = "Lista tarefas por nome e status", parameters = {
			@Parameter(name = "nome", in = ParameterIn.QUERY,   required = true, description = "nome para busca", example = "tarefa01", schema = @Schema(implementation = String.class)),
			@Parameter(name = "status", in = ParameterIn.QUERY,  required = true, description = "status para busca", example = "concluida", schema = @Schema(implementation = String.class)) })

	
	@GetMapping(value = "/tarefas/", params = { "nome", "status" })
	public ResponseEntity<List<Tarefa>> findByNomeEStatus(
			@RequestParam(value = "nome") @Valid @NotBlank @Size(min = 1, max = 10) String nome,
			@RequestParam(value = "status") @Valid @NotBlank @Size(min = 1, max = 10)  String status) {
		List<Tarefa> tarefas = tarefaService.findByNomeEStatus(nome, status);

		if (tarefas.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(tarefas, HttpStatus.OK);

	}
}