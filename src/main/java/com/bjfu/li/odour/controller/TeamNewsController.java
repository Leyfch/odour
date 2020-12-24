package com.bjfu.li.odour.controller;

import com.bjfu.li.odour.common.pojo.SverResponse;
import com.bjfu.li.odour.vo.DownloadFileVo;
import com.bjfu.li.odour.vo.TeamNewsVo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/team")
public class TeamNewsController {

    @Value("${teamNewsDir}")
    private String teamNewsDir;

    @GetMapping("/news")
    public SverResponse<List<TeamNewsVo>> getTeamNews(){
        List<TeamNewsVo> teamNewsList=new ArrayList<>();
        File teamNews=new File(teamNewsDir);
        File[] fileList=teamNews.listFiles();
        if(fileList!=null) {
            for (File file : fileList) {
                if(file.isFile()) {
                    String filename=file.getName();
                    if(filename.endsWith("html")||filename.endsWith("htm")) {
                        String title = filename.substring(0, filename.lastIndexOf("."));
                        String link = "/resource/static/news/" + filename;
                        teamNewsList.add(new TeamNewsVo(title, link, new Date(file.lastModified())));
                    }
                }
            }
        }
        return SverResponse.createRespBySuccess(teamNewsList);
    }
}
