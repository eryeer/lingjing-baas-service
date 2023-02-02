package com.onchain.dna2explorer.mapper;

import com.onchain.dna2explorer.model.dao.Summary;
import com.onchain.dna2explorer.model.response.ResponseSummary;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface SummaryMapper {
    String FULL_DETAIL = " id, create_time, update_time, status, summary_time, block_count, tx_count, active_address_count ";

    @Select("select max(summary_time) " +
            "from tbl_daily_summary ")
    Long getDailyStartTime();

    @Select("select summary_time, active_address_count as summary_count " +
            "from tbl_daily_summary " +
            "order by summary_time desc " +
            "limit #{limit}")
    List<ResponseSummary> getAddressSummary(@Param("limit") Integer limit);

    @Select("select summary_time, block_count as summary_count " +
            "from tbl_daily_summary " +
            "order by summary_time desc " +
            "limit #{limit}")
    List<ResponseSummary> getBlockSummary(@Param("limit") Integer limit);

    @Select("select summary_time, tx_count as summary_count " +
            "from tbl_daily_summary " +
            "order by summary_time desc " +
            "limit #{limit}")
    List<ResponseSummary> getTransactionDailySummary(@Param("limit") Integer limit);

    @Select("select max(summary_time) " +
            "from tbl_monthly_summary ")
    Long getMonthlyStartTime();

    @Select("select summary_time, tx_count as summary_count " +
            "from tbl_monthly_summary " +
            "order by summary_time desc " +
            "limit #{limit}")
    List<ResponseSummary> getTransactionMonthlySummary(@Param("limit") Integer limit);

    @Select("select " + FULL_DETAIL +
            " from tbl_daily_summary where summary_time = #{summaryTime}")
    Summary selectDailySummary(Long summaryTime);

    @Insert("insert into tbl_daily_summary (summary_time, block_count, tx_count, active_address_count ) " +
            "values (#{summaryTime}, #{blockCount}, #{txCount}, #{activeAddressCount} ) ")
    void insertDaily(Summary summary);

    @Update("update tbl_daily_summary set block_count = #{blockCount}, tx_count = #{txCount}, active_address_count = #{activeAddressCount} " +
            "where summary_time = #{summaryTime} ")
    void updateDaily(Summary summary);

    @Select("select " + FULL_DETAIL +
            " from tbl_monthly_summary where summary_time = #{summaryTime}")
    Summary selectMonthlySummary(Long summaryTime);

    @Insert("insert into tbl_monthly_summary (summary_time, block_count, tx_count, active_address_count ) " +
            "values (#{summaryTime}, #{blockCount}, #{txCount}, #{activeAddressCount} ) ")
    void insertMonthly(Summary summary);

    @Update("update tbl_monthly_summary set block_count = #{blockCount}, tx_count = #{txCount}, active_address_count = #{activeAddressCount} " +
            "where summary_time = #{summaryTime} ")
    void updateMonthly(Summary summary);

}
