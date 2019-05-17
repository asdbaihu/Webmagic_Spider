//package com.lenovo.common;
//
//import org.apache.log4j.Logger;
//import org.openqa.selenium.*;
//import org.openqa.selenium.interactions.Actions;
//import us.codecraft.webmagic.Page;
//import us.codecraft.webmagic.Request;
//import us.codecraft.webmagic.Site;
//import us.codecraft.webmagic.Task;
//import us.codecraft.webmagic.downloader.Downloader;
//import us.codecraft.webmagic.selector.PlainText;
//
//import java.io.Closeable;
//import java.io.IOException;
//import java.util.Map;
//
///**
// * 使用Selenium调用浏览器进行渲染。目前仅支持chrome。<br>
// * 需要下载Selenium driver支持。<br>
// *
// * Selenium加载器
// */
//public class SeleniumDownloader02 implements Downloader, Closeable {
//
//    private volatile WebDriverPool webDriverPool;
//
//    private Logger logger = Logger.getLogger(getClass());
//
//    private int sleepTime = 0;
//
//    private int poolSize = 1;
//
//    private static final String DRIVER_PHANTOMJS = "phantomjs";
//
//    /**
//     * 新建
//     *
//     * @param chromeDriverPath chromeDriverPath
//     */
//    public SeleniumDownloader02(String chromeDriverPath) {
//        System.getProperties().setProperty("webdriver.chrome.driver",
//                chromeDriverPath);
//    }
//
//    /**
//     * Constructor without any filed. Construct PhantomJS browser
//     *
//     * @author bob.li.0718@gmail.com
//     */
//    public SeleniumDownloader02() {
//        // System.setProperty("phantomjs.binary.path",
//        // "/Users/Bingo/Downloads/phantomjs-1.9.7-macosx/bin/phantomjs");
//    }
//
//    /**
//     * set sleep time to wait until load success
//     *
//     * @param sleepTime sleepTime
//     * @return this
//     */
//    public SeleniumDownloader02 setSleepTime(int sleepTime) {
//        this.sleepTime = sleepTime;
//        return this;
//    }
//
//    @Override
//    public Page download(Request request, Task task) {
//        checkInit();
//        WebDriver webDriver;
//        try {
//            webDriver = webDriverPool.get();
//        } catch (InterruptedException e) {
//            logger.warn("interrupted", e);
//            return null;
//        }
//        logger.info("downloading page " + request.getUrl());
//        webDriver.get(request.getUrl());
//        try {
//            Thread.sleep(sleepTime);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        WebDriver.Options manage = webDriver.manage();
//        Site site = task.getSite();
//        if (site.getCookies() != null) {
//            for (Map.Entry<String, String> cookieEntry : site.getCookies()
//                    .entrySet()) {
//                Cookie cookie = new Cookie(cookieEntry.getKey(),
//                        cookieEntry.getValue());
//                manage.addCookie(cookie);
//            }
//        }
//
//        /*
//         * TODO You can add mouse event or other processes
//         *
//         * @author: bob.li.0718@gmail.com
//         */
////        String js = "";
////        for (int i=0; i < 20; i++){
////            System.out.println("休眠1s");
////            try {
////                //滚动到最底部
////                ((JavascriptExecutor)webDriver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
////                //休眠，等待加载页面
////                Thread.sleep(2000);
////                //往回滚一点，否则不加载
////                ((JavascriptExecutor)webDriver).executeScript("window.scrollBy(0,-300)");
////            } catch (InterruptedException e) {
////                e.printStackTrace();
////            }
////        }
//        Actions action = new Actions(webDriver);
//        WebElement element = webDriver.findElement(By.xpath("//*[@id=\"block-D\"]/div/span[12]"));
//        action.contextClick(element).perform();
//
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
//
//        WebElement webElement = webDriver.findElement(By.xpath("/html"));
//        String content = webElement.getAttribute("outerHTML");
//        Page page = new Page();
//        page.setRawText(content);
////        page.setHtml(new Html(content, request.getUrl()));
//        page.setUrl(new PlainText(request.getUrl()));
//        page.setRequest(request);
//
//        webDriverPool.returnToPool(webDriver);
//
//        return page;
//    }
//
//    private void checkInit() {
//        if (webDriverPool == null) {
//            synchronized (this) {
//                webDriverPool = new WebDriverPool(poolSize);
//            }
//        }
//    }
//
//    @Override
//    public void setThread(int thread) {
//        this.poolSize = thread;
//    }
//
//    @Override
//    public void close() throws IOException {
//        webDriverPool.closeAll();
//    }
//}
//
//
