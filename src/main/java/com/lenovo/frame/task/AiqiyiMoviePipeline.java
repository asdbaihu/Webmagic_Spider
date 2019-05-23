package com.lenovo.frame.task;

import com.lenovo.frame.domain.AiqiyiMovie;
import com.lenovo.frame.service.AiqiyiMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 14:06
 * @description 自定个 Pipeline对象
 */
@Component
public class AiqiyiMoviePipeline implements Pipeline {

    @Autowired
    private AiqiyiMovieService aiqiyiMovieService;

    @Override
    public void process(ResultItems resultItems, Task task) {
        AiqiyiMovie crawlerData = resultItems.get("CrawlerData");
        aiqiyiMovieService.save(crawlerData);
    }
}
