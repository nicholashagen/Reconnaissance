package com.znet.reconnaissance.commands;

import com.znet.reconnaissance.model.Registration;

@Commandable(name=RegisterCommand.NAME)
public class RegisterCommand extends AnnotatedCommand<Registration> {

	public static final String NAME = "register";
	
	public RegisterCommand(Registration registration) {
		super(registration);
	}
}
