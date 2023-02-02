package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.NFTHolder;
import com.onchain.dna2explorer.model.response.ResponseNFTHolder;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface TableHeightMapper {

    //通过tableName获取表格统计的高度
    @Select("select height from tbl_table_height where table_name = #{tableName}")
    Long getLastestTableHeightByTableName(String tableName);

    @Insert("insert into tbl_table_height (table_name, height) " +
            "values (#{tableName},  #{height} )  on duplicate key update height=#{height} ")
    void updateTableHeightByTableName(String tableName, Long height);

}
