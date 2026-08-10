package com.pilotcoupondispatchservice.annotations;

import com.pilotcoupondispatchservice.dao.I18NService;
import com.pilotcoupondispatchservice.dao.PermissionService;
import com.pilotcoupondispatchservice.exceptions.PermissionDeniedException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@AllArgsConstructor
public class HasPermissionAspect {

    private final PermissionService permissionService;
    private final I18NService i18NService;

    @Pointcut(value = "@annotation(permission)")
    public void callAt(HasPermission permission) {
    }

    @Around(value = "callAt(permission)", argNames = "joinPoint,permission")
    public Object around(ProceedingJoinPoint joinPoint, HasPermission permission) throws Throwable {

        if (!permissionService.hasAccessPermission(permission.permission())) {
            log.info(joinPoint.getSignature().toShortString() + " Permission Denied.");
            throw new PermissionDeniedException(i18NService.getMessage("error.permission_denied"));
        }

        return joinPoint.proceed();
    }
}
