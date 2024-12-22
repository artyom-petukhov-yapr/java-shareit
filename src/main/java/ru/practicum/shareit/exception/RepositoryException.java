package ru.practicum.shareit.exception;

/**
 * Исключение слоя работы с данными.
 * Данный тип используется в InMemory-реализации для обработки ситуаций:
 * - данные не найдены
 * - not-null ограничение привело к ошибке
 * - ограничению на уникальность привело к ошибке.
 * При переходе с InMemory-реализаций к примеру на JPA-реализацию можно будет отказаться от этого класса
 */
public class RepositoryException extends RuntimeException {
    public RepositoryException(String message) {
        super(message);
    }
}
