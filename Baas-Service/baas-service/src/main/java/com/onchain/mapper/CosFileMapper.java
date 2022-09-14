package com.onchain.mapper;

import com.onchain.entities.dao.CosFile;
import com.onchain.entities.response.ResponseCosFile;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

public interface CosFileMapper {
    @Update("<script>" +
            "update tbl_cos_file set is_temp = false where uuid in (" +
            "<foreach collection='list' index='index' item='item' separator=',' > #{item.uuid}</foreach>" +
            ")" +
            "</script>")
    void updateCosFiles(@Param("list") List<CosFile> list);

    // 根据文件Id列表标记文件已被关联使用并设置启用
    @Update("<script>" +
            "update tbl_cos_file set is_temp = false, status = 1 where uuid in (" +
            "<foreach collection='list' index='index' item='item' separator=',' > #{item}</foreach>" +
            ")" +
            "</script>")
    void markFileUsed(@Param("list") List<String> list);

    // 根据文件Id列表标记文件为已删除
    @Update("<script>" +
            "update tbl_cos_file set status = 0 where uuid in (" +
            "<foreach collection='list' index='index' item='item' separator=',' > #{item}</foreach>" +
            ")" +
            "</script>")
    void deleteFiles(@Param("list") List<String> list);

    @Insert("insert into tbl_cos_file(user_id, uuid, file_suffix, file_length, bucket_name, file_name, file_key, is_temp, file_type) " +
            "values (#{userId}, #{uuid}, #{fileSuffix}, #{fileLength}, #{bucketName}, #{fileName}, #{fileKey}, #{isTemp}, #{fileType} )")
    void insertFile(CosFile cosFiles);

    @Insert("update tbl_cos_file set file_length = #{fileLength} where uuid=#{uuid} ")
    void updateFile(CosFile cosFiles);

    @Select("select uuid, user_id, file_name, file_key, bucket_name from tbl_cos_file where uuid=#{uuid}")
    ResponseCosFile getFileInfo(String uuid);

    @Select("select uuid, user_id, file_name, file_key, bucket_name from tbl_cos_file where uuid=#{uuid}")
    CosFile getFile(String uuid);

    @Select("<script>" +
            "select uuid, user_id, file_name, file_key, bucket_name from tbl_cos_file where uuid in ( " +
            "<foreach collection='list' index='index' item='item' separator=',' > #{item}</foreach>)" +
            "</script>")
    List<ResponseCosFile> getFileByIds(@Param("list") List<String> list);

    @Delete("delete from tbl_cos_file where is_temp='1' and update_time < " + " #{date}")
    void deleteOutDateCos(Date date);
}
