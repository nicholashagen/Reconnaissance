package com.znet.reconnaissance.commands;


public abstract class AnnotatedCommand<T> implements Command<T> {

	private T target;
	private Commandable commandable;
	
	public AnnotatedCommand() {
		this(null);
	}
	
	public AnnotatedCommand(T target) {
		this.target = target;
		this.commandable = this.getClass().getAnnotation(Commandable.class);
		if (this.commandable == null) {
			throw new IllegalArgumentException(
				"non commandable class: " + this.getClass()
			);
		}
	}
	
	@Override
	public String getName() {
		return this.commandable.name();
	}
	
	@Override
	public T getTarget() {
		return this.target;
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Class<T> getTargetType() {
		return (Class) this.commandable.targetType();
	}

}
