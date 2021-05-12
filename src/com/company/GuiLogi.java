package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;

public class GuiLogi implements ActionListener {
    PathFindingAlgo pathFindingTool= new PathFindingAlgo();
    JFrame frame;
    JPanel masterPanel;
    JButton[][] arrayOfbuttons;
    JButton startButton;
    private final int MaxRow;
    private final int MaxCol;
    private final int row;
    private final int col;
    public GuiLogi(){
        this.row=2;
        this.col=2;
        MaxRow= row*row;
        MaxCol = col*col;
    }
    public GuiLogi(int row, int col){
        this.row=row;
        this.col=col;
        MaxRow= row*row;
        MaxCol = col*col;
    }

    public void guiStarter(){
        frame = new JFrame();
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(row,col));
        mainPanel.setBorder(BorderFactory.createEmptyBorder());
        JPanel[][] miniPanels = new JPanel[row][col];
        for (int i=0; i< miniPanels.length; i++){
            for (int j=0; j<miniPanels[i].length;j++){
                miniPanels[i][j] = new JPanel(new GridLayout(row,col,1,1));
                miniPanels[i][j].setBackground(Color.black);
                miniPanels[i][j].setBorder(BorderFactory.createEmptyBorder(1,1,1,1));
                mainPanel.add(miniPanels[i][j]);
            }
        }
        arrayOfbuttons = new JButton[MaxRow][MaxCol];
        for (int row=0; row<MaxRow;row++){
            for(int col=0;col<MaxCol; col++){
                int i= row/this.row;
                int j= col/this.col;
                JButton temp = new JButton(String.format("[%d , %d]",row,col));
                temp.addMouseListener(new MouseListener() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        if (e.getButton()==MouseEvent.BUTTON1){
                        if (temp.getBackground().equals(Color.white)){
                            temp.setBackground(Color.black);
                        }else
                            temp.setBackground(Color.white);}
                        if (e.getButton() == 2 && onlySource()){
                            temp.setBackground(Color.green);
                        }
                        if (e.getButton() == 3 && onlyGoal()){
                            temp.setBackground(Color.CYAN);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {

                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {

                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                            temp.setBorder(BorderFactory.createLineBorder(Color.yellow,2,true));
                    }

                    @Override
                    public void mouseExited(MouseEvent e) {
                        temp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                    }
                });
                temp.setBackground(Color.white);
                arrayOfbuttons[row][col] = temp;
                miniPanels[i][j].add(temp);
            }
        }
        arrayOfbuttons[0][0].setBackground(Color.GREEN);
         startButton =  new JButton("Start");
        startButton.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                findPath();
                startButton.setEnabled(false);
                Timer timer = new Timer(1500, GuiLogi.this);
                timer.setRepeats(false);
                timer.start();

            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }

        });
         masterPanel = new JPanel(new BorderLayout());
        masterPanel.add(mainPanel,BorderLayout.CENTER);
        masterPanel.add(startButton,BorderLayout.PAGE_END);
        frame.add(masterPanel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public boolean onlyGoal(){

       for (JButton[] array: arrayOfbuttons){
           if(Arrays.stream(array).anyMatch(obj-> obj.getBackground().equals(Color.CYAN)))return false;
       }
        return true;
    }
    public boolean onlySource(){

        for (JButton[] array: arrayOfbuttons){
            if(Arrays.stream(array).anyMatch(obj-> obj.getBackground().equals(Color.GREEN)))return false;
        }
        return true;
    }
    private int value(Color color){
        if (Color.WHITE.equals(color)) {
            return 0; //nothing
        }else if (Color.BLACK.equals(color)){
            return -1;//blocked
        }else if (Color.GREEN.equals(color)){
            return 1;//start
        } else if (Color.CYAN.equals(color)){
            return 2;//goal
        }
        return 0;
    }

    private void findPath(){
        AbstractMap.SimpleEntry<Integer,Integer> startingPosition = new AbstractMap.SimpleEntry<>(null,null);
        int[][] convertedTable = new int[MaxRow][MaxCol];
        for (int i=0; i<convertedTable.length; i++){
            for (int j=0; j<convertedTable[i].length;j++){
               if (value(arrayOfbuttons[i][j].getBackground())==1){
                   convertedTable[i][j] = 1;
                   startingPosition = new AbstractMap.SimpleEntry<>(i,j);
               }else {
                   convertedTable[i][j] = value(arrayOfbuttons[i][j].getBackground());
               }
            }
        }
        if (startingPosition.getKey()==null||startingPosition.getValue()==null){
            JOptionPane.showMessageDialog(frame,"No starting Position Please middle mouse Click on a square","Warning",JOptionPane.INFORMATION_MESSAGE);
            System.out.println("No Starting Position please middle click on a Square");
            return;
        }
        int dist = pathFindingTool.pathFinding(convertedTable,startingPosition);
        if(dist !=-1){
            System.out.println(dist);
            JOptionPane.showMessageDialog(frame,"Least Path was " + dist);
            int counter =0;
            Object[] options = new Object[]{"next","back","exit Dialog"};
            while (counter < pathFindingTool.timeStamps.size()){
                int n = JOptionPane.showOptionDialog(frame,"Do you want to load the next steps","Steps "+(counter)+"/"+(pathFindingTool.timeStamps.size()-1),JOptionPane.YES_NO_CANCEL_OPTION,JOptionPane.QUESTION_MESSAGE,null,options,options[0]);
                if (n==JOptionPane.YES_OPTION){
                    paintButtons(counter,startingPosition);
                    counter++;
                }
                if (n==JOptionPane.NO_OPTION){
                    if (counter>1){
                        counter-=2;
                        paintButtons(counter,startingPosition);
                        counter++;
                    }
                }
                if (n==JOptionPane.CANCEL_OPTION){
                    break;
                }
            }
            resetButton();

        }else {
            JOptionPane.showMessageDialog(frame,"No path was found");
        }


    }
    private void paintButtons(int counter, AbstractMap.SimpleEntry<Integer,Integer> startingPosition){
        boolean[][] array = pathFindingTool.timeStamps.get(counter);
        for (int i=0; i<array.length; i++){
            for (int j=0;j < array[i].length;j++){
                if (array[i][j] && !(startingPosition.getValue()==i && startingPosition.getKey()==j)){
                    arrayOfbuttons[i][j].setBackground(Color.RED);
                }else if (!array[i][j] && !(arrayOfbuttons[i][j].getBackground().equals(Color.CYAN) || arrayOfbuttons[i][j].getBackground().equals(Color.black) || arrayOfbuttons[i][j].getBackground().equals(Color.GREEN))){
                    arrayOfbuttons[i][j].setBackground(Color.WHITE);
                }
            }
        }

    }
    private void resetButton(){
        for (JButton[] array:arrayOfbuttons) {
            for (JButton buttons:array) {
                buttons.setBackground(Color.white);
            }
        }
        arrayOfbuttons[0][0].setBackground(Color.GREEN);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        startButton.setEnabled(true);
    }
}
