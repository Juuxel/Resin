package juuxel.resin.impl;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.NinePatch;
import juuxel.resin.Resin;
import juuxel.resin.api.ResinRegistries;
import juuxel.resin.api.client.screen.ResinScreen;
import juuxel.resin.api.widget.WidgetType;
import juuxel.resin.impl.client.ScreenManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourceType;
import net.minecraft.server.command.CommandManager;
import net.minecraft.text.LiteralText;

public final class ResinInit implements ModInitializer, ClientModInitializer {
	@Override
	public void onInitialize() {
		ResinRegistries.init();
		WidgetType.init();
		BackgroundPainter.init();

		CommandRegistrationCallback.EVENT.register((commandDispatcher, dedicated) -> {
			if (!dedicated) {
				commandDispatcher.register(CommandManager.literal("resin").executes(context -> {
					MinecraftClient.getInstance().execute(() -> {
						MinecraftClient.getInstance().openScreen(new ResinScreen(new LiteralText("Resin Test"), Resin.id("test")));
					});
					return 0;
				}));
			}
		});
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onInitializeClient() {
		ResourceManagerHelper resources = ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES);
		resources.registerReloadListener(NinePatch.MetadataLoader.INSTANCE);
		resources.registerReloadListener(ScreenManager.INSTANCE);
	}
}
