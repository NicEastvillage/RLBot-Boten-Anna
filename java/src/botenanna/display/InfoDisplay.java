package botenanna.display;

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

/** Abstract InfoDisplay that ensures consistent layout. */
public abstract class InfoDisplay extends VBox {

    private static final Color DEFAULT_HEADER_COLOR = new Color(0.82, 0.82, 0.82, 1);

    protected HBox header;
    protected Label headerLabel;
    protected Label infoLabel;

    /** Setup the components for a InfoDisplay. If no color is provided a default gray is chosen. */
    protected InfoDisplay(String headerLabel, Color color) {
        super();

        buildHeader(headerLabel, color);

        infoLabel = new Label("No data");
        infoLabel.setPadding(new Insets(3, 3, 4, 10));
        infoLabel.setFont(new Font("Courier New", 12));
        getChildren().add(infoLabel);
    }

    /** Create the header of the InfoDisplay. Color can be null. */
    private void buildHeader(String label, Color color) {
        header = new HBox();
        getChildren().add(header);
        header.setPadding(new Insets(3, 5, 3, 5));

        if (color == null) color = DEFAULT_HEADER_COLOR;
        header.setBackground(new Background(new BackgroundFill(color, null, null)));

        headerLabel = new Label(label);
        header.getChildren().add(headerLabel);
    }
}
