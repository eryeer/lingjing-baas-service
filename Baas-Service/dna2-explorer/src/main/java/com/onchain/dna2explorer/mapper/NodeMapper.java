package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.Node;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface NodeMapper {
    String FULL_DETAIL = " id, create_time, update_time, status, name, region, ip, rest_port, version, is_active ";

    @Select("select " + FULL_DETAIL +
            "from tbl_node " +
            "where status <> 'delete' " +
            "order by create_time desc " +
            "limit #{startIndex}, #{pageSize}")
    List<Node> selectNodes(@Param("startIndex") int startIndex, @Param("pageSize") int pageSize);

    @Select("select count(1) from tbl_node where status != 'delete'")
    Integer selectNodeCount();

    @Insert("insert into tbl_node (name, region, ip, rest_port, version, is_active) " +
            "value (#{name},#{region},#{ip},#{restPort},#{version},#{isActive})")
    @Options(useGeneratedKeys = true)
    Integer insertNode(Node node);

    @Update("update tbl_node set " +
            "name=#{name}, region=#{region}, ip = #{ip}, rest_port = #{restPort}, version = #{version}, is_active = #{isActive} " +
            "where id = #{id}")
    Integer updateNodeById(Node person);

    @Delete("update tbl_node set " +
            "status='delete' " +
            "where id = #{id}")
    Integer deleteNodeById(@Param("id") Long id);

    @Select("select " + FULL_DETAIL + "from tbl_node where id =#{id} and status <> 'delete'")
    Node selectNodeById(Long id);

    @Select("select " + FULL_DETAIL +
            "from tbl_node " +
            "where status <> 'delete' " +
            "order by create_time desc ")
    List<Node> selectAllNodes();

    @Update("update tbl_node set " +
            "version = #{version}, is_active = #{isActive} " +
            "where id = #{id} ")
    void updateNodeActive(Node node);

}
