package io.pritam11638.mythofy.event;

import org.bukkit.event.Event;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public interface ListeningConsumer<T extends Event> extends Consumer<T>, Listener {}