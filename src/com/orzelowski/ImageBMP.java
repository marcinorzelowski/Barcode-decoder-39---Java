package com.orzelowski;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


private class ImageBMP
{

    private BufferedImage imageBuffer;
    private String text;
    private int width;
    private int height;
    private int checkedLine;
    private int barWidth;
    private int actualPositionX;
    private int[] codes = new int[50];
    private char[] symbols = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F','G','H','I','J','K','L','M','N','O','P','Q','R','S','T','U','V','W','X','Y','Z','-','.',' ','$','/','+','%','*'};
    private int[] codeTable = {111221211,  211211112, 112211112,  212211111, 111221112, 211221111, 112221111, 111211212, 211211211, 112211211, 211112112, 112112112, 212112111, 111122112,
            211122111,  112122111, 112122111,  211112211, 112112211, 111122211, 211111122, 112111122, 212111121, 111121122, 211121121, 112121121,
            111111222,  211111221, 112111221,  111121221, 221111112, 122111112, 222111111, 121121112, 221121111, 122121111, 121111212, 221111211,
            122111211,  121212111, 121211121,  121112121, 111212121, 121121211};
    public ImageBMP(BufferedImage img)
    {
        this.imageBuffer = img;

            this.width = img.getWidth();
            this.height = img.getHeight();
            this.checkedLine = 25;
            this.barWidth = setBarWidth();
            this.actualPositionX = getStartingPosition();


    }

    private ImageBMP(BufferedImage img, String textToEncode, int barWidth)
    {
        this.imageBuffer = img;
        this.barWidth = barWidth;
        this.width = img.getWidth();
        this.height = img.getHeight();
        this.text = textToEncode;
        actualPositionX = 10;
        toWhite();
    }

    private int getCheckedLine() {
        return checkedLine;
    }

    private int getWidth() {
        return width;
    }

    private int getHeight() {
        return height;
    }

    private int getBarWidth() {return barWidth; }

    private int getStartingPosition() //returning starting position of black bars;
    {
        int position = 0;
        for(int i = 0; i < getWidth(); i++)
        {
            if((byte)(imageBuffer.getRGB(i,getCheckedLine()))==0) {
                position = i;
                System.out.println(position);
                break;
            }

        }


        return position;
    }

    private int setBarWidth() //returning width of the thin bar;
    {
        int counter = 0;
        for(int i = 0; i < getWidth() - getStartingPosition();i++)
        {
            if((byte)(imageBuffer.getRGB(getStartingPosition()+i,getCheckedLine()))==0)
                counter++;
            else
                break;
        }
        return counter;
    }

    public int getSingleSymbolCode()
    {
        int symbolCode = 0;

        int counter = 0;

        while(counter < 9 && actualPositionX < width)
        {
            if(getLengthOfBar()==barWidth)
                symbolCode = symbolCode*10+1;
            else
                symbolCode = symbolCode*10+2;


            counter++;
        }
        return symbolCode;
    }
    private int getLengthOfBar()
    {
        int barLenght = 0;
        int blackOrWhite = (byte)imageBuffer.getRGB(actualPositionX,checkedLine);
        while(actualPositionX != width && blackOrWhite == (byte)imageBuffer.getRGB(actualPositionX,checkedLine))
        {
            barLenght++;
            actualPositionX++;
        }
        return barLenght;
    }

    private void skipToBlack()
    {
        while(actualPositionX!=width && ((byte)imageBuffer.getRGB(actualPositionX,checkedLine)!=0))
        {
            actualPositionX++;
        }
    }

    private void decodeNumbers()
    {
        int tmp; //holding single symbol;
        int i = 0; //iterating through the table of codes;
        while(actualPositionX<width)
        {
            tmp = getSingleSymbolCode();
            codes[i] = tmp;
            skipToBlack();
            i++;
        }

    }

    public String decode() {
        String encodedText = "";
        int i = 0;
        decodeNumbers();

        while (codes[i] != 0) {

            for (int j = 0; j < codeTable.length; j++) {
                if (codes[i] == codeTable[j]) {
                    encodedText += symbols[j];
                }

            }
            i++;
        }
        return encodedText;
    }

    private void toWhite()//painting whole picture for a white color
    {
        Color MyWhite = new Color(255,255,255);
        for(int y = 0; y < getHeight(); y++)
        {
            for(int x = 0; x < getWidth(); x++){
                imageBuffer.setRGB(x, y, MyWhite.getRGB());
            }
        }
    }
    public BufferedImage returnPhoto()
    {
        for(int i = 0; i < this.text.length(); i++){
            paintSymbol(this.text.charAt(i));
        }
        return imageBuffer;
    }

    private void paintBlackBar(boolean ifFat, int xCoordinate)// if fat tells size of a bar (small of big)
    {
        Color myBlack = new Color(1,1,1);
        int widthOfColoredBar;
        if(ifFat==true)
            widthOfColoredBar = 2*getBarWidth();
        else
            widthOfColoredBar = getBarWidth();
        for(int x = 0; x < widthOfColoredBar; x++){
            for(int y = 0; y < getHeight(); y++){
                imageBuffer.setRGB(xCoordinate,y,myBlack.getRGB());
        }
            xCoordinate++;
        }
    }

    private int findSymbolIndex(char symbol)
    {
        int i = 0;
        while(i<symbols.length)
        {
            if(symbol==symbols[i])
            {
                return i;
            }
            i++;
        }
        return 100;
    }
    private void paintSymbol(char symbolToEncode)
    {

        int index = findSymbolIndex(symbolToEncode);
        if(index!=100)
        {
            String codeInString = Integer.toString(codeTable[index]);
            for(int i = 0; i < codeInString.length();i++)
            {
                System.out.println("12");
                char s = codeInString.charAt(i);
                if(i%2==0)
                {
                    if(s=='2')
                    {
                        paintBlackBar(true, actualPositionX);
                        actualPositionX = actualPositionX+(2*barWidth);
                    }else{
                        paintBlackBar(false,actualPositionX);
                        actualPositionX += barWidth;
                    }
                }else{
                    if(s=='2')
                        actualPositionX+=(2*barWidth);
                    else
                        actualPositionX+=barWidth;
                }

            }
        }
    }


}


