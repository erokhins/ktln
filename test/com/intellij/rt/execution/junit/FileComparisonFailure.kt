package com.intellij.rt.execution.junit

import junit.framework.ComparisonFailure

// this is very dirty hack to use IDEA feature for change files from exception
// this class isn't called in runtime, because real class from idea library will be going first in class path
@Suppress("UNUSED")
class FileComparisonFailure(
        message: String,
        myExpected: String,
        myActual: String,
        val filePath: String
) : ComparisonFailure(message, myExpected, myActual)

