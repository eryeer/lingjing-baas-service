package com.onchain.feign;


import com.onchain.constants.UrlConst;
import com.onchain.entities.ResponseFormat;
import com.onchain.entities.request.RequestMarkFiles;
import com.onchain.entities.response.ResponseCenterCompany;
import com.onchain.entities.response.ResponseCosFile;
import com.onchain.entities.response.ResponseSupplierInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Component
@FeignClient(value = UrlConst.USER_MANAGE_SERVICE)
public interface UserManageService {

    @GetMapping(value = UrlConst.REST_GET_VERIFIED_CENTERS)
    ResponseFormat<List<ResponseCenterCompany>> getVerifiedCenters();

    @GetMapping(value = UrlConst.REST_GET_SUPPLIER_BY_ID)
    ResponseFormat<ResponseSupplierInfo> getSupplierById(@RequestParam String SupplierId, @RequestParam String SupplierName);

    @GetMapping(value = UrlConst.REST_GET_FILE_BY_UUID)
    ResponseFormat<ResponseCosFile> getFileByUuid(@RequestParam String uuid);

    @PostMapping(value = UrlConst.REST_GET_FILE_BY_UUIDS)
    ResponseFormat<List<ResponseCosFile>> getFileByUuids(@RequestParam @NotEmpty List<String> uuids);

    @PostMapping(value = UrlConst.REST_MARK_FILES)
    ResponseFormat<?> markFiles(@Valid @RequestBody RequestMarkFiles requestMarkFiles);
}
