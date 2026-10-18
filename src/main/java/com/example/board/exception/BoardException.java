package com.example.board.exception;

import com.example.board.enums.ErrorCode;
import lombok.Getter;

@Getter
public class BoardException extends RuntimeException {

	private final ErrorCode errorCode;

	public BoardException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

}
