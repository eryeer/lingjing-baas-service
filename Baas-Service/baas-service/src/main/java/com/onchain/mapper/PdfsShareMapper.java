package com.onchain.mapper;


import com.onchain.entities.dao.PdfsShare;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

public interface PdfsShareMapper {
    String INSERT_COLS = " from_user_id, to_user_id, file_hash ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{fromUserId}, #{toUserId}, #{fileHash} ";

    @Select("select " + BASIC_COLS + " from tbl_pdfs_share where to_user_id= #{toUserId} and file_hash = #{fileHash} ")
    PdfsShare getPdfsShare(String toUserId, String fileHash);

    //添加
    @Insert("insert ignore into tbl_pdfs_share (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertPdfsShare(PdfsShare pdfsShare);

}
