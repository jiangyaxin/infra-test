package com.jyx.feature.test.mail;

import com.jyx.infra.mail.MailService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.MimeMailMessage;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author JYX
 * @since 2021/11/23 15:29
 */
@SpringBootTest
public class MailTest {

    @Autowired
    private MailService mailService;

    @Test
    public void sendMail(){
        MimeMessagePreparator mimeMessagePreparator = mimeMessage -> {
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
            mimeMessageHelper.setFrom(mailService.getFrom());
            mimeMessageHelper.setTo("ganxjie@163.com");
            mimeMessageHelper.setSubject("I am a Tester.");
            mimeMessageHelper.setText("Em... puta puta puta");
        };

        mailService.send(mimeMessagePreparator);
    }

    @Test
    public void sendSimpleMail(){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(mailService.getFrom());
        message.setTo("jiangyaxin@sutpc.com");
        message.setSubject("I am a Tester.");
        message.setText("Em... puta puta puta");

        mailService.send(message);
    }

    @Test
    public void sendMineMail() throws Exception{
        MimeMessage mimeMessage = mailService.createMimeMessage();

        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
        mimeMessageHelper.setFrom(mailService.getFrom());
        mimeMessageHelper.setTo("jiangyaxin@sutpc.com");
        mimeMessageHelper.setSubject("I am a Tester.");
        mimeMessageHelper.setText("Em... puta puta puta");

        mailService.send(mimeMessage);
    }

    @Test
    public void send50Mail(){

        List<MimeMessagePreparator> mimeMessagePrepareList = IntStream.range(1, 51)
                .mapToObj(index -> (MimeMessagePreparator) mimeMessage -> {
                    MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
                    mimeMessageHelper.setFrom(mailService.getFrom());
                    mimeMessageHelper.setTo("jiangyaxin@sutpc.com");
                    mimeMessageHelper.setSubject("I am a Tester" + index + ".");
                    mimeMessageHelper.setText("Em... puta puta puta "+index);
                })
                .collect(Collectors.toList());
        List<CompletableFuture<Void>> completableFutureList = mailService.asyncSend(mimeMessagePrepareList);
        CompletableFuture.allOf(completableFutureList.toArray(new CompletableFuture[0])).join();
    }
}
