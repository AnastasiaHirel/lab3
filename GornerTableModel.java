package bsu.rfe.course2.group6.AnastasiaHirel.lab3;
import javax.swing.table.AbstractTableModel;

public class GornerTableModel extends AbstractTableModel {
    private Double[] coefficients; //коэффициенты
    private Double from;//начальная точка
    private Double to;//конечная точка
    private Double step;//размер шага
    public GornerTableModel(Double from, Double to, Double step,
                            Double[] coefficients) {
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
    public int getColumnCount() {
        return 2;
    }
    public int getRowCount() {
        return new Double(Math.ceil((to-from)/step)).intValue()+1;
    }
    public Object getValueAt(int row, int col) {
        double x = from + step*row;
        double y = x;
       // for(int i = coefficients.length; i > 1; i--){
       //     y = y + coefficients[i]*x + i;
       // }
        return y;
    }

    public String getColumnName(int col) {
        switch (col) {
            case 0:
                return "Значение X";
            default:
                return "Значение многочлена";
        }
    }
    public Class<?> getColumnClass(int col) {
        return Double.class;
    }
}