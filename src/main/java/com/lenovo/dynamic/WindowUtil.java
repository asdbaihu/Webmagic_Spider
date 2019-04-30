package com.lenovo.dynamic;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class WindowUtil {

    /**
     * �������ڡ�
     * @param driver
     * @param height
     */
    public static void scroll(WebDriver driver,int height){
        ((JavascriptExecutor)driver).executeScript("window.scrollTo(0,"+height+" );");
    }
    /**
     * ���µ������ڴ�С������Ӧҳ�棬��Ҫ�ķ�һ��ʱ�䡣����ȴ������ʱ�䡣
     * @param driver
     */
    public static void loadAll(WebDriver driver){
        Dimension od=driver.manage().window().getSize();
        int width=driver.manage().window().getSize().width;
        //�����Խ����https://github.com/ariya/phantomjs/issues/11526����
        driver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        long height=(Long)((JavascriptExecutor)driver).executeScript("return document.body.scrollHeight;");
        driver.manage().window().setSize(new Dimension(width, (int)height));
        driver.navigate().refresh();
    }
    public static void taskScreenShot(WebDriver driver,File saveFile){
        File src=((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(src, saveFile);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void changeWindow(WebDriver driver){
        // ��ȡ��ǰҳ����
        String handle = driver.getWindowHandle();
        // ��ȡ����ҳ��ľ������ѭ���жϲ��ǵ�ǰ�ľ��������ѡȡswitchTo()
        for (String handles : driver.getWindowHandles()) {
            if (handles.equals(handle))
                continue;
            driver.switchTo().window(handles);
        }
    }
}
