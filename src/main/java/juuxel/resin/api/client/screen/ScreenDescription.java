package juuxel.resin.api.client.screen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import juuxel.resin.api.widget.WidgetTemplate;

public final class ScreenDescription {
	public static final Codec<ScreenDescription> CODEC = RecordCodecBuilder.create(
		instance -> instance.group(
			WidgetTemplate.CODEC.fieldOf("root").forGetter(ScreenDescription::getRoot),
			Title.CODEC.optionalFieldOf("title", Title.DEFAULT).forGetter(ScreenDescription::getTitle)
		).apply(instance, ScreenDescription::new)
	);

	private final WidgetTemplate<?> root;
	private final Title title;

	public ScreenDescription(WidgetTemplate<?> root, Title title) {
		this.root = root;
		this.title = title;
	}

	public WidgetTemplate<?> getRoot() {
		return root;
	}

	public Title getTitle() {
		return title;
	}

	public static final class Title {
		static final Codec<Title> CODEC = RecordCodecBuilder.create(
			instance -> instance.group(
				Codec.BOOL.optionalFieldOf("visible", true).forGetter(Title::isVisible),
				Codec.INT.optionalFieldOf("x", 0).forGetter(Title::getX),
				Codec.INT.optionalFieldOf("y", 0).forGetter(Title::getY),
				HorizontalAlignment.CODEC.optionalFieldOf("alignment", HorizontalAlignment.LEFT).forGetter(Title::getAlignment)
			).apply(instance, Title::new)
		);
		static final Title DEFAULT = new Title();

		private final boolean visible;
		private final int x;
		private final int y;
		private final HorizontalAlignment alignment;

		private Title() {
			this(true, 0, 0, HorizontalAlignment.LEFT);
		}

		public Title(boolean visible, int x, int y, HorizontalAlignment alignment) {
			this.visible = visible;
			this.x = x;
			this.y = y;
			this.alignment = alignment;
		}

		public boolean isVisible() {
			return visible;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}

		public HorizontalAlignment getAlignment() {
			return alignment;
		}
	}
}
