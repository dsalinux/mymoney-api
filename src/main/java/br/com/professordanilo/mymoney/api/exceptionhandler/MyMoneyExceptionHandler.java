package br.com.professordanilo.mymoney.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MyMoneyExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	private MessageSource messageSource;
	
	@Override	
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String mensagem = messageSource.getMessage("mensagem.invalida", null, LocaleContextHolder.getLocale());
		List<Error> errors = Arrays.asList(new Error(mensagem, ex.getMessage()));
		return handleExceptionInternal(ex, errors, headers, HttpStatus.BAD_REQUEST, request);
	}
	
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		return handleExceptionInternal(ex, createErrors(ex.getBindingResult()), headers, HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler({EmptyResultDataAccessException.class})
	protected ResponseEntity<Object> handleEmptyResulDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
		String mensagem = messageSource.getMessage("recurso.nao_encontrado", null, LocaleContextHolder.getLocale());
		List<Error> errors = Arrays.asList(new Error(mensagem, ex.toString()));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request){
		String mensagem = messageSource.getMessage("recurso.operacao_nao_permitida", null, LocaleContextHolder.getLocale());
		List<Error> errors = Arrays.asList(new Error(mensagem, ex.getMostSpecificCause().toString()));
		return handleExceptionInternal(ex, errors, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	
	
	private List<Error> createErrors(BindingResult bindingResult){
		List<Error> errors = new ArrayList<>();
		
		for (FieldError fieldError : bindingResult.getFieldErrors()) {
			
			errors.add(new Error(messageSource.getMessage(fieldError, LocaleContextHolder.getLocale()), fieldError.toString()));
		}
		
		return errors;
	}
	
	public static class Error {
		
		private String message;
		private String exception;
		
		public Error(String message, String ex) {
			this.message = message;
			this.exception = ex;
		}
		
		public String getMessage() {
			return message;
		}
		public void setMessage(String message) {
			this.message = message;
		}
		public String getException() {
			return exception;
		}
		public void setException(String exception) {
			this.exception = exception;
		}
		
		
		
	}
	
}
