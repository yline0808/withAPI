package net.ddns.yline.withAPI.service.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.ddns.yline.withAPI.domain.mailvo.MailVo;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl{
    private final JavaMailSender mailSender;

    private static final String title = "with 임시 비밀번호 안내 이메일입니다.";
    private static final String message = "안녕하세요. with 임시 비밀번호 안내 메일입니다."
            + "\n" + "회원님의 임시 비밀번호는 아래와 같습니다. 로그인 후 반드시 비밀번호를 변경해주세요." + "\n";
    private static final String fromAddress = "${spring.mail.username}";

    public MailVo createAuthCodeMail(String authCode, String memberEmail) {

        MailVo mailVo = MailVo.builder()
                .toAddress(memberEmail)
                .title("with 가입 코드 안내 이메일입니다.")
                .message("안녕하세요. with 가입 코드 안내 메일입니다.\n회원님의 가입 코드는 아래와 같습니다. 3분안에 가입 코드를 입력해주세요.\n"+authCode)
                .fromAddress(fromAddress)
                .build();

        log.info("메일 생성 완료");
        return mailVo;
    }

    public MailVo createPwUpdateMail(String tmpPassword, String memberEmail) {

        MailVo mailVo = MailVo.builder()
                .toAddress(memberEmail)
                .title(title)
                .message(message + tmpPassword)
                .fromAddress(fromAddress)
                .build();

        log.info("메일 생성 완료");
        return mailVo;
    }

    public void sendMail(MailVo mailVo) {
        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(mailVo.getToAddress());
        mailMessage.setSubject(mailVo.getTitle());
        mailMessage.setText(mailVo.getMessage());
        mailMessage.setFrom(mailVo.getFromAddress());
        mailMessage.setReplyTo(mailVo.getFromAddress());

        mailSender.send(mailMessage);

        log.info("메일 전송 완료");
    }
}
