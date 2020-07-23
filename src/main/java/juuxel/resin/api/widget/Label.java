package juuxel.resin.api.widget;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import juuxel.resin.api.widget.config.LabelConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

public class Label extends Widget {
	/**
	 * The default text color for light mode labels.
	 */
	public static final int DEFAULT_TEXT_COLOR = 0x404040;

	/**
	 * The default text color for dark mode labels.
	 */
	public static final int DEFAULT_DARKMODE_TEXT_COLOR = 0xbcbcbc;

	private final LabelConfig config;

	public Label(LabelConfig config) {
		super(WidgetType.LABEL);
		this.config = config;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		ScreenDrawing.drawString(matrices, config.getText(), config.getHorizontalAlignment(), x, y, getWidth(), config.getColor());
	}
}
