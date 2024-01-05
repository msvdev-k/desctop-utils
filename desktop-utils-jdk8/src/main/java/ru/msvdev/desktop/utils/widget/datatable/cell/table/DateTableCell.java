package ru.msvdev.desktop.utils.widget.datatable.cell.table;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.msvdev.desktop.utils.widget.datatable.cell.model.DateCellModel;

import java.time.LocalDate;


public class DateTableCell<ROW> extends BaseTableCell<ROW, DateCellModel> {

    private DatePicker editNode;


    private void onAction(ActionEvent event) {
        DateCellModel cellModel = new DateCellModel();

        LocalDate localDate = editNode.getValue();
        if (localDate != null) {
            cellModel.setValue(localDate);
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
            editNode = new DatePicker();
            editNode.setOnAction(this::onAction);
            editNode.setOnKeyReleased(this::onKeyReleased);
        }

        DateCellModel item = getItem();
        LocalDate value = item.getValue();

        editNode.setValue(value == null ? LocalDate.now() : value);
        editNode.getEditor().selectAll();

        return editNode;
    }

    @Override
    protected Node getGraphicToView() {
        return null;
    }

    @Override
    protected String getTextToView() {
        LocalDate value = getItem().getValue();
        return value == null ? "" : String.valueOf(value);
    }

    @Override
    protected Pos getAlignmentToView() {
        return Pos.TOP_CENTER;
    }

}
