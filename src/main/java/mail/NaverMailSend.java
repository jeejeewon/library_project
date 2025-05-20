package mail;

import java.util.Properties;
import java.util.Random;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

public final class NaverMailSend {

	private final String host = "smtp.naver.com";
	private final String user = "calculon@naver.com";
	private final String password = "HCHKH8L3L5WL";

	public String sendEmail(String to) throws Exception {

		String authenCode = null;

		// 네이버 SMTP 서버 접속 정보
		/* Property 객체에 SMTP 서버 정보 설정 */
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.port", "465"); // SMTP 포트
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.ssl.trust", host);

		/* SMTP 서버 정보와 사용자 정보를 기반으로 Session 클래스의 인스턴스를 생성 */
		Session session = Session.getDefaultInstance(props, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		/* Message 객체에 수신자와 내용, 제목의 메시지를 작성 */
		try {
			// 인증번호 생성
			authenCode = makeAuthenticationCode();

			Message message = new MimeMessage(session);

			// 발신자 설정
			message.setFrom(new InternetAddress(user, "MVC Book SHOP"));

			// 수신자 메일주소 설정
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

			// 메일제목 설정
			message.setSubject("MVCBookSHOP :: 임시 비밀번호 메일입니다.");

			// 메일 내용 설정
			message.setText("비밀번호 변경 인증번호는 [ " + authenCode + " ] 입니다.");

			// Send the message
			Transport.send(message);

			System.out.println(" NaverMailSend : Email sent successfully.");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		System.out.println(" NaverMailSend : sendEmail() 종료");
		return authenCode;
	}

	/** 인증코드 생성 메서드 */
	private String makeAuthenticationCode() throws Exception {

		int pwdLength = 8;
		final char[] pwdTable = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q',
				'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l',
				'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', '!', '@', '#', '$', '%', '^', '&',
				'*', '(', ')', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0' };

		// System.currentTimeMillis(): 중복 방지 처리
		Random ran = new Random(System.currentTimeMillis());

		StringBuffer bf = new StringBuffer();
		for (int i = 0; i < pwdLength; i++) {
			bf.append(pwdTable[ran.nextInt(pwdTable.length)]);

		}

		return bf.toString();
	}

}
