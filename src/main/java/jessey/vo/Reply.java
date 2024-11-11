package jessey.vo;

import java.sql.Timestamp;

public class Reply {
	
	private int replyId;
	private int replier;
	private int reviewId;
	private String comments;
	private Timestamp replyDate;
	
	private String replierNickname;
	
	public Reply() {
	};

	public Reply(int replyId, int replier, int reviewId, String comments, Timestamp replyDate) {
		this.replyId = replyId;
		this.replier = replier;
		this.reviewId = reviewId;
		this.comments = comments;
		this.replyDate = replyDate;
		}

	public int getReplyId() {
		return replyId;
	}

	public void setReplyId(int replyId) {
		this.replyId = replyId;
	}

	public int getReplier() {
		return replier;
	}

	public void setReplier(int replier) {
		this.replier = replier;
	}

	public int getReviewId() {
		return reviewId;
	}

	public void setReviewId(int reviewId) {
		this.reviewId = reviewId;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public Timestamp getReplyDate() {
		return replyDate;
	}

	public void setReplyDate(Timestamp replyDate) {
		this.replyDate = replyDate;
	}

	public String getReplierNickname() {
		return replierNickname;
	}

	public void setReplierNickname(String replierNickname) {
		this.replierNickname = replierNickname;
	}

}
