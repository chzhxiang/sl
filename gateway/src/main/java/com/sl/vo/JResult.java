package com.sl.vo;

import java.io.Serializable;

/**
 * Created by wtf on 2016年3月11日17:05:33
 */
@SuppressWarnings("serial")
public class JResult<T> implements Serializable {

	private boolean status = true;
	private int errcode = 0;
	private String errmsg;
	private T data;

	public JResult(){}

	public JResult(T data){
		this.errmsg = "";
		this.data = data;
	}

	public JResult(boolean status ,int errcode ,String errmsg ,T data){
		this.status = status;
		this.errcode =errcode;
		this.errmsg = errmsg;
		this.data = data;
	}

	public static final JResult SUCCESS = new JResult(true,0,null, null);

	public static  JResult ERROR(String errmsg){
		return new JResult(false,1,errmsg, null);
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public int getErrcode() {
		return errcode;
	}

	private void setErrcode(int errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	private void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
