package bsu.rfe.course2.group6.AnastasiaHirel.lab3;
import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

public class MainFrame extends JFrame {
    private static final int WIDTH = 700;
    private static final int HEIGHT = 500;
    private JFileChooser fileChooser = null;
    private JMenuItem saveToTextMenuItem;
    private JMenuItem searchValueMenuItem;
    private JMenuItem infoMenuItem;
    private JCheckBoxMenuItem showColumnMenuItem;
    private JTextField textFieldFrom;
    private JTextField textFieldTo;
    private JTextField textFieldStep;
    private Box hBoxResult;
    private GornerTableCellRenderer renderer = new GornerTableCellRenderer();
    private GornerTableModel data;
    private JTable table;
    private TableColumn bool_column;
    private Double[] coefficients;

    public MainFrame(Double[] coefficients){
        super("Табулирование функции на отрезке");
        setSize(WIDTH, HEIGHT);
        this.coefficients = coefficients;
        Toolkit kit = Toolkit.getDefaultToolkit();
        setLocation((kit.getScreenSize().width - WIDTH)/2, (kit.getScreenSize().height - HEIGHT)/2);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        JMenu fileMenu = new JMenu("Файл");
        menuBar.add(fileMenu);
        JMenu tableMenu = new JMenu("Таблица");
        menuBar.add(tableMenu);
        JMenu infoMenu = new JMenu("Справка");
        menuBar.add(infoMenu);
        Action saveToTextAction = new AbstractAction( "Сохранить в текстовый файл") {
            public void actionPerformed(ActionEvent event) {
                if (fileChooser == null) {
                    fileChooser = new JFileChooser();
                    fileChooser.setCurrentDirectory(new File("."));
                }
                if (fileChooser.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION){
                    saveToTextFile(fileChooser.getSelectedFile());
                }
            }
        };
        saveToTextMenuItem = fileMenu.add(saveToTextAction);

        fileMenu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                if (data == null) saveToTextMenuItem.setEnabled(false);
                else saveToTextMenuItem.setEnabled(true);
            }
            @Override
            public void menuDeselected(MenuEvent e) { }
            @Override
            public void menuCanceled(MenuEvent e) { }
        });
        Action searchValueAction = new AbstractAction("Найти значение функции") {
            public void actionPerformed(ActionEvent event) {
                String value = JOptionPane.showInputDialog(MainFrame.this, "Введите значение для поиска", "Поиск значения", JOptionPane.QUESTION_MESSAGE);
                renderer.setNeedle(value);
                getContentPane().repaint();
            }
        };
        searchValueMenuItem = tableMenu.add(searchValueAction);
        tableMenu.add(new JSeparator());
        showColumnMenuItem = new JCheckBoxMenuItem("Показать третий столбец", true);
        tableMenu.add(showColumnMenuItem);
        showColumnMenuItem.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == 2) {
                    bool_column = table.getColumnModel().getColumn(2);
                    table.removeColumn(bool_column);
                }if(e.getStateChange() == 1){
                    table.addColumn(bool_column);
                }
            }
        });

        Action aboutProgrammAction = new AbstractAction("О программе") {
            public void actionPerformed(ActionEvent event) {
                JLabel info = new JLabel("Гирель Анастасия, 6 группа");
                info.setHorizontalTextPosition(JLabel.CENTER);
                info.setVerticalTextPosition(JLabel.BOTTOM);
                info.setIconTextGap(10);
                JOptionPane.showMessageDialog(MainFrame.this, info, "О программе", JOptionPane.PLAIN_MESSAGE);
            }
        };
        infoMenuItem = infoMenu.add(aboutProgrammAction);
        textFieldFrom = new JTextField("0.0", 10);
        textFieldFrom.setMaximumSize(textFieldFrom.getPreferredSize());
        textFieldTo = new JTextField("1.0", 10);
        textFieldTo.setMaximumSize(textFieldTo.getPreferredSize());
        textFieldStep = new JTextField("0.1", 10);
        textFieldStep.setMaximumSize(textFieldStep.getPreferredSize());
        Box hboxXRange = Box.createHorizontalBox();
        hboxXRange.add(Box.createHorizontalGlue());
        hboxXRange.add(new JLabel("X изменяется на интервале от:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldFrom);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(new JLabel("до:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldTo);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(new JLabel("с шагом:"));
        hboxXRange.add(Box.createHorizontalStrut(10));
        hboxXRange.add(textFieldStep);
        hboxXRange.add(Box.createHorizontalStrut(20));
        hboxXRange.add(Box.createHorizontalGlue());
        hboxXRange.setPreferredSize(new Dimension((int)(hboxXRange.getMaximumSize().getWidth()),
                (int)(hboxXRange.getMinimumSize().getHeight()*1.5)));
        getContentPane().add(hboxXRange, BorderLayout.NORTH);
        JButton buttonCalc = new JButton("Вычислить");
        buttonCalc.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                try {
                    showColumnMenuItem.setState(true);
                    Double from = Double.parseDouble(textFieldFrom.getText());
                    Double to = Double.parseDouble(textFieldTo.getText());
                    Double step = Double.parseDouble(textFieldStep.getText());
                    data = new GornerTableModel(from, to, step, coefficients);
                    table = new JTable(data);
                    table.setDefaultRenderer(Double.class, renderer);
                    table.setRowHeight(30);
                    hBoxResult.removeAll();
                    hBoxResult.add(new JScrollPane(table));
                    hBoxResult.revalidate();
                }
                catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(MainFrame.this,
                            "Ошибка в формате записи числа с плавающей точкой",
                            "Ошибочный формат числа", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
        JButton buttonReset = new JButton("Очистить поля");
        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                textFieldFrom.setText("0.0");
                textFieldTo.setText("1.0");
                textFieldStep.setText("0.1");
                hBoxResult.removeAll();
                hBoxResult.repaint();
                data = null;
            }
        });
        Box hboxButtons = Box.createHorizontalBox();
        hboxButtons.setBorder(BorderFactory.createEtchedBorder());
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.add(buttonCalc);
        hboxButtons.add(Box.createHorizontalStrut(30));
        hboxButtons.add(buttonReset);
        hboxButtons.add(Box.createHorizontalGlue());
        hboxButtons.setPreferredSize(new Dimension((int)(hboxButtons.getMaximumSize().getWidth()),
                (int)(hboxButtons.getMinimumSize().getHeight() * 2)));
        getContentPane().add(hboxButtons, BorderLayout.SOUTH);
        hBoxResult = Box.createHorizontalBox();
        getContentPane().add(hBoxResult, BorderLayout.CENTER);
    }

    protected void saveToTextFile(File selectedFile) {// Создать  символьный поток вывода, направленный в указанный файл
        try{

            PrintStream out = new PrintStream(selectedFile); // Записать в поток вывода заголовочные сведения
            out.println("Результаты табулирования функции:");
            out.println("Интервал от " + data.getFrom() + " до " + data.getTo()+ " с шагом " +
                    data.getStep());

            for (int i = 0; i < data.getRowCount(); i++)// Записать в поток вывода значения в точках
            {
                out.println("Значение в точке " + data.getValueAt(i,0)  + " равно " + data.getValueAt(i,1));
            }
            out.close();
        } catch (FileNotFoundException e){
        }
    }

    public static void main(String[] args){
        if (args.length==0) {
            System.out.println("Невозможно табулировать многочлен, для которого не задано ни одного коэффициента!");
            System.exit(-1);
        }
        Double[] coefficients = new Double[args.length];
        int i = 0;
        try {
            for (String arg: args) {
                coefficients[i++] = Double.parseDouble(arg);
            }
        }
        catch (NumberFormatException ex) {
            System.out.println("Ошибка преобразования строки '" + args[i] +
                    "' в число типа Double");
            System.exit(-2);
        }
//
        MainFrame frame = new MainFrame(coefficients);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}