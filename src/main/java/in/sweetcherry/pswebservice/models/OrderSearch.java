package in.sweetcherry.pswebservice.models;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

public class OrderSearch {
	
	private String orderState;
	private long orderStateId;
	private long employeeId;
	private long profileId;
	
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date fromDate;
	
	@DateTimeFormat(pattern="dd-MM-yyyy")
	private Date toDate;
	
	private String paymentMode;
	private String handoverStatus;
	
	public String getOrderState() {
		return orderState;
	}
	public void setOrderState(String orderState) {
		this.orderState = orderState;
	}
	public long getOrderStateId() {
		return orderStateId;
	}
	public void setOrderStateId(long orderStateId) {
		this.orderStateId = orderStateId;
	}
	public long getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	public String getPaymentMode() {
		return paymentMode;
	}
	public void setPaymentMode(String paymentMode) {
		this.paymentMode = paymentMode;
	}
	public String getHandoverStatus() {
		return handoverStatus;
	}
	public void setHandoverStatus(String handoverStatus) {
		this.handoverStatus = handoverStatus;
	}
	public long getProfileId() {
		return profileId;
	}
	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}
	

}
