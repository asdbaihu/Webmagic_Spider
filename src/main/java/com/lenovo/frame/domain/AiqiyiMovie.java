package com.lenovo.frame.domain;

import javax.persistence.*;

/**
 * @author XiuChong@lenovo.com
 * @data 2019/5/16 11:05
 * @description 实体类
 */
@Entity
@Table(name="demo02")
public class AiqiyiMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String source;
    private String name;
    private String pty;
    private String rty;
    private String cty;
    private String time;
    private String language;
    private String director;
    private String atlr;
    private String actor;
    private String introduction;
    private String poster;
    private String ptyurl;

    public AiqiyiMovie() {
    }

    public AiqiyiMovie(String source, String name, String pty, String rty, String cty, String time, String language, String director, String atlr, String actor, String introduction, String poster, String ptyurl) {
        this.source = source;
        this.name = name;
        this.pty = pty;
        this.rty = rty;
        this.cty = cty;
        this.time = time;
        this.language = language;
        this.director = director;
        this.atlr = atlr;
        this.actor = actor;
        this.introduction = introduction;
        this.poster = poster;
        this.ptyurl = ptyurl;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPty() {
        return pty;
    }

    public void setPty(String pty) {
        this.pty = pty;
    }

    public String getRty() {
        return rty;
    }

    public void setRty(String rty) {
        this.rty = rty;
    }

    public String getCty() {
        return cty;
    }

    public void setCty(String cty) {
        this.cty = cty;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getAtlr() {
        return atlr;
    }

    public void setAtlr(String atlr) {
        this.atlr = atlr;
    }

    public String getActor() {
        return actor;
    }

    public void setActor(String actor) {
        this.actor = actor;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getPtyurl() {
        return ptyurl;
    }

    public void setPtyurl(String ptyurl) {
        this.ptyurl = ptyurl;
    }

    @Override
    public String toString() {
        return "AiqiyiMovie{" +
                "id=" + id +
                ", source='" + source + '\'' +
                ", name='" + name + '\'' +
                ", pty='" + pty + '\'' +
                ", rty='" + rty + '\'' +
                ", cty='" + cty + '\'' +
                ", time=" + time +
                ", language='" + language + '\'' +
                ", director='" + director + '\'' +
                ", atlr='" + atlr + '\'' +
                ", actor='" + actor + '\'' +
                ", introduction='" + introduction + '\'' +
                ", poster='" + poster + '\'' +
                ", ptyurl='" + ptyurl + '\'' +
                '}';
    }
}
