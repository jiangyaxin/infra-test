package com.jyx.infra.mail;

import com.jyx.infra.log.Logs;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

/**
 * @author jiangyaxin
 * @since 2021/11/23 12:30
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    @Value("${spring.mail.username}")
    private String from;

    private final JavaMailSender javaMailSender;

    @Override
    public MimeMessage createMimeMessage() {
        return javaMailSender.createMimeMessage();
    }

    @Override
    public List<CompletableFuture<Void>> asyncSend(ExecutorService executorService, List<MimeMessagePreparator> mimeMessageList) {
        return mimeMessageList.stream()
                .map(mimeMessage -> CompletableFuture.runAsync(() -> send(mimeMessage), executorService)
                        .exceptionally(ex -> {
                            Logs.error(log, "发送失败: {}", mimeMessage, ex);
                            return null;
                        }))
                .collect(Collectors.toList());
    }

    @Override
    public void send(MimeMessage mimeMessage) {
        javaMailSender.send(mimeMessage);
    }

    @Override
    public void send(MimeMessagePreparator mimeMessagePreparator) {
        javaMailSender.send(mimeMessagePreparator);
    }

    @Override
    public void send(SimpleMailMessage simpleMessage) {
        javaMailSender.send(simpleMessage);
    }

    @Override
    public String getFrom() {
        return from;
    }

}
