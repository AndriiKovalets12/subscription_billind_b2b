package dev.kovalets.subscription_billind_b2b;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.OptimisticLockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntityNotFoundException.class)
    public ProblemDetail handleEntityNotFoundException(EntityNotFoundException ex) {

        log.warn("EntityNotFoundException message:{}", ex.getMessage(), ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(EntityExistsException.class)
    public ProblemDetail handleEntityExistsException(EntityExistsException ex) {

        log.warn("EntityExistsException message:{}", ex.getMessage(), ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ProblemDetail handleIllegalArgumentException(IllegalArgumentException ex){

        log.warn("IllegalArgumentException message:{}", ex.getMessage(), ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(OptimisticLockException.class)
    public ProblemDetail handleOptimisticLockException(OptimisticLockException ex){
        log.warn("Optimistic locking failure: {}", ex.getMessage(), ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleObjectOptimisticLockingFailureException(ObjectOptimisticLockingFailureException ex){
        log.warn("Optimistic locking failure: {}", ex.getMessage(), ex);

        return ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "The record was modified by another user. Please refresh and try again.");
    }

}
