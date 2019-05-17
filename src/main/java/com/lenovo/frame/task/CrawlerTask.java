package com.lenovo.frame.task;

import com.lenovo.common.ProxyProvider;
import com.lenovo.frame.domain.AiqiyiMovie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.downloader.selenium.SeleniumDownloader;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.Collections;
import java.util.List;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 14:13
 * @description 利用WebMagic进行数据爬取
 */
@Component
public class CrawlerTask implements PageProcessor {

    @Autowired
    private CrawlerPipeline CrawlerPipeline;

    public static final String URL_LIST = "https://list.iqiyi.com/www/1/-------------24-\\d+\\-1-iqiyi--.html";
    public static final String URL_POST = "https://www.iqiyi.com/v_\\w+\\.html";

    private Site site = Site
            .me()
            //.setDomain("www.iqiyi.movie.com")
            .setUseGzip(true)
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Cache-Control", "max-age=0")
            .addHeader("Upgrade-Insecure-Requests", "1")
            .setSleepTime(3000)
            .setUserAgent(
                    "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.81 Safari/537.36");


    /***
     * 解析和处理爬取结果
     * @param page
     */
    @Override
    public void process(Page page) {
        //获取当前页url元素
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
            // 当前是详情页
            // 解析详情页，获取想要的数据，然后将结果存入Pipeline
            getItem(page);
        }
    }

    /***
     * 解析详情页，获取想要的数据，然后将结果存入Pipeline
     * @return
     */
    private void getItem(Page page) {
        //通过page获取 Html文档对象
        Html html = page.getHtml();
        //需要解析的内容
        String source = html.css("#nav_logo > a.logo-sprite.logo-link-nonIndex", "title").toString();
        String name = html.xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[1]/h3/tidyText()").toString();
        String pty = html.xpath("//*[@id=\"nav_logo\"]/a[2]/h2/tidyText()").toString();
        String rty = html.css("#titleRow > div.intro-mn > div > div.qy-player-tag > a:nth-child(1)", "title").toString();
        String cty = html.css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_cTop > ul > li:nth-child(4) > span > a", "title").toString();
        //Integer time = Integer.valueOf(html.xpath("//*[@id=\"block-L\"]/div[2]/div[1]/ul/li[5]/span/tidyText()").get());
        String director = html.css("#block-L > div.qy-play-side-introduction.vInfoSide_vNCon > div.vInfoSide_cTop > ul > li:nth-child(2) > span > a", "title").toString();
        String atlr = html.css("#comment > div.qy-comment-page > div > div.qycp-bd > div.csPpCircle_relatedWrap > div > div > ul > li:nth-child(2) > div.related_item_tit > p > span", "text").toString();
        String actor = html.css("#comment > div.qy-comment-page > div > div.qycp-bd > div.csPpCircle_relatedWrap > div > div > ul > li:nth-child(3) > div.related_item_tit > p > span", "text").toString();
        String introduction = html.css("#block-F > div.qy-play-role-tab.j_people_wrap > ul > li.role-item.j_people_item.selected > div > div.role-con > h3 > a", "title").toString();
        String ptyurl = page.getUrl().toString();

        //对象赋值
        AiqiyiMovie aiqiyiMovie = new AiqiyiMovie();
        aiqiyiMovie.setSource(source);
        aiqiyiMovie.setName(name);
        aiqiyiMovie.setPty(pty);
        aiqiyiMovie.setRty(rty);
        aiqiyiMovie.setCty(cty);
        //aiqiyiMovie.setTime(time);
        aiqiyiMovie.setDirector(director);
        aiqiyiMovie.setAtlr(atlr);
        aiqiyiMovie.setActor(actor);
        aiqiyiMovie.setIntroduction(introduction);
        aiqiyiMovie.setPtyurl(ptyurl);

        // 将对象添加到Pipeline中
        page.putField("CrawlerData",aiqiyiMovie);
    }

    @Override
    public Site getSite() {
        return site;
    }

    @Scheduled(initialDelay = 1000, fixedDelay = 1000 * 100)
    public void task(){

        System.setProperty("selenuim_config", "D:\\develop\\code\\Demo\\src\\main\\resources\\selenium_test.properties");

        //动态代理方法调用
        ProxyProvider httpClientDownloader = new ProxyProvider();
        HttpClientDownloader downloaderAndProxy = httpClientDownloader.createDownloaderAndProxy();

        String url="https://list.iqiyi.com/www/1/-------------24-4-1-iqiyi--.html";

         Spider.create(new CrawlerTask())
                .addUrl(url)
                .addPipeline(CrawlerPipeline)
                .setScheduler(new QueueScheduler().setDuplicateRemover(
                        new BloomFilterDuplicateRemover(10000)))
                .setDownloader(downloaderAndProxy)
                .setDownloader(new SeleniumDownloader())
                .thread(1)
                .run();
    }
}
