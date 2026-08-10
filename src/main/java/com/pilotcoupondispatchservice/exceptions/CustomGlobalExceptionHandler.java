package com.pilotcoupondispatchservice.exceptions;

import com.pilotcoupondispatchservice.payloads.ApiErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class CustomGlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        String message = errors.isEmpty() ? "Something went wrong. Request argument not valid." : errors.get(0);

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(HttpStatus.valueOf(status.value()), new Date(), message, errors, ((ServletWebRequest) request).getRequest().getRequestURI());
        return handleExceptionInternal(ex, apiErrorResponse, headers, apiErrorResponse.getStatus(), request);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<?> handleBindException(BindException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());

        String message = errors.isEmpty() ? "Something went wrong." : errors.get(0);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, errors, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String error = ex.getValue() + " value for " + ex.getPropertyName() + " should be of type " + ex.getRequiredType();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(
                new ApiErrorResponse(HttpStatus.valueOf(status.value()), new Date(), error, error, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(MissingServletRequestPartException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = ex.getRequestPartName() + " part is missing";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(
                new ApiErrorResponse(HttpStatus.valueOf(status.value()), new Date(), message, message, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String error = ex.getParameterName() + " parameter is missing";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(
                new ApiErrorResponse(HttpStatus.valueOf(status.value()), new Date(), error, error, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String error = "Request Body is missing";
        String[] split = ex.getMessage() == null ? new String[]{} : ex.getMessage().split(";");
        String message = split.length > 1 ? split[0] : ex.getMessage();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(
                new ApiErrorResponse(HttpStatus.valueOf(status.value()), new Date(), message, error, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<?> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String error = ex.getName() + " should be of type " + (ex.getRequiredType() == null ? "" : ex.getRequiredType().getName());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), error, error, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleConstraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        List<String> errors = ex.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());

        String message = errors.isEmpty() ? "Something went wrong. Requested variable not valid." : errors.get(0);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, errors, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(MultipartException.class)
    public ResponseEntity<?> handleMultipartException(MultipartException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, message, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).headers(headers).body(
                new ApiErrorResponse(HttpStatus.PAYLOAD_TOO_LARGE, new Date(), message, message, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String error = "Api Endpoint not found for " + ex.getHttpMethod() + ":" + ex.getRequestURL();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).headers(new HttpHeaders()).body(
                new ApiErrorResponse(HttpStatus.NOT_FOUND, new Date(), error, error, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" Method is not supported for this request. Supported methods are ");
        if (ex.getSupportedHttpMethods() != null) {
            ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
        }

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).headers(new HttpHeaders()).body(
                new ApiErrorResponse(HttpStatus.METHOD_NOT_ALLOWED, new Date(), builder.toString(), builder.toString(), ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(HttpMediaTypeNotAcceptableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(
                new ApiErrorResponse(HttpStatus.NOT_ACCEPTABLE, new Date(), message, "Media Type not acceptable.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types are ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).headers(headers).body(
                new ApiErrorResponse(HttpStatus.UNSUPPORTED_MEDIA_TYPE, new Date(), message, builder.toString(), ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(ServletRequestBindingException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).headers(headers).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, message, ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ApiErrorResponse(HttpStatus.NOT_FOUND, new Date(), message, "Resource not found error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(ResourceAlreadyExistException.class)
    public ResponseEntity<?> handleResourceAlreadyExistException(ResourceAlreadyExistException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "Resource already exist error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(InvalidRequestException.class)
    public ResponseEntity<?> handleInvalidRequestException(InvalidRequestException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "Invalid request error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    public ResponseEntity<?> handleInvalidAuthenticationException(InvalidAuthenticationException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ApiErrorResponse(HttpStatus.UNAUTHORIZED, new Date(), message, "Authentication error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(PermissionDeniedException.class)
    public ResponseEntity<?> handlePermissionDeniedException(PermissionDeniedException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                new ApiErrorResponse(HttpStatus.FORBIDDEN, new Date(), message, "Authorization error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(VerificationFailedException.class)
    public ResponseEntity<?> handleVerificationFailedException(VerificationFailedException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "Verification failed error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(MissingHeaderException.class)
    public ResponseEntity<?> handleMissingHeaderException(MissingHeaderException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "Header missing error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(FileStorageException.class)
    public ResponseEntity<?> handleFileStorageException(FileStorageException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "File operation failed error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(MessageSendingException.class)
    public ResponseEntity<?> handleMessageSendingException(MessageSendingException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "Failed to send message error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(DataProcessingException.class)
    public ResponseEntity<?> handleDataProcessingException(DataProcessingException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ApiErrorResponse(HttpStatus.BAD_REQUEST, new Date(), message, "Data processing failed error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleAll(Exception ex, WebRequest request) {
        log.error(ex.getMessage(), ex);

        String message = StringUtils.hasText(ex.getMessage()) ? ex.getMessage() : "Something went wrong.";

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, new Date(), message, "Unexpected error occurred.", ((ServletWebRequest) request).getRequest().getRequestURI())
        );
    }
}
