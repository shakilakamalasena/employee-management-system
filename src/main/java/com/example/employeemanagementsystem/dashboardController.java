package com.example.employeemanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.control.Alert.AlertType;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

public class dashboardController implements Initializable {

    @FXML
    private Button addEmployee_addBtn;

    @FXML
    private Button addEmployee_btn;

    @FXML
    private Button addEmployee_clearBtn;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_date;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_employeeID;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_fname;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_gender;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_lname;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_phone;

    @FXML
    private TableColumn<employeeData, String> addEmployee_col_position;

    @FXML
    private Button addEmployee_deleteBtn;

    @FXML
    private TextField addEmployee_employeeID;

    @FXML
    private TextField addEmployee_fname;

    @FXML
    private AnchorPane addEmployee_form;

    @FXML
    private ComboBox<?> addEmployee_gender;

    @FXML
    private ImageView addEmployee_image;

    @FXML
    private Button addEmployee_importBtn;

    @FXML
    private TextField addEmployee_lname;

    @FXML
    private TextField addEmployee_phone;

    @FXML
    private ComboBox<?> addEmployee_position;

    @FXML
    private TextField addEmployee_search;

    @FXML
    private TableView<employeeData> addEmployee_tableView;

    @FXML
    private Button addEmployee_updateBtn;

    @FXML
    private Button close_btn;

    @FXML
    private Button home_btn;

    @FXML
    private BarChart<?, ?> home_chart;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_totalEmployees;

    @FXML
    private Label home_totalInactiveEmp;

    @FXML
    private Label home_totalPresents;

    @FXML
    private Button logout_btn;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize_btn;

    @FXML
    private Button salary_btn;

    @FXML
    private Button salary_clearBtn;

    @FXML
    private TableColumn<employeeData, String> salary_col_employeeID;

    @FXML
    private TableColumn<employeeData, String> salary_col_fname;

    @FXML
    private TableColumn<employeeData, String> salary_col_lname;

    @FXML
    private TableColumn<employeeData, String> salary_col_position;

    @FXML
    private TableColumn<employeeData, String> salary_col_salary;

    @FXML
    private TextField salary_employeeID;

    @FXML
    private Label salary_fname;

    @FXML
    private AnchorPane salary_form;

    @FXML
    private Label salary_position;

    @FXML
    private TextField salary_salary;

    @FXML
    private Label salary_sname;

    @FXML
    private TableView<employeeData> salary_tableView;

    @FXML
    private Button salary_updateBtn;

    @FXML
    private Label username;

    private Connection connect;
    private Statement statement;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet;

    private Image image;

    public void addEmployeeAdd() {

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "INSERT INTO employee "
                + "(employee_id,firstName,lastName,gender,phoneNum,position,image,date)"
                + "VALUES(?,?,?,?,?,?,?,?)";

        connect = database.connectDb();

        try {

            Alert alert;
            if(addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_fname.getText().isEmpty()
                    || addEmployee_lname.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phone.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || getData.path == null
                    || getData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            } else {

                String check = "SELECT employee_id FROM employee WHERE employee_id = '"
                        + addEmployee_employeeID.getText() + "'";

                statement = connect.createStatement();
                resultSet = statement.executeQuery(check);

                if(resultSet.next()) {

                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Employee ID:" + addEmployee_employeeID.getText() + " was already exist!");
                    alert.showAndWait();

                } else {

                    preparedStatement = connect.prepareStatement(sql);
                    preparedStatement.setString(1, addEmployee_employeeID.getText());
                    preparedStatement.setString(2, addEmployee_fname.getText());
                    preparedStatement.setString(3, addEmployee_lname.getText());
                    preparedStatement.setString(4, (String) addEmployee_gender.getSelectionModel().getSelectedItem());
                    preparedStatement.setString(5, addEmployee_phone.getText());
                    preparedStatement.setString(6, (String) addEmployee_position.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");

                    preparedStatement.setString(7, uri);
                    preparedStatement.setString(8, String.valueOf(sqlDate));

                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Added");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeUpdate() {

        String uri = getData.path;
        uri = uri.replace("//", "////");

        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());

        String sql = "UPDATE employee SET firstName = '"
                + addEmployee_fname.getText() + "', lastName = '"
                + addEmployee_lname.getText() + "', gender = '"
                + addEmployee_gender.getSelectionModel().getSelectedItem() + "', phoneNum = '"
                + addEmployee_phone.getText() + "', position = '"
                + addEmployee_position.getSelectionModel().getSelectedItem() + "', image = '"
                + uri + "', date = '" + sqlDate + "' WHERE employee_id = '"
                + addEmployee_employeeID.getText() + "'";

        connect = database.connectDb();

        try {

            Alert alert;
            if(addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_fname.getText().isEmpty()
                    || addEmployee_lname.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phone.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || getData.path == null
                    || getData.path == "") {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            } else {

                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE Employee ID: " + addEmployee_employeeID.getText() + "?");
                Optional<ButtonType> optional = alert.showAndWait();

                if(optional.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeDelete() {

        String sql = "DELETE FROM employee WHERE employee_id = '"
                + addEmployee_employeeID.getText() + "'";

        connect = database.connectDb();

        try {

            Alert alert;
            if(addEmployee_employeeID.getText().isEmpty()
                    || addEmployee_fname.getText().isEmpty()
                    || addEmployee_lname.getText().isEmpty()
                    || addEmployee_gender.getSelectionModel().getSelectedItem() == null
                    || addEmployee_phone.getText().isEmpty()
                    || addEmployee_position.getSelectionModel().getSelectedItem() == null
                    || getData.path == null
                    || getData.path == "") {
                alert = new Alert(AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all the blank fields");
                alert.showAndWait();
            } else {

                alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE Employee ID: " + addEmployee_employeeID.getText() + "?");
                Optional<ButtonType> optional = alert.showAndWait();

                if(optional.get().equals(ButtonType.OK)) {
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);

                    String deleteInfo = "DELETE FROM employee WHERE employee_id = '"
                            + addEmployee_employeeID.getText() + "'";

                    preparedStatement = connect.prepareStatement(deleteInfo);
                    preparedStatement.executeUpdate();

                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Deleted!");
                    alert.showAndWait();

                    addEmployeeShowListData();
                    addEmployeeReset();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void addEmployeeReset() {
        addEmployee_employeeID.setText("");
        addEmployee_fname.setText("");
        addEmployee_lname.setText("");
        addEmployee_gender.getSelectionModel().clearSelection();
        addEmployee_position.getSelectionModel().clearSelection();
        addEmployee_phone.setText("");
        addEmployee_image.setImage(null);
        getData.path = "";
    }

    public void addEmployeeInsertImage() {

        FileChooser open = new FileChooser();
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if(file != null) {
            getData.path = file.getAbsolutePath();

            image = new Image(file.toURI().toString(), 101, 127, false, true);
            addEmployee_image.setImage(image);
        }
    }

    public String[] positionList = {"Data Scientist", "Cybersecurity Analyst", "Software Engineer", "Cloud Solutions Architect"};
    public void addEmployeePositionList() {
        List<String> listP = new ArrayList<>();

        for(String data: positionList) {
            listP.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listP);
        addEmployee_position.setItems(listData);
    }

    private String[] listGender = {"Male", "Female", "Others"};
    public void addEmployeeGenderList() {
        List<String> listG = new ArrayList<>();

        for(String data: listGender) {
            listG.add(data);
        }

        ObservableList listData = FXCollections.observableArrayList(listG);
        addEmployee_gender.setItems(listData);
    }

    public void addEmployeeSearch() {

        FilteredList<employeeData> filter = new FilteredList<>(addEmployeeList, e-> true);

        addEmployee_search.textProperty().addListener((Observable, oldValue, newValue) ->{

            filter.setPredicate(predicateEmployeeData ->{

                if(newValue == null || newValue.isEmpty()){
                    return true;
                }

                String searchKey = newValue.toLowerCase();

                if(predicateEmployeeData.getEmployeeId().toString().contains(searchKey)){
                    return true;
                } else if (predicateEmployeeData.getFirstName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getLastName().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getGender().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPhoneNum().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getPosition().toLowerCase().contains(searchKey)) {
                    return true;
                } else if (predicateEmployeeData.getDate().toString().contains(searchKey)) {
                    return true;
                } else {
                    return false;
                }
            });
        });

        SortedList<employeeData> sortedList = new SortedList<>(filter);

        sortedList.comparatorProperty().bind(addEmployee_tableView.comparatorProperty());
        addEmployee_tableView.setItems(sortedList);

    }

    public ObservableList<employeeData> addEmployeeListData() {

        ObservableList<employeeData> listData = FXCollections.observableArrayList();
        String sql = "SELECT * FROM employee";

        connect = database.connectDb();

        try {
            preparedStatement = connect.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            employeeData employeeD;

            while (resultSet.next()) {
                employeeD = new employeeData(resultSet.getInt("employee_id"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        resultSet.getString("gender"),
                        resultSet.getString("phoneNum"),
                        resultSet.getString("position"),
                        resultSet.getString("image"),
                        resultSet.getDate("date"));
                listData.add(employeeD);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return listData;

    }

    private ObservableList<employeeData> addEmployeeList;
    public void addEmployeeShowListData() {

        addEmployeeList = addEmployeeListData();

        addEmployee_col_employeeID.setCellValueFactory(new PropertyValueFactory<>("employeeId"));
        addEmployee_col_fname.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        addEmployee_col_lname.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        addEmployee_col_gender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        addEmployee_col_phone.setCellValueFactory(new PropertyValueFactory<>("phoneNum"));
        addEmployee_col_position.setCellValueFactory(new PropertyValueFactory<>("position"));
        addEmployee_col_date.setCellValueFactory(new PropertyValueFactory<>("date"));

        addEmployee_tableView.setItems(addEmployeeList);

    }

    public void addEmployeeSelect() {
        employeeData employeeD = addEmployee_tableView.getSelectionModel().getSelectedItem();
        int num = addEmployee_tableView.getSelectionModel().getSelectedIndex();

        if ((num - 1) < -1) {
            return;
        }

        addEmployee_employeeID.setText(String.valueOf(employeeD.getEmployeeId()));
        addEmployee_fname.setText(employeeD.getFirstName());
        addEmployee_lname.setText(employeeD.getLastName());
        addEmployee_phone.setText(employeeD.getPhoneNum());

        getData.path = employeeD.getImage();

        String uri = "file:" + employeeD.getImage();

        image = new Image(uri, 101, 127, false, true);
        addEmployee_image.setImage(image);
    }

    public void displayUsername() {
        username.setText(getData.username);
    }

    public void switchForm(ActionEvent event) {

        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom left, #a9a9a9, #fffcfc);");
            addEmployee_btn.setStyle("-fx-background-color: transparent");
            salary_btn.setStyle("-fx-background-color: transparent");

        } else if (event.getSource() == addEmployee_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(true);
            salary_form.setVisible(false);

            addEmployee_btn.setStyle("-fx-background-color: linear-gradient(to bottom left, #a9a9a9, #fffcfc);");
            home_btn.setStyle("-fx-background-color: transparent");
            salary_btn.setStyle("-fx-background-color: transparent");

            addEmployeeGenderList();
            addEmployeePositionList();
            addEmployeeSearch();

        } else if (event.getSource() == salary_btn) {
            home_form.setVisible(false);
            addEmployee_form.setVisible(false);
            salary_form.setVisible(true);

            salary_btn.setStyle("-fx-background-color: linear-gradient(to bottom left, #a9a9a9, #fffcfc);");
            addEmployee_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");

        }

    }

    private double x = 0;
    private double y = 0;

    public void logout() {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to logout?");
        Optional<ButtonType> optional = alert.showAndWait();

        try {
            if (optional.get().equals(ButtonType.OK)) {
                logout_btn.getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });

                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);

                    stage.setOpacity(0.8);
                });

                root.setOnMouseReleased((MouseEvent event) -> {
                    stage.setOpacity(1);
                });

                stage.initStyle(StageStyle.TRANSPARENT);

                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void close() {
        System.exit(0);
    }

    public void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        displayUsername();

        addEmployeeShowListData();
        addEmployeeGenderList();
        addEmployeePositionList();

    }
}
