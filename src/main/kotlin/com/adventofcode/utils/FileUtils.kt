package com.adventofcode.utils

import java.io.File

fun readFileFromClasspath(filepath: String) =
    File(ClassLoader.getSystemResource(filepath).file)

fun splitLine(line: String, delimiter: String = " "): List<String> =
    line.split(delimiter).filter { it.isNotBlank() }.map { it.trim() }
