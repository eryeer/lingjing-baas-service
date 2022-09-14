package com.onchain.mapper;


import com.onchain.entities.dao.PdfsFile;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PdfsMapper {
    String INSERT_COLS = " user_id, file_hash, tx_hash, file_suffix, file_length, file_name, upload_time ";
    String BASIC_COLS = " id, create_time, update_time, status, " + INSERT_COLS;
    String INSERT_VALS = " #{userId}, #{fileHash}, #{txHash}, #{fileSuffix}, #{fileLength}, #{fileName}, #{uploadTime} ";

    //根据用户id和上传者类型查询PDFS
    //    @Select("select " + BASIC_COLS + " from tbl_pdfs_file where user_id= #{userId} order by upload_time desc ")
    @Select("<script> " +
            "select " + BASIC_COLS +
            "from (select *, 'SELF' as uploaderType " +
            "from tbl_pdfs_file where user_id = #{userId} " +
            "union all " +
            "select a.*, 'OTHER' as uploaderType  " +
            "from tbl_pdfs_file a join tbl_pdfs_share b " +
            "on a.file_hash = b.file_hash " +
            "where b.to_user_id = #{userId}) temp " +
            "<where> 1=1 " +
            "<if test=\"uploaderType != null and uploaderType != 'ALL'\">AND uploaderType = #{uploaderType} </if> " +
            "</where> " +
            "order by upload_time desc " +
            " </script>")
    List<PdfsFile> getPdfsFileList(String uploaderType, String userId);

    @Select("select " + BASIC_COLS + " from tbl_pdfs_file where user_id= #{userId} and file_hash = #{fileHash} ")
    PdfsFile getPdfsFile(String userId, String fileHash);

    @Select("select " + BASIC_COLS + " from tbl_pdfs_file where file_hash = #{fileHash} ")
    PdfsFile getPdfsFileByHash(String fileHash);

    //添加
    @Insert("insert into tbl_pdfs_file (" + INSERT_COLS + ") " +
            "values ( " + INSERT_VALS + " )")
    void insertPdfsFile(PdfsFile file);

}
