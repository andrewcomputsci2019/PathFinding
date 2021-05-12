package com.company;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        // write your code here
        GuiLogi logi;
        if (args.length>=2){
            try{
                int row = Integer.parseInt(args[0]);
                int col = Integer.parseInt(args[1]);
                logi=new GuiLogi(row,col);
            }catch (NumberFormatException e){
                System.err.println(e.getMessage());
                logi=new GuiLogi();

            }
        }else{
            logi=new GuiLogi();
        }
        logi.guiStarter();
    }

}
