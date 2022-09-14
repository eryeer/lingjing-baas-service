package com.onchain.mapper;


import com.onchain.entities.dao.GasApply;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface GasApplyMapper {

    String INSERT_COLS = " user_id, user_address, apply_amount, apply_time ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{userAddress}, #{applyAmount}, #{applyTime} ";

    //添加申领记录
    @Insert("insert into tbl_gas_apply (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertGasApply(GasApply gasApply);

    @Select("select " + BASIC_COLS + " from tbl_gas_apply where user_id= #{userId} order by apply_time desc")
    List<GasApply> getApplyList(String userId);
}
