package juuxel.resin;

import net.minecraft.util.Identifier;

public final class Resin {
	public static final String ID = "resin";

	public static Identifier id(String path) {
		return new Identifier(ID, path);
	}
}
