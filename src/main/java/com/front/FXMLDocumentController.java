package com.front;
import java.io.IOException;
import com.types.DtAccumulator;
import com.types.DtCollector;
import com.types.DtExchanger;
import com.logic.Accumulator;
import com.logic.Pump;
import com.logic.Collector;
import com.logic.PumpController;
import com.logic.TempInCollectorController;
import com.logic.FinnedTubeController;
import com.logic.MeteorologicalData;
import com.logic.Date;
import com.logic.Fluid;
import com.logic.Exchanger;
import com.logic.ProcessPoint;
import com.logic.Radiation;
import com.logic.FinnedTube;
import com.logic.Utils;
import com.db.DBAccess;

import java.net.URL;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class FXMLDocumentController implements Initializable 
{
    
    /* -------------------------- */
    /*         Parameters         */
    /* -------------------------- */

    // Panels
    
    @FXML private Button pause;
    @FXML private Button startSimulation;
    @FXML private Pane supPane;
    @FXML private Pane leftPane;
    @FXML private Label day;
    
        // Collector Tab
    
            // Control panel       
            @FXML private TextField tiltAngle;
            @FXML private Slider tiltAngleSli;
            @FXML private TextField orientationAngle;
            @FXML private Slider orientationAngleSli;
            @FXML private TextField colArea;
            @FXML private Slider colAreaSli;
            @FXML private TextField numCol;
            @FXML private TextField coefP1;
            @FXML private TextField coefP2;
            @FXML private TextField opEfficiency;
            private double initialLength;
            private double initialWidth;
            private double initialArea;
            @FXML private Label colEfficiency;
            
            // Animation
            @FXML private Box box;
            private Rotate tiltRotation;
            private Rotate orientationRotation;
            
        // Exchanger Tab

            // Control panel  
            @FXML private TextField tansfCoef;
            @FXML private TextField exchangeArea;
            @FXML private Label exchangerEfficiency;
        
        // Pumps Tab
        
            // Control panel  

            // Pump 1
            @FXML private ToggleButton btnPump1;
            @FXML private Slider SliPump1;
            @FXML private TextField TFPump1;
           
            
            // Pump 2
            @FXML private ToggleButton btnPump2;
            @FXML private Slider SliPump2;
            @FXML private TextField TFPump2;
           
            
            // Pump 3
            @FXML private ToggleButton btnPump3;
            @FXML private Slider SliPump3;
            @FXML private TextField TFPump3;
           
            
        // Controllers Tab
            
            // Efficiency control
            @FXML private TextField ctrlEfOn;
            @FXML private TextField ctrlEfOff;
            @FXML private ToggleButton ctrlEfActive;
               
    // Charts
       
        // Temperature chart
        @FXML private LineChart<Number,Number> tempChart;
        @FXML private NumberAxis x;
        @FXML private NumberAxis y;
        private Series<Number,Number> serie;
        private Series<Number,Number> serie1;
        private Series<Number,Number> serie2;
        private Series<Number,Number> serie3;

        // Irradiation chart
        @FXML private LineChart<Number,Number> irradChart;
        @FXML private NumberAxis x1;
        @FXML private NumberAxis y1;
        private Series<Number,Number> serie4;
        private Series<Number,Number> serie5;
    
        // Annual irradiation chart
        @FXML private LineChart<Number,Number> annualIrradChart;
        @FXML private NumberAxis x2;
        @FXML private NumberAxis y2;
        private Series<Number,Number> serie6;
        
    // Animation
        
        // Images
        @FXML private Circle sun;
        @FXML private Rectangle sky;
        @FXML private ImageView floor;
        @FXML private ImageView collectorSystem1;
        @FXML private ImageView collectorSystem2;
        @FXML private ImageView cloud1;
        @FXML private ImageView cloud2;
        @FXML private ImageView cloud3;
        private Image cloud;

        // Circuit parameters
        @FXML private ToggleButton tempOutColTGB; 
        @FXML private ToggleButton tempInColTGB; 
        @FXML private TextField tempOutColTF; 
        @FXML private TextField tempInColTF; 
        @FXML private ToggleButton tempOutTKTGB; 
        @FXML private ToggleButton tempInTKTGB; 
        @FXML private TextField tempOutTKTF; 
        @FXML private TextField tempInTKTF; 
        
        private Timeline timeLine;
        private boolean running = false;
        private KeyFrame kF; 

        // Collector system parameters
        private int hourYear;
        private ProcessPoint processPointAnt;
        private ProcessPoint processPoint;
            
        // Auxiliar
        private DecimalFormat intFormat = new DecimalFormat("#");
        private DecimalFormat twoDecimalFormat = new DecimalFormat("#.00");
       
    /* ----------------------------- */
    /*         Initialization        */
    /* ----------------------------- */
    
    @Override
    public void initialize(URL url, ResourceBundle rb)
    {
        // Initial data
        
        FinnedTubeController finnedTubeController = new FinnedTubeController(100, 104, true);
        Collector collector = new Collector(finnedTubeController);
        PumpController pumpController2 = new PumpController(2, 7, true);
        processPointAnt = new ProcessPoint(hourYear, 25, 25, 25, 25, 0, pumpController2, collector); 
        
        // Annual irradiation chart
        
        serie6 = new Series();
        annualIrradChart.getData().add(serie6); 
        annualIrradChart.setCreateSymbols(false); //hide dots
        annualIrradChart.getXAxis().setTickLabelsVisible(false);
        annualIrradChart.getYAxis().setTickLabelsVisible(false);
        annualIrradChart.getXAxis().setVisible(false);
        annualIrradChart.getYAxis().setVisible(false);
        annualIrradChart.getStyleClass().add("annualIrradChart");
        
        y2.setAutoRanging(false);
        x2.setLowerBound(0);
        x2.setUpperBound(24*365);
        y2.setLowerBound(0);
        y2.setUpperBound(1200);
        
        
        for (int i = 1; i <= 24*365; i++) 
        {
            try {
                DBAccess aDB = DBAccess.getInstance();
                Data data6 = new Data(i, aDB.getHotizontalIrradiation(i));
                serie6.getData().add(data6);
                data6 = null;
            } catch (ClassNotFoundException | IOException ex) {
                Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        // Animation
        
        Image grass = new Image(getClass().getResource("/com/img/grass.png").toExternalForm());
        Image colSyst1 = new Image(getClass().getResource("/com/img/colSyst1.png").toExternalForm());
        Image colSyst2 = new Image(getClass().getResource("/com/img/colSyst2.png").toExternalForm());
        Image cloud = new Image(getClass().getResource("/com/img/cloud.png").toExternalForm());

        collectorSystem1.setImage(colSyst1);
        collectorSystem2.setImage(colSyst2);
        floor.setImage(grass);
        cloud1.setImage(cloud);
        cloud2.setImage(cloud);
        cloud3.setImage(cloud);
        
        tempInColTGB.getStyleClass().add("plotOn");
        tempOutColTGB.getStyleClass().add("plotOn");
        tempInTKTGB.getStyleClass().add("plotOn");
        tempOutTKTGB.getStyleClass().add("plotOn");

        day.getStyleClass().add("efficiencyLabel");

        // Panel controllers

        // Collector Tab

            // Sliders
            tiltAngleSli.setMin(0);
            tiltAngleSli.setMax(180);
            tiltAngleSli.setValue(35);
            tiltAngleSli.valueProperty().addListener((obs, oldval, newVal) -> tiltAngleSli.setValue(newVal.intValue()));

            orientationAngleSli.setMin(220);
            orientationAngleSli.setMax(360 + 220);
            orientationAngleSli.setValue(0);
            orientationAngleSli.valueProperty().addListener((obs, oldval, newVal) -> orientationAngleSli.setValue(newVal.intValue()));
            
            colAreaSli.setMin(0.5);
            colAreaSli.setMax(2);
            colAreaSli.setValue(1.86);
            
            // TextFields
            tiltAngle.setDisable(true);
            tiltAngle.setText("35");

            orientationAngle.setDisable(true);
            orientationAngle.setText("0");

            colArea.setDisable(true);
            colArea.setText("1.86");

            numCol.setText("210");
            Utils.onlyDecimal(numCol);

            coefP1.setText("2.881");
            Utils.onlyDecimal(coefP1);

            coefP2.setText("0.012");
            Utils.onlyDecimal(coefP2);
            
            opEfficiency.setText("0.776");
            Utils.onlyDecimal(opEfficiency);

            // Animation
            initialLength = box.getHeight();
            initialWidth = box.getWidth();
            initialArea = Double.parseDouble(colArea.getText());

            box.setRotationAxis(Rotate.Y_AXIS);
            box.rotateProperty().bind(orientationAngleSli.valueProperty());

            box.getTransforms().add(new Rotate(0, 35, 0));
            tiltRotation = new Rotate(90-Double.parseDouble(tiltAngle.getText()), 0, 0, 0, Rotate.X_AXIS);      
            box.getTransforms().add(tiltRotation);
            
            // Efficiency
            colEfficiency.getStyleClass().add("efficiencyLabel");

        // Exchanger Tab

            // TextFields

            tansfCoef.setText("5312");
            Utils.onlyDecimal(tansfCoef);

            exchangeArea.setText("8.3");
            Utils.onlyDecimal(exchangeArea);

            // Efficiency
            exchangerEfficiency.getStyleClass().add("efficiencyLabel");
            
        // Pumps Tab

            // Sliders

            SliPump1.setMin(0.1);
            SliPump1.setMax(6);
            SliPump1.setValue(4.34);

            SliPump2.setMin(0.1);
            SliPump2.setMax(6);
            SliPump2.setValue(4.66);

            SliPump3.setMin(0.1);
            SliPump3.setMax(1.5);
            SliPump3.setValue(0.22);

            // Buttons

            btnPump1.setSelected(true);
            btnPump1.getStyleClass().add("ctrlButtonOn");
           
            btnPump2.setSelected(true);
            btnPump2.getStyleClass().add("ctrlButtonOn");

            btnPump3.setSelected(true);
            btnPump3.getStyleClass().add("ctrlButtonOn");

            // TextFields

            TFPump1.setDisable(true);
            TFPump1.setText("4.34");

            TFPump2.setDisable(true);
            TFPump2.setText("4.66");

            TFPump3.setDisable(true);
            TFPump3.setText("0.22");

        // Controllers Tab

            // TextFields

            ctrlEfOn.setText("7");
            Utils.onlyDecimal(ctrlEfOn);

            ctrlEfOff.setText("2");
            Utils.onlyDecimal(ctrlEfOff);

            // Buttons

            ctrlEfActive.setSelected(true);
            ctrlEfActive.getStyleClass().add("ctrlButtonOn");
            
    }    
        
    /* --------------------------- */
    /*         Controllers         */
    /* --------------------------- */

    // Buttons

    @FXML private void handleButtonActionStart(ActionEvent event) 
    {
        startSimulation.setDisable(true);
        hourYear = 0;

        // Charts

            // Initial chart configuration
            tempChart.setTitle("Temperatures °C");
            tempChart.setCreateSymbols(false); //hide dots
                    
            irradChart.setTitle("Irradiation W/m²");
            irradChart.setCreateSymbols(false); //hide dots
            irradChart.getStyleClass().add("irradChart");
            
            tempChart.getXAxis().setTickLabelsVisible(false);  
            tempChart.setHorizontalGridLinesVisible(false);
            tempChart.setHorizontalZeroLineVisible(false);
            tempChart.setVerticalGridLinesVisible(false);
            tempChart.setVerticalZeroLineVisible(false);
            
            irradChart.getXAxis().setTickLabelsVisible(false);  
            irradChart.setHorizontalGridLinesVisible(false);
            irradChart.setHorizontalZeroLineVisible(false);
            irradChart.setVerticalGridLinesVisible(false);
            irradChart.setVerticalZeroLineVisible(false);
            
            // Axis
            x.setAutoRanging(false);
            x.setTickUnit(1);
            y.setAutoRanging(false);
            y.setLowerBound(0);
            y.setUpperBound(120);
            
            x1.setAutoRanging(false);
            x1.setTickUnit(1);
            y1.setAutoRanging(false);
            y1.setLowerBound(0);
            y1.setUpperBound(1200);

            
            // Series
            serie = new Series();
            serie.setName("Temp. Collector IN"); 
            
            serie1 = new Series();
            serie1.setName("Temp.Collector OUT");
            
            serie2 = new Series();
            serie2.setName("Temp. Accumulator IN");
            
            serie3 = new Series();
            serie3.setName("Temp. Accumulator OUT");
            
            serie4 = new Series();
            serie4.setName("Horizontal Irradiation");
            
            serie5 = new Series();
            serie5.setName("Tilted Irradiation");
            
            tempChart.getStyleClass().add("graf");
            tempChart.getData().addAll(serie, serie1, serie2 , serie3);
            irradChart.getData().addAll(serie4, serie5);  
        
        
        // Animation

            timeLine = new Timeline();
            timeLine.setCycleCount(Animation.INDEFINITE);
            kF = new KeyFrame(Duration.seconds(0.25), (ActionEvent actionEvent) -> {
                try {
                    plot();
                } catch (ClassNotFoundException | IOException ex) {
                    Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            timeLine.getKeyFrames().add(kF);
            timeLine.play(); // Play animation
            running = true;
    }

    @FXML private void handleButtonActionPause(ActionEvent event) 
    {
        if (running)
        {
            timeLine.pause();
            running = false;
            pause.setText("Play");
        }
        else
        {
            timeLine.play(); 
            running = true;
            pause.setText("Pause");
        }

    }
    
     @FXML private void tempInColShowChart(ActionEvent event) 
    {
       if (tempInColTGB.isSelected())
       {
            serie.getNode().setId("serie-unselect");
            serie.getNode().toBack();
            tempInColTGB.getStyleClass().remove("plotOn");
            tempInColTGB.getStyleClass().add("plotOff");
            tempInColTGB.setText("Show");
       }
       else
       {
            serie.getNode().setId("serie-select");
            tempInColTGB.getStyleClass().remove("plotOff");
            tempInColTGB.getStyleClass().add("plotOn");
            tempInColTGB.setText("Hide");
       }
    }
    
    @FXML private void tempOutColShowChart(ActionEvent event) 
    {
       if (tempOutColTGB.isSelected())
       {
           serie1.getNode().setId("serie-unselect");
           serie1.getNode().toBack();
           tempOutColTGB.getStyleClass().remove("plotOn");
           tempOutColTGB.getStyleClass().add("plotOff");
           tempOutColTGB.setText("Show");
       }
       else
       {
            serie1.getNode().setId("serie-select");
            tempOutColTGB.getStyleClass().remove("plotOff");
            tempOutColTGB.getStyleClass().add("plotOn");
            tempOutColTGB.setText("Hide");
       }

    }
          
    @FXML private void tempInTKShowChart(ActionEvent event) 
    {
       if (tempInTKTGB.isSelected())
       {
            serie3.getNode().setId("serie-unselect");
            serie3.getNode().toBack();
            tempInTKTGB.getStyleClass().remove("plotOn");
            tempInTKTGB.getStyleClass().add("plotOff");
            tempInTKTGB.setText("Show");
       }
       else
       {
            serie3.getNode().setId("serie-select");
            tempInTKTGB.getStyleClass().remove("plotOff");
            tempInTKTGB.getStyleClass().add("plotOn");
            tempInTKTGB.setText("Hide");

       }
    }
               
     @FXML private void tempOutTKShowChart(ActionEvent event) 
    {
       if (tempOutTKTGB.isSelected())
       {
           
            serie2.getNode().setId("serie-unselect");
            serie2.getNode().toBack();
            tempOutTKTGB.getStyleClass().remove("plotOn");
            tempOutTKTGB.getStyleClass().add("plotOff");
            tempOutTKTGB.setText("Show");
       }
       else
       {
            serie2.getNode().setId("serie-select");
            tempOutTKTGB.getStyleClass().remove("plotOff");
            tempOutTKTGB.getStyleClass().add("plotOn");
            tempOutTKTGB.setText("Hide");

       }
    }
     
    @FXML private void btnCtrlEf(MouseEvent event)
    {
        if (!ctrlEfActive.isSelected())
        {
            ctrlEfOn.setDisable(true);
            ctrlEfOff.setDisable(true);
            ctrlEfActive.setText("Off");
            ctrlEfActive.getStyleClass().removeAll("ctrlButtonOn");
            ctrlEfActive.getStyleClass().add("ctrlButtonOff");
            
        }
        else
        {
            ctrlEfOn.setDisable(false);
            ctrlEfOff.setDisable(false);
            ctrlEfActive.setText("On");
            ctrlEfActive.getStyleClass().removeAll("ctrlButtonOff");
            ctrlEfActive.getStyleClass().add("ctrlButtonOn");

        }
    }
       
    @FXML private void btnCtrlPump1(MouseEvent event)
    {
        if (!btnPump1.isSelected())
        {
            btnPump1.setText("Off");
            btnPump1.getStyleClass().removeAll("ctrlButtonOn");
            btnPump1.getStyleClass().add("ctrlButtonOff");
            
        }
        else
        {
            btnPump1.setText("On");
            btnPump1.getStyleClass().removeAll("ctrlButtonOff");
            btnPump1.getStyleClass().add("ctrlButtonOn");

        }
    }
    
    @FXML private void btnCtrlPump2(MouseEvent event)
    {
        if (!btnPump2.isSelected())
        {
            btnPump2.setText("Off");
            btnPump2.getStyleClass().removeAll("ctrlButtonOn");
            btnPump2.getStyleClass().add("ctrlButtonOff");
            
        }
        else
        {
            btnPump2.setText("On");
            btnPump2.getStyleClass().removeAll("ctrlButtonOff");
            btnPump2.getStyleClass().add("ctrlButtonOn");

        }
    }
        
    @FXML private void btnCtrlPump3(MouseEvent event)
    {
        if (!btnPump3.isSelected())
        {
            btnPump3.setText("Off");
            btnPump3.getStyleClass().removeAll("ctrlButtonOn");
            btnPump3.getStyleClass().add("ctrlButtonOff");
            
        }
        else
        {
            btnPump3.setText("On");
            btnPump3.getStyleClass().removeAll("ctrlButtonOff");
            btnPump3.getStyleClass().add("ctrlButtonOn");

        }
    }
       
    // Sliders
    
    @FXML private void tiltAngSlider(MouseEvent event) 
    {
       tiltAngle.setText(Integer.toString((int)tiltAngleSli.getValue()));
       double ang = 90 - (int)tiltAngleSli.getValue();
       tiltRotation.setAngle(ang);       
    }
     
    @FXML private void orientationAngSlider(MouseEvent event) 
    {
       orientationAngle.setText(Integer.toString((int)orientationAngleSli.getValue()-220));
       box.setRotationAxis(Rotate.Y_AXIS);
       box.rotateProperty().bind(orientationAngleSli.valueProperty());
    }
    
    @FXML private void colAreaSlider(MouseEvent event) 
    {                  
       colArea.setText(Double.toString(colAreaSli.getValue()));
       double coef = colAreaSli.getValue()/initialArea;
       box.setHeight(initialLength*coef);
       box.setWidth(initialWidth*coef);
       
    }
        
    @FXML private void Pump1Slider(MouseEvent event) 
    {
       TFPump1.setText(Double.toString(SliPump1.getValue()));
    }
    
    @FXML private void Pump2Slider(MouseEvent event) 
    {
    TFPump2.setText(Double.toString(SliPump2.getValue()));
    }
    
    @FXML private void Pump3Slider(MouseEvent event) 
    {
       TFPump3.setText(Double.toString(SliPump3.getValue()));
    }
     
    // TextFields
    
    @FXML private void tiltAngTextField(KeyEvent event)
    {
       double ang;
       if ((tiltAngle.getText().equals("")) || (Integer.parseInt(tiltAngle.getText()) <= 0 ))
       {
           tiltAngle.setText("0");
           tiltAngleSli.setValue(0);
           ang = 90;
           tiltRotation.setAngle(ang);
       }
       else if (Integer.parseInt(tiltAngle.getText()) >= 180)
       {
           tiltAngle.setText("180");
           tiltAngleSli.setValue(180);
           ang = -90;
           tiltRotation.setAngle(ang);
       }
       else
       {
           tiltAngleSli.setValue(Double.parseDouble(tiltAngle.getText()));
           ang = 90 - Double.parseDouble(tiltAngle.getText());
           tiltRotation.setAngle(ang);
       }
    }
     
    @FXML private void orientationAngTextField(KeyEvent event)
    {
       if ((orientationAngle.getText().equals("")) || (Integer.parseInt(orientationAngle.getText()) <= 0 ))
       {
           orientationAngle.setText("0");
           orientationAngleSli.setValue(0);
       }
       else if (Integer.parseInt(tiltAngle.getText()) >= 180)
       {
           orientationAngle.setText("180");
           orientationAngleSli.setValue(180);
       }
       else
       {
            box.setRotationAxis(Rotate.Y_AXIS);
            box.rotateProperty().bind(orientationAngleSli.valueProperty());
       }
    }
    
    @FXML private void Pump1TextField(KeyEvent event)
    {
       if ((TFPump1.getText().equals("")) || (Double.parseDouble(TFPump1.getText()) < 0 ))
       {
           TFPump1.setText("1");
           SliPump1.setValue(1);
       }
       else if (Double.parseDouble(TFPump1.getText()) > 6)
       {
           TFPump1.setText("6");
           SliPump1.setValue(6);
       }
       else
       {
           SliPump1.setValue(Double.parseDouble(TFPump1.getText()));
       }
    }
    
    @FXML private void Pump2TextField(KeyEvent event)
    {
       if ((TFPump2.getText().equals("")) || (Double.parseDouble(TFPump2.getText()) < 0 ))
       {
           TFPump2.setText("1");
           SliPump2.setValue(1);
       }
       else if (Double.parseDouble(TFPump2.getText()) > 6)
       {
           TFPump2.setText("6");
           SliPump2.setValue(6);
       }
       else
       {
           SliPump2.setValue(Double.parseDouble(TFPump2.getText()));
       }
    }
    
    @FXML private void Pump3TextField(KeyEvent event)
    {
       if ((TFPump3.getText().equals("")) || (Double.parseDouble(TFPump3.getText()) < 0 ))
       {
           TFPump3.setText("1");
           SliPump3.setValue(1);
       }
       else if (Double.parseDouble(TFPump3.getText()) > 1.5)
       {
           TFPump3.setText("1.5");
           SliPump3.setValue(1.5);
       }
       else
       {
           SliPump3.setValue(Double.parseDouble(TFPump3.getText()));
       }
    }

    /* ------------------------ */
    /*         Auxiliar         */
    /* ------------------------ */
    
    // Processes data in real time

    private void plot() throws IOException, ClassNotFoundException
    {
        // Time increasing

        hourYear++;        
        if (hourYear == 365*24 + 1)
            hourYear = 1;
        
        // Date

            Date date = new Date(hourYear);
            day.setText(Utils.ordinal(date.getDay()) + " of " + date.getMonthName());
        
        // Data processing
       
            // Meteorological data 
            MeteorologicalData meteorologicalData = new MeteorologicalData(hourYear);

            // Fluids
            Fluid fluid1 = new Fluid(3765.6 , 1.036);
            Fluid fluid2 = new Fluid(4180 , 1);
            
            // Pumps 
            Pump pump1 = new Pump(true, Double.parseDouble(TFPump1.getText())*fluid1.getDensity());
            Pump pump2 = new Pump((btnPump2.isSelected() && btnPump1.isSelected()), Double.parseDouble(TFPump2.getText())*fluid2.getDensity());
            Pump pump3 = new Pump(btnPump3.isSelected(), Double.parseDouble(TFPump3.getText())*fluid2.getDensity());

            // Radiation
            double tiltAnglelinacion = Double.parseDouble(tiltAngle.getText());
            double orientationAngleientacion = 180 + Double.parseDouble(orientationAngle.getText());
            Radiation radiation = new Radiation(hourYear, tiltAnglelinacion, orientationAngleientacion, meteorologicalData);

            // Finned tube
            FinnedTube finnedTube = new FinnedTube(22.25, 0.0762, 20.44);

            // Controllers
            FinnedTubeController finnedTubeController = new FinnedTubeController(/*Double.parseDouble(ctrlSobCorte.getText())*/100, /*Double.parseDouble(ctrlSobEncendido.getText())*/104, /*ctrlSobActive.isSelected()*/false);
            PumpController pumpController2 = new PumpController(Double.parseDouble(ctrlEfOff.getText()), Double.parseDouble(ctrlEfOn.getText()), ctrlEfActive.isSelected());
            TempInCollectorController tempInCollectorController = new TempInCollectorController(processPointAnt, Double.parseDouble(ctrlEfOff.getText()), Double.parseDouble(ctrlEfOn.getText()), ctrlEfActive.isSelected());

            // Collectors
            DtCollector DtCol = new DtCollector(Double.parseDouble(coefP1.getText()), Double.parseDouble(coefP2.getText()), Double.parseDouble(opEfficiency.getText()), Double.parseDouble(colArea.getText()),  Integer.parseInt(numCol.getText()));
            Collector collector = new Collector(meteorologicalData, radiation, fluid1, pump1, finnedTube, finnedTubeController, DtCol);

            // Exchanger
            double coefTransf = Double.parseDouble(tansfCoef.getText());
            double areaInt = Double.parseDouble(exchangeArea.getText());
            DtExchanger dtExchanger = new DtExchanger(coefTransf, areaInt);
            Exchanger exchanger = new Exchanger(dtExchanger, fluid1, fluid2, pump1, pump2, collector.getTempInCol(), collector.getTempInCol(), collector.getCollectorHeat());

            // Accumulator Tank
            DtAccumulator DtAc = new DtAccumulator(6.5, collector.getAreaCol(), 0.01, fluid2.getDensity());
            Accumulator accumulator = new Accumulator(meteorologicalData, DtAc, fluid2, pump2, pump3);

            processPoint = new ProcessPoint(hourYear, collector, exchanger, accumulator, pumpController2, tempInCollectorController, processPointAnt);
            
        // Data showing
        
            Data data = new Data(hourYear, processPoint.getTempInCollector());
            serie.getData().add(data);

            Data data1 = new Data(hourYear, processPoint.getTempOutCollector());
            serie1.getData().add(data1);

            Data data2 = new Data(hourYear, processPoint.getTempOutAccumulator());
            serie2.getData().add(data2);

            Data data3 = new Data(hourYear, processPoint.getTempInAccumulator());
            serie3.getData().add(data3);

            Data data4 = new Data(hourYear, processPoint.getCollector().getRadiation().getHotizontalIrradiation());
            serie4.getData().add(data4);

            Data data5 = new Data(hourYear, processPoint.getCollector().getRadiation().getTiltedIrradiation());
            serie5.getData().add(data5);
            
            // Axis movement
            x.setLowerBound(hourYear - 100);
            x.setUpperBound(hourYear - 4);

            x1.setLowerBound(hourYear - 100);
            x1.setUpperBound(hourYear - 4);

            // Temperature in boxes
            tempOutColTF.setText(intFormat.format(processPoint.getTempOutCollector()));
            tempInColTF.setText(intFormat.format(processPoint.getTempInCollector()));
            tempOutTKTF.setText(intFormat.format(processPoint.getTempOutAccumulator()));
            tempInTKTF.setText(intFormat.format(processPoint.getTempInAccumulator()));
            
            // Efficiencies
            colEfficiency.setText(Integer.toString((int)(processPoint.getCollector().collectorEfficiency()*100)));
            exchangerEfficiency.setText(Integer.toString((int)(processPoint.getExchanger().getExchangerEfficiency()*100)));
        
        // Animation effects
        
            // System circuit fluid effect
            if (hourYear%5 == 0)
            {
                collectorSystem1.setVisible(!collectorSystem1.isVisible());
            }
    
            // Sky animation

                // Clouds (transparency of clouds is related with the parameter "Atmospheric Transparency")
                double atmosphericTransp = processPoint.getCollector().getRadiation().atmosphericTransparency();
                if (atmosphericTransp == 0)
                {
                    cloud1.setOpacity(0);
                    cloud2.setOpacity(0);
                    cloud3.setOpacity(0);
                }
                else
                {
                    cloud1.setOpacity(1-atmosphericTransp);
                    cloud2.setOpacity(1-atmosphericTransp);
                    cloud3.setOpacity(1-atmosphericTransp);
                }

                // Sky and sun
                double hourDay = processPoint.getCollector().getRadiation().getHourDay();
                
                if (((hourDay > 23) && (hourDay <= 24)) || ((hourDay >= 0) && (hourDay < 11)))
                {
                    sky.setFill(Color.rgb(33, 47, 60, 1));
                    sun.setTranslateX(0);
                    sun.setTranslateY(0);
                }
                else 
                {
                    sun.setTranslateY(7*(hourDay-11)*(hourDay-23));
                    sun.setTranslateX((hourDay-11)*70);
                    
                    Integer intHourDay = Double.valueOf(hourDay).intValue()-5;
                    
                    Double r= -0.0057267*(Math.pow(intHourDay, 6))
                    +0.4008972*(Math.pow(intHourDay, 5))-11.2911490*(Math.pow(intHourDay, 4))+163.0498889*(Math.pow(intHourDay, 3))-1267.3988091*(Math.pow(intHourDay, 2))+5006.9352308*(intHourDay)-7807.1718613;
                    
                    Double g= 0.0017802*(Math.pow(intHourDay, 6))
                    -0.1193796*(Math.pow(intHourDay, 5))+ 3.2285366*(Math.pow(intHourDay, 4)) - 44.99*(Math.pow(intHourDay, 3))+ 337.73*(Math.pow(intHourDay, 2)) - 1264.2*(intHourDay)+ 1874.8;
                    
                    Double b= 0.0086975*(Math.pow(intHourDay, 6))
                    -0.5986915*(Math.pow(intHourDay, 5))+ 16.6031355*(Math.pow(intHourDay, 4)) - 236.62*(Math.pow(intHourDay, 3))+ 1816.33*(Math.pow(intHourDay, 2)) - 7041.5*(intHourDay)+ 10794.8;
                    
                    sky.setFill(Color.rgb(r.intValue(),g.intValue(),b.intValue(),1));
                }


        // Clean the series to save memory
                
        if (serie1.getData().size() >= 500)
        {
            serie1.getData().remove(0,300);
            serie2.getData().remove(0,300);
            serie.getData().remove(0,300);
            serie4.getData().remove(0,300);
            serie5.getData().remove(0,300);
            serie3.getData().remove(0,300);
        }

        // Reset parameters

        processPointAnt = processPoint;
        processPoint = null;
        kF = null;
    }
    
}