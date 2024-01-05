package ru.msvdev.desktop.utils.widget.datatable.cell.table;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.CellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.DoubleCellModel;


public class DoubleTableCell<ROW> extends BaseTableCell<ROW, DoubleCellModel> {

    private TextField editNode;


    private void onAction(ActionEvent event) {
        DoubleCellModel cellModel = new DoubleCellModel();

        String string = editNode.getText();
        if (string != null && !string.trim().isEmpty()) {
            try {
                Double value = Double.valueOf(string);
                cellModel.setValue(value);

            } catch (Exception e) {
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

        DoubleCellModel item = getItem();
        Double value = item.getValue();

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
        Double value = getItem().getValue();
        return value == null ? "" : String.valueOf(value);
    }

    @Override
    protected Pos getAlignmentToView() {
        return Pos.TOP_RIGHT;
    }

}
