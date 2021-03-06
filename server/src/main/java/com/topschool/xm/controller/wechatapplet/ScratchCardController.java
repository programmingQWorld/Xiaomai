package com.topschool.xm.controller.wechatapplet;

import com.topschool.xm.dto.ScratchResult;
import com.topschool.xm.exception.ScratchCardException;
import com.topschool.xm.service.ScratchCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/wechat_applet_api/scratch-card")
public class ScratchCardController {

    @Qualifier("defaultScratchCardServiceImpl")
    @Autowired
    private ScratchCardService scratchCardService;

    @GetMapping("")
    public ResponseEntity<?> scratch(String uid, int isScratch) throws ScratchCardException {
        System.out.printf("id:%15s isScratch:%5s\n", uid, isScratch);
        scratchCardService.initCardPool(100);
        ScratchResult result = new ScratchResult();
        result.setPartnerStatus(-1);
        result.setCurUserGroup(1);
        if (scratchCardService.getPartnerTodayStatus(uid)){
            result.setPartnerStatus(1);
        }
        if (isScratch == 1) {
            result.setCurrentScratchResult(scratchCardService.scratch(uid.trim()));
            result.setPartnerStatus(0);
        }
        if (isScratch!=1 && isScratch!=-1){
            throw new IllegalArgumentException("请求存在非法参数");
        }
        result.setLastList(scratchCardService.getTodayLastList(0, 2));
        result.setTopList(scratchCardService.getTodayTopList(0, 2));
        result.setTodayList(scratchCardService.getTodayResult(0, 2));
        result.setTotalTop(scratchCardService.getTotalTopResult(0, 3));
        result.setTotalNum(scratchCardService.getTodayTotal(new Date()));
        return new ResponseEntity<Object>(result, OK);
    }
}
