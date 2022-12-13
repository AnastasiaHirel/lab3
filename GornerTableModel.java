package bsu.rfe.course2.group6.AnastasiaHirel.lab3;
import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel{

    private Double from, to, step;
    private Double[] coefficients;

    public GornerTableModel(Double from, Double to, Double step, Double[] coefficients) {
        this.from = from;
        this.to = to;
        this.step = step;
        this.coefficients = coefficients;
    }

    public Double getFrom() {
        return from;
    }

    public Double getTo() {
        return to;
    }

    public Double getStep() {
        return step;
    }

    @Override
    public int getRowCount() {
        return (int)(Math.ceil((to - from) / step)) + 1;
    }
    @Override
    public int getColumnCount() {
        return 3;
    }
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Double x = from + step * rowIndex;
        Double y = calculateGorner(x);
        Boolean z = y >0;
        switch (columnIndex){
            case 0: return x;
            case 1: return y;
            case 2: return z;
        }
        return null;
    }
    public Double calculateGorner(Double x){
        Double y = coefficients[coefficients.length - 1];
        for(int i = coefficients.length - 2; i > 0; i--){
            y += y*x + x*coefficients[i];
        }
        return y;
    }
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if(columnIndex == 2) return Boolean.class;
        return Double.class;
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0: return "Значение Х";
            case 1: return "Значение многочлена";
            case 2: return "Значение многочлена >0?";
        }
        return null;
    }
}