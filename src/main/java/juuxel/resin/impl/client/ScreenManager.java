package juuxel.resin.impl.client;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.SyntaxError;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import io.github.cottonmc.jankson.JanksonOps;
import juuxel.resin.Resin;
import juuxel.resin.api.client.screen.ScreenDescription;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.SinglePreparationResourceReloadListener;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class ScreenManager extends SinglePreparationResourceReloadListener<Map<Identifier, JsonObject>> implements IdentifiableResourceReloadListener {
	public static final ScreenManager INSTANCE = new ScreenManager();

	private static final Logger LOGGER = LogManager.getLogger();
	private static final Identifier ID = Resin.id("screen_manager");
	private static final String PREFIX = "resin/screen";
	private static final String EXTENSION = ".json5";

	private ImmutableMap<Identifier, ScreenDescription> screens;

	public static ScreenDescription get(Identifier id) {
		// TODO: Default
		return INSTANCE.screens.getOrDefault(id, null);
	}

	private ScreenManager() {
	}

	@Override
	public Identifier getFabricId() {
		return ID;
	}

	@Override
	protected Map<Identifier, JsonObject> prepare(ResourceManager manager, Profiler profiler) {
		Jankson jankson = Jankson.builder().build();
		ImmutableMap.Builder<Identifier, JsonObject> builder = ImmutableMap.builder();

		for (Identifier resourceId : manager.findResources(PREFIX, it -> it.endsWith(EXTENSION))) {
			String path = resourceId.getPath();
			Identifier id = new Identifier(resourceId.getNamespace(), path.substring(PREFIX.length() + 1, path.length() - EXTENSION.length()));

			try (Resource resource = manager.getResource(resourceId);
				 InputStream in = resource.getInputStream()) {
				builder.put(id, jankson.load(in));
			} catch (IOException | SyntaxError e) {
				LOGGER.error("Could not load screen description from {}", resourceId, e);
			}
		}

		return builder.build();
	}

	@Override
	protected void apply(Map<Identifier, JsonObject> loader, ResourceManager manager, Profiler profiler) {
		ImmutableMap.Builder<Identifier, ScreenDescription> builder = ImmutableMap.builder();

		for (Map.Entry<Identifier, JsonObject> entry : loader.entrySet()) {
			Identifier id = entry.getKey();
			Either<ScreenDescription, DataResult.PartialResult<ScreenDescription>> result =
				ScreenDescription.CODEC.decode(JanksonOps.INSTANCE, entry.getValue())
					.map(Pair::getFirst)
					.get();

			result.ifLeft(description -> builder.put(id, description));
			result.ifRight(partial -> LOGGER.error("Could not load screen {}: {}", id, partial.message()));
		}

		screens = builder.build();
	}
}
