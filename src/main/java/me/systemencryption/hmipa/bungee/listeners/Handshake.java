package me.systemencryption.hmipa.bungee.listeners;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.util.UUID;

import net.md_5.bungee.api.event.PlayerHandshakeEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import net.md_5.bungee.netty.ChannelWrapper;

public class Handshake implements Listener {

	@EventHandler(priority = EventPriority.LOWEST)
	public void onPlayerHandshake(PlayerHandshakeEvent event) {
		InitialHandler initialHandler = ((InitialHandler) event.getConnection());

		try {
			Field bungeeField = initialHandler.getClass().getDeclaredField("bungee");
			bungeeField.setAccessible(true);

			Object BungeeCord = bungeeField.get(initialHandler);

			Field connectionThrottleField = BungeeCord.getClass().getDeclaredField("connectionThrottle");
			connectionThrottleField.setAccessible(true);
			connectionThrottleField.set(BungeeCord, null);

			Field chField = initialHandler.getClass().getDeclaredField("ch");
			chField.setAccessible(true);

			ChannelWrapper channelWrapper = (ChannelWrapper) chField.get(initialHandler);

			Field remoteAddressField = channelWrapper.getClass().getDeclaredField("remoteAddress");
			remoteAddressField.setAccessible(true);

			InetSocketAddress InetSocketAddress = (java.net.InetSocketAddress) remoteAddressField.get(channelWrapper);

			Field holderField = InetSocketAddress.getClass().getDeclaredField("holder");
			holderField.setAccessible(true);

			Object InetSocketAddressHolder = holderField.get(InetSocketAddress);

			Field hostnameField = InetSocketAddressHolder.getClass().getDeclaredField("hostname");
			hostnameField.setAccessible(true);
			hostnameField.set(InetSocketAddressHolder, UUID.randomUUID().toString().replace("-", ""));

			Field addrField = InetSocketAddressHolder.getClass().getDeclaredField("addr");
			addrField.setAccessible(true);
			addrField.set(InetSocketAddressHolder, null);
		} catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
