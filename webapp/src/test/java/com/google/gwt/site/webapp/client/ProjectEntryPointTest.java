/**
 * Copyright 2015 ArcBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.google.gwt.site.webapp.client;

import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.regexp.shared.RegExp;

/**
 * Test class for the GWTProject entry-point running in JVM
 */
public class ProjectEntryPointTest extends GWTTestCase {

    public String getModuleName() {
        return null;
    }

    /*
     * We need this method because GWTProjectEntryPoint.absoluteUrl cannot
     * be instantiated in the JVM
     */
    protected String getOrigin() {
        String absoluteUrl = "HTTP://AAA.COM/adfaf/adsfa.html?aa=b&c=d#aaa:aaa";

        // This is the same Regexp that we use in client class
        String origin = absoluteUrl.replaceFirst("^(\\w+://.+?)/.*", "$1").toLowerCase();
        assertEquals("http://aaa.com", origin);

        return origin;
    }

    /*
     * We need this method because GWTProjectEntryPoint.isSameOriginRexp cannot
     * be instantiated in the JVM
     */
    protected RegExp getSameOriginRegex() {
        String origin = getOrigin();
        // This is the same Regexp that we use in client class
        return RegExp.compile(
                "^" + origin + "|^(?!(#|[a-z#]+:))(?!.*(|/)javadoc/)(?!.*\\.(jpe?g|png|mpe?g|mp[34]|avi)$)",
                "i");
    }

    public void testEntryPointRegex() {
        RegExp isSameOriginRexp = getSameOriginRegex();

        assertTrue(isSameOriginRexp.test(getOrigin() + "/adfaf"));
        assertFalse(isSameOriginRexp.test(getOrigin().replace("http", "https") + "/adfaf"));
        assertFalse(isSameOriginRexp.test("file:///aaa.com/adfaf"));
        assertFalse(isSameOriginRexp.test("telnet:///aaa.com/adfaf"));
        assertFalse(isSameOriginRexp.test("ftp:///aaa.com/adfaf"));
        assertFalse(isSameOriginRexp.test("mailto:foo@bar"));
        assertTrue(isSameOriginRexp.test(".aaa"));
        assertTrue(isSameOriginRexp.test("./aaa"));
        assertTrue(isSameOriginRexp.test("/aaa"));
        assertTrue(isSameOriginRexp.test("///aaa"));
        assertFalse(isSameOriginRexp.test("#adfaf"));
        assertFalse(isSameOriginRexp.test("#"));
        assertTrue(isSameOriginRexp.test(""));
        assertFalse(isSameOriginRexp.test("/javadoc/foo/bar"));
        assertFalse(isSameOriginRexp.test("javadoc/foo/bar"));
        assertFalse(isSameOriginRexp.test("whatever/javadoc/foo/bar"));
        assertFalse(isSameOriginRexp.test("whatever/foo/bar.jpg"));
        assertFalse(isSameOriginRexp.test("whatever/foo/bar.mp4"));
        assertTrue(isSameOriginRexp.test("whatever/foo/bar.mp4foo"));
    }
}
