package com.gs.core.web.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;

/**
 * Created by linjuntan on 2018/2/16.
 * email: ljt1343@gmail.com
 */
@Component
@Order(-2)
@Slf4j
public class GSWebExceptionHandler implements WebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        if (ex instanceof BaseWebException) {
            exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
            exchange.getResponse().getHeaders().add(HttpHeaders.CONTENT_TYPE, "application/json");
            DataBuffer dataBuffer = exchange.getResponse().bufferFactory().allocateBuffer();
            try {
                dataBuffer.write(ex.toString().getBytes("UTF-8"));
            } catch (Exception e) {
                log.error(" >>> handler exception error,", e);
                DataBufferUtils.release(dataBuffer);
                return Mono.error(ex);
            }
            exchange.getResponse().writeWith(Mono.just(dataBuffer));
            exchange.getResponse().setComplete();
        }
        return Mono.error(ex);
    }
}
