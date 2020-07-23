package juuxel.resin.api.widget;

import com.mojang.serialization.Codec;
import juuxel.resin.api.ResinRegistries;
import juuxel.resin.api.util.Codecs;
import juuxel.resin.api.widget.config.WidgetConfig;

public final class WidgetTemplate<WC extends WidgetConfig> {
	@SuppressWarnings({"unchecked", "rawtypes"})
	public static final Codec<WidgetTemplate<?>> CODEC = Codecs.flatDispatch(ResinRegistries.WIDGET, WidgetTemplate::getType, type -> {
		Codec<WidgetConfig> configCodec = (Codec<WidgetConfig>) type.configCodec();
		return configCodec.xmap(config -> new WidgetTemplate(type, config), WidgetTemplate::getConfig);
	});

	private final WidgetType<WC> type;
	private final WC config;

	public WidgetTemplate(WidgetType<WC> type, WC config) {
		this.type = type;
		this.config = config;
	}

	public WidgetType<WC> getType() {
		return type;
	}

	public WC getConfig() {
		return config;
	}

	public Widget create() {
		return type.create(config);
	}
}
