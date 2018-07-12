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
                    BufferedImage imgRead = null;
                    try
                    {
                        imgRead = ImageIO.read(new File(nameOfFile));

                    }catch (IOException e)
                    {
                        System.out.println("Error of file.");
                    }
                    ImageBMP photo = new ImageBMP(imgRead);
                    System.out.println(photo.decode());
                    break;
                case 2:
//                    System.out.println("Give the name of the file you want to create.");
//                    nameOfFile = reader.nextLine();
                    String textToEncode = "HEJKA";
                    int barWidth = 4;
                    BufferedImage imgWrite = new BufferedImage(600,50,BufferedImage.TYPE_INT_RGB);
                    ImageBMP photoWrite = new ImageBMP(imgWrite, textToEncode, barWidth);

                    try
                    {
                        File f = new File("output.bmp");
                        ImageIO.write(photoWrite.returnPhoto(),"bmp",f);
                    }catch (IOException e)
                    {
                        System.out.println("Error of file.");
                    }


                    break;
                case 0:
                    System.out.println("Goodbye!");

            }
        }



    }
}
