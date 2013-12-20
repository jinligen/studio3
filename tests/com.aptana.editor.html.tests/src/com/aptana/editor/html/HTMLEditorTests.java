/**
 * Aptana Studio
 * Copyright (c) 2005-2011 by Appcelerator, Inc. All Rights Reserved.
 * Licensed under the terms of the GNU Public License (GPL) v3 (with exceptions).
 * Please see the license.html included with this distribution for details.
 * Any modifications to this file must keep this entire header intact.
 */
package com.aptana.editor.html;

import org.junit.runners.Suite.SuiteClasses;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;

@RunWith(Suite.class)
@SuiteClasses({HTMLDoctypeScannerTest.class, HTMLEditorTest.class, HTMLFoldingComputerTest.class, HTMLOpenTagCloserTest.class, HTMLScannerTest.class, HTMLSourcePartitionScannerTest.class, HTMLTagScannerTest.class, HTMLTagUtilTest.class, })
public class HTMLEditorTests
{

//	public static Test suite()
//	{
//		TestSuite suite = new TestSuite(HTMLEditorTests.class.getName())
//		{
//			@Override
//			public void runTest(Test test, TestResult result)
//			{
//				System.err.println("Running test: " + test.toString());
//				super.runTest(test, result);
//			}
//		};
//		// $JUnit-BEGIN$
//		suite.addTestSuite(HTMLDoctypeScannerTest.class);
//		suite.addTestSuite(HTMLEditorTest.class);
//		suite.addTestSuite(HTMLFoldingComputerTest.class);
//		suite.addTestSuite(HTMLOpenTagCloserTest.class);
//		suite.addTestSuite(HTMLScannerTest.class);
//		suite.addTestSuite(HTMLSourcePartitionScannerTest.class);
//		suite.addTestSuite(HTMLTagScannerTest.class);
//		suite.addTestSuite(HTMLTagUtilTest.class);
//		// $JUnit-END$
//		return suite;
//	}
//
}
