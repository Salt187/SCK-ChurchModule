package org.xry.churchmodule.service.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xry.churchmodule.dao.DivinationMapper;
import org.xry.churchmodule.exception.Exceptions.serviceException;
import org.xry.churchmodule.pojo.Code;
import org.xry.churchmodule.pojo.Omen;
import org.xry.churchmodule.service.DivinationService;
import org.xry.churchmodule.utils.ThreadLocalUtils.UserId;

import java.util.Date;
import java.util.Random;


@Service
public class divinationServiceImp implements DivinationService {
    @Autowired
    private DivinationMapper mapper;

    public Omen randomIdAndGetOmenById(Integer luckyNum) {
        if (luckyNum == null) throw new serviceException("参数异常", Code.BUSINESS_ERROR);
//                         [1,6)
        //据随机数取真实id
        int id = randomInt(1, 6, luckyNum);

        //存储占卜结果
        try {
            mapper.insertOmen(UserId.getId(),id,new Date());
        } catch (Exception e) {
            throw new serviceException("明日再会",Code.BUSINESS_ERROR);
        }

        //查完整结果并返回
        return mapper.getOmenById(id).getFirst();
    }

    //随机工具类
    public static int randomInt(int min, int max, long seed) {
        // 校验参数：min 必须小于 max
        if (min >= max) {
            throw new IllegalArgumentException("min 必须小于 max");
        }
        // 1. 基于种子创建 Random 实例（相同种子会生成完全相同的随机序列）
        Random random = new Random(seed);
        // 2. 生成 [0, max - min) 范围内的随机数，再加上 min 偏移到目标范围
        int range = max - min;
        return random.nextInt(range) + min;
    }
}
