package ru.practicum.shareit.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ApiExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> notFoundExceptionHandler(NotFoundException exception) {
        return exceptionHandler(exception, HttpStatus.NOT_FOUND, "Ресурс не найден");
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> validationException(ValidationException exception) {
        return exceptionHandler(exception, HttpStatus.BAD_REQUEST, "Некорректные данные");
    }

    @ExceptionHandler(RepositoryException.class)
    public ResponseEntity<ErrorResponse> validationException(RepositoryException exception) {
        return exceptionHandler(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Некорректные данные");
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> runtimeException(RuntimeException exception) {
        return exceptionHandler(exception, HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера");
    }

    /**
     * Обработка исключения
     * 1. В тело ответа будет передано {@Link ErrorResponse} с {@Link ErrorResponse#description} заполненным
     * сообщением об ошибке из исключения
     * 2. Исключение будет записано в лог
     * @return {@Link ResponseEntity} с соответствующим статусом
     */
    private ResponseEntity<ErrorResponse> exceptionHandler(Exception e, HttpStatus httpStatus, String error) {
        // централизованное логирование ошибки
        log.error(e.getMessage(), e);
        return ResponseEntity
                .status(httpStatus)
                .body(new ErrorResponse(error, e.getMessage()));
    }
}
