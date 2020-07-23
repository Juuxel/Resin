package juuxel.resin.api.widget;

import juuxel.resin.api.widget.config.PanelConfig;
import juuxel.resin.api.widget.data.Insets;
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

	@Override
	public boolean canResize() {
		return true;
	}

	public Insets getInsets() {
		return config.getInsets();
	}

	public void expandToFit(Widget widget) {
		int width = Math.max(getWidth(), widget.getX() + widget.getWidth());
		int height = Math.max(getHeight(), widget.getY() + widget.getHeight());
		System.out.println("With " + widget + " from " + getWidth() + ", " + getHeight() + " to " + width + ", " + height);
		setSize(width, height);
	}

	@Override
	public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
		config.getBackgroundPainter().paintBackground(x, y, this);

		for (int i = children.size() - 1; i >= 0; i--) {
			Widget child = children.get(i);
			child.paint(matrices, x + child.getX(), y + child.getY(), mouseX - child.getX(), mouseY - child.getY());
		}
	}
}
