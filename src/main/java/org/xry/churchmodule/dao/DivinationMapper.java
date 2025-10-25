package org.xry.churchmodule.dao;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.xry.churchmodule.pojo.Omen;

import java.util.Date;
import java.util.List;

@Mapper
public interface DivinationMapper {
    //根据id查27个占卜结果之一
    @Select("select * from divination where id = #{id};")
    List<Omen> getOmenById(@Param("id") int id);

    //存储占卜结果
    @Insert("insert into omen_list (user_id, omen_id, date) values (#{userId},#{omenId},#{date})")
    Integer insertOmen(@Param("userId") Integer userId,@Param("omenId") Integer omenId,@Param("date") Date date);
}
