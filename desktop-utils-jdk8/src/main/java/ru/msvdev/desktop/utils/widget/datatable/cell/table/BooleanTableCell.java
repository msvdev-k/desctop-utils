package ru.msvdev.desktop.utils.widget.datatable.cell.table;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.controlsfx.control.textfield.TextFields;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.BooleanCellModel;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.StringCellModel;


public class BooleanTableCell<ROW> extends BaseTableCell<ROW, BooleanCellModel> {

    private TextField editNode;


    private void onAction(ActionEvent event) {
        BooleanCellModel cellModel = new BooleanCellModel();

//        String string = editNode.getText();
//        if (string != null && !string.trim().isEmpty()) {
//            cellModel.setValue(string);
//        }

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
//        if (editNode == null) {
//            editNode = new TextField();
//            editNode.setOnAction(this::onAction);
//            editNode.setOnKeyReleased(this::onKeyReleased);
//        }
//
//        StringCellModel item = getItem();
//        String value = item.getValue();
//
//        editNode.setText(value == null ? "" : value);
//        editNode.selectAll();
//
//        if (item.getCompletionValues() != null) {
//            TextFields.bindAutoCompletion(editNode, item.getCompletionValues());
//        }

        return null;
    }

    @Override
    protected Node getGraphicToView() {
        return null;
    }

    @Override
    protected String getTextToView() {
        Boolean value = getItem().getValue();
        return value != null && value ? "TRUE" : "";
    }

    @Override
    protected Pos getAlignmentToView() {
        return Pos.TOP_CENTER;
    }

}
