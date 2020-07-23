package juuxel.resin.api.widget.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import juuxel.resin.api.util.Codecs;
import juuxel.resin.api.widget.data.Binding;
import net.minecraft.text.Text;

public final class ButtonConfig implements WidgetConfig {
	private static final Runnable EMPTY_RUNNABLE = () -> {};
	private static final Binding<Runnable> DEFAULT_ON_CLICK = new Binding.Constant<>(EMPTY_RUNNABLE);

	public static final Codec<ButtonConfig> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			Codecs.TEXT.fieldOf("label").forGetter(ButtonConfig::getLabel),
			HorizontalAlignment.CODEC.optionalFieldOf("horizontal_alignment", HorizontalAlignment.LEFT).forGetter(ButtonConfig::getHorizontalAlignment),
			Binding.codec(Codec.unit(EMPTY_RUNNABLE)).optionalFieldOf("on_click", DEFAULT_ON_CLICK).forGetter(ButtonConfig::getOnClick),
			Codec.INT.optionalFieldOf("background_color", 0xFF_808080).forGetter(ButtonConfig::getBackgroundColor),
			Codec.INT.optionalFieldOf("foreground_color", 0xFF_FFFFFF).forGetter(ButtonConfig::getForegroundColor),
			Codec.BOOL.optionalFieldOf("label_shadow", true).forGetter(ButtonConfig::isLabelShadowed)
		).apply(instance, ButtonConfig::new)
	);

	private final Text label;
	private final HorizontalAlignment horizontalAlignment;
	private final Binding<Runnable> onClick;
	private final int backgroundColor;
	private final int foregroundColor;
	private final boolean labelShadowed;

	public ButtonConfig(Text label, HorizontalAlignment horizontalAlignment, Binding<Runnable> onClick, int backgroundColor, int foregroundColor, boolean labelShadowed) {
		this.label = label;
		this.horizontalAlignment = horizontalAlignment;
		this.onClick = onClick;
		this.backgroundColor = backgroundColor;
		this.foregroundColor = foregroundColor;
		this.labelShadowed = labelShadowed;
	}

	public Text getLabel() {
		return label;
	}

	public HorizontalAlignment getHorizontalAlignment() {
		return horizontalAlignment;
	}

	public Binding<Runnable> getOnClick() {
		return onClick;
	}

	public int getBackgroundColor() {
		return backgroundColor;
	}

	public int getForegroundColor() {
		return foregroundColor;
	}

	public boolean isLabelShadowed() {
		return labelShadowed;
	}
}
