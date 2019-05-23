package com.lenovo.frame.dao;

import com.lenovo.frame.domain.AiqiyiMovie;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 11:01
 * @description 爬虫持久层接口
 */
public interface AiqiyiMovieDao extends JpaRepository<AiqiyiMovie,Long> {
}
