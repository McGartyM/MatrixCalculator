package core;

import javafx.event.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.geometry.*;

// This class is a simple Dialog wrapper, with a purpose of abstracting all
// the instructions into a single *dialog*.
public class TextDialog
{
  // Standard constructor
  TextDialog(Window owner)
  {
    Stage dialog = new Stage();
    dialog.setTitle("Instructions: ");

    dialog.initOwner(owner);
    dialog.initStyle(StageStyle.UTILITY);
    dialog.initModality(Modality.WINDOW_MODAL);

    dialog.setX(owner.getX() + owner.getWidth() / 3);
    dialog.setY(owner.getY() + owner.getHeight() / 3);


    String message = "1. On the right side of the program is a drop down with possible operations.\n" +
                     "--- Choose an operation, then input the dimensions of your matricies in the top.\n" +
                     "--- A grid of entries will appear in the bottom left, where you can enter data.\n" +
                     "--- If the operation requires more than one Matrix, a second pane will be available.\n\n" +
                     "2. Input your entries then click compute." +
                     "--- The results will be displayed in an externam window.\n" +
                     "3. The save button will copy the result back into the grid.\n\n"+
                     "--Note -- If you wish to update the decimal precision of the calculation, go to the settings tab.\n" +
                     "---There will be a tab titled, \"Precision\", where you can enter a new precision.";

    final Text header = new Text("Thank you for choosing to use my Matrix Calculator!\n");
    header.setTextAlignment(TextAlignment.CENTER);

    final Text prompt = new Text(message);
    prompt.setFont(Font.font("Arial", 20));

    // Creating the close button
    final Button close = new Button("OK");
    close.setOnAction(new EventHandler <ActionEvent> ()
    {
      @Override public void handle(ActionEvent t)
      {
        dialog.close();
      }
    });

    close.setPrefWidth(50);
    close.setPrefHeight(20);

    VBox layout = new VBox();
    layout.setAlignment(Pos.CENTER);
    layout.getChildren().addAll(header, prompt, close);

    dialog.setScene(new Scene(layout));
    dialog.setResizable(false);
    dialog.showAndWait();
  }
}
