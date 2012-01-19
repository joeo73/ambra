/* $HeadURL::                                                                            $
 * $Id$
 *
 * Copyright (c) 2006 by Topaz, Inc.
 * http://topazproject.org
 *
 * Licensed under the Educational Community License version 1.0
 * http://opensource.org/licenses/ecl1.php
 */
package org.plos.user;

import com.opensymphony.xwork.ActionInvocation;
import com.opensymphony.xwork.interceptor.Interceptor;
import com.opensymphony.webwork.ServletActionContext;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import static org.plos.Constants.PLOS_ONE_USER_KEY;
import static org.plos.Constants.ReturnCode;
import static org.plos.Constants.SINGLE_SIGNON_USER_KEY;
import static org.plos.Constants.SINGLE_SIGNON_RECEIPT;

import org.plos.ApplicationException;
import org.plos.user.service.UserService;
import org.plos.util.FileUtils;
import org.plos.user.service.DisplayNameAlreadyExistsException;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;

/**
 * Ensures that the user has a profile if the user does something which requires membership
 * Get the user object for the logged in user or redirect the user to set up his profile.
 */
public class EnsureUserAccountInterceptor implements Interceptor {
  private UserService userService;
  private static final Log log = LogFactory.getLog(EnsureUserAccountInterceptor.class);

  public String intercept(final ActionInvocation actionInvocation) throws Exception {
    log.debug("ensure user account interceptor called");
    final Map<String, Object> sessionMap = getUserSessionMap();
    final String userId = (String) sessionMap.get(SINGLE_SIGNON_USER_KEY);

    if (null == userId) {
      if (log.isDebugEnabled()){
        log.debug("no single sign on user key");
        log.debug("ticket is: " + sessionMap.get(SINGLE_SIGNON_RECEIPT));
      }
      return actionInvocation.invoke();
    }

    PlosOneUser plosUser = (PlosOneUser) sessionMap.get(PLOS_ONE_USER_KEY);
    if (null != plosUser) {
      if (log.isDebugEnabled()) {
        log.debug ("Retrieved user from session with userId: " + plosUser.getUserId());
      }
      return getReturnCodeDependingOnDisplayName(plosUser, actionInvocation);
    } else {
      final UserService userService = getUserService();
      plosUser = userService.getUserByAuthId(userId);
      if (log.isDebugEnabled()) {
        log.debug("UserService : " + userService + " hashcode = "  + userService.hashCode());
        log.debug("Session: " + ServletActionContext.getRequest().getSession().getId());
      }
      
      if (null == plosUser) {
        //forward to new profile creation page
        log.debug("This is a new user with id: " + userId);
        return ReturnCode.NEW_PROFILE;
      } else {
        updateUserEmailAddress(plosUser, userId);
        plosUser.setAuthId(userId);
        sessionMap.put(PLOS_ONE_USER_KEY, plosUser);
        log.debug("Existing user detected: " + userId);
        return getReturnCodeDependingOnDisplayName(plosUser, actionInvocation);
      }
    }
  }

  private String getReturnCodeDependingOnDisplayName(final PlosOneUser plosOneUser, final ActionInvocation actionInvocation) throws Exception {
    if (StringUtils.hasText(plosOneUser.getDisplayName())) {
      //forward the user to the page he was initially going to
      return actionInvocation.invoke();
    } else {
      // profile has partial details as the user might have been ported from old application
      return ReturnCode.UPDATE_PROFILE; //forward to update profile page
    }
  }

  protected Map<String, Object> getUserSessionMap() {
    return userService.getUserContext().getSessionMap();
  }

  private UserService getUserService() {
    return userService;
  }

  public void setUserService(final UserService userService) {
    this.userService = userService;
  }

  private void updateUserEmailAddress (PlosOneUser user, String authId) throws ApplicationException {
    String emailAddress = fetchUserEmailAddress (user, authId);
    if (emailAddress != null) {
      if (!emailAddress.equals (user.getEmail())) {
        user.setEmail(emailAddress);
        try {
          userService.setProfile(user);
        } catch (DisplayNameAlreadyExistsException de) {
          if (log.isErrorEnabled()) {
            log.error("Username: " + user.getDisplayName() + 
                      " already exists while trying to update email address for user: " + 
                      user.getUserId(), de);
          }
        }
      } 
    } else {
      if (log.isErrorEnabled()) {
        log.error("Retrieved a null email address from CAS for userId: " + user.getUserId());
      }
    }
  }
  
  private String fetchUserEmailAddress(PlosOneUser user, String authId) throws ApplicationException {
    final String emailAddressUrl = getEmailAddressUrl();
    final String url = emailAddressUrl + authId;
    try {
      return FileUtils.getTextFromUrl(url);
    } catch (IOException ex) {
      final String errorMessage = "Failed to fetch the email address using the url:" + url;
      log.error(errorMessage, ex);
      throw new ApplicationException(errorMessage, ex);
    }
  }

  private String getEmailAddressUrl() {
    return getUserService().getEmailAddressUrl();
  }
  
  
  public void destroy() {}
  public void init() {}
}
