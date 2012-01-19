/* $HeadURL::                                                                            $
 * $Id$
 *
 * Copyright (c) 2006-2010 by Public Library of Science
 * http://plos.org
 * http://ambraproject.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.topazproject.ambra.user;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.mock.MockActionInvocation;

import org.topazproject.ambra.BaseAmbraTestCase;
import org.topazproject.ambra.Constants;
import org.topazproject.ambra.user.EnsureRoleInterceptor;
import org.topazproject.ambra.user.AmbraUser;
import org.topazproject.ambra.user.service.UserService;

import java.util.HashMap;
import java.util.Map;

public class EnsureRoleInterceptorTest extends BaseAmbraTestCase {

  public void testShouldReturnErrorAsUserNotLoggedIn() throws Exception {
    final EnsureRoleInterceptor interceptor = new EnsureRoleInterceptor();
    final MockActionInvocation actionInvocation = new MockActionInvocation();

    final Map<String, Object> map = new HashMap<String, Object>();
    map.put(Constants.SINGLE_SIGNON_USER_KEY, "A_GUID");
    ActionContext.getContext().setSession(map);

    final UserService userService = getUserServiceReturningRole("webUser");

    actionInvocation.setAction(null);
    final String result = interceptor.intercept(actionInvocation);
    assertEquals(Action.ERROR, result);
  }

  public void testShouldReturnNotSufficientRole() throws Exception {
    final String GUID = "A_GUID";
    final AmbraUser ambraUser = new AmbraUser(GUID);
    ambraUser.setUserId("topazId");
    ambraUser.setEmail("viru@home.com");
    ambraUser.setDisplayName("Viru");  //Display name is already set
    ambraUser.setRealName("virender");

    final EnsureRoleInterceptor interceptor = new EnsureRoleInterceptor() {
      protected Map<String, Object> getUserSessionMap() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SINGLE_SIGNON_USER_KEY, "A_SINGLE_SIGNON_KEY");
        map.put(Constants.AMBRA_USER_KEY, ambraUser);
        return map;
      }
    };

    final MockActionInvocation actionInvocation = new MockActionInvocation();

    final Map<String, Object> map = new HashMap<String, Object>();
    map.put(Constants.SINGLE_SIGNON_USER_KEY, "ASDASDASD12312313EDB");
    ActionContext.getContext().setSession(map);

    final UserService userService = getUserServiceReturningRole("webUser");

    actionInvocation.setAction(null);
    final String result = interceptor.intercept(actionInvocation);
    assertEquals(Constants.ReturnCode.NOT_SUFFICIENT_ROLE, result);
  }

  public void testShouldForwardToOriginalActionAsUserIsAdmin() throws Exception {
    final String GUID = "ASDASDASD12312313EDB";
    final AmbraUser ambraUser = new AmbraUser(GUID);
    ambraUser.setUserId("topazId");
    ambraUser.setEmail("viru@home.com");
    ambraUser.setDisplayName("Viru");  //Display name is already set
    ambraUser.setRealName("virender");

    final EnsureRoleInterceptor interceptor = new EnsureRoleInterceptor() {
      protected Map<String, Object> getUserSessionMap() {
        final Map<String, Object> map = new HashMap<String, Object>();
        map.put(Constants.SINGLE_SIGNON_USER_KEY, "SINGLE_SIGNON_KEY_ASDASDASD12312313EDB");
        map.put(Constants.AMBRA_USER_KEY, ambraUser);
        return map;
      }
    };

    final String actionCalledStatus = "actionCalled";

    final ActionInvocation actionInvocation = new MockActionInvocation() {
      public String invoke() throws Exception {
        return actionCalledStatus;
      }
    };

    final UserService userService = getUserServiceReturningRole(Constants.ADMIN_ROLE);

    final String result = interceptor.intercept(actionInvocation);
    assertEquals(actionCalledStatus, result);
  }

  private UserService getUserServiceReturningRole(final String roleToReturn) throws Exception {
    /*
    final UserService userService = getUserService();
    final UserRoleWebService userRoleWebService = new UserRoleWebService() {
      public String[] getRoles(final String topazId) throws NoSuchUserIdException, RemoteException {
        return new String[]{roleToReturn};
      }
    };
    userService.setUserRoleWebService(userRoleWebService);
    return userService;
    */
    return new UserService() {
      public String[] getRoles(final String topazId) {
        return new String[] { roleToReturn };
      }
    };
  }

}
