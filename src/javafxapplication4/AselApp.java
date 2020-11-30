package javafxapplication4;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class AselApp extends Application {

    private static ResourceBundle rbNew;
    Background defaultBg = new Background(new BackgroundFill(Color.web("black", 0.75), null, null));
    String textFill = "-fx-text-fill: white";
    String[] languages = {"ru", "en", "zh", "es", "ja"};
    String[] countries = {"RU", "US", "HK", "ES", "JP"};

    @Override
    public void start(Stage stage) throws Exception {
        Locale.setDefault(new Locale("ru", "RU"));
        ResourceBundle rb = ResourceBundle.getBundle("translate", new Locale("ru", "RU"));
        createFirstScene(stage, rb, false);
    }

    public static class MenuItem extends StackPane {

        MenuItem(String name, Runnable action) {
            LinearGradient gradient = new LinearGradient(
                    0, 0.5, 1, 0.5, true, CycleMethod.NO_CYCLE,
                    new Stop(0.1, Color.web("black", 0.75)),
                    new Stop(1.0, Color.web("black", 0.15))
            );
            Rectangle bg0 = new Rectangle(250, 40, gradient);

            Rectangle line = new Rectangle(5, 40);
            line.widthProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(8).otherwise(5)
            );
            line.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.RED).otherwise(Color.GRAY)
            );

            Text text = new Text(name);
            text.setFont(Font.font(19.0));
            text.fillProperty().bind(
                    Bindings.when(hoverProperty())
                            .then(Color.WHITE).otherwise(Color.GRAY)
            );

            setOnMouseClicked(e -> action.run());
            setOnMousePressed(e -> bg0.setFill(Color.LIGHTBLUE));

            setOnMouseReleased(e -> bg0.setFill(gradient));

            setAlignment(Pos.CENTER_LEFT);

            HBox box = new HBox(15, line, text);
            box.setAlignment(Pos.CENTER_LEFT);
            getChildren().addAll(bg0, box);
        }

    }

    public ResourceBundle chooseLang(Stage stage, ChoiceBox chooseLg) throws UnsupportedEncodingException {
        boolean choiceVis = true;
        chooseLg.getSelectionModel().selectedIndexProperty().addListener(
                ((observable, oldValue, newValue) -> {
                    String currentL = languages[newValue.intValue()];
                    String currentC = countries[newValue.intValue()];
                    chooseLg.setValue(currentL);
                    rbNew = ResourceBundle.getBundle("translate", new Locale(currentL, currentC));
                    try {
                        createFirstScene(stage, rbNew, choiceVis);

                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(AselApp.class.getName()).log(Level.SEVERE, null, ex);
                    }
                })
        );
        return rbNew;
    }

    public void createFirstScene(Stage stage, ResourceBundle rb, boolean choiceVis) throws UnsupportedEncodingException {
        Pane root1 = new Pane();
        Scene scene1 = new Scene(root1, 720, 405);
        Image bgImage = new Image(getClass().getResourceAsStream("world.jpg"), 720, 405, false, true);
        ChoiceBox chooseLg = new ChoiceBox();
        chooseLg.setBackground(new Background(new BackgroundFill(Color.web("white", 0.7), null, null)));
        //chooseLg.setBackground(new Background(new BackgroundFill(Color.web("black", 0.7), null, null)));
        chooseLg.setItems(FXCollections.observableArrayList(
                "Русский", "English", "\u4e2d\u6587", "\u0045\u0073\u0070\u0061\u00f1\u006f\u006c", "\u65e5\u672c\u8a9e"
        ));
        chooseLg.setTranslateX(50);
        chooseLg.setTranslateY(50);
        chooseLg.setVisible(choiceVis);
        MenuItem btnSelectLang = new MenuItem(getStr(rb.getString("chooseLang"), rb.getString("utf16")), () -> chooseLg.setVisible(true));
        MenuItem btnShowCongr = new MenuItem(getStr(rb.getString("showCongr"), rb.getString("utf16")), () -> {
            try {
                createSecondScene(stage, scene1, rb);
            } catch (UnsupportedEncodingException ex) {
                
            }
        });
        MenuItem btnExit = new MenuItem(getStr(rb.getString("exit"), rb.getString("utf16")), () -> Platform.exit());
        VBox box = new VBox(5, btnSelectLang, btnShowCongr, btnExit);
        box.setBackground(new Background(
                new BackgroundFill(Color.web("black", 0.6), null, null)
        ));
        box.setTranslateX(720 - 400);
        box.setTranslateY(450 - 300);
        root1.getChildren().addAll(
                new ImageView(bgImage), box, chooseLg
        );
        
        
        stage.setResizable(false);
        stage.getIcons().add(new Image(AselApp.class.getResourceAsStream("icon.png"))); 

        stage.setTitle("My International App");
        stage.setScene(scene1);
        stage.setResizable(false);
        stage.show();
        chooseLang(stage, chooseLg);

    }

    public void createSecondScene(Stage stage, Scene scene1, ResourceBundle rb) throws UnsupportedEncodingException {
        Image bgImage = new Image(getClass().getResourceAsStream("walpaper_"
                + rb.getLocale().getLanguage() + ".jpg"), 720, 405, false, true);
        GridPane table = new GridPane();
        Pane root2 = new Pane();
        Scene scene2 = new Scene(root2, 720, 405);
        Label labelSurname = new Label(getStr(rbNew.getString("surname"), rbNew.getString("utf16")));
        labelSurname.setBackground(defaultBg);
        labelSurname.setStyle(textFill);
        Label labelName = new Label(getStr(rbNew.getString("name"), rbNew.getString("utf16")));
        labelName.setBackground(defaultBg);
        labelName.setStyle(textFill);
        Label labelPatronymic = new Label(getStr(rbNew.getString("patronymic"), rbNew.getString("utf16")));
        labelPatronymic.setBackground(defaultBg);
        labelPatronymic.setStyle(textFill);
        Label labelAge = new Label(getStr(rbNew.getString("age"), rbNew.getString("utf16")));
        labelAge.setBackground(defaultBg);
        labelAge.setStyle(textFill);
        Label labelGender = new Label(getStr(rbNew.getString("gender"), rbNew.getString("utf16")));
        labelGender.setBackground(defaultBg);
        labelGender.setStyle(textFill);
        TextField fieldSurname = new TextField();
        TextField fieldName = new TextField();
        TextField fieldPatronymic = new TextField();
        TextField fieldAge = new TextField();

        RadioButton rbMan = new RadioButton(getStr(rbNew.getString("male"), rbNew.getString("utf16")));
        rbMan.setBackground(defaultBg);
        rbMan.setStyle(textFill);
        RadioButton rbWoman = new RadioButton(getStr(rbNew.getString("female"), rbNew.getString("utf16")));
        rbWoman.setBackground(defaultBg);
        rbWoman.setStyle(textFill);
        ToggleGroup genderGroup = new ToggleGroup();
        rbMan.setUserData("man");
        rbWoman.setUserData("woman");
        rbMan.setToggleGroup(genderGroup);
        rbWoman.setToggleGroup(genderGroup);
        table.setHgap(20);
        table.setVgap(10);
        table.addColumn(1, labelSurname, labelName, labelPatronymic, labelAge, labelGender);
        table.addColumn(2, fieldSurname, fieldName, fieldPatronymic, fieldAge, rbMan, rbWoman);
        table.setTranslateX(20);
        table.setTranslateY(20);

        Button btnCreateCongr = new Button();btnCreateCongr.setBackground(defaultBg); btnCreateCongr.setStyle(textFill);
        btnCreateCongr.setText(
                getStr(rbNew.getString("createCongr"), rbNew.getString("utf16"))
        );
        Button btnTo1from2 = new Button(); btnTo1from2.setBackground(defaultBg); btnTo1from2.setStyle(textFill);
        btnTo1from2.setText(
                getStr(rbNew.getString("back"), rbNew.getString("utf16"))
        );
        HBox btns2 = new HBox(10, btnTo1from2, btnCreateCongr);
        btns2.setTranslateX(100);
        btns2.setTranslateY(300);
        btnTo1from2.setOnAction((ActionEvent event) -> {
            stage.setScene(scene1);
        });

        btnCreateCongr.setOnAction((ActionEvent event) -> {
            try {
                String userSurname = fieldSurname.getText();
                String userName = fieldName.getText();
                String userPatronymic = fieldPatronymic.getText();
                String userAge = fieldAge.getText();
                genderGroup.getSelectedToggle();
                RadioButton selectionGender = (RadioButton) genderGroup.getSelectedToggle();
                String userGender = selectionGender.getText();
                createThirdScene(stage, scene2, rbNew, userSurname, userName, userPatronymic, userAge, userGender, bgImage);
            } catch (UnsupportedEncodingException e) {
            }
        });

        stage.setScene(scene2);
        root2.getChildren().addAll(new ImageView(bgImage), table, btns2);

    }

    public void createThirdScene(Stage stage, Scene scene2, ResourceBundle rb,
            String userSurname, String userName, String userPatronymic, String userAge, 
            String userGender, Image bgImage) throws UnsupportedEncodingException {
        String dear;if(userGender.equals("Man")) dear = "dearM";else dear = "dearW";
        Pane root3 = new Pane();
        Scene scene3 = new Scene(root3, 720, 405);
        
        
        Label congrDear = new Label();congrDear.setBackground(defaultBg); congrDear.setStyle(textFill);
        congrDear.setFont(Font.font(25));
        congrDear.setText(
        (getStr(rbNew.getString(dear), rbNew.getString("utf16")))+" "+
                userSurname+" "+userName+" "+userPatronymic+"."
        );
        Label congrPart1 = new Label(); congrPart1.setBackground(defaultBg); congrPart1.setStyle(textFill);
        congrPart1.setFont(Font.font(25));
        congrPart1.setText(getStr(rbNew.getString("congr1"), rbNew.getString("utf16")));
        
        Label congrPart2 = new Label(); congrPart2.setBackground(defaultBg); congrPart2.setStyle(textFill);
        congrPart2.setFont(Font.font(25));
        congrPart2.setText(getStr(rbNew.getString("congr2"), rbNew.getString("utf16")));
        
        if (rb.getLocale().getLanguage().equals("ru")) {congrDear.setFont(Font.font(20)); congrPart1.setFont(Font.font(20));congrPart2.setFont(Font.font(20));
        } 
        VBox congr = new VBox(5, congrDear, congrPart1, congrPart2);
        congr.setPrefSize(620, 300);
        congr.setTranslateX(50);
        congr.setTranslateY(50);
        stage.setScene(scene3);
        
        
        Button btnTo2from3 = new Button();
        btnTo2from3.setBackground(defaultBg); btnTo2from3.setStyle(textFill);
        btnTo2from3.setText(
                getStr(rbNew.getString("back"), rbNew.getString("utf16"))
        );
        Button exit = new Button();
        exit.setBackground(defaultBg); exit.setStyle(textFill);
        exit.setText(getStr(rb.getString("exit"), rb.getString("utf16")));
        
        HBox btns3 = new HBox(70, btnTo2from3, exit);
        btns3.setTranslateX(70);
        btns3.setTranslateY(300);
        btnTo2from3.setOnAction((ActionEvent event) -> {
            stage.setScene(scene2);
        });
        exit.setOnAction((ActionEvent event) -> {
            Platform.exit();
        });

        root3.getChildren().addAll(new ImageView(bgImage), congr, btns3);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static String getStr(String text, String utf16) throws UnsupportedEncodingException {
        if (!utf16.equals("true")) {
            return new String(text.getBytes("ISO-8859-1"), "Windows-1251");
        } else {
            return text;
        }
    }

}
