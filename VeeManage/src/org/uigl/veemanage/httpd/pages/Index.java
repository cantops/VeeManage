package org.uigl.veemanage.httpd.pages;

import java.io.InputStream;
import java.util.Properties;

import org.uigl.veemanage.settings.SettingsManager;
import org.uigl.veemanage.httpd.Session;
import org.uigl.veemanage.httpd.templates.Template;
import org.uigl.veemanage.httpd.VeeManageHTTPD;
import org.uigl.veemanage.httpd.VeeManageHTTPPage;

public class Index implements VeeManageHTTPPage {

	private static final String TEMPLATE_NAME = "Index.html";
	
	@Override
	public String getPageClassName() {
		return "Index";
	}
	
	private String pRedirect = "/login/";
	private String pStatus = VeeManageHTTPD.HTTP_OK;
	private int pFlags = VeeManageHTTPD.FLAG_NONE;
	
	private Session mUserSession = null;
	private String mErrorText = null;
	
	@Override
	public boolean hasMatch(String uri, String method) {
		return uri.equals("/");
	}

	@Override
	public void init(String uri, String method, Properties headers, Properties params, Properties files, Session userSession) {
		// Reset all page vars.
		pRedirect = "/login/";
		pStatus = VeeManageHTTPD.HTTP_OK;
		pFlags = VeeManageHTTPD.FLAG_NONE;
		
		mUserSession = userSession;
		
		if (!mUserSession.getBoolean("UserAuthenticated", false)) {
			pRedirect = "/login/";
			pStatus = VeeManageHTTPD.HTTP_OK;
			pFlags |= VeeManageHTTPD.FLAG_REDIRECT;
			return;
		}
			
	}

	@Override
	public String getStatus() {
		return pStatus;
	}

	@Override
	public String getMimeType() {
		return VeeManageHTTPD.MIME_HTML;
	}

	@Override
	public Properties getHeaders() {
		return null;
	}

	@Override
	public InputStream getData() {
		Session pageVars = new Session(null);
		pageVars.put("app_name", SettingsManager.DEFAULT_WWW_APP_NAME);
		pageVars.put("title", "Index");
		
		//Handle errors
		pageVars.put("error_text", mErrorText == null ? "" : mErrorText);

		return Template.applyTemplate(TEMPLATE_NAME, pageVars);
	}

	@Override
	public int getFlags() {
		return pFlags;
	}

	@Override
	public String getRedirectLocation() {
		return pRedirect;
	}

}
