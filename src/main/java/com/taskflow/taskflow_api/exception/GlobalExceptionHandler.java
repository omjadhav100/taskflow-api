package com.taskflow.taskflow_api.exception;

import java.util.HashMap;
import java.util.Map;

import com.taskflow.taskflow_api.exception.TaskListNotFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Catches @Valid failures - turns them into a clean field-by-field error map
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
            errors.put(error.getField(), error.getDefaultMessage())
        );

        return ResponseEntity.badRequest().body(errors);
    }
      @ExceptionHandler(BoardNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleBoardNotFound(
            BoardNotFoundException ex) {
   ErrorResponse error =
        new ErrorResponse(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage());

return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
        @ExceptionHandler(TaskListNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleTaskListNotFound(
            TaskListNotFoundException ex) {

        Map<String, String> error = Map.of("error", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }
      @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGeneral(Exception ex) {
        Map<String, String> error = Map.of("error", "Something went wrong. Please try again.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}