package ru.msvdev.desktop.utils.widget.datatable.cell.model;


public class IntegerCellModel extends CellModel<Integer> implements Cloneable {

    @Override
    public IntegerCellModel clone() {
        try {
            return (IntegerCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
