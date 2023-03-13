package com.hanghae.navis.email.service;

import java.util.Random;

import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.hanghae.navis.common.dto.Message;
import com.hanghae.navis.common.util.RedisUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import static com.hanghae.navis.common.entity.ExceptionMessage.*;
import static com.hanghae.navis.common.entity.SuccessMessage.*;

@Service
@RequiredArgsConstructor
public class EmailService {

    @Autowired
    JavaMailSender emailSender;
    private final RedisUtil redisUtil;

    public static final String ePw = createKey();

    private MimeMessage createMessage(String to) throws Exception {
        System.out.println("보내는 대상 : " + to);
        System.out.println("인증 번호 : " + ePw);
        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(RecipientType.TO, to);//보내는 대상
        message.setSubject("이메일 인증 테스트");//제목

        String msgg = "";
        msgg += "<div style='margin:20px;'>";
        msgg += "<h1> 안녕하세요 NAVIS입니다. </h1>";
        msgg += "<br>";

        msgg += "<p>아래 코드를 입력해주세요<p>";
        msgg += "<div align='center' style='border:1px solid black; font-family:verdana';>";
        msgg += "<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>";
        msgg += "<div style='font-size:130%'>";
        msgg += "CODE : <strong>";
        msgg += ePw + "</strong><div><br/> ";
        msgg += "</div>";

//        msgg += "<h1>[이메일 인증]</h1> <p>아래 링크를 클릭하시면 이메일 인증이 완료됩니다.</p> " +
//                "<a href='http://localhost:8080/emails/confirm?key=" + ePw + "' target='_blenk'>이메일 인증 확인</a>";

        message.setText(msgg, "utf-8", "html");//내용
        message.setFrom(new InternetAddress("teamnavis99@gmail.com", "teamnavis99"));//보내는 사람

        return message;
    }

    public static String createKey() {
        StringBuffer key = new StringBuffer();
        Random rnd = new Random();

        for (int i = 0; i < 4; i++) { // 인증코드 생성
            //4자리 숫자 생성
            key.append((rnd.nextInt(10)));
        }
        return key.toString();
    }

    public ResponseEntity<Message> sendMail(String to) throws Exception {
        MimeMessage sendMessage = createMessage(to);

        try {
            emailSender.send(sendMessage);
            redisUtil.set(ePw, to, 10);
        } catch (MailException es) {
            return Message.toExceptionResponseEntity(EMAIL_SEND_FAIL);
        }
        return Message.toResponseEntity(EMAIL_SEND_SUCCESS);
    }

    public ResponseEntity<Message> emailConfirm(String key){
        //코드가 유효하지 않으면
        if (redisUtil.get(key) == null) {
            return Message.toExceptionResponseEntity(EMAIL_CODE_INVALID);
        }

        //코드가 유효하다면 키 삭제 후 ok보내줌
        redisUtil.delete(key);

        return Message.toResponseEntity(EMAIL_CONFIRM_SUCCESS);
    }
}