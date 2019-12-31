package core;


import core.Matrix;
import core.Operations;
import core.ResultDialog;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.*;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;

import java.util.ArrayList;
import java.io.FileInputStream;
import java.util.HashMap;

// Primary Application class for the calculator.
// Currntly it supports matricies up to 12 x 12, I may upgrade the size later on.

public class GUI extends Application
{
  // Start dimensions of the primary Window and input GridPane.
  private double width = 800;
  private double height = 600;
  private double gridHeight = 560;
  private double gridWidth = 510;

  // Reference to the user-input grid.
  private GridPane grid;

  // Storing the two states of possible inputs.
  private GridPane oneMatrix;
  private GridPane twoMatrix;
  private VBox twoMatrixLayout;
  private boolean isDouble = false;

  // Parameters for the primary Matrix.
  private int rows = 0;
  private int cols = 0;
  private int precision = 4;
  private double [][] input = null;
  private String operationType = null;
  private String operation = null;
  private Double parameter = null;

  // Parameters for the secondary Matrix
  private int altRows;
  private int altCols;
  private double [][] altInput;

  // Arbitrary choice to allow for 12 x 12 matricies.
  private final int MAXROWS = 12;
  private final int MAXCOLS = 12;

  // Given a component, set a scaling parameter for it.
  private void setResize(Node component, float scaleX, float scaleY)
  {
    component.setScaleX(scaleX);
    component.setScaleY(scaleY);
  }

  // Given a component, resize it to its preferred size.
  private void resize(Region component)
  {
    // System.out.println(component);
    component.setPrefWidth(component.getScaleX() * width);
    component.setPrefHeight(component.getScaleY() * height);
  }

  // All of the components in the Application will be in the components ArrayList.
  // This method just loops over the components and resizes them.
  // -- Currntly resizing of nested components is not supported. --
  private void resize(ArrayList <Region> components)
  {
    for (int i = 0; i < components.size(); i++)
      resize(components.get(i));
  }

  // Initializes the applications menu bar.
  private VBox menuInit(Window owner)
  {
    Menu file = new Menu("File");
    MenuItem close = new MenuItem("Exit");
    close.setOnAction(new EventHandler <ActionEvent>()
    {
      @Override public void handle(ActionEvent e)
      {
        Platform.exit(); // Terminates application
      }
    });

    Menu settings = new Menu("Settings");
    MenuItem precisionItem = new MenuItem("Precision");
    precisionItem.setOnAction(new EventHandler <ActionEvent>()
    {
      // Update the precision of computations
      @Override public void handle(ActionEvent t)
      {
        PrecisionDialog dialog = new PrecisionDialog(owner, precision);
        precision = dialog.getPrecision();
      }
    });

    Menu help = new Menu("Help");
    MenuItem usage = new MenuItem("Instructions");
    usage.setOnAction(new EventHandler <ActionEvent> ()
    {
      // Opens a dialog with the instructions.
      @Override public void handle(ActionEvent t)
      {
        TextDialog instructions = new TextDialog(owner);
      }
    });

    file.getItems().add(close);
    settings.getItems().add(precisionItem);
    help.getItems().add(usage);

    MenuBar menubar = new MenuBar();
    menubar.getMenus().addAll(file, settings, help);

    return new VBox(menubar);
  }

  // Initializes the +- buttons for the row and column inputs.
  private VBox [] dimButtons(TextField rowField, TextField colField) throws Exception
  {
    // Backgrounds of the images need to be removed
    FileInputStream p = new FileInputStream("../resources/plus.jpg");
    Image plusImg = new Image(p, 10, 15, true, true);

    FileInputStream m = new FileInputStream("../resources/minus.png");
    Image minusImg = new Image(m, 10, 15, true, true);

    Button rowPlus = new Button();
    rowPlus.setGraphic(new ImageView(plusImg));
    rowPlus.setPrefWidth(10);
    rowPlus.setPrefHeight(15);

    Button rowMinus = new Button();
    rowMinus.setGraphic(new ImageView(minusImg));
    rowMinus.setPrefWidth(10);
    rowMinus.setPrefHeight(15);

    Button colPlus = new Button();
    colPlus.setGraphic(new ImageView(plusImg));
    colPlus.setPrefWidth(10);
    colPlus.setPrefHeight(15);

    Button colMinus = new Button();
    colMinus.setGraphic(new ImageView(minusImg));
    colMinus.setPrefWidth(10);
    colMinus.setPrefHeight(15);

    // Setting actions for the buttons.
    // The plus buttons will increment the row, minus will decrement.
    rowPlus.setOnAction(new EventHandler <ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        rows++;
        if (rows <= MAXROWS && rows >= 0)
          rowField.setText(Integer.toString(rows));
      }
    });

    rowMinus.setOnAction(new EventHandler <ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        rows--;
        if (rows <= MAXROWS && rows >= 0)
          rowField.setText(Integer.toString(rows));
      }
    });

    colPlus.setOnAction(new EventHandler <ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        cols++;
        if (cols <= MAXCOLS && cols >= 0)
          colField.setText(Integer.toString(cols));
      }
    });

    colMinus.setOnAction(new EventHandler <ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        cols--;
        if (cols <= MAXCOLS && cols >= 0)
          colField.setText(Integer.toString(cols));
      }
    });

    VBox [] adjustments = new VBox[2];
    adjustments[0] = new VBox();
    adjustments[1] = new VBox();

    adjustments[0].setAlignment(Pos.CENTER);
    adjustments[1].setAlignment(Pos.CENTER);

    adjustments[0].getChildren().addAll(rowPlus, rowMinus);
    adjustments[1].getChildren().addAll(colPlus, colMinus);

    return adjustments;
  }

  // Initialize the uppermost pane of the Application.
  // This pane includes the row and column input fields.
  private FlowPane topPaneInit(GridPane inputPane) throws Exception
  {
    FlowPane pane = new FlowPane();
    pane.setAlignment(Pos.CENTER);
    pane.setHgap(5);

    // Creating the Text labels for the inputs.
    Text rowLabel = new Text();
    rowLabel.setText("Rows:");
    rowLabel.setFont(new Font(18));

    Text colLabel = new Text();
    colLabel.setText("Columns:");
    colLabel.setFont(new Font(18));

    // Creating the input fields
    TextField rowInput = new TextField();
    rowInput.setAlignment(Pos.CENTER_RIGHT);
    rowInput.setPrefWidth(80);

    TextField colInput = new TextField();
    colInput.setAlignment(Pos.CENTER_RIGHT);
    colInput.setPrefWidth(80);

    VBox [] adjusts = dimButtons(rowInput, colInput);

    // ChangeListener tracks changes in the dimensions and updates the
    // text field and Matrix parameters.
    ChangeListener<String> dimensionListener = (observable, oldValue, newValue) ->
    {
      try
      {
        int newRows = Integer.parseInt(rowInput.getText());
        if (newRows <= MAXROWS)
          rows = newRows;
      }
      catch (NumberFormatException e)
      {
        rows = 0;
      }

      try
      {
        int newCols = Integer.parseInt(colInput.getText());
        if (newCols <= MAXCOLS)
          cols = newCols;
      }
      catch (NumberFormatException e)
      {
        cols = 0;
      }

      // Update the dimensions of the input grid.
      dynamicGrid();
    };

    colInput.textProperty().addListener(dimensionListener);
    rowInput.textProperty().addListener(dimensionListener);

    FileInputStream img = new FileInputStream("../resources/matrix.jpg");
    Image matrixImg = new Image(img, 100, 100, true, false);

    Region space0 = new Region();
    space0.setPrefWidth(20);
    Region space1 = new Region();
    space1.setPrefWidth(20);

    pane.getChildren().addAll(new ImageView (matrixImg), space0, rowLabel, rowInput,
                                             adjusts[0], space1, colLabel, colInput,
                                             adjusts[1]);
    return pane;
  }

  // Utility method to retrieve the Matrix from the input GridPane.
  private double [][] retrieveMatricies(GridPane pane, double [][] userInput,
                                        int numRows, int numCols, Alert noInput)
  {
    userInput = new double [numRows][numCols];
    for (int i = 0, index = 0; i < numRows; i++)
    {
      for (int j = 0; j < numCols; j++, index++)
      {
        try
        {
          TextField text = (TextField) pane.getChildren().get(index);
          if (text.getText().equals(""))
          {
            noInput.showAndWait();
            return null;
          }

          // Will catch any non-numeric values
          userInput[i][j] = Double.parseDouble(text.getText());
        }
        catch (NumberFormatException e)
        {
          // Throw an alert saying the input is invalid.
          userInput = null;
          Alert invalidData= new Alert(AlertType.ERROR);
          invalidData.setContentText("Non-numeric value entered into Matrix.\n" +
                                     "Please enter numbers if you wish to compute something.\n");
          invalidData.showAndWait();
          return null;
        }
      }
    }

    return userInput;
  }

  // Initialzie the top-right pane with the solve button.
  private HBox solveInit(GridPane inputPane, Window rootWin) throws Exception
  {
    Button solve = new Button();
    solve.setText("Compute!");
    solve.setStyle("-fx-font-size:16");

    solve.setPrefHeight(45);
    solve.setPrefWidth(120);

    // Retrieve the Matrix and do the calculation.
    solve.setOnAction(new EventHandler <ActionEvent> ()
    {
      @Override
      public void handle(ActionEvent t)
      {

        // No need to compute if theres nothing.
        Alert noInput = new Alert(AlertType.ERROR);
        noInput.setContentText("No data has been Entered.\nEnter some data and try again.");

        Alert noOperation = new Alert(AlertType.ERROR);
        noOperation.setContentText("No Operation has been chosen.\nPlease choose an Operation.");

        if (rows <= 0 || cols <= 0 )
        {
          noInput.showAndWait();
          input = null;
          return;
        }

        if (operation == null)
        {
          noOperation.showAndWait();
          input = null;
          return;
        }

        input = retrieveMatricies(inputPane, input, rows, cols, noInput);
        if (input == null)
          return;

        if (isDouble)
        {
          HBox parent = (HBox) twoMatrixLayout.getChildren().get(0);
          GridPane altGrid = (GridPane) parent.getChildren().get(1);
          altInput = retrieveMatricies(altGrid, altInput, altRows, altCols, noInput);
          if (altInput == null)
          return;

        }

        compute(rootWin);
      }
    });

    HBox pane = new HBox();
    pane.setAlignment(Pos.CENTER);
    pane.getChildren().add(solve);

    return pane;
  }

  // Initializes the four families of operations the user can choose.
  private HBox [] choicesInit()
  {
    final ListView <String> arithmetic = new ListView<>();
    arithmetic.getItems().addAll("Scale", "Add", "Subtract", "Multiply", "Exponentiate");

    final ListView <String> properties = new ListView<>();
    properties.getItems().addAll("Trace", "Rank", "Linear Dependence", "Inconsistent",
                                 "Determinant", "EigenValues", "EigenVectors");

    final ListView <String> standard = new ListView<>();
    standard.getItems().addAll("Transpose", "Append", "Normalize", "RREF",
                               "Invert", "Cofactor", "Adjugate",  "Orthogonalize" );

    final ListView <String> decompositions = new ListView<>();
    decompositions.getItems().addAll("QR", "LU", "Rank", "Diagonal");

    ChangeListener <String> event = new ChangeListener <String>()
    {
      @Override
      public void changed(ObservableValue<? extends String> observable, String old, String newVal)
      {
        operation = newVal;

        // Determine if the input Pane needs to switch modes.
        if (operation.equals("Add") || operation.equals("Subtract"))
          isDouble = true;
        else if (operation.equals("Multiply"))
          isDouble = true;
        else if (operation.equals("Append"))
          isDouble = true;
        else
          isDouble = false;

        // Determine what mode the input Pane is currently on.
        // These operations and string comparisons could be improved.
        boolean currentlyDouble = false;
        if (old != null)
          if (old.equals("Add") || old.equals("Subtract") || old.equals("Multiply") || old.equals("Append"))
            currentlyDouble = true;

        if (isDouble && currentlyDouble)
          return;

        // Switch out the layout with the two Matrix version.
        if (isDouble)
        {
          grid.getChildren().remove(4);
          grid.add(twoMatrixLayout, 0, 1);
          dynamicGrid();
        }
        else
        {
          // Remove and repaint the single matrix (resizes from the double pane).
          grid.getChildren().remove(4);
          grid.setPrefWidth(Integer.MAX_VALUE);
          grid.setPrefHeight(Integer.MAX_VALUE);
          grid.add(oneMatrix, 0, 1);
        }
      }
    };

    arithmetic.getSelectionModel().selectedItemProperty().addListener(event);
    properties.getSelectionModel().selectedItemProperty().addListener(event);
    standard.getSelectionModel().selectedItemProperty().addListener(event);
    decompositions.getSelectionModel().selectedItemProperty().addListener(event);

    TextField pInput = new TextField();
    pInput.setPromptText("Enter Param for Scale Exponent");
    pInput.textProperty().addListener((observable, oldValue, newValue) ->
    {
      try
      {
        parameter = Double.parseDouble(pInput.getText());
      }
      catch(NumberFormatException e){;}
    });

    // Using a VBox as a wrapper for list alignment
    VBox zero = new VBox(10);
    zero.getChildren().addAll(pInput, arithmetic);

    HBox first = new HBox();
    first.getChildren().add(zero);

    HBox second = new HBox();
    second.getChildren().add(properties);

    HBox third = new HBox();
    third.getChildren().add(standard);

    HBox fourth = new HBox();
    fourth.getChildren().add(decompositions);

    HBox [] panes = new HBox[4];
    panes[0] = first;
    panes[1] = second;
    panes[2] = third;
    panes[3] = fourth;

    return panes;
  }

  // Initializes the Operations choice "menu".
  private BorderPane operationsInit()
  {
    final ComboBox <String> options = new ComboBox <>();
    options.getItems().setAll("Arithmetic", "Properties",
                              "Standard Operations", "Decompositions");

    BorderPane pane = new BorderPane();

    // Set alignment for the ComboBox.
    pane.setAlignment(options, Pos.CENTER);
    pane.setMargin(options, new Insets(15, 0, 15, 0));
    pane.setTop(options);

    HBox [] choices = choicesInit();

    // Event Handler to swap the panes when the user wants different properties.
    EventHandler <ActionEvent> event = new EventHandler <ActionEvent>()
    {
      public void handle(ActionEvent e)
      {
        operationType = options.getValue();
        if (operationType == null)
          return;

        // Show only the relevant operations for each type of operation.
        if (operationType.equals("Arithmetic"))
          pane.setCenter(choices[0]);
        else if (operationType.equals("Properties"))
          pane.setCenter(choices[1]);
        else if (operationType.equals("Standard Operations"))
          pane.setCenter(choices[2]);
        else if (operationType.equals("Decompositions"))
          pane.setCenter(choices[3]);
      }
    };

    options.setOnAction(event);
    return pane;
  }

  // Dynamically updates the number of matrix entries whenever the rows and columns change
  private void dynamicGrid()
  {
    oneMatrix.getChildren().clear();

    double fieldHeight = (oneMatrix.getHeight() / rows);
    double fieldWidth = (oneMatrix.getWidth() / cols);

    for (int i = 0; i < rows; i++)
    {
      for (int j = 0; j < cols; j++)
      {
        TextField newField = new TextField();

        newField.setPrefHeight(fieldHeight);
        newField.setPrefWidth(fieldWidth);
        newField.setAlignment(Pos.CENTER);

        oneMatrix.add(newField, j, i);
      }
    }

    // Add the second Matrix input if necessary
    if (isDouble)
    {
      if (operation.equals("Add") || operation.equals("Subtract"))
      {
        altRows = rows;
        altCols = cols;
      }
      if (operation.equals("Multiply"))
      {
        altRows = cols;
      }
      if (operation.equals("Append"))
      {
        altRows = rows;
      }

      HBox parent = (HBox) twoMatrixLayout.getChildren().get(0);
      parent.getChildren().clear();
      twoMatrix.getChildren().clear();

      // Create the second Matrix input.
      for (int i = 0; i < altRows; i++)
      {
        for (int j = 0; j < altCols; j++)
        {
          TextField newField = new TextField();

          newField.setPrefHeight(fieldHeight);
          newField.setPrefWidth(fieldWidth);
          newField.setAlignment(Pos.CENTER);

          twoMatrix.add(newField, j, i);
        }
      }

      oneMatrix.setPrefWidth(250);
      oneMatrix.setPrefHeight(250);

      parent.getChildren().add(oneMatrix);
      parent.getChildren().add(twoMatrix);
    }
  }

  // Initialize the dynamic user-input pane.
  private void inputInit()
  {
    oneMatrix = new GridPane();
    oneMatrix.setPrefHeight(560);
    oneMatrix.setPrefWidth(510);

    twoMatrix = new GridPane();
    twoMatrix.setGridLinesVisible(true);
    twoMatrix.setPrefHeight(250);
    twoMatrix.setPrefWidth(250);

    TextField newCols = new TextField();
    newCols.setAlignment(Pos.CENTER_RIGHT);
    newCols.setMaxHeight(50);
    newCols.setMaxWidth(120);
    newCols.setPromptText("Column parameter:");

    // Updates grid based on user-inputs to the rows and columns.
    newCols.textProperty().addListener((observable, oldValue, newValue) ->
    {
      try
      {
        if (operation.equals("Multiply") || operation.equals("Append"))
        {
          int newInput = Integer.parseInt(newCols.getText());
          if (newInput > 0 && newInput <= 20)
          {
            altCols = newInput;
            dynamicGrid();
          }
        }

      }
      catch(NumberFormatException e)
      {
        parameter = null;
      }
    });

    HBox inputBox = new HBox(15);
    inputBox.setAlignment(Pos.CENTER);
    inputBox.getChildren().addAll(oneMatrix, twoMatrix);

    twoMatrixLayout = new VBox(15);
    twoMatrixLayout.setMargin(inputBox, new Insets (15, 15, 15, 15));
    twoMatrixLayout.setAlignment(Pos.CENTER);
    twoMatrixLayout.getChildren().addAll(inputBox, newCols);

    dynamicGrid();
  }

  // Driver function for computation decision tree.
  // The purpose is to reduce the number of string comparisons to reach a desired operation.
  private void compute(Window rootWin)
  {
    Matrix m = new Matrix(input);
    m.setPrecision(precision);
    Matrix n = null;

    try
    {
      if (operationType.equals("Arithmetic"))
        arithmetic(m, n, rootWin);
      else if (operationType.equals("Properties"))
        properties(m, rootWin);
      else if (operationType.equals("Standard Operations"))
        operations(m, n, rootWin);
      else if (operationType.equals("Decompositions"))
        decompositions(m, rootWin);
    }
    catch (Exception e)
    {
      System.out.println("Error has occured in compute().");
      e.printStackTrace();
    }
  }

  // Decision tree for the arithmetic operations.
  // Essentially a massive if-else tree comparing Strings for correct operations.
  private void arithmetic(Matrix m, Matrix n, Window rootWin) throws Exception
  {
    if (isDouble)
      n = new Matrix(altInput);

    Matrix result = null;
    String [] labels = null;

    switch (operation)
    {
      case "Scale":
      {
        if (parameter == null)
        {
          Alert invalidData= new Alert(AlertType.ERROR);
          invalidData.setContentText("Non-numeric value entered into parameter field.\n" +
                                     "Please enter a numeric value if you wish to continue.\n");
          invalidData.showAndWait();
          return;
        }

        result = Operations.scale(m, parameter);
        break;
      }

      case "Add":
      {
        result = Operations.add(m, n);
        break;
      }

      case "Subtract":
      {
        result = Operations.subtract(m, n);
        break;
      }

      case "Multiply":
      {
        result = Operations.multiply(m, n);
        break;
      }

      case "Exponentiate":
      {
        if (parameter == null)
        {
          Alert invalidData= new Alert(AlertType.ERROR);
          invalidData.setContentText("Non-numeric value entered into parameter field.\n" +
                                     "Please enter a numeric value if you wish to continue.\n");
         invalidData.showAndWait();
         return;
        }

        result = Operations.power(m, parameter.intValue());
        break;
      }
    }

    ResultDialog show = new ResultDialog(rootWin, new Matrix[]{result}, new String []{null}, m.format());
    return;
  }

  // Decision tree for property operations.
  private void properties(Matrix m, Window rootWin) throws Exception
  {
    double value;
    boolean is;
    String result = null;
    switch (operation)
    {
      case "Trace":
      {
        result = String.format(m.format(), Operations.trace(m));
        break;
      }

      case "Rank":
      {
        result = Integer.toString(Operations.rank(m));
        break;
      }

      case "Linear Dependence":
      {
        result = Boolean.toString(Operations.isLinDep(m));
        break;
      }

      case "Inconsistent":
      {
        result = Boolean.toString(Operations.isInconsistent(m));
        break;
      }

      case "Determinant":
      {
        result = String.format(m.format(), Operations.determinant(m));
        break;
      }

      case "EigenValues":
      {
        double [] eigenvalues = Operations.eigenvalues(m);
        String [] labels = new String [eigenvalues.length];

        for (int i = 0; i < labels.length; i++)
          labels[i] = String.format(m.format(), eigenvalues[i]);

        ResultDialog results = new ResultDialog(rootWin, null, labels, m.format());
        return;
      }

      case "EigvenVectors":
      {
        Vector [] eigenvectors = Operations.eigenvectors(m);
        Matrix eigenMatrix = new Matrix(eigenvectors);

        if (!eigenMatrix.isValid())
        {
          Alert errorOccurred= new Alert(AlertType.ERROR);
          errorOccurred.setContentText("An error has occured in calculating the EigenVectors.\n" +
                                       "This method is still a WIP, sorry for the inconvenience.");
          errorOccurred.showAndWait();
        }

        ResultDialog results = new ResultDialog(rootWin, new Matrix[]{eigenMatrix}, new String []{"Eigenvectors"}, m.format());
        return;
      }
    }

    ResultDialog results = new ResultDialog(rootWin, null, new String []{result}, m.format());
  }

  // Decision tree for standard operations.
  private void operations(Matrix m, Matrix n, Window rootWin) throws Exception
  {
    if (isDouble)
      n = new Matrix(altInput);

    Matrix result = null;
    switch (operation)
    {
      case "Transpose":
      {
        result = Operations.transpose(m);
        break;
      }

      case "Append":
      {
        result = Operations.append(m, n);
        break;
      }

      case "Normalize":
      {
        result = Operations.normalize(m);
        break;
      }

      case "RREF":
      {
        result = Operations.rref(m, true);
        break;
      }

      case "Invert":
      {
        result = Operations.invert(m);
        break;
      }

      case "Cofactor":
      {
        result = Operations.cofactor(m);
        break;
      }

      case "Adjugate":
      {
        result = Operations.adjugate(m);
        break;
      }

      case "Orthogonalize":
      {
        result = Operations.orthogonalize(m);
        break;
      }

    }

    if (result == null)
      System.out.println("error with " + operation);
    else
      result.print();

    ResultDialog results = new ResultDialog(rootWin, new Matrix[]{result}, new String []{null}, m.format());
  }

  // Decision tree for decomposition operations.
  private void decompositions(Matrix m, Window rootWin) throws Exception
  {
    Matrix [] results = null;
    String [] labels = null;
    switch (operation)
    {
      case "QR":
      {
        results = Operations.qr(m);
        labels = new String []{"Q:", "R:"};
        break;
      }

      case "LU":
      {
        results = Operations.lu(m);
        labels = new String [] {"P:", "L: ", "U:"};
        break;
      }

      case "Rank":
      {
        results = Operations.rankDecomp(m);
        labels = new String [] {"C: ", "R:"};
        break;
      }

      case "Diagonal":
      {
        results = new Matrix []{Operations.diagonalize(m)};
        labels = new String []{"A:", "D:", "A^-1:"};
        break;
      }
    }

    if (results == null)
    {
      System.out.printf("Error happened with %s", operation);
      return;
    }

    try
    {
      ResultDialog show = new ResultDialog(rootWin, results, labels, m.format());
    }
    catch(Exception e)
    {
      e.printStackTrace();
      System.out.println("Error Occurred");
    }
  }

  // Returns the GridPane dividing the primarystage into four quadrants.
  private GridPane rootInit()
  {
    GridPane root = new GridPane();
    root.setAlignment(Pos.CENTER);
    root.setGridLinesVisible(true);

    // Divide root pane into four quadrants.
    ColumnConstraints c1 = new ColumnConstraints();
    c1.setPercentWidth(70);
    ColumnConstraints c2 = new ColumnConstraints();
    c2.setPercentWidth(30);

    RowConstraints r1 = new RowConstraints();
    r1.setPercentHeight(15);
    RowConstraints r2 = new RowConstraints();
    r2.setPercentHeight(85);

    root.getColumnConstraints().addAll(c1, c2);
    root.getRowConstraints().addAll(r1, r2);

    return root;
  }

  @Override
  public void start(Stage window) throws Exception
  {
    // Set Window defaults
    window.setMinWidth(width);
    window.setMinHeight(height);

    window.setMaxWidth(1280);
    window.setMaxHeight(720);

    // Store all components.
    ArrayList <Region> components = new ArrayList<>();

    // Initializing the grid pane, and attaching the top panel to it.
    grid = rootInit();
    inputInit();

    final Window rootWin = window;
    grid.add(topPaneInit(oneMatrix), 0, 0);
    grid.add(solveInit(oneMatrix, rootWin), 1, 0);
    grid.add(operationsInit(), 1, 1);
    grid.add(oneMatrix, 0, 1);


    grid.setMargin(grid.getChildren().get(4), new Insets(15, 15, 15, 15));

    // The title and grid pane are wrapped in a Border Pane.
    BorderPane background = new BorderPane();
    background.setTop(menuInit(window));
    background.setCenter(grid);

    window.initStyle(StageStyle.DECORATED);
    window.setTitle("Matrix Calculator");
    window.setScene(new Scene (background, width, height));
    window.show();

    ChangeListener<Number> windowSizeListener = (observable, oldValue, newValue) ->
    {
      this.width = window.getWidth();
      this.height = window.getHeight();

      resize(components);
    };

    window.heightProperty().addListener(windowSizeListener);
    window.widthProperty().addListener(windowSizeListener);
  }

  public static void main (String [] args)
  {
    launch();
  }
}
