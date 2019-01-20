package com.taulukko.ws.client;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WSBase {
	static AtomicBoolean loaded = new AtomicBoolean(false);
	static WSConfig wsconfig;
 
}
