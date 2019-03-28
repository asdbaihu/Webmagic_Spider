package com.lenovo.main;

import com.lenovo.common.ProxyProvider;
import com.lenovo.spider.jianshu.JianShuProxyLinux01;
import com.lenovo.spider.jianshu.JianShuProxyLinux02;
import com.lenovo.spider.jianshu.JianShuProxyLinux03;
import com.lenovo.spider.jianshu.JianShuProxyLinux04;
import com.lenovo.spider.weibo.WeiBoProxyLinux01;
import com.lenovo.spider.weibo.WeiBoProxyLinux02;
import com.lenovo.spider.xiushi.XiuShiProxyLinux;
import com.lenovo.spider.zhihu.ZhiHuProxyLinux01;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

/***
 * main方法启动类
 */
public class Start {

    public static void main(String[] args) {

        for (String arg : args) {
            //动态代理方法调用
            ProxyProvider httpClientDownloader = new ProxyProvider();
            HttpClientDownloader downloaderAndProxy = httpClientDownloader.createDownloaderAndProxy();

            Spider spider = null;
            //简书爬虫
            if (arg.equalsIgnoreCase("jianshu01")) {
                spider = Spider.create(new JianShuProxyLinux01())
                        //简书01链接入口
                        .addUrl("https://www.jianshu.com/p/bc6f9b0523fe?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation")
                        //文件存储路径
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //设置布隆过滤器进行去重
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url缓存路径
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }else if(arg.equalsIgnoreCase("jianshu02")){
                spider = Spider.create(new JianShuProxyLinux02())
                        //简书02链接入口
                        .addUrl("https://www.jianshu.com/u/2b1aecf85f2f?utm_campaign=maleskine&utm_content=user&utm_medium=seo_notes&utm_source=recommendation")
                        //文件存储路径
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //设置布隆过滤器进行去重
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url缓存路径
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }else if(arg.equalsIgnoreCase("jianshu03")){
                spider = Spider.create(new JianShuProxyLinux03())
                        //简书03链接入口
                        .addUrl("https://www.jianshu.com/p/b0bdf1a74cee?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation")
                        //文件存储路径
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //设置布隆过滤器进行去重
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url缓存路径
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }else if(arg.equalsIgnoreCase("jianshu04")){
                spider = Spider.create(new JianShuProxyLinux04())
                        //简书04链接入口
                        .addUrl("https://www.jianshu.com/p/4219abf4aaae?utm_campaign=maleskine&utm_content=note&utm_medium=seo_notes&utm_source=recommendation")
                        //文件存储路径
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //设置布隆过滤器进行去重
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url缓存路径
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }
            //微博爬虫
            else if(arg.equalsIgnoreCase("weibo01")){
                spider = Spider.create(new WeiBoProxyLinux01())
                        .addUrl("http://blog.sina.com.cn/guoxuebo")
                        .addPipeline(new FilePipeline("/home/shuju/weibo"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/weibo/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }else if(arg.equalsIgnoreCase("weibo02")){
                spider = Spider.create(new WeiBoProxyLinux02())
                        .addUrl("http://blog.sina.com.cn/u/1224900857")
                        .addPipeline(new FilePipeline("/home/shuju/weibo"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/weibo/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }
            //知乎爬虫
            else if(arg.equalsIgnoreCase("zhihu01")){
                //selenium的Linux配置路径
                System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");
                 spider = Spider.create(new ZhiHuProxyLinux01())
                        .addUrl("https://www.zhihu.com/question/315422907")
                        .addPipeline(new FilePipeline("/data/dynamic_spider/zhihu"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/zhihu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        //.setDownloader(new SeleniumDownloader("E:\\chromedriver_win32\\chromedriver.exe").setSleepTime(1000))
                        .setDownloader(new SeleniumDownloader())
                        .thread(5);
            }else if(arg.equalsIgnoreCase("zhihu02")){
                System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");
                spider = Spider.create(new ZhiHuProxyLinux01())
                        .addUrl("https://zhuanlan.zhihu.com/p/19702477")
                        .addPipeline(new FilePipeline("/data/dynamic_spider/zhihu"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/zhihu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        //.setDownloader(new SeleniumDownloader("E:\\chromedriver_win32\\chromedriver.exe").setSleepTime(1000))
                        .setDownloader(new SeleniumDownloader())
                        .thread(5);
            }
            //嗅事百科爬虫
            else if(arg.equalsIgnoreCase("xiushi01")){
                spider = Spider.create(new XiuShiProxyLinux())
                        .addUrl("https://www.qiushibaike.com/users/28192478/")
                        .addPipeline(new FilePipeline("/home/static_spider/xiushi"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/home/static_spider/xiushi/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }
            Scheduler scheduler = spider.getScheduler();
            spider.run();
        }
    }
}
