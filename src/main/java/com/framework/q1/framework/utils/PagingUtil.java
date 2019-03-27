package com.framework.q1.framework.utils;

import vo.PagingVo;

public class PagingUtil {

    public static PagingVo setPaging(PagingVo pagingVo, int pageNum, int pageSize, int countPage) {

        pagingVo.setPageSize(pageSize);
        pagingVo.setCountPage(countPage);

        pagingVo.setPg(pageNum);
        pagingVo.setStart(pagingVo.getPg() * pageSize - (pageSize - 1));
        pagingVo.setEnd(pagingVo.getPg() * pageSize);

        pagingVo.setStartPage(((pagingVo.getPg() - 1) / countPage * countPage) + 1);
        pagingVo.setEndPage(((pagingVo.getPg() - 1) / countPage * countPage) + countPage);

        return pagingVo;
    }

}
