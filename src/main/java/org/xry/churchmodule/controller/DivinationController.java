package org.xry.churchmodule.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xry.churchmodule.service.DivinationService;
import org.xry.interceptors.pojo.Code;
import org.xry.interceptors.pojo.Result;

import java.util.Map;

@RestController
@RequestMapping("/church")
public class DivinationController {
    @Autowired
    private DivinationService ds;

    //进行占卜
    @PostMapping("/divination")
    public Result InsertWorker(@RequestBody Map<String,Integer> inputMap) {
        System.out.println("种子"+inputMap.get("luckyNum"));
        return new Result(ds.randomIdAndGetOmenById(inputMap.get("luckyNum")) ,"占卜成功", Code.INSERT_OK);
    }

}
