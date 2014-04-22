package com.zmax.app.chat.promelo;

import java.util.EventListener;

/**
 * Listener of broadcast message.
 * 
 */
public interface DataListener extends EventListener {
	void receiveData(DataEvent event);
}
