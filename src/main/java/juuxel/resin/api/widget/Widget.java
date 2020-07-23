package juuxel.resin.api.widget;

import juuxel.resin.api.widget.data.Bindings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.Nullable;

public abstract class Widget {
	private final WidgetType<?> type;

	private int x, y;
	private int width = 18, height = 18;
	@Nullable private Panel parent;
	private Bindings bindings;

	public Widget(WidgetType<?> type) {
		this.type = type;
	}

	public WidgetType<?> getType() {
		return type;
	}

	public boolean canResize() {
		return true;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
	}

	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Nullable
	public final Panel getParent() {
		return parent;
	}

	public final void setParent(@Nullable Panel parent) {
		this.parent = parent;
	}

	public int getAbsoluteX() {
		return parent != null ? x + parent.getAbsoluteX() : x;
	}

	public int getAbsoluteY() {
		return parent != null ? y + parent.getAbsoluteY() : y;
	}

	public Bindings getBindings() {
		return bindings;
	}

	public void setBindings(Bindings bindings) {
		this.bindings = bindings;
	}

	public boolean isWithinBounds(int x, int y) {
		return x >= 0 && y >= 0 && x <= getWidth() && y <= getHeight();
	}

	public Widget hit(int x, int y) {
		return this;
	}

	@Environment(EnvType.CLIENT)
	public abstract void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY);

	@Environment(EnvType.CLIENT)
	public void onMouseDown(int mouseX, int mouseY, int button) {
	}

	@Environment(EnvType.CLIENT)
	public void onMouseUp(int mouseX, int mouseY, int button) {
	}

	@Environment(EnvType.CLIENT)
	public void onClick(int mouseX, int mouseY, int button) {
	}
}
