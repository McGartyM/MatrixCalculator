package core;

import core.Matrix;

import javafx.event.*;
import javafx.geometry.*;
import javafx.stage.*;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.scene.image.*;
import javafx.scene.control.*;

import java.io.FileInputStream;

// Custom dialog class which displays the results of any Matrix operation.
// Whenever the user clicks the compute button, this class will create
// and show the results.

public class ResultDialog
{

  // Standard constructor
  ResultDialog(Window owner, Matrix [] args, String [] labels, String format) throws Exception
  {
    // Create a new window centered on the parent.
    Stage dialog = new Stage();
    dialog.setTitle("Results: ");

    dialog.initOwner(owner);
    dialog.initStyle(StageStyle.UTILITY);
    dialog.initModality(Modality.WINDOW_MODAL);

    dialog.setX(owner.getX() + owner.getWidth() / 3);
    dialog.setY(owner.getY() + owner.getHeight() / 3);

    HBox rootPane = new HBox(15);
    rootPane.setAlignment(Pos.CENTER);

    // Allows the dialog be used for non-Matrix results.
    if (args != null)
    {
      for (int i = 0; i < args.length; i++)
        rootPane.getChildren().add(initResult(args[i], labels[i], format));
    }
    else
    {
      Label result = new Label(labels[0]);
      result.setFont(new Font("Arial", 24));
      rootPane.getChildren().add(result);
      rootPane.setMinSize(150, 60);
      rootPane.setMaxSize(150, 60);
    }

    dialog.setScene(new Scene(rootPane));
    dialog.showAndWait();
  }

  // The Matrix results will be stored in a GridPane where each index
  // represents the same index in the Matrix. Assumes the Matrix is valid
  // and has results, or else it wouldn't have been called.
  private GridPane initGrid(Matrix m, String format)
  {
    GridPane textGrid = new GridPane();
    textGrid.setAlignment(Pos.CENTER);

    // Setting up grid *resizability*
    textGrid.setPrefSize(300, 300);
    double rowPercent = 100.0 / m.cols();
    double colPercent = 100.0 / m.rows();

    // Initialize Row Constraints
    for (int i = 0; i < m.rows(); i++)
    {
      RowConstraints r = new RowConstraints();
      r.setPercentHeight(rowPercent);
      r.setFillHeight(true);
      textGrid.getRowConstraints().add(r);
    }

    // Initialize column constraints
    for (int i = 0; i < m.cols(); i++)
    {
      ColumnConstraints c = new ColumnConstraints();
      c.setPercentWidth(colPercent);
      c.setFillWidth(true);
      textGrid.getColumnConstraints().add(c);
    }

    // Placing the Matrix entries into the GridPane as Text Objects.
    double [][] entries = m.entries();
    for (int i = 0; i < m.rows(); i++)
    {
      for (int j = 0; j < m.cols(); j++)
      {
        Text index = new Text(String.format(format, entries[i][j]));
        textGrid.add(index, j, i);
        textGrid.setHalignment(index, HPos.CENTER);
      }
    }

    return textGrid;
  }

  // Driver function which essentially creates the contents of the dialog.
  // Assumes the Matrix is valid.
  private BorderPane initResult(Matrix m, String name, String format) throws Exception
  {
    BorderPane border = new BorderPane();
    GridPane textGrid = initGrid(m, format);

    Label label = new Label(name);

    // Optional use of Matrix labels with name parameter.
    if (name != null)
    {
      border.setAlignment(label, Pos.CENTER);
      border.setMargin(label, new Insets(15, 0, 0, 0));
      border.setTop(label);
    }

    border.setCenter(textGrid);
    border.setMargin(textGrid, new Insets(15, 0, 15, 0));

    // FileInputStream lb = new FileInputStream("../resources/Left_Bracket.png");
    // ImageView lbView = new ImageView(new Image(lb, 70, 500, false, false));
    //
    // FileInputStream rb = new FileInputStream("../resources/Right_Bracket.png");
    // ImageView rbView = new ImageView(new Image(rb, 70, 500, false, false));

    // border.setLeft(lbView);
    // border.setRight(rbView);

    border.setMinSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
    border.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

    return border;
  }
}
