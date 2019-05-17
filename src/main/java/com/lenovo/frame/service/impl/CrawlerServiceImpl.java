package com.lenovo.frame.service.impl;

import com.lenovo.frame.dao.CrawlerDao;
import com.lenovo.frame.domain.AiqiyiMovie;
import com.lenovo.frame.service.CrawlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 14:01
 * @description 爬虫业务层实现类
 */
@Service
public class CrawlerServiceImpl implements CrawlerService {

    @Autowired
    private CrawlerDao CrawlerDao;

    @Override
    public void save(AiqiyiMovie aiqiyiMovie) {
        CrawlerDao.save(aiqiyiMovie);
    }
}
