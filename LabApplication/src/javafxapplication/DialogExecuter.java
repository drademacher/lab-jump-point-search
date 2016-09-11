package javafxapplication;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;
import core.util.Vector;
import core.util.Tuple2;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Provides dialogs for the JavaFX application.
 *
 * @author Danny Rademacher, Patrick Loka
 * @version 1.0
 * @since 1.0
 */
public class DialogExecuter {
    void executeAlertDialog(String title, String text) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(text);
        alert.showAndWait();
    }

    //Todo: Vll kann man auf die tupel verzichten, wenn man einfach callbacks übergibt, vll geht das sogar abstrahiert
    Vector executeMapDimensionDialog(String title) {
        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Map<String, TextField> fields = new LinkedHashMap<>();
        fields.put("xDim", new DimensionTextField().setWidth(55));
        fields.put("yDim", new DimensionTextField().setWidth(55));

        return executeDialog(title, confirmButton, fields, dialogButton -> {
            if (dialogButton == confirmButton) {
                return new Vector(
                        Integer.valueOf(fields.get("xDim").getText()),
                        Integer.valueOf(fields.get("yDim").getText()));
            }
            return null;
        });
    }

    //Todo: can we abstract executeRandomDialog on executeMapDimensionDialog, because its just one textfield more =(
    Tuple2<Vector, Double> executeRandomMapDialog() {
        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Map<String, TextField> fields = new LinkedHashMap<>();
        fields.put("xDim", new DimensionTextField().setWidth(55));
        fields.put("yDim", new DimensionTextField().setWidth(55));
        fields.put("pPas", new ProbabilityTextField().setWidth(55));

        return executeDialog("New Random Map", confirmButton, fields, dialogButton -> {
            if (dialogButton == confirmButton) {
                return new Tuple2<>(
                        new Vector(Integer.valueOf(fields.get("xDim").getText()),Integer.valueOf(fields.get("yDim").getText())),
                        Double.valueOf(fields.get("pPas").getText()));
            }
            return null;
        });
    }

    Tuple2<Vector, Integer> executeRoomNumberMapDialog() {
        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Map<String, TextField> fields = new LinkedHashMap<>();
        fields.put("xDim", new DimensionTextField().setWidth(55));
        fields.put("yDim", new DimensionTextField().setWidth(55));
        fields.put("max number of rooms", new DimensionTextField().setWidth(55));

        return executeDialog("New Random Map", confirmButton, fields, dialogButton -> {
            if (dialogButton == confirmButton) {
                return new Tuple2<>(
                        new Vector(Integer.valueOf(fields.get("xDim").getText()),Integer.valueOf(fields.get("yDim").getText())),
                        Integer.valueOf(fields.get("pPas").getText()));
            }
            return null;
        });
    }


    Vector executePositionDialog(String title) {
        ButtonType confirmButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        Map<String, TextField> fields = new LinkedHashMap<>();
        fields.put("x coordinate", new DimensionTextField().setWidth(55));
        fields.put("y coordinate", new DimensionTextField().setWidth(55));

        return executeDialog(title, confirmButton, fields, dialogButton -> {
            if (dialogButton == confirmButton) {
                return new Vector(
                        Integer.valueOf(fields.get("x coordinate").getText()),
                        Integer.valueOf(fields.get("y coordinate").getText()));
            }
            return null;
        });
    }


    /* ------- DialogExecuter ------- */

    private <T> T executeDialog(String title, ButtonType confirmButton, Map<String, TextField> fields, Callback<ButtonType, T> callback) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton, ButtonType.CANCEL);
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        Iterator<String> labels = fields.keySet().iterator();
        for (int i = 0; labels.hasNext(); i++) {
            String label = labels.next();
            gridPane.add(new Label(label), 0, i);
            gridPane.add(fields.get(label), 1, i);
        }
        dialog.getDialogPane().setContent(gridPane);
        dialog.setResultConverter(callback);
        return dialog.showAndWait().get();
    }


    /* ------- Data Types for Dialog Fields ------- */

    private abstract class ValidatedTextField extends TextField {
        @Override
        public void replaceText(int start, int end, String text) {
            if (validate(text)) super.replaceText(start, end, text);
        }

        @Override
        public void replaceSelection(String text) {
            if (validate(text)) super.replaceSelection(text);
        }

        public ValidatedTextField setWidth(int width) {
            this.setPrefWidth(width);
            return this;
        }

        protected abstract boolean validate(String text);
    }

    private class DimensionTextField extends ValidatedTextField {
        @Override
        protected boolean validate(String text) {
            return (this.getText() + text).matches("[0-9]{0,3}");
        }
    }

    private class ProbabilityTextField extends ValidatedTextField {
        @Override
        protected boolean validate(String text) {
            String newText = this.getText() + text;
            if (newText.equals("0")) return true;
            if (newText.equals("1")) return true;
            return newText.matches("0\\.[0-9]{0,3}");
        }
    }
}