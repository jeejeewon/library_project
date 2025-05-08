package Vo;

import java.sql.Timestamp;

public class RentalVo {
	private int rentNo;
	private String userId;
	private int bookNo;

	private Timestamp startDate;
	private Timestamp returnDue;
	private Timestamp returnDate;

	private int returnState; // 반납 여부 (0: 미반납, 1: 반납완료)

	private BookVo book; // JOIN -> 도서 정보 가져오기

	// Getters and Setters
	public int getRentNo() {
		return rentNo;
	}

	public void setRentNo(int rentNo) {
		this.rentNo = rentNo;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public int getBookNo() {
		return bookNo;
	}

	public void setBookNo(int bookNo) {
		this.bookNo = bookNo;
	}

	public Timestamp getStartDate() {
		return startDate;
	}

	public void setStartDate(Timestamp startDate) {
		this.startDate = startDate;
	}

	public Timestamp getReturnDue() {
		return returnDue;
	}

	public void setReturnDue(Timestamp returnDue) {
		this.returnDue = returnDue;
	}

	public Timestamp getReturnDate() {
		return returnDate;
	}

	public void setReturnDate(Timestamp returnDate) {
		this.returnDate = returnDate;
	}

	public int getReturnState() {
		return returnState;
	}

	public void setReturnState(int returnState) {
		this.returnState = returnState;
	}

	public BookVo getBook() {
		return book;
	}

	public void setBook(BookVo book) {
		this.book = book;
	}
}