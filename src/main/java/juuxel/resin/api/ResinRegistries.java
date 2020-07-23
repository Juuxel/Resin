package juuxel.resin.api;

import com.mojang.serialization.Lifecycle;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import juuxel.resin.Resin;
import juuxel.resin.api.widget.WidgetType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import org.jetbrains.annotations.ApiStatus;

public final class ResinRegistries {
	public static final Registry<WidgetType<?>> WIDGET = create("widget");
	public static final Registry<BackgroundPainter> BACKGROUND_PAINTER = create("background_painter");

	@SuppressWarnings({"rawtypes", "unchecked"})
	private static <T> Registry<T> create(String id) {
		Identifier identifier = Resin.id(id);
		return Registry.register((Registry) Registry.REGISTRIES, identifier, new SimpleRegistry<>(RegistryKey.ofRegistry(identifier), Lifecycle.stable()));
	}

	@ApiStatus.Internal
	public static void init() {
	}
}
