package com.lenovo.frame.service;

import com.lenovo.frame.domain.AiqiyiMovie;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 13:59
 * @description 爬虫业务层接口
 */
public interface CrawlerService {

    void save(AiqiyiMovie aiqiyiMovie);
}
