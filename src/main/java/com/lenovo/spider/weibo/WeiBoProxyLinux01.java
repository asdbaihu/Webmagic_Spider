package com.lenovo.spider.weibo;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.Selectable;

/**
 *微博
 */
public class WeiBoProxyLinux01 implements PageProcessor {

   public static final String URL_POST = "http://blog\\.sina\\.com\\.cn/s/blog_\\w+\\.html";
   public static final String URL_LIST1 = "http://blog.sina.com.cn/lm/";
   public static final String URL_LIST2 = "http://blog\\.sina\\.com\\.cn/\\w{3,}";
   public static final String URL_LIST3 = "http://blog\\.sina\\.com\\.cn/s/articlelist_\\w+_0_\\d+\\.html";
   public static final String URL_LIST4 = "http://blog.sina.com.cn/lm/\\w+";

    private Site site = Site
            .me()
            .setDomain("blog.sina.com.cn01")
            .setUseGzip(true)
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("Upgrade-Insecure-Requests", "1")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36");

    @Override
    public void process(Page page) {
        Selectable selectUrl = page.getUrl();
        //首页
        if(selectUrl.regex(URL_LIST1).match()){
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST2).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST4).all());
         //   page.putField("content", page.getHtml().xpath("//*[@id=\"module_10001_SG_connBody\"]/div/div[3]/div/p/font/text()"));
        }
        //导航
        else if(selectUrl.regex(URL_LIST4).match()){
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST2).all());
        }
        //博客链接
        else if(selectUrl.regex(URL_LIST2).match()){
            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST2).all());
           // page.putField("content", page.getHtml().xpath("//div[@id=\"module_10001_SG_connBody\"]//div[@class='content newfont_family']/p/text()"));
            // page.addTargetRequests(page.getHtml().links().regex(URL_LIST3).all());
        }
        //列表页
//        else if(selectUrl.regex(URL_LIST3).match()){
//            page.addTargetRequests(page.getHtml().links().regex(URL_POST).all());
//            page.addTargetRequests(page.getHtml().links().regex(URL_LIST3).all());
//        }
        //文章页
        else if(selectUrl.regex(URL_POST).match()){
//            page.putField("date",
//                    page.getHtml().xpath("//div[@id='articlebody']//span[@class='time SG_txtc']").regex("\\((.*)\\)"));
//            page.putField("tag",
//                    page.getHtml().xpath("//div[@id='articlebody']//td[@class='blog_tag']/allText()"));
//            page.putField("class",
//                    page.getHtml().xpath("//div[@id='articlebody']//td[@class='blog_class']/allText()"));
//            page.putField("title", page.getHtml().xpath("//div[@class='articalTitle']/h2/text()"));
            page.putField("", page.getHtml().xpath("//div[@id='articlebody']//div[@class='articalContent']/tidyText()"));

        }
    }
    @Override
    public Site getSite() {
        return site;
    }
}