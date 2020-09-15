package com.example.stock_check_api.utility;

import com.example.stock_check_api.dto.SignUpForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Component
public class CustomMailSender {

    @Autowired
    private JavaMailSender javaMailSender;


    private String generateMessage(String username, String password){
        return "<h1>登録が完了しました</h1>" +
                "<p>ユーザー名：" + username + "</p>" +
                "<p>パスワード" + password + "</p>";

    }
    public void sendEmail(SignUpForm signUpForm) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);
        helper.setTo(signUpForm.getEmail());
        helper.setSubject(signUpForm.getUsername() + "さん、登録が完了しました");
        helper.setText(generateMessage(signUpForm.getUsername(), signUpForm.getPassword()), true);
        javaMailSender.send(msg);

    }

}
