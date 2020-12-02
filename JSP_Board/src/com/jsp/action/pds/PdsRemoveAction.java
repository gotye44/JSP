package com.jsp.action.pds;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.jsp.action.Action;
import com.jsp.dto.AttachVO;
import com.jsp.service.PdsService;

public class PdsRemoveAction implements Action{

	private PdsService pdsService;
	
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/pds/remove_success";
		
		int pno = Integer.parseInt(request.getParameter("pno"));
		
		List<AttachVO> attachList = null;
		
		try {
			
			attachList = pdsService.getPds(pno).getAttachList();
			
			if(attachList != null) {
				for(AttachVO attach : attachList) {
					String storedFilePath = attach.getUploadPath() + File.separator + attach.getFileName();
					File file = new File(storedFilePath);
					if(file.exists()) {
						file.delete();
					}
				}
			}
			
			pdsService.remove(pno);
			
		} catch (Exception e) {
			url = "/pds/remove_fail";
			e.printStackTrace();
		}
		
		return url;
	}

}
