package com.mygubbi.common;

import java.util.Scanner;

/**
 * Created by Sunil on 21-05-2016.
 */
public class CurrencyUtil
{
    private static final String st1[] = {"Zero", "One", "Two", "Three", "Four", "Five", "Six",
            "Seven", "Eight", "Nine",};
    private static final String st2[] = {"Hundred", "Thousand", "Lac", "Crore"};
    private static final String st3[] = {"Ten", "Eleven", "Twelve", "Thirteen", "Fourteen",
            "Fifteen", "Sixteen", "Seventeen", "Eighteen", "Ninteen",};
    private static final String st4[] = {"Twenty", "Thirty", "Fourty", "Fifty", "Sixty", "Seventy",
            "Eighty", "Ninty"};

    private String string;

    public String convert(String amountInText)
    {
        String str2 = "";
        int rupees = Integer.parseInt(amountInText.split("\\.")[0]);
        String str1 = this.convert(rupees);
        str1 += " Rupees ";
        int paise = Integer.parseInt(amountInText.split("\\.")[1]);
        if (paise != 0)
        {
            str2 += " and";
            str2 = this.convert(paise);
            str2 += " Paise";
        }
        return (str1 + str2 + " Only");
    }

    private String convert(int number)
    {
        if (number == 0) return "Zero";

        int n = 1;
        int word;
        string = "";
        while (number != 0)
        {
            switch (n)
            {
                case 1:
                    word = number % 100;
                    pass(word);
                    if (number > 100 && number % 100 != 0)
                    {
                        show("and ");
                    }
                    number /= 100;
                    break;
                case 2:
                    word = number % 10;
                    if (word != 0)
                    {
                        show(" ");
                        show(st2[0]);
                        show(" ");
                        pass(word);
                    }
                    number /= 10;
                    break;
                case 3:
                    word = number % 100;
                    if (word != 0)
                    {
                        show(" ");
                        show(st2[1]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;
                case 4:
                    word = number % 100;
                    if (word != 0)
                    {
                        show(" ");
                        show(st2[2]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;
                case 5:
                    word = number % 100;
                    if (word != 0)
                    {
                        show(" ");
                        show(st2[3]);
                        show(" ");
                        pass(word);
                    }
                    number /= 100;
                    break;
            }
            n++;
        }
        return string;
    }


    public void pass(int number)
    {
        int word, q;
        if (number < 10)
        {
            show(st1[number]);
        }
        if (number > 9 && number < 20)
        {
            show(st3[number - 10]);
        }
        if (number > 19)
        {
            word = number % 10;
            if (word == 0)
            {
                q = number / 10;
                show(st4[q - 2]);
            }
            else
            {
                q = number / 10;
                show(st1[word]);
                show(" ");
                show(st4[q - 2]);
            }
        }
    }


    public void show(String s)
    {
        String st;
        st = string;
        string = s;
        string += st;
    }

    public static void main(String[] args)
    {
        convertAndPrint("123.45");
        convertAndPrint("10929.00");
        convertAndPrint("510929.00");
    }

    private static void convertAndPrint(String amount)
    {
        System.out.println(amount + " is " + new CurrencyUtil().convert(amount));
    }
}


