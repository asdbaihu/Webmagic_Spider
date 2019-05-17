package com.lenovo.spider.aiqiyi;

import com.lenovo.common.FileCacheQueueScheduler;
import com.lenovo.common.ProxyProvider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Collections;
import java.util.List;

/**
 *爱奇艺电视剧资料抓取
 */
public class Teleplay implements PageProcessor {

    public static final String URL_LIST = "https://list.iqiyi.com/www/2/-------------24-\\d+\\-1-iqiyi--.html";
    public static final String URL_POST = "https://www.iqiyi.com/a_\\w+\\.html";

    private Site site = Site
            .me()
            .setDomain("www.iqiyi.teleplay.com")
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

        //列表页
        if(selectUrl.regex(URL_LIST).match()){
            page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all());
            List<String> itemlist = page.getHtml().links().regex(URL_POST).all();
            if(!itemlist.isEmpty()) {
                for (String item : itemlist) {
                    page.addTargetRequests(Collections.singletonList(item));
                }
            }
        }
        //文章页
        else if(selectUrl.regex(URL_POST).match()) {

            page.putField("来源", page.getHtml().css("#oldNavCtrlElem > div > div > div.topNavLt > div.topLogo.caihong > a > img","title"));
            page.putField("电视剧名称", page.getHtml().css("#block-BB > div > div.album-head-info > div > h1 > a:nth-child(1)","title"));
            page.putField("节目类型", page.getHtml().xpath("//*[@id=\"oldNavCtrlElem\"]/div/div/div[1]/div[2]/a/h2/tidyText()"));
            page.putField("上映类型", page.getHtml().xpath("//*[@id=\"block-BB\"]/div/div[1]/div/div[3]/div[1]/div[2]/p[3]/em/tidyText()"));
            page.putField("内容类型", page.getHtml().css("#block-BB > div > div.album-head-info > div > div.episodeIntro > div.episodeIntro-item.clearfix > div:nth-child(2) > p.episodeIntro-type > a:nth-child(2)","text"));
            page.putField("年代", page.getHtml().xpath("//*[@id=\"block-BB\"]/div/div[1]/div/div[3]/div[1]/div[2]/p[4]/a/tidyText()"));
            page.putField("语言", page.getHtml().css("#block-BB > div > div.album-head-info > div > div.episodeIntro > div.episodeIntro-item.clearfix > div:nth-child(2) > p:nth-child(2) > a","text"));
            page.putField("地区", page.getHtml().css("#block-BB > div > div.album-head-info > div > div.episodeIntro > div.episodeIntro-item.clearfix > div:nth-child(1) > p.episodeIntro-area > a","text"));
            page.putField("导演", page.getHtml().css("#block-BB > div > div.album-head-info > div > div.episodeIntro > div.episodeIntro-item.clearfix > div:nth-child(1) > p.episodeIntro-director > a","text"));
            page.putField("主演", page.getHtml().css("#block-E > div:nth-child(2) > ul > li:nth-child(1) > div.headImg-info.fl > p.headImg-name > a","title"));
           // page.putField("主演2", page.getHtml().css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_startBox.vInfoSide_startBox.start_name_pr > div > div > div.start_cardInner > div.start_cardT > p > a","title"));
           // page.putField("主演", page.getHtml().css("#block-C > div.qy-player-detail-pop > div.qy-player-intro-pop > div.intro-right > ul > li:nth-child(2) > span > span:nth-child(1) > a","title"));
            page.putField("演员", page.getHtml().css("#block-E > div:nth-child(2) > ul > li:nth-child(2) > div.headImg-info.fl > p.headImg-name > a","title"));
            page.putField("简介", page.getHtml().xpath("//*[@id=\"block-BB\"]/div/div[1]/div/div[3]/div[2]/span/text()"));
            page.putField("热度", page.getHtml().xpath("//*[@id=\"block-BB\"]/div/div[1]/div/div[2]/span[3]/text()"));
            //page.putField("评分数", page.getHtml().css("#block-F > div.qy-play-role-tab.j_people_wrap > ul > li.role-item.j_people_item.selected > div > div.role-con > h3 > a","title"));
            //page.putField("评分", page.getHtml().css("//*[@id=\"movie-score-show\"]/span/i[1]/tidyText()"));
            page.putField("集数", page.getHtml().css("#widget-tab-3 > div.piclist-wrapper > div > ul > li:nth-child(3)","data-order"));
            //page.putField("链接", page.getUrl().links().toString());


           // String s = new JsonPathSelector("$..description").select(page.getRawText());
            //page.putField("content", s);
           // page.putField("content", page.getJson().removePadding("callback").jsonPath("$..data.description"));

            //将文章页链接加入到集合中
            List<String> itemlist = page.getHtml().links().regex(URL_POST).all();
            if(!itemlist.isEmpty()) {
                for (String item : itemlist) {
                    page.addTargetRequests(Collections.singletonList(item));
                }
            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        System.setProperty("selenuim_config", "D:\\develop\\code\\Demo\\src\\main\\resources\\selenium_test.properties");

        //动态代理方法调用
        ProxyProvider httpClientDownloader = new ProxyProvider();
        HttpClientDownloader downloaderAndProxy = httpClientDownloader.createDownloaderAndProxy();

        Spider spider = Spider.create(new Teleplay())
                .addUrl("https://list.iqiyi.com/www/2/-------------24-4-1-iqiyi--.html")
                .addPipeline(new FilePipeline("D:\\jianshu"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setScheduler(new FileCacheQueueScheduler("D:\\jianshu\\urlfile"))
                 .setDownloader(downloaderAndProxy)
                .setDownloader(new SeleniumDownloader())
                .thread(1);

        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}