package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Iterator;
import java.util.Scanner;

public class Controller {
    public Main main = new Main();
    public String[] studentList;
    public int[] studentFrequency;
    public int size;
    public boolean repeats = false;
    public static boolean quit = false;
    @FXML public Label selectedStudent;

    @FXML
    private void openFile() throws IOException, InvalidFormatException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Student List");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Excel Files","*.xlsx"),
                    new FileChooser.ExtensionFilter("Text Files","*.txt"));
        File file = fileChooser.showOpenDialog(main.stage);
        setStudentList(file);
    }

    @FXML private void setRepeatPermission(){
        System.out.println("switching repeat permission");
        repeats = !repeats;
        resetStats();
    }

    public void setStudentList(File file) throws IOException, InvalidFormatException {
        String fileName = file.getName();
        String fileExtension = fileName.substring(fileName.indexOf('.'));
        switch (fileExtension) {
            case ".txt":
                Scanner brad = new Scanner(file);
                size = 0;
                while (brad.hasNextLine()) {
                    brad.nextLine();
                    size++;
                }
                Scanner chad = new Scanner(file);
                studentList = new String[size];
                studentFrequency = new int[size];
                int pos = 0;
                while (chad.hasNextLine()) {
                    studentList[pos] = chad.nextLine();
                    pos++;
                }
                break;
            case ".xlsx":
                FileInputStream inputStream = new FileInputStream(file);
                XSSFWorkbook couture = new XSSFWorkbook(inputStream);
                XSSFSheet sheet = couture.getSheetAt(0);
                size = sheet.getLastRowNum();
                System.out.println(size);
                studentList = new String[size];
                studentFrequency = new int[size];
                Iterator<Row> rowIterator = sheet.iterator();
                String cellNames = "";
                while (rowIterator.hasNext()) {
                    Row row = rowIterator.next();
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        cellNames += cell.getStringCellValue() + ",";
                        System.out.println(cell.getStringCellValue());
                    }
                }
                for(int i = 0;i < size;i++){
                    studentList[i] = cellNames.substring(0,cellNames.indexOf(','));
                    cellNames = cellNames.substring(cellNames.indexOf(',') + 1);
                }
        }

        selectedStudent.setText("Select Next Student");

    }

    @FXML
    private void resetStats(){
        try {
            for (int i = 0; i < studentFrequency.length; i++)
                studentFrequency[i] = 0;
        }catch (Exception e){
            System.out.println("No Stats to Reset");
        }
    }

    @FXML private void next() throws MalformedURLException {
        int student;
        int small = findSmall();
        do{
            student = (int) (Math.random() * size);
            if(repeats)
                break;
        }while(studentFrequency[student] > small);

        File file = new File("sample/soundEffect.mp3");
        Media media = new Media(file.toURI().toURL().toExternalForm());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.play();
        selectedStudent.setText(studentList[student]);
        studentFrequency[student]++;

    }

    public int findSmall(){
        int small = studentFrequency[0];
        for(int i = 1;i < studentFrequency.length;i++){
            if(studentFrequency[i] < small)
                small = studentFrequency[i];
        }
        return small;
    }

    @FXML
    private void quit(){
        quit = true;
    }

    public String setString(){
        String studentStats = "";
        try {
            for (int i = 0; i < studentList.length; i++)
                studentStats += (studentList[i] + ":\t\t" + studentFrequency[i] + "\n");
        }catch(Exception e){
            studentStats = "There have been zero selections with the current student list.";
        }
        return studentStats;
    }


    @FXML
    private void showStats(){
        String studentStats = setString();
        Alert stats = new Alert(Alert.AlertType.INFORMATION);
        stats.setTitle("Statistics");
        stats.setHeaderText("Student Frequencies");
        stats.setGraphic(new ImageView(new Image("sample/stats.jpg")));
        stats.setContentText(studentStats);
        stats.showAndWait();
    }
}
