import java.io.File;
import javax.imageio.ImageIO;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.SnapshotParameters;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.embed.swing.SwingFXUtils;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;


public class s20ti082 extends Application {
    Canvas canvas      = new Canvas(640, 480);
    GraphicsContext gc = canvas.getGraphicsContext2D();
    Slider   sliderBrush = new Slider(1, 10, 2);
    Slider[] sliderColor = new Slider[3];

    @Override
    public void start(Stage stage) {
        MenuBar mBar = new MenuBar();
        Menu mFileMenu = new Menu("ファイル");
        MenuItem mFileOpen = new MenuItem("開く");
        mFileOpen.setOnAction(event -> fileLoad(stage));
        MenuItem mFileSave = new MenuItem("名前をつけて保存");
        mFileSave.setOnAction(event -> fileSave(stage));
        MenuItem mExit = new MenuItem("終了");
        mExit.setOnAction(event -> System.exit(0));
        mFileMenu.getItems().addAll(mFileOpen, mFileSave, mExit);
        mBar.getMenus().addAll(mFileMenu);

        Label cl = new Label();
        cl.setPrefWidth(250);cl.setPrefHeight(50);

        Button clear = new Button();
        clear.setPrefWidth(250);clear.setPrefHeight(50);
        clear.setText("Clear");
        clear.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent event){
                gc.setFill(Color.WHITE);
                gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
            

            }

        });
        VBox root = new VBox();
        root.getChildren().addAll(clear);
        
        

        

        Label lbBrush = new Label("   ブラシの太さ  ");
        sliderBrush.setPrefWidth(300);
        sliderBrush.setShowTickMarks(true);
        sliderBrush.setShowTickLabels(true);
        sliderBrush.setMajorTickUnit(1.0);

        HBox hBoxPaneTop = new HBox();
        hBoxPaneTop.getChildren().addAll(lbBrush, sliderBrush);
        VBox vBoxPaneTop = new VBox();
        vBoxPaneTop.getChildren().addAll(mBar,hBoxPaneTop);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        canvas.setOnMousePressed(event -> putPoints(event));
        canvas.setOnMouseDragged(event -> putPoints(event));

        Label lbColor = new Label("   ブラシの色  ");
        Label lbR = new Label("   R  ");
        Label lbG = new Label("   G  ");
        Label lbB = new Label("   B  ");
        for(int i=0; i<3; i++){
            sliderColor[i] = new Slider(0, 255, 0);
            sliderColor[i].setPrefWidth(300);
            sliderColor[i].setShowTickMarks(true);
            sliderColor[i].setShowTickLabels(true);
            sliderColor[i].setMajorTickUnit(50.0);
        }
        HBox hBoxPaneBottom0 = new HBox();
        hBoxPaneBottom0.getChildren().addAll(lbR, sliderColor[0]);
        HBox hBoxPaneBottom1 = new HBox();
        hBoxPaneBottom1.getChildren().addAll(lbG, sliderColor[1]);
        HBox hBoxPaneBottom2 = new HBox();
        hBoxPaneBottom2.getChildren().addAll(lbB, sliderColor[2]);
        VBox vBoxPaneBottom = new VBox();
        vBoxPaneBottom.getChildren().addAll(lbColor, hBoxPaneBottom0, hBoxPaneBottom1, hBoxPaneBottom2);

        BorderPane rootBorderPane = new BorderPane();
        rootBorderPane.setTop(vBoxPaneTop);
        rootBorderPane.setCenter(canvas);
        rootBorderPane.setBottom(vBoxPaneBottom);
        rootBorderPane.setLeft(clear);

        Scene scene = new Scene(rootBorderPane, 1000, 900);

        stage.setTitle("Paint");
        stage.setScene(scene);
        stage.show();
    }

    void putPoints(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            double x = event.getX();
            double y = event.getY();
            gc.setFill(Color.rgb((int)sliderColor[0].getValue(), (int)sliderColor[1].getValue(), (int)sliderColor[2].getValue()));
            gc.fillOval(x - sliderBrush.getValue(), y - sliderBrush.getValue(), sliderBrush.getValue()*2, sliderBrush.getValue()*2);
        }
    }
    
    void fileLoad(Stage stage) {
        FileChooser fchooser = new FileChooser();
        fchooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png"));
        fchooser.setInitialDirectory(new File("."));
        File file = fchooser.showOpenDialog(stage);
        if(file != null && file.isFile()) {
            Image img = new Image(file.getName());
            canvas.setWidth(img.getWidth());
            canvas.setHeight(img.getHeight());
            gc.drawImage(img, 0, 0);
        }
    }
    
    void fileSave(Stage stage) {
        FileChooser fchooser = new FileChooser();
        fchooser.getExtensionFilters().addAll(new ExtensionFilter("Image Files", "*.png"));
        fchooser.setInitialDirectory(new File("."));
        File file = fchooser.showSaveDialog(stage);
        WritableImage wimage = new WritableImage((int)canvas.getWidth(), (int)canvas.getHeight());
        SnapshotParameters params = new SnapshotParameters();
        if(file != null) {
            wimage = canvas.snapshot(params, null);
            try{
              saveImage(wimage, file.getName(), "png");
            }catch(Exception e){
              System.out.println(e);
            }
        }
    }

    private boolean saveImage(WritableImage img, String base, String fmt) throws Exception {
        File f = new File(base);
        return ImageIO.write(SwingFXUtils.fromFXImage(img, null), fmt, f);
    }

}
