package com.jsp.action.pds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.josephoconnell.html.HTMLInputFilter;
import com.jsp.action.Action;
import com.jsp.dto.AttachVO;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;
import com.jsp.utils.ExceptionLoggerHepler;
import com.jsp.utils.GetUploadPath;
import com.jsp.utils.MakeFileName;
import com.jsp.utils.ServletFileUploadBuilder;

public class PdsRegistAction implements Action{

	private PdsService pdsService;
	
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "/pds/regist_success";
		
		
		try {
			PdsVO pds = fileUpload(request);
			pdsService.regist(pds);
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, pdsService);
			url = "/pds/regist_fail";
		}
		return url;
	}
	
	private static final int MEMORY_THRESHOLD = 1024*1024*3;
	private static final int MAX_FILE_SIZE = 1024*1024*40;
	private static final int MAX_REQUEST_SIZE = 1024*1024*200;
	
	// 업로드 파일 환경설정
	private PdsVO fileUpload(HttpServletRequest request) throws Exception {
		PdsVO pds = new PdsVO();
		
		List<AttachVO> attachList = new ArrayList<>();
		
		ServletFileUpload upload = ServletFileUploadBuilder.build(request, MEMORY_THRESHOLD, MAX_FILE_SIZE, MAX_REQUEST_SIZE);
		
		String uploadPath = GetUploadPath.getUploadDatePath("pds.upload");
		File file = new File(uploadPath);
		if(!file.mkdirs()) {
			System.out.println(uploadPath+"가 이미 존재하거나 생성을 실패했습니다.");
		}
		
		List<FileItem> formItems = upload.parseRequest(request);
		
		for(FileItem item : formItems) {
			if(item.isFormField()) {
				switch (item.getFieldName()) {
				case "title":
					pds.setTitle(HTMLInputFilter.htmlSpecialChars(item.getString("utf-8")));
					break;
				case "writer":
					pds.setWriter(item.getString("utf-8"));
					break;
				case "content":
					pds.setContent(item.getString("utf-8"));
					break;
				}
			}else {
				if(!item.getFieldName().equals("uploadFile")) continue;
				
				String fileName = new File(item.getName()).getName();
				fileName = MakeFileName.toUUIDFIleName(fileName, "$$");
				String filePath = uploadPath + File.separator+fileName;
				File storeFile = new File(filePath);
				
				try {
					item.write(storeFile);
				} catch (Exception e) {
					e.printStackTrace();
					throw e;
				}
				
				AttachVO attach = new AttachVO();
				attach.setFileName(fileName);
				attach.setUploadPath(uploadPath);
				attach.setFileType(fileName.substring(fileName.lastIndexOf(".")+1));
				
				attachList.add(attach);
				
				System.out.println("upload file : " + attach);
			}
		}
		
		pds.setAttachList(attachList);
		
		return pds;
	}

}
