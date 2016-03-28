/*******************************************************************************
 * Copyright (c) 2015, 2016 David Green.
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
 *******************************************************************************/
package greensopinion.finance.services.web.model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Test;

import greensopinion.finance.services.web.model.ExceptionContent;

public class ExceptionContentTest {

	@Test
	public void simple() {
		ExceptionContent exceptionContent = new ExceptionContent(new Exception("arg"));
		assertEquals("arg", exceptionContent.getMessage());
		assertEquals("Exception", exceptionContent.getErrorCode());
	}

	@Test
	public void nested() {
		ExceptionContent exceptionContent = new ExceptionContent(new RuntimeException(new IOException("nope")));
		assertEquals("nope", exceptionContent.getMessage());
		assertEquals("RuntimeException", exceptionContent.getErrorCode());
	}

	@Test
	public void nested2() {
		ExceptionContent exceptionContent = new ExceptionContent(new RuntimeException(new IOException()));
		assertEquals("Unexpected exception: RuntimeException: IOException", exceptionContent.getMessage());
		assertEquals("RuntimeException", exceptionContent.getErrorCode());
	}
}