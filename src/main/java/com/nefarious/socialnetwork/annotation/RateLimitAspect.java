package com.nefarious.socialnetwork.annotation;

import com.nefarious.socialnetwork.exceptions.BusinessException;
import com.nefarious.socialnetwork.service.RateLimiterService;
import com.nefarious.socialnetwork.util.enums.BaseErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

/**
 * Aspect that intercepts methods annotated with @RateLimit and enforces rate limiting
 * using Redis and configuration properties.
 *
 * <p>This uses Redis to track the number of attempts and rejects calls
 * when the threshold is exceeded.
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class RateLimitAspect {
    private final RateLimiterService rateLimiterService;

    /**
     * Intercepts methods with @RateLimit, resolves the Redis key using method parameters,
     * and enforces the configured limit.
     *
     * @param joinPoint the method invocation being intercepted
     * @param rateLimit the annotation with rate limit config
     * @return the result of the method call, if allowed
     * @throws Throwable if the limit is exceeded or method throws
     */
    @Around("@annotation(rateLimit)")
    public Object around(ProceedingJoinPoint joinPoint, RateLimit rateLimit) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String keyTemplate = rateLimit.key();
        Object[] args = joinPoint.getArgs();
        String[] paramNames = signature.getParameterNames();

        String resolvedKey = keyTemplate;
        for (int i = 0; i < paramNames.length; i++) {
            resolvedKey = resolvedKey.replace("{" + paramNames[i] + "}", args[i].toString());
        }

        if (!rateLimiterService.tryAcquire(resolvedKey, rateLimit.property())) {
            throw new BusinessException(BaseErrorCode.TOO_MANY_ATTEMPTS);
        }

        return joinPoint.proceed();
    }
}
