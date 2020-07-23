package juuxel.resin.api.client.screen;

import io.github.cottonmc.cotton.gui.client.Scissors;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import juuxel.resin.api.widget.Label;
import juuxel.resin.api.widget.Panel;
import juuxel.resin.api.widget.Widget;
import juuxel.resin.impl.client.ScreenManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ResinScreen extends Screen {
	private final Panel root;
	private final ScreenDescription description;
	private int x, y;
	private int titleX, titleY;
	private int width;

	public ResinScreen(Text text, Identifier screenId) {
		this(text, ScreenManager.get(screenId));
	}

	public ResinScreen(Text text, ScreenDescription description) {
		super(text);
		this.description = description;
		Widget root = description.getRoot().create();

		if (root instanceof Panel) {
			this.root = (Panel) root;
		} else {
			throw new IllegalArgumentException("Root " + root + " is not a panel!");
		}
	}

	@Override
	public void init(MinecraftClient client, int width, int height) {
		super.init(client, width, height);
		x = (width - root.getWidth()) / 2;
		y = (height - root.getHeight()) / 2;
		titleX = x + root.getInsets().getLeft() + description.getTitle().getX();
		titleY = y + root.getInsets().getTop() + description.getTitle().getY();
		this.width = root.getWidth() - root.getInsets().getLeft() - root.getInsets().getRight();
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		Scissors.refreshScissors();
		root.paint(matrices, x, y, mouseX - x, mouseY - y);
		Scissors.checkStackIsEmpty();

		ScreenDescription.Title title = description.getTitle();
		if (title.isVisible()) {
			ScreenDrawing.drawString(matrices, this.title, title.getAlignment(), titleX, titleY, width, Label.DEFAULT_TEXT_COLOR);
		}
	}
}
