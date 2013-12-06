package com.znet.reconnaissance.server.notifications;

import com.znet.reconnaissance.commands.AnnotatedCommand;
import com.znet.reconnaissance.commands.Commandable;

@Commandable(name=NotificationCommand.NAME)
public class NotificationCommand extends AnnotatedCommand<Notification> {

	public static final String NAME = "notify";
	
	public NotificationCommand(Notification notification) {
		super(notification);
	}
}
