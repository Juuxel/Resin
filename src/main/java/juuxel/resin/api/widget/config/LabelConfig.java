package juuxel.resin.api.widget.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import juuxel.resin.api.util.Codecs;
import juuxel.resin.api.widget.Label;
import net.minecraft.text.Text;

public final class LabelConfig implements WidgetConfig {
	public static final Codec<LabelConfig> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codecs.TEXT.fieldOf("text").forGetter(LabelConfig::getText),
			HorizontalAlignment.CODEC.optionalFieldOf("horizontal_alignment", HorizontalAlignment.LEFT).forGetter(LabelConfig::getHorizontalAlignment),
			Codec.INT.optionalFieldOf("color", Label.DEFAULT_TEXT_COLOR).forGetter(LabelConfig::getColor)
		).apply(instance, LabelConfig::new)
	);

	private final Text text;
	private final HorizontalAlignment horizontalAlignment;
	private final int color;

	public LabelConfig(Text text, HorizontalAlignment horizontalAlignment, int color) {
		this.text = text;
		this.horizontalAlignment = horizontalAlignment;
		this.color = color;
	}

	public Text getText() {
		return text;
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public int getColor() {
		return color;
	}
}
