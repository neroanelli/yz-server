package com.yz.network.examination.model;

import java.util.Date;

/**
 * 
 * @author Administrator
 *
 */
@SuppressWarnings("serial")
public class YzNetworkExamLogFrm implements java.io.Serializable {
	
    private Integer id;

    private String frmId;

    private String frmName;
    
    private String frmKey ; // 

    private String frmType;

    private String frmAction;

    private String frmParam;

    private long frmTime;

    private String frmNo;

    private String frmCode;

    private String frmAddr;

    private Date frmDate;
    
    private int frmResult= 0 ; // 处理结果 
    
    private String frmErrmsg ; // 错误信息
    
    private String frmUserId;
    
    private String frmUserName;
     
    public String getFrmUserId() {
		return frmUserId;
	}

	public void setFrmUserId(String frmUserId) {
		this.frmUserId = frmUserId;
	}

	public String getFrmUserName() {
		return frmUserName;
	}

	public void setFrmUserName(String frmUserName) {
		this.frmUserName = frmUserName;
	}

	public void setFrmKey(String frmKey) {
		this.frmKey = frmKey;
	}
    
    public String getFrmKey() {
		return frmKey;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFrmId() {
        return frmId;
    }

    public void setFrmId(String frmId) {
        this.frmId = frmId == null ? null : frmId.trim();
    }

    public String getFrmName() {
        return frmName;
    }

    public void setFrmName(String frmName) {
        this.frmName = frmName == null ? null : frmName.trim();
    }

    public String getFrmType() {
        return frmType;
    }

    public void setFrmType(String frmType) {
        this.frmType = frmType == null ? null : frmType.trim();
    }

    public String getFrmAction() {
        return frmAction;
    }

    public void setFrmAction(String frmAction) {
        this.frmAction = frmAction == null ? null : frmAction.trim();
    }

    public String getFrmParam() {
        return frmParam;
    }

    public void setFrmParam(String frmParam) {
        this.frmParam = frmParam == null ? null : frmParam.trim();
    }

    public long getFrmTime() {
        return frmTime;
    }

    public void setFrmTime(long frmTime) {
        this.frmTime = frmTime;
    }

    public String getFrmNo() {
        return frmNo;
    }

    public void setFrmNo(String frmNo) {
        this.frmNo = frmNo == null ? null : frmNo.trim();
    }

    public String getFrmCode() {
        return frmCode;
    }

    public void setFrmCode(String frmCode) {
        this.frmCode = frmCode;
    }

    public String getFrmAddr() {
        return frmAddr;
    }

    public void setFrmAddr(String frmAddr) {
        this.frmAddr = frmAddr == null ? null : frmAddr.trim();
    }

    public Date getFrmDate() {
        return frmDate;
    }

    public void setFrmDate(Date frmDate) {
        this.frmDate = frmDate;
    }

	public int getFrmResult() {
		return frmResult;
	}

	public void setFrmResult(int frmResult) {
		this.frmResult = frmResult;
	}

	public String getFrmErrmsg() {
		return frmErrmsg;
	}

	public void setFrmErrmsg(String frmErrmsg) {
		this.frmErrmsg = frmErrmsg;
	}
     
    
}