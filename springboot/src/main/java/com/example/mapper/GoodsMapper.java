package com.example.mapper;

import com.example.entity.Goods;
import com.example.entity.StatVo;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * 操作goods相关数据接口
*/
public interface GoodsMapper {

    /**
      * 新增
    */
    int insert(Goods goods);

    /**
      * 删除
    */
    @Delete("delete from goods where id = #{id}")
    int deleteById(Integer id);

    /**
      * 修改
    */
    int updateById(Goods goods);

    /**
      * 根据ID查询
    */
    @Select("select * from goods where id = #{id}")
    Goods selectById(Integer id);

    @Update("update goods set store = store - #{num} where id = #{goodsId} and store >= #{num}")
    int decreaseStock(@Param("goodsId") Integer goodsId, @Param("num") Integer num);

    @Update("update goods set store = store + #{num} where id = #{goodsId}")
    int increaseStock(@Param("goodsId") Integer goodsId, @Param("num") Integer num);

    /**
      * 查询所有
    */
    List<Goods> selectAll(Goods goods);

    /**
     *
     * 查询所有商品分类
     * @return
     */
    List<StatVo> countByCategory();

    @Select("select count(*) from goods where category_id = #{categoryId}")
    int countByCategoryId(Integer categoryId);

    @Select("select count(*) from goods")
    int countAll();

}
