package ru.msvdev.desktop.utils.widget.datatable.cell.table;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.CellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.MoneyCellModel;

import java.math.BigDecimal;
import java.math.RoundingMode;


public class MoneyTableCell<ROW> extends BaseTableCell<ROW, MoneyCellModel> {

    private TextField editNode;


    private void onAction(ActionEvent event) {
        MoneyCellModel cellModel = new MoneyCellModel();

        String string = editNode.getText();
        if (string != null && !string.trim().isEmpty()) {
            try {
                String tmpString = string.replace(",", ".");

                double parseDouble = Double.parseDouble(tmpString);
                if (parseDouble >= 0) {

                    BigDecimal value = BigDecimal
                            .valueOf(parseDouble)
                            .setScale(2, RoundingMode.HALF_UP);
                    cellModel.setValue(value);

                } else {
                    cellModel.setError(CellModel.CellError.NUMBER);
                }

            } catch (Exception ignored) {
                cellModel.setError(CellModel.CellError.NUMBER);
            }
        }

        commitEdit(cellModel);
        event.consume();
    }

    private void onKeyReleased(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            cancelEdit();
            event.consume();
        }
    }

    @Override
    protected Node getEditor() {
        if (editNode == null) {
            editNode = new TextField();
            editNode.setOnAction(this::onAction);
            editNode.setOnKeyReleased(this::onKeyReleased);
        }

        MoneyCellModel item = getItem();
        BigDecimal value = item.getValue();

        editNode.setText(value == null ? "" : String.valueOf(value));
        editNode.selectAll();

        return editNode;
    }

    @Override
    protected Node getGraphicToView() {
        return null;
    }

    @Override
    protected String getTextToView() {
        MoneyCellModel item = getItem();
        BigDecimal value = item.getValue();

        return value == null ? "" : String.format("%s %s", value, item.getSign());
    }

    @Override
    protected Pos getAlignmentToView() {
        return Pos.TOP_RIGHT;
    }

}
