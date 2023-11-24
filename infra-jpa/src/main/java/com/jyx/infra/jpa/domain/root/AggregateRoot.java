package com.jyx.infra.jpa.domain.root;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;
import com.jyx.infra.jpa.domain.audit.Auditable;
import com.jyx.infra.spring.event.GuavaEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;
import org.springframework.util.Assert;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.util.Collections;
import java.util.List;

/**
 * @author jiangyaxin
 * @since 2021/11/9 11:07
 */
@Getter
@SuperBuilder
@AllArgsConstructor
@MappedSuperclass
public class AggregateRoot extends Auditable {

    @JsonIgnore
    @Transient
    private final List<GuavaEvent> domainEvents = Lists.newCopyOnWriteArrayList();

    public <T extends GuavaEvent> T registerEvent(T event) {
        Assert.notNull(event, "Domain event is null.");
        domainEvents.add(event);
        return event;
    }

    @DomainEvents
    public List<GuavaEvent> events() {
        return Collections.unmodifiableList(domainEvents);
    }

    @AfterDomainEventPublication
    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

}
