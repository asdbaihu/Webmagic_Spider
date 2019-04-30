//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.lenovo.common;

import java.io.Closeable;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map.Entry;

import com.lenovo.dynamic.SeleniumAction;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.interactions.Actions;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.Downloader;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.PlainText;

public class SeleniumDownloader implements Downloader, Closeable {
    private WebDriverPool webDriverPool=new WebDriverPool();
    private Logger logger = Logger.getLogger(this.getClass());
    private int sleepTime = 0;
    private SeleniumAction action=null;
    private int poolSize = 1;
    private static final String DRIVER_PHANTOMJS = "phantomjs";

    public SeleniumDownloader(String chromeDriverPath) {
        System.getProperties().setProperty("webdriver.chrome.driver", chromeDriverPath);
    }

    public SeleniumDownloader() {
    }

    public SeleniumDownloader(int sleepTime,WebDriverPool pool,SeleniumAction action){
        this.sleepTime=sleepTime;
        this.action=action;
        if(pool!=null){
            webDriverPool=pool;
        }
    }

    public SeleniumDownloader setSleepTime(int sleepTime) {
        this.sleepTime = sleepTime;
        return this;
    }
    public void setOperator(SeleniumAction action){
        this.action=action;
    }

    public Page download(Request request, Task task) {
        this.checkInit();

        WebDriver webDriver;
        try {
            webDriver = this.webDriverPool.get();
        } catch (InterruptedException var10) {
            this.logger.warn("interrupted", var10);
            return null;
        }

        this.logger.info("downloading page " + request.getUrl());
        webDriver.get(request.getUrl());

        try {
            Thread.sleep((long)this.sleepTime);
        } catch (InterruptedException var9) {
            var9.printStackTrace();
        }

        Options manage = webDriver.manage();
        Site site = task.getSite();
        if (site.getCookies() != null) {
            Iterator var6 = site.getCookies().entrySet().iterator();

            while(var6.hasNext()) {
                Entry<String, String> cookieEntry = (Entry)var6.next();
                Cookie cookie = new Cookie((String)cookieEntry.getKey(), (String)cookieEntry.getValue());
                manage.addCookie(cookie);
            }
        }
        //模拟执行浏览器组件操作
        manage.window().maximize();
        if(action!=null){
            action.execute(webDriver);
        }
        SeleniumAction reqAction=(SeleniumAction) request.getExtra("action");
        if(reqAction!=null){
            reqAction.execute(webDriver);
        }

//        String js = "";
//        for (int i=0; i < 5; i++){
//            System.out.println("休眠1s");
//            try {
//                //滚动到最底部
//                ((JavascriptExecutor)webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
//                //休眠，等待加载页面
//                Thread.sleep(2000);
//                //往回滚一点，否则不加载
//                ((JavascriptExecutor)webDriver).executeScript("window.scrollBy(0,-300)");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

//        Actions action = new Actions(webDriver);
////        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"block-D\"]/div/span[12]"));
////        action.click(element).perform();

//        WebElement link1 =webDriver.findElement(By.xpath("//a[@id='link1']"));
//        WebElement link2 = webDriver.findElement(By.xpath("//a[@id='link2']"));
//
//        new Actions(webDriver).moveToElement(link1).perform();
//
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            // TODO: handle exception
//            e.printStackTrace();
//        }
//
//        new Actions(webDriver).moveToElement(link2).perform();

        WebElement webElement = webDriver.findElement(By.xpath("/html"));
        String content = webElement.getAttribute("outerHTML");
        Page page = new Page();
        page.setRawText(content);
        page.setHtml(new Html(content, request.getUrl()));
        page.setUrl(new PlainText(request.getUrl()));
        page.setRequest(request);
        this.webDriverPool.returnToPool(webDriver);
        return page;
    }

    private void checkInit() {
        if (this.webDriverPool == null) {
            synchronized(this) {
                this.webDriverPool = new WebDriverPool(this.poolSize);
            }
        }

    }

    public void setThread(int thread) {
        this.poolSize = thread;
    }

    public void close() throws IOException {
        this.webDriverPool.closeAll();
    }
}
