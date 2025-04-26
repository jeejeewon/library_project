package VO;

import java.sql.Date;

//게시판테이블에서 조회해온 데이터를 저장할 VO
public class boardVO {

	//변수들 선언
	private int boardId;  //게시글 고유 번호
	private	int category;  //게시글 카테고리번호 (0:공지사항, 1:문의게시판, 2:내 서평)
	private String title;  //게시글 제목
	private String content;  //게시글 내용
	private String userId;  //유저 ID
	private int bookNo;  //도서번호
	private String file;  //첨부파일명
	private String bannerImg;  //배너파일명
	private Date date;  //게시글 작성일
	private int views;  //게시글 조회수
	private Boolean secret;  //게시글 공개 여부
	private String reply;  //게시글 답변
	
	//기본생성자
	public boardVO() {}
	
	//모든 게시판정보 받아서 boardVO객체를 만드는 생성자
	public boardVO(int boardId, int category, String title, String content, String userId, int bookNo, String file, String bannerImg, Date date, int views, Boolean secret, String reply) {
		
		super();
		this.boardId = boardId;
		this.category = category;
		this.title = title;
		this.content = content;
		this.userId = userId;
		this.bookNo = bookNo;
		this.file = file;
		this.bannerImg = bannerImg;
		this.date = date;
		this.views = views;
		this.secret = secret;
		this.reply = reply;
		
	}
}
