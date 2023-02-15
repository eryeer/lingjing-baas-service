package com.onchain.dna2explorer.mapper;


import com.onchain.dna2explorer.model.dao.Contract;
import com.onchain.dna2explorer.model.dao.MethodMap;
import org.apache.ibatis.annotations.CacheNamespace;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@CacheNamespace(implementation = com.onchain.dna2explorer.config.RedisCache.class)
public interface MethodMapMapper {

    String INSERT_COLS = " method_id, method_name, method_hash, method_signature ";
    String INSERT_VALS = "  #{item.methodId}, #{item.methodName}, #{item.methodHash}, #{item.methodSignature} ";
    String COLS = " id, create_time, update_time, status, " + INSERT_COLS;

    @Insert("<script>" +
            "insert ignore into tbl_method_map( " + INSERT_COLS + " ) values " +
            "<foreach collection='list' item='item' separator=','> " +
            "( " + INSERT_VALS + " ) " +
            "</foreach> " +
            "</script>")
    void batchInsert(@Param("list") List<MethodMap> list);

    @Select("select " + COLS + " from tbl_method_map where method_hash = #{methodHash}")
    MethodMap getMethodMapByHash(@Param("methodHash") String methodHash);
}
