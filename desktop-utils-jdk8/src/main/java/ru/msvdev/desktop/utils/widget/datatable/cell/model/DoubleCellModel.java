package ru.msvdev.desktop.utils.widget.datatable.cell.model;


public class DoubleCellModel extends CellModel<Double> implements Cloneable {

    @Override
    public DoubleCellModel clone() {
        try {
            return (DoubleCellModel) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
