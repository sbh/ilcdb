/* Copyright 2004-2005 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT c;pWARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/** 
  * A taglib to help with J2EE security implementations (e.g. ACEGI) within Grails. 
  * 
  * @author Joe Mooney 
  * @author Adrien Thebo
  * @since 31-December-2008 
  */ 

class authTagLib {

    static namespace = 'auth'

    // Execute main body only if the user is logged in 
    def ifLoggedIn = {attrs, body -> 
        def session = attrs.session
        if (getUserName(session)) 
            out << body() 
    }

    // Execute main body only if the user is NOT logged in
    def ifNotLoggedIn = {attrs, body -> 
        def session = attrs.session
        if (!getUserName(session)) 
            out << body() 
    }

    // Execute main body only if the user is logged in and has one of the roles requested 
    def ifUserHasRole = {attrs, body -> 
        def session = attrs.session
        if (userHasOneOfRequiredRoles(attrs.roles.split(/,/), session))
            out << body() 
    }

    // Execute main body only if the user is logged in and has none of the roles requested 
    def ifUserHasNoRole = {attrs, body -> 
        def session = attrs.session
        if (!userHasOneOfRequiredRoles(attrs.roles.split(/,/), session)) 
            out << body() 
    }

    // Output the signed on user name (if they are logged in) 
    def loggedInUser = { attrs, body -> 
        def session = attrs.session
        def username = getUserName(session) 
        if (username)
            out << username 
        else
            out << "Not logged in"
    }

    /* 
     * Execute main body only if the user is logged in and is either an admin user (based on supplied roles) or 
     * if they were the creator of the bean (based on the supplied userName) 
     */ 
     def ifUserCanEdit = {attrs, body -> 
        def session = attrs.session
        def userIsGood = false 
        def adminRoles = attrs["adminRoles"] 
        if (adminRoles)
            userIsGood = userHasOneOfRequiredRoles(adminRoles.split(/,/), session) 
        if (!userIsGood) 
		{
            def userName = attrs["userName"] 
            def signedOnUserName = getUserName(session) 
            if (signedOnUserName && userName == signedOnUserName)
                userIsGood = true 
        } 
        if (userIsGood)
            out << body() 
    }

    // Helper method to get the user name 
    String getUserName(def session)
	{ 
        return session?.getAttribute('SPRING_SECURITY_LAST_USERNAME')
    }

    // Helper method to indicate if a user has at least one of the requested roles 
    Boolean userHasOneOfRequiredRoles(def requiredRoles, def session)
	{
        def userHasOneRole = false 
        if (getUserName(session))
		{
            requiredRoles.each {roleRequired -> 
                if (request.isUserInRole(roleRequired))
                    userHasOneRole = true 
            } 
        } 
        return userHasOneRole 
    } 
}

