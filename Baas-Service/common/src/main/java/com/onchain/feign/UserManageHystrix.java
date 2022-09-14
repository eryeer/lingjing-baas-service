package com.onchain.feign;

import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestMarkFiles;
import com.onchain.entities.response.ResponseCenterCompany;
import com.onchain.entities.response.ResponseCosFile;
import com.onchain.entities.response.ResponseSupplierInfo;
import com.onchain.util.FeignClientUtil;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Component
public class UserManageHystrix implements UserManageService {

    @Override
    public ResponseFormat<List<ResponseCenterCompany>> getVerifiedCenters() {
        return FeignClientUtil.generateFallbackResponse(null);
    }

    @Override
    public ResponseFormat<ResponseSupplierInfo> getSupplierById(String SupplierId, String SupplierName) {
        return FeignClientUtil.generateFallbackResponse(null);
    }


    @Override
    public ResponseFormat<ResponseCosFile> getFileByUuid(String uuid) {
        return FeignClientUtil.generateFallbackResponse(null);
    }


    @Override
    public ResponseFormat<List<ResponseCosFile>> getFileByUuids(@NotEmpty List<String> uuids) {
        return FeignClientUtil.generateFallbackResponse(null);
    }

    @Override
    public ResponseFormat<?> markFiles(@Valid RequestMarkFiles requestMarkFiles) {
        return FeignClientUtil.generateFallbackResponse(null);
    }
}
