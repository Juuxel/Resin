package juuxel.resin.api.widget;

import com.mojang.serialization.Codec;
import juuxel.resin.Resin;
import juuxel.resin.api.ResinRegistries;
import juuxel.resin.api.widget.config.ButtonConfig;
import juuxel.resin.api.widget.config.GridPanelConfig;
import juuxel.resin.api.widget.config.LabelConfig;
import juuxel.resin.api.widget.config.WidgetConfig;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.ApiStatus;

import java.util.function.Function;

public interface WidgetType<WC extends WidgetConfig> {
	WidgetType<LabelConfig> LABEL = of(LabelConfig.CODEC, Label::new);
	WidgetType<GridPanelConfig> GRID_PANEL = of(GridPanelConfig.CODEC, GridPanel::new);
	WidgetType<ButtonConfig> BUTTON = of(ButtonConfig.CODEC, Button::new);

	Widget create(WC config);
	Codec<WC> configCodec();

	static <WC extends WidgetConfig> WidgetType<WC> of(Codec<WC> configCodec, Function<? super WC, ? extends Widget> factory) {
		return new WidgetType<WC>() {
			@Override
			public Widget create(WC config) {
				return factory.apply(config);
			}

			@Override
			public Codec<WC> configCodec() {
				return configCodec;
			}
		};
	}

	@ApiStatus.Internal
	static void init() {
		Registry.register(ResinRegistries.WIDGET, Resin.id("label"), LABEL);
		Registry.register(ResinRegistries.WIDGET, Resin.id("grid_panel"), GRID_PANEL);
		Registry.register(ResinRegistries.WIDGET, Resin.id("button"), BUTTON);
	}
}
