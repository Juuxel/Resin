package juuxel.resin.api.widget.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import juuxel.resin.api.ResinRegistries;
import juuxel.resin.api.widget.data.Insets;

public final class PanelConfig implements WidgetConfig {
	private static final Insets DEFAULT_INSETS = new Insets(8, 8, 8, 8);

	public static final Codec<PanelConfig> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			ResinRegistries.BACKGROUND_PAINTER.optionalFieldOf("background_painter", BackgroundPainter.VANILLA).forGetter(PanelConfig::getBackgroundPainter),
			Insets.CODEC.optionalFieldOf("insets", DEFAULT_INSETS).forGetter(PanelConfig::getInsets)
		).apply(instance, PanelConfig::new)
	);

	private final BackgroundPainter backgroundPainter;
	private final Insets insets;

	public PanelConfig(BackgroundPainter backgroundPainter, Insets insets) {
		this.backgroundPainter = backgroundPainter;
		this.insets = insets;
	}

	public BackgroundPainter getBackgroundPainter() {
		return backgroundPainter;
	}

	public Insets getInsets() {
		return insets;
	}
}
