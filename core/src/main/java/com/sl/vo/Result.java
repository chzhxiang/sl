package com.sl.vo;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by ZuoMJ on 2015/7/29.
 */
@SuppressWarnings("serial")
public class Result<T> implements Serializable {
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static final Result SUCCESS = new Result(Code.SUCCESS, null, null);

	@SuppressWarnings({ "rawtypes"})
	public static boolean isSuccessResult( Result r ){
		if( r == null ){
			return false;
		}
		return r.isSuccess();
	}
	
	public enum Code {
		SUCCESS, ERROR
	}

	private Code code = Code.SUCCESS;
	private String message;
	private T data;

	public Result() {
	}

	public Result(Code code, String message, T data) {
		this.code = code;
		this.message = message;
		this.data = data;
	}

	public Result(T data) {
		this.code = Code.SUCCESS;
		this.data = data;
	}

	@JsonIgnore
	public boolean isSuccess(){
		return Code.SUCCESS.equals(this.code);
	}
	
	public Code getCode() {
		return code;
	}

	public void setCode(Code code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	public void doErrorHandle(String message) {
		this.code = Code.ERROR;
		this.message = message;
	}
}
