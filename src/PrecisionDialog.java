package core;

import javafx.event.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;

// Dialog wrapper class that contains all the information and logic behind
// updating the precision of any Matrix calculation.

class PrecisionDialog
{
  private final int DEFAULT_PRECISION = 4;
  private final int MAX_PRECISION = 12;
  private int newPrecision;

  // Default constructor
  PrecisionDialog(Window owner, int currentPrecision)
  {
    // Initializing the Dialog centered on the parent.
    Stage dialog = new Stage();
    dialog.setTitle("Matrix Precision Update");
    dialog.initOwner(owner);
    dialog.initStyle(StageStyle.UTILITY);
    dialog.initModality(Modality.WINDOW_MODAL);
    dialog.setX(owner.getX() + owner.getWidth() / 3);
    dialog.setY(owner.getY() + owner.getHeight() / 3);

    final Text prompt = new Text("Enter your decired decimal Precision\n");
    prompt.setFont(Font.font("Arial", 20));

    final Text warning = new Text("--Note --\n" +
                                  "1. Values greater than 12\n" +
                                  "2. Negative values\n\n" +
                                  "Will not update the precision.");
    warning.setFont(Font.font("Arial", 15));

    final TextField input = new TextField();
    final Button update = new Button("Update");

    newPrecision = currentPrecision;
    update.setDefaultButton(true);

    // On activation the button will save and update the precision field.
    update.setOnAction(new EventHandler <ActionEvent>()
    {
      @Override public void handle(ActionEvent e)
      {
        int value = newPrecision;
        try
        {
          value = Integer.parseInt(input.getText());
        }
        catch(NumberFormatException z){;}

        if (value >= 0 && value <= MAX_PRECISION)
          newPrecision = value;

        dialog.close();
      }
    });

    input.setMinHeight(TextField.USE_PREF_SIZE);

    // Position everything onto the new window.
    final VBox layout = new VBox();
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().setAll(prompt, warning, input, update);

    dialog.setScene(new Scene(layout));
    dialog.showAndWait();
  }

  public int getPrecision()
  {
    return newPrecision;
  }
}
