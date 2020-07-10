package com.insigma.report;

import com.insigma.po.soldier.ArchivesReport;
import com.insigma.po.soldier.AreaCascadeData;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 *用来存储市，省，
 * 区个个节点的信息及其子节点信息
 */
@Data
public class SysOrgOfReportVo {
    private String regionalName;

    private String regionalCode;

    List<ArchivesReport> children;
}
