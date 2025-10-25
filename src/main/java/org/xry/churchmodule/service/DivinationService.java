package org.xry.churchmodule.service;

import org.xry.churchmodule.pojo.Omen;

import java.util.List;

public interface DivinationService {
    //获取用户给的种子并随机出id
    Omen randomIdAndGetOmenById(Integer luckyNum);
}
