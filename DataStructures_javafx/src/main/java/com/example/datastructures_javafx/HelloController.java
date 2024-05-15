package com.example.datastructures_javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class HelloController {
    @FXML
    private TextField txt_dato;

    @FXML
    private TextArea area_datos;

    @FXML
    private TextField txt_dato_set;

    @FXML
    private TextArea area_datos_set;

    @FXML
    private Label lbl_count_set;

    @FXML
    private Label lbl_date_time;

    private TreeSet<String> datosSet = new TreeSet<>();

    @FXML
    public void initialize() {
        // Agregar los jugadores a la ComboBox
        ObservableList<String> jugadores = FXCollections.observableArrayList(
                "dovbyck", "bellingham", "sorloth", "lewandoski", "vinicius");
        comboJugadores.setItems(jugadores);

        // Start the timeline to update the date and time every minute
        Timeline timeline = new Timeline(new KeyFrame(Duration.minutes(1), event -> updateDateTime()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        // Initial call to set the current date and time
        updateDateTime();
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        lbl_date_time.setText(now.format(formatter));
    }

    @FXML
    protected void agregarSet() {
        datosSet.add(txt_dato_set.getText());
        txt_dato_set.clear();
        updateSetCount();
    }

    @FXML
    protected void mostrarSet() {
        StringBuilder sb = new StringBuilder();
        for (String item : datosSet) {
            sb.append(item).append("\n");
        }
        area_datos_set.setText(sb.toString());
        updateSetCount();

        // Mostrar un Alert con la IP local
        try {
            String ipAddress = InetAddress.getLocalHost().getHostAddress();
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("Información de IP");
            alert.setHeaderText(null);
            alert.setContentText("La IP desde la que estás trabajando es: " + ipAddress);
            alert.showAndWait();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void ordenarSetMostrado() {
        // Get the text from the TextArea, split by new lines, sort, and join back
        String sortedText = Arrays.stream(area_datos_set.getText().split("\n"))
                .sorted()
                .collect(Collectors.joining("\n"));
        area_datos_set.setText(sortedText);
    }

    @FXML
    protected void handleSetClick(MouseEvent event) {
        String selectedText = area_datos_set.getSelectedText();
        if (!selectedText.isEmpty() && datosSet.contains(selectedText.trim())) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Confirmación de eliminación");
            alert.setHeaderText(null);
            alert.setContentText("¿Deseas eliminar el elemento: " + selectedText.trim() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                datosSet.remove(selectedText.trim());
                mostrarSet();
            }
        }
    }

    private void updateSetCount() {
        lbl_count_set.setText("Número de elementos: " + datosSet.size());
    }

    @FXML
    public void agregarAL() {
        // Implementación de agregarAL para la pestaña List
    }

    @FXML
    public void mostrarAL() {
        // Implementación de mostrarAL para la pestaña List
    }

    @FXML
    private ComboBox<String> comboJugadores;

    @FXML
    private TextField txtGoles;

    @FXML
    private Button btnClasificacion;

    private Map<String, Integer> golesPorJugador = new HashMap<>();

    @FXML
    protected void agregarGoles() {
        String jugador = comboJugadores.getValue();
        if (jugador != null && !txtGoles.getText().isEmpty()) {
            int goles = Integer.parseInt(txtGoles.getText());
            golesPorJugador.put(jugador, goles);
            txtGoles.clear();
        }
    }

    @FXML
    protected void mostrarClasificacion() {
        // Crear una lista de Map.Entry para ordenar por cantidad de goles
        ObservableList<Map.Entry<String, Integer>> clasificacion = FXCollections.observableArrayList(golesPorJugador.entrySet());
        clasificacion.sort((entry1, entry2) -> entry2.getValue().compareTo(entry1.getValue()));

        // Mostrar los tres primeros en orden descendente
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Math.min(clasificacion.size(), 3); i++) {
            Map.Entry<String, Integer> entry = clasificacion.get(i);
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append(" goles\n");
        }

        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Clasificación de Pichichis");
        alert.setHeaderText(null);
        alert.setContentText(sb.toString());
        alert.showAndWait();
    }
}
