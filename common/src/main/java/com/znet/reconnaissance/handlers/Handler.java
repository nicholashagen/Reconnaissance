package com.znet.reconnaissance.handlers;

import com.znet.reconnaissance.model.Client;

public interface Handler<T, R> {

	R process(Client<?> client, T value);
}
