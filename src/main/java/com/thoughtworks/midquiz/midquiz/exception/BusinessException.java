package com.thoughtworks.midquiz.midquiz.exception;

import com.netflix.hystrix.exception.ExceptionNotWrappedByHystrix;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BusinessException extends RuntimeException implements ExceptionNotWrappedByHystrix {

    private HttpStatus status;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }


}
