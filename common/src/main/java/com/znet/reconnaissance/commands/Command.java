package com.znet.reconnaissance.commands;

public interface Command<T> {

	String getName();
	T getTarget();
	Class<T> getTargetType();
}
