package io.github.cottonmc.cotton.gui.widget.data;

import com.mojang.serialization.Codec;
import net.minecraft.util.StringIdentifiable;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public enum HorizontalAlignment implements StringIdentifiable {
	LEFT("left"),
	CENTER("center"),
	RIGHT("right");

	private static final Map<String, HorizontalAlignment> VALUES = Stream.of(values()).collect(Collectors.toMap(HorizontalAlignment::asString, x -> x));
	public static final Codec<HorizontalAlignment> CODEC = Codec.STRING.xmap(VALUES::get, HorizontalAlignment::asString);
	private final String id;

	HorizontalAlignment(String id) {
		this.id = id;
	}

	@Override
	public String asString() {
		return id;
	}
}
