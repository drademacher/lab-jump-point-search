package application;

import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.util.Pair;

import java.util.Optional;

/**
 * Created by paloka on 02.06.16.
 */
public class DialogExecuter {

    Pair<Integer,Integer> executeEmptyMapDialoge(){
        Dialog<Pair<Integer,Integer>> dialog    = new Dialog<>();
        dialog.setTitle("New Random Map");
        ButtonType confirmButton  = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(confirmButton,ButtonType.CANCEL);

        TextField xDim = new IntegerTextField();
        xDim.setPrefWidth(55);
        TextField yDim = new IntegerTextField();
        yDim.setPrefWidth(55);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(new Label("xDim:"), 0, 0);
        gridPane.add(xDim, 1, 0);
        gridPane.add(new Label("yDim:"), 0, 1);
        gridPane.add(yDim, 1, 1);
        dialog.getDialogPane().setContent(gridPane);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == confirmButton) {
                return new Pair<Integer,Integer>(Integer.valueOf(xDim.getText()), Integer.valueOf(yDim.getText()));
            }
            return null;
        });

        Optional<Pair<Integer,Integer>> result  = dialog.showAndWait();
        return result.get();
    }





    /* ------- Data Type for Dialog Fields ------- */

    private class IntegerTextField extends TextField
    {
        @Override
        public void replaceText(int start, int end, String text) {
            if (validate(text)) super.replaceText(start, end, text);
        }

        @Override
        public void replaceSelection(String text) {
            if (validate(text)) super.replaceSelection(text);
        }

        private boolean validate(String text) {
            System.out.println(text);
            return (this.getText()+text).matches("[0-9]{0,3}");
        }
    }
}