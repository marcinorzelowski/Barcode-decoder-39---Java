package com.orzelowski;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args)
    {
        Scanner reader = new Scanner(System.in);
        int whatToDo = 1;
        String nameOfFile;
        while(whatToDo!=0)
        {
            System.out.println("What you want to do? \n 1. Decode Bitmap \n 2. Encode Bitmap \n 0. Exit \n Please press a key to choose option. ");
            whatToDo = Integer.parseInt(reader.nextLine());
            switch (whatToDo)
            {
                case 1:
                    System.out.println("Give the name of the file to encode.");
                    nameOfFile = reader.nextLine();
                    BufferedImage img = null;
                    try
                    {
                        img = ImageIO.read(new File(nameOfFile));
                    }catch (IOException e)
                    {
                        System.out.println("Error of file.");
                    }
                    ImageBMP photo = new ImageBMP(img);
                    System.out.println(photo.decode());
                    break;
                case 2:
                    break;
                case 0:
                    System.out.println("Goodbye!");

            }
        }



    }
}
