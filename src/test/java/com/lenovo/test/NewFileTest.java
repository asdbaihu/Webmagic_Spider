package com.lenovo.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NewFileTest {

    public static void main(String[] args) {

         Date date = new Date();
         String dataForm = new SimpleDateFormat("yyyy-MM-dd").format(date);
         String path = "E:\\" + dataForm;
        // ���������,�����ļ���
        File f = new File(path);

        if(!f.exists())
        {
            f.mkdirs();
        }
        FileOutputStream out = null;
        // �����ļ���
        String fileName = "\\blk_daily_data_" + dataForm + ".txt";
        try {
            out =new FileOutputStream(path +fileName);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
