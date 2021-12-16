package com.example.web.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.sun.bean.UserJobInfoEntity;
import com.sun.interfaces.JobInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JobController {
    @Reference
    JobInfoService jobInfoService;

    @ResponseBody
    @RequestMapping("/getUserJobInfo")
    public UserJobInfoEntity getUserJobInfo(@RequestParam("userId") String userId, @RequestParam("jobId") Long jobId) {
        return jobInfoService.getUserJobInfo(userId, jobId);
    }
}
