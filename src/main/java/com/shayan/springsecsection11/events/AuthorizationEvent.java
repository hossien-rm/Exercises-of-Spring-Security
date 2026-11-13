package com.shayan.springsecsection11.events;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authorization.event.AuthorizationDeniedEvent;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AuthorizationEvent {


    //don't write success because in real application we have 1000 or 10000 success authorize and have cost on server.

    @EventListener
    public void onFailure(AuthorizationDeniedEvent deniedEvent) {
        log.error("Login failed for user: {} due to : {}", deniedEvent.getAuthentication().get().getName()
                , deniedEvent.getAuthorizationResult());
    }


}
