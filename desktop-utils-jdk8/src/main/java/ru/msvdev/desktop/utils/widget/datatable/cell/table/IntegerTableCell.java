package ru.msvdev.desktop.utils.widget.datatable.cell.table;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.CellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.IntegerCellModel;


public class IntegerTableCell<ROW> extends BaseTableCell<ROW, IntegerCellModel> {

    private TextField editNode;


    private void onAction(ActionEvent event) {
        IntegerCellModel cellModel = new IntegerCellModel();

        String string = editNode.getText();
        if (string != null && !string.trim().isEmpty()) {
            try {
                Integer value = Integer.valueOf(string);
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

        IntegerCellModel item = getItem();
        Integer value = item.getValue();

        editNode.setText(value == null ? "" : String.valueOf(value));

        editNode.setText(String.valueOf(item.getValue()));
        editNode.selectAll();

        return editNode;
    }

    @Override
    protected Node getGraphicToView() {
        return null;
    }

    @Override
    protected String getTextToView() {
        Integer value = getItem().getValue();
        return value == null ? "" : String.valueOf(value);
    }

    @Override
    protected Pos getAlignmentToView() {
        return Pos.TOP_RIGHT;
    }

}
