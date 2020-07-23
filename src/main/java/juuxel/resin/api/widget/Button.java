package juuxel.resin.api.widget;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import juuxel.resin.api.widget.config.ButtonConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

public class Button extends Widget {
	private final ButtonConfig config;

	public Button(ButtonConfig config) {
		super(WidgetType.BUTTON);
		this.config = config;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		ScreenDrawing.coloredRect(x, y, getWidth(), getHeight(), isWithinBounds(mouseX, mouseY) ? 0xFF_FFFFFF : 0xFF_000000);
		ScreenDrawing.coloredRect(x + 1, y + 1, getWidth() - 2, getHeight() - 2, config.getBackgroundColor());

		TextRenderer font = MinecraftClient.getInstance().textRenderer;
		int ty = y + (getHeight() - font.fontHeight) / 2;

		if (config.isLabelShadowed()) {
			ScreenDrawing.drawStringWithShadow(matrices, config.getLabel(), config.getHorizontalAlignment(), x + 2, ty, getWidth() - 2 * 2, config.getForegroundColor());
		} else {
			ScreenDrawing.drawString(matrices, config.getLabel(), config.getHorizontalAlignment(), x + 2, ty, getWidth() - 2 * 2, config.getForegroundColor());
		}
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void onClick(int mouseX, int mouseY, int button) {
		MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
		config.getOnClick().get(getBindings()).run();
	}
}
