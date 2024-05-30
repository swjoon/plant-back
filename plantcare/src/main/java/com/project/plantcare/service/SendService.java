package com.project.plantcare.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.project.plantcare.dto.FindPwDTO;
import com.project.plantcare.dto.MailDTO;
import com.project.plantcare.entity.User;
import com.project.plantcare.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SendService {
	private final JavaMailSender mailSender;
	private static final String FROM_ADDRESS = "jetkid1446@gmail.com";
	private final UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	public MailDTO createMailAndChargePassword(FindPwDTO pwDTO) {
		String str = getTempPassword();
		MailDTO dto = new MailDTO();
		dto.setAddress(pwDTO.getUserId());
		dto.setTitle(pwDTO.getUserName() + "님의 임시비밀번호 안내 이메일 입니다.");
		dto.setMessage(
				"안녕하세요. 임시비밀번호 안내 관련 메일 입니다." + "[" + pwDTO.getUserName() + "]" + "님의 임시 비밀번호는 " + str + " 입니다.");
		updatePassword(str, pwDTO);
		return dto;
	}
	
	@Transactional
	public void updatePassword(String str, FindPwDTO pwDTO) {
        String pw = bCryptPasswordEncoder.encode(str);
        Optional<User> OptionalUser = userRepository.findById(pwDTO.getUserId());
		User user = OptionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
				"User not found with userId: " + pwDTO.getUserId()));
        user.updatePassword(pw);
        userRepository.save(user);
    }

	public String getTempPassword() {
		char[] charSet = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
				'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z' };
		String str = "";

		int idx = 0;
		for (int i = 0; i < 10; i++) {
			idx = (int) (charSet.length * Math.random());
			str += charSet[idx];
		}
		return str;
	}
	
	public void mailSend(MailDTO mailDto) {
        System.out.println("이메일 전송 완료");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);
    }

}
