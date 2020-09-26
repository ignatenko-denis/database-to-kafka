package com.sample.util.db;

import com.sample.entity.TradeType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.sample.util.DateUtil.toMillis;

@Slf4j
@Aspect
@Component
public class DatabaseAspect {
    @Autowired
    private PageSizeCalculator pageSizeCalculator;

    @Around("@annotation(com.sample.util.db.DatabaseExecutionTime)")
    @SuppressWarnings("checkstyle:IllegalThrows")
    public Object dbExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        final long start = System.nanoTime();
        final Object proceed = joinPoint.proceed();
        final long executionTime = System.nanoTime() - start;

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        if (!(proceed instanceof Integer)) {
            log.error("invalid method {}, should return type 'int'", signature);
            return proceed;
        }

        int amount = (int) proceed;
        DatabaseExecutionTime annotation = signature.getMethod().getAnnotation(DatabaseExecutionTime.class);
        TradeType type = annotation.value();

        pageSizeCalculator.add(executionTime, amount, type);

        log.info("{}, amount {}: {} millis, {}", signature, amount,
                toMillis(executionTime), type);

        return proceed;
    }
}
