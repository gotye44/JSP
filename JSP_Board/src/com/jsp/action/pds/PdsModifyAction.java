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
import com.jsp.dao.AttachDAO;
import com.jsp.dto.AttachVO;
import com.jsp.dto.PdsVO;
import com.jsp.service.PdsService;
import com.jsp.utils.GetUploadPath;
import com.jsp.utils.MakeFileName;
import com.jsp.utils.ServletFileUploadBuilder;

public class PdsModifyAction implements Action{

	private PdsService pdsService; 
	private AttachDAO attachDAO;
//	private
	
	public void setPdsService(PdsService pdsService) {
		this.pdsService = pdsService;
	}
	
	public void setAttachDAO(AttachDAO attachDAO) {
		this.attachDAO = attachDAO;
	}

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String url = "/pds/modify_success";
		
		PdsVO pds = null;
		
		try {
			pds = modifyFile(request, response);
			pdsService.modify(pds);
		} catch (Exception e) {
			e.printStackTrace();
			url = "/pds/modify_fail";
		} finally {
			request.setAttribute("pds", pds);
		}
		return url;
	}

	private static final int MEMORY_THRESHOLD = 1024*1024*3;
	private static final int MAX_FILE_SIZE = 1024*1024*40;
	private static final int MAX_REQUEST_SIZE = 1024*1024*200;
	
	private PdsVO modifyFile(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		PdsVO pds = new PdsVO();
		
		List<AttachVO> attachList = new ArrayList<AttachVO>();
		
		ServletFileUpload upload = ServletFileUploadBuilder.build(request, MEMORY_THRESHOLD, MAX_FILE_SIZE, MAX_REQUEST_SIZE);
		
		String uploadPath = GetUploadPath.getUploadDatePath("pds.upload");
		
		File file = new File(uploadPath);
		if(!file.mkdirs()) {
			System.out.println(uploadPath + "가 이미 존재하거나 생성을 실패했습니다.");
		}
		
		List<FileItem> formItems = upload.parseRequest(request);
		if(formItems != null && formItems.size()>0) {
			List<String> deleteFile = new ArrayList<>();
			for(FileItem item : formItems) {
				if(!item.isFormField()) {
					if(!item.getFieldName().equals("uploadFile")) continue;
					
					String fileName = new File(item.getName()).getName();
					fileName = MakeFileName.toUUIDFIleName(fileName, "$$");
					String filePath = uploadPath + File.separator + fileName;
					File storeFile = new File(filePath);
					
					item.write(storeFile);
					
					AttachVO attach = new AttachVO();
					attach.setFileName(fileName);
					attach.setUploadPath(uploadPath);
					attach.setFileType(fileName.substring(fileName.lastIndexOf(".")+1));
					
					attachList.add(attach);
					
					System.out.println("upload File : " + attach);
					
					request.setAttribute("message", "업로드 되었습니다");
				}else {
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
					case "pno":
						pds.setPno(Integer.parseInt(item.getString("utf-8")));
						break;
					case "deleteFile":
						int ano = Integer.parseInt(item.getString("utf-8"));
						
						AttachVO attach = attachDAO.selectAttachByAno(ano);
						
						attachDAO.deleteAttach(ano);
						
						String filePath = attach.getUploadPath() + File.separator + attach.getFileName();
						File targetFile = new File(filePath);
						
						if(targetFile.exists()) {
							targetFile.delete();
						}
						
						deleteFile.add(item.getString("utf-8"));
						break;
					}
				}
			}
			
			pds.setAttachList(attachList);
			
			System.out.println("receive pds : \n" + pds);
			System.out.println("deleteFile : \n" + deleteFile);
		}
		return pds;
	}

}
