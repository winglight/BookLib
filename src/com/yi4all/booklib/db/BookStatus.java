package com.yi4all.booklib.db;

public enum BookStatus {

	IN("在库"),
	OUT("借出");

	private final String displayName;

	BookStatus(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayName() {
		return displayName;
	}
}
