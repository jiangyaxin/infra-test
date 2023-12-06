package com.jyx.infra.mail;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

/**
 * @author jiangyaxin
 * @since 2021/11/23 12:29
 */
public interface MailService {

    /**
     * Create MimeMessage.
     *
     * @return MimeMessage
     */
    MimeMessage createMimeMessage();

    /**
     * 并发发送
     *
     * @param mimeMessageList List<MimeMessagePreparator>
     * @return List<CompletableFuture < Void>>
     */
    List<CompletableFuture<Void>> asyncSend(ExecutorService executorService,List<MimeMessagePreparator> mimeMessageList);

    /**
     * mimeMessage
     *
     * @param mimeMessage mimeMessage
     */
    void send(MimeMessage mimeMessage);

    /**
     * mimeMessagePreparator
     *
     * @param mimeMessagePreparator mimeMessagePreparator
     */
    void send(MimeMessagePreparator mimeMessagePreparator);

    /**
     * simpleMessage
     *
     * @param simpleMessage simpleMessage
     */
    void send(SimpleMailMessage simpleMessage);

    /**
     * Get application.yml spring.mail.username
     *
     * @return from
     */
    String getFrom();
}
