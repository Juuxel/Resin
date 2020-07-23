package juuxel.resin.api.widget;

import juuxel.resin.api.widget.config.GridPanelConfig;
import juuxel.resin.api.widget.data.Insets;

public class GridPanel extends Panel {
	public GridPanel(GridPanelConfig config) {
		super(WidgetType.GRID_PANEL, config.getPanelConfig());

		Insets insets = config.getPanelConfig().getInsets();
		int gridSize = config.getGridSize();

		for (GridPanelConfig.Child child : config.getChildren()) {
			Widget widget = child.getWidget().create();

			widget.setLocation(insets.getLeft() + gridSize * child.getX(), insets.getTop() + gridSize * child.getY());
			widget.setParent(this);
			children.add(widget);

			if (widget.canResize()) {
				int width = child.getWidth().orElse(1);
				int height = child.getHeight().orElse(1);

				widget.setSize(width * gridSize, height * gridSize);
			}

			expandToFit(widget);
		}
	}
}
