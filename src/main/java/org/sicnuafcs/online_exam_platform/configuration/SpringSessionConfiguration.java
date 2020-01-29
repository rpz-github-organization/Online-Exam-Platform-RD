package org.sicnuafcs.online_exam_platform.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.events.SessionCreatedEvent;
import org.springframework.session.events.SessionDeletedEvent;
import org.springframework.session.events.SessionExpiredEvent;

/**
 * 创建监听配置类
 */

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 30*60*100)
@Slf4j
public class SpringSessionConfiguration {
    //redis内session过期时事件监听
    @EventListener
    public void onSessionExpired(SessionExpiredEvent expiredEvent) {
        String sessionId = expiredEvent.getSessionId();

        log.info("session失效事件："+sessionId);
    }

    //redis内session创建事件监听
    @EventListener
    public void onSessionCreated(SessionCreatedEvent createdEvent) {
        String sessionId = createdEvent.getSessionId();

        log.info("创建session事件："+sessionId);
    }

    //redis内session删除事件监听
    @EventListener
    public void onSessionDeleted (SessionDeletedEvent deletedEvent) {
        String sessionId = deletedEvent.getSessionId();

        log.info("删除session事件："+sessionId);
    }
}
