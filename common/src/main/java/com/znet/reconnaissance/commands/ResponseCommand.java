package com.znet.reconnaissance.commands;

@Commandable(name=ResponseCommand.NAME)
public class ResponseCommand extends AnnotatedCommand<Object> {

	public static final String NAME = "response";
	
	private Object value;
	
	public ResponseCommand(Object value) {
		this.value = value;
	}

	@Override
	public Object getTarget() {
		return value;
	}
}
