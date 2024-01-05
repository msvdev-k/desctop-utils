package ru.msvdev.desktop.utils.widget.datatable.cell.model;


public class BooleanCellModel extends CellModel<Boolean> implements Cloneable {

    @Override
    public BooleanCellModel clone() {
        try {
            return (BooleanCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
