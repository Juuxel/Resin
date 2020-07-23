package juuxel.resin.api.widget.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import juuxel.resin.api.util.Codecs;
import juuxel.resin.api.widget.WidgetTemplate;

import java.util.List;
import java.util.Optional;

public final class GridPanelConfig implements WidgetConfig {
	private static final int DEFAULT_GRID_SIZE = 18;

	public static final Codec<GridPanelConfig> CODEC = Codecs.merge(
		PanelConfig.CODEC,
		GridPanelConfig::getPanelConfig,
		panelConfig -> RecordCodecBuilder.create(
			instance -> instance.group(
				RecordCodecBuilder.point(panelConfig),
				Child.CODEC.listOf().fieldOf("children").forGetter(GridPanelConfig::getChildren),
				Codec.INT.optionalFieldOf("grid_size", DEFAULT_GRID_SIZE).forGetter(GridPanelConfig::getGridSize)
			).apply(instance, GridPanelConfig::new)
		)
	);

	private final PanelConfig panelConfig;
	private final List<Child> children;
	private final int gridSize;

	public GridPanelConfig(PanelConfig panelConfig, List<Child> children, int gridSize) {
		this.panelConfig = panelConfig;
		this.children = children;
		this.gridSize = gridSize;
	}

	public PanelConfig getPanelConfig() {
		return panelConfig;
	}

	public List<Child> getChildren() {
		return children;
	}

	public int getGridSize() {
		return gridSize;
	}

	@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
	public static final class Child {
		static final Codec<Child> CODEC = Codecs.merge(
			WidgetTemplate.CODEC,
			Child::getWidget,
			widget -> RecordCodecBuilder.create(
				instance -> instance.group(
					RecordCodecBuilder.point(widget),
					Codec.INT.fieldOf("x").forGetter(Child::getX),
					Codec.INT.fieldOf("y").forGetter(Child::getY),
					Codec.INT.optionalFieldOf("width").forGetter(Child::getWidth),
					Codec.INT.optionalFieldOf("height").forGetter(Child::getHeight)
				).apply(instance, Child::new)
			)
		);

		private final WidgetTemplate<?> widget;
		private final int x, y;
		private final Optional<Integer> width, height;

		public Child(WidgetTemplate<?> widget, int x, int y, Optional<Integer> width, Optional<Integer> height) {
			this.widget = widget;
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}

		public WidgetTemplate<?> getWidget() {
			return widget;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public Optional<Integer> getWidth() {
			return width;
		}

		public Optional<Integer> getHeight() {
			return height;
		}
	}
}
