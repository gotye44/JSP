package com.jsp.action.pds;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import com.jsp.action.Action;
import com.jsp.dao.AttachDAO;
import com.jsp.dto.AttachVO;
import com.jsp.utils.ExceptionLoggerHepler;
import com.jsp.utils.MakeFileName;

public class GetAttachAction implements Action{

	private AttachDAO attachDAO;
	
	public void setAttachDAO(AttachDAO attachDAO) {
		this.attachDAO = attachDAO;
	}
	
	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String url = "";
		
		int ano = Integer.parseInt(request.getParameter("ano"));
		
		try {
			AttachVO attach = attachDAO.selectAttachByAno(ano);
			
			sendfile(request,response,attach.getFileName(),attach.getUploadPath());
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionLoggerHepler.write(request, e, attachDAO);
			response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
		
		return url;
	}

	private void sendfile(HttpServletRequest request, HttpServletResponse response, String fileName,
			String filePath) throws IOException{
		filePath = filePath + File.separator+fileName;
		
		File downloadFile = new File(filePath);
		FileInputStream inStream = new FileInputStream(downloadFile);
		
		ServletContext context = request.getServletContext();
		
		String mimeType = context.getMimeType(filePath);
		if(mimeType == null) {
			mimeType = "application/octet-stream";
		}
		
		response.setContentType(mimeType);
		response.setContentLength((int) downloadFile.length());
		
		String downloadFileName = new String(downloadFile.getName().getBytes("utf-8"), "ISO-8859-1");
		downloadFileName = MakeFileName.parseFileNameFromUUID(downloadFileName, "\\$\\$");
		
		String headerKey = "Content-Disposition";
		String headerValue = String.format("attachment; filename=\"%s\"", downloadFileName);
		response.setHeader(headerKey, headerValue);
		
		OutputStream outStream = null;
		try {
			outStream = response.getOutputStream();
			byte[] buffer = new byte[4096];
			int bytesRead = -1;
			
			while((bytesRead = inStream.read(buffer)) != -1) {
				outStream.write(buffer,0,bytesRead);
			}
		} catch (Exception e) {
		} finally {
			if(inStream !=null) inStream.close();
			if(outStream !=null) outStream.close();
		}
	}

}
