/*
 * Copyright 2005-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.ws.transport.jms;

import org.custommonkey.xmlunit.XMLAssert;

import org.springframework.test.AbstractDependencyInjectionSpringContextTests;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.xml.transform.StringResult;
import org.springframework.xml.transform.StringSource;

public class JmsIntegrationTest extends AbstractDependencyInjectionSpringContextTests {

    private WebServiceTemplate webServiceTemplate;

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"classpath:org/springframework/ws/transport/jms/jms-applicationContext.xml"};
    }

    public void setWebServiceTemplate(WebServiceTemplate webServiceTemplate) {
        this.webServiceTemplate = webServiceTemplate;
    }

    @Override
    protected void onTearDown() throws Exception {
        applicationContext.close();
        setDirty();
    }

    public void testTemporaryQueue() throws Exception {
        String content = "<root xmlns='http://springframework.org/spring-ws'><child/></root>";
        StringResult result = new StringResult();
        webServiceTemplate.sendSourceAndReceiveToResult(new StringSource(content), result);
        XMLAssert.assertXMLEqual("Invalid content received", content, result.toString());
    }

    public void testPermanentQueue() throws Exception {
        String url = "jms:RequestQueue?deliveryMode=NON_PERSISTENT;replyToName=ResponseQueue";
        String content = "<root xmlns='http://springframework.org/spring-ws'><child/></root>";
        StringResult result = new StringResult();
        webServiceTemplate.sendSourceAndReceiveToResult(url, new StringSource(content), result);
        XMLAssert.assertXMLEqual("Invalid content received", content, result.toString());
    }
}
