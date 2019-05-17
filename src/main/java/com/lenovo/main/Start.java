package com.lenovo.main;

import com.lenovo.common.FileCacheQueueScheduler;
import com.lenovo.common.ProxyProvider;
import com.lenovo.spider.jianshu.*;
import com.lenovo.spider.weibo.WeiBoProxyLinux01;
import com.lenovo.spider.weibo.WeiBoProxyLinux02;
import com.lenovo.spider.xiushi.XiuShiProxyLinux;
import com.lenovo.spider.zhihu.ZhiHuProxyLinux01;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;

/***
 * main����������
 */
public class Start {

    public static void main(String[] args) {

        for (String arg : args) {
            //��̬����������
            ProxyProvider httpClientDownloader = new ProxyProvider();
            HttpClientDownloader downloaderAndProxy = httpClientDownloader.createDownloaderAndProxy();

            Spider spider = null;
            //��������
            if (arg.equalsIgnoreCase("jianshu01")) {
                spider = Spider.create(new JianShuProxyLinux01())
                        //����01�������
                        .addUrl("https://www.jianshu.com/p/1804d4507fbe")
                        //�ļ��洢·��
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //���ò�¡����������ȥ��
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url����·��
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(10);
            }else if(arg.equalsIgnoreCase("jianshu02")){
                spider = Spider.create(new JianShuProxyLinux02())
                        //����02�������
                        .addUrl("https://www.jianshu.com/p/3b37d010f0f8")
                        //�ļ��洢·��
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //���ò�¡����������ȥ��
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url����·��
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(10);
            }else if(arg.equalsIgnoreCase("jianshu03")){
                spider = Spider.create(new JianShuProxyLinux03())
                        //����03�������
                        .addUrl("https://www.jianshu.com/p/a780114c5d7c")
                        //�ļ��洢·��
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //���ò�¡����������ȥ��
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url����·��
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(10);
            }else if(arg.equalsIgnoreCase("jianshu04")){
                spider = Spider.create(new JianShuProxyLinux04())

                        //����04�������
                        .addUrl("https://www.jianshu.com/p/c459b109d973")
                        //�ļ��洢·��
                        .addPipeline(new FilePipeline("/home/shuju/jianshu"))
                        //���ò�¡����������ȥ��
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url����·��
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(10);
            }else if(arg.equalsIgnoreCase("jianshu05")){
                spider = Spider.create(new JianShuProxyLinux05())

                        //����05�������
                        .addUrl("https://www.jianshu.com/p/8cfedba1affe")
                        //�ļ��洢·��
                        .addPipeline(new FilePipeline("/data/dynamic_spider/jianshu"))
                        //���ò�¡����������ȥ��
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url����·��
                        .setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(15);
            }else if(arg.equalsIgnoreCase("jianshu06")){
                spider = Spider.create(new JianShuProxyLinux06())

                        //����06�������
                        .addUrl("https://www.jianshu.com/p/6d5fff029a68")
                        //�ļ��洢·��
                        .addPipeline(new FilePipeline("/data/dynamic_spider/jianshu"))
                        //���ò�¡����������ȥ��
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        //url����·��
                        .setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/jianshu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(15);
            }
            //΢������
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
                        //.addUrl("http://blog.sina.com.cn/u/1224900857")
                        //.addUrl("http://blog.sina.com.cn/u/1255589934")
                        //.addUrl("http://blog.sina.com.cn/u/1945326343")
                        //.addUrl("http://blog.sina.com.cn/u/5892736543")
                        .addUrl("http://blog.sina.com.cn/u/1686546714")
                        .addPipeline(new FilePipeline("/home/shuju/weibo"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/home/shuju/weibo/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        .thread(20);
            }
            //֪������
            else if(arg.equalsIgnoreCase("zhihu01")){
                //selenium��Linux����·��
                System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");
                 spider = Spider.create(new ZhiHuProxyLinux01())
                        .addUrl("https://www.zhihu.com/question/311489634")
                        .addPipeline(new FilePipeline("/data/dynamic_spider/zhihu"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/zhihu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        //.setDownloader(new SeleniumDownloader02("E:\\chromedriver_win32\\chromedriver.exe").setSleepTime(1000))
                        .setDownloader(new SeleniumDownloader())
                        .thread(5);
            }else if(arg.equalsIgnoreCase("zhihu02")){
                System.setProperty("selenuim_config", "/shuju/jar/selenium.properties");
                spider = Spider.create(new ZhiHuProxyLinux01())
                        .addUrl("https://www.zhihu.com/question/310938068")
                        .addPipeline(new FilePipeline("/data/dynamic_spider/zhihu"))
                        .setScheduler(new QueueScheduler().setDuplicateRemover(
                                new BloomFilterDuplicateRemover(10000)))
                        .setScheduler(new FileCacheQueueScheduler("/data/dynamic_spider/zhihu/urlfile"))
                        .setDownloader(downloaderAndProxy)
                        //.setDownloader(new SeleniumDownloader02("E:\\chromedriver_win32\\chromedriver.exe").setSleepTime(1000))
                        .setDownloader(new SeleniumDownloader())
                        .thread(5);
            }
            //���°ٿ�����
            else if(arg.equalsIgnoreCase("xiushi01")){
                spider = Spider.create(new XiuShiProxyLinux())
                        .addUrl("https://www.qiushibaike.com/users/20053149/")
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
