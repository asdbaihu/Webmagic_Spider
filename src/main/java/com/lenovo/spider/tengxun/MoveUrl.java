package com.lenovo.spider.tengxun;

import com.lenovo.common.ProxyProvider;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.pipeline.FilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.FileCacheQueueScheduler;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.scheduler.Scheduler;
import us.codecraft.webmagic.selector.JsonPathSelector;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Collections;
import java.util.List;

/**
 *��Ѷ��Ӱurl����ץȡ
 */
public class MoveUrl implements PageProcessor {

    public static final String URL_LIST = "https://v.qq.com/channel/movie?listpage=1&channel=movie&sort=18&_all=1";
    public static final String URL_POST = "https://v.qq.com/x/cover/\\w+\\.html";

    private Site site = Site
            .me()
            .setDomain("www.tengxun.move.com")
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

        //�б�ҳ
        //if(selectUrl.regex(URL_LIST).match()){
            List<String> itemlist = page.getHtml().links().regex(URL_POST).all();
            if(!itemlist.isEmpty()) {
                for (String item : itemlist) {
                    page.addTargetRequests(Collections.singletonList(item));
                }
            }
        //}
        //����ҳ
         if(selectUrl.regex(URL_POST).match()) {

            page.putField("��Դ", page.getHtml().css("#nav_logo > a.logo-sprite.logo-link-nonIndex","title"));
            page.putField("��Ӱ����", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[1]/h3/tidyText()"));
            page.putField("��Ŀ����", page.getHtml().xpath("//*[@id=\"nav_logo\"]/a[2]/h2/tidyText()"));
            page.putField("��ӳ����", page.getHtml().css("#titleRow > div.intro-mn > div > div.qy-player-tag > a:nth-child(1)","title"));
            page.putField("��������", page.getHtml().css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_cTop > ul > li:nth-child(4) > span > a","title"));
            page.putField("���", page.getHtml().xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[5]/span/tidyText()"));
            page.putField("����", page.getHtml().css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_cTop > ul > li:nth-child(2) > span > a","title"));
            page.putField("����", page.getHtml().css("#comment > div.qy-comment-page > div > div.qycp-bd > div.csPpCircle_relatedWrap > div > div > ul > li:nth-child(2) > div.related_item_tit > p > span","text"));
            page.putField("����2", page.getHtml().css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_startBox.vInfoSide_startBox.start_name_pr > div > div > div.start_cardInner > div.start_cardT > p > a","title"));
           // page.putField("����", page.getHtml().css("#block-C > div.qy-player-detail-pop > div.qy-player-intro-pop > div.intro-right > ul > li:nth-child(2) > span > span:nth-child(1) > a","title"));
            page.putField("��Ա", page.getHtml().css("#comment > div.qy-comment-page > div > div.qycp-bd > div.csPpCircle_relatedWrap > div > div > ul > li:nth-child(3) > div.related_item_tit > p > span","text"));
            page.putField("���", page.getHtml().css("#block-F > div.qy-play-role-tab.j_people_wrap > ul > li.role-item.j_people_item.selected > div > div.role-con > h3 > a","title"));
            page.putField("�ȶ�", page.getHtml().css("#block-F > div.qy-play-role-tab.j_people_wrap > ul > li.role-item.j_people_item.selected > div > div.role-con > h3 > a","title"));
            page.putField("������", page.getHtml().css("#block-F > div.qy-play-role-tab.j_people_wrap > ul > li.role-item.j_people_item.selected > div > div.role-con > h3 > a","title"));
            page.putField("����", page.getHtml().css("#block-C > div.qy-player-detail > div > div > div > div > div.qy-player-title.title-score > article > div.score-top > span.score-new","text"));
            page.putField("����", page.getHtml().css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_cTop > ul > li:nth-child(3) > span > a","title"));

            //String s = new JsonPathSelector("$..description").select(page.getRawText());
            //page.putField("content", s);
           // page.putField("content", page.getJson().removePadding("callback").jsonPath("$..data.description"));

            //������ҳ���Ӽ��뵽������
//            List<String> itemlist = page.getHtml().links().regex(URL_POST).all();
//            if(!itemlist.isEmpty()) {
//                for (String item : itemlist) {
//                    page.addTargetRequests(Collections.singletonList(item));
//                }
//            }
        }
    }

    @Override
    public Site getSite() {
        return site;
    }

    public static void main(String[] args) {

        System.setProperty("selenuim_config", "D:\\develop\\code\\Demo\\src\\main\\resources\\selenium_test.properties");

        //��̬����������
        ProxyProvider httpClientDownloader = new ProxyProvider();
        HttpClientDownloader downloaderAndProxy = httpClientDownloader.createDownloaderAndProxy();

        Spider spider = Spider.create(new MoveUrl())
                .addUrl("https://v.qq.com/channel/movie?listpage=1&channel=movie&sort=18&_all=1")
                .addPipeline(new FilePipeline("D:\\data"))
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setScheduler(new FileCacheQueueScheduler("D:\\data\\urlfile"))
                 .setDownloader(downloaderAndProxy)
                .setDownloader(new SeleniumDownloader())
                .thread(2);

        Scheduler scheduler = spider.getScheduler();
        spider.run();
    }
}