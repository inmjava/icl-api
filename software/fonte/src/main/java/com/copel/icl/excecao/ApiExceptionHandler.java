package com.copel.icl.excecao;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


import com.fasterxml.jackson.databind.JsonMappingException.Reference;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.PropertyBindingException;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(ApiExceptionHandler.class);
	
	public static final String MSG_ERRO_GENERICA_USUARIO_FINAL
		= "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
				+ "o problema persistir, entre em contato com o administrador do sistema.";
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

	    ProblemType problemType = ProblemType.DADOS_INVALIDOS;
	    String detail = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.";
	    
	    BindingResult bindingResult = ex.getBindingResult();
	    
	    List<Problem.Object> problemObjects = bindingResult.getAllErrors().stream()
	    		.map(objectError -> {
	    			String message = messageSource.getMessage(objectError, LocaleContextHolder.getLocale());
	    			
	    			String name = objectError.getObjectName();
	    			
	    			if (objectError instanceof FieldError) {
	    				name = ((FieldError) objectError).getField();
	    			}
	    			
	    			return new Problem.Object(name, message);
	    		})
	    		.collect(Collectors.toList());
	    
	    Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail, problemObjects);
	    
	    return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleUncaught(Exception ex, WebRequest request) {
		HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;		
		ProblemType problemType = ProblemType.ERRO_DE_SISTEMA;
		String detail = MSG_ERRO_GENERICA_USUARIO_FINAL;
		List<String> problems = new ArrayList<>();
		
		ex.printStackTrace();
		
		problems.add(ex.getLocalizedMessage());
		
		Throwable hasCause = ex.getCause();
		while(hasCause != null) {
			problems.add(hasCause.getClass().getCanonicalName() + ": " + hasCause.getLocalizedMessage());
			hasCause = hasCause.getCause();
		}
		
		String joinErrors = problems.stream().collect(Collectors.joining(",", "[", "]"));
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), joinErrors, detail);
	
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, 
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = String.format("O recurso %s, que você tentou acessar, é inexistente.", 
				ex.getRequestURL());
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, MSG_ERRO_GENERICA_USUARIO_FINAL);
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (ex instanceof MethodArgumentTypeMismatchException) {
			return handleMethodArgumentTypeMismatch(
					(MethodArgumentTypeMismatchException) ex, headers, status, request);
		}
	
		return super.handleTypeMismatch(ex, headers, status, request);
	}
	
	private ResponseEntity<Object> handleMethodArgumentTypeMismatch(
			MethodArgumentTypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {

		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;

		String detail = String.format("O parâmetro de URL '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				ex.getName(), ex.getValue(), ex.getRequiredType().getSimpleName());

		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, MSG_ERRO_GENERICA_USUARIO_FINAL);
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		Throwable rootCause = ExceptionUtils.getRootCause(ex);
		
		if (rootCause instanceof InvalidFormatException) {
			return handleInvalidFormat((InvalidFormatException) rootCause, headers, status, request);
		} else if (rootCause instanceof PropertyBindingException) {
			return handlePropertyBinding((PropertyBindingException) rootCause, headers, status, request); 
		}
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = "O corpo da requisição está inválido. Verifique erro de sintaxe.";
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, MSG_ERRO_GENERICA_USUARIO_FINAL);
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	
	private ResponseEntity<Object> handlePropertyBinding(PropertyBindingException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' não existe. "
				+ "Corrija ou remova essa propriedade e tente novamente.", path);

		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, MSG_ERRO_GENERICA_USUARIO_FINAL);
		
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	private ResponseEntity<Object> handleInvalidFormat(InvalidFormatException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		String path = joinPath(ex.getPath());
		
		ProblemType problemType = ProblemType.MENSAGEM_INCOMPREENSIVEL;
		String detail = String.format("A propriedade '%s' recebeu o valor '%s', "
				+ "que é de um tipo inválido. Corrija e informe um valor compatível com o tipo %s.",
				path, ex.getValue(), ex.getTargetType().getSimpleName());
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, MSG_ERRO_GENERICA_USUARIO_FINAL);
		
		return handleExceptionInternal(ex, problem, headers, status, request);
	}
	
	@ExceptionHandler({DomainException.class})
	public ResponseEntity<?> handleNegocio(DomainException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_NEGOCIO;
		String detail = ex.getMessage();
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler({ConstraintViolationException.class})
	public ResponseEntity<?> handleValidacoesBeanValidation(ConstraintViolationException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Um ou mais campos estão inválidos (ConstraintViolation). Faça o preenchimento correto e tente novamente.";
		
		List<Problem.Object> problemObjects = ex.getConstraintViolations().stream()
				.map( cv -> {
					String message = "";
					String name = "";
					if (cv != null) {
						message = cv.getMessage();
						name = cv.getPropertyPath().toString();
					}
					return new Problem.Object(name, message);
						
				})
				.collect(Collectors.toList());
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail, problemObjects);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler({IntegrationException.class})
	public ResponseEntity<?> handleIntegracao(IntegrationException ex, WebRequest request) {

		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.ERRO_INTEGRACAO;
		String detail = ex.getMessage();
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler({NotYetImplementedException.class})
	public ResponseEntity<?> handleNotYetImplementedException(NotYetImplementedException ex, WebRequest request) {

		HttpStatus status = HttpStatus.NOT_IMPLEMENTED;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
	
	@ExceptionHandler({ NotFoundException.class })
	public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
		HttpStatus status = HttpStatus.NOT_FOUND;
		ProblemType problemType = ProblemType.RECURSO_NAO_ENCONTRADO;
		String detail = ex.getMessage();
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);	
	}
	
	@ExceptionHandler({ BadRequestException.class })
	public ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
		HttpStatus status = HttpStatus.BAD_REQUEST;
		ProblemType problemType = ProblemType.PARAMETRO_INVALIDO;
		String detail = ex.getMessage();
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);	
	}
	
	
	/* 
	 * Remember:
	 * Spring Security's request handling all takes place in the filter chain, 
	 * before the dispatcher servlet is invoked, so it doesn't know anything about Spring MVC exception handlers
	 * 
	 */
	@ExceptionHandler({ AccessDeniedException.class })
	public ResponseEntity<Object> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
		HttpStatus status = HttpStatus.FORBIDDEN;
		ProblemType problemType = ProblemType.NAO_AUTORIZADO;
		String detail = ex.getMessage();
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), detail, detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);	
	}	
	
	
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		
		if (body == null) {
			body = new Problem(status.value(), status.getReasonPhrase(), null, MSG_ERRO_GENERICA_USUARIO_FINAL);
		} else if (body instanceof String) {
			body =  new Problem(status.value(), (String) body, null, MSG_ERRO_GENERICA_USUARIO_FINAL);
		}
		logger.error(ex.getMessage(), ex);
		return super.handleExceptionInternal(ex, body, headers, status, request);
	}

	private String joinPath(List<Reference> references) {
		return references.stream()
			.map(ref -> ref.getFieldName())
			.collect(Collectors.joining("."));
	}
	
	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public ResponseEntity<Object> handleMaxUploadSizeExceededException(Exception ex, WebRequest request) {
		
		HttpStatus status = HttpStatus.BAD_REQUEST;	
		ProblemType problemType = ProblemType.DADOS_INVALIDOS;
		String detail = "Arquivo excedeu o tamanho limite permitido";
		
		Problem problem = new Problem(status.value(), problemType.getTitle(), ex.getMessage(), detail);
		
		return handleExceptionInternal(ex, problem, new HttpHeaders(), status, request);
	}
}
