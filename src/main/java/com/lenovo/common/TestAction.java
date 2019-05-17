//package com.lenovo.common;
//
//import com.lenovo.dynamic.SeleniumAction;
//import com.lenovo.dynamic.WindowUtil;
//import org.openqa.selenium.By;
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.WebElement;
//import org.openqa.selenium.interactions.Actions;
//import org.openqa.selenium.support.ui.WebDriverWait;
//
///**
// * @author XiuChong@lenovo.com
// * @data 2019/5/5 10:54
// */
//public class TestAction implements SeleniumAction {
//    @Override
//    public void execute(WebDriver driver) {
//        WebDriverWait wait = new WebDriverWait(driver, 10);
//
//        String url = driver.getCurrentUrl();
//        WindowUtil.loadAll(driver);
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        Actions action = new Actions(driver);
//        WebElement element = driver.findElement(By.xpath("//*[@id=\"block-D\"]/div/span[12]"));
//        action.click(element).perform();
//        try {
//            Thread.sleep(5000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
