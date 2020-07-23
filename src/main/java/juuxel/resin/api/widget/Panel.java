package juuxel.resin.api.widget;

import juuxel.resin.api.widget.config.PanelConfig;
import juuxel.resin.api.widget.data.Bindings;
import juuxel.resin.api.widget.data.Insets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Panel extends Widget {
	protected final List<Widget> children = new ArrayList<>();
	private final PanelConfig config;

	public Panel(WidgetType<?> type, PanelConfig config) {
		super(type);
		this.config = config;
	}

	public Insets getInsets() {
		return config.getInsets();
	}

	public void expandToFit(Widget widget) {
		int width = Math.max(getWidth(), widget.getX() + widget.getWidth() + getInsets().getRight());
		int height = Math.max(getHeight(), widget.getY() + widget.getHeight() + getInsets().getBottom());
		setSize(width, height);
	}

	@Override
	public Widget hit(int x, int y) {
		for (int i = children.size() - 1; i >= 0; i--) {
			Widget child = children.get(i);
			int cx = x - child.getX();
			int cy = y - child.getY();

			if (child.isWithinBounds(cx, cy)) {
				return child.hit(cx, cy);
			}
		}

		return this;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		config.getBackgroundPainter().paintBackground(x, y, this);

		for (Widget child : children) {
			child.paint(matrices, x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
		}
	}

	@Override
	public void setBindings(Bindings bindings) {
		super.setBindings(bindings);
		for (Widget child : children) {
			child.setBindings(bindings);
		}
	}
}
