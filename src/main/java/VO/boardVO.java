package VO;

import java.sql.Date;

public class boardVO {

	//변수들 선언
	private int board_id;  //게시글 고유 번호
	private	int category;  //게시글 카테고리번호 (0:공지사항, 1:문의게시판, 2:내 서평)
	private String title;  //게시글 제목
	private String content;  //게시글 내용
	private String user_id;  //유저 ID
	private int book_no;  //도서번호
	private String file;  //첨부파일명
	private String banner_img;  //배너파일명
	private Date date;  //게시글 작성일
	private int views;  //게시글 조회수
	private Boolean secret;  //게시글 공개 여부
	private String reply;  //게시글 답변
	
	//기본생성자
	public boardVO() {}
	
}
